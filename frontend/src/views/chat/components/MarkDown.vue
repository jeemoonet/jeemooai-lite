<template>
  <!-- 初始提示语 -->
  <div
    v-if="messageValue.role == 'assistant' && messageValue.isInitMessage"
    class="chat-user-view-markdown"
    :class="type == 1 ? 'chat-user-view-markdown-white' : ''"
  >
    <div class="markdown-body">
      <img class="chat-user-avatar" src="@/assets/images/logo1.png" />
      <div class="chat-init-message pdb_15">
        <div class="init-message-content" v-html="$xss(messageValue.message)"></div>
        <div class="init-message-tips">
          <div
            class="init-message-tips-item float"
            v-for="item in tipList"
            :key="item.id"
            @click.stop="onTips(item.message)"
          >
            <div class="tips-value one_line">{{ item.title }}</div>
            <i class="ico_right bg"></i>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- 系统消息 -->
  <div
    v-else-if="messageValue.role == 'assistant'"
    class="chat-user-view-markdown"
    :class="type == 1 ? 'chat-user-view-markdown-white' : ''"
  >
    <div class="markdown-body">
      <img class="chat-user-avatar" src="@/assets/images/logo1.png" />
      <div class="chat-user-message">
        <!-- 搜索信息 -->

        <search-loading
          :title="messageValue.loading_title"
          v-if="messageValue.loading && chat.needSearch"
        />
        <!-- 搜索列表 -->
        <search-info-list
          v-if="messageValue.searchInfoList && messageValue.searchInfoList.length != 0"
          :searchInfoList="messageValue.searchInfoList || []"
        />
        <!-- 深度思考 -->
        <deep-thinking
          v-if="messageValue.reasoningContent"
          :reasoningContent="messageValue.reasoningContent"
        ></deep-thinking>
        <div
          class="chat-content"
          ref="copyMessage"
          v-html="md.render(`${messageValue.message}`)"
        ></div>
        <!-- 控制器 -->
        <div class="chat-controller right" v-if="type != 1">
          <!-- <div class="del left" @click.stop="handleResetMessage(messageValue)">
            <i class="ico_reset bg"></i>
            
          </div> -->
          <div class="controller right">
            <a-tooltip>
              <template #title>删除</template>
              <a-popconfirm
                title="请确认是否删除？"
                @confirm.stop="handleDelMessage(messageValue)"
                ok-text="是"
                cancel-text="否"
              >
                <i class="ico_del bg"></i>
              </a-popconfirm>
            </a-tooltip>
            <a-tooltip>
              <template #title>复制</template>
              <i class="ico_copy bg" @click.stop="handleCopyMessage"></i>
            </a-tooltip>

            <div class="like right">
              <a-tooltip>
                <template #title>还不错</template>
                <i
                  class="ico_like bg"
                  :class="messageValue.isLike == 1 ? 'active' : ''"
                  @click.stop="handleLikeMessage(messageValue, true)"
                ></i>
              </a-tooltip>
              <a-tooltip>
                <template #title>不喜欢</template>
                <i
                  class="ico_unlike bg"
                  :class="messageValue.isLike == 2 ? 'active' : ''"
                  @click.stop="handleLikeMessage(messageValue, false)"
                ></i>
              </a-tooltip>
            </div>
          </div>
        </div>
        <div
          class="chat-docInfo left"
          v-if="messageValue.docInfo && messageValue.docInfo.length != 0"
        >
          <div class="chat-docInfo-title left"><i class="ico_doc bg"></i> 参考资料:</div>
          <div class="chat-docInfo-list">
            <div class="item" v-for="(item, index) in messageValue.docInfo" :key="index">
              <div v-if="!item.url">{{ item.title }}</div>
              <a class="link" target="_blank" :href="item.url" v-else>{{
                item.title || item.url
              }}</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- 用户消息 -->
  <div v-else class="chat-user-view">
    <div class="chat-user-content">
      <img class="chat-user-avatar" :src="avatar()" />
      <div class="chat-user-info">
        <div class="chat-user-message" v-html="$xss(messageValue.message)"></div>
        <div class="chat-user-imgs left">
          <div
            class="item float"
            v-for="(item, index) in messageValue.fileInfo"
            :key="index"
          >
            <a-image :src="item.icon" v-if="item.fileType == 'image'" />
            <img
              v-else
              class="img"
              :src="
                item.fileType == 'voice'
                  ? iconVoice
                  : item.fileType == 'pdf'
                  ? iconPdf
                  : item.fileType == 'docx' || item.fileType == 'doc'
                  ? iconDocx
                  : iconDocx
              "
            />
            <div class="info">
              <div class="picture_title one_line">{{ item.fileName }}</div>
              <div class="picture_size">
                {{ fileSize(item.fileSize) }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { message } from "ant-design-vue";
import { DeleteOutlined } from "@ant-design/icons-vue";
import noneLogo from "@/assets/images/logo1.png";
import iconVoice from "./images/ico_voice.png";
import iconPdf from "./images/ico_pdf.png";
import iconDocx from "./images/ico_doc.png";
import iconImg from "./images/ico_img.png";
import {
  ref,
  reactive,
  computed,
  watch,
  defineProps,
  defineEmits,
  onMounted,
  h,
  createApp,
  inject,
  nextTick,
} from "vue";
import { useCitationTooltipController } from "./citationTooltip";
import searchLoading from "@/components/chat/loading.vue";
import searchInfoList from "@/components/chat/searchInfoList.vue";
import deepThinking from "@/components/chat/deepThinking.vue";
import { storeToRefs } from "pinia";
import { ChatStore } from "@/stores/chat";
import { InfoStore } from "@/stores/user";
import $xss from "xss";
import markdownIt from "markdown-it";
import EchartsPlugin from "@/plugin/md-echart";
import SearchNamePlugin from "@/plugin/md-search-name";
import hljs from "highlight.js";
import "@/assets/css/markdown-light.css";
//获取store slider数据
const { getUserInfo } = storeToRefs(InfoStore());
const { getCurrentWindow } = storeToRefs(ChatStore());
const axios = inject("axios");
const chat = inject("chat");
const tipList = ref([]);
const props = defineProps({
  messageValue: {
    type: Object,
    default: () => {},
  },
  type: {
    type: String,
    default: () => 0,
  },
});

const emit = defineEmits(["submitMessage", "update:chatMessage"]);

const md = markdownIt({
  html: true, // 在源码中启用 HTML 标签
  xhtmlOut: false, // 使用 '/' 来闭合单标签 （比如 <br />）。
  // 这个选项只对完全的 CommonMark 模式兼容。
  breaks: true, // 转换段落里的 '\n' 到 <br>。
  langPrefix: "language-", // 给围栏代码块的 CSS 语言前缀。对于额外的高亮代码非常有用。
  linkify: false, // 将类似 URL 的文本自动转换为链接。
  typographer: false,
  quotes: "“”‘’",
  highlight: function (str, lang) {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return (
          '<pre class="hljs" style="padding:20px;margin:20px 0"><code>' +
          hljs.highlight(str, { language: lang, ignoreIllegals: true }).value +
          "</code></pre>"
        );
      } catch (__) {}
    }
    if (lang == "mermaid") {
      try {
        return `<div class="mermaid">${str}</div>`;
      } catch {
        return `<pre>${str}</pre>`;
      }
    }

    return (
      '<pre class="hljs" style="padding:20px;margin:20px 0"><code>' +
      md.utils.escapeHtml(str) +
      "</code></pre>"
    );
  },
  searchInfoList: props.messageValue?.searchInfoList || [],
});
md.use(EchartsPlugin);
// md.use(SearchPlugin);
md.use(SearchNamePlugin);
let htmlStr = ref();
let copyMessage = ref(null);

//deepseek 搜索引用预览
props.messageValue.role == "assistant" && useCitationTooltipController();

onMounted(() => {
  if (props.messageValue.isInitMessage) {
    updateTipData();
  }
});

const onTips = (res) => {
  emit("update:chatMessage", res || "");
};

const handleCopyMessage = () => {
  let copyMessageChildren = copyMessage.value.children || [];
  let contents = [];
  if (!copyMessageChildren && copyMessageChildren.length <= 0) {
    return;
  }
  for (var i = 0; i < copyMessageChildren.length; i++) {
    let child = copyMessageChildren[i];
    if (child.nodeType === 1) {
      contents.push(child.textContent + "\r\n");
    }
  }
  let contentStringVal = contents.join("");
  const input = document.createElement("textarea");
  input.value = contentStringVal || "";
  document.body.appendChild(input);
  input.select();
  document.execCommand("Copy");
  document.body.removeChild(input);
  message.success("复制成功");
};

const handleResetMessage = (item = {}) => {
  emit("reset", item);
};

const handleDelMessage = (item = {}) => {
  emit("del", item);
};

const handleLikeMessage = (item = {}, isLike = false) => {
  if (isLike && item.isLike != 1) {
    emit("like", item);
  }
  if (!isLike && item.isLike != 2) {
    emit("unlike", item);
  }
};

const updateTipData = async () => {
  tipList.value = [];
  if (!getCurrentWindow.value.promptId) return;
  try {
    let res = await axios.get(`/api/prompt/tips`, {
      params: {
        promptId: getCurrentWindow.value.promptId,
      },
    });
    let data = res.data.data || [];
    tipList.value = data || [];
  } catch (err) {}
};

const avatar = computed(() => {
  return () => {
    if (props.type == 1) {
      return props.messageValue.avatar || noneLogo;
    }
    return getUserInfo.value.avatar || noneLogo;
  };
});

const fileSize = computed(() => {
  return function (size) {
    if (!size) return "0KB";
    let num = 1024.0;
    if (size < num) return size + "B";
    if (size < Math.pow(num, 2)) return (size / num).toFixed(1) + "K";
    if (size < Math.pow(num, 3)) return (size / Math.pow(num, 2)).toFixed(1) + "M";
    if (size < Math.pow(num, 4)) return (size / Math.pow(num, 3)).toFixed(2) + "G";
    return (size / Math.pow(num, 4)).toFixed(2) + "T";
  };
});

watch(
  props.messageValue,
  (newVal, oldVal) => {
    if (
      newVal &&
      newVal?.searchInfoList &&
      (!md?.options?.searchInfoList || md?.options?.searchInfoList.length == 0)
    ) {
      md.options.searchInfoList = newVal?.searchInfoList;
    }
  },
  { deep: true, immediate: true }
);
</script>
<style scoped lang="less">
audio {
  display: block !important;
}
.chat-user-view {
  width: 100%;
  &.chat-user-view-markdown {
  }
}
.chat-user-view-markdown {
  width: 100%;
}
.chat-user-content {
  display: flex;
  align-items: flex-start;
  justify-content: flex-start;
  flex-wrap: wrap;
  width: 852px;
  margin: 0 auto 15px;
  white-space: pre-wrap;
  position: relative;
  .chat-user-avatar {
    display: block;
    width: 38px;
    height: 38px;
    border-radius: 50%;
    // background-color: black;
  }
  .chat-user-info {
    flex: 1;
    width: 100%;
    min-width: 0;
    margin-left: 14px;
    // overflow: hidden;
    margin-top: 7px;
  }
  .chat-user-message {
    flex: 1;
    width: 100%;
    overflow: hidden;
    word-wrap: break-word;
    word-break: break-all;
    color: #333333;
    font-weight: 500;
    font-size: 16px;
    line-height: 23px;
  }
  .chat-user-imgs {
    flex: 1;
    width: 100%;
    min-width: 0;
    margin-top: 18px;

    .item {
      width: 148px;
      height: 48px;
      background: #fff;
      border-radius: 3px;
      padding: 4px 6px 4px 4px;
      cursor: pointer;
      margin-right: 8px;
      transition: all 0.2s;
      &:hover {
        box-shadow: 0 1px 2px -2px rgba(0, 0, 0, 0.16), 0 3px 6px 0 rgba(0, 0, 0, 0.12),
          0 5px 12px 4px rgba(0, 0, 0, 0.09);
      }
      &:nth-last-of-type(1) {
        margin-right: 0;
      }
      :deep(.ant-image-img) {
        width: 40px;
        height: 40px;
        border-radius: 3px;
      }
      .img {
        width: 40px;
        height: 40px;
        border-radius: 3px;
      }
      .info {
        flex: 1;
        min-width: 0;
        padding-left: 8px;
        .picture_title {
          width: 100%;
          font-weight: 400;
          font-size: 14px;
          color: #333333;
          line-height: 20px;
          text-align: left;
          font-style: normal;
          margin-bottom: 1px;
        }
        .picture_size {
          font-weight: 400;
          font-size: 12px;
          color: #9599a5;
          line-height: 17px;
          text-align: left;
          font-style: normal;
          .close {
            width: 12px;
            height: 12px;
          }
        }
      }
    }
  }
}
.markdown-body {
  display: flex;
  align-items: flex-start;
  justify-content: flex-start;
  flex-wrap: wrap;
  width: 852px;
  min-height: 60px;
  margin: 0 auto 26px;
  background-color: transparent;
  position: relative;
  .chat-user-avatar {
    display: block;
    width: 38px;
    height: 38px;
    border-radius: 50%;
    background-color: transparent;
  }
  .chat-user-message,
  .chat-init-message {
    flex: 1;
    width: 100%;
    margin-left: 14px;
    overflow: hidden;
    word-wrap: break-word;
    word-break: break-all;
    background-color: #fff;
    border-radius: 6px;
    padding: 20px 0 0;
    .chat-content {
      padding-left: 25px;
      padding-right: 18px;
    }
    .chat-controller {
      width: 100%;
      margin-top: 20px;
      padding-bottom: 14px;
      padding-left: 25px;
      padding-right: 18px;
      .reset {
        font-size: 14px;
        font-weight: 400;
        color: #9599a5;
        line-height: 24px;
        cursor: pointer;

        .ico_reset {
          display: block;
          width: 18px;
          height: 18px;
          margin-right: 4px;
          background-image: url(../../../assets/images/ico_reset.png);
        }
      }
      .controller {
        .ico_del {
          display: block;
          width: 18px;
          height: 18px;
          background-image: url(../../../assets/images/ico_del.png);
          cursor: pointer;
          margin-right: 14px;
        }
        .ico_copy {
          display: block;
          width: 18px;
          height: 18px;
          background-image: url(../../../assets/images/ico_copy.png);
          cursor: pointer;
        }
        .like {
          margin-left: 15px;
          padding-left: 14px;
          position: relative;
          border-left: 1px solid #e8e8e9;
          .ico_like {
            display: block;
            width: 18px;
            height: 18px;
            background-image: url(../../../assets/images/ico_like.png);
            margin-right: 14px;
            cursor: pointer;
            &.active {
              background-image: url(../../../assets/images/ico_like_active.png);
            }
          }
          .ico_unlike {
            display: block;
            width: 18px;
            height: 18px;
            background-image: url(../../../assets/images/ico_like.png);
            transform: rotate(180deg);
            cursor: pointer;
            margin-top: 2px;
            &.active {
              background-image: url(../../../assets/images/ico_like_active.png);
            }
          }
        }
      }
    }
    .chat-docInfo {
      width: 100%;
      height: 38px;
      background-color: rgba(30, 37, 56, 0.1);
      padding-left: 25px;
      padding-right: 18px;
      font-size: 14px;
      font-weight: 400;
      color: #333333;
      line-height: 24px;
      .chat-docInfo-title {
        flex: 0 0 auto;
        padding-right: 10px;
        .ico_doc {
          display: block;
          width: 18px;
          height: 18px;
          margin-right: 2px;
        }
      }

      .chat-docInfo-list {
        display: flex;
        align-items: center;
        justify-content: flex-start;
        max-width: calc(100% - 100px);
        flex: auto;
        overflow-x: auto;
        .item {
          flex-shrink: 0;
          border-right: 1px solid #333;
          padding-right: 4px;
          margin-right: 4px;
          line-height: 1;
          &:nth-last-of-type(1) {
            border: none;
          }
          .link {
            color: #5657f6;
            text-decoration: underline;
          }
        }
      }
    }
  }
  .chat-init-message {
    padding-left: 25px;
    padding-right: 18px;
    .init-message-tips {
      display: flex;
      justify-content: space-between;
      flex-wrap: wrap;
      margin-top: 18px;
      .init-message-tips-item {
        width: 375px;
        height: 40px;
        background-color: #f5f6f8;
        padding: 0 10px 0 12px;
        margin-bottom: 8px;
        cursor: pointer;
        transition: all linear 0.2s;
        &:hover {
          box-shadow: 0 3px 8px rgba(0, 0, 0, 0.2);
        }
        .tips-value {
          flex: 1;
          font-weight: 500;
          font-size: 14px;
          color: #333333;
        }
        .ico_right {
          width: 17px;
          height: 17px;
          background-image: url(../../../assets/images/ico_right1.png);
        }
      }
    }
  }
}
.pdb_15 {
  padding-bottom: 15px !important;
}
.markdown-body .highlight pre,
.markdown-body pre {
  background-color: #000000 !important;
}

.chat-user-view-markdown-white {
  .markdown-body {
    .chat-user-message,
    .chat-init-message {
      background-color: #f5f6f8;
    }
  }
}
</style>

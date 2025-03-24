<template>
  <!-- 系统消息 -->
  <div
    v-if="messageValue.role == 'assistant' && !messageValue.isInitMessage"
    class="chat-user-view-markdown"
  >
    <div class="markdown-body">
      <img :src="prompt.promptIcon" class="chat-user-avatar" alt="" />
      <div class="chat-user-message">
        <!-- 搜索信息 -->
        <search-loading
          :title="messageValue.loading_title"
          v-if="messageValue.loading && chat.needSearch"
        />
        <!-- 深度思考 -->
        <deep-thinking
          v-if="messageValue.reasoningContent"
          :reasoningContent="messageValue.reasoningContent"
        ></deep-thinking>
        <!-- 内容 -->
        <div
          class="chat-content"
          ref="copyMessage"
          v-html="md.render(`${messageValue.message}`)"
        ></div>
      </div>
      <!-- 控制器 -->
      <div class="chat-controller float">
        <div class="reset left">
          {{ messageValue.isShowChatLoading ? "正在回答中..." : "" }}
        </div>
        <div class="controller right">
          <i class="ico_copy bg" @click.stop="handleCopyMessage"></i>
        </div>
      </div>
    </div>
  </div>
  <!-- 用户消息 -->
  <div v-if="messageValue.role == 'user'" class="chat-user-view">
    <div class="chat-user-content">
      <div class="chat-user-message" v-html="$xss(messageValue.message)"></div>
    </div>
    <img :src="avatar()" class="chat-user-avatar" alt="" />
  </div>
</template>
<script setup>
import { message } from "ant-design-vue";
import { DeleteOutlined } from "@ant-design/icons-vue";
import noneLogo from "@/assets/images/logo1.png";
import "@/assets/css/markdown-light.css";
import {
  ref,
  reactive,
  computed,
  watch,
  defineProps,
  defineEmits,
  onMounted,
  inject,
} from "vue";
import searchLoading from "@/components/chat/loading.vue";
import deepThinking from "@/components/chat/deepThinking.vue";
import { storeToRefs } from "pinia";
import { InfoStore } from "@/stores/user";
import $xss from "xss";
import markdownIt from "markdown-it";
import EchartsPlugin from "@/plugin/md-echart";
import hljs from "highlight.js";
const { getUserInfo } = storeToRefs(InfoStore());
const props = defineProps({
  messageValue: {
    type: Object,
    default: () => {},
  },
  tips: {
    type: Array,
    default: () => [],
  },
  prompt: {
    type: Object,
    default: {},
  },
});
const emit = defineEmits(["submitMessage"]);
const md = markdownIt({
  html: true, // 在源码中启用 HTML 标签
  xhtmlOut: false, // 使用 '/' 来闭合单标签 （比如 <br />）。
  // 这个选项只对完全的 CommonMark 模式兼容。
  breaks: true, // 转换段落里的 '\n' 到 <br>。
  langPrefix: "language-", // 给围栏代码块的 CSS 语言前缀。对于额外的高亮代码非常有用。
  linkify: false, // 将类似 URL 的文本自动转换为链接。

  // 启用一些语言中立的替换 + 引号美化
  typographer: false,

  // 双 + 单引号替换对，当 typographer 启用时。
  // 或者智能引号等，可以是 String 或 Array。
  //
  // 比方说，你可以支持 '«»„“' 给俄罗斯人使用， '„“‚‘'  给德国人使用。
  // 还有 ['«\xA0', '\xA0»', '‹\xA0', '\xA0›'] 给法国人使用（包括 nbsp）。
  quotes: "“”‘’",

  // 高亮函数，会返回转义的HTML。
  // 或 '' 如果源字符串未更改，则应在外部进行转义。
  // 如果结果以 <pre ... 开头，内部包装器则会跳过。
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
});
md.use(EchartsPlugin);
const chat = inject("chat");
let htmlStr = ref();
let copyMessage = ref(null);

onMounted(() => {});

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

const onTips = (item) => {
  chat.message = item.message;
  emit("submitMessage");
};

const handleResetMessage = (item = {}) => {
  emit("reset", item);
};

const avatar = computed(() => {
  return () => {
    return getUserInfo.value.avatar || noneLogo;
  };
});
</script>
<style scoped lang="less">
audio {
  display: block !important;
}
.chat-user-view {
  width: 100%;
  display: flex;
  align-items: flex-start;
  justify-content: flex-end;
  overflow-anchor: auto;
  margin-bottom: 20px;
  .chat-user-avatar {
    display: block;
    width: 39px;
    height: 39px;
    border-radius: 50%;
    background-color: transparent;
  }
}
.chat-user-view-markdown {
  width: 100%;
  overflow-anchor: auto;
}
.chat-user-content {
  width: fit-content;
  white-space: pre-wrap;
  position: relative;
  background-color: #5657f6;
  border-radius: 10px 2px 10px 10px;
  padding: 12px 22px;
  margin-right: 8px;
  .chat-user-message {
    overflow: hidden;
    word-wrap: break-word;
    word-break: break-all;
    color: #fafafa;
    font-weight: 400;
    font-size: 16px;
    line-height: 22px;
  }
}
.markdown-body {
  display: flex;
  align-items: flex-start;
  justify-content: flex-start;
  flex-wrap: wrap;
  width: 100%;
  min-height: 52px;
  margin: 0 auto 20px;
  background-color: transparent;
  position: relative;
  .chat-user-avatar {
    display: block;
    width: 39px;
    height: 39px;
    border-radius: 50%;
    background-color: transparent;
  }
  .chat-user-message,
  .chat-init-message {
    width: calc(100% - 47px);
    margin-left: 8px;
    overflow: hidden;
    word-wrap: break-word;
    word-break: break-all;
    background-color: #fafafa;
    // border-radius: 10px;
    border-radius: 2px 10px 10px 10px;
    padding: 12px 0 16px;
    font-size: 16px;
    color: #000;
    .chat-content {
      padding-left: 25px;
      padding-right: 18px;
    }
  }
  .chat-init-message {
    width: 100%;
    margin-left: 0;
    padding-left: 20px;
    padding-right: 20px;
  }
  .tips-item {
    width: 100%;
    height: 39px;
    background-color: #f5f6f8;
    border-radius: 5px;
    margin-bottom: 5px;
    margin-top: 15px;
    padding-left: 13px;
    padding-right: 8px;
    &:nth-last-of-type(1) {
      margin-bottom: 0;
    }
    &:nth-of-type(1) {
      margin-top: 0;
    }
    .title {
      width: calc(100% - 24px);
      font-size: 14px;
      font-weight: 500;
      color: #000000;
      line-height: 39px;
    }
    .ico_right {
      width: 16px;
      height: 16px;
      background-image: url(../images/ico_right.png);
    }
  }
  .chat-controller {
    width: 100%;
    margin-top: 10px;
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
        background-image: url(../images/ico_reset.png);
      }
    }
    .controller {
      .ico_copy {
        display: block;
        width: 18px;
        height: 18px;
        background-image: url(../images/ico_copy.png);
        cursor: pointer;
      }
    }
  }
}
</style>

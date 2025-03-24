<template>
  <!-- 系统消息 -->
  <div class="chat-user-view-markdown">
    <div class="header float">
      <div class="info left">
        <img :src="messageValue.icon" alt="" />
        <p class="one_line">{{ messageValue.modelName }}</p>
      </div>
      <div class="controller right">
        <a-tooltip>
          <template #title>复制</template>
          <i class="ico_copy bg" @click.stop="handleCopyMessage"></i>
        </a-tooltip>
        <a-tooltip>
          <template #title>放大</template>
          <i class="ico_enlarge bg" @click.stop="onOpenEnlargeModal"></i>
        </a-tooltip>
      </div>
    </div>
    <div class="markdown-main">
      <!-- 深度思考 -->
      <deep-thinking
        v-if="messageValue.reasoningContent"
        :reasoningContent="messageValue.reasoningContent"
      ></deep-thinking>
      <div
        class="markdown-body"
        ref="copyMessage"
        v-html="md.render(`${messageValue.message}`)"
      ></div>
    </div>
  </div>
</template>
<script setup>
import { message } from "ant-design-vue";
import noneLogo from "@/assets/images/logo1.png";
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
} from "vue";
import { storeToRefs } from "pinia";
import { InfoStore } from "@/stores/user";
import deepThinking from "@/components/chat/deepThinking.vue";
import $xss from "xss";
import markdownIt from "markdown-it";
import EchartsPlugin from "@/plugin/md-echart";
import hljs from "highlight.js";
import "@/assets/css/markdown-light.css";
//获取store slider数据
const { getUserInfo } = storeToRefs(InfoStore());
const axios = inject("axios");
const enlargeVisible = inject("enlargeVisible");
const enlargeData = inject("enlargeData");
const tipList = ref([]);
let copyMessage = ref(null);

const props = defineProps({
  messageValue: {
    type: Object,
    default: () => {},
  },
});

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
});
md.use(EchartsPlugin);
let htmlStr = ref();

onMounted(() => {});

const onOpenEnlargeModal = () => {
  enlargeData.value = {
    ...props.messageValue,
  };
  enlargeVisible.value = true;
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

const avatar = computed(() => {
  return () => {
    if (props.type == 1) {
      return props.messageValue.avatar || noneLogo;
    }
    return getUserInfo.value.avatar || noneLogo;
  };
});
</script>
<style scoped lang="less">
.chat-user-view {
  width: 100%;
}
.chat-user-view-markdown {
  //   width: 31%;
  height: 324px;
  background: #fff;
  border-radius: 16px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
  width: calc(33.33% - 14px);
  margin-right: 20px;
  .header {
    flex: 0 0 auto;
    p {
      flex: 1;
      min-width: 0;
      font-size: 16px;
      font-weight: 500;
      line-height: 24px;
      color: #1d2129;
    }
    img {
      display: block;
      width: 36px;
      height: 36px;
      margin-right: 6px;
      border-radius: 50%;
    }
    .controller {
      .ico_copy {
        display: block;
        width: 18px;
        height: 18px;
        background-image: url(../../../../assets/images/ico_copy.png);
        cursor: pointer;
        margin-right: 8px;
      }
      .ico_enlarge {
        display: block;
        width: 16px;
        height: 16px;
        background-image: url(../../../../assets/images/ico_enlarge.png);
        cursor: pointer;
      }
    }
  }
}
.markdown-main {
  min-height: auto;
  flex: 1;
  background: #fafafa;
  border-radius: 8px;
  overflow-y: auto;
  margin-top: 12px;
  padding-top: 20px;
  &::-webkit-scrollbar {
    display: block;
    width: 6px;
    height: 4px;
  }
  &::-webkit-scrollbar-thumb {
    display: block;
    background: #ebeef5;
    border-radius: 10px;
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  }
  &::-webkit-scrollbar-track {
    display: block;
    background: #fff;
    border-radius: 10;
    box-shadow: inset 0 0 5px rgba(205, 203, 203, 0.2);
  }
}
.markdown-body {
  padding: 0 18px 12px 25px;
  background: #fafafa;
}

.markdown-body .highlight pre,
.markdown-body pre {
  background-color: #000000 !important;
}
</style>

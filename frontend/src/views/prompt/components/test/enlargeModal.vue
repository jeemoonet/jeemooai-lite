<template>
  <a-modal
    :visible="visible"
    width="80%"
    height="80%"
    :title="enlargeData.modelName"
    :closable="true"
    :footer="null"
    @cancel="closeModal"
    wrapClassName="enlargeDialog"
  >
    <div class="markdown-main">
      <!-- 深度思考 -->
      <deep-thinking
        v-if="enlargeData.reasoningContent"
        :reasoningContent="enlargeData.reasoningContent"
      ></deep-thinking>
      <div class="markdown-body" v-html="md.render(`${enlargeData.message}`)"></div>
    </div>
  </a-modal>
</template>
<script setup>
import { message } from "ant-design-vue";
import { ref, reactive, defineProps, defineEmits, watch, inject } from "vue";
import deepThinking from "@/components/chat/deepThinking.vue";
import markdownIt from "markdown-it";
import EchartsPlugin from "@/plugin/md-echart";
import hljs from "highlight.js";
import "@/assets/css/markdown-light.css";
const emit = defineEmits(["update:visible"]);
const props = defineProps({
  visible: {
    type: Boolean,
    default: () => false,
  },
  enlargeData: {
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

const closeModal = () => {
  emit("update:visible", false);
};
</script>
<style lang="less">
.enlargeDialog {
  .ant-modal-content {
    height: 100%;
    .ant-modal-body {
      height: calc(100% - 55px);
      overflow-y: auto;
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
  }
}
</style>
<style lang="less" scoped>
.modal_content {
  height: 322px;
  overflow-y: auto;
  font-size: 16px;
  font-weight: 400;
  color: #333333;
  line-height: 23px;
}

.markdown-main {
}
.markdown-body {
  padding: 0 18px 0 25px;
}
</style>

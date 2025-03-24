<template>
  <!-- 初始提示语 -->
  <div class="chat-user-view-markdown" v-if="prompt.initMessage">
    <div class="markdown-body">
      <div class="chat-init-message pdb_15">
        <div v-html="$xss(prompt.initMessage)"></div>
        <div
          class="tips-item float"
          @click="onTips(item)"
          v-for="(item, index) in prompt.tips"
          :key="index"
        >
          <div class="title">{{ item.title }}</div>
          <i class="ico_right bg"></i>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { message } from "ant-design-vue";
import { DeleteOutlined } from "@ant-design/icons-vue";
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
import { storeToRefs } from "pinia";
import { InfoStore } from "@/stores/user";
import $xss from "xss";

const props = defineProps({
  prompt: {
    type: Object,
    default: {},
  },
});
const emit = defineEmits(["submitMessage"]);

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

const handleLikeMessage = (item = {}, isLike = false) => {
  if (isLike && item.isLike != 1) {
    emit("like", item);
  }
  if (!isLike && item.isLike != 2) {
    emit("unlike", item);
  }
};
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
        background-image: url(../../../assets/images/ico_reset.png);
      }
    }
    .controller {
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
}
</style>

<template>
  <div class="preview_prompt">
    <div class="init_prompt_header float">
      <h3 class="title">预览与调试</h3>
      <!-- <div v-if="form.promptType == 1" class="auto right" @click="debugVisible = true">
        <i class="ico_preview bg"></i>
        运行日志
      </div> -->
    </div>
    <div class="layout">
      <div class="layout-content" ref="scrollDom" @scroll="scrollToTop">
        <init-message :prompt="form" />
        <MarkDown
          v-for="(item, index) in chatList"
          :key="index"
          :message-value="item"
          :prompt="form"
          v-model:chatMessage="chat.message"
          @submitMessage="submitMessage"
          @like="likeMessage"
          @unlike="unlikeMessage"
        ></MarkDown>
      </div>
      <div class="layout-footer-fixed">
        <send-message @submitMessage="submitMessage" />
      </div>
    </div>
  </div>
</template>
<script setup>
import { message, Modal } from "ant-design-vue";
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import {
  ref,
  onMounted,
  inject,
  nextTick,
  provide,
  reactive,
  watch,
  onBeforeUnmount,
} from "vue";
import setting from "@/utils/setting";
import SendMessage from "./preview/SendMessage.vue";
import InitMessage from "./preview/InitMessage.vue";
import MarkDown from "./preview/MarkDown.vue";
import rate from "./preview/Rate.vue";
const axios = inject("axios");
const form = inject("form");
const windowId = inject("windowId");
const debugVisible = inject("debugVisible");
//0 文字 1音频
const sendMessageStatus = ref(0);
const chat = reactive({
  type: "chat",
  message: "",
  loading: false,
  stop: false,
  fileLoading: false,
  needSearch: false,
});
//滚动dom实例
const scrollDom = ref(null);
//打分实例
const rateRef = ref(null);
const chatList = ref([]);
const pagination = reactive({
  page: 1,
  size: 40,
  timestamp: 0,
  loading: false,
  lock: false,
});
provide("chat", chat);

onMounted(() => {});

const submitMessage = async (sendmessage = "") => {
  chat.stop = true;
  try {
    if (chat.loading) return;
    if (chat.message.trim().length == 0 && !sendmessage) {
      return;
    }
    chat.loading = true;
    await axios.post("/api/prompt/update", {
      ...form.value,
      isPublic: form.value.isPublic ? 1 : 0,
      isRecommend: form.value.isRecommend ? 1 : 0,
      isNew: form.value.isNew ? 1 : 0,
      isContext: form.value.isContext ? 1 : 0,
      isRecommendQuestions: form.value.isRecommendQuestions ? 1 : 0,
      longHistory: form.value.longHistory ? 1 : 0,
    });
    let res = await axios.post("/api/chat/sendMessage", {
      message: sendmessage || `${chat.message}`,
      windowId: windowId.value || null,
      filesUuid: [],
    });

    let chatMenuData = res.data.data || {};
    // 联网搜索开关
    chat.needSearch = chatMenuData.needSearch || false;
    //新增问题数据
    chatList.value.push({
      messageId: chatMenuData.messageId,
      message: sendmessage || chat.message,
      role: "user",
      masterUuid: chatMenuData.masterUuid,
    });
    //文字返回
    EventSoureFn();
  } catch (err) {
    chat.loading = false;
  } finally {
    chat.stop = false;
  }
};

//文字eventsouce
const EventSoureFn = () => {
  if (typeof EventSource !== "undefined") {
    const sourc = new EventSource(
      `${
        import.meta.env.VITE_API_BASE_URL
      }/api/chat/stream?Authorization=${setting.takeToken()}`,
      {
        withCredentials: true,
        headers: { Authorization: `${setting.takeToken()}` },
      }
    );
    sourc.timeout = 120000;
    sourc.onopen = (e) => {
      chatList.value.push({ message: "", role: "assistant", loading: true });
    };
    sourc.onmessage = (event) => {
      let data = eval("(" + event.data + ")");
      console.log(data);
      let talkMessage = chatList.value[chatList.value.length - 1].message || "";
      if (data.finish == "length") {
        chatList.value[chatList.value.length - 1]["loading"] = false;
        chat.message = "";
        chat.loading = false;
        chat.stop = false;
        sourc.close();
        scrollToBottom();
        return;
      }
      if (data.finish || chat.stop) {
        chatList.value[chatList.value.length - 1]["loading"] = false;
        chat.message = "";
        chat.loading = false;
        chat.stop = false;
        sourc.close();

        setTimeout(() => {
          scrollToBottom();
        }, 300);
        return;
      }

      if (!data.content && data.reasoningContent) {
        let reasoningContent =
          chatList.value[chatList.value.length - 1].reasoningContent || "";
        reasoningContent += data.reasoningContent;
        chatList.value[chatList.value.length - 1]["reasoningContent"] = reasoningContent;

        scrollToBottom();
        return;
      }

      if (data.content.indexOf("!!###loading") != -1) {
        chat.needSearch = true;
        const regex = /^!!###loading###(.*?)!!$/;
        const match = data.content.match(regex);
        if (match && match[1]) {
          const dynamicContent = match[1];
          chatList.value[chatList.value.length - 1]["loading_title"] = dynamicContent;
        } else {
          console.log("未找到动态内容");
        }
        scrollToBottom();
        return;
      }

      if (data.hasOwnProperty("content")) {
        if (talkMessage == "" && data.content) {
          talkMessage = data.content.replace(/^\n+/, ""); //去掉回复消息中偶尔开头就存在的连续换行符
        } else {
          talkMessage += data.content;
        }
      }
      talkMessage = talkMessage.replace(/\\n/g, "\n");
      chatList.value[chatList.value.length - 1] = {
        messageId: data.messageId,
        masterUuid: data.masterUuid,
        message: talkMessage,
        role: "assistant",
        docInfo: data.docInfo || [],
        loading: true,
        reasoningContent:
          chatList.value[chatList.value.length - 1].reasoningContent || "",
      };
      scrollToBottom();
    };
    sourc.onerror = (e) => {
      console.log(e, "onerror");
      chatList.value[chatList.value.length - 1]["loading"] = false;
      chat.message = "";
      chat.loading = false;
      chat.stop = false;

      sourc.close();
      if (e?.target?.readyState === 2) {
        message.error("请求超时");
      } else {
        message.error("请求失败,请稍后重试");
      }
    };
  } else {
    message.error("当前浏览器不支持服务器发送的事件");
  }
};

//获取窗口数据
const getHistoryMessageList = async ({ chatMenuId }) => {
  let oScrollHeight = scrollDom.value.scrollHeight;

  if (pagination.lock || pagination.loading) return;
  if (!chatMenuId) {
    chatList.value = [];
    return;
  }
  try {
    pagination.loading = true;
    let res = await axios.get("/api/chat/history", {
      params: {
        windowId: chatMenuId,
        pageNum: pagination.page || 1,
        pageSize: pagination.size,
        timestamp: pagination.timestamp || 0,
      },
    });
    let data = res.data.rows || [];
    data = data.reverse();
    if (
      data.length < pagination.size ||
      pagination.size * pagination.page >= res.data.total
    ) {
      pagination.lock = true;
    }
    if (data.length > 0) {
      if (pagination.page <= 1) {
        pagination.timestamp = data[data.length - 1]["timestamp"];
      }
      pagination.page += 1;
      chatList.value.unshift(...data);

      if (pagination.page <= 2) {
        nextTick(() => scrollToBottom());
      } else {
        nextTick(() => {
          scrollDom.value.scrollTo({
            left: 0,
            top: nScrollHeight - oScrollHeight,
          });
        });
      }

      setTimeout(() => {
        pagination.loading = false;
      }, 1000);
    }
  } catch (err) {
    pagination.loading = false;
  } finally {
  }
};

const scrollToTop = () => {
  let scrollTop = scrollDom.value.scrollTop;
  if (scrollTop <= 90) {
    getHistoryMessageList({ chatMenuId: windowId.value });
  }
};

//滚动底部
const scrollToBottom = () => {
  scrollDom.value.scrollTo({
    left: 0,
    top: scrollDom.value.scrollHeight,
  });
  // scrollDom.value.scrollTop = scrollDom.value.scrollHeight;
};

onBeforeUnmount(() => {
  chat.stop = true;
});

watch(
  () => windowId.value,
  (newVal, oldVal) => {
    if (newVal && newVal != oldVal) {
      getHistoryMessageList({
        chatMenuId: newVal,
      });
    }
  }
);
</script>
<style scoped lang="less">
.preview_prompt {
  display: flex;
  flex-direction: column;
  //   width: calc(33.33% - 10px);
  flex: 1;
  min-width: 0;
  height: 100%;
  position: relative;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;

  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.04);
  .init_prompt_header {
    width: 100%;
    height: 46px;
    padding: 0 15px 0 22px;
    border-bottom: 1px solid #fafafa;

    .title {
      font-size: 16px;
      font-weight: 600;
      line-height: normal;
      color: #333333;
      margin: 0;
    }
    .auto {
      font-size: 14px;
      font-weight: normal;
      line-height: normal;
      color: #1d2129;
      cursor: pointer;
      .ico_preview {
        width: 16px;
        height: 16px;
        background-image: url(./images/preview.png);
        margin-right: 5px;
      }
    }
  }
}
.layout {
  display: flex;
  flex: auto;
  flex-direction: column;
  width: 100%;
  flex: 1;
  min-height: 0;
  margin: 0 auto;
  position: relative;

  .layout-content {
    width: 100%;
    flex: 1;
    min-height: 0;
    padding: 15px 15px 0px;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;
    overflow-anchor: none;
  }

  .layout-footer-fixed {
    width: 100%;

    padding: 8px 15px 12px;
    display: flex;
    align-items: center;
    justify-content: flex-start;
  }
}
</style>

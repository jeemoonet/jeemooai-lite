<template>
  <div class="chatPage headerMenuPadding left">
    <common-menu />
    <div class="layout" ref="scrollDom">
      <div class="layout-header">
        <layout-header />
      </div>
      <div class="layout-content">
        <MarkDown
          v-for="(item, index) in chatList"
          :key="index"
          :message-value="item"
          v-model:chatMessage="chat.message"
          @like="likeMessage"
          @unlike="unlikeMessage"
          @del="delMessage"
        ></MarkDown>
      </div>
      <div class="layout-footer"></div>
      <div class="layout-footer-fixed">
        <send-message @submitMessage="submitMessage" />
      </div>
    </div>
  </div>
</template>
<script setup>
import {
  inject,
  ref,
  reactive,
  onMounted,
  onUpdated,
  defineAsyncComponent,
  watch,
  provide,
  computed,
  nextTick,
  onBeforeUnmount,
  createVNode,
  toRaw,
} from "vue";
import { message, Modal } from "ant-design-vue";
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import setting from "@/utils/setting";
import { debounce } from "@/utils/utils";
import CommonMenu from "./components/menu/index.vue";
import SendMessage from "./components/SendMessage.vue";
import LayoutHeader from "./components/LayoutHeader.vue";
import MarkDown from "./components/MarkDown.vue";
import { useRoute } from "vue-router";
import { ChatStore } from "@/stores/chat";
import { storeToRefs } from "pinia";
const chatStore = ChatStore();
const route = useRoute();
const axios = inject("axios");
const { getChatMenuId, getCurrentWindow } = storeToRefs(chatStore);
let sourc;
const chat = reactive({
  type: "chat",
  message: "",
  loading: false,
  stop: false,
  fileLoading: false,
  needSearch: false,
  pageAutoScroll: true,
  startListenerPageScroll: false,
});
const chatFileList = ref([]);
const chatList = ref([]);
//滚动dom实例
const scrollDom = ref(null);
//组件：推荐问题实例
const questionRef = ref(null);
provide("chat", chat);
provide("chatFileList", chatFileList);
const pagination = reactive({
  page: 1,
  size: 40,
  timestamp: 0,
  loading: false,
  lock: false,
});
onMounted(() => {
  chatStore.initSendOptions();
  chatStore
    .setChatMenuList()
    .then(() => {
      //选择提示器 主动创建窗口
      createPromptWindow();
    })
    .catch((err) => {});

  scrollToBottom(true);
  scrollDom.value.addEventListener("scroll", scrollToTop);
  scrollDom.value.addEventListener("wheel", handleWheel);
});

const submitMessage = async (sendmessage = "") => {
  let chatFileListStatic = toRaw(chatFileList.value);
  if (chatFileListStatic.length != 0 && !chatFileListStatic.every((item) => item.uuid)) {
    message.info("请耐心等待文件全部上传完成");
    return;
  }
  if (chat.loading) return;
  if (chat.message.trim().length == 0 && !sendmessage) {
    message.info("请输入内容");
    return;
  }
  chat.stop = true;
  let hide = null;
  try {
    chat.loading = true;
    hide = message.loading("请求中..", 0);
    let res = await axios.post("/api/chat/sendMessage", {
      message: sendmessage || `${chat.message}`,
      windowId: getChatMenuId.value || null,
      filesUuid: chatFileListStatic.map((chatFileItem) => chatFileItem.uuid) || [],
      isNeedSearch: chatStore.isNetWork,
      isReasoning: getCurrentWindow.value.promptId ? 0 : chatStore.isReasoning,
    });
    hide();
    //更新左侧窗口列表数据
    let chatMenuData = res.data.data || {};
    if (!getChatMenuId.value) {
      chatStore.updateChatMenu(chatMenuData);
    }
    // 联网搜索开关
    chat.needSearch = chatMenuData.needSearch || false;
    //新增问题数据
    chatList.value.push({
      messageId: chatMenuData.messageId,
      message: sendmessage || chat.message,
      role: "user",
      masterUuid: chatMenuData.masterUuid,
      fileInfo: chatFileListStatic || [],
    });
    //文字返回
    EventSoureFn();
  } catch (err) {
    chat.loading = false;
    hide();
  } finally {
    chat.pageAutoScroll = true;
    chat.stop = false;
  }
};

//文字eventsouce
const EventSoureFn = () => {
  if (typeof EventSource !== "undefined") {
    sourc = new EventSource(
      `${
        import.meta.env.VITE_API_BASE_URL
      }/api/chat/stream?Authorization=${setting.takeToken()}`,
      {
        withCredentials: true,
        headers: {
          Authorization: `${setting.takeToken()}`,
        },
      }
    );
    sourc.timeout = 120000;
    sourc.onopen = (e) => {
      chatList.value.push({ message: "", role: "assistant", loading: true });
      scrollToBottom();
      setTimeout(() => {
        chat.startListenerPageScroll = true;
      }, 2000);
    };
    sourc.onmessage = (event) => {
      let data = eval("(" + event.data + ")");
      if (data.finish == "length") {
        resetChatData();
        scrollToBottom();
        showContinueModel();
        return;
      }
      if (data.finish || chat.stop) {
        resetChatData();
        scrollToBottom();

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
          if (data.searchInfoList) {
            chatList.value[chatList.value.length - 1]["searchInfoList"] =
              data.searchInfoList;
          }
        } else {
          console.log("未找到动态内容");
        }
        scrollToBottom();
        return;
      }

      let talkMessage = chatList.value[chatList.value.length - 1].message || "";

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
        searchInfoList: data.searchInfoList || [],
        reasoningContent:
          chatList.value[chatList.value.length - 1].reasoningContent || "",
      };
      scrollToBottom();
    };
    sourc.onerror = (e) => {
      resetChatData();
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

//重置对话
const resetChatData = () => {
  chat.message = "";
  chatFileList.value = [];
  chat.loading = false;
  chat.startListenerPageScroll = false;
  chat.stop = false;
  if (chatList.value.length) {
    chatList.value[chatList.value.length - 1]["loading"] = false;
  }
  sourc.close();
};

const showContinueModel = () => {
  let modal = Modal.info({
    title: "温馨提示",
    icon: createVNode(ExclamationCircleOutlined),
    content:
      "非常抱歉，我现在的Token设置过低，无法完成您的请求。请您联系助手管理员，他们将提供解决方案以满足您的需求。非常感谢您一直以来对我的支持与理解！",

    okText: "确认",
    class: "continueModel",
    onOk() {
      modal.destroy();
    },
  });
};

////删除用户发送记录
const delMessage = async (delData) => {
  chat.stop = true;
  let delIndex = chatList.value.findIndex(
    (item) => item.masterUuid == delData.masterUuid
  );
  let delLength =
    chatList.value.filter((item) => item.masterUuid == delData.masterUuid).length || 0;

  if (delIndex >= 0) {
    chatList.value.splice(delIndex, delLength);
    try {
      let res = await axios.delete("/api/message", {
        params: {
          masterUuid: delData.masterUuid,
        },
      });
    } catch (err) {}
  }
};

//标记不喜欢
const unlikeMessage = async (data) => {
  try {
    let messageIndex = chatList.value.findIndex(
      (item) => item.messageId == data.messageId
    );
    if (messageIndex >= 0) {
      chatList.value[messageIndex]["isLike"] = 2;
      try {
        let res = await axios.get("/api/message/unlike", {
          params: {
            messageId: data.messageId,
          },
        });
      } catch (err) {}
    }
  } catch (err) {}
};

//标记喜欢
const likeMessage = async (data = {}) => {
  try {
    let messageIndex = chatList.value.findIndex(
      (item) => item.messageId == data.messageId
    );
    if (messageIndex >= 0) {
      chatList.value[messageIndex]["isLike"] = 1;
      try {
        let res = await axios.get("/api/message/like", {
          params: {
            messageId: data.messageId,
          },
        });
      } catch (err) {}
    }
  } catch (err) {}
};

//选择提示器 主动创建窗口
const createPromptWindow = async () => {
  let query = history.state || {};
  try {
    if (!query || !query.id) return;
    let res = await axios.post("/api/window/create", {
      promptId: query.id,
    });
    //更新左侧窗口列表数据
    let chatMenuData = res.data.data || {};
    chatStore.updateChatMenu(chatMenuData);
    if (chatMenuData.initMessage) {
      chatList.value.push({
        message: chatMenuData.initMessage,
        role: "assistant",
        isInitMessage: 1,
      });
    }
  } catch (err) {
  } finally {
    query.id = null;
  }
};

//获取窗口数据
const getHistoryMessageList = async ({ chatMenuId }) => {
  let oScrollHeight = scrollDom.value.scrollHeight;
  let hide = null;
  if (pagination.lock || pagination.loading) return;
  if (!chatMenuId) {
    chatList.value = [];
    return;
  }
  try {
    pagination.loading = true;
    hide = message.loading("请求中..", 0);
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

    hide();
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
        nextTick(() => scrollToBottom(true));
      } else {
        nextTick(() => {
          let nScrollHeight = scrollDom.value.scrollHeight;
          scrollDom.value.scrollTop = nScrollHeight - oScrollHeight + 90;
        });
      }

      setTimeout(() => {
        pagination.loading = false;
      }, 1000);
    }
  } catch (err) {
    pagination.loading = false;
    hide();
  } finally {
  }
};

const scrollToTop = debounce(() => {
  if (chat.startListenerPageScroll) {
    const { scrollTop, scrollHeight, clientHeight } = scrollDom.value;
    const isAtBottom = scrollHeight - (scrollTop + clientHeight) <= 68;
    if (isAtBottom) {
      chat.pageAutoScroll = true; // 滚动到底部时，启用自动滚动
    }
  }
  let scrollTop = scrollDom.value.scrollTop;
  if (scrollTop <= 90) {
    getHistoryMessageList({ chatMenuId: getChatMenuId.value });
  }
}, 100);

// 鼠标滚轮事件处理函数
const handleWheel = () => {
  if (chat.startListenerPageScroll) {
    chat.pageAutoScroll = false; // 用户手动滚动时，禁用自动滚动
  }
};

onUpdated(() => {});

//滚动底部
const scrollToBottom = (force = false) => {
  if (!chat.pageAutoScroll && !force) return;
  scrollDom.value.scrollTop = scrollDom.value.scrollHeight;
};

watch([getChatMenuId], (newVal, oldVal) => {
  if (newVal[0] != oldVal[0]) {
    pagination.loading = false;
    pagination.timestamp = 0;
    pagination.page = 1;
    pagination.lock = false;
    chatList.value = [];
    getHistoryMessageList({ chatMenuId: newVal[0] });
    //离开初始化
    if (oldVal[0] != 0) {
      chat.loading = false;
      chat.startListenerPageScroll = false;
      chat.stop = true;
      chat.message = "";
      chatFileList.value = [];
    }
  }
});

onBeforeUnmount(() => {
  if (scrollDom.value) {
    scrollDom.value.removeEventListener("scroll", scrollToTop);
    scrollDom.value.removeEventListener("wheel", handleWheel);
  }

  chat.stop = true;
  chatStore.setChatMenuId(0);
});
</script>

<style scoped lang="less">
.chatPage {
  width: 100%;
  height: 100%;
}
.layout {
  display: flex;
  flex: auto;
  flex-direction: column;
  width: 852px;
  height: 100%;
  margin: 0 auto;
  position: relative;
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
  .layout-content {
    min-height: auto;
    flex: auto;
    width: 852px;
    margin: 0 auto;
    // padding-top: 110px;
  }
  .layout-header {
    width: 100%;
    // width: calc(100% - 260px);
    // position: fixed;
    // top: 65px;
    // left: 260px;
    z-index: 3;
    margin-bottom: 35px;
  }
  .layout-footer {
    flex: 0 0 auto;
    width: 100%;
    height: 156px;
  }
  .layout-footer-fixed {
    width: calc(100% - 260px);
    position: fixed;
    // bottom: 53px;
    bottom: 0;
    left: 260px;
    padding-bottom: 30px;
    background: linear-gradient(180deg, rgba(245, 246, 248, 0) 0%, #f5f6f8 61%);
  }
}
</style>

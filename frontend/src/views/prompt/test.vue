<template>
  <test-header :prompt="promptData" />
  <div class="layout" ref="scrollDom">
    <div class="layout-content">
      <template v-for="(item, index) in chatList" :key="index">
        <MarkDownUser
          v-if="item.role == 'user' || item.isInitMessage"
          :message-value="item"
          v-model:chatMessage="chat.message"
        ></MarkDownUser>
        <div
          class="modelList"
          :class="{
            modelList_1: item.list.length == 1 ? true : false,
            modelList_2: item.list.length > 1 && item.list.length < 5 ? true : false,
          }"
          v-else
        >
          <MarkDown
            v-for="(itm, idx) in item.list"
            :key="idx"
            :message-value="itm"
          ></MarkDown>
        </div>
      </template>
    </div>
    <div class="layout-footer-fixed">
      <send-message
        @stopEventSouce="closeEventSoure"
        @submitMessage="submitMessage"
        v-model:chatMessage="chat.message"
      />
    </div>
  </div>
  <enlarge-modal v-model:visible="enlargeVisible" :enlargeData="enlargeData" />
</template>
<script setup>
import { message } from "ant-design-vue";
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
import setting from "@/utils/setting";
import testHeader from "./components/test/header.vue";
import sendMessage from "./components/test/sendMessage.vue";
import MarkDownUser from "./components/test/MarkDownUser.vue";
import MarkDown from "./components/test/MarkDown.vue";
import EnlargeModal from "./components/test/enlargeModal.vue";
import { useRoute } from "vue-router";
const route = useRoute();
const axios = inject("axios");
const models = ref([]);
const promptData = reactive({
  id: "",
  name: "",
  tips: [],
  model: "",
});
//是否在提交中
const chat = reactive({
  message: "",
  loading: false,
  stop: false,
  uuid: "",
  event: [],
});
const chatList = ref([]);
const enlargeData = ref({});
const enlargeVisible = ref(false);
provide("chat", chat);
provide("models", models);
provide("enlargeData", enlargeData);
provide("enlargeVisible", enlargeVisible);
//滚动dom实例
const scrollDom = ref(null);
onMounted(() => {
  let query = route.query || {};
  query.tips = JSON.parse(query.tips || []);
  Object.assign(promptData, query);
  chatList.value.push({
    message: query.initMessage,
    tips: query.tips,
    role: "assistant",
    isInitMessage: true,
  });
});

const submitMessage = async () => {
  if (chat.loading) return;
  if (chat.message.trim().length == 0) {
    message.error("请输入内容");
    return;
  }
  await closeEventSoure(false);
  let hide = null;
  try {
    chat.loading = true;
    hide = message.loading("请求中..", 0);
    let res = await axios.post(
      "/api/prompt/test/sendMessage",
      {
        ...chat,
        promptId: promptData.id,
        models: models.value.map((item) => item.modelLabel),
      },
      {
        timeout: 50000,
      }
    );
    hide();
    let data = res.data.data || {};
    chat.uuid = data.uuid;
    if (data.windows.length == 0 || !data.windows) {
      chat.loading = false;
      return;
    }
    //添加用户问题
    chatList.value.push({
      message: chat.message,
      role: "user",
    });
    // 处理返回模型数据
    let _models = toRaw(models.value);
    let windows = data.windows.map((dataItem, index) => {
      let modelItemData =
        _models.find((modelItem) => modelItem.modelLabel == dataItem.model) || {};
      dataItem.icon = modelItemData.icon;
      dataItem.modelLabel = modelItemData.modelLabel;
      dataItem.modelName = modelItemData.modelName;
      dataItem.message = "";
      dataItem.index = index;
      return dataItem;
    });
    chatList.value.push({
      role: "assistant",
      list: windows,
    });
    nextTick(() => {
      chat.event = [];
      windows.forEach((item, index) => {
        chat.event.push(EventSoureFn({ windowId: item.windowId, chatIndex: index }));
      });
      chat.message = "";
      scrollToBottom();
    });
  } catch (err) {
    chat.loading = false;
    hide();
  } finally {
  }
};

//文字eventsouce
const EventSoureFn = ({ windowId, chatIndex }) => {
  if (typeof EventSource !== "undefined") {
    const sourc = new EventSource(
      `${
        import.meta.env.VITE_API_BASE_URL
      }/api/py/test/stream?Authorization=${setting.takeToken()}&windowId=${windowId}`,
      {
        withCredentials: true,
        headers: {
          Authorization: `${setting.takeToken()}`,
        },
      }
    );
    sourc.timeout = 120000;
    sourc.onopen = (e) => {};
    sourc.onmessage = (event) => {
      let data = eval("(" + event.data + ")");
      if (data.finish == "length") {
        chat.loading = false;
        sourc.close();
        scrollToBottom();
        return;
      }
      if (data.finish) {
        chat.loading = false;
        sourc.close();
        return;
      }

      if (!data.content && data.reasoningContent) {
        let reasoningContent =
          chatList.value[chatList.value.length - 1].list[chatIndex].reasoningContent ||
          "";
        reasoningContent += data.reasoningContent;
        chatList.value[chatList.value.length - 1].list[
          chatIndex
        ].reasoningContent = reasoningContent;

        return;
      }

      let talkMessage =
        chatList.value[chatList.value.length - 1].list[chatIndex].message || "";

      if (data.hasOwnProperty("content")) {
        if (talkMessage == "" && data.content) {
          talkMessage = data.content.replace(/^\n+/, ""); //去掉回复消息中偶尔开头就存在的连续换行符
        } else {
          talkMessage += data.content;
        }
      }
      talkMessage = talkMessage.replace(/\\n/g, "\n");
      chatList.value[chatList.value.length - 1].list[chatIndex].message = talkMessage;
      chatList.value[chatList.value.length - 1].list[chatIndex].docInfo = data.docInfo;
    };
    sourc.onerror = (e) => {
      console.log(e, "onerror");
      chat.message = "";
      chat.loading = false;
      sourc.close();
      if (e?.target?.readyState === 2) {
        message.error("请求超时");
      } else {
        message.error("请求失败,请稍后重试");
      }
    };
    return sourc;
  } else {
    message.error("当前浏览器不支持服务器发送的事件");
  }
};

const closeEventSoure = (cleanMessage = true) => {
  return new Promise((resolve, reject) => {
    if (chat.event.length != 0) {
      chat.event.forEach((item) => {
        item.close();
      });

      chat.loading = false;
      chat.event = [];
      if (cleanMessage) {
        chat.message = "";
      }
    }
    resolve();
  });
};

//滚动底部
const scrollToBottom = () => {
  scrollDom.value.scrollTop = scrollDom.value.scrollHeight;
};

onBeforeUnmount(() => {
  closeEventSoure();
});
</script>

<style lang="less" scoped>
.layout {
  display: flex;
  flex: auto;
  flex-direction: column;
  width: 100%;
  height: 100%;
  margin: 0 auto;
  position: relative;
  overflow-y: auto;
  .layout-content {
    width: 90%;
    margin: 0 auto;
    min-height: auto;
    flex: auto;
    padding-top: 100px;
    padding-bottom: 156px;
    .modelList {
      display: flex;
      align-items: flex-start;
      justify-content: flex-start;
      flex-wrap: wrap;
      padding-left: 52px;
      .chat-user-view-markdown {
        &:nth-of-type(3n) {
          margin-right: 0;
        }
      }
      &.modelList_1 {
        .chat-user-view-markdown {
          margin-right: 0;
          width: 100%;
        }
      }
      &.modelList_2 {
        justify-content: space-between;
        .chat-user-view-markdown {
          margin-right: 0;
          width: 49%;
        }
      }
    }
  }

  .layout-footer-fixed {
    width: 100%;
    position: fixed;
    // bottom: 53px;
    bottom: 0;
    left: 0;
    padding-bottom: 30px;
    background: linear-gradient(180deg, rgba(245, 246, 248, 0) 0%, #f5f6f8 61%);
  }
}
</style>

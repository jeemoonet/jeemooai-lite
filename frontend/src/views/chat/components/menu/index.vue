<template>
  <div class="menuPage">
    <div class="nav_title_box">
      <div class="nav_title_content">
        <div class="nav_title">工作助手</div>
      </div>
    </div>
    <!-- 添加文本 -->
    <AddButton title="自由问答" @addClick="onAddMenuItem"></AddButton>
    <!-- 窗口列表 -->
    <a-menu @click="selectMenuItem" :selectedKeys="[getChatMenuId]" model="inline">
      <a-menu-item v-for="item in getChatMenuList" :key="item.windowId">
        <div class="nav-text">
          <i class="ico_chat bg"></i>
          <p class="one_line">{{ item.windowName }}</p>
          <div class="chat-controller right">
            <edit-outlined
              v-if="item.windowId"
              @click.stop="editMenuItem(item)"
              class="edit-svg"
            />
            <a-popconfirm
              title="确认是否删除？"
              @confirm.stop="deleteMenuItem(item)"
              ok-text="是"
              cancel-text="否"
            >
              <delete-outlined v-if="item.windowId" @click.stop class="delete-svg" />
            </a-popconfirm>
          </div>
        </div>
      </a-menu-item>
    </a-menu>
    <select-prompt @onChange="createPromptWindow" />
  </div>
  <!-- 窗口名称修改 -->
  <a-modal
    v-model:visible="windowNameVisible"
    :confirmLoading="windowNameLoading"
    :title="false"
    :closable="false"
    okText="确认"
    cancelText="关闭"
    @ok="onEditWindowName"
  >
    <a-input
      v-model:value="windowNameValue"
      :maxlength="12"
      placeholder="请输入窗口名称"
    />
  </a-modal>
</template>
<script setup>
import { inject, ref, reactive, onMounted, defineProps, defineEmits } from "vue";
import { MessageOutlined, DeleteOutlined, EditOutlined } from "@ant-design/icons-vue";
import AddButton from "./AddButton.vue";
import SelectPrompt from "./SelectPrompt1.vue";
import { useRouter } from "vue-router";
import { message } from "ant-design-vue";
import { ChatStore } from "@/stores/chat";
import { storeToRefs } from "pinia";
const axios = inject("axios");

const router = useRouter();
//获取store slider数据
const chatStore = ChatStore();
const { getChatMenuList, getChatMenuId } = storeToRefs(chatStore);
//修改名称
const windowNameVisible = ref(false);
const windowNameValue = ref("");
const windowNameLoading = ref(false);
const windowNameData = ref({});

//添加窗口
const onAddMenuItem = () => {
  chatStore.addDefaultChatMenu();
};

//选择窗口
const selectMenuItem = ({ item, key, keyPath }) => {
  chatStore.setChatMenuId(key);
};

//选择提示器 主动创建窗口
const createPromptWindow = async (id) => {
  try {
    let res = await axios.post("/api/window/create", {
      promptId: id,
    });
    //更新左侧窗口列表数据
    let chatMenuData = res.data.data || {};
    chatStore.addChatMenu(chatMenuData);
  } catch (err) {
  } finally {
  }
};

//删除
const deleteMenuItem = (res) => {
  if (!res.windowId) return;
  chatStore.deleteChatMenu(res);
};

//修改名称
const onEditWindowName = async (res) => {
  if (windowNameValue.value == windowNameData.value.windowName) {
    windowNameVisible.value = false;
    return;
  }
  if (windowNameValue.value.trim().length == 0) {
    message.warning("请输入窗口名");
    return;
  }
  let d = windowNameData.value || {};
  d.windowName = windowNameValue.value;
  windowNameLoading.value = true;
  try {
    await chatStore.updateChatMenuItemName(d);
    message.success("修改成功");
  } catch (err) {
  } finally {
    windowNameLoading.value = false;
    windowNameVisible.value = false;
  }
};

//点击修改名称 打开model
const editMenuItem = (res) => {
  if (!res.windowId) return;
  windowNameValue.value = res.windowName || "";
  windowNameVisible.value = true;
  windowNameData.value = res || {};
};
</script>
<style lang="less" scoped>
.menuPage {
  width: 260px;
  height: 100%;
  // overflow-y: auto;
  background-color: #f5f6f8;
  :deep(.ant-menu) {
    height: calc(100% - 160px - 62px);
    overflow-y: auto;
    background-color: transparent;
    border: none;
    .ant-menu-item-selected {
      background-color: transparent !important;

      .nav-text,
      .anticon {
        color: #5657f6;
      }
      .chat-controller {
        display: block;
      }
      .ico_chat {
        background-image: url(./images/chat1.png);
      }
    }
    .ant-menu-item {
      // display: flex;
      font-size: 16px;
      font-weight: 500;
      line-height: 24px;
      height: 24px;
      margin-top: 0;
      margin-bottom: 23px;
      padding-left: 30px;
      padding-right: 0;
      &:hover {
        background-color: transparent;
        .chat-controller {
          display: block;
        }
      }
    }

    .ant-menu-title-content {
      display: flex;
      align-items: center;
      justify-content: flex-start;
    }
  }
}
.nav_title_box {
  padding-left: 30px;
  margin-top: 0px;
  margin-bottom: 16px;
  .nav_title_content {
    padding: 20px 0;
    border-bottom: 1px solid #1d212910;
    .nav_title {
      font-size: 16px;
      font-weight: 600;
      color: #333333;
      line-height: 24px;
    }
  }
}

.chat-controller {
  display: none;
  line-height: 22px;
  color: #1d2129;
}

.nav-text {
  display: flex;
  // display: block;
  align-items: center;
  // width: 168px;
  width: 100%;
  color: #1d2129;
  > .one_line {
    flex: 1;
    min-width: 0;
    font-weight: 500;
  }
}

.ico_chat {
  width: 22px;
  height: 22px;
  background-image: url(./images/chat.png);
  margin-right: 22px;
}
</style>

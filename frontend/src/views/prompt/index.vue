<template>
  <div class="promptPage headerMenuPadding left">
    <div class="layout">
      <div class="layout-header right">
        <a-select
          v-model:value="params.categoryId"
          class="filter-select"
          :bordered="false"
          @change="onSearch"
        >
          <a-select-option value="">全部</a-select-option>
          <a-select-option
            :value="item.categoryId"
            v-for="(item, index) in categoryList"
            :key="index"
            >{{ item.categoryName }}</a-select-option
          >
        </a-select>
        <a-input-group class="search">
          <a-input
            @pressEnter="onSearch"
            v-model:value="params.keywords"
            :maxlength="20"
            placeholder="请输入搜索内容"
          />
          <i class="ico_search bg" @click="onSearch"></i>
        </a-input-group>
      </div>
      <div class="layout-content all" ref="scrollDom">
        <div class="item add" :class="{ item_four: collapsed }" @click="onAdd">
          <i class="add_wf bg"></i>
          <h4 class="title">创建智能助手</h4>
        </div>
        <div
          class="item wf"
          :class="{ item_four: collapsed }"
          @click="onWorkFlowItem(item)"
          v-for="(item, index) in dataSource"
          :key="index"
        >
          <div class="wf_content left">
            <div class="wf_img_box iconcenter">
              <img :src="item.promptIcon" class="wf_img" />
              <img
                v-if="item.promptType == 1"
                src="./images/prompt_type_workflow.png"
                class="ico_workflow"
              />
            </div>
            <div class="wf_detail">
              <div class="float">
                <div class="title one_line">{{ item.promptName || "-" }}</div>
                <div class="right">
                  <a-dropdown>
                    <div class="more iconcenter">
                      <div></div>
                      <div></div>
                      <div></div>
                    </div>
                    <template #overlay>
                      <a-menu>
                        <a-menu-item @click="onEdit(item)"> 编辑 </a-menu-item>
                        <a-menu-item @click="onCopy(item)"> 创建副本 </a-menu-item>
                        <a-menu-item @click="onDel(item)"> 删除 </a-menu-item>
                      </a-menu>
                    </template>
                  </a-dropdown>
                </div>
              </div>
              <div class="detail one_line">{{ item.promptDesc || "-" }}</div>
            </div>
          </div>
          <div class="wf_info left">
            <div class="user float">
              <img :src="item.avatar" class="avatar" />
              {{ item.nickName || "-" }}
            </div>
            <div class="at one_line">编辑时间：{{ item.updateTime || "-" }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <add-modal v-model:visible="visible" :promptData="promptData" @update="onUpdate" />
</template>
<script setup>
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import {
  inject,
  ref,
  reactive,
  onMounted,
  onBeforeUnmount,
  watch,
  computed,
  nextTick,
  createVNode,
} from "vue";
import { message, Modal } from "ant-design-vue";
import addModal from "./components/addPromptModal.vue";
import { useRouter } from "vue-router";
import { throttle } from "@/utils/utils";
import { WorkflowStore } from "@/stores/workflow.js";
import { storeToRefs } from "pinia";
const { collapsed } = storeToRefs(WorkflowStore());
const axios = inject("axios");
const router = useRouter();
//滚动dom实例
const scrollDom = ref(null);
//数据源
const dataSource = ref([]);
const categoryList = ref([]);
//分页
const pagination = reactive({
  pageSize: 15,
  pageNum: 1,
  total: 0,
  lock: false,
});
const params = reactive({
  categoryId: "",
  keywords: "",
});
//内容
const promptData = ref({});
const visible = ref(false);

onMounted(() => {
  getPromptList();
  getPromptCategoryList();
  scrollDom.value.addEventListener("scroll", throttle(scrollToBottom, 100));
});

const onWorkFlowItem = (res = {}) => {
  const href = router.resolve({
    path: "/prompt/detail/" + res.promptId,
  });
  window.open(href.href, "_blank");
};
const onAdd = () => {
  promptData.value = {};
  visible.value = true;
};

const onEdit = (res = {}) => {
  promptData.value = res;
  visible.value = true;
};

const onCopy = async (res = {}) => {
  promptData.value = JSON.parse(JSON.stringify(res));
  promptData.value.isCopy = true;
  visible.value = true;
};

const onRelease = (res = {}) => {
  const href = router.resolve({
    path: "/prompt/release/" + res.promptId,
  });
  window.open(href.href, "_blank");
};

const onDel = async (e = {}) => {
  Modal.confirm({
    title: "请确认是否删除?",
    icon: createVNode(ExclamationCircleOutlined),
    content: "删除该智能助手后将不可恢复，需重新创建",
    okText: "是",
    cancelText: "否",
    onOk: async () => {
      try {
        await axios.delete("/api/prompt", {
          params: {
            promptId: e.promptId,
          },
        });
        resetWorkFlowList();
        message.success("删除成功");
      } catch (err) {
      } finally {
      }
    },
    onCancel() {},
  });
};

const onUpdate = (res = {}) => {
  res.promptId && onWorkFlowItem(res);
  resetWorkFlowList();
};

const onSearch = () => {
  resetWorkFlowList();
};

const resetWorkFlowList = () => {
  pagination.pageNum = 1;
  pagination.total = 0;
  nextTick(() => getPromptList());
};

const getPromptList = async () => {
  try {
    let res = await axios.get("/api/prompt/page", {
      params: {
        ...params,
        pageSize: pagination.pageSize,
        pageNum: pagination.pageNum,
      },
    });
    let total = res.data.total || 0;
    let data = res.data.rows || [];
    if (pagination.pageNum == 1) {
      dataSource.value = data;
    } else {
      dataSource.value.push(...data);
    }
    pagination.total = total;
    pagination.pageNum += 1;
  } catch (err) {
  } finally {
  }
};

const getPromptCategoryList = async () => {
  try {
    let res = await axios.get("/api/home/categoryList", {});

    let data = res.data.data || [];
    categoryList.value = data;
  } catch (err) {
  } finally {
  }
};

const scrollToBottom = () => {
  if ((pagination.pageNum - 1) * pagination.pageSize >= pagination.total) return;
  let scrollHeight = scrollDom.value.scrollHeight;
  let windowHeight = scrollDom.value.clientHeight;
  let scrollTop = scrollDom.value.scrollTop;
  let scrollBottom = scrollHeight - windowHeight - scrollTop;
  if (scrollBottom <= 140) {
    getPromptList();
  }
};

onBeforeUnmount(() => {
  scrollDom.value.removeEventListener("scroll", throttle(scrollToBottom, 100));
});
</script>

<style scoped lang="less">
.promptPage {
  width: 100%;
  height: 100%;
}

.layout {
  display: flex;
  flex: auto;
  flex-direction: column;
  width: calc(100% - 260px);
  height: 100%;
  margin: 0 auto;
  position: relative;
  overflow-y: auto;
  padding: 0 30px;
  .layout-header {
    flex: 0 0 auto;
    width: 100%;
    padding-top: 20px;
    padding-bottom: 10px;

    :deep(.filter-select) {
      width: 96px;
      height: 42px;
      background-color: #fff;
      .ant-select-arrow {
        color: #333;
      }
      .ant-select-selector {
        .ant-select-selection-item {
          line-height: 42px;
        }
      }
      &.ant-select-open {
        .ant-select-arrow {
          color: rgba(0, 0, 0, 0.25);
        }
      }
    }
    .search {
      width: 185px;
      height: 42px;
      margin-left: 18px;
      :deep(.ant-input) {
        width: 100%;
        height: 42px;
        background-color: #fff;
        border-radius: 4px;
        border: none;
        color: #333;
        font-size: 16px;
        padding-left: 9px;
        border: 1px solid #ebeef5;
        &::placeholder {
          color: #9599a5;
        }
      }

      .ico_search {
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
        right: 16px;
        z-index: 2;
        width: 16px;
        height: 16px;
        background-image: url("../../assets/images/ico_search1.png");
        cursor: pointer;
      }
    }
  }
  .layout-content {
    // max-height: calc(100% - 72px);
    max-height: calc(100vh - 65px - 82px - 10px);
    // min-height: 0;
    // flex: 1;
    padding: 10px 0 0;
    overflow-y: auto;

    .item {
      width: calc(33.33% - 20px);
      height: 148px;
      border-radius: 12px;
      background: #ffffff;
      margin-right: 20px;
      margin-bottom: 20px;
      padding: 20px 16px 16px;
      cursor: pointer;
      &.item_four {
        width: calc(25% - 17px);
        &:nth-of-type(3n) {
          margin-right: 17px;
        }
        &:nth-of-type(4n) {
          margin-right: 0;
        }
      }
      &:hover {
        box-shadow: 0px 4px 6px -2px rgba(16, 24, 40, 0.03),
          0px 12px 16px -4px rgba(16, 24, 40, 0.08);
      }
      &:nth-of-type(3n) {
        margin-right: 0;
      }
    }
    .add {
      padding-top: 40px;
      .add_wf {
        width: 40px;
        height: 40px;
        background-image: url(./images/add_wf.png);
        margin: 0 auto;
      }
      .title {
        font-size: 14px;
        font-weight: normal;
        line-height: normal;
        color: #333333;
        margin: 0;
        padding: 0;
        margin-top: 6px;
        text-align: center;
      }
    }
    .wf {
      .wf_content {
        padding-bottom: 16px;
        border-bottom: 1px solid #ebebeb;
        .wf_img_box {
          width: 56px;
          height: 56px;
          margin-right: 12px;
          position: relative;
          .wf_img {
            display: block;
            width: 100%;
            height: 100%;
            border-radius: 8px;
          }
          .ico_workflow {
            display: block;
            width: 18px;
            height: 18px;
            position: absolute;
            top: -8px;
            right: -8px;
          }
        }
        .wf_detail {
          flex: 1;
          min-width: 0;
          height: 56px;
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          .title {
            font-size: 16px;
            font-weight: 500;
            line-height: 24px;
            color: #333333;
          }
          .release {
            width: 16px;
            height: 16px;
            background-image: url(./images/release.png);
            margin-right: 6px;
            &:hover {
              background-image: url(./images/release-hover.png);
            }
          }
          .more {
            width: 24px;
            height: 24px;
            cursor: pointer;
            > div {
              width: 3px;
              height: 3px;
              border-radius: 50%;
              background: #1d2129;
              margin-right: 3px;
              &:nth-last-of-type(1) {
                margin-right: 0;
              }
            }
            &:hover {
              > div {
                background: #5657f6;
              }
            }
          }
          .detail {
            font-size: 14px;
            font-weight: normal;
            line-height: normal;
            color: rgba(29, 33, 41, 0.6);
          }
        }
      }
      .wf_info {
        width: 100%;
        padding-top: 16px;
        .user {
          font-size: 12px;
          font-weight: normal;
          line-height: 16px;
          color: #1d2129;
          .avatar {
            display: block;
            width: 24px;
            height: 24px;
            border-radius: 50%;
            margin-right: 8px;
          }
        }
        .at {
          flex: 1;
          min-width: 0;
          margin-left: 16px;
          font-size: 12px;
          font-weight: normal;
          line-height: 16px;
          color: rgba(29, 33, 41, 0.4);
        }
        .status {
          font-size: 12px;
          font-weight: normal;
          line-height: 16px;
          text-align: right;
          color: #333333;
          .ico_status {
            display: block;
            width: 8px;
            height: 8px;
            opacity: 1;
            background: #14c388;
            margin-right: 4px;
            border-radius: 50%;
            &.status_fail {
              background: #9599a5;
            }
          }
        }
      }
    }
  }
}

:global(.ant-modal-confirm-confirm .ant-modal-confirm-body > .anticon) {
  color: #f44e4e;
}
</style>

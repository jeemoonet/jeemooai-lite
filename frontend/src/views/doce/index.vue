<template>
  <div class="docePage headerMenuPadding left">
    <div class="layout">
      <div class="layout-header">
        <div class="float">
          <div class="controller left">
            <a-button
              class="upload iconcenter"
              v-if="
                doceHistory.length > 1 && params.folderId != 1 && params.folderId != 2
              "
              @click="uploadFileVisible = true"
              ><UploadOutlined class="ico_upload" />上传资料</a-button
            >
            <a-button
              class="upload iconcenter"
              v-if="doceHistory.length > 1"
              @click="onAddFolder"
              ><PlusCircleOutlined class="ico_upload" />新建文件夹</a-button
            >
            <a-button class="upload iconcenter" @click="testingVisible = true"
              ><i class="ico_test bg"></i>命中测试</a-button
            >
          </div>
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
        <div class="controller1 float">
          <div class="history left">
            <div class="back" v-if="doceHistory.length > 1" @click="onBack()">
              返回上一级
            </div>
            <div
              class="item"
              :class="doceHistory.length - 1 == index ? '' : 'active'"
              v-for="(item, index) in doceHistory"
              :key="index"
              @click="doceHistory.length - 1 == index ? '' : onBack(index + 1)"
            >
              {{ item.documentName
              }}{{ doceHistory.length - 1 == index ? "" : "&nbsp;>&nbsp;" }}
            </div>
          </div>
          <div class="view">
            <UnorderedListOutlined
              @click="onViewChange('table')"
              :class="tabStatus == 'table' ? 'active' : ''"
            />
            <AppstoreOutlined
              @click="onViewChange('grid')"
              :class="tabStatus == 'grid' ? 'active' : ''"
            />
          </div>
        </div>
      </div>
      <div class="layout-content all">
        <component
          :dataSource="dataSource"
          @update="resetDoceList"
          @edit="onEditFolder"
          @manage="onManage"
          @split="onSplit"
          :is="tableComponents.get(tabStatus)"
        ></component>
      </div>
    </div>
  </div>
  <!-- 我的知识库 新建/编辑文件夹 -->
  <edit-doce-modal
    v-model:visible="addFolderVisible"
    :treeSelectData="selectFolderData"
    @update="resetDoceList"
  />
  <!-- 上传文件 -->
  <update-file-modal v-model:visible="uploadFileVisible" @update="resetDoceList" />
  <!-- 命中测试 -->
  <testing-modal v-model:visible="testingVisible" />
  <!-- 向量管理 -->
  <manage-modal :current-data="selectFileData" v-model:visible="manageVisible" />
  <!-- 向量切分 -->
  <split-modal v-model:visible="splitVisible" :id="selectFileData.documentId" />
</template>
<script setup>
import {
  ExclamationCircleOutlined,
  UploadOutlined,
  PlusCircleOutlined,
  UnorderedListOutlined,
  AppstoreOutlined,
} from "@ant-design/icons-vue";
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
  provide,
  defineAsyncComponent,
} from "vue";
import { message, Modal } from "ant-design-vue";
import UpdateFileModal from "./components/updateFileModal.vue";
import TestingModal from "./components/testingModal.vue";
import ManageModal from "./components/manageModal.vue";
import SplitModal from "./components/splitModal.vue";
import EditDoceModal from "./components/EditDoceModal.vue";
import { useRouter } from "vue-router";
import { throttle } from "@/utils/utils";
import { WorkflowStore } from "@/stores/workflow.js";
import { storeToRefs } from "pinia";
const { collapsed } = storeToRefs(WorkflowStore());
const axios = inject("axios");
const router = useRouter();
//table grid
const tabStatus = ref("table");
const tableComponents = ref(new Map());
tableComponents.value.set(
  "table",
  defineAsyncComponent(() => import("./components/listTable.vue"))
);
tableComponents.value.set(
  "grid",
  defineAsyncComponent(() => import("./components/listGrid.vue"))
);

//数据源
const dataSource = ref([]);
const params = reactive({
  folderId: 1,
  keywords: "",
});
provide("doceParams", params);

const doceHistory = ref([
  {
    documentName: "全部文件",
    folderId: "",
  },
  {
    documentName: "我的知识库",
    folderId: 1,
  },
]);
provide("doceHistory", doceHistory);
//选择的文件夹
const selectFolderData = ref({});
// 选择的文档
const selectFileData = ref({});
const uploadFileVisible = ref(false);
const addFolderVisible = ref(false);
const testingVisible = ref(false);
const manageVisible = ref(false);
const splitVisible = ref(false);

onMounted(() => {
  getDoceList();
});
// 向量管理
const onManage = (e) => {
  selectFileData.value = e;
  nextTick(() => {
    manageVisible.value = true;
  });
};
const onSplit = async (e) => {
  selectFileData.value = e;
  nextTick(() => {
    splitVisible.value = true;
  });
};

//新建文件夹
const onAddFolder = () => {
  if (doceHistory.value.length < 2) return;
  let folderType = doceHistory.value[1]["folderId"];
  selectFolderData.value = {
    type: folderType,
    parentId: params.folderId,
  };
  nextTick(() => {
    addFolderVisible.value = true;
  });
};
//编辑文件夹
const onEditFolder = (e) => {
  if (doceHistory.value.length < 2) return;
  let folderType = doceHistory.value[1]["folderId"];
  selectFolderData.value = {
    type: folderType,
    parentId: params.folderId,
    categoryId: e.folderId,
    categoryName: e.documentName,
  };
  nextTick(() => {
    addFolderVisible.value = true;
  });
};
// 返回指定目录
const onBack = (e) => {
  if (!e) {
    //返回上一级
    doceHistory.value.splice(-1);
  } else {
    //返回指定文件夹
    doceHistory.value = doceHistory.value.slice(0, e);
  }
  nextTick(() => {
    let lastHistory = doceHistory.value[doceHistory.value.length - 1] || {};
    params.folderId = lastHistory.folderId;
    resetDoceList();
  });
};
// 切换视图类型
const onViewChange = (e) => {
  if (e == tabStatus.value) return;
  tabStatus.value = e;
};

const onSearch = () => {
  getDoceList();
};

const resetDoceList = () => {
  params.keywords = "";
  nextTick(() => getDoceList());
};

const getDoceList = async () => {
  try {
    let res = await axios.get("/api/document/folder", {
      params: {
        ...params,
      },
    });
    let data = res.data.data || res.data.rows || [];
    dataSource.value = data;
  } catch (err) {
  } finally {
  }
};

onBeforeUnmount(() => {});
</script>

<style scoped lang="less">
.docePage {
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
    width: 100%;
    padding-top: 20px;
    .controller {
      .upload {
        width: fit-content;
        height: 42px;
        border-radius: 8px;
        background: #5657f6;
        border: 1px solid #5657f6;
        font-size: 14px;
        font-weight: 500;
        line-height: 42px;
        color: #fff;
        border: none;
        transition: all 0.15s ease-in-out;
        margin-right: 10px;
        padding: 0 16px;
        .ico_upload {
          font-size: 16px;
        }
        .ico_test {
          width: 18px;
          height: 18px;
          margin-right: 5px;
          background-image: url(../../assets/images/ico_test.png);
        }
        &:hover {
          background: #3838c0;
        }
      }
    }
    .search {
      width: 185px;
      height: 42px;
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
    .controller1 {
      width: 100%;
      margin-top: 20px;
      margin-bottom: 20px;
      padding-right: 10px;
      .view {
        font-size: 16px;
        color: #999;
        cursor: pointer;
        .anticon {
          margin-left: 12px;
          &.active {
            color: #5657f6;
          }
        }
      }
    }
    .history {
      flex: 1;
      min-width: 0;
      overflow-x: auto;

      .back {
        font-size: 14px;
        font-weight: normal;
        line-height: 18px;
        color: #5657f6;
        cursor: pointer;
        position: relative;
        margin-right: 8px;
        padding-right: 8px;
        &::after {
          content: "";
          width: 1px;
          height: 12px;
          background: #999999;
          position: absolute;
          top: 50%;
          right: 0;
          transform: translateY(-50%);
        }
      }
      .item {
        font-size: 14px;
        font-weight: normal;
        line-height: 18px;
        color: #999999;
        &.active {
          color: #5657f6;
          cursor: pointer;
        }
      }
    }
  }
  .layout-content {
    max-height: calc(100% - 128px);
    // min-height: auto;
    // flex: 1;
    padding: 0px 0 0;
    overflow-y: auto;
  }
}

:global(.ant-modal-confirm-confirm .ant-modal-confirm-body > .anticon) {
  color: #f44e4e;
}
</style>

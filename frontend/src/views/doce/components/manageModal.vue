<template>
  <a-modal
    :visible="visible"
    width="1180px"
    :title="currentData.documentName"
    :closable="true"
    :footer="null"
    @cancel="closeModal"
  >
    <div class="controller">
      <div class="filter float">
        <a-button type="primary" class="add iconcenter" @click="onAdd"
          ><PlusCircleOutlined class="ico_upload" />添加分段</a-button
        >
        <a-input-group class="search">
          <a-input
            @pressEnter="onSearch"
            v-model:value="params.keywords"
            :maxlength="20"
            placeholder="请输入"
          />
          <i class="ico_search bg" @click="onSearch"></i>
        </a-input-group>
      </div>
    </div>
    <div class="list">
      <div class="total">{{ pagination.total }}段落</div>
      <div class="content all" ref="scrollDom">
        <div
          class="item"
          v-for="(item, index) in list"
          :key="index"
          @click.stop="onTap(item)"
        >
          <div class="header float">
            <div class="sort left"><i class="ico_sort bg"></i>{{ item.id || 0 }}</div>
            <a-popconfirm
              title="请确认是否删除?"
              @confirm="onDelete(item)"
              ok-text="是"
              cancel-text="否"
            >
              <i class="ico_delete bg" @click.stop></i>
            </a-popconfirm>
          </div>
          <div class="desc more_line">{{ item.content || "暂无" }}</div>
        </div>
        <empty-data v-show="list.length == 0" />
      </div>
    </div>
  </a-modal>
  <manage-add-modal
    v-model:visible="manageModalVisible"
    :current-data="manageItemData"
    @update="onUpdate"
  />
</template>
<script setup>
import { PlusCircleOutlined } from "@ant-design/icons-vue";
import {
  inject,
  ref,
  reactive,
  onMounted,
  watch,
  computed,
  nextTick,
  onBeforeUnmount,
  provide,
  defineAsyncComponent,
} from "vue";
import { message, Modal } from "ant-design-vue";
import setting from "@/utils/setting";
import EmptyData from "@/components/EmptyData.vue";

import ManageAddModal from "./manageAddModal.vue";
import { throttle } from "@/utils/utils";
const axios = inject("axios");
const emit = defineEmits(["update:visible", "update"]);
const props = defineProps({
  visible: {
    type: Boolean,
    default: () => false,
  },
  currentData: {
    type: Object,
    default: {},
  },
});
//滚动dom实例
const scrollDom = ref(null);
const list = ref([]);
//分页
const pagination = reactive({
  pageSize: 50,
  pageNum: 1,
  total: 0,
  lock: false,
});
const params = reactive({
  keywords: "",
});
const manageItemData = reactive({});
const manageModalVisible = ref(false);
onMounted(() => {});

const onAdd = () => {
  Object.assign(manageItemData, {
    id: null,
    content: "",
    documentId: props.currentData.documentId,
  });
  manageModalVisible.value = true;
};

const onTap = (item) => {
  Object.assign(manageItemData, { ...item, documentId: props.currentData.documentId });
  manageModalVisible.value = true;
  console.log(manageItemData, manageModalVisible.value);
};

const onBack = () => {
  emit("update:visible", false);
};

const onUpdate = () => {
  resetList();
};

const onSearch = () => {
  resetList();
};

const resetList = () => {
  pagination.pageNum = 1;
  pagination.total = 0;
  getList();
};

const getList = async () => {
  try {
    let res = await axios.get("/api/documentEntity/page", {
      params: {
        ...params,
        ...pagination,
        documentId: props.currentData.documentId,
      },
    });
    let total = res.data.total || 0;
    let data = res.data.rows || [];
    if (pagination.pageNum == 1) {
      list.value = data;
    } else {
      list.value.push(...data);
    }
    pagination.total = total;
    pagination.pageNum += 1;
  } catch (err) {
  } finally {
  }
};

const onDelete = async (record) => {
  try {
    let res = await axios.post("/api/documentEntity/delete", {
      documentId: props.currentData.documentId,
      ids: [record.id],
    });
    resetList();
  } catch (err) {}
};

const scrollToBottom = () => {
  if ((pagination.pageNum - 1) * pagination.pageSize >= pagination.total) return;
  let scrollHeight = scrollDom.value.scrollHeight;
  let windowHeight = scrollDom.value.clientHeight;
  let scrollTop = scrollDom.value.scrollTop;
  let scrollBottom = scrollHeight - windowHeight - scrollTop;
  if (scrollBottom <= 100) {
    getList();
  }
};

const closeModal = () => {
  emit("update:visible", false);
};

watch(
  () => props.visible,
  (newVal, oldVal) => {
    list.value = [];
    params.keywords = "";
    pagination.pageNum = 1;
    pagination.total = 0;
    pagination.lock = false;
    if (!newVal) {
      scrollDom.value && scrollDom.value.removeEventListener("scroll", scrollToBottom);
    } else {
      getList();
      nextTick(() => {
        scrollDom.value.addEventListener("scroll", throttle(scrollToBottom, 100));
      });
    }
  },
  { deep: true, immediate: true }
);
</script>

<style scoped lang="less">
.controller {
  width: 100%;
  .add {
    width: 142px;
    height: 42px;
    background: #5657f6;
    border-radius: 6px;
    font-size: 16px;
    font-weight: 500;
    color: #ffffff;
    cursor: pointer;
    .ico_upload {
      font-size: 16px;
    }
  }
  .filter {
    .search {
      width: 185px;
      margin-right: 10px;

      :deep(.ant-input) {
        width: 100%;
        height: 42px;
        background-color: #fafafa;
        border-radius: 6px;
        border: none;
        color: #333;
        font-size: 16px;
        padding-left: 15px;
        &::placeholder {
          color: #999999;
        }
      }

      .ico_search {
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
        right: 16px;
        z-index: 2;
        width: 18px;
        height: 18px;
        background-image: url("../../../assets/images/ico_search1.png");
        cursor: pointer;
      }
    }
  }
}
.list {
  width: 100%;
  //   max-height: 470px;
  padding: 20px 0;
  background-color: #fafafa;
  border-radius: 6px;
  margin-top: 20px;

  .total {
    font-size: 16px;
    font-weight: 700;
    color: #3d3d3d;
    line-height: 23px;
    margin-bottom: 15px;
    padding-left: 20px;
  }
  .content {
    width: 100%;
    padding: 0px 0px 0px 20px;
    min-height: 197px;
    max-height: 410px;
    overflow-y: scroll;
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
  .item {
    width: calc(25% - 14px);
    height: 125px;
    margin-right: 14px;
    margin-bottom: 15px;
    background-color: #f5f6f8;
    padding: 20px 20px 0;
    border-radius: 6px;
    transition: all linear 0.3s;
    cursor: pointer;
    &:nth-of-type(4n) {
      margin-right: 0;
    }
    &:nth-last-of-type(1),
    &:nth-last-of-type(2),
    &:nth-last-of-type(3) {
      margin-bottom: 0;
    }
    .header {
      width: 100%;
      .sort {
        width: fit-content;
        max-width: 200px;
        height: 18px;
        padding: 0 4px;
        border-radius: 4px;
        border: 1px solid #dedede;
        font-size: 12px;
        font-weight: 400;
        color: #333333;
        line-height: 14px;
        overflow: hidden;
        .ico_sort {
          width: 10px;
          height: 10px;
          background-image: url(../../../assets/images/ico_sort.png);
          margin-right: 3px;
        }
      }
      .ico_delete {
        width: 18px;
        height: 18px;
        background-image: url(../../../assets/images/ico_del.png);
      }
    }
    .desc {
      font-size: 14px;
      font-weight: 400;
      color: #333333;
      line-height: 20px;
      margin-top: 16px;
      height: 40px;
    }
    &:hover {
      background-color: #ecedef;
      .desc {
        // color: #5657f6;
      }
    }
  }
}
</style>

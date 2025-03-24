<template>
  <div>
    <div class="search_box">
      <a-form :label-col="labelCol" ref="formRef" :model="formState" @finish="onFinish">
        <div ref="search_row">
          <a-row :gutter="10">
            <slot name="search" :form="formState" :expand="expand"></slot>
            <a-col :span="6">
              <a-button type="primary" style="margin: 0" html-type="submit"
                >搜索</a-button
              >
              <a-button type="text" style="color: #333333" @click="resetFields"
                >重置
                <template #icon>
                  <ReloadOutlined></ReloadOutlined>
                </template>
              </a-button>
              <a
                v-if="searchChildren > 6"
                style="font-size: 12px"
                @click="expand = !expand"
              >
                <template v-if="expand">
                  <UpOutlined />
                </template>
                <template v-else>
                  <DownOutlined />
                </template>
                {{ expand ? "收起" : "展开" }}
              </a>
            </a-col>
          </a-row>
        </div>
        <a-row>
          <a-col :span="12" style="text-align: left">
            <a-button type="primary" @click="onAdd"
              >添加
              <template #icon>
                <plus-outlined />
              </template>
            </a-button>
          </a-col>
          <a-col :span="12" style="text-align: right; display: none">
            <a-button type="primary" html-type="submit">搜索</a-button>
            <a-button style="margin: 0 8px" @click="resetFields">重置</a-button>
            <a
              v-if="searchChildren > 6"
              style="font-size: 12px"
              @click="expand = !expand"
            >
              <template v-if="expand">
                <UpOutlined />
              </template>
              <template v-else>
                <DownOutlined />
              </template>
              {{ expand ? "收起" : "展开" }}
            </a>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <a-table
      @change="handleTableChange"
      :pagination="pagination"
      v-bind="$attrs"
      :data-source="list"
      :loading="loading"
      class="doceTable"
    >
      <template v-slot:[item]="$scope" v-for="item in slotsData">
        <slot :name="item" v-bind="{ ...$scope }"></slot>
        <template
          v-if="
            $scope.column.key === 'action' &&
            $scope.column.customFunction &&
            $scope.column.customFunction($scope.record)
          "
        >
          <a-button
            type="link"
            style="margin: 0; padding: 0"
            size="small"
            @click="onEdit($scope.record)"
          >
            <div class="icon_edit"></div>
          </a-button>

          <a-popconfirm
            title="确认删除?"
            ok-text="确认"
            cancel-text="取消"
            @confirm="onDel($scope.record)"
          >
            <a-button style="margin: 0; padding: 0" type="link" danger size="small">
              <div class="icon_delete"></div>
            </a-button>
          </a-popconfirm>
        </template>
      </template>
    </a-table>
    <Model
      :edit-obj="editObj"
      :add-url="addUrl"
      :detail-url="detailUrl"
      :del-url="delUrl"
      :edit-url="editUrl"
      v-model:show="showModel"
      :rulesRef="$attrs.rules"
    >
      <template #form="{ form }">
        <slot name="form" :form="form"></slot>
      </template>
    </Model>
  </div>
</template>

<script setup>
import {
  DownOutlined,
  UpOutlined,
  PlusOutlined,
  ReloadOutlined,
  EditOutlined,
} from "@ant-design/icons-vue";
import { ref, useSlots, computed, reactive, onMounted, provide } from "vue";
import { useTableList, useDel } from "./usHooks";
import Model from "./model.vue";
defineOptions({ inheritAttrs: false });
const props = defineProps({
  url: {
    type: String,
    default: "",
  },
  addUrl: {
    type: String,
    default: "",
  },
  delUrl: {
    type: String,
    default: "",
  },
  detailUrl: {
    type: String,
    default: "",
  },
  editUrl: {
    type: String,
    default: "",
  },
  limit: {
    type: Number,
    default: 10,
  },
  labelCol: {
    type: Object,
    default: { style: { width: "100px" } },
  },
  editId: {
    type: Function,
    default: () => {},
  },
  canAdd: {
    type: String,
    default: "",
  },
  canEdit: {
    type: String,
    default: "",
  },
  canDel: {
    type: String,
    default: "",
  },
});
const slotsData = ref(Object.keys(useSlots()));
const search_row = ref();
const searchChildren = ref(0);
const formRef = ref();
const formState = reactive({});
const expand = ref(false);
const showModel = ref(false);
let editObj = ref({});
const rowSelection = ref([]);
let sendData = reactive({
  pageSize: props.limit,
  pageNum: 1,
  refresh: Math.random(),
});
// const list=ref([])
// const total = ref(0)
// const loading=ref(false)
let { list, total, loading } = useTableList(props.url, sendData);

const pagination = computed(() => ({
  pageSize: props.limit,
  current: sendData.pageNum,
  total: total.value,
  showSizeChanger: false,
}));
onMounted(() => {
  if (search_row) {
    let len = search_row.value.querySelector(".ant-row").children.length;
    searchChildren.value = len;
  }
});
const handleTableChange = (page, filters, sorter) => {
  sendData.pageNum = page.current;
};
const onFinish = (values) => {
  if (Object.keys(formState).length) {
    for (let key in formState) {
      sendData[key] = formState[key];
    }
  }
  console.log(sendData);
};
const resetFields = () => {
  if (Object.keys(formState).length) {
    for (let key in formState) {
      formState[key] = "";
      sendData[key] = "";
    }
  } else {
    sendData.refresh = Math.random();
  }
  // sendData.pageNum = 1;
};
const onEdit = async (row) => {
  editObj.value = props.editId(row);

  showModel.value = true;
};
const onAdd = () => {
  showModel.value = true;
};
const onDel = (row) => {
  let data = props.editId(row);
  useDel(props.delUrl, data).then(() => {
    sendData.refresh = Math.random();
  });
};

provide("refresh", () => {
  sendData.refresh = Math.random();
});
</script>

<style lang="less" scoped>
.search_box {
  padding: 0 0px;
  box-sizing: border-box;
  background-color: transparent;
  margin-bottom: 0px;
  :deep(.ant-btn) {
    // width: 80px;
    height: 42px;
    // background-color: #5657f6;
    border-radius: 6px;
    text-align: center;
    cursor: pointer;
    font-size: 16px;
    font-weight: 500;
    color: #ffffff;
    margin-right: 11px;
  }
}
.doceTable {
  margin-top: 18px;
  .icon_delete {
    width: 18px;
    height: 18px;
    background-image: url(/src/assets/images/ico_del.png);
    cursor: pointer;
    background-size: 100% 100%;
  }
  .icon_edit {
    width: 18px;
    height: 18px;
    background-image: url(/src/assets/images/ico_edit.png);
    margin-right: 10px;
    cursor: pointer;
    background-size: 100% 100%;
  }
  :deep(.ant-table) {
    .ant-table-thead {
      .ant-table-cell {
        background-color: #e8e9eb;
      }
    }
  }
}
</style>

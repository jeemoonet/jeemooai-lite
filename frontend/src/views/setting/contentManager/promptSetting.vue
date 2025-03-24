<template>
  <a-row class="filter">
    <a-col :span="6">
      <a-input v-model:value="form.promptName" placeholder="请输入提示器名称" />
    </a-col>
    <a-col :span="6">
      <a-select v-model:value="form.isPublic" placeholder="是否公开">
        <a-select-option value="1">公开</a-select-option>
        <a-select-option value="0">不公开</a-select-option>
      </a-select>
    </a-col>
    <a-col :span="6">
      <a-select v-model:value="form.status" placeholder="请选择状态">
        <a-select-option value="1">上架</a-select-option>
        <a-select-option value="0">下架</a-select-option>
      </a-select>
    </a-col>
    <a-col :span="6">
      <a-select v-model:value="form.isShare" placeholder="是否分享">
        <a-select-option value="1">是</a-select-option>
        <a-select-option value="0">否</a-select-option>
      </a-select>
    </a-col>
    <a-col :span="6">
      <a-input v-model:value="form.createBy" placeholder="请输入创建人" />
    </a-col>
    <a-col :span="6">
      <a-space>
        <div class="search" @click="onSearch">搜索</div>
        <div class="reset left" @click="onReset"><i class="ico_reset bg"></i>重置</div>
      </a-space>
    </a-col>
  </a-row>

  <a-table
    :dataSource="dataSource"
    :columns="columns"
    :pagination="pagination"
    @change="handleTableChange"
    class="doceTable"
  >
    <template #bodyCell="{ column, record }">
      <template v-if="column.key === 'promptIcon'">
        <img
          :style="{ width: '30px' }"
          :src="record.promptIcon"
          class="icon_template"
          alt=""
        />
      </template>
      <template v-if="column.key === 'isRecommend'">
        {{ record.isRecommend == 1 ? "是" : "否" }}
      </template>
      <template v-if="column.key === 'isPublic'">
        {{ record.isPublic == 1 ? "是" : "否" }}
      </template>
      <template v-if="column.key === 'isShare'">
        {{ record.isShare == 1 ? "是" : "否" }}
      </template>
      <template v-if="column.key === 'status'">
        <a-switch
          @click="onStatusChange(record)"
          :checked="record.status == 1 ? true : false"
        />
      </template>
      <template v-if="column.key === 'action'">
        <div class="iconcenter">
          <i class="ico_edit bg" @click="onEdit(record)"></i>
          <a-popconfirm
            v-if="dataSource.length"
            title="请确认是否删除?"
            @confirm="onDelete(record)"
            ok-text="是"
            cancel-text="否"
          >
            <i class="ico_delete bg"></i>
          </a-popconfirm>
        </div>
      </template>
    </template>
  </a-table>
</template>
<script setup>
import { inject, ref, onMounted, watch, computed, nextTick } from "vue";
import { message } from "ant-design-vue";
import setting from "@/utils/setting";
import { useRouter } from "vue-router";
import { PromptStore } from "@/stores/prompt";
import { storeToRefs } from "pinia";
//获取store slider数据
const promptStore = PromptStore();
const router = useRouter();
const axios = inject("axios");
const columns = [
  {
    title: "id",
    dataIndex: "promptId",
    key: "promptId",
  },
  {
    title: "图标",
    dataIndex: "promptIcon",
    key: "promptIcon",
  },
  {
    title: "名称",
    dataIndex: "promptName",
    key: "promptName",
  },
  {
    title: "角色设置",
    dataIndex: "initPrompt",
    key: "initPrompt",
    ellipsis: true,
  },
  {
    title: "是否推荐",
    dataIndex: "isRecommend",
    key: "isRecommend",
  },
  {
    title: "是否内部公开",
    dataIndex: "isPublic",
    key: "isPublic",
  },
  {
    title: "是否外部分享",
    dataIndex: "isShare",
    key: "isShare",
  },
  {
    title: "上/下架",
    dataIndex: "status",
    key: "status",
  },
  {
    title: "创建人",
    dataIndex: "createBy",
    key: "createBy",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
    key: "createTime",
  },
  {
    title: "操作",
    key: "action",
    align: "center",
  },
];
//数据源
const dataSource = ref([]);
//当前页
const current = ref(1);
//总数
const total = ref(0);
const limit = 10;
const pagination = computed(() => ({
  pageSize: limit,
  current: current.value,
  total: total.value,
  showSizeChanger: false,
}));
//检索
const initForm = () => {
  return {
    promptName: "",
    isPublic: null,
    isShare: null,
    status: null,
    createBy: "",
  };
};
const form = ref(initForm());
const visible = ref(false);
onMounted(() => {
  tableList();
});

const handleTableChange = (page, filters, sorter) => {
  current.value = page.current;
  nextTick(() => {
    tableList();
  });
};

const onReset = () => {
  form.value = initForm();
  current.value = 1;
  nextTick(() => {
    tableList();
  });
};

const onSearch = () => {
  current.value = 1;
  nextTick(() => {
    tableList();
  });
};

const onAdd = () => {
  // editData.value = {};
  nextTick(() => {
    visible.value = true;
  });
};

const onEdit = async (record) => {
  const href = router.resolve({
    path: "/prompt/detail/" + record.promptId,
  });
  window.open(href.href, "_blank");
};

const onDelete = async (record) => {
  let res = await axios.delete("/api/prompt", {
    params: {
      promptId: record.promptId,
    },
  });
  tableList();
};

const onStatusChange = async (record = {}) => {
  let res = await axios.post("/api/prompt/status", {
    promptId: record.promptId,
    status: record.status == 0 ? 1 : 0,
  });
  tableList();
};

const tableList = async () => {
  try {
    let res = await axios.get("/api/company/promptPage", {
      params: {
        pageSize: limit,
        pageNum: current.value,
        promptName: form.value.promptName || "",
        isPublic: form.value.isPublic || "",
        isShare: form.value.isShare || null,
        status: form.value.status || "",
        createBy: form.value.createBy || "",
      },
    });
    let data = res.data.rows || [];
    dataSource.value = data;
    total.value = res.data.total || 0;
  } catch (err) {}
};
</script>

<style scoped lang="less">
.filter {
  width: 100%;
  .ant-col {
    margin-bottom: 10px;
  }
  :deep(.ant-input) {
    width: 90%;
    height: 42px;
    background-color: #fff;
    border-radius: 6px;
    border: none;
    color: #333;
    font-size: 14px;
    padding-left: 15px;
    margin-right: 5px;
    &::placeholder {
      color: #999999;
    }
  }
  :deep(.ant-select) {
    width: 90%;
    height: 42px;
    background-color: #fff;
    border-radius: 6px;
    border: none;
    color: #333;
    font-size: 14px;
    line-height: 42px;
    margin-right: 5px;
    .ant-select-selector {
      background-color: transparent;
      border: none;
      height: 100%;
      line-height: 42px;
    }
    .ant-select-selection-placeholder,
    .ant-select-selection-item {
      line-height: 42px;
    }
  }
  .search {
    width: 80px;
    height: 42px;
    background-color: #5657f6;
    border-radius: 6px;
    text-align: center;
    line-height: 42px;
    cursor: pointer;
    font-size: 16px;
    font-weight: 500;
    color: #ffffff;
    margin-right: 11px;
  }
  .reset {
    height: 42px;
    cursor: pointer;
    font-size: 16px;
    font-weight: 400;
    color: #333333;
    line-height: 23px;
    .ico_reset {
      width: 18px;
      height: 18px;
      background-image: url(../../../assets/images/ico_reset.png);
      margin-right: 5px;
    }
  }
}
.controller {
  width: 100%;
  margin-top: 17px;
  .add {
    width: 142px;
    height: 42px;
    background-color: #5657f6;
    border-radius: 6px;
    font-size: 16px;
    font-weight: 500;
    color: #ffffff;
    cursor: pointer;
    .ico_add {
      width: 18px;
      height: 18px;
      background-image: url(../../../assets/images/ico_add.png);
      margin-right: 5px;
    }
  }
}

.doceTable {
  margin-top: 18px;
  :deep(.ant-table) {
    .ant-table-thead {
      .ant-table-cell {
        background-color: #e8e9eb;
      }
    }
  }
  :deep(.ant-switch-checked) {
    background-color: #5657f6;
  }
}
.ico_delete {
  width: 18px;
  height: 18px;
  background-image: url(../../../assets/images/ico_del.png);
  cursor: pointer;
}
.ico_edit {
  width: 18px;
  height: 18px;
  background-image: url(../../../assets/images/ico_edit.png);
  margin-right: 10px;
  cursor: pointer;
}
</style>

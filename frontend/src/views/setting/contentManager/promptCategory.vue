<template>
  <div class="controller float">
    <div class="add iconcenter" @click="onAdd">
      <i class="ico_add bg"></i>
      新增分类
    </div>
    <div class="filter right">
      <a-input-group class="search">
        <a-input
          @pressEnter="onSearch"
          v-model:value="keywords"
          :maxlength="20"
          placeholder="请输入关键词搜索"
        />
        <i class="ico_search bg" @click="onSearch"></i>
      </a-input-group>
    </div>
  </div>
  <a-table
    :dataSource="dataSource"
    :columns="columns"
    :pagination="pagination"
    @change="handleTableChange"
    class="doceTable"
  >
    <template #bodyCell="{ column, record }">
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
  <modal-prompt-category
    v-model:visible="visible"
    :promptData="promptData"
    @update="onSearch"
  />
</template>
<script setup>
import { inject, ref, onMounted, watch, computed, nextTick } from "vue";
import { message } from "ant-design-vue";
import setting from "@/utils/setting";
import { useRouter } from "vue-router";
import ModalPromptCategory from "./components/ModalPromptCategory.vue";
const router = useRouter();
const axios = inject("axios");
const columns = [
  {
    title: "助手分类名称",
    dataIndex: "categoryName",
    key: "categoryName",
  },
  {
    title: "排序号",
    dataIndex: "sortNum",
    key: "sortNum",
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
const keywords = ref("");
//内容
const promptData = ref({});
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

const onSearch = () => {
  current.value = 1;
  nextTick(() => {
    tableList();
  });
};

const onAdd = () => {
  promptData.value = {};
  nextTick(() => {
    visible.value = true;
  });
};

const onEdit = async (record) => {
  promptData.value = record;
  nextTick(() => {
    visible.value = true;
  });
};

const onDelete = async (record) => {
  let res = await axios.delete("/api/promptCategory", {
    params: {
      categoryId: record.categoryId,
    },
  });
  tableList();
};

const tableList = async () => {
  try {
    let res = await axios.get("/api/promptCategory/list", {
      params: {
        pageSize: limit,
        pageNum: current.value,
        keywords: keywords.value || "",
      },
    });
    let data = res.data.rows || [];
    dataSource.value = data;
    total.value = res.data.total || 0;
  } catch (err) {}
};
</script>

<style scoped lang="less">
.controller {
  width: 100%;
  // margin-top: 22px;
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
  .filter {
    .search {
      width: 185px;
      margin-left: 5px;

      :deep(.ant-input) {
        width: 100%;
        height: 42px;
        background-color: #fff;
        border-radius: 6px;
        border: none;
        color: #333;
        font-size: 14px;
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
        background-image: url("../../../assets/images/ico_search.png");
        cursor: pointer;
      }
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

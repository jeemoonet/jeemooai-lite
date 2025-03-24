<template>
  <div class="filter left">
    <a-input v-model:value="form.nickName" :maxlength="20" placeholder="请输入用户名称" />
    <a-input v-model:value="form.userName" :maxlength="20" placeholder="请输入手机号码" />
    <a-select v-model:value="form.status" placeholder="请选择状态">
      <a-select-option value="0">正常</a-select-option>
      <a-select-option value="1">禁用</a-select-option>
    </a-select>
    <div class="search" @click="onSearch">搜索</div>
    <div class="reset left" @click="onReset"><i class="ico_reset bg"></i>重置</div>
  </div>
  <div class="controller float">
    <div class="add iconcenter" @click="onAdd">
      <i class="ico_add bg"></i>
      新增员工
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
      <template v-if="column.key === 'roles'">
        <a-tag v-for="(item, index) in record.roles" :key="index">{{
          item.roleName
        }}</a-tag>
      </template>
      <template v-if="column.key === 'posts'">
        <a-tag v-for="(item, index) in record.posts" :key="index">{{
          item.postName
        }}</a-tag>
      </template>
      <template v-if="column.key === 'status'">
        <a-switch
          @click="onStatusChange(record)"
          :checked="record.status == 0 ? true : false"
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
  <modal-user v-model:visible="visible" :editData="editData" @update="onSearch" />
</template>
<script setup>
import { inject, ref, onMounted, watch, computed, nextTick } from "vue";
import { message } from "ant-design-vue";
import setting from "@/utils/setting";
import { useRouter } from "vue-router";
import ModalUser from "./components/ModalUser.vue";
const router = useRouter();
const axios = inject("axios");
const columns = [
  {
    title: "姓名",
    dataIndex: "nickName",
    key: "nickName",
  },
  {
    title: "手机号码",
    dataIndex: "userName",
    key: "userName",
  },
  {
    title: "状态",
    dataIndex: "status",
    key: "status",
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
    nickName: "",
    userName: "",
    status: null,
  };
};
const form = ref(initForm());
//内容
const editData = ref({});
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
  editData.value = {};
  nextTick(() => {
    visible.value = true;
  });
};

const onEdit = async (record) => {
  editData.value = record;
  nextTick(() => {
    visible.value = true;
  });
};

const onDelete = async (record) => {
  let res = await axios.delete("/api/user", {
    params: {
      userId: record.userId,
    },
  });
  tableList();
};

const onStatusChange = async (record = {}) => {
  let res = await axios.post("/api/user/changeStatus", {
    userId: record.userId,
    status: record.status == 0 ? 1 : 0,
  });
  tableList();
};

const tableList = async () => {
  try {
    let res = await axios.get("/api/user/list", {
      params: {
        pageSize: limit,
        pageNum: current.value,
        nickName: form.value.nickName || "",
        userName: form.value.userName || "",
        status: form.value.status || "",
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
  :deep(.ant-input) {
    width: 228px;
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
    width: 228px;
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

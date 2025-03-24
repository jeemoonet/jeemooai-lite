<template>
  <!-- <a-button class="reset-btn" type="primary" @click="onReset">重置</a-button> -->
  <a-table
    :dataSource="dataSource"
    :columns="columns"
    :pagination="false"
    class="doceTable"
    :scroll="{ y: 445 }"
  >
    <template #bodyCell="{ column, record }">
      <template v-if="column.key === 'score'">
        <span>{{ (Number(record.score) * 100).toFixed(6) }}%</span>
      </template></template
    >
  </a-table>
</template>
<script setup>
import {
  inject,
  ref,
  reactive,
  onMounted,
  watch,
  computed,
  nextTick,
  onBeforeUnmount,
} from "vue";
import { useRouter } from "vue-router";
const router = useRouter();
const axios = inject("axios");
const tab = inject("tab");
const columns = [
  {
    title: "文档名称",
    dataIndex: "documentName",
    key: "documentName",
  },
  {
    title: "切片序号",
    dataIndex: "id",
    key: "id",
  },
  {
    title: "CHUNK内容",
    dataIndex: "content",
    key: "content",
    ellipsis: true,
  },
  {
    title: "相似度",
    dataIndex: "score",
    key: "score",
  },
];
//数据源
const dataSource = inject("tableList");

const onReset = () => {
  dataSource.value = [];
};

onMounted(() => {});

onBeforeUnmount(() => {});
</script>

<style scoped lang="less">
.reset-btn {
  width: 125px;
  height: 46px;
  font-size: 16px;
  border-radius: 6px;
  margin-top: 10px;
}
.doceTable {
  // max-height: 490px;
  // overflow-y: auto;
  // margin-top: 18px;
  :deep(.ant-table) {
    .ant-table-thead {
      .ant-table-cell {
        background-color: transparent;
      }
    }
  }
}
</style>

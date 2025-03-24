<template>
  <a-modal
    :visible="visible"
    width="1180px"
    title="命中测试"
    :closable="true"
    :footer="null"
    @cancel="closeModal"
  >
    <a-row :gutter="24">
      <a-col :span="10">
        <testing-form />
      </a-col>
      <a-col :span="14">
        <testing-table />
      </a-col>
    </a-row>
  </a-modal>
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
  provide,
  defineAsyncComponent,
} from "vue";
import TestingForm from "./testingForm.vue";
import TestingTable from "./testingTable.vue";
const tab = ref("form");
const axios = inject("axios");
const emit = defineEmits(["update:visible"]);
const tableList = ref([]);
provide("tab", tab);
provide("tableList", tableList);
onMounted(() => {});

const closeModal = () => {
  emit("update:visible", false);
};

onBeforeUnmount(() => {});
</script>

<style scoped lang="less">
.controller {
  width: 100%;
  margin-top: 22px;
  .add {
    width: 142px;
    height: 42px;
    background: #5657f6;
    border-radius: 6px;
    font-size: 16px;
    font-weight: 500;
    color: #ffffff;
    cursor: pointer;
    .ico_add {
      width: 18px;
      height: 18px;
      background-image: url(../../assets/images/ico_add.png);
      margin-right: 5px;
    }
  }
  .filter {
    .search {
      width: 185px;
      margin-right: 10px;

      :deep(.ant-input) {
        width: 100%;
        height: 42px;
        background-color: #fff;
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
        background-image: url("../../assets/images/ico_search.png");
        cursor: pointer;
      }
    }
  }
  .header {
    font-size: 26px;
    font-weight: 700;
    color: #333333;
    line-height: 1;
    letter-spacing: 1px;
    cursor: pointer;
    .ico_back {
      width: 28px;
      height: 28px;
      background-image: url(../../assets/images/ico_back.png);
      margin-right: 10px;
    }
    .sub-title {
      display: flex;
      align-items: flex-end;
      justify-content: flex-start;
      font-weight: 700;
    }
    span {
      font-size: 16px;
      font-weight: 400;
      color: #333333;
      line-height: 1;
      margin-left: 12px;
    }
  }
}
</style>

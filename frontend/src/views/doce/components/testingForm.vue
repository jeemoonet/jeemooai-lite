<template>
  <a-form class="form" ref="formRef" :model="form" layout="vertical" name="form_in_modal">
    <a-row :gutter="24" class="form_item">
      <a-col :span="24">
        <a-form-item
          name="searchType"
          label="搜索策略"
          :rules="[{ required: true, message: '请选择搜索策略' }]"
        >
          <a-select ref="select" v-model:value="form.searchType" placeholder="请选择">
            <a-select-option
              v-for="(item, index) in searchTypeList"
              :key="index"
              :value="item.value"
              >{{ item.label }}</a-select-option
            >
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :span="24">
        <a-form-item name="topK" label="返回范围">
          <a-input-number
            v-model:value="form.topK"
            placeholder="返回范围1~10"
            max="10"
            min="1"
            :controls="false"
            style="width: 100%"
          />
        </a-form-item>
      </a-col>
      <a-col :span="24">
        <a-form-item name="score" label="最小相似度">
          <a-input-number
            v-model:value="form.score"
            placeholder="最小相似度范围0.01~0.99"
            :max="0.99"
            :min="0.01"
            :step="0.01"
            :controls="false"
            style="width: 100%"
          />
        </a-form-item>
      </a-col>
      <a-col :span="24">
        <a-form-item
          name="categoryIds"
          label="可见数据范围"
          :rules="[{ required: true, message: '请选择可见数据范围' }]"
        >
          <a-tree-select
            class=""
            v-model:value="form.categoryIds"
            :treeDefaultExpandedKeys="[1, 2]"
            :tree-data="docCategoryList"
            :multiple="true"
            allow-clear
            :show-checked-strategy="SHOW_PARENT"
            placeholder="请选择可见数据范围"
            :fieldNames="{
              label: 'categoryName',
              value: 'categoryId',
            }"
          />
        </a-form-item>
      </a-col>
      <a-col :span="24">
        <a-form-item
          name="search"
          label="查询文本"
          :rules="[{ required: true, message: '请输入需要查询测试的文本' }]"
        >
          <a-textarea
            v-model:value="form.search"
            placeholder="请输入需要查询测试的文本"
            :autoSize="{ minRows: 3, maxRows: 3 }"
            row="3"
          />
        </a-form-item>
      </a-col>
    </a-row>
    <a-row justify="end">
      <a-col>
        <a-button class="submit-btn" style="margin-right: 8px" @click="onReset"
          >重置</a-button
        >
        <a-button
          class="submit-btn"
          type="primary"
          :loading="confirmLoading"
          @click="onSubmit"
          >查询</a-button
        >
      </a-col>
    </a-row>
  </a-form>
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
const dataSource = inject("tableList");
const tab = inject("tab");
const searchTypeList = [
  {
    label: "语义检索",
    value: "vector_search",
  },
  {
    label: "全文检索",
    value: "full_text_search",
  },
  {
    label: "混合检索",
    value: "hybrid_search",
  },
];
const formRef = ref(null);
const form = ref({
  search: "",
  categoryIds: [],
  topK: "",
  score: "",
  searchType: "vector_search",
});
const confirmLoading = ref(false);
const docCategoryList = ref([]);

onMounted(() => {
  getDocCategoryList();
});

const onReset = () => {
  formRef.value.resetFields();
};

const onSubmit = () => {
  formRef.value
    .validateFields()
    .then((values) => {
      submit();
    })
    .catch((info) => {});
};

const submit = async () => {
  confirmLoading.value = true;
  try {
    let res = await axios.post("/api/document/search", {
      ...form.value,
    });

    dataSource.value = res.data.data || [];
  } catch (err) {
  } finally {
    confirmLoading.value = false;
  }
};

//获取知识库
const getDocCategoryList = async (res) => {
  try {
    let res = await axios.get("/api/documentCategory/categoryList");
    let data = res.data.data || [];
    docCategoryList.value = data;
  } catch (err) {}
};

onBeforeUnmount(() => {});
</script>

<style scoped lang="less">
.form {
  width: 100%;
  // padding: 22px 30px;
  background-color: #fff;
  // border-radius: 6px;
  // margin-top: 20px;
}

.ant-form-item {
  margin-bottom: 18px;
}

.submit-btn {
  width: 125px;
  height: 46px;
  font-size: 16px;
  border-radius: 6px;
}
</style>

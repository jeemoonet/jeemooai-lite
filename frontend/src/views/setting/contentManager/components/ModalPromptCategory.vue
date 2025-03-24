<template>
  <a-modal
    :visible="visible"
    :width="522"
    title="助手分类"
    :closable="true"
    wrapClassName="documentCategoryDialog"
    cancelText="取消"
    okText="保存"
    @cancel="closeModal"
    @ok="submit"
    :confirmLoading="confirmLoading"
  >
    <a-form ref="formRef" :model="form" layout="vertical" name="form_in_modal">
      <a-form-item
        name="sortNum"
        label="排序"
        :rules="[{ required: true, message: '请输入序号' }]"
      >
        <a-input-number
          v-model:value="form.sortNum"
          placeholder="请输入"
          style="width: 100%"
          :min="0"
        />
      </a-form-item>
      <a-form-item
        name="categoryName"
        label="分类名称"
        :rules="[{ required: true, message: '请输入分类名称' }]"
      >
        <a-input v-model:value="form.categoryName" placeholder="请输入" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive, defineProps, defineEmits, watch, inject } from "vue";
const axios = inject("axios");
const emit = defineEmits(["update:visible", "update"]);
const props = defineProps({
  visible: {
    type: Boolean,
    default: () => false,
  },
  promptData: {
    type: Object,
    default: () => {},
  },
});
const formRef = ref(null);
const confirmLoading = ref(false);
const initData = () => {
  return {
    categoryName: "",
    categoryIcon: "",
    categoryId: "",
    sortNum: 0,
  };
};
const form = ref(initData());

const submit = () => {
  formRef.value
    .validateFields()
    .then((values) => {
      if (form.value.categoryId) {
        editSubmit();
      } else {
        addSubmit();
      }
    })
    .catch((info) => {});
};

const addSubmit = async () => {
  confirmLoading.value = true;
  try {
    let res = await axios.post(`/api/promptCategory/create`, {
      categoryName: form.value.categoryName || "",
      categoryIcon: "",
      sortNum: form.value.sortNum || 0,
    });
    formRef.value.resetFields();
    emit("update");
    closeModal();
  } catch (err) {
  } finally {
    confirmLoading.value = false;
  }
};

const editSubmit = async () => {
  confirmLoading.value = true;
  try {
    let res = await axios.post(`/api/promptCategory/update`, {
      categoryName: form.value.categoryName || "",
      categoryIcon: "",
      categoryId: form.value.categoryId || "",
      sortNum: form.value.sortNum || 0,
    });
    formRef.value.resetFields();
    emit("update");
    closeModal();
  } catch (err) {
  } finally {
    confirmLoading.value = false;
  }
};

const closeModal = () => {
  emit("update:visible", false);
};

watch(
  () => props.visible,
  (newVal, oldVal) => {
    if (newVal) {
      if (props.promptData && props.promptData.categoryId) {
        form.value = Object.assign({}, props.promptData);
      } else {
        form.value = initData();
      }
    }
  },
  { deep: true, immediate: true }
);
</script>

<style lang="less" scoped>
.modal_content {
  height: 322px;
  overflow-y: auto;
  font-size: 16px;
  font-weight: 400;
  color: #333333;
  line-height: 23px;
}
</style>

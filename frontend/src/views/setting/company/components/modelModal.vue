<template>
  <a-modal
    :visible="visible"
    width="690px"
    title="模型管理"
    :closable="true"
    cancelText="取消"
    okText="保存"
    @cancel="closeModal"
    @ok="submit"
    :confirmLoading="confirmLoading"
  >
    <a-form
      ref="formRef"
      :model="form"
      :rules="rules"
      name="form_in_modal"
      layout="horizontal"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 18 }"
    >
      <a-row :gutter="24">
        <a-col :span="24">
          <a-form-item name="modelName" label="展示名称	">
            <a-input v-model:value="form.modelName" placeholder="请输入展示名称	" />
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item name="modelLabel" label="模型名">
            <a-input
              v-model:value="form.modelLabel"
              placeholder="请输入模型名"
              style="width: 100%"
            /> </a-form-item
        ></a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>
<script setup>
import { message } from "ant-design-vue";
import { ref, reactive, onMounted, defineProps, defineEmits, watch, inject } from "vue";
const axios = inject("axios");
const emit = defineEmits(["update:visible", "update"]);
const props = defineProps({
  visible: {
    type: Boolean,
    default: () => false,
  },
  editData: {
    type: Object,
    default: () => {},
  },
});

const formRef = ref(null);
const confirmLoading = ref(false);
const initData = () => {
  return {
    id: "",
    modelName: "",
    modelLabel: "",
  };
};
const form = ref(initData());
const rules = {
  modelName: [{ required: true, message: "请输入展示名称" }],
  modelLabel: [{ required: true, message: "请选择模型名" }],
};
const platformList = ref([]);

onMounted(() => {});

const submit = () => {
  formRef.value
    .validateFields()
    .then((values) => {
      if (form.value.id) {
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
    let res = await axios.post(`/api/init/models/create`, {
      ...form.value,
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
    let res = await axios.post(`/api/init/models/update`, {
      ...form.value,
      password: form.value.password || null,
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
  formRef.value.resetFields();
  emit("update:visible", false);
};

watch(
  () => props.visible,
  (newVal, oldVal) => {
    if (newVal == true) {
      if (props.editData && props.editData.id) {
        form.value = Object.assign({}, props.editData);
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

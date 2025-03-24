<template>
  <a-modal
    :visible="visible"
    width="522px"
    title="添加分段"
    :closable="true"
    cancelText="取消"
    okText="保存"
    @cancel="closeModal"
    @ok="submit"
    wrapClassName="manageDialog"
    :confirmLoading="confirmLoading"
  >
    <a-form
      ref="formRef"
      :model="form"
      :rules="rules"
      name="form_in_modal"
      layout="horizontal"
      :label-col="{ span: 0 }"
      :wrapper-col="{ span: 24 }"
    >
      <a-row :gutter="24">
        <a-col :span="24">
          <a-form-item name="content" label="">
            <a-textarea v-model:value="form.content" placeholder="请输入" :rows="8" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>
<script setup>
import { message } from "ant-design-vue";
import {
  ref,
  reactive,
  onMounted,
  defineProps,
  defineEmits,
  watch,
  inject,
  toRaw,
} from "vue";
const axios = inject("axios");
const emit = defineEmits(["update:visible"]);
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

const formRef = ref(null);
const confirmLoading = ref(false);

const form = reactive({});
const rules = {
  content: [{ required: true, message: "请输入分段" }],
};

onMounted(() => {});

const submit = () => {
  formRef.value
    .validateFields()
    .then((values) => {
      if (form.id) {
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
    let res = await axios.post(`/api/documentEntity/create`, {
      ...form,
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
    let res = await axios.post(`/api/documentEntity/update`, {
      ...form,
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
    if (newVal) {
      Object.assign(form, toRaw(props.currentData));
    }
  },
  { deep: true, immediate: true }
);
</script>

<style lang="less" scoped>
:global(.manageDialog .ant-modal-body) {
  padding-bottom: 0;
}
</style>

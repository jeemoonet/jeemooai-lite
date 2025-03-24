<template>
  <a-modal
    :visible="visible"
    width="690px"
    title="向量切分设置"
    :closable="true"
    cancelText="取消"
    okText="重新索引"
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
        <a-col :span="20">
          <a-form-item name="id" label="切分策略">
            <a-select v-model:value="form.id" placeholder="请选择" @change="onIdChange">
              <a-select-option
                v-for="(item, index) in splitList"
                :key="index"
                :value="item.id"
                >{{ item.name }}</a-select-option
              >
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="20">
          <a-form-item name="splitChar" label="分割符">
            <a-input v-model:value="form.splitChar" placeholder="请输入" />
          </a-form-item>
        </a-col>
        <a-col :span="20">
          <a-form-item name="length" label="切分长度">
            <template #extra>
              提醒: 减少切分长度，应适当增加工作助手中大模型参数:
              向量返回数量。如1024推荐设置返回数量为4，512设置6，256设置12，128设置20等，以此类推
            </template>
            <a-select v-model:value="form.length" placeholder="请选择">
              <a-select-option
                v-for="(item, index) in lengthSelectList"
                :key="index"
                :value="item"
                >{{ item }}</a-select-option
              >
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>
<script setup>
import { message } from "ant-design-vue";
import { ref, reactive, onMounted, defineProps, defineEmits, watch, inject } from "vue";
const axios = inject("axios");
const emit = defineEmits(["update:visible"]);
const props = defineProps({
  visible: {
    type: Boolean,
    default: () => false,
  },
  id: {
    type: String,
    default: () => "",
  },
});

const formRef = ref(null);
const confirmLoading = ref(false);
const initData = () => {
  return {
    id: null,
    splitChar: "",
    length: null,
    name: "",
  };
};
const form = ref(initData());
const rules = {
  splitChar: [{ required: true, message: "请输入分隔符" }],
  length: [{ required: true, message: "请选择切分长度" }],
};
const splitList = ref([]);
const lengthSelectList = ref([]);

onMounted(() => {
  getSplitList();
});

const onIdChange = (value, option) => {
  let item = splitList.value.find((item) => item.id == value);
  form.value = Object.assign(form.value, item);
};

const getUserData = async (id) => {
  try {
    let res = await axios.get(`/api/document/splitInfo`, {
      params: {
        documentId: id,
      },
    });
    let data = res.data.data || {};
    form.value = Object.assign({}, data);
  } catch (err) {
  } finally {
  }
};

const submit = () => {
  formRef.value
    .validateFields()
    .then((values) => {
      editSubmit();
    })
    .catch((info) => {});
};

const editSubmit = async () => {
  confirmLoading.value = true;
  try {
    let res = await axios.post(`/api/document/split`, {
      ...form.value,
      documentId: props.id,
    });
    formRef.value.resetFields();
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

const getSplitList = async () => {
  try {
    let res = await axios.get(`/api/document/splitList`);
    let data = res.data.data || {};
    splitList.value = data.split || [];
    lengthSelectList.value = data.lengthSelect || [];
  } catch (err) {
  } finally {
  }
};

watch(
  () => props.visible,
  (newVal, oldVal) => {
    if (newVal) {
      getUserData(props.id);
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

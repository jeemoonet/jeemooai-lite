<template>
  <a-modal
    :visible="visible"
    :width="522"
    title="知识库分类"
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
        name="categoryName"
        label="文件夹名称"
        :rules="[{ required: true, message: '请输入文件夹名称' }]"
      >
        <a-input v-model:value="form.categoryName" placeholder="请输入" />
      </a-form-item>
      <a-form-item name="roleIds" label="可见设置" v-if="treeSelectData.type == 2">
        <a-select
          v-model:value="form.roleIds"
          mode="multiple"
          style="width: 100%"
          placeholder="请选择"
          :options="options"
        >
        </a-select>
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
  treeSelectData: {
    type: Object,
    default: () => {},
  },
  list: {
    type: Array,
    default: () => [],
  },
});
const formRef = ref(null);
const confirmLoading = ref(false);
const initData = () => {
  return {
    categoryIcon: "",
    categoryName: "",
    parentId: null,
    categoryId: "",
    roleIds: [],
  };
};
const form = ref(initData());
const options = ref([]);

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
    let res = await axios.post(
      props.treeSelectData.type == 1
        ? "/api/myDocumentCategory/create"
        : `/api/documentCategory/create`,
      {
        ...form.value,
      }
    );
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
    let res = await axios.post(
      props.treeSelectData.type == 1
        ? "/api/myDocumentCategory/update"
        : `/api/documentCategory/update`,
      {
        ...form.value,
      }
    );
    formRef.value.resetFields();
    emit("update");
    closeModal();
  } catch (err) {
  } finally {
    confirmLoading.value = false;
  }
};

const onSetRole = (id = "") => {
  axios
    .get("/api/documentCategory/role", {
      params: {
        categoryId: id,
      },
    })
    .then((res) => {
      options.value = res.data.data.list.map((item) => {
        return {
          value: item.roleId,
          label: item.roleName,
        };
      });
      if (res.data.data.selected) {
        form.value.roleIds = res.data.data.selected.map((item) => {
          return item.roleId;
        });
      }
    });
};

const closeModal = () => {
  emit("update:visible", false);
};

watch(
  () => props.visible,
  (newVal, oldVal) => {
    if (newVal) {
      if (props.treeSelectData && props.treeSelectData.categoryId) {
        form.value = Object.assign({}, props.treeSelectData);
      } else {
        form.value = initData();
        form.value.parentId = props.treeSelectData.parentId;
      }
      if (props.treeSelectData.type == 2) {
        onSetRole(props.treeSelectData.categoryId || "");
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

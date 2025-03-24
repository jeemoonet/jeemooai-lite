<template>
  <a-modal
    v-model:visible="visible"
    v-bind="$attrs"
    @cancel="onCancel"
    :confirmLoading="subLoading"
    :title="isEdit ? '编辑' : '创建'"
  >
    <a-form
      :model="formState"
      name="basic"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 16 }"
      autocomplete="off"
      ref="formBox"
    >
      <slot
        name="form"
        :form="{ form: formState, validateInfos, others: editObj }"
      ></slot>
    </a-form>
    <template #footer>
      <a-button key="back" @click="onCancel">取消</a-button>
      <a-button key="submit" type="primary" :loading="subLoading" @click="handleOk"
        >保存</a-button
      >
    </template>
    <!-- <template v-slot:[item]="$scope" v-for="item in slotsData">
      <slot :name="item" v-bind="{ ...$scope }"></slot>
    </template> -->
  </a-modal>
</template>

<script setup>
import { reactive, ref, useSlots, watchEffect, toRaw, watch, inject } from "vue";
import { Form } from "ant-design-vue";
import { useAdd, useEdit, useGetDetail } from "./usHooks";
const props = defineProps({
  show: {
    type: Boolean,
    default: false,
  },
  rulesRef: {
    type: Object,
    default: {},
  },
  delUrl: {
    type: String,
    default: "",
  },
  detailUrl: {
    type: String,
    default: "",
  },
  editUrl: {
    type: String,
    default: "",
  },
  addUrl: {
    type: String,
    default: "",
  },
  editObj: {
    type: Object,
    default: () => {},
  },
});
const emits = defineEmits(["update:show"]);
const visible = ref(false);
const formState = reactive({});
let validate = null;
let validateInfos = null;
let resetFields = null;
const subLoading = ref(false);
const useForm = Form.useForm;
let isEdit = ref(false);
const refresh = inject("refresh");
watchEffect(() => {
  visible.value = props.show;
  if (Object.keys(toRaw(formState)).length == 0) {
    Object.keys(props.rulesRef).forEach((key) => {
      formState[key] = "";
    });
  }
  if (!validate) {
    const {
      validate: validates,
      validateInfos: validateInfo,
      resetFields: reset,
    } = useForm(formState, props.rulesRef, {
      onValidate: (...args) => console.log(...args),
    });
    validate = validates;
    validateInfos = validateInfo;
    resetFields = reset;
  }
});
watch(
  () => props.editObj,
  async (n) => {
    if (n && Object.keys(n).length) {
      let keys = Object.keys(toRaw(formState));
      let { detail } = await useGetDetail(props.detailUrl, n);
      //   console.log(toRaw(detail))

      keys.forEach((item) => {
        formState[item] = detail.value[item];
      });
      Object.keys(n).forEach((item) => {
        formState[item] = n[item];
      });
      isEdit.value = true;
    }
  }
);

const handleOk = () => {
  validate()
    .then(async (res) => {
      subLoading.value = true;
      if (isEdit.value) {
        const { success, loading } = await useEdit(props.editUrl, formState);
        subLoading.value = loading.validate;
        if (success.value) {
          visible.value = false;
          subLoading.value = false;
          refresh();
          emits("update:show", false);
          onCancel();
        }
      } else {
        let { success, loading } = await useAdd(props.addUrl, toRaw(formState));
        subLoading.value = loading.validate;
        if (success.value) {
          visible.value = false;
          refresh();
          emits("update:show", false);
          onCancel();
        }
      }
    })
    .catch((err) => {
      console.log("error", err);
    });
};
const onCancel = () => {
  resetFields();

  isEdit.value = false;
  emits("update:show", false);
};
</script>

<style lang="scss" scoped></style>

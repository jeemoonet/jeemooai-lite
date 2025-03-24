<template>
  <a-modal
    :visible="visible"
    :width="522"
    :title="form.id ? '编辑智能助手' : '创建智能助手'"
    :closable="true"
    wrapClassName="workFlowDialog"
    cancelText="取消"
    okText="保存"
    @cancel="closeModal"
    @ok="submit"
    :confirmLoading="confirmLoading"
  >
    <a-form ref="formRef" :model="form" layout="vertical" name="form_in_modal">
      <a-upload
        name="file"
        :capture="false"
        accept="image/*"
        list-type="picture-card"
        :show-upload-list="false"
        :action="uploadFileUrl"
        :headers="uploadFileHeaders"
        :before-upload="beforeUpload"
        @change="handleChange"
      >
        <div class="iconBox iconcenter">
          <img :src="form.promptIcon" v-if="form.promptIcon && !avatarUploadLoading" />
          <div v-else>
            <loading-outlined v-if="avatarUploadLoading"></loading-outlined>
            <plus-outlined v-else></plus-outlined>
          </div>
          <i class="ico_upload bg"></i>
        </div>
      </a-upload>
      <div class="prompt_type" v-if="!promptData.promptId || promptData.isCopy">
        <div class="prompt_type_select float">
          <div
            class="item left"
            :class="form.promptType == 0 ? 'active' : ''"
            @click="form.promptType = 0"
          >
            <i class="normal bg"></i>
            提示词助手
          </div>
          <!-- <div
            class="item left"
            :class="form.promptType == 1 ? 'active' : ''"
            @click="form.promptType = 1"
          >
            <i class="workflow bg"></i>
            工作流助手
          </div> -->
        </div>
        <p class="prompt_type_tips">
          {{
            form.promptType == 0
              ? "支持角色设置+大模型+知识库标准流程，可以调用技能插件。"
              : "支持基本信息设置以及灵活的工作流配置。"
          }}
        </p>
      </div>
      <a-form-item
        name="promptName"
        label="助手名称"
        :rules="[{ required: true, message: '请输入工作流名称' }]"
      >
        <a-input
          v-model:value="form.promptName"
          placeholder="请输入助手名称，建议不超过8个字，超出后部分场景显示不全。"
        />
      </a-form-item>
      <a-form-item
        name="promptDesc"
        label="助手描述"
        :rules="[{ required: true, message: '请输入工作流描述' }]"
      >
        <a-textarea
          v-model:value="form.promptDesc"
          placeholder="介绍一下你的智能助手有哪些功能"
          :autoSize="{
            minRows: 3,
            maxRows: 6,
          }"
        />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive, defineProps, defineEmits, watch, inject } from "vue";
import { message } from "ant-design-vue";
import { LoadingOutlined, PlusOutlined } from "@ant-design/icons-vue";
import setting from "@/utils/setting";
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
//文件上传地址
const uploadFileUrl = `${import.meta.env.VITE_API_BASE_URL}/api/home/upload`;
//文件上传url
const uploadFileHeaders = {
  [setting.tokenHeaderName]: setting.takeToken(),
};
const formRef = ref(null);
const confirmLoading = ref(false);
const initData = () => {
  return {
    promptIcon: "",
    promptName: "",
    promptDesc: "",
    promptType: 0,
    promptId: "",
    isCopy: false,
    returnUpdateData: false,
  };
};
const form = ref(initData());
const avatarUploadLoading = ref(false);

//选择背景
const handleChange = (info, fileList, event) => {
  if (info.file.status === "uploading") {
    avatarUploadLoading.value = true;
    return;
  }
  if (info.file.status === "done") {
    avatarUploadLoading.value = false;
    let res = info.file.response || {};
    if (res.code != 200) {
      message.error(res.msg || "上传失败，请稍后再试");
      return;
    }
    form.value.promptIcon = res.data.url;
  }
  if (info.file.status === "error") {
    avatarUploadLoading.value = false;
    message.error("上传失败，请稍后再试");
  }
};

//上传投降前校验
const beforeUpload = (file) => {
  const isJpgOrPng = file.type === "image/jpeg" || file.type === "image/png";
  if (!isJpgOrPng) {
    message.error("请上传jpg或png格式图片");
  }
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    message.error("最大上传2MB");
  }
  return isJpgOrPng && isLt2M;
};

const submit = () => {
  formRef.value
    .validateFields()
    .then((values) => {
      if (form.value.isCopy) {
        copySubmit();
      } else if (form.value.returnUpdateData) {
        updateSubmit();
      } else if (form.value.promptId) {
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
    let res = await axios.post(`/api/prompt/create`, {
      ...form.value,
    });
    let data = res.data.data;
    emit("update", { ...form.value, promptId: data.promptId });
    formRef.value.resetFields();
    closeModal();
  } catch (err) {
  } finally {
    confirmLoading.value = false;
  }
};

const copySubmit = async () => {
  confirmLoading.value = true;
  try {
    let res = await axios.post(`/api/prompt/copy`, {
      ...form.value,
    });
    let data = res.data.data;
    emit("update", { ...form.value, promptId: data.promptId });
    formRef.value.resetFields();
    closeModal();
  } catch (err) {
  } finally {
    confirmLoading.value = false;
  }
};

const editSubmit = async () => {
  confirmLoading.value = true;
  try {
    let res = await axios.post(`/api/prompt/baseInfo/update`, {
      ...form.value,
    });
    emit("update");
    formRef.value.resetFields();
    closeModal();
  } catch (err) {
  } finally {
    confirmLoading.value = false;
  }
};

const updateSubmit = async () => {
  confirmLoading.value = true;
  try {
    let res = await axios.post(`/api/prompt/baseInfo/update`, {
      ...form.value,
    });
    emit("update", { ...form.value });
    formRef.value.resetFields();
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
      if (props.promptData && props.promptData.promptId) {
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
:deep(.ant-input) {
  background-color: #fafafa !important;
  border: none;
}
:deep(.ant-upload) {
  margin: 0 auto 24px;
  width: 80px;
  height: 80px;
  display: block;
  border-radius: 12px;
}
.iconBox {
  position: relative;
  overflow: hidden;
  width: 80px;
  height: 80px;
  border-radius: 12px;
  opacity: 1;
  background: #5657f6;
  margin: 0 auto;
  color: #fff;
  overflow: hidden;
  > img {
    display: block;
    width: 100%;
    height: 100%;
  }
  .ico_upload {
    width: 26px;
    height: 26px;
    position: absolute;
    bottom: 0;
    right: 0;
    background-image: url(../images/upload.png);
  }
}
.prompt_type {
  margin-bottom: 24px;
  .prompt_type_select {
    margin-bottom: 12px;
    .item {
      width: 233px;
      height: 55px;
      border-radius: 12px;
      background: #fafafa;
      border: 0.5px solid #ebebeb;
      font-size: 14px;
      font-weight: normal;
      line-height: normal;
      color: #333333;
      padding: 0 0 0 40px;
      cursor: pointer;
      .normal {
        width: 30px;
        height: 30px;
        background-image: url(../images/prompt_type_normal.png);
        margin-right: 22px;
      }
      .workflow {
        width: 30px;
        height: 30px;
        background-image: url(../images/prompt_type_workflow.png);
        margin-right: 22px;
      }
      &.active {
        background: rgba(86, 87, 246, 0.1);
        border: 0.5px solid rgba(86, 87, 246, 0.2);
      }
    }
  }
  .prompt_type_tips {
    font-size: 14px;
    font-weight: normal;
    line-height: normal;
    color: #9599a5;
  }
}
</style>

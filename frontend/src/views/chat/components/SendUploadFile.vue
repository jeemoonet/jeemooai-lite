<template>
  <a-upload
    name="files"
    :multiple="true"
    :capture="false"
    accept=".doc,.docx,.pdf"
    list-type="picture-card"
    :show-upload-list="false"
    :action="uploadFileUrl"
    :headers="uploadFileHeaders"
    :before-upload="beforeUpload"
    @change="handleChange"
    class="upload-w"
    :disabled="isUploadLoading || chat.loading || chatFileList.length >= 5"
  >
    <loading-outlined v-if="isUploadLoading"></loading-outlined>
    <template v-else>
      <a-tooltip placement="top" overlayClassName="tooltip">
        <template #title>
          支持一次上传多个文件（不同类型），文件支持pdf、文档(doc/docx)</template
        >
        <i class="upload bg"></i>
      </a-tooltip>
    </template>
  </a-upload>
</template>
<script setup>
import { message } from "ant-design-vue";
import { LoadingOutlined } from "@ant-design/icons-vue";
import { inject, ref, reactive, onMounted, nextTick, toRaw } from "vue";
import setting from "@/utils/setting";
const axios = inject("axios");
//文件上传地址
const uploadFileUrl = `${import.meta.env.VITE_API_BASE_URL}/api/chat/upload`;
//文件上传url
const uploadFileHeaders = {
  [setting.tokenHeaderName]: setting.takeToken(),
};
const isUploadLoading = ref(false);
const chat = inject("chat");
const chatFileList = inject("chatFileList");

const handleChange = (info, fileList, event) => {
  console.log(info);
  if (info.file.status === "uploading") {
    isUploadLoading.value = true;
    if (info.file.percent == 0) {
      chatFileList.value.push({
        fileName: info.file.name,
        fileSize: info.file.size,
        antUid: info.file.uid,
      });
    }

    return;
  }
  if (info.file.status === "done") {
    isUploadLoading.value = false;
    let chatFileIndex = chatFileList.value.findIndex(
      (chatFileItem) => chatFileItem.antUid == info.file.uid
    );
    let res = info.file.response || {};
    if (res.code != 200) {
      chatFileList.value.splice(chatFileIndex, 1);
      message.error(res.msg || "上传失败，请稍后再试");
      return;
    }

    chatFileList.value[chatFileIndex] = res.data[0] || {};
  }
  if (info.file.status === "error") {
    avatarUploadLoading.value = false;
    let chatFileIndex = chatFileList.value.findIndex(
      (chatFileItem) => chatFileItem.antUid == info.file.uid
    );
    if (chatFileIndex != -1) {
      chatFileList.value.splice(chatFileIndex, 1);
    }

    message.error("上传失败，请稍后再试");
  }
};

//上传投降前校验
const beforeUpload = (file, fileList) => {
  const isLimit = chatFileList.value.length + fileList.length > 5;
  if (isLimit) {
    message.error("最多上传5个文件");
    return false;
  }
  const isLt2M = file.size / 1024 / 1024 < 10;
  if (!isLt2M) {
    message.error("最大上传10MB");
    return false;
  }
  return true;
};
</script>
<style lang="less" scoped>
.upload-w {
  width: 32px;
  height: 32px;
  display: block;
  // margin-right: 12px;
}
:deep(.ant-upload-select) {
  display: block;
  background-color: transparent !important;
  width: 100%;
  height: 100%;
  position: relative;
  margin-right: 0;
  margin-bottom: 0;
  transition: all 0.15s ease-in-out;
  // border: 0.8px solid rgba(86, 87, 246, 0.1);
  border: none;
  border-radius: 50%;
  text-align: center;
  line-height: 32px;
  &:hover {
    // border: 0.8px solid rgba(86, 87, 246, 0.8);
  }
  &.ant-upload-disabled {
    &:hover {
      // border: 0.8px solid rgba(86, 87, 246, 0.1);
    }
  }
}
.anticon-loading {
  color: rgba(86, 87, 246, 1);
}
.upload {
  width: 100%;
  height: 100%;
  background-image: url(./images/file.png);
  background-size: 20px 20px;
  cursor: pointer;

  &:hover {
    background-image: url(./images/file1.png);
  }
}
</style>

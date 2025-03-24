<template>
  <a-form ref="formRef" :model="form" layout="vertical" name="form_in_modal">
    <a-row :gutter="24" class="form_item">
      <a-col :span="24">
        <a-form-item
          name="nickName"
          label="姓名"
          :rules="[{ required: true, message: '请输入' }]"
        >
          <a-input v-model:value="form.nickName" placeholder="请输入" />
        </a-form-item>
      </a-col>
      <a-col :span="24">
        <a-form-item
          name="phonenumber"
          label="电话"
          :rules="[{ required: true, message: '请输入' }]"
        >
          <a-input-number
            v-model:value="form.phonenumber"
            placeholder="请输入"
            :controls="false"
            style="width: 100%"
          />
        </a-form-item>
      </a-col>
      <a-col :span="24">
        <a-form-item name="email" label="邮箱">
          <a-input v-model:value="form.email" placeholder="请输入" />
        </a-form-item>
      </a-col>
      <a-col :span="24">
        <a-form-item name="sex" label="性别">
          <a-radio-group v-model:value="form.sex">
            <a-radio value="0">男</a-radio>
            <a-radio value="1">女</a-radio>
          </a-radio-group>
        </a-form-item>
      </a-col>
      <a-col :span="24">
        <a-form-item name="avatar" label="头像">
          <a-upload
            name="file"
            :capture="false"
            accept="image/*"
            list-type="picture-card"
            class="icon-uploader iconcenter"
            :show-upload-list="false"
            :action="uploadFileUrl"
            :headers="uploadFileHeaders"
            :before-upload="beforeUpload"
            @change="handleIconChange"
          >
            <div class="uploader-img iconcenter">
              <img :src="form.avatar" alt="" />
              <div class="uploadr-svg iconcenter">
                <loading-outlined v-if="iconUploadLoading"></loading-outlined>
                <plus-outlined v-else></plus-outlined>
              </div>
            </div>
          </a-upload>
        </a-form-item>
      </a-col>
    </a-row>
  </a-form>
  <div class="submit iconcenter">
    <a-button
      class="submit-btn"
      type="primary"
      :loading="confirmLoading"
      @click="onSubmit"
      >保存</a-button
    >
  </div>
</template>
<script setup>
import { ref, watch, inject, onMounted } from "vue";
import { message } from "ant-design-vue";
import { LoadingOutlined, PlusOutlined } from "@ant-design/icons-vue";
import setting from "@/utils/setting";
import { InfoStore } from "@/stores/user";
import { storeToRefs } from "pinia";
const infoStore = InfoStore();
const { getUserInfo } = storeToRefs(infoStore);
const axios = inject("axios");
//文件上传地址
const uploadFileUrl = `${import.meta.env.VITE_API_BASE_URL}/api/home/upload`;
//文件上传url
const uploadFileHeaders = {
  [setting.tokenHeaderName]: setting.takeToken(),
};
const iconUploadLoading = ref(false);
const formRef = ref(null);
const confirmLoading = ref(false);
const form = ref({});

onMounted(() => {
  form.value = {
    ...getUserInfo.value,
  };
});

const onSubmit = () => {
  formRef.value
    .validateFields()
    .then((values) => {
      updateCompanyData();
    })
    .catch((info) => {});
};

const updateCompanyData = async () => {
  confirmLoading.value = true;
  try {
    let res = await axios.post("/api/user/updateInfo", {
      ...form.value,
    });
    message.success("更新成功");
    infoStore.updateUserInfo(true);
  } catch (err) {
  } finally {
    confirmLoading.value = false;
  }
};

//选择图标
const handleIconChange = (info, fileList, event) => {
  if (info.file.status === "uploading") {
    iconUploadLoading.value = true;
    return;
  }
  if (info.file.status === "done") {
    iconUploadLoading.value = false;
    let res = info.file.response || {};
    if (res.code != 200) {
      message.error(res.msg || "上传失败，请稍后再试");
      return;
    }
    form.value.avatar = res.data.url;
  }
  if (info.file.status === "error") {
    iconUploadLoading.value = false;
    message.error("上传失败，请稍后再试");
  }
};

//上传投降前校验
const beforeUpload = (file) => {
  const isJpgOrPng = file.type === "image/jpeg" || file.type === "image/png";
  if (!isJpgOrPng) {
    message.error("请上传jpg或png格式图片");
  }
  const isLt2M = file.size / 1024 / 1024 < 3;
  if (!isLt2M) {
    message.error("最大上传3MB");
  }
  return isJpgOrPng && isLt2M;
};
</script>
<style scoped lang="less">
.form_item {
  margin-top: 30px;
  .ant-form-item {
    margin-bottom: 10px;
  }
  .ant-input,
  .ant-input-number {
    border: none;
  }
  :deep(.ant-select-selector) {
    border: none;
    height: 30px;
  }
  .personMobile {
    border-left: 1px solid #e8e8e9;
    &:focus {
      border: none;
    }
  }
}

textarea.ant-input {
  height: 90px;
}
.icon-uploader {
  width: 104px;
  height: 104px;
  margin-right: 16px;
  position: relative;

  .uploadr-svg {
    position: absolute;
    background-color: rgba(0, 0, 0, 0.5);
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    color: #fff;
    font-size: 18px;
  }
  :deep(.ant-upload) {
    width: 100%;
    height: 100%;
    background-color: #fff !important;
    color: #333;
    //   border: 1px dashed #333;
    position: relative;
    .uploader-img {
      width: 100%;
      height: 100%;
      position: relative;

      img {
        width: 100%;
        height: 100%;
      }
    }
  }
}
.submit {
  width: 100%;
  margin-top: 40px;
  .submit-btn {
    width: 125px;
    height: 46px;
    background-color: #5657f6;
    font-size: 16px;
  }
}
</style>

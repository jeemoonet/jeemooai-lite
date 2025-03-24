<template>
  <a-form
    ref="formRef"
    :model="form"
    :rules="rules"
    layout="vertical"
    name="form_in_modal"
  >
    <a-row :gutter="24" class="form_item">
      <!-- <a-col :span="24">
        <a-form-item name="oldPassword" label="旧密码">
          <a-input-password v-model:value="form.oldPassword" placeholder="请输入" />
        </a-form-item>
      </a-col> -->
      <a-col :span="24">
        <a-form-item name="newPassword" label="新密码">
          <a-input-password v-model:value="form.newPassword" placeholder="请输入" />
        </a-form-item>
      </a-col>
      <a-col :span="24">
        <a-form-item name="verifyPassword" label="确认密码">
          <a-input-password
            v-model:value="form.verifyPassword"
            placeholder="请再次确认密码"
          />
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
const rules = {
  // oldPassword: [{ required: true, message: "请输入旧密码" }],
  newPassword: [
    {
      required: true,
      validator: async (rule, value) => {
        if (value != form.value.verifyPassword && form.value.verifyPassword) {
          return Promise.reject("密码不一致,请检查密码");
        }
        let lengthRegex = /^(?=.*[a-zA-Z0-9!@#$%^&*()_+{}|:<>?]).{8,14}$/;
        let varietyRegex = /^(?![\d]+$)(?![a-zA-Z]+$)(?![!@#$%^&*()_+{}|:<>?]+$)[\da-zA-Z!@#$%^&*()_+{}|:<>?]+$/;
        let noSpaceChineseRegex = /^(?!.*[\s\u4e00-\u9fa5]).+$/;
        let validatePassword =
          lengthRegex.test(value) &&
          varietyRegex.test(value) &&
          noSpaceChineseRegex.test(value);
        if (!validatePassword) {
          return Promise.reject("密码长度为8~14,字母/数字以及标点符号至少包含2种");
        }
        return Promise.resolve();
      },
    },
  ],
  verifyPassword: [
    {
      required: true,
      validator: async (rule, value) => {
        if (!value) {
          return Promise.reject("请输入确认密码");
        }
        if (value != form.value.newPassword && form.value.newPassword) {
          return Promise.reject("密码不一致,请检查密码");
        }
        return Promise.resolve();
      },
    },
  ],
};
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
    let res = await axios.post("/api/user/password", {
      ...form.value,
    });
    message.success("更新成功");
    formRef.value.resetFields();
  } catch (err) {
  } finally {
    confirmLoading.value = false;
  }
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

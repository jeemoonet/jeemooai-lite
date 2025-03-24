<template>
  <div class="content">
    <a-input-group compact>
      <a-select v-model:value="mobileCode">
        <a-select-option value="+86">+86</a-select-option>
      </a-select>
      <a-input v-model:value="userName" placeholder="请输入手机号码" />
    </a-input-group>
    <a-input-password
      @pressEnter="onPasswordSubmit"
      v-model:value="password"
      placeholder="请输入密码"
    />
  </div>
  <SubmitView @ok="onPasswordSubmit" />
</template>
<script setup>
import { message } from "ant-design-vue";
import { ref, onMounted, inject, nextTick, defineEmits, onBeforeUnmount } from "vue";
import SubmitView from "./submit.vue";
const emit = defineEmits(["submit"]);
const mobileCode = ref("+86");
const userName = ref("");
const password = ref("");

//密码提交
const onPasswordSubmit = async () => {
  if (!userName.value) {
    message.error("请输入正确的账号");
    return;
  }
  if (!password.value) {
    message.error("请输入密码");
    return;
  }
  emit("submit", { userName: userName.value, password: password.value });
};
</script>
<style scoped lang="less">
.content {
  width: 100%;
  margin-top: 32px;

  :deep(.ant-input) {
    width: calc(100% - 76px);
    height: 42px;
  }
  :deep(.ant-select) {
    .ant-select-selector {
      width: 76px;
      height: 42px;
      line-height: 42px;

      .ant-select-selection-item {
        line-height: 42px;
      }
    }
    .ant-select-arrow {
      top: 50%;
      transform: translateY(-50%);
      margin-top: 0;
    }
  }
  :deep(.ant-input-password) {
    width: 100%;
    margin-top: 15px;
    height: 42px;
    .ant-input {
      width: 100%;
      height: auto;
    }
  }
  .forgot {
    width: 100%;
    text-align: right;
    font-size: 14px;
    font-weight: 400;
    color: #333333;
    line-height: 20px;
    margin-top: 10px;
    cursor: pointer;
  }
  .getCode {
    width: 113px;
    height: 42px;
    color: #5657f6;
    font-size: 16px;
    line-height: 42px;
    text-align: center;
    cursor: pointer;
    position: relative;
    border: 1px solid #d9d9d9;
    border-left: none;
    transition: all 0.3s;
    &:hover {
      border-color: #8286ff;
      border-left: 1px solid #8286ff;
    }
  }
}
</style>

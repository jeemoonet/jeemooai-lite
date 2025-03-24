<template>
  <div class="submit">
    <div class="submit_btn iconcenter" @click.stop="emit('ok')">登录</div>
  </div>
</template>
<script setup>
import { message } from "ant-design-vue";
import { ref, onMounted, inject, nextTick, defineEmits, onBeforeUnmount } from "vue";
const emit = defineEmits(["register", "ok"]);
const register = inject("onRegister", Function, true);
onBeforeUnmount(() => {});

const onRegister = () => {};
</script>
<style scoped lang="less">
.submit {
  width: 100%;
  margin-top: 40px;
  .submit_btn {
    width: 205px;
    height: 46px;
    text-align: center;
    line-height: 46px;
    background: #5657f6;
    border-radius: 6px;
    outline: none;
    border: none;
    font-size: 18px;
    font-weight: 500;
    color: #f5f6f8;
    margin: 0 auto;
    cursor: pointer;
  }
  .tips {
    width: 100%;
    font-size: 14px;
    font-weight: 400;
    color: #333333;
    line-height: 20px;
    margin-top: 10px;
    .register {
      color: #5657f6;
      cursor: pointer;
    }
  }
}
</style>

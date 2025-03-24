<template>
  <div class="model">
    <div class="tabbar left">
      <div class="item active">账号登录</div>
    </div>
    <password-login @submit="onPasswordSubmit" />
  </div>
</template>
<script setup>
import { message } from "ant-design-vue";
import {
  ref,
  onMounted,
  inject,
  provide,
  nextTick,
  defineEmits,
  onBeforeUnmount,
} from "vue";
import PasswordLogin from "./login/password.vue";
import { useRouter } from "vue-router";
import { storeToRefs } from "pinia";
import setting from "@/utils/setting";
const emit = defineEmits(["close", "register"]);
const router = useRouter();
const axios = inject("axios");
const tabbarStatus = ref(0);

onMounted(() => {});
onBeforeUnmount(() => {});

//密码提交
const onPasswordSubmit = async (params) => {
  let hide = null;
  hide = message.loading("请求中..", 0);
  try {
    let res = await axios.post("/api/login/password", {
      ...params,
    });
    setting.cacheToken(res.data.data.token, true);
    router.replace("/chat");
  } catch (err) {
  } finally {
    hide();
  }
};
</script>

<style scoped lang="less">
.model {
  width: 100%;
}
.tabbar {
  width: 100%;
  // padding: 0 26px;
  .item {
    font-size: 24px;
    font-weight: 400;
    color: #333333;
    line-height: 35px;
    cursor: pointer;
    margin-right: 30px;
    &.active {
      font-weight: 700;
    }
  }
}
</style>

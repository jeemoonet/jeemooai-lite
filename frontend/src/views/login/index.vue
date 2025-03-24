<template>
  <div class="loginPage float">
    <div class="layout-slider">
      <a class="logo left" :href="linkUrl" target="_blank" rel="noopener noreferrer">
        <img src="@/assets/images/logo.png" alt="" />
        积木大脑
      </a>
      <img src="@/assets/images/login/bg.webp" alt="" class="bg" />
      <div class="more">
        打造企业私域大模型和AI 应用平台
        <a class="iconcenter" :href="linkUrl" target="_blank" rel="noopener noreferrer">
          了解更多
          <img src="@/assets/images/ico_link_light.png" alt="" />
        </a>
      </div>
    </div>
    <div class="layout-content">
      <div class="layout-content-w">
        <component
          @register="onRegister"
          @close="onClose"
          @login="open"
          :is="chatComponents.get(status)"
        ></component>
      </div>
    </div>
  </div>
</template>
<script setup>
import {
  ref,
  reactive,
  onMounted,
  inject,
  provide,
  nextTick,
  watch,
  defineAsyncComponent,
  onBeforeUnmount,
} from "vue";
import { useRouter } from "vue-router";
import { throttle } from "@/utils/utils";
import setting from "@/utils/setting";
const router = useRouter();
const axios = inject("axios");
const linkUrl = ref(setting.jeemooPublicizeUrl);
const status = ref("login");

const VITE_WX_APPID = import.meta.env.VITE_WX_PC_APPID;
const chatComponents = ref(new Map());
chatComponents.value.set(
  "login",
  defineAsyncComponent(() => import("./components/login.vue"))
);

onMounted(() => {});

const open = () => {
  status.value = "login";
};

const onRegister = () => {
  status.value = "register";
};

const onClose = () => {
  status.value = "";
};

onBeforeUnmount(() => {});
</script>

<style scoped lang="less">
.loginPage {
  width: 100%;
  height: 100vh;
  background: #fff;
}
.layout-slider {
  width: 720px;
  height: 100%;
  position: relative;
  background: #5657f6;
  padding: 30px;
  overflow-y: auto;
  .logo {
    font-size: 18px;
    color: #fff;
    line-height: 1;
    font-weight: 500;
    img {
      display: block;
      width: 24px;
      height: 24px;
      margin-right: 12px;
    }
  }
  .bg {
    display: block;
    width: 539px;
    height: 579px;
    margin: 62px auto 0;
    user-select: none;
  }
  .more {
    font-weight: 400;
    font-size: 22px;
    color: #ffffff;
    line-height: 31px;
    letter-spacing: 2px;
    text-align: center;
    font-style: normal;
    position: relative;
    z-index: 2;
    top: -14px;
    a {
      width: 120px;
      height: 42px;
      border-radius: 30px 30px 30px 30px;
      border: 1px solid rgba(255, 255, 255, 0.3);
      font-size: 16px;
      color: #ffffff;
      margin: 23px auto 0;
      letter-spacing: 0;

      img {
        display: block;
        width: 18px;
        height: 18px;
        margin-left: 5px;
      }
    }
  }
}
.layout-content {
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 1;
  min-width: 0;
  height: 100%;
  .layout-content-w {
    width: 424px;
  }
}
</style>

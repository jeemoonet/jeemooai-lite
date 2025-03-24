<template>
  <div class="settingPage headerMenuPadding left">
    <common-menu />
    <div class="layout">
      <div class="layout-content">
        <RouterView />
      </div>
    </div>
  </div>
</template>
<script setup>
import { inject, ref, onMounted, watch, nextTick } from "vue";
import { message, Modal } from "ant-design-vue";
import CommonMenu from "./components/menu/index.vue";
import setting from "@/utils/setting";
import { useRouter, RouterView } from "vue-router";
import { DoceStore } from "@/stores/doce";
import { storeToRefs } from "pinia";
const doceStore = DoceStore();
const { doceMenuList, doceMenuId, doceSelectTitle } = storeToRefs(doceStore);
const router = useRouter();
const axios = inject("axios");

onMounted(() => {});
</script>

<style scoped lang="less">
.settingPage {
  width: 100%;
  height: 100%;
}
.layout {
  display: flex;
  flex: auto;
  flex-direction: column;
  width: calc(100% - 260px);
  height: 100%;
  margin: 0 auto;
  position: relative;
  overflow-y: auto;
  .layout-footer {
    flex: 0 0 auto;
    width: 100%;
    height: 40px;
    text-align: center;
    font-size: 14px;
    font-weight: 400;
    color: #999999;
    line-height: 19px;
  }
  .layout-content {
    min-height: auto;
    flex: auto;
    padding: 30px 30px 0;
  }
}
</style>

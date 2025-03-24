<template>
  <div class="indexPage headerMenuPadding">
    <div class="indexPage_w">
      <div class="float">
        <!-- 常用助手 -->
        <recent @onPromptItem="onPromptItem" />
        <!-- 轮播图 -->
        <banner />
      </div>
      <prompt-list @onPromptItem="onPromptItem" />
    </div>
  </div>
  <quick-start />
</template>
<script setup>
import { message } from "ant-design-vue";
import { ref, onMounted, inject, nextTick } from "vue";
import recent from "./components/recent.vue";
import PromptList from "./components/PromptList.vue";
import banner from "./components/swiper.vue";
import QuickStart from "./components/quickStart.vue";
import { useRouter } from "vue-router";
import { storeToRefs } from "pinia";
const router = useRouter();
const axios = inject("axios");

onMounted(() => {});

//点击提示器
const onPromptItem = (item) => {
  router.push({
    name: "chat",
    state: {
      id: item.promptId,
    },
  });
};
</script>

<style scoped lang="less">
.indexPage {
  width: 100%;
}
.indexPage_w {
  width: 1200px;
  margin: 0 auto;
  padding-top: 40px;
}
</style>

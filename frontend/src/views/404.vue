<template>
  <div class="errorPage">
    <img class="banner" src="@/assets/images/404.png" alt="" />
    <h4 class="title">暂无权限。</h4>
    <a-button class="btn" @click="goHome" type="primary">返回工作台</a-button>
  </div>
</template>
<script setup>
import { ref, onMounted, inject, nextTick, watch, computed, onBeforeUnmount } from "vue";
import { useRouter } from "vue-router";
const router = useRouter();

const goHome = () => {
  router.push({
    path: "/",
  });
};
</script>
<style scoped lang="less">
.errorPage {
  height: 100%;
  display: flex;
  justify-content: center;
  flex-flow: column;
  align-items: center;
  .banner {
    display: block;
    width: 380px;
  }
  .title {
    font-size: 20px;
    color: #333333;
    font-weight: 500;
    margin-top: 20px;
    letter-spacing: 1px;
  }
  .btn {
    width: 200px;
    margin-top: 28px;
    border-radius: 4px;
    background: #5657f6;
    color: #fff;
  }
}
</style>

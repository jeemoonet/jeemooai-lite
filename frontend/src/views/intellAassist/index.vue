<template>
  <div class="plugin_tools">
    <div class="headerMenuPadding">
      <slideNav></slideNav>
    </div>
    <div class="right_box">
      <RouterView />
    </div>
  </div>
</template>

<script setup>
import slideNav from "./components/slideNav.vue";
</script>

<style lang="less" scoped>
.plugin_tools {
  width: 100%;
  display: flex;
  justify-content: flex-start;
  height: 100%;
  .right_box {
    flex: 1;
    width: calc(100% - 200px);
    height: 100%;
  }
}
</style>

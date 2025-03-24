<template>
  <div class="empty-data" :style="{ color: color }">暂无数据</div>
</template>
<script setup>
import { defineProps } from "vue";
const props = defineProps({
  color: {
    type: String,
    default: () => "#000000d9",
  },
});
</script>
<style scoped lang="less">
.empty-data {
  width: 100%;
  text-align: center;
  padding-top: 40px;
  padding-bottom: 20px;
  font-size: 16px;
  color: #000000d9;
  line-height: 23px;
}
</style>

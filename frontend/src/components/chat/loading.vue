<template>
  <div class="md-loading left">
    <LoadingOutlined class="icon-md-loading" />
    {{ title }}
  </div>
</template>

<script setup>
import { LoadingOutlined } from "@ant-design/icons-vue";
import { ref, onMounted, inject, defineProps, defineEmits, computed } from "vue";
const props = defineProps({
  title: {
    type: String,
    default: "正在思考中",
  },
});
</script>

<style scoped lang="less">
.md-loading {
  margin-bottom: 18px;
  font-size: 14px;
  font-weight: normal;
  line-height: 16px;
  color: #3d3d3d;
  padding-left: 25px;
  padding-right: 18px;
}
.icon-md-loading {
  font-size: 16px;
  margin-right: 10px;
}
</style>

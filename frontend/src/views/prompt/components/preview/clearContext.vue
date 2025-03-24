<template>
  <div class="clearContext">
    <loading-outlined v-if="isLoading"></loading-outlined>
    <template v-else>
      <a-tooltip placement="top" overlayClassName="tooltip">
        <template #title> 清空上下文</template>
        <i class="clear bg" @click="onClean"></i>
      </a-tooltip>
    </template>
  </div>
</template>
<script setup>
import { message } from "ant-design-vue";
import { LoadingOutlined } from "@ant-design/icons-vue";
import { inject, ref, reactive, onMounted, nextTick, toRaw } from "vue";
const axios = inject("axios");
const windowId = inject("windowId");
const isLoading = ref(false);

const onClean = async () => {
  if (!windowId.value || isLoading.value) return;
  isLoading.value = true;
  try {
    let res = await axios.post("/api/chat/clearContext", {
      windowId: windowId.value || null,
    });
    message.success("恭喜您,清空上下文成功");
  } catch (err) {
  } finally {
    isLoading.value = false;
  }
};
</script>
<style lang="less" scoped>
.clearContext {
  width: 32px;
  height: 32px;
  border: 0.8px solid rgba(86, 87, 246, 0.1);
  border-radius: 50%;
  text-align: center;
  font-size: 20px;
  line-height: 32px;
  color: rgba(86, 87, 246, 1);
  cursor: pointer;
  transition: all 0.15s ease-in-out;
  margin-right: 12px;
  &:hover {
    border: 0.8px solid rgba(86, 87, 246, 0.8);
  }
  .clear {
    width: 100%;
    height: 100%;
    background-image: url(../images/clean.png);
    background-size: 20px 20px;
  }
}
</style>

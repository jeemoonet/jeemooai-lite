<template>
  <div class="rate" v-show="isShow">
    <div class="title">请为我们服务评分</div>
    <a-rate @change="onChange" v-model:value="rateVal" allow-half />
    <a-button :loading="isLoading" @click="onSubmit">提交</a-button>
  </div>
</template>
<script setup>
import { message } from "ant-design-vue";
import { inject, ref, reactive, onMounted, nextTick, onBeforeUnmount } from "vue";
const props = defineProps({
  customerUuid: {
    type: String,
    default: () => "",
  },
  windowId: {
    type: String,
    default: () => "",
  },
  isWindowStar: {
    type: Number,
    default: () => 0,
  },
});
const axios = inject("axios");
const rateVal = ref(5);
const isLoading = ref(false);
const timmer = ref(null);
const isShow = ref(false);

const onChange = (e) => {
  if (e <= 0) {
    rateVal.value = 0.5;
  }
};

const onSubmit = async () => {
  isLoading.value = true;
  try {
    let res = await axios.post("/customer/window/star", {
      customerUuid: props.customerUuid,
      windowId: props.windowId,
      star: rateVal.value,
    });
    onClearTimeout();
    message.success("感谢您的评分");
  } catch (err) {
  } finally {
    isLoading.value = false;
  }
};

const onStart = (fn) => {
  if (props.isWindowStar != 1) return;
  onClearTimeout();
  timmer.value = setTimeout(() => {
    isShow.value = true;
    fn && fn();
  }, 300000);
};

const onClearTimeout = () => {
  isShow.value = false;
  rateVal.value = 5;
  clearInterval(timmer.value);
};

onBeforeUnmount(() => {
  onClearTimeout();
});

defineExpose({
  onStart,
  onClearTimeout,
});
</script>
<style scoped lang="less">
.rate {
  display: flex;
  justify-content: center;
  flex-direction: column;
  width: 100%;
  background: #ffffff;
  border-radius: 4px 20px 20px 20px;
  padding: 20px 0 30px;
  margin-bottom: 20px;
  .title {
    width: fit-content;
    position: relative;
    font-size: 16px;
    font-weight: 400;
    color: #000000;
    line-height: 23px;
    margin: 0 auto 15px;
    &::before {
      content: "";
      width: 18px;
      height: 1px;
      background-color: #9599a5;
      position: absolute;
      left: -30px;
      top: 50%;
      transform: translateY(-50%);
    }
    &::after {
      content: "";
      width: 18px;
      height: 1px;
      background-color: #9599a5;
      position: absolute;
      right: -30px;
      top: 50%;
      transform: translateY(-50%);
    }
  }
  :deep(.ant-rate) {
    color: #5657f6;
    font-size: 22px;
    margin: 0 auto;
    .ant-rate-star:not(:last-child) {
      margin-right: 25px;
    }
  }
  :deep(.ant-btn) {
    border-color: #5657f6;
    color: #5657f6;
    width: 214px;
    height: 42px;
    margin: 15px auto 0;
    font-weight: 500;
    span {
      font-weight: 500;
    }
  }
}
</style>

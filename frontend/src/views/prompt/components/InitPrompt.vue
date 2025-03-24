<template>
  <div class="init_prompt">
    <a-spin v-if="spinning"></a-spin>
    <div class="init_prompt_header float">
      <h3 class="title">角色设置</h3>
      <div class="auto right" @click="onAutoCreate">
        <i class="ico_auto bg"></i>
        优化
      </div>
    </div>
    <a-textarea
      class="input"
      v-model:value="form.initPrompt"
      placeholder="请输入角色性格设计，如“请扮演苏格拉底“"
    />
  </div>
</template>
<script setup>
import { message } from "ant-design-vue";
import { ref, onMounted, inject, nextTick, provide, reactive } from "vue";
const axios = inject("axios");
const form = inject("form");
const spinning = ref(false);
onMounted(() => {});
const onAutoCreate = async () => {
  if (!form.value.initPrompt) {
    message.warn("请输入需要优化的内容");
    return;
  }
  spinning.value = true;
  try {
    let res = await axios.post(`/api/prompt/init`, {
      prompt: form.value.initPrompt,
    });

    let data = res.data.data || {};
    form.value.initPrompt = data.initPrompt;
  } catch (err) {
  } finally {
    spinning.value = false;
  }
};
</script>
<style scoped lang="less">
.init_prompt {
  display: flex;
  flex-direction: column;
  // width: calc(33.33% - 10px);
  flex: 1;
  min-width: 0;
  height: 100%;
  position: relative;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;

  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.04);
  .init_prompt_header {
    width: 100%;
    height: 46px;
    padding: 0 8px 0 22px;
    border-bottom: 1px solid #fafafa;

    .title {
      font-size: 16px;
      font-weight: 600;
      line-height: normal;
      color: #333333;
      margin: 0;
    }
    .auto {
      font-size: 14px;
      font-weight: normal;
      line-height: normal;
      color: #1d2129;
      cursor: pointer;
      padding: 5px 12px;
      border-radius: 6px;
      transition: all 0.2s ease-in-out;
      .ico_auto {
        width: 16px;
        height: 16px;
        background-image: url(./images/prompt_auto.png);
        margin-right: 5px;
      }
      &:hover {
        background: #fafafa;

        box-shadow: inset 0px 0px 20px 0px rgba(0, 0, 0, 0.01);
      }
    }
  }
}
.ant-input {
  flex: 1;
  min-height: 0;
  display: block;
  width: 100%;
  margin-top: 12px;
  background-color: #fff !important;
  border: none;
  border-radius: 0;
  color: #333333;
  font-size: 14px;
  box-shadow: none;
  padding: 0 15px 12px 22px;
  &::placeholder {
    color: #9599a5;
  }

  &:focus {
    box-shadow: none;
  }
}
.ant-spin {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  width: 100%;
  height: 100%;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  clear: both;
  opacity: 0.5;
  background-color: #e6f4ff;
  z-index: 2;
}
</style>

<template>
  <div class="commonFormPage">
    <a-textarea
      class="input"
      @keydown="($event) => textareaEnter($event)"
      v-model:value="chatMessage"
      placeholder="请输入问题..."
    ></a-textarea>
    <button class="submit iconcenter" @click="submitMessage">自由提问</button>
    <div class="declare">
      测试产生的所有内容均由人工智能模型输出，其内容的准确性和完整性无法保证，不代表我们的态度或观点。
    </div>
  </div>
</template>
<script setup>
import { StopOutlined } from "@ant-design/icons-vue";
import { ref, reactive, defineProps, watch, defineEmits } from "vue";

const emit = defineEmits(["submitMessage"]);

const chatMessage = ref("");

const textareaEnter = (event) => {
  if (!event.shiftKey && event.keyCode == 13) {
    event.cancelBubble = true;
    event.stopPropagation();
    event.preventDefault();
    submitMessage();
  }
};

const submitMessage = async () => {
  emit("submitMessage", chatMessage.value);
  chatMessage.value = "";
};
</script>

<style lang="less" scoped>
.commonFormPage {
  display: flex;
  align-items: flex-start;
  width: 100%;
  position: relative;
  margin-top: 40px;
  .input {
    width: 100%;
    height: 96px;
    background-color: #fff;
    color: #3d3d3d;
    border: 1px solid #5657f6;
    // overflow-y: hidden;
    padding: 20px 136px 0 20px;
    outline: none;
    &:focus {
      box-shadow: none;
      border-color: #5657f6;
    }
  }

  .submit {
    width: 120px;
    height: 44px;
    border-radius: 80px;
    background-color: #5657f6;
    text-align: center;
    line-height: 44px;
    font-size: 18px;
    color: #ffffff;
    position: absolute;
    bottom: 20px;
    right: 15px;
    outline: none;
    border: none;
  }
  .declare {
    width: 100%;
    font-size: 14px;
    font-weight: 400;
    color: #9599a5;
    line-height: 19px;
    text-align: center;
    position: absolute;
    bottom: -32px;
    left: 0;
    right: 0;
  }
}
</style>

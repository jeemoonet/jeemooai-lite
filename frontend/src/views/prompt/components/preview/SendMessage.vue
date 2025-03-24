<template>
  <clear-context />
  <div class="commonFormPage">
    <a-input
      class="input"
      @keydown="($event) => textareaEnter($event)"
      @input="changeChatMessageInput"
      :value="chat.message"
      placeholder="请输入问题..."
    ></a-input>
    <button class="submit submit-stop" v-show="chat.loading" @click="stopSubmitConfirm">
      <stop-outlined />
    </button>
    <button class="submit bg" v-show="!chat.loading" @click="submitMessage"></button>
  </div>
</template>
<script setup>
import { message, notification } from "ant-design-vue";
import { StopOutlined } from "@ant-design/icons-vue";
import {
  ref,
  reactive,
  defineProps,
  onMounted,
  watch,
  defineEmits,
  toRaw,
  inject,
} from "vue";
import clearContext from "./clearContext.vue";
import setting from "@/utils/setting";

const emit = defineEmits(["submitMessage"]);

const chat = inject("chat");
const promptData = inject("promptData");
let isCanStop = false;
let resultText = "";

onMounted(() => {});

const changeChatMessageInput = (val) => {
  chat.message = val.target.value;
};

const stopSubmitConfirm = () => {
  chat.stop = true;
};

const textareaEnter = (event) => {
  if (!event.shiftKey && event.keyCode == 13) {
    event.cancelBubble = true;
    event.stopPropagation();
    event.preventDefault();
    submitMessage();
  }
};

const submitMessage = async () => {
  emit("submitMessage");
};
</script>

<style lang="less" scoped>
.commonFormPage {
  display: flex;
  align-items: flex-start;
  position: relative;
  flex: 1;
  min-width: 0;
  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.04);
  border-radius: 80px;
  border: 0.5px solid #ebebeb;
  .input {
    width: 100%;
    height: 47px;
    line-height: 47px;
    font-size: 14px;
    background-color: #fff;
    border-radius: 40px;
    color: #3d3d3d;
    border: none;
    padding: 0px 44px 0 14px;
    outline: none;
    &:focus {
      box-shadow: none;
      border: none !important;
    }
  }

  .submit {
    width: 36px;
    height: 36px;
    line-height: 36px;
    border-radius: 50px;
    background-color: #5657f6;
    background-image: url(../images/ico_send.png);
    background-size: 21px 21px;
    position: absolute;
    top: 50%;
    right: 4px;
    transform: translateY(-50%);
    outline: none;
    border: none;
  }
  .submit-stop {
    background-image: none;
    color: #5657f6;
    font-size: 24px;
    background-color: transparent;
  }
}
</style>

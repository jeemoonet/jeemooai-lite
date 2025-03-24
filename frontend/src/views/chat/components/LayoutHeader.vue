<template>
  <div class="layoutHeaderPage left">
    <img
      v-if="getCurrentWindow.promptIcon"
      :src="getCurrentWindow.promptIcon"
      class="prompt_logo"
      alt=""
    />

    <img src="@/assets/images/ico_message.png" v-else class="prompt_logo" alt="" />
    <div class="prompt_title">{{ getCurrentWindow.promptName || "自由问答" }}</div>
    <div class="prompt_desc one_line">
      {{ getCurrentWindow.promptDesc || "输入你的问题，我们开始对话吧！" }}
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, computed, watch, defineProps, defineEmits, onMounted } from "vue";
import { storeToRefs } from "pinia";
import { ChatStore } from "@/stores/chat";
const chatStore = ChatStore();
const { getCurrentWindow } = storeToRefs(chatStore);
</script>
<style scoped lang="less">
.layoutHeaderPage {
  width: 852px;
  border-bottom: 1px solid #c7c7c7;
  background-color: #f5f6f8;
  margin: 0 auto;
  position: relative;
  align-items: flex-end;
  padding: 30px 0 20px;

  .prompt_logo {
    display: block;
    width: 40px;
    height: 40px;
    margin-right: 10px;
    font-size: 40px;
    line-height: 40px;
  }
  .prompt_title {
    font-size: 26px;
    font-weight: 700;
    color: #333333;
    line-height: 38px;
    letter-spacing: 1px;
    margin-left: 5px;
    margin-bottom: 2px;
  }
  .prompt_desc {
    width: 500px;
    font-size: 16px;
    font-weight: 400;
    color: #333333;
    line-height: 23px;
    margin-left: 7px;
    margin-bottom: 6px;
  }
}
</style>

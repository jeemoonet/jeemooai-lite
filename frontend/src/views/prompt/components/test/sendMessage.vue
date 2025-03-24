<template>
  <div class="commonFormPage">
    <div class="w">
      <a-textarea
        class="input"
        @keydown="($event) => textareaEnter($event)"
        @input="changeChatMessageInput"
        :autoSize="{ minRows: 1, maxRows: 3 }"
        row="1"
        :value="chat.message"
        placeholder="请输入问题..."
      ></a-textarea>
      <div class="controller right">
        <div class="clearContext">
          <loading-outlined v-if="isLoading"></loading-outlined>
          <template v-else>
            <a-tooltip placement="top" overlayClassName="tooltip">
              <template #title> 清空上下文</template>
              <i class="clear bg" @click="onClearContext"></i>
            </a-tooltip>
          </template>
        </div>
        <button
          class="submit submit-stop iconcenter"
          v-show="chat.loading"
          @click="stopSubmitConfirm"
        >
          终止
        </button>
        <button
          class="submit iconcenter"
          :disabled="!chat.message ? true : false"
          v-show="!chat.loading"
          @click="submitMessage"
        >
          发送
        </button>
      </div>
    </div>
  </div>
</template>
<script setup>
import { message } from "ant-design-vue";
import { LoadingOutlined } from "@ant-design/icons-vue";
import { ref, reactive, inject, defineEmits, watch, computed } from "vue";
const chat = inject("chat");
const emit = defineEmits(["submitMessage", "stopEventSouce"]);
const isLoading = ref(false);

const changeChatMessageInput = (val) => {
  chat.message = val.target.value;
};

const onClearContext = () => {
  chat.uuid = "";
  message.success("清空成功");
};

const stopSubmitConfirm = () => {
  emit("stopEventSouce");
};

const textareaEnter = (event) => {
  if (!event.shiftKey && event.keyCode == 13) {
    event.cancelBubble = true;
    event.stopPropagation();
    event.preventDefault();
    if (!chat.message || chat.loading) return;
    submitMessage();
  }
};

const submitMessage = async () => {
  emit("submitMessage");
};
</script>

<style lang="less" scoped>
.commonFormPage {
  width: 852px;
  position: relative;
  margin: 0 auto;
  border: none;
  background-color: rgba(255, 255, 255, 0.5);
  // background-color: #fff;
  backdrop-filter: blur(6px);
  border-radius: 16px;
  padding: 20px 30px;
  box-shadow: 0 1px 2px -2px #00000029, 0 3px 6px #0000001f, 0 0px 0px 0px #00000017;
  transition: box-shadow 0.3s, border-color 0.3s;
  &:focus,
  &:active,
  &:hover {
    // box-shadow: none;
    border-color: transparent;
    box-shadow: 0 1px 2px -2px rgba(0, 0, 0, 0.16), 0 3px 6px 0 rgba(0, 0, 0, 0.12),
      0 5px 12px 2px rgba(0, 0, 0, 0.09);
    // box-shadow: 0px 0px 0px 2px rgba(86, 87, 246, 0.5);
  }
  .w {
    display: flex;
    align-items: center;
    .input {
      flex: 1;
      min-width: 0;
      background-color: transparent;
      color: #3d3d3d;
      line-height: 28px;
      // border: 1px solid #5657f6;
      padding: 0 40px 0 0;
      outline: none;
      border: none;
      &:focus {
        box-shadow: none;
        border-color: transparent;
        // box-shadow: 0px 0px 0px 2px rgba(86, 87, 246, 0.5);
      }
    }

    .controller {
    }

    .submit {
      width: 72px;
      height: 36px;
      border-radius: 80px;
      background-color: #5657f6;
      text-align: center;
      line-height: 36px;
      font-size: 18px;
      color: #ffffff;

      outline: none;
      border: none;
      margin-left: 20px;
      &:disabled,
      &[disabled] {
        background-color: #bbbcfb;
      }
      &:not([disabled]):hover {
        transition: all linear 0.3s;
        background-color: #4041cd;
      }
    }
    .submit-stop {
      border: 1px solid #d9d9d9;
      background-color: #f5f5f5;
      color: #00000040;
      &:not([disabled]):hover {
        background-color: #f5f5f5;
      }
    }
  }
}
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
    background-image: url(./images/clean.png);
    background-size: 20px 20px;
  }
}
</style>

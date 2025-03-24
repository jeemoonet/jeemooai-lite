<template>
  <div class="commonFormPage">
    <div class="upload-file-list left" v-if="chatFileList.length > 0">
      <template v-for="(item, index) in chatFileList" :key="index">
        <a-spin :spinning="!item.uuid">
          <div class="item left">
            <a-image :src="item.icon" v-if="item.fileType == 'image'" />
            <img
              v-else
              class="avatar"
              :src="
                item.fileType == 'voice'
                  ? iconVoice
                  : item.fileType == 'pdf'
                  ? iconPdf
                  : item.fileType == 'docx' || item.fileType == 'doc'
                  ? iconDocx
                  : iconDocx
              "
            />
            <div class="info">
              <div class="title one_line">{{ item.fileName }}</div>
              <div class="fileSize float">
                {{ fileSize(item.fileSize) }}
                <a-popconfirm
                  title="请问是否要删除该图片?"
                  ok-text="确认"
                  cancel-text="取消"
                  @confirm="onFileDel(index)"
                >
                  <i class="ico_remove bg" v-if="item.uuid"></i>
                </a-popconfirm>
              </div>
            </div>
          </div>
        </a-spin>
      </template>
    </div>
    <div class="w">
      <a-textarea
        id="scroll_textarea"
        class="input"
        @keydown="($event) => textareaEnter($event)"
        @input="changeChatMessageInput"
        :autoSize="{ minRows: 1, maxRows: 3 }"
        row="1"
        :value="chat.message"
        placeholder="请输入问题..."
      ></a-textarea>
      <div class="controller float">
        <div class="left">
          <div
            class="reasoning iconcenter"
            :class="{
              active: chatStore.isReasoning == 0 ? false : true,
            }"
            @click="onReasoningChange"
            v-if="!getCurrentWindow.promptId"
          >
            <i class="ico_reasoning bg"></i>
            深度思考
          </div>
          <div
            class="search iconcenter"
            :class="{
              active: chatStore.isNetWork == 0 ? false : true,
            }"
            @click="onSearchChange"
          >
            <i class="ico_search bg"></i>
            联网搜索
          </div>
        </div>

        <div class="right">
          <clear-context />
          <upload-file />
          <!-- <recognize v-model:isRecognize="isRecognize" /> -->
          <button
            class="submit submit-stop iconcenter"
            v-if="chat.loading"
            @click="stopSubmitConfirm"
          >
            <div class="stop-outlined"></div>
          </button>
          <button
            class="submit iconcenter"
            :disabled="!chat.message || isRecognize ? true : false"
            v-else
            @click="submitMessage"
          ></button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, inject, defineEmits, watch, computed } from "vue";
import iconVoice from "./images/ico_voice.png";
import iconPdf from "./images/ico_pdf.png";
import iconDocx from "./images/ico_doc.png";
import iconImg from "./images/ico_img.png";
import { storeToRefs } from "pinia";
import { ChatStore } from "@/stores/chat";
const chatStore = ChatStore();
const { getCurrentWindow } = storeToRefs(chatStore);
import uploadFile from "./SendUploadFile.vue";
import clearContext from "./SendClearContext.vue";
const chat = inject("chat");
const chatFileList = inject("chatFileList");
const isRecognize = ref(false);
const emit = defineEmits(["submitMessage"]);

//删除上传的文件
const onFileDel = (index) => {
  chatFileList.value.splice(index, 1);
};

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
    if (!chat.message || chat.loading || isRecognize.value) return;
    submitMessage();
  }
};

const submitMessage = async () => {
  emit("submitMessage");
};

const onSearchChange = () => {
  chatStore.updateIsNetWork();
};

const onReasoningChange = () => {
  chatStore.updateIsReasoning();
};

const fileSize = computed(() => {
  return function (size) {
    if (!size) return "0KB";
    let num = 1024.0;
    if (size < num) return size + "B";
    if (size < Math.pow(num, 2)) return (size / num).toFixed(1) + "K";
    if (size < Math.pow(num, 3)) return (size / Math.pow(num, 2)).toFixed(1) + "M";
    if (size < Math.pow(num, 4)) return (size / Math.pow(num, 3)).toFixed(2) + "G";
    return (size / Math.pow(num, 4)).toFixed(2) + "T";
  };
});
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
    // display: flex;
    // align-items: center;
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
      margin-top: 12px;
      .search {
        width: 88px;
        height: 28px;
        border-radius: 20px;
        background: #f5f6f8;
        font-size: 12px;
        font-weight: normal;
        line-height: 16px;
        color: #1d2129;
        cursor: pointer;
        .ico_search {
          width: 16px;
          height: 16px;
          background-image: url(./images/search.png);
          margin-right: 5px;
        }
        &:hover {
          background: #d9d9d9;
        }
        &.active {
          background: #e0eaff;
          color: #5657f6;
          .ico_search {
            background-image: url(./images/search1.png);
          }
        }
      }
      .reasoning {
        width: 88px;
        height: 28px;
        border-radius: 20px;
        background: #f5f6f8;
        font-size: 12px;
        font-weight: normal;
        line-height: 16px;
        color: #1d2129;
        cursor: pointer;
        margin-right: 10px;
        .ico_reasoning {
          width: 16px;
          height: 16px;
          background-image: url(./images/reasoning.png);
          margin-right: 5px;
        }
        &:hover {
          background: #d9d9d9;
        }
        &.active {
          background: #e0eaff;
          color: #5657f6;
          .ico_reasoning {
            background-image: url(./images/reasoning1.png);
          }
        }
      }
    }

    .submit {
      width: 36px;
      height: 36px;
      line-height: 36px;
      border-radius: 50px;
      background-color: #5657f6;
      background-image: url(./images/submit1.png);
      background-position: center center;
      background-size: 18px 18px;
      background-repeat: no-repeat;
      outline: none;
      border: none;
      margin-left: 6px;
      &:disabled,
      &[disabled] {
        background-image: url(./images/submit.png);
        background-color: #e8e8e9;
      }
      &:not([disabled]):hover {
        transition: all linear 0.3s;
        background-color: #4041cd;
      }
    }
    .submit-stop {
      background-image: none;
      color: #fff;
      font-size: 18px;
      background-color: #5657f6;
      .stop-outlined {
        width: 14px;
        height: 14px;
        border-radius: 4px;
        background: #fff;
      }
      &:not([disabled]):hover {
        transition: all linear 0.3s;
        background-color: #4041cd;
        color: #fff;
      }
    }
  }

  .upload-file-list {
    width: 100%;
    margin-bottom: 12px;
    :deep(.ant-spin-nested-loading) {
      margin-right: 12px;
      &:nth-last-of-type(1) {
        margin-right: 0;
      }
    }
    .item {
      width: 148px;
      height: 48px;
      border-radius: 3px;
      background: #f5f6f8;
      padding-left: 4px;
      &:hover {
        box-shadow: 0 1px 2px -2px rgba(0, 0, 0, 0.16), 0 3px 6px 0 rgba(0, 0, 0, 0.12),
          0 5px 12px 4px rgba(0, 0, 0, 0.09);
      }

      :deep(.ant-image-img) {
        width: 40px;
        height: 40px;
        overflow: hidden;
        border-radius: 3px;
      }
      .avatar {
        width: 40px;
        height: 40px;
        border-radius: 3px;
      }
      .info {
        flex: 1;
        min-width: 0;
        padding-left: 4px;
        padding-right: 8px;
        .title {
          font-size: 12px;
          font-weight: normal;
          line-height: 16px;
          color: #1d2129;
          margin-bottom: 4px;
        }
        .fileSize {
          font-size: 12px;
          font-weight: normal;
          line-height: 16px;
          color: #1d2129;
          .ico_remove {
            width: 12px;
            height: 12px;
            background-image: url(./images/ico_remove.png);
            cursor: pointer;
            &:hover {
              background-image: url(./images/ico_remove1.png);
            }
          }
        }
      }
    }
  }
}
</style>

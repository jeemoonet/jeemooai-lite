<template>
  <!-- 初始提示语 -->
  <div
    v-if="messageValue.role == 'assistant' && messageValue.isInitMessage"
    class="chat-user-view-markdown"
    :class="type == 1 ? 'chat-user-view-markdown-white' : ''"
  >
    <div class="markdown-body">
      <img class="chat-user-avatar" src="@/assets/images/logo1.png" />
      <div class="chat-init-message pdb_15">
        <div class="init-message-content" v-html="$xss(messageValue.message)"></div>
        <div class="init-message-tips">
          <div
            class="init-message-tips-item float"
            v-for="item in messageValue.tips"
            :key="item.id"
            @click.stop="onTips(item.message)"
          >
            <div class="tips-value one_line">{{ item.title }}</div>
            <i class="ico_right bg"></i>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- 用户消息 -->
  <div v-else class="chat-user-view">
    <div class="chat-user-content">
      <img class="chat-user-avatar" :src="avatar()" />
      <div class="chat-user-info">
        <div class="chat-user-message" v-html="$xss(messageValue.message)"></div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { message } from "ant-design-vue";
import noneLogo from "@/assets/images/logo1.png";
import {
  ref,
  reactive,
  computed,
  watch,
  defineProps,
  defineEmits,
  onMounted,
  h,
  createApp,
  inject,
} from "vue";
import { storeToRefs } from "pinia";
import { InfoStore } from "@/stores/user";
import $xss from "xss";
//获取store slider数据
const { getUserInfo } = storeToRefs(InfoStore());
const axios = inject("axios");
const tipList = ref([]);
const props = defineProps({
  messageValue: {
    type: Object,
    default: () => {},
  },
  type: {
    type: String,
    default: () => 0,
  },
});

const emit = defineEmits(["submitMessage", "update:chatMessage"]);

onMounted(() => {});

const onTips = (res) => {
  emit("update:chatMessage", res || "");
};

const avatar = computed(() => {
  return () => {
    if (props.type == 1) {
      return props.messageValue.avatar || noneLogo;
    }
    return getUserInfo.value.avatar || noneLogo;
  };
});
</script>
<style scoped lang="less">
audio {
  display: block !important;
}
.chat-user-view {
  width: 100%;
}
.chat-user-view-markdown {
  width: 100%;
}
.chat-user-content {
  display: flex;
  align-items: flex-start;
  justify-content: flex-start;
  flex-wrap: wrap;
  width: 100%;
  margin: 0 auto 15px;
  white-space: pre-wrap;
  position: relative;
  .chat-user-avatar {
    display: block;
    width: 38px;
    height: 38px;
    border-radius: 50%;
    // background-color: black;
  }
  .chat-user-info {
    flex: 1;
    width: 100%;
    min-width: 0;
    margin-left: 14px;
    // overflow: hidden;
    margin-top: 7px;
  }
  .chat-user-message {
    flex: 1;
    width: 100%;
    overflow: hidden;
    word-wrap: break-word;
    word-break: break-all;
    color: #333333;
    font-weight: 500;
    font-size: 16px;
    line-height: 23px;
  }
}
.markdown-body {
  display: flex;
  align-items: flex-start;
  justify-content: flex-start;
  flex-wrap: wrap;
  width: 100%;
  min-height: 60px;
  margin: 0 auto 26px;
  background-color: transparent;
  position: relative;
  .chat-user-avatar {
    display: block;
    width: 38px;
    height: 38px;
    border-radius: 50%;
    background-color: transparent;
  }
  .chat-user-message,
  .chat-init-message {
    flex: 1;
    width: 100%;
    margin-left: 14px;
    overflow: hidden;
    word-wrap: break-word;
    word-break: break-all;
    background-color: #fff;
    border-radius: 6px;
    padding: 20px 0 0;
    .chat-content {
      padding-left: 25px;
      padding-right: 18px;
    }
    .chat-controller {
      width: 100%;
      margin-top: 20px;
      padding-bottom: 14px;
      padding-left: 25px;
      padding-right: 18px;
      .reset {
        font-size: 14px;
        font-weight: 400;
        color: #9599a5;
        line-height: 24px;
        cursor: pointer;

        .ico_reset {
          display: block;
          width: 18px;
          height: 18px;
          margin-right: 4px;
          background-image: url(../../../assets/images/ico_reset.png);
        }
      }
      .controller {
        .ico_del {
          display: block;
          width: 18px;
          height: 18px;
          background-image: url(../../../assets/images/ico_del.png);
          cursor: pointer;
          margin-right: 14px;
        }
        .ico_copy {
          display: block;
          width: 18px;
          height: 18px;
          background-image: url(../../../assets/images/ico_copy.png);
          cursor: pointer;
        }
        .like {
          margin-left: 15px;
          padding-left: 14px;
          position: relative;
          border-left: 1px solid #e8e8e9;
          .ico_like {
            display: block;
            width: 18px;
            height: 18px;
            background-image: url(../../../assets/images/ico_like.png);
            margin-right: 14px;
            cursor: pointer;
            &.active {
              background-image: url(../../../assets/images/ico_like_active.png);
            }
          }
          .ico_unlike {
            display: block;
            width: 18px;
            height: 18px;
            background-image: url(../../../assets/images/ico_like.png);
            transform: rotate(180deg);
            cursor: pointer;
            margin-top: 2px;
            &.active {
              background-image: url(../../../assets/images/ico_like_active.png);
            }
          }
        }
      }
    }
    .chat-docInfo {
      width: 100%;
      height: 38px;
      background-color: rgba(30, 37, 56, 0.1);
      padding-left: 25px;
      padding-right: 18px;
      font-size: 14px;
      font-weight: 400;
      color: #333333;
      line-height: 24px;
      .chat-docInfo-title {
        flex: 0 0 auto;
        padding-right: 10px;
        .ico_doc {
          display: block;
          width: 18px;
          height: 18px;
          margin-right: 2px;
        }
      }

      .chat-docInfo-list {
        display: flex;
        align-items: center;
        justify-content: flex-start;
        max-width: calc(100% - 100px);
        flex: auto;
        overflow-x: auto;
        .item {
          flex-shrink: 0;
          border-right: 1px solid #333;
          padding-right: 4px;
          margin-right: 4px;
          line-height: 1;
          &:nth-last-of-type(1) {
            border: none;
          }
          .link {
            color: #5657f6;
            text-decoration: underline;
          }
        }
      }
    }
  }
  .chat-init-message {
    padding-left: 25px;
    padding-right: 18px;
    .init-message-tips {
      display: flex;
      justify-content: flex-start;
      flex-wrap: wrap;
      margin-top: 18px;
      .init-message-tips-item {
        width: 23%;
        height: 40px;
        background-color: #f5f6f8;
        padding: 0 10px 0 12px;
        margin-bottom: 8px;
        cursor: pointer;
        transition: all linear 0.2s;
        margin-right: 2.6%;
        &:nth-of-type(4n) {
          margin-right: 0;
        }
        &:hover {
          box-shadow: 0 3px 8px rgba(0, 0, 0, 0.2);
        }
        .tips-value {
          flex: 1;
          font-weight: 500;
          font-size: 14px;
          color: #333333;
        }
        .ico_right {
          width: 17px;
          height: 17px;
          background-image: url(../../../../assets/images/ico_right1.png);
        }
      }
    }
  }
}
.pdb_15 {
  padding-bottom: 15px !important;
}
.markdown-body .highlight pre,
.markdown-body pre {
  background-color: #000000 !important;
}

.chat-user-view-markdown-white {
  .markdown-body {
    .chat-user-message,
    .chat-init-message {
      background-color: #f5f6f8;
    }
  }
}
</style>

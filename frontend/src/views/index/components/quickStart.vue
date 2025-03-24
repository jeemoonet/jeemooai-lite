<template>
  <a-modal
    class="quickStartModal"
    :visible="visible"
    width="522px"
    :title="modalTitle"
    :closable="true"
    :footer="null"
    :maskClosable="false"
    @cancel="closeModal"
  >
    <div class="quickStart">
      <swiper
        class="quickStart_swiper"
        :modules="modules"
        :loop="true"
        :pagination="{ clickable: true }"
        :autoplay="{ delay: 3000, disableOnInteraction: false }"
        @swiper="onSwiper"
        @slideChange="onSlideChange"
      >
        <swiper-slide class="item">
          <p>
            工作助手通过聊天方式，完成某个任务，比如：知识问答、AI生图、翻译、创意文案等。
          </p>
          <img src="../images/1.png" alt="" />
        </swiper-slide>
        <swiper-slide class="item">
          <p>
            通过提示词，自己设计工作助手，包括提示词、欢迎语、大模型参数、是否分享他人。
          </p>
          <img src="../images/2.png" alt="" />
        </swiper-slide>
        <swiper-slide class="item">
          <p>打开知识库，上传文档或提供网站地址，就可以把自己的知识教给工作助手。</p>
          <img src="../images/3.png" alt="" />
        </swiper-slide>
      </swiper>
    </div>
    <div class="quickStart_footer float">
      <a-checkbox @change="onChange">下次登录不再提示</a-checkbox>
      <a target="_blank" :href="setting.jeemooHelpUrl" class="more right"
        >了解更多<i class="bg question"></i
      ></a>
    </div>
  </a-modal>
</template>
<script setup>
import { message } from "ant-design-vue";
import {
  ref,
  reactive,
  onMounted,
  defineProps,
  defineEmits,
  watch,
  inject,
  computed,
  nextTick,
} from "vue";
import setting from "@/utils/setting";
import { InfoStore } from "@/stores/user";
import { storeToRefs } from "pinia";
const infoStore = InfoStore();
const { getIsAlert } = storeToRefs(infoStore);
import { Swiper, SwiperSlide } from "swiper/vue";
import { Pagination, A11y, Autoplay } from "swiper";
import "swiper/css";
import "swiper/css/pagination";
const modules = [Pagination, A11y, Autoplay];
const axios = inject("axios");
const swiperIndex = ref(0);
const visible = ref(false);
onMounted(() => {});

const onSwiper = (res) => {};

const onSlideChange = (res) => {
  swiperIndex.value = res.realIndex;
};

const onChange = async (e) => {
  try {
    let res = await axios.post("/api/user/alertTag", {
      isAlert: e.target.checked ? 1 : 0,
    });
  } catch (err) {}
};

const closeModal = () => {
  visible.value = false;
};

const modalTitle = computed(() => {
  return swiperIndex.value == 1
    ? "如何创建我的工作助手？"
    : swiperIndex.value == 2
    ? "如何使用私有知识库？"
    : "什么是工作助手";
});

watch(
  getIsAlert,
  (newVal, oldVal) => {
    if (!newVal) {
      visible.value = true;
      nextTick(() => {
        infoStore.updateIsAlert(true);
      });
    }
  },
  { deep: true, immediate: true }
);
</script>

<style lang="less" scoped>
:global(.quickStartModal .ant-modal-body) {
  padding-left: 0;
  padding-right: 0;
  padding-bottom: 0;
}
.quickStartModal {
  .quickStart {
    padding: 0 30px 0;
    border-bottom: 1px solid #e8e8e9;
  }
  .quickStart_swiper {
    padding-bottom: 48px;
    .item {
      font-size: 14px;
      font-weight: 400;
      color: #3d3d3d;
      line-height: 20px;
      img {
        width: 100%;
        margin-top: 20px;
      }
    }
    :deep(.swiper-pagination) {
      .swiper-pagination-bullet {
        position: relative;
        background-color: transparent;
        width: 24px;
        height: 20px;
        opacity: 1;
        margin: 0;
        &::after {
          content: "";
          width: 8px;
          height: 8px;
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%);
          border-radius: 50%;
          background-color: rgba(0, 0, 0, 0.2);
        }
        // background-color: rgba(255, 255, 255, 0.5);
      }
      .swiper-pagination-bullet-active {
        // background-color: #fff;
        &::after {
          content: "";
          background-color: #007aff;
        }
      }
    }
  }

  .quickStart_footer {
    padding: 0 30px 0;
    height: 50px;

    .more {
      font-size: 14px;
      font-weight: 400;
      color: #5657f6;
      line-height: 20px;
      cursor: pointer;
    }
    .question {
      width: 18px;
      height: 18px;
      background-image: url(../../../assets/images/ico_question2.png);
      margin-left: 2px;
    }
  }
}
</style>

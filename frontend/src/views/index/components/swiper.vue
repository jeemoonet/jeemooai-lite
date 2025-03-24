<template>
  <div class="indexSwiperPage">
    <swiper
      class="indexSwiper"
      :modules="modules"
      :loop="true"
      :pagination="{ clickable: true }"
      :autoplay="{ delay: 3000, disableOnInteraction: false }"
    >
      <swiper-slide @click.stop="onAlert(setting.jeemooHelpUrl)" class="item">
        <img src="../images/banner_1.png" alt="" />
        <div class="swiper-slide-mask"></div>
      </swiper-slide>
      <swiper-slide @click.stop="onRoute('')" class="item">
        <img src="../images/banner_2.png" alt="" />
        <div class="swiper-slide-mask"></div>
      </swiper-slide>
    </swiper>
  </div>
</template>
<script setup>
import { ref, onMounted, inject, nextTick, defineEmits } from "vue";
import { useRouter } from "vue-router";
import setting from "@/utils/setting";
//swiper
import { Swiper, SwiperSlide } from "swiper/vue";
import { Pagination, A11y, Autoplay } from "swiper";
import "swiper/css";
import "swiper/css/pagination";
const router = useRouter();
import { InfoStore } from "@/stores/user";
const infoStore = InfoStore();
const axios = inject("axios");
const modules = [Pagination, A11y, Autoplay];
const list = ref([]);
onMounted(() => {});
const onRoute = () => {
  router.push({
    name: "chat",
  });
};
const onAlert = (url) => {
  // window.open(url);
  infoStore.updateIsAlert(false);
};
</script>
<style lang="less" scoped>
.indexSwiperPage {
  width: 372px;
  position: relative;
  height: 180px;
  .indexSwiper {
    width: 100%;
    height: 100%;
    border-radius: 6px;
    .item {
      height: 100%;
      background-color: #fff;

      cursor: pointer;
      overflow: hidden;
      position: relative;
      > img {
        display: block;
        width: 100%;
        height: 100%;
      }
      .swiper-slide-mask {
        width: 100%;
        height: 100%;
        background: linear-gradient(
          180deg,
          rgba(44, 52, 75, 0) 30%,
          rgba(44, 52, 75, 0.6) 100%
        );
        position: absolute;
        top: 0;
        left: 0;
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
          background-color: rgba(255, 255, 255, 0.5);
        }
        // background-color: rgba(255, 255, 255, 0.5);
      }
      .swiper-pagination-bullet-active {
        // background-color: #fff;
        &::after {
          content: "";
          background-color: #fff;
        }
      }
    }
  }
}
</style>

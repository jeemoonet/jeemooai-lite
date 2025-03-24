<template>
  <div class="searchInfoList">
    <div class="searchInfoListTitle left" @click="isShow = !isShow">
      <img src="./images/success.png" class="ico_success" />
      搜索到{{ searchInfoList.length || 0 }}篇参考内容
      <img
        src="./images/ico_select.png"
        class="ico_select"
        :class="isShow ? '' : 'ico_select_close'"
      />
    </div>
    <div class="searchInfoList_c" v-if="isShow">
      <div class="btn_scroll_left" @click="onTabScroll('left')">
        <img src="./images/ico_select.png" class="btn" />
      </div>
      <div class="btn_scroll_right" @click="onTabScroll('right')">
        <img src="./images/ico_select.png" class="btn" />
      </div>
      <div class="searchInfoList-w" ref="scrollTabRef">
        <a
          :href="item.url"
          target="_blank"
          class="item one_line"
          v-for="(item, index) in searchInfoList"
          :key="index"
        >
          <div class="title one_line">{{ index + 1 }}.{{ item.title || "-" }}</div>
          <div class="link more_line">{{ item.url || "-" }}</div>
        </a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, inject, defineProps, defineEmits, computed } from "vue";
const props = defineProps({
  searchInfoList: {
    type: Array,
    default: () => [],
  },
});
const scrollTabRef = ref(null);
const isShow = ref(false);

const onTabScroll = (type) => {
  const container = scrollTabRef.value;
  if (container) {
    const currentScroll = container.scrollLeft;
    const maxScroll = container.scrollWidth - container.clientWidth;
    const scrollAmount = 230;
    let targetScroll;

    if (type === "left") {
      targetScroll = currentScroll - scrollAmount;
      if (targetScroll < 0) {
        targetScroll = 0;
      }
    } else if (type === "right") {
      targetScroll = currentScroll + scrollAmount;
      if (targetScroll > maxScroll) {
        targetScroll = maxScroll;
      }
    }

    smoothScroll(container, targetScroll, 300); // 300ms 是动画持续时间
  }
};

const smoothScroll = (element, target, duration) => {
  let start = null;
  const initialScroll = element.scrollLeft;
  const step = (timestamp) => {
    if (!start) start = timestamp;
    const progress = timestamp - start;
    const percent = Math.min(progress / duration, 1);
    element.scrollLeft = initialScroll + (target - initialScroll) * percent;
    if (progress < duration) {
      window.requestAnimationFrame(step);
    }
  };
  window.requestAnimationFrame(step);
};
</script>

<style scoped lang="less">
.searchInfoList {
  width: 100%;
  padding-left: 25px;
  padding-right: 18px;
}
.searchInfoListTitle {
  font-size: 14px;
  font-weight: normal;
  line-height: 16px;
  color: #3d3d3d;
  margin-bottom: 18px;
  cursor: pointer;
  -moz-user-select: none; /*火狐*/
  -webkit-user-select: none; /*webkit浏览器*/
  -ms-user-select: none; /*IE10*/
  -khtml-user-select: none; /*早期浏览器*/
  user-select: none;
  .ico_success {
    display: block;
    width: 15px;
    height: 15px;
    margin-right: 5px;
  }
  .ico_select {
    display: block;
    width: 12px;
    height: 7px;
    margin-left: 6px;
    transition: transform 0.3s ease;
    &.ico_select_close {
      transform: rotate(180deg);
    }
  }
}
.searchInfoList_c {
  width: 100%;
  height: 75px;
  margin-bottom: 18px;
  position: relative;
  .btn_scroll_left {
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background: #fff;
    box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
    position: absolute;
    left: -6px;
    top: 50%;
    transform: translateY(-50%);
    cursor: pointer;
    .btn {
      width: 11px;
      height: 6px;
      transform: translateX(2.5px) translateY(-4px) rotate(90deg);
    }
  }
  .btn_scroll_right {
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background: #fff;
    box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
    position: absolute;
    right: -6px;
    top: 50%;
    transform: translateY(-50%);
    cursor: pointer;
    .btn {
      width: 11px;
      height: 6px;
      transform: translateX(5px) translateY(-4px) rotate(270deg);
    }
  }
}
.searchInfoList-w {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  width: 100%;
  height: 100%;
  overflow-x: auto;
  .item {
    width: 220px;
    height: 100%;
    flex-shrink: 0;
    margin-right: 10px;
    border-radius: 12px;
    background: #f8f8f8;
    text-decoration: none;
    padding: 9px 8px 0 10px;
    transition: all 0.3s ease;
    &:hover {
      background: #dcdcdc;
      box-shadow: 0px 2px 8px 0px rgba(228, 228, 244, 0.6);
    }
    .title {
      font-size: 12px;
      font-weight: normal;
      line-height: 16px;
      color: #1d2129;
      margin-bottom: 8px;
    }
    .link {
      height: 32px;
      font-size: 12px;
      font-weight: normal;
      line-height: 16px;
      color: rgba(29, 33, 41, 0.4);
    }
  }
}
</style>

<template>
  <div class="newsListPage">
    <div class="header float">
      <div class="title left">
        <i class="ico_news bg"></i>
        新闻公告
      </div>
      <router-link to="/404" class="link bg"></router-link>
    </div>
    <div class="list">
      <router-link
        v-for="(item, index) in list"
        :key="index"
        class="item one_line"
        to="/404"
        >{{ item }}</router-link
      >
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted, inject, nextTick } from "vue";
const list = ref([]);

onMounted(() => {
  getNewsData();
});

const getNewsData = async () => {
  try {
    list.value = ["企业大脑V1.0正式发布！"];
  } catch (err) {}
};
</script>

<style lang="less" scoped>
.newsListPage {
  width: 100%;
  margin-top: 20px;
  .header {
    width: 100%;
    .title {
      font-size: 18px;
      font-weight: 500;
      color: #3d3d3d;
      line-height: 26px;
      .ico_news {
        width: 24px;
        height: 24px;
        background-color: #5657f6;
        border-radius: 50%;
        margin-right: 8px;
        background-image: url(../../../assets/images/ico_news.png);
        background-size: 16px 16px;
      }
    }
    .link {
      width: 17px;
      height: 17px;
      background-image: url(../../../assets/images/ico_link.png);
    }
  }
  .list {
    width: 100%;
    height: 264px;
    background-color: #fff;
    padding: 7px 20px 7px;
    overflow-y: auto;
    background-color: #fff;
    margin-top: 15px;
    .item {
      display: block;
      width: 100%;
      border-bottom: 1px solid #e8e8e9;
      padding: 13px 0;
      font-size: 16px;
      font-weight: 400;
      color: #3d3d3d;
      line-height: 23px;
      &:nth-last-of-type(1) {
        border: none;
      }
    }
  }
}
</style>

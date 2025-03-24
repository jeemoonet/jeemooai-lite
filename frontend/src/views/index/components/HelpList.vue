<template>
  <div class="helpListPage">
    <router-link class="header bg" to="/404"></router-link>
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
  getHelpData();
});

const getHelpData = async () => {
  try {
    list.value = [
      "以前写的内容去哪了？",
      "跑半天跑不出来怎么办？",
      "如何提高原创度？",
      "一次性可以写多长的文章？",
      "论文怎么写？",
    ];
  } catch (err) {}
};
</script>

<style lang="less" scoped>
.helpListPage {
  width: 100%;
  background-color: #fff;
  .header {
    width: 100%;
    height: 113px;
    background-image: url(../../../assets/images/help_bg.png);
  }
  .list {
    width: 100%;
    height: 264px;
    background-color: #fff;
    padding: 7px 20px 7px;
    overflow-y: auto;
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

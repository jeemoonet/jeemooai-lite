<template>
  <div class="indexRecentPage">
    <div class="recent" :class="isOpen ? 'recent_unfold' : ''">
      <div class="recent_w">
        <div class="title">常用助手</div>
        <div class="list all">
          <template v-for="(item, index) in list" :key="index">
            <div
              class="item"
              v-show="index > 6 && !isOpen ? false : true"
              @click="onTap(item)"
            >
              <div class="icon iconcenter">
                <img :src="item.promptIcon" class="ico_logo" alt="" />
              </div>
              <div class="title one_line">{{ item.promptName || "暂无标题" }}</div>
            </div>
          </template>
        </div>
        <div class="collapse iconcenter" v-if="list.length > 7" @click="isOpen = !isOpen">
          <i class="ico_unfold bg"></i>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted, inject, nextTick, defineEmits } from "vue";
const axios = inject("axios");
const emit = defineEmits(["onPromptItem"]);
const list = ref([]);
const isOpen = ref(false);
onMounted(() => {
  getData();
});

const onTap = (item) => {
  emit("onPromptItem", item);
};

const getData = async () => {
  try {
    let res = await axios.get(`/api/home/recent`);
    let data = res.data.data || [];
    list.value = data;
  } catch (err) {
  } finally {
  }
};
</script>
<style lang="less" scoped>
.indexRecentPage {
  width: 810px;
  position: relative;
  height: 180px;
  background-color: #fff;
  border-radius: 6px;
  .recent {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 180px;
    background-color: #fff;
    transition: all ease-out 0.1s;
    z-index: 2;
    .recent_w {
      width: 100%;
      height: 100%;
      padding: 28px 72px 0 18px;
      overflow: hidden;
      position: relative;
    }
    .title {
      font-size: 18px;
      font-weight: bold;
      color: #333333;
      line-height: 18px;
    }
    .list {
      width: 100%;
      margin-top: 18px;
      .item {
        width: 84px;
        margin-right: 22px;
        margin-bottom: 18px;
        cursor: pointer;
        position: relative;
        &:nth-of-type(7n) {
          margin-right: 0;
        }
        .icon {
          width: 64px;
          height: 64px;
          background-color: #f5f6f8;
          border-radius: 6px;
          margin: 0 auto;
          .ico_logo {
            display: block;
            width: 32px;
            height: 32px;
            font-size: 32px;
            line-height: 32px;
          }
        }
        .title {
          font-size: 14px;
          font-weight: 400;
          color: #333333;
          line-height: 18px;
          margin-top: 6px;
          text-align: center;
        }
        &:hover {
          &::after {
            content: "";
            width: 105px;
            height: 105px;
            background: #ffffff;
            box-shadow: 0px 0px 6px 0px rgba(192, 192, 192, 0.6);
            border-radius: 6px 6px 6px 6px;
            position: absolute;
            left: -10px;
            top: -10px;
            z-index: -1;
          }
          .title {
            color: #5657f6;
          }
        }
      }
    }
    .collapse {
      width: 18px;
      height: 64px;
      border-radius: 6px;
      background-color: #f5f6f8;
      position: absolute;
      right: 28px;
      bottom: 52px;
      cursor: pointer;
      .ico_unfold {
        width: 6px;
        height: 8px;
        background-image: url(../../../assets/images/ico_unfold.png);
        transition: all linear 0.3s;
      }
    }
  }
  .recent_unfold {
    height: 286px;
    .collapse {
      .ico_unfold {
        transform: rotate(180deg);
      }
    }
  }
}
</style>

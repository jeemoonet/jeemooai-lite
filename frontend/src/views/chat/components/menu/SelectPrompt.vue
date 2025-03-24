<template>
  <div class="select-prompt" v-show="list.length != 0">
    <div class="select-prompt-btn iconcenter" @click="isOpen = !isOpen">
      <i class="ico_select bg"></i>
      选择助手
    </div>
    <div class="prompt-select-dropdown" v-show="isOpen">
      <template v-for="(item, index) in list" :key="index">
        <div class="item left">
          <p class="one_line">{{ item.categoryName }}</p>
          <i class="ico_link bg"></i>
          <div class="sub-select-list">
            <div
              class="sub-select-list_w"
              :style="{
                width:
                  (item.children.length >= 4 ? 4 : item.children.length) * 152 +
                  40 +
                  'px',
              }"
            >
              <template v-for="(itm, idx) in item.children" :key="idx">
                <div class="sub_item left" @click="onTap(itm)">
                  <img :src="itm.promptIcon" class="prompt_icon" alt="" />
                  <p class="one_line">{{ itm.promptName }}</p>
                  <div class="tag" v-if="itm.isMy">我的</div>
                </div>
              </template>
            </div>
          </div>
        </div>
      </template>
    </div>
  </div>
</template>
<script setup>
import { inject, ref, reactive, onMounted, defineProps, defineEmits } from "vue";
const emit = defineEmits(["onChange"]);
const props = defineProps({
  list: {
    type: Array,
    default: () => [],
  },
});
const isOpen = ref(false);

const onTap = (res) => {
  isOpen.value = false;
  emit("onChange", res.promptId);
};
</script>
<style lang="less" scoped>
.select-prompt {
  width: 100%;
  padding: 0 20px;
  margin-bottom: 20px;
  position: relative;
  * {
    user-select: none;
  }
}
.select-prompt-btn {
  width: 100%;
  height: 42px;
  background: rgba(86, 87, 246, 0.1);
  background: rgba(86, 87, 246, 0.1);
  border-radius: 6px;
  font-size: 16px;
  font-weight: 500;
  color: #5657f6;
  line-height: 23px;
  cursor: pointer;
  transition: all linear 0.2s;
  .ico_select {
    width: 18px;
    height: 18px;
    margin-right: 5px;
    background-image: url(./images/ico_select.png);
  }
}

.prompt-select-dropdown {
  width: 220px;
  padding: 14px 0;
  background-color: #2c344b;
  border-radius: 6px;
  position: absolute;
  bottom: 52px;
  left: 20px;
  .item {
    width: 100%;
    padding: 0 18px;
    height: 42px;
    cursor: pointer;
    transition: all ease 0.2s;
    position: relative;
    .prompt_icon {
      width: 18px;
      height: 18px;
      margin-right: 9px;
      font-size: 18px;
      line-height: 18px;
      color: #9599a5;
    }
    p {
      flex: 1;
      font-size: 16px;
      font-weight: 400;
      color: #9599a5;
      line-height: 23px;
    }
    .ico_link {
      width: 18px;
      height: 18px;
      background-image: url(../../../../assets/images/ico_right1.png);
    }
    &:hover {
      background-color: #1e2538;
      p {
        color: #ffffff;
      }
      .sub-select-list {
        display: flex;
      }
    }
    &:nth-last-of-type(1) {
      // .sub-select-list {
      //   display: block;
      // }
    }
  }
  .sub-select-list {
    display: none;
    position: absolute;
    bottom: 0;
    left: 220px;
    z-index: 3;
    padding-left: 10px;

    .sub-select-list_w {
      display: flex;
      flex-wrap: wrap;
      //   width: 638px;
      max-height: 185px;
      padding: 20px 20px 10px;
      // background-color: #2c344b;
      background: #ffffff;
      box-shadow: 0px 4px 20px 0px rgba(149, 153, 165, 0.2);
      border-radius: 6px;
      overflow-y: auto;
      .sub_item {
        width: 142px;
        height: 42px;
        border-radius: 6px;
        background: #f5f6f8;
        border: 1px solid #ebeef5;
        margin-bottom: 10px;
        padding: 0 12px;
        margin-right: 10px;
        position: relative;
        overflow: hidden;
        .prompt_icon {
          width: 18px;
          height: 18px;
          margin-right: 6px;
          font-size: 18px;
          line-height: 18px;
          color: #ffffff;
        }
        p {
          flex: 1;
          font-size: 12px;
          font-weight: 400;
          color: #333333;
          line-height: 16px;
        }
        .tag {
          width: 45px;
          height: 13px;
          line-height: 13px;
          background-color: #9599a5;
          font-size: 10px;
          font-weight: 500;
          color: #ffffff;
          text-align: center;

          transform: rotate(45deg);
          position: absolute;
          right: -12px;
          top: 4px;
        }
        &:nth-of-type(4n) {
          margin-right: 0;
        }
        &:hover {
          //   border: none;
          background: #ecedef;
          p {
            color: #5657f6;
          }
        }
      }
    }
  }
}
</style>

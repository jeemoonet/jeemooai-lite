<template>
  <div class="select-prompt" v-show="userPromptList.length != 0">
    <div class="select-prompt-btn iconcenter" @click="visible = true">
      <i class="ico_select bg"></i>
      选择助手
    </div>
  </div>
  <a-drawer
    v-model:visible="visible"
    :closable="true"
    :maskClosable="true"
    title="助手列表"
    placement="left"
    :width="322"
  >
    <!-- <template #extra>
      <a-button type="primary" @click="onClose">确认</a-button>
    </template> -->
    <a-spin :spinning="loading">
      <template v-for="(pitem, pindex) in userPromptList" :key="pindex">
        <div class="tips">{{ pitem.categoryName }}</div>
        <div class="model_list">
          <div
            class="item left"
            :class="item.check ? 'active' : ''"
            v-for="(item, index) in pitem.children"
            :key="index"
            @click="onSelect(item)"
          >
            <img :src="item.promptIcon" alt="" />
            <p class="one_line">{{ item.promptName }}</p>
            <!-- <div class="tag" v-if="item.isMy">我的</div> -->
          </div>
        </div>
      </template>
    </a-spin>
  </a-drawer>
</template>
<script setup>
import { message } from "ant-design-vue";
import {
  ref,
  onMounted,
  inject,
  defineProps,
  nextTick,
  watch,
  computed,
  toRaw,
} from "vue";
const axios = inject("axios");
const emit = defineEmits(["onChange"]);

const userPromptList = ref([]);
const visible = ref(false);
const loading = ref(true);

onMounted(() => {
  getUserPromptList();
});

const onSelect = (res) => {
  visible.value = false;
  emit("onChange", res.promptId);
};

const getUserPromptList = async () => {
  try {
    let res = await axios.get("/api/chat/prompt");
    let data = res.data.data || [];
    userPromptList.value = data;
  } catch (err) {
  } finally {
    loading.value = false;
  }
};

const onClose = () => {
  visible.value = false;
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
.tips {
  font-size: 14px;
  font-weight: normal;
  line-height: 24px;

  color: #1d2129;
  margin-bottom: 10px;
  text-align: left;

  span {
    color: #5657f6;
  }
}
.model_list {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  flex-wrap: wrap;
  .item {
    width: 46%;
    height: 40px;
    border-radius: 8px;
    background: #f5f6f8;
    padding: 0 12px 0;
    margin-bottom: 16px;
    cursor: pointer;
    transition: all 0.15s ease-in-out;
    position: relative;
    overflow: hidden;
    &:hover {
      // border: 1px solid rgba(86, 87, 246, 0.4);
      box-shadow: 0 1px 2px -2px #00000029, 0 3px 6px #0000001f, 0 0px 0px 0px #00000017;
    }
    &.active {
      border: 1px solid rgba(86, 87, 246, 1);
      &:hover {
        box-shadow: none;
      }
    }
    img {
      display: block;
      width: 24px;
      height: 24px;
      margin-right: 10px;
      border-radius: 50%;
    }
    p {
      flex: 1;
      min-width: 0;
      font-size: 14px;
      font-weight: 500;
      color: #1d2129;
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
  }
}
</style>

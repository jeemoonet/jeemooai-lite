<template>
  <div class="headerMenu iconcenter">
    <div class="headerMenu_w float">
      <div class="title">{{ prompt.name }}</div>
      <div class="setting right" @click="onOpen">
        <MenuUnfoldOutlined />
        配置大模型
      </div>
    </div>
  </div>
  <a-drawer
    v-model:visible="visible"
    :closable="false"
    :maskClosable="false"
    class="order-drawer"
    title="模型列表"
    placement="right"
    :width="422"
  >
    <template #extra>
      <a-button type="primary" @click="onClose">确认</a-button>
    </template>
    <a-spin :spinning="loading">
      <div class="tips"><span>*</span>最多支持配置6个大模型</div>
      <div class="model_list">
        <div
          class="item left"
          :class="item.check ? 'active' : ''"
          v-for="(item, index) in modelList"
          :key="index"
          @click="onSelect(item)"
        >
          <img :src="item.icon" alt="" />
          <p class="one_line">{{ item.modelName }}</p>
        </div>
      </div>
    </a-spin>
  </a-drawer>
</template>
<script setup>
import { MenuUnfoldOutlined } from "@ant-design/icons-vue";
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
const models = inject("models");
const props = defineProps({
  prompt: {
    type: Object,
    default: () => {},
  },
});
const visible = ref(true);
const loading = ref(true);
const modelList = ref([]);
onMounted(() => {
  getModelsData();
});

const onSelect = (res) => {
  let _list = toRaw(modelList.value);
  let _checkList = _list.filter((item) => item.check);

  modelList.value = _list.map((item) => {
    if (item.modelLabel == res.modelLabel) {
      if (_checkList.length >= 6 && !item.check) {
        message.warning("最多选择6个模型");
        return item;
      }
      item.check = !item.check;
    }
    return item;
  });
};

const getModelsData = async () => {
  try {
    let res = await axios.get("/api/home/modelList");
    let data = res.data.data || [];
    data = data.map((item) => {
      if (item.modelLabel == props.prompt.model) {
        item.check = true;
      }
      return item;
    });
    modelList.value = data || [];
  } catch (err) {
  } finally {
    loading.value = false;
  }
};

const onOpen = () => {
  visible.value = true;
};

const onClose = () => {
  let _list = toRaw(modelList.value);
  let _checkList = _list.filter((item) => item.check);
  if (_checkList.length == 0) {
    message.warning("最少选择1个模型");
    return;
  }
  modelList.value = _checkList;
  models.value = _checkList;
  visible.value = false;
};
</script>

<style lang="less" scoped>
.headerMenu {
  width: 100%;
  height: 56px;
  background: linear-gradient(180deg, #5657f6 0%, #4d4eee 100%);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 3;
  &.hide {
    display: none;
  }
  &.iconcenter {
    display: flex;
  }
  .headerMenu_w {
    width: 100%;
    height: 100%;
    padding: 0 20px;
    overflow: hidden;
    .title {
      font-size: 18px;
      font-weight: normal;
      line-height: 24px;
      color: #ffffff;
    }
    .setting {
      font-size: 16px;
      font-weight: normal;
      line-height: 24px;
      color: #ffffff;
      cursor: pointer;
      :deep(.anticon) {
        margin-right: 10px;
        font-size: 14px;
      }
    }
  }
}
.tips {
  font-size: 14px;
  font-weight: normal;
  line-height: 24px;

  color: #1d2129;
  margin-bottom: 20px;
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
  }
}
</style>

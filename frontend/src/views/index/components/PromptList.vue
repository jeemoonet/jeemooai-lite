<template>
  <div class="promptListPage">
    <div class="header float">
      <div class="title">工作助手</div>
      <div class="filter right">
        <a-select
          v-model:value="params.orderByColumn"
          class="filter-select"
          :bordered="false"
          @change="onSearch"
        >
          <a-select-option value="isNew">按最新</a-select-option>
          <a-select-option value="isRecommend">按推荐</a-select-option>
        </a-select>
        <a-input-group class="search">
          <a-input
            @pressEnter="onSearch"
            v-model:value="params.keywords"
            :maxlength="20"
            placeholder="请输入您要查找的内容..."
          />
          <i class="ico_search bg" @click="onSearch"></i>
        </a-input-group>
      </div>
    </div>
    <div class="prompt">
      <div class="tabbar">
        <div class="tabbar_w">
          <div
            class="item"
            :class="!params.categoryId ? 'active' : ''"
            @click="onTab('')"
          >
            全部
          </div>
          <div
            class="item"
            :class="params.categoryId == item.categoryId ? 'active' : ''"
            v-for="(item, index) in categoryList"
            :key="index"
            @click="onTab(item.categoryId)"
          >
            {{ item.categoryName }}
          </div>
        </div>
      </div>
      <div class="list all" ref="scrollDom">
        <div class="item" v-for="(item, index) in list" :key="index" @click="onTap(item)">
          <div class="icon iconcenter">
            <img :src="item.promptIcon" alt="" />
          </div>
          <div class="info">
            <div class="title left">
              <p class="one_line">{{ item.promptName }}</p>
              <div class="tag new iconcenter" v-if="item.isNew == 1">最新</div>
              <div class="tag recommend iconcenter" v-if="item.isRecommend == 1">
                推荐
              </div>
            </div>
            <div class="desc more_line">{{ item.promptDesc }}</div>
          </div>
        </div>
        <empty-data v-show="list.length == 0" />
      </div>
    </div>
  </div>
</template>
<script setup>
import { message, Modal } from "ant-design-vue";
import {
  ref,
  reactive,
  onMounted,
  inject,
  nextTick,
  defineEmits,
  onBeforeUnmount,
} from "vue";
import { throttle } from "@/utils/utils";
import EmptyData from "@/components/EmptyData.vue";
const axios = inject("axios");
const emit = defineEmits(["onPromptItem"]);
//滚动dom实例
const scrollDom = ref(null);
//分类列表
const categoryList = ref([]);
const list = ref([]);
//分页
const pagination = reactive({
  pageSize: 15,
  pageNum: 1,
  total: 0,
  lock: false,
});
const params = reactive({
  categoryId: "",
  orderByColumn: "isNew",
  keywords: "",
  isAsc: "desc",
});

onMounted(() => {
  getCategoryList();
  getHomePrompt();
  scrollDom.value.addEventListener("scroll", throttle(scrollToBottom, 100));
});

const onTab = (categoryId = "") => {
  params.categoryId = categoryId;
  resetPromptList();
};

const onSearch = () => {
  resetPromptList();
};

const resetPromptList = () => {
  // list.value = [];
  pagination.pageNum = 1;
  pagination.total = 0;
  getHomePrompt();
};

const getHomePrompt = async () => {
  try {
    let res = await axios.get("/api/home/promptList", {
      params: {
        ...params,
        ...pagination,
      },
    });
    let total = res.data.total || 0;
    let data = res.data.rows || [];
    if (pagination.pageNum == 1) {
      list.value = data;
    } else {
      list.value.push(...data);
    }

    pagination.total = total;
    pagination.pageNum += 1;
  } catch (err) {
  } finally {
  }
};

//获取角色分类
const getCategoryList = async (res) => {
  try {
    let res = await axios.get("/api/home/categoryList");
    let data = res.data.data || [];
    categoryList.value = data || [];
  } catch (err) {}
};

const onTap = (item) => {
  emit("onPromptItem", item);
};

const scrollToBottom = () => {
  if ((pagination.pageNum - 1) * pagination.pageSize >= pagination.total) return;
  let scrollHeight = scrollDom.value.scrollHeight;
  let windowHeight = scrollDom.value.clientHeight;
  let scrollTop = scrollDom.value.scrollTop;
  let scrollBottom = scrollHeight - windowHeight - scrollTop;
  if (scrollBottom <= 140) {
    getHomePrompt();
  }
};

onBeforeUnmount(() => {
  scrollDom.value.removeEventListener("scroll", throttle(scrollToBottom, 100));
});
</script>
<style lang="less" scoped>
.promptListPage {
  width: 100%;
  background-color: #fff;
  margin-top: 18px;
  padding-top: 19px;
  border-radius: 6px 6px 0px 0px;
  .header {
    width: 100%;
    padding: 0 28px 9px;
    .title {
      font-size: 18px;
      font-weight: bold;
      color: #333333;
      line-height: 18px;
    }
    .filter {
      :deep(.filter-select) {
        width: 93px;
        height: 36px;
        background-color: #f5f6f8;
        .ant-select-arrow {
          color: #333;
        }
        .ant-select-selector {
          .ant-select-selection-item {
            line-height: 36px;
          }
        }
        &.ant-select-open {
          .ant-select-arrow {
            color: rgba(0, 0, 0, 0.25);
          }
        }
      }
      .search {
        width: 350px;
        height: 36px;
        margin-left: 18px;
        :deep(.ant-input) {
          width: 100%;
          height: 36px;
          background-color: #fff;
          border-radius: 4px;
          border: none;
          color: #333;
          font-size: 16px;
          padding-left: 9px;
          border: 1px solid #ebeef5;
          &::placeholder {
            color: #9599a5;
          }
        }

        .ico_search {
          position: absolute;
          top: 50%;
          transform: translateY(-50%);
          right: 16px;
          z-index: 2;
          width: 16px;
          height: 16px;
          background-image: url("../../../assets/images/ico_search.png");
          cursor: pointer;
        }
      }
    }
  }
  .prompt {
    width: 100%;
    .tabbar {
      width: 100%;
      padding: 12px 28px 0 28px;

      .tabbar_w {
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        flex: auto;
        overflow-x: auto;
        border-bottom: 1px solid #ebeef5;
        padding-bottom: 12px;
        .item {
          font-size: 18px;
          font-weight: 400;
          color: #000000;
          line-height: 18px;
          margin-right: 30px;
          position: relative;
          flex-shrink: 0;
          cursor: pointer;
          &.active {
            color: #5657f6;
            &::after {
              content: "";
              width: 16px;
              height: 2px;
              background-color: #5657f6;
              position: absolute;
              bottom: -12px;
              left: 50%;
              transform: translateX(-50%);
            }
          }
          &:hover {
            color: rgba(86, 87, 246, 0.8);
          }
        }
      }
    }
    .list {
      width: 100%;
      padding: 18px 0px 18px 28px;
      min-height: 197px;
      max-height: 447px;
      overflow-y: scroll;
      &::-webkit-scrollbar {
        display: block;
        width: 6px;
        height: 4px;
      }
      &::-webkit-scrollbar-thumb {
        display: block;
        background: #ebeef5;
        border-radius: 10px;
        box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
      }
      &::-webkit-scrollbar-track {
        display: block;
        background: #fff;
        border-radius: 10;
        box-shadow: inset 0 0 5px rgba(205, 203, 203, 0.2);
      }
      .item {
        display: flex;
        align-items: flex-start;
        justify-content: flex-start;
        width: 350px;
        height: 125px;
        background-color: #f5f6f8;
        border: 1px solid #ebeef5;
        border-radius: 6px;
        margin-right: 47px;
        margin-bottom: 18px;
        padding: 24px 24px 0;
        cursor: pointer;
        &:nth-of-type(3n) {
          margin-right: 0;
        }
        .icon {
          width: 32px;
          height: 32px;
          background-color: #ffffff;
          border-radius: 6px;
          > img {
            display: block;
            width: 18px;
            height: 18px;
          }
          .font_family {
            font-size: 18px;
          }
        }
        .info {
          width: calc(100% - 44px);
          margin-left: 12px;
          padding-top: 8px;
          .title {
            font-size: 16px;
            font-weight: bold;
            color: #333333;
            line-height: 16px;
            > p {
              font-weight: bold;
              max-width: 172px;
            }
            .tag {
              width: 36px;
              height: 19px;
              border-radius: 4px;
              margin-left: 6px;
              border: 1px solid;
              font-size: 12px;
            }
            .new {
              border-color: #ff1523;
              color: #ff1523;
            }
            .recommend {
              border-color: #5657f6;
              color: #5657f6;
            }
          }
          .desc {
            width: 100%;
            height: 42px;
            font-size: 14px;
            font-weight: 400;
            color: #333333;
            line-height: 21px;
            margin-top: 12px;
          }
        }
        &:hover {
          background-color: #ecedef;
          .info {
            .title {
              color: #5657f6;
            }
            .desc {
              color: #5657f6;
            }
          }
        }
      }
    }
  }
}
</style>

<template>
  <a-modal
    :visible="visible"
    :width="522"
    title="搜索策略设置"
    :closable="true"
    wrapClassName="docSettingDialog"
    :footer="null"
    @cancel="closeModal"
  >
    <div class="item">
      <div class="title">
        <div class="question left">
          搜索策略
          <a-popover title="搜索策略">
            <template #content>
              <p class="popover_item">
                从知识库中获取知识的检索方式，不同的检索策略可以更有效地找到正确的信息，提高其生成的答案的准确性和可用性。
              </p>
            </template>
            <i class="ico_question bg"></i>
          </a-popover>
        </div>
      </div>
      <div class="content">
        <a-select
          ref="select"
          v-model:value="form.searchType"
          style="width: 100%"
          placeholder="请选择"
        >
          <a-select-option
            class="select-option-item"
            v-for="(item, index) in searchTypeList"
            :key="index"
            :value="item.value"
            >{{ item.label }}</a-select-option
          >
        </a-select>
      </div>
    </div>
    <div class="item">
      <div class="title">返回范围&nbsp;(当前值：{{ form.topK || 0 }})</div>
      <a-slider :max="10" :min="1" :step="1" v-model:value="form.topK" />
    </div>
    <div class="item" v-if="form.searchType != 'full_text_search'">
      <div class="title">最小相似度&nbsp;(当前值：{{ form.score || 0 }})</div>
      <a-slider :max="0.99" :min="0.01" :step="0.01" v-model:value="form.score" />
    </div>
  </a-modal>
</template>
<script setup>
import { ref, reactive, defineProps, defineEmits, watch, inject } from "vue";
const axios = inject("axios");
const emit = defineEmits(["update:visible"]);
const props = defineProps({
  visible: {
    type: Boolean,
    default: () => false,
  },
});
const searchTypeList = [
  {
    label: "语义检索",
    value: "vector_search",
  },
  {
    label: "全文检索",
    value: "full_text_search",
  },
  {
    label: "混合检索",
    value: "hybrid_search",
  },
];

const form = inject("form");

const closeModal = () => {
  emit("update:visible", false);
};
</script>

<style lang="less" scoped>
.item {
  width: 100%;
  margin-bottom: 15px;
  .title {
    font-size: 14px;
    line-height: 24px;
    font-weight: 400;
    color: #333333;
  }
  .question {
    position: relative;
    .ico_question {
      display: block;
      width: 18px;
      height: 18px;
      background-image: url(../images/ico_question1.png);
      cursor: pointer;
    }
  }
  .content {
    margin-top: 10px;
  }
  :deep(.ant-select) {
    .ant-select-selector {
      box-shadow: none !important;
      border: none;
      background-color: #fafafa;
      height: 40px;
      line-height: 40px;
      .ant-select-selection-item {
        line-height: 40px;
      }
      .ant-select-selection-overflow {
        .ant-select-selection-item {
          line-height: 22px;
        }
      }
      .ant-select-selection-placeholder {
        color: #9599a5;
        line-height: 40px;
      }
    }
  }

  :deep(.ant-slider) {
    .ant-slider-rail {
      background-color: #e8e8e9;
    }
    .ant-slider-track {
      background-color: #5657f6;
    }
  }
}
</style>

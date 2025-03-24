<template>
  <a-modal
    :visible="visible"
    :width="522"
    title="个性化配置"
    :closable="true"
    wrapClassName="modalDialog"
    :footer="null"
    @cancel="closeModal"
  >
    <div class="item">
      <div class="title">
        <div class="question left">
          语言模型
          <a-popover title="语言模型">
            <template #content>
              <p class="popover_item">
                集成了MiniMax、文心一言、清华智谱、月之暗面等大语言模型。
              </p>
            </template>
            <i class="ico_question bg"></i>
          </a-popover>
        </div>
      </div>
      <div class="content">
        <a-select
          ref="select"
          v-model:value="form.model"
          :options="getModelList"
          style="width: 100%"
          placeholder="请选择"
        >
        </a-select>
        <!-- <a-select
          ref="select"
          v-model:value="form.model"
          style="width: 100%"
          placeholder="请选择"
        >
          <a-select-option
            class="select-option-item"
            v-for="(item, index) in getModelList"
            :key="index"
            :value="item.modelLabel"
            >{{ item.modelName }}</a-select-option
          >
        </a-select> -->
      </div>
    </div>

    <div class="item">
      <a-checkbox v-model:checked="form.isContext">是否给LLM提交对话上下文</a-checkbox>
    </div>
    <div class="item">
      <div class="title">返回字数&nbsp;(当前值：{{ form.maxTokens || 0 }})</div>
      <a-slider :max="8000" :min="100" :step="100" v-model:value="form.maxTokens" />
    </div>
    <div class="item">
      <div class="title">随机程度&nbsp;(当前值：{{ form.temperature || 0 }})</div>
      <a-slider :max="2" :min="0" :step="0.1" v-model:value="form.temperature" />
    </div>
    <div class="item" v-if="form.isContext">
      <div class="title">对话轮数&nbsp;(当前值：{{ form.historyCount || 1 }})</div>
      <a-slider :max="10" :min="1" :step="1" v-model:value="form.historyCount" />
    </div>
  </a-modal>
</template>
<script setup>
import { ref, reactive, defineProps, defineEmits, watch, inject } from "vue";
import { PromptStore } from "@/stores/prompt";
import { storeToRefs } from "pinia";
const promptStore = PromptStore();
const { getModelList } = storeToRefs(promptStore);
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

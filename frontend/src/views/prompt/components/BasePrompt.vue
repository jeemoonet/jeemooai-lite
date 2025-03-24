<template>
  <div class="normal_prompt">
    <div class="init_prompt_header right">
      <div class="auto right" @click="modelVisivle = true">
        <img class="ico_auto" :src="modelData.icon" />
        {{ modelData.modelName || "请选择模型" }}
      </div>
    </div>
    <a-collapse v-model:activeKey="collapseActiveKey" class="collapse">
      <a-collapse-panel key="1" :showArrow="false">
        <template #header>
          <div class="custom_header float">
            <div class="title left">
              <right-outlined class="ico_collapse_right" />
              <div class="question">知识库</div>
            </div>
            <div class="controller right">
              <div class="setting" @click.stop="docSettingVisible = true">
                搜索策略设置
              </div>
            </div>
          </div>
        </template>
        <div class="item">
          <a-tree-select
            class=""
            v-model:value="form.docCategoryIds"
            style="width: 100%"
            :treeDefaultExpandedKeys="[1, 2]"
            :tree-data="docCategoryList"
            :multiple="true"
            allow-clear
            :show-checked-strategy="SHOW_PARENT"
            placeholder="请选择"
            :fieldNames="{
              label: 'categoryName',
              value: 'categoryId',
            }"
          />
        </div>
      </a-collapse-panel>

      <a-collapse-panel key="4" :showArrow="false">
        <template #header>
          <div class="custom_header float">
            <div class="title left">
              <right-outlined class="ico_collapse_right" />
              <div class="question">对话设置</div>
            </div>
          </div>
        </template>
        <!-- 欢迎语 -->
        <div class="item">
          <div class="title">
            <div class="question float">用户欢迎语</div>
          </div>
          <div class="content">
            <a-textarea
              class="input"
              :autoSize="{ minRows: 3, maxRows: 3 }"
              row="3"
              :maxlength="500"
              v-model:value="form.initMessage"
              placeholder="请输入用户欢迎语"
            />
          </div>
        </div>
        <!-- 自动推荐问题 -->
        <!-- 默认常见问题 -->
        <div class="item">
          <div class="title float">
            <div class="question left">
              默认常见问题
              <a-popover title="默认常见问题">
                <template #content>
                  <p class="popover_item">
                    添加后可以内置多个对话例句，在对话过程中可以供用户参考。
                  </p>
                </template>
                <i class="ico_question bg"></i>
              </a-popover>
            </div>
            <i class="ico_add bg" @click="addTip"></i>
          </div>
          <div class="textarea_content" v-for="(item, index) in form.tips" :key="index">
            <div class="title">
              提示语:<a-input
                v-model:value="item.title"
                :maxlength="10"
                placeholder="请输入"
              />
              <i class="ico_del bg" @click="delTip(index)"></i>
            </div>
            <a-textarea
              class="input"
              :autoSize="{ minRows: 3, maxRows: 3 }"
              row="3"
              :maxlength="500"
              v-model:value="item.message"
              placeholder="请输入"
            />
          </div>
          <a-empty
            :image="simpleImage"
            description="暂无数据,请点击上方[+]进行增加"
            v-if="form.tips == 0"
          />
        </div>
      </a-collapse-panel>

      <a-collapse-panel key="6" :showArrow="false">
        <template #header>
          <div class="custom_header float">
            <div class="title left">
              <right-outlined class="ico_collapse_right" />
              <div class="question">可见配置</div>
            </div>
          </div>
        </template>
        <!-- 助手分类 -->
        <div class="item">
          <div class="title">
            <div class="question left">助手分类</div>
          </div>
          <div class="content">
            <a-select
              v-model:value="form.categoryId"
              placeholder="请选择助手分类"
              style="width: 100%"
            >
              <a-select-option
                class="select-option-item"
                v-for="(item, index) in categoryList"
                :key="index"
                :value="item.categoryId"
                >{{ item.categoryName }}</a-select-option
              >
            </a-select>
          </div>
        </div>
        <!-- 可见设置 -->
        <div class="item">
          <div class="title">
            <div class="question left">
              可见设置<a-popover title="可见设置">
                <template #content>
                  <p class="popover_item">内部公开选中后，可以被所有人访问；</p>
                </template>
                <i class="ico_question bg"></i>
              </a-popover>
            </div>
          </div>
          <div class="content float">
            <a-checkbox v-model:checked="form.isPublic">是否内部公开</a-checkbox>
            <a-checkbox v-model:checked="form.isRecommend">是否推荐</a-checkbox>
            <a-checkbox v-model:checked="form.isNew">是否最新</a-checkbox>
          </div>
        </div>
      </a-collapse-panel>
    </a-collapse>
  </div>
  <model-modal v-model:visible="modelVisivle" />
  <doc-setting-modal v-model:visible="docSettingVisible" />
</template>
<script setup>
import { RightOutlined } from "@ant-design/icons-vue";
import { message, Empty } from "ant-design-vue";
import { ref, onMounted, inject, nextTick, provide, reactive, computed } from "vue";

import ModelModal from "./prompt/modelModal.vue";
import DocSettingModal from "./prompt/docSettingModal.vue";
import { PromptStore } from "@/stores/prompt";
import { storeToRefs } from "pinia";
const simpleImage = Empty.PRESENTED_IMAGE_SIMPLE;
const promptStore = PromptStore();
const { getModelList } = storeToRefs(promptStore);
const axios = inject("axios");
const form = inject("form");
const recommendQuestionPrompt = inject("recommendQuestionPrompt");
const collapseActiveKey = ref([]);
//模型
const modelVisivle = ref(false);
// 知识库
const docCategoryList = inject("docCategoryList");
const docSettingVisible = ref(false);
//插件
const knowHowList = inject("knowHowList");
const toolVisible = ref(false);
//语音原始数据
const vocieList = inject("vocieList");
const voiceVisible = ref(false);
//数字人模型
const digiPersonList = inject("digiPersonList");
const digiPersonVisible = ref(false);
//助手分类
const categoryList = inject("categoryList");
onMounted(() => {});

//删除常用语
const delTip = (index) => {
  form.value.tips.splice(index, 1);
};
//新增常用语
const addTip = () => {
  form.value.tips.push({ title: "", message: "" });
};
//删除插件
const onToolDel = (index, id) => {
  form.value.functions.splice(index, 1);
  knowHowList.value = knowHowList.value.map((item) => {
    if (id == item.functionId) {
      item.check = false;
    }
    return item;
  });
};

const digiPersonData = computed(() => {
  let _data =
    digiPersonList.value.find((item) => item.digit_person == form.value.digitPerson) ||
    {};
  return _data || {};
});
const voiceData = computed(() => {
  let _voice = vocieList.value.find((item) => item.voice == form.value.voice) || {};
  return _voice || {};
});
const knowHowData = computed(() => {
  let functionIds = form.value.functions.map((functionObj) => functionObj.functionId);

  let matchedKnowHowList = knowHowList.value.filter((item) =>
    functionIds.includes(item.functionId)
  );

  return matchedKnowHowList || [];
});
//获取选择的大模型
const modelData = computed(() => {
  const allModels = getModelList.value.flatMap((platform) => platform.options);
  const selectedModel = allModels.find((model) => model?.modelLabel === form.value.model);
  return selectedModel ? selectedModel : {};
});
</script>
<style scoped lang="less">
.normal_prompt {
  display: flex;
  flex-direction: column;
  // width: calc(33.33% - 10px);
  flex: 1;
  min-width: 0;
  height: 100%;
  position: relative;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.04);
  .init_prompt_header {
    width: 100%;
    height: 46px;
    padding: 0 8px 0 20px;
    border-bottom: 1px solid #fafafa;

    .title {
      font-size: 16px;
      font-weight: 600;
      line-height: normal;
      color: #333333;
      margin: 0;
    }
    .auto {
      font-size: 14px;
      font-weight: normal;
      line-height: normal;
      color: #1d2129;
      cursor: pointer;
      padding: 5px 12px;
      border-radius: 6px;
      transition: all 0.2s ease-in-out;
      .ico_auto {
        width: 16px;
        height: 16px;
        margin-right: 5px;
      }
      &:hover {
        background: #fafafa;

        box-shadow: inset 0px 0px 20px 0px rgba(0, 0, 0, 0.01);
      }
    }
  }
}
.tool-item {
  width: 100%;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 6px;
  background: #fafafa;
  border: 1px solid #fafafa;
  box-shadow: inset 0px 0px 20px 0px rgba(0, 0, 0, 0.01);
  cursor: pointer;
  img {
    display: block;
    width: 36px;
    height: 36px;
    margin-right: 8px;
    border-radius: 8px;
    cursor: pointer;
  }
  .ico_switch {
    width: 16px;
    height: 16px;
    background-image: url("./images/ico_switch.png");
    cursor: pointer;
  }
  .icon_sex {
    width: 36px;
    height: 36px;
    margin-right: 8px;
    border-radius: 8px;
    &.boy {
      background-image: url("./images/boy_init.png");
    }
    &.giry {
      background-image: url("./images/girl_init.png");
    }
  }
  .info {
    flex: 1;
    min-width: 0;
    height: 48px;
    .title {
      font-size: 14px;
      font-weight: 500;
      line-height: 24px;
      color: #333333;
      margin: 0;
    }
    .detail {
      font-size: 12px;
      font-weight: normal;
      line-height: 24px;
      color: #1d2129;
    }
  }
  .ico_del {
    display: none;
    width: 18px;
    height: 18px;
    background-image: url("./images/ico_del.png");
    cursor: pointer;
  }
  &:hover {
    border: 1px solid #5657f6;
    box-shadow: inset 0px 0px 20px 0px rgba(0, 0, 0, 0.01);
    .ico_del {
      display: block;
    }
  }
}
.item {
  width: 100%;
  margin-bottom: 15px;
  .title {
    font-size: 14px;
    line-height: 24px;
    font-weight: 400;
    color: #333333;
    margin-bottom: 10px;
    .ico_add {
      width: 16px;
      height: 16px;
      background-image: url(./images/add.png);
      cursor: pointer;
      transition: all 0.2s ease-in-out;
      &:hover {
        transform: scale(1.2);
      }
    }
    &.no_mb {
      margin-bottom: 0;
    }
  }
  .question {
    position: relative;
    .ico_question {
      display: block;
      width: 18px;
      height: 18px;
      background-image: url(./images/ico_question1.png);
      cursor: pointer;
    }
  }
  .textarea_content {
    width: 100%;
    margin-bottom: 12px;
    &:nth-last-of-type(1) {
      margin-bottom: 0;
    }
    .title {
      display: flex;
      align-items: center;
      justify-content: flex-start;
      width: 100%;
      font-size: 14px;
      font-weight: 400;
      color: #333333;
      line-height: 36px;
      border-bottom: 0.5px solid #e8e8e9;
      padding-left: 11px;
      margin-bottom: 0;
      background-color: #fafafa;
      position: relative;
      .ant-input {
        width: 400px;
        background-color: transparent;
        margin: 0;
        box-shadow: none;
      }
      .ico_del {
        display: block;
        width: 18px;
        height: 18px;
        background-image: url("./images/ico_del.png");

        position: absolute;
        top: 50%;
        right: 20px;
        transform: translateY(-50%);
        cursor: pointer;
      }
    }
  }
  :deep(.ant-select-selector) {
    box-shadow: none !important;
    border: none !important;
    background-color: #fafafa !important;
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
  &.scroll_input {
    .ant-input {
      min-height: 150px;
    }
  }
}
.ant-input {
  flex: 1;
  min-height: 0;
  display: block;
  width: 100%;
  margin-top: 0;
  background-color: #fafafa !important;
  border: none;
  border-radius: 0;
  color: #333333;
  font-size: 14px;
  box-shadow: none;
  padding: 10px 20px;
  &::placeholder {
    color: #9599a5;
  }

  &:focus {
    box-shadow: none;
  }
}

.collapse {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  background-color: #fff;
  border: none;
  padding: 0px 20px 40px;
  .custom_header {
    width: 100%;
    .title {
      font-size: 14px;
      line-height: 24px;
      font-weight: 500;
      color: #333333;
    }
    .controller {
      .setting {
        font-size: 12px;
        font-weight: normal;
        line-height: 24px;
        text-decoration: underline;
        color: #333333;
        // margin-right: 14px;
        cursor: pointer;
        &:hover {
          color: #5657f6;
        }
      }
      .add {
        width: 16px;
        height: 16px;
        background-image: url(./images/add.png);
        cursor: pointer;
        transition: all 0.2s ease-in-out;
        &:hover {
          transform: scale(1.2);
        }
      }
    }
  }

  :deep(.ant-collapse-item) {
    border: none;
    // margin-bottom: 15px;
    .ant-collapse-header {
      background-color: #fff;
      border: none;
      font-size: 16px;
      font-weight: 350;
      color: #f5f6f8;
      padding-left: 0;
      padding: 12px 0 10px !important;
      border-radius: 8px;
    }
  }
  :deep(.ant-collapse-content) {
    background-color: transparent;
    border: none;
    padding: 0;
    .ant-collapse-content-box {
      padding: 0;
    }
  }
  .ico_collapse_right {
    font-size: 12px;
    color: #9599a5;
    transition: transform 0.24s;
    margin-right: 8px;
  }
  :deep(.ant-collapse-item-active) {
    .ico_collapse_right {
      transform: rotate(90deg);
    }
    .ant-collapse-header {
      background-color: #fff;
      border: none;
      font-size: 16px;
      font-weight: 350;
      color: #f5f6f8;
      padding: 12px 0 10px !important;
      border-radius: 8px;
    }
  }
}
</style>

<template>
  <prompt-header :prompt="form" />
  <div class="prompt_body">
    <init-prompt v-if="form.promptType == 0" />
    <base-prompt v-if="form.promptType == 0" />
    <!-- <workflow-prompt v-if="form.promptType == 1" /> -->
    <preview-prompt />
  </div>
  <!-- <debug-prompt v-model:visible="debugVisible" /> -->
</template>
<script setup>
import { message } from "ant-design-vue";
import { ref, onMounted, inject, nextTick, provide, reactive } from "vue";
import PromptHeader from "./components/prompt/header.vue";
import InitPrompt from "./components/InitPrompt.vue";
import BasePrompt from "./components/BasePrompt.vue";
import PreviewPrompt from "./components/PreviewPrompt.vue";
import { useRoute } from "vue-router";
const axios = inject("axios");
const route = useRoute();
const recommendQuestionPrompt =
  "任务：根据已提供回答和关键词，生成3个引导用户进行下一步提问的问句。避免与上一个问题意思重复。要求：- 每个问句都应该是一个完整的问题，并且尽量模拟真实用户的表达方式。-避免使用过于正式或技术性的语言，除非关键词本身具有这样的性质。- 问句应该具有一定的多样性，避免结构和用词过于相似。";
const form = ref({});
const windowId = ref("");
const debugVisible = ref(false);
// 知识库
const docCategoryList = ref([]);
//插件 functioncall
const knowHowList = ref([]);
//语音原始数据
const vocieList = ref([]);
//数字人模型
const digiPersonList = ref([]);
//角色分类
const categoryList = ref([]);
provide("form", form);
provide("windowId", windowId);
provide("debugVisible", debugVisible);
provide("recommendQuestionPrompt", recommendQuestionPrompt);
provide("docCategoryList", docCategoryList);
provide("knowHowList", knowHowList);
provide("vocieList", vocieList);
provide("categoryList", categoryList);
provide("digiPersonList", digiPersonList);
onMounted(() => {
  getPromptData();
});

const onUpdate = async () => {
  let hide = null;
  let _form = form.value;
  hide = message.loading("请求中..", 0);
  try {
    let res = await axios.post("/api/prompt/update", {
      ..._form,
    });
    message.success("保存成功");
  } catch (err) {
  } finally {
    hide();
  }
};

const getPromptData = async () => {
  let params = route.params || {};
  let hide = null;
  hide = message.loading("请求中..", 0);
  try {
    let res = await axios.get(`/api/prompt`, {
      params: {
        promptId: params.id,
      },
    });
    let data = res.data.data || {};
    document.title = data.promptName;
    data.isPublic = data.isPublic == 1 ? true : false;
    data.isRecommend = data.isRecommend == 1 ? true : false;
    data.isNew = data.isNew == 1 ? true : false;
    data.isContext = data.isContext == 1 ? true : false;
    data.isRecommendQuestions = data.isRecommendQuestions == 1 ? true : false;
    data.longHistory = data.longHistory == 1 ? true : false;
    data.recommendQuestionPrompt =
      data.recommendQuestionPrompt || recommendQuestionPrompt;
    form.value = data;
    nextTick(() => {
      promptWindowId();
      getDocCategoryList();
      getCategoryList();
    });
  } catch (err) {
  } finally {
    hide();
  }
};

//根据提示器id获取windowid
const promptWindowId = async () => {
  try {
    let res = await axios.get("/api/window/debuggerWindowId", {
      params: {
        promptId: form.value.promptId,
      },
    });
    let data = res.data.data || {};
    windowId.value = data.windowId;
  } catch (err) {
  } finally {
  }
};

//获取知识库
const getDocCategoryList = async (res) => {
  try {
    let res = await axios.get("/api/documentCategory/categoryList");
    let data = res.data.data || [];
    docCategoryList.value = data || [];
  } catch (err) {}
};

//获取角色分类
const getCategoryList = async (res) => {
  try {
    let res = await axios.get("/api/home/categoryList");
    let data = res.data.data || [];
    categoryList.value = data || [];
    if (!form.categoryId && data.length != 0) {
      form.value.categoryId = data[0]?.categoryId || null;
    }
  } catch (err) {}
};
</script>
<style scoped lang="less">
.prompt_body {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 20px;
  width: 100%;
  height: 100%;
  padding: 76px 20px 20px;
}
</style>

<template>
  <header class="workflowHeader float">
    <div class="left">
      <div class="info left" @click="onEdit()">
        <img :src="form.promptIcon" class="logo" />
        <div class="title left">
          <h4 class="one_line">{{ form.promptName || "-" }}</h4>
          <i class="edit bg"></i>
        </div>
      </div>
      <div class="promptType-select float">
        <img src="../images/prompt-type-1.png" class="ico_type" />
        <span class="title">提示词助手</span>
        <DownOutlined />
      </div>
    </div>

    <div class="controller right">
      <a-button class="upload iconcenter" @click="testSubmit"
        ><i class="ico_test bg"></i>多模型测试</a-button
      >
      <a-button :loading="saveLoading" class="save iconcenter" @click="onSave"
        ><i class="ico_save bg"></i>保存</a-button
      >
    </div>
  </header>
  <add-modal v-model:visible="editVisible" :promptData="promptData" @update="onUpdate" />
</template>
<script setup>
import { DownOutlined } from "@ant-design/icons-vue";
import { message } from "ant-design-vue";
import { inject, ref, reactive, onMounted, watch, computed, nextTick, toRaw } from "vue";
import addModal from "../addPromptModal.vue";
const axios = inject("axios");
const form = inject("form");

const editVisible = ref(false);
const promptData = ref({});
const saveLoading = ref(false);

//测试提示器
const testSubmit = () => {
  let _form = toRaw(form.value);
  const params = {
    id: _form.promptId,
    name: _form.promptName,
    model: _form.model,
    initMessage: _form.initMessage,
    tips: JSON.stringify(_form.tips || []),
  };
  const queryString = new URLSearchParams(params).toString();
  window.open(`/prompt/test?${queryString}`, "_blank");
};

const onPromptTypeChange = (e) => {
  form.value.promptType = e;
};

const onSave = async () => {
  saveLoading.value = true;
  try {
    await axios.post("/api/prompt/update", {
      ...form.value,
      isPublic: form.value.isPublic ? 1 : 0,
      isRecommend: form.value.isRecommend ? 1 : 0,
      isNew: form.value.isNew ? 1 : 0,
      isContext: form.value.isContext ? 1 : 0,
      isRecommendQuestions: form.value.isRecommendQuestions ? 1 : 0,
      longHistory: form.value.longHistory ? 1 : 0,
    });
    message.success("保存成功");
  } catch (err) {
  } finally {
    saveLoading.value = false;
  }
};

const onUpdate = (res = {}) => {
  Object.assign(form.value, res);
};

const onEdit = () => {
  promptData.value = {
    promptIcon: form.value.promptIcon,
    promptName: form.value.promptName,
    promptDesc: form.value.promptDesc,
    promptType: form.value.promptType,
    promptId: form.value.promptId,
    returnUpdateData: true,
  };
  editVisible.value = true;
};
</script>
<style scope lang="less">
.workflowHeader {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 56px;
  background: #ffffff;
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.02);
  padding: 0 40px;
  z-index: 2;
  .info {
    max-width: 50%;
    font-size: 12px;
    font-weight: normal;
    line-height: normal;
    color: #333333;
    cursor: pointer;
    .logo {
      display: block;
      width: 22px;
      height: 22px;
      margin-right: 8px;
    }
    .title {
      flex: 1;
      min-width: 0;
      font-size: 18px;
      font-weight: 500;
      line-height: 24px;
      color: #333333;
      h4 {
        margin: 0;
        flex: 1;
        min-width: 0;
      }
      .edit {
        width: 16px;
        height: 16px;
        margin-left: 10px;
        background-image: url(../images/edit.png);
        cursor: pointer;
      }
      &:hover {
        .edit {
          background-image: url(../images/edit_hover.png);
        }
      }
    }
  }
  .promptType-select {
    width: 130px;
    height: 32px;
    border-radius: 6px;
    margin-left: 30px;
    background: #fafafa;
    box-shadow: inset 0px 0px 20px 0px rgba(0, 0, 0, 0.01);
    color: rgba(0, 0, 0, 0.25);
    padding: 0 14px 0 4px;
    font-size: 10px;
    line-height: 10px;
    cursor: pointer;
    .title {
      font-size: 14px;
      font-weight: normal;
      line-height: 24px;
      color: #333333;
    }
    .ico_type {
      display: block;
      width: 22px;
      height: 22px;
    }
  }
  .controller {
    .upload {
      width: fit-content;
      height: 32px;
      border-radius: 8px;
      background: #5657f6;
      border: 1px solid #5657f6;
      font-size: 14px;
      font-weight: 500;
      line-height: 32px;
      color: #fff;
      border: none;
      transition: all 0.15s ease-in-out;
      margin-right: 10px;
      padding: 0 16px;

      .ico_test {
        width: 18px;
        height: 18px;
        margin-right: 2px;
        background-image: url(../images/ico_test.png);
      }
      &:hover {
        background: #3838c0;
      }
    }
    .save {
      width: 80px;
      height: 32px;
      border-radius: 8px;
      background: rgba(86, 87, 246, 0.1);
      border: 1px solid #5657f6;
      font-size: 14px;
      font-weight: 500;
      line-height: 32px;
      color: #5657f6;
      transition: all 0.15s ease-in-out;

      .ico_save {
        width: 16px;
        height: 16px;
        background-image: url(../images/save.png);
        margin-right: 2px;
      }
      &:hover {
        background: #5657f6;
        border: 1px solid #5657f6;
        color: #ffffff;

        .ico_save {
          background-image: url(../images/save-hover.png);
        }
      }
    }
    .submit {
      width: 80px;
      height: 32px;
      border-radius: 8px;
      background: #5657f6;
      border: 1px solid #5657f6;
      font-size: 14px;
      font-weight: 500;
      line-height: 32px;
      color: #fff;
      border: none;
      transition: all 0.15s ease-in-out;
      .ico_submit {
        width: 16px;
        height: 16px;
        background-image: url(../../images/submit.png);
        margin-right: 2px;
      }
      &:hover {
        background: #3838c0;
      }
    }
  }
}
</style>
<style lang="less">
.prompt-promptType-select-dropdown {
  background-color: transparent;
  box-shadow: 0px 4px 10px 0px rgba(218, 218, 247, 0.6);
}
.prompt-promptType-select-list {
  width: 265px;
  padding: 10px;
  border-radius: 8px;
  background: #ffffff;
  box-shadow: 0px 4px 10px 0px rgba(218, 218, 247, 0.6);
  .item {
    width: 100%;
    padding: 10px 10px 12px;
    border-radius: 8px;
    background: #fafafa;
    box-shadow: inset 0px 0px 20px 0px rgba(0, 0, 0, 0.01);
    border: 1px solid #fafafa;
    margin-bottom: 10px;
    cursor: pointer;
    &:nth-last-of-type(1) {
      margin-bottom: 0;
    }
    .logo {
      display: block;
      width: 48px;
      height: 48px;
      margin-right: 8px;
    }
    .info {
      flex: 1;
      min-width: 0;
      padding-top: 2px;
      .title {
        font-size: 14px;
        font-weight: 500;
        line-height: 20px;
        color: #333333;
        margin-bottom: 7px;
      }
      .desc {
        font-size: 12px;
        font-weight: normal;
        line-height: normal;
        color: #1d2129;
      }
    }
    &:hover {
      border: 1px solid #5657f6;
      box-shadow: inset 0px 0px 20px 0px rgba(0, 0, 0, 0.01);
    }
  }
}
</style>

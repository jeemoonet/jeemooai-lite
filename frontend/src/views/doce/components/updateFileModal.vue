<template>
  <a-modal
    :visible="visible"
    :width="tabbarStatus == 3 ? 690 : 522"
    title="新增资料"
    :closable="true"
    :footer="null"
    wrapClassName="doceUpdateDialog"
    @cancel="closeModal"
  >
    <a-spin tip="上传中，请勿关闭页面" :spinning="uploadFileLoading">
      <div class="modal_content">
        <div class="tabbar float">
          <div
            class="item"
            :class="tabbarStatus == 0 ? 'active' : ''"
            @click="tabbarStatus = 0"
          >
            网站
          </div>
          <div
            class="item"
            :class="tabbarStatus == 1 ? 'active' : ''"
            @click="tabbarStatus = 1"
          >
            文件
          </div>
          <div
            class="item"
            :class="tabbarStatus == 2 ? 'active' : ''"
            @click="tabbarStatus = 2"
          >
            网页
          </div>
          <div
            class="item"
            :class="tabbarStatus == 3 ? 'active' : ''"
            @click="tabbarStatus = 3"
          >
            文本
          </div>
          <!-- <div class="item" :class="tabbarStatus == 3 ? 'active' : ''" @click="alertError">
          小红书
        </div> -->
        </div>
        <div class="content">
          <!-- 网站地址 -->
          <div class="url_content" v-show="tabbarStatus == 0">
            <div class="title float">
              网站地址
              <button class="submit iconcenter" @click="onUrlSubmit">导入</button>
            </div>
            <a-textarea
              class="input"
              v-model:value="urlInput"
              placeholder="输入单个网站地址，系统自动循环抓取该网站下所有网页，最多不超过100页"
            />
          </div>
          <!-- 微信地址 -->
          <div class="url_content" v-show="tabbarStatus == 2">
            <div class="title float">
              网页地址
              <button class="submit iconcenter" @click="onWeChatSubmit">导入</button>
            </div>
            <a-textarea
              class="input"
              v-model:value="wechatInput"
              placeholder="支持多个网页地址，每个页面地址换行分隔，系统依次抓取指定页面地址内容。"
            />
          </div>
          <div class="file_content" v-show="tabbarStatus == 1">
            <a-upload-dragger
              :disabled="uploadFileLoading"
              name="file"
              :capture="false"
              accept=".doc,.docx,.pdf,.txt,.ppt,.pptx"
              class="file-uploader"
              :multiple="true"
              :show-upload-list="false"
              :action="uploadFileUrl"
              :headers="uploadFileHeaders"
              :before-upload="beforeUpload"
              :data="{ categoryId: doceParams.folderId }"
              @change="handleChange"
            >
              <div class="upload_hint">拖放文件至此</div>
            </a-upload-dragger>
            <div class="upload_desc">仅支持txt、docx、pdf文件类型，单个文件100MB以内</div>
            <div class="file_list">
              <template v-if="fileList.length != 0">
                <div class="item left" v-for="(item, index) in fileList" :key="index">
                  <i class="ico_file bg"></i>
                  <div class="title one_line">{{ item }}</div>
                </div>
              </template>
              <div class="no-date" v-else>暂无数据</div>
            </div>
          </div>
          <div class="md_content" v-show="tabbarStatus == 3">
            <vditor :doceMenuId="doceParams.folderId"></vditor>
          </div>
        </div>
      </div>
    </a-spin>
  </a-modal>
</template>
<script setup>
import { message } from "ant-design-vue";
import {
  ref,
  defineProps,
  computed,
  defineEmits,
  onMounted,
  inject,
  nextTick,
  onBeforeUnmount,
  provide,
} from "vue";
import vditor from "./vditor.vue";
import setting from "@/utils/setting";
import { validateWeChatURL, validateURL } from "@/utils/utils";
import { DoceStore } from "@/stores/doce";
import { storeToRefs } from "pinia";

const axios = inject("axios");
const emit = defineEmits(["update:visible", "update"]);
const props = defineProps({
  visible: {
    type: Boolean,
    default: () => false,
  },
});
const doceParams = inject("doceParams");
//文件上传地址
const uploadFileUrl = `${import.meta.env.VITE_API_BASE_URL}/api/document/upload`;
//文件上传url
const uploadFileHeaders = {
  [setting.tokenHeaderName]: setting.takeToken(),
};
const uploadFileLoading = ref(false);
provide("uploadFileLoading", uploadFileLoading);
const tabbarStatus = ref(1);
const urlInput = ref("");
const wechatInput = ref("");
const fileList = ref([]);

//文件上传
//选择文件
const handleChange = (info) => {
  console.log("handleChange-:", info);
  // return;
  if (info.file.status === "uploading") {
    uploadFileLoading.value = true;
    return;
  }
  if (info.file.status === "done") {
    uploadFileLoading.value = false;
    let res = info.file.response || {};
    if (res.code != 200) {
      message.error(res.msg || "上传失败，请稍后再试");
      return;
    }
    fileList.value.unshift(info.file.name);
  }
  if (info.file.status === "error") {
    uploadFileLoading.value = false;
    message.error("上传失败，请稍后再试");
  }
};

const beforeUpload = (file) => {
  const isLt2M = file.size / 1024 / 1024 < 100;
  if (!isLt2M) {
    message.error("最大上传100MB");
  }
  return isLt2M;
};

//网站上传
const onUrlSubmit = async () => {
  if (urlInput.value == "" || uploadFileLoading.value) return;
  // let urls = urlInput.value.split("\n") || [];
  let isURL = validateURL(urlInput.value);
  if (!isURL) {
    message.error("请输入正确格式的网站地址");
    return;
  }
  let hide = null;
  hide = message.loading("上传中..", 0);
  uploadFileLoading.value = true;
  try {
    let res = await axios.post(`/api/document/url`, {
      categoryId: doceParams.folderId,
      urls: [urlInput.value],
    });
    urlInput.value = "";

    message.success("上传成功");
  } catch (err) {
  } finally {
    hide();
    uploadFileLoading.value = false;
  }
};

//微信上传
const onWeChatSubmit = async () => {
  if (wechatInput.value == "" || uploadFileLoading.value) return;
  let urls = wechatInput.value.split("\n") || [];
  let isURL = urls.every((item) => validateURL(item));
  if (!isURL) {
    message.error("请输入正确格式的网页地址");
    return;
  }
  let hide = null;
  hide = message.loading("上传中..", 0);
  uploadFileLoading.value = true;
  try {
    let res = await axios.post(`/api/document/wechat`, {
      categoryId: doceParams.folderId,
      urls: urls,
    });
    wechatInput.value = "";
    message.success("上传成功");
  } catch (err) {
  } finally {
    hide();
    uploadFileLoading.value = false;
  }
};

const alertError = () => {
  message.error("暂未开放,敬请期待。");
};

const closeModal = () => {
  fileList.value = [];
  wechatInput.value = "";
  urlInput.value = "";
  emit("update");
  emit("update:visible", false);
};

onMounted(() => {});
onBeforeUnmount(() => {});
</script>

<style lang="less" scoped>
.modal_content {
  width: 100%;
  .tabbar {
    width: 100%;
    padding: 0 54px;
    .item {
      position: relative;
      font-size: 18px;
      font-weight: 400;
      color: #333333;
      line-height: 26px;
      cursor: pointer;
      &.active {
        color: #5657f6;
        &::after {
          content: "";
          width: 18px;
          height: 2px;
          position: absolute;
          background-color: #5657f6;
          bottom: -12px;
          left: 50%;
          transform: translateX(-50%);
        }
      }
    }
  }
  .content {
    width: 100%;
    margin-top: 27px;
    .url_content {
      width: 100%;
      .title {
        width: 100%;
        font-size: 16px;
        font-weight: 400;
        color: #333333;
        line-height: 23px;
        .submit {
          width: 60px;
          height: 32px;
          background-color: #5657f6;
          border-radius: 6px;
          text-align: center;
          line-height: 32px;
          font-size: 16px;
          font-weight: 500;
          color: #ffffff;
          border: none;
          outline: none;
        }
      }
      .input {
        margin-top: 10px;
        height: 142px;
        padding: 10px 18px;
      }
    }
    .file_content {
      :deep(.file-uploader) {
        background-color: transparent;
        border-color: #83838a;

        &:hover {
          border-color: #9599a5;
          .upload_hint {
            color: #9599a5;
          }
        }
        // }
        &.ant-upload-drag-hover {
          border-color: #9599a5;
          .upload_hint {
            color: #9599a5;
          }
          &:not(.ant-upload-disabled) {
            border-color: #9599a5;
          }
        }
        .upload_hint {
          width: 100%;
          text-align: center;
          font-size: 16px;
          color: #333;
          font-weight: 500;
          transition: color linear 0.2s;
        }
      }
      .upload_desc {
        font-size: 14px;
        font-weight: 400;
        color: #9599a5;
        line-height: 20px;
        margin-top: 12px;
        text-align: center;
      }
      .file_list {
        width: 100%;
        max-height: 131px;
        overflow-y: auto;
        margin-top: 25px;
        padding-top: 16px;
        border-top: 1px solid #e8e8e9;
        .item {
          margin-bottom: 18px;
          .ico_file {
            width: 18px;
            height: 18px;
            background-image: url(../../../assets/images/ico_file.png);
            margin-right: 10px;
          }
          .title {
            width: calc(100% - 30px);
            font-size: 16px;
            font-weight: 500;
            color: #3d3d3d;
            line-height: 23px;
          }
        }
        .no-date {
          width: 100%;
          text-align: center;
          padding-top: 25px;
          font-size: 16px;
          color: #999;
          font-weight: 500;
          padding-bottom: 10px;
        }
      }
    }
  }
}
</style>

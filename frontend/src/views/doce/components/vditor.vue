<template>
  <div class="title float">
    文本输入
    <button class="submit iconcenter" @click="onSave">导入</button>
  </div>
  <div class="vditor" id="vditor"></div>
</template>
<script setup>
import Vditor from "vditor";
import { message } from "ant-design-vue";
import { inject, ref, onMounted, watch, computed, nextTick } from "vue";
import "@/assets/css/vditor.css";
import setting from "@/utils/setting";
const axios = inject("axios");

const props = defineProps({
  doceMenuId: {
    type: String,
    default: () => "",
  },
});

const uploadFileLoading = inject("uploadFileLoading");
//文件上传地址
const uploadFileUrl = `${import.meta.env.VITE_API_BASE_URL}/api/home/multipart-upload`;
//文件上传url
const uploadFileHeaders = {
  [setting.tokenHeaderName]: setting.takeToken(),
};
let contentEditor = null;

onMounted(() => {
  contentEditor = new Vditor("vditor", {
    height: 360,
    mode: "wysiwyg",
    toolbar: [],
    toolbarConfig: {
      hide: true,
      pin: false,
    },
    cache: {
      enable: false,
    },
    after: () => {
      contentEditor.setValue("将所需内容填充此处");
    },
    upload: {
      url: uploadFileUrl,
      multiple: true,
      fieldName: "files",
      headers: uploadFileHeaders,
      setHeaders: function () {
        return uploadFileHeaders;
      },
      //   linkToImgUrl: uploadFileUrl,
      //   linkToImgFormat: function (responseText) {
      //     let result = JSON.parse(responseText);
      //     console.log("linkToImgFormat", result);
      //     if (result.status === true) {
      //       return `{"msg": "","code": 0,"data" : {"originalURL": "${result.data.path}","url": "${result.data.url}"}}`;
      //     } else {
      //       return '{"msg": "上传失败","code": -1,"data" : {"originalURL": "","url": ""}}';
      //     }
      //   },
      file: function (files) {
        // console.log("上传前");
        return files;
      },
      format: function (files, res) {
        let result = JSON.parse(res);
        console.log("format", result);
        if (result.code === 200 && result.data && result.data.length != 0) {
          const succMap =
            result.data.reduce((acc, curr) => {
              acc[curr.path] = curr.url;
              return acc;
            }, {}) || {};
          return `{"msg": "","code": 0,"data": {"errFiles": [],"succMap": ${JSON.stringify(
            succMap
          )}}}`;
        } else {
          return '{"msg": "上传失败","code": -1,"data": {"errFiles": [],"succMap": {}}}';
        }
      },
      extraData: {},
    },
  });
});

//markdown上传
const onSave = async () => {
  let _value = contentEditor.getValue();
  if (_value == "" || uploadFileLoading.value) return;
  let hide = null;
  hide = message.loading("上传中..", 0);
  uploadFileLoading.value = true;
  try {
    let res = await axios.post(`/api/document/text`, {
      categoryId: props.doceMenuId,
      text: _value,
    });
    contentEditor.setValue("");
    message.success("上传成功");
  } catch (err) {
  } finally {
    hide();
    uploadFileLoading.value = false;
  }
};
</script>
<style lang="less" scoped>
.title {
  width: 100%;
  font-size: 16px;
  font-weight: 400;
  color: #333333;
  line-height: 23px;
  margin-bottom: 10px;
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
:deep(.vditor-reset) {
  padding: 10px 18px !important;
}
</style>

<template>
  <iframe class="ifarm" :class="visible ? '' : 'full'" :src="previewUrl">
    This browser does not support PDFs. Please download the PDF to view it:
    <a :href="previewUrl">Download PDF</a>
  </iframe>
  <a-drawer
    v-model:visible="visible"
    width="300px"
    class="doce-detail-drawer"
    title="文件详情"
    placement="right"
    :mask="false"
  >
    <div class="doce-detail-content">
      <div class="file-type iconcenter">
        <i class="ico_file bg" :class="previewData.documentType"></i>
      </div>
      <div class="title">{{ previewData.documentName || "-" }}</div>
      <div class="desc left">文件大小：{{ fileSize(previewData.fileSize) }}</div>
      <div class="desc left">
        状态：<span
          class="left"
          :class="
            previewData.status == 3
              ? 'status_red'
              : previewData.status == 1 || previewData.status == 0
              ? 'status_blue'
              : ''
          "
          >{{
            previewData.status == 0
              ? "未处理"
              : previewData.status == 1
              ? "进行中"
              : previewData.status == 3
              ? "失败"
              : "已完成"
          }}
          <i
            class="ico_info bg"
            v-if="previewData.status == 3"
            @click="onRemark(previewData)"
          ></i
        ></span>
      </div>
      <div class="desc left">页面数量：{{ previewData.pageNum || "-" }}</div>
      <div class="desc left">上传日期：{{ previewData.createTime || "-" }}</div>
      <div class="desc left">更新日期：{{ previewData.updateTime || "-" }}</div>
      <div class="desc left">
        来源：{{
          previewData.documentType == "url"
            ? "网站"
            : previewData.documentType == "wechat"
            ? "微信公众号"
            : "文件"
        }}
      </div>
    </div>
  </a-drawer>
  <a-modal
    :visible="errorInfoVisible"
    :width="522"
    title="详细内容"
    :closable="true"
    :footer="null"
    wrapClassName="doceInfoDialog"
    @cancel="closeModal"
  >
    <div class="modal_content">
      {{ previewData.remark || "-" }}
    </div>
  </a-modal>
</template>
<script setup>
import { inject, ref, onMounted, watch, computed, nextTick, createVNode } from "vue";
import { useRoute } from "vue-router";
const axios = inject("axios");
const route = useRoute();
const previewUrl = ref("");
const previewData = ref({});
const visible = ref(true);
const errorInfoVisible = ref(false);

onMounted(() => {
  let params = route.params || {};
  getDoceDetail(params.id);
});

const onRemark = () => {
  errorInfoVisible.value = true;
};
const closeModal = () => {
  errorInfoVisible.value = false;
};
const getDoceDetail = async (id) => {
  try {
    let res = await axios.get("/api/document/info", {
      params: {
        documentId: id,
      },
    });
    let data = res.data.data || {};

    if (data.documentType == "docx" || data.documentType == "doc") {
      previewUrl.value = `https://view.officeapps.live.com/op/view.aspx?src=${data.url}`;
    } else {
      previewUrl.value = data.url;
    }
    previewData.value = data;
  } catch (err) {}
};
const fileSize = computed(() => {
  return function (size) {
    if (!size) return "0KB";
    let num = 1024.0;
    if (size < num) return size + "B";
    if (size < Math.pow(num, 2)) return (size / num).toFixed(1) + "K";
    if (size < Math.pow(num, 3)) return (size / Math.pow(num, 2)).toFixed(1) + "M";
    if (size < Math.pow(num, 4)) return (size / Math.pow(num, 3)).toFixed(2) + "G";
    return (size / Math.pow(num, 4)).toFixed(2) + "T";
  };
});
</script>
<style lang="less" scoped>
.ifarm {
  width: calc(100% - 300px);
  height: 100%;
  border: none;
  &.full {
    width: 100%;
  }
}
.doce-detail-content {
  .file-type {
    width: 100%;
    height: 114px;
    margin-bottom: 18px;
    border-radius: 8px;
    background: #fafafa;
    .ico_file {
      width: 68px;
      height: 68px;
    }
    .pdf {
      background-image: url(./components/images/ico_pdf.png);
    }
    .txt,
    .docx,
    .doc {
      background-image: url(./components/images/ico_doc.png);
    }
    .word {
      background-image: url(./components/images/ico_doc.png);
    }
    .url {
      background-image: url(./components/images/ico_url.png);
    }
    .wechat {
      background-image: url(./components/images/ico_wechat.png);
    }
    .xls {
      background-image: url(./components/images/ico_xls.png);
    }
    .zip {
      background-image: url(./components/images/ico_zip.png);
    }
  }
  .title {
    font-size: 14px;
    font-weight: 500;
    line-height: 24px;
    color: #333333;
    text-align: left;
    margin-bottom: 9px;
  }
  .desc {
    font-size: 14px;
    font-weight: normal;
    line-height: 26px;
    color: #999999;
    .status_red {
      color: #ea0000;
    }
    .status_blue {
      color: #5657f6;
    }
    .ico_info {
      width: 16px;
      height: 16px;
      background-image: url(../../assets/images/ico_info.png);
      margin-left: 2px;
      cursor: pointer;
    }
  }
}
.modal_content {
  height: 322px;
  overflow-y: auto;
  font-size: 16px;
  font-weight: 400;
  color: #333333;
  line-height: 23px;
}
</style>

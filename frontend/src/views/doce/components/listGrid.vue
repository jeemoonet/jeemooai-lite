<template>
  <div class="doce-list-grid">
    <div
      class="item"
      v-for="(item, index) in dataSource"
      :key="index"
      @click="item.type == 'folder' ? onFolder(item) : onFileDetail(item)"
    >
      <a-dropdown>
        <div class="more right" v-if="item.folderId != 1 && item.folderId != 2">
          <div></div>
          <div></div>
          <div></div>
        </div>
        <template #overlay>
          <a-menu>
            <template
              v-if="item.type == 'folder' && item.folderId != 1 && item.folderId != 2"
            >
              <a-menu-item @click="onEditFolder(item)"> 编辑 </a-menu-item>
              <a-menu-item @click="onDelFolder(item)"> 删除 </a-menu-item>
            </template>
            <template v-if="item.type != 'folder'">
              <a-menu-item @click="onManage(item)"> 向量管理 </a-menu-item>
              <a-menu-item @click="onSplit(item)"> 向量切分 </a-menu-item>
              <a-menu-item @click="onFileDetail(item)"> 文件详情 </a-menu-item>
              <a-menu-item @click="onFileDel(item)"> 删除 </a-menu-item>
            </template>
          </a-menu>
        </template>
      </a-dropdown>
      <i class="ico_file bg" :class="item.documentType || item.type"></i>
      <a-tooltip>
        <template #title>{{ item.documentName || item.url || "暂无标题" }}</template>
        <div class="title one_line">
          {{ item.documentName || item.url || "暂无标题" }}
        </div>
      </a-tooltip>

      <div class="desc one_line" v-if="item.type != 'folder'">
        {{
          item.documentType == "url" || item.documentType == "wechat"
            ? item.createTime
            : fileSize(item.fileSize)
        }}
      </div>
    </div>
    <a-empty v-if="dataSource.length == 0" description="暂无数据" />
  </div>
</template>
<script setup>
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import { inject, ref, onMounted, watch, computed, nextTick, createVNode } from "vue";
import { message, Modal } from "ant-design-vue";
import setting from "@/utils/setting";
import { useRouter } from "vue-router";
import { storeToRefs } from "pinia";
const router = useRouter();
const axios = inject("axios");
const emit = defineEmits(["update", "edit", "manage", "split"]);
const props = defineProps({
  dataSource: {
    type: Array,
    default: () => [],
  },
});
const doceParams = inject("doceParams");
const doceHistory = inject("doceHistory");

onMounted(() => {});

// 文档惭怍
const onFileDetail = (e) => {
  const href = router.resolve({
    path: "/doce/detail/" + e.documentId,
  });
  window.open(href.href, "_blank");
};

const onManage = (e) => {
  emit("manage", e);
};

const onSplit = (e) => {
  emit("split", e);
};

const onFileDel = async (e) => {
  Modal.confirm({
    title: "请确认是否删除?",
    icon: createVNode(ExclamationCircleOutlined),
    content: "删除该文件后将不可恢复，需重新上传",
    okText: "是",
    cancelText: "否",
    onOk: async () => {
      try {
        await axios.delete("/api/document", {
          params: {
            documentId: e.documentId,
          },
        });
        message.success("删除文件夹成功");
        emit("update");
      } catch (err) {
      } finally {
      }
    },
    onCancel() {},
  });
};

//文件夹操作
const onEditFolder = (e) => {
  emit("edit", e);
};
const onDelFolder = async (e) => {
  Modal.confirm({
    title: "请确认是否删除?",
    icon: createVNode(ExclamationCircleOutlined),
    content: "删除该文件夹后将不可恢复，需重新创建",
    okText: "是",
    cancelText: "否",
    onOk: async () => {
      if (doceHistory.value.length < 2) return;
      let folderType = doceHistory.value[1]["folderId"];
      try {
        await axios.delete(
          folderType == 1 ? `/api/myDocumentCategory` : `/api/documentCategory`,
          {
            params: {
              categoryId: e.folderId,
            },
          }
        );
        message.success("删除文件夹成功");
        emit("update");
      } catch (err) {
      } finally {
      }
    },
    onCancel() {},
  });
};
const onFolder = (e) => {
  doceHistory.value.push(e);
  doceParams.folderId = e.folderId;
  emit("update");
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

<style scoped lang="less">
.doce-list-grid {
  display: flex;
  align-items: flex-start;
  justify-content: flex-start;
  flex-wrap: wrap;
  width: 100%;
  max-height: calc(100%);
  background: #fff;
  border-radius: 12px;
  padding: 20px 20px 33px;
  overflow-y: auto;
}
.item {
  width: calc(100% / 7 - 32.6px);
  height: 166px;
  margin-right: 38px;
  padding: 0 8px 0;
  cursor: pointer;
  // margin-bottom: 30px;
  border-radius: 12px;
  position: relative;
  &:hover {
    background: #fafafa;
    .more {
      display: flex;
    }
  }
  &:nth-of-type(7n) {
    margin-right: 0;
  }
  .ico_file {
    width: 50px;
    height: 50px;
    margin: 30px auto 10px;
  }
  .title {
    font-size: 14px;
    font-weight: 500;
    line-height: 24px;
    text-align: center;
    color: #333333;
    margin-bottom: 2px;
  }
  .desc {
    font-size: 14px;
    font-weight: normal;
    line-height: 18px;
    text-align: center;
    color: #999999;
  }
  .more {
    position: absolute;
    right: 10px;
    top: 5px;
    display: none;
    width: 24px;
    height: 16px;
    cursor: pointer;
    > div {
      width: 3px;
      height: 3px;
      border-radius: 50%;
      background: #1d2129;
      margin-right: 3px;
      &:nth-last-of-type(1) {
        margin-right: 0;
      }
    }
    &:hover {
      > div {
        background: #5657f6;
      }
    }
  }
  .pdf {
    background-image: url(./images/ico_pdf.png);
  }
  .txt,
  .docx,
  .doc {
    background-image: url(./images/ico_doc.png);
  }
  .word {
    background-image: url(./images/ico_doc.png);
  }
  .url {
    background-image: url(./images/ico_url.png);
  }
  .wechat {
    background-image: url(./images/ico_wechat.png);
  }
  .xls {
    background-image: url(./images/ico_xls.png);
  }
  .zip {
    background-image: url(./images/ico_zip.png);
  }
  .folder {
    background-image: url(./images/ico_folder.png);
  }
}
.ant-empty {
  margin: 40px auto 20px;
}
:global(.ant-modal-confirm-confirm .ant-modal-confirm-body > .anticon) {
  color: #f44e4e;
}
</style>

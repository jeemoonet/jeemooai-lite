<template>
  <a-table
    :dataSource="dataSource"
    :columns="columns"
    :pagination="false"
    class="doce-list-table"
  >
    <template #bodyCell="{ column, record }">
      <template v-if="column.key === 'info'">
        <div
          class="row_file"
          @click="record.type == 'folder' ? onFolder(record) : onFileDetail(record)"
        >
          <i class="ico_file bg" :class="record.documentType || record.type"></i>
          <div class="info">
            <div class="title one_line">
              {{ record.documentName || record.url || "暂无标题" }}
            </div>
          </div>
        </div>
      </template>
      <template v-if="column.key === 'fileSize'">
        {{ record.type == "folder" ? "-" : fileSize(record.fileSize) }}
      </template>
      <template v-if="column.key === 'status'">
        <span
          :class="
            record.status == 3
              ? 'status_red'
              : record.status == 1 || record.status == 0
              ? 'status_blue'
              : ''
          "
          >{{
            record.status == 0
              ? "未处理"
              : record.status == 1
              ? "进行中"
              : record.status == 3
              ? "失败"
              : "已完成"
          }}</span
        >
      </template>
      <template v-if="column.key === 'action'">
        <div class="iconcenter">
          <template
            v-if="record.type == 'folder' && record.folderId != 1 && record.folderId != 2"
          >
            <a-tooltip>
              <template #title>编辑</template>

              <i class="ico_edit bg" @click="onEditFolder(record)"></i>
            </a-tooltip>
            <a-popconfirm
              title="请确认是否删除?"
              @confirm="onDelFolder(record)"
              ok-text="是"
              cancel-text="否"
            >
              <i class="ico_delete bg"></i>
            </a-popconfirm>
          </template>
          <template v-if="record.type != 'folder'">
            <a-tooltip>
              <template #title>向量管理</template>

              <i class="ico_edit bg" @click="onManage(record)"></i>
            </a-tooltip>
            <a-tooltip>
              <template #title>向量切分</template>
              <i class="ico_split bg" @click="onSplit(record)"></i>
            </a-tooltip>

            <a-tooltip>
              <template #title>文件详情</template>
              <i class="ico_info bg" @click="onFileDetail(record)"></i>
            </a-tooltip>

            <a-popconfirm
              title="请确认是否删除?"
              @confirm="onFileDel(record)"
              ok-text="是"
              cancel-text="否"
            >
              <i class="ico_delete bg"></i>
            </a-popconfirm>
          </template>
        </div>
      </template>
    </template>
  </a-table>
</template>
<script setup>
import { inject, ref, onMounted, watch, computed, nextTick } from "vue";
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
const columns = [
  {
    title: "产品介绍",
    dataIndex: "documentId",
    key: "info",
  },

  {
    title: "修改时间",
    dataIndex: "updateTime",
    key: "updateTime",
  },
  {
    title: "文件大小",
    dataIndex: "fileSize",
    key: "fileSize",
  },

  {
    title: "操作",
    key: "action",
    align: "center",
  },
];
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
};

//文件夹操作
const onEditFolder = (e) => {
  emit("edit", e);
};
const onDelFolder = async (e) => {
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
.doce-list-table {
  width: 100%;
  :deep(.ant-table) {
    .ant-table-thead {
      .ant-table-cell {
        background-color: #fff;
      }
    }
  }
}
.row_file {
  display: flex;
  align-items: center;
  justify-items: flex-start;
  //   flex-wrap: wrap;
  cursor: pointer;
  .ico_file {
    width: 46px;
    height: 46px;
  }
  .info {
    flex: 1;
    min-width: 0;
    margin-left: 10px;
    .title {
      font-size: 16px;
      font-weight: 500;
      color: #3d3d3d;
      line-height: 23px;
    }
    .desc {
      font-size: 14px;
      font-weight: 400;
      color: #9599a5;
      line-height: 20px;
      margin-top: 4px;
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
.status_red {
  color: #ea0000;
}
.status_blue {
  color: #5657f6;
}
.ico_delete {
  width: 18px;
  height: 18px;
  background-image: url(../../../assets/images/ico_del.png);
  cursor: pointer;
}
.ico_info {
  width: 18px;
  height: 18px;
  background-image: url(../../../assets/images/ico_info.png);
  margin-right: 10px;
  cursor: pointer;
}
.ico_split {
  width: 18px;
  height: 18px;
  background-image: url(../../../assets/images/ico_setting.png);
  margin-right: 10px;
  cursor: pointer;
}

.ico_edit {
  width: 18px;
  height: 18px;
  background-image: url(../../../assets/images/ico_edit.png);
  margin-right: 10px;
  cursor: pointer;
}
</style>

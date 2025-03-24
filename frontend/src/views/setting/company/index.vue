<template>
  <div class="companyContent">
    <a-form ref="formRef" :model="form" layout="vertical" name="form_in_modal">
      <!-- 大模型推理配置 -->
      <a-row :gutter="24" class="form_item">
        <a-col :span="24">
          <div class="sub_header">大模型配置</div>
        </a-col>
        <a-col :span="12">
          <a-form-item
            :name="['modelConfig', 'url']"
            label="URL 地址"
            :rules="[{ required: true, message: '请输入URL 地址' }]"
          >
            <a-input v-model:value="form.modelConfig.url" placeholder="请输入" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            :name="['modelConfig', 'key']"
            label="Key"
            :rules="[{ required: true, message: '请输入Key 地址' }]"
          >
            <a-input v-model:value="form.modelConfig.key" placeholder="请输入Key 地址" />
          </a-form-item>
        </a-col>
      </a-row>
      <!-- 模型列表 -->
      <a-row :gutter="24" class="form_item">
        <a-col :span="24">
          <div class="sub_header float">
            模型列表
            <a-button class="upload iconcenter" @click="onAdd"
              ><PlusOutlined class="ico_upload" />新增模型</a-button
            >
          </div>
        </a-col>
        <a-col :span="24">
          <a-table
            :dataSource="dataSource"
            :columns="columns"
            :pagination="false"
            @change="handleTableChange"
            class="modelTable"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'platformName'">
                <a-tag>{{ record.platformName }}</a-tag>
              </template>
              <template v-if="column.key === 'isDefault'">
                <a-switch
                  @click="onStatusChange(record, 'normal')"
                  :checked="record.isDefault == 1 ? true : false"
                />
              </template>
              <template v-if="column.key === 'isReasoningDefault'">
                <a-switch
                  @click="onStatusChange(record, 'reasoning')"
                  :checked="record.isReasoningDefault == 1 ? true : false"
                />
              </template>
              <template v-if="column.key === 'action'">
                <div class="iconcenter">
                  <i class="ico_edit bg" @click="onEdit(record)"></i>
                  <a-popconfirm
                    v-if="dataSource.length"
                    title="请确认是否删除?"
                    @confirm="onDelete(record)"
                    ok-text="是"
                    cancel-text="否"
                  >
                    <i class="ico_delete bg"></i>
                  </a-popconfirm>
                </div>
              </template>
            </template>
          </a-table>
        </a-col>
      </a-row>
      <!-- 向量模型（bge-large-zh） -->
      <a-row :gutter="24" class="form_item">
        <a-col :span="24">
          <div class="sub_header">向量模型（bge-large-zh）</div>
        </a-col>
        <a-col :span="12">
          <a-form-item
            :name="['embeddingConfig', 'key']"
            label="Key 地址"
            :rules="[{ required: true, message: '请输入Key 地址' }]"
          >
            <a-input v-model:value="form.embeddingConfig.key" placeholder="请输入" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            :name="['embeddingConfig', 'secret']"
            label="secret"
            :rules="[{ required: true, message: '请输入秘钥' }]"
          >
            <a-input
              v-model:value="form.embeddingConfig.secret"
              placeholder="请输入秘钥"
            />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <div class="tips left">
            <img src="./images/ico_tips.png" class="icon_tips" />
            <div class="tips-content">
              <div class="tips-title">如何获取向量模型（bge-large-zh）的秘钥？</div>
              <div class="tips-desc">
                请访问百度智能云密钥管理页面创建 Key 和秘钥：<a
                  href="https://console.bce.baidu.com/iam/#/iam/accesslist "
                  target="_blank"
                  rel="noopener noreferrer"
                  >https://console.bce.baidu.com/iam/#/iam/accesslist
                </a>
              </div>
            </div>
          </div>
        </a-col>
      </a-row>

      <!-- bing搜索（用于联网功能） -->
      <a-row :gutter="24" class="form_item">
        <a-col :span="24">
          <div class="sub_header">bing搜索（用于联网功能）</div>
        </a-col>
        <a-col :span="12">
          <a-form-item
            :name="['bingConfig', 'secret']"
            label="秘钥"
            :rules="[{ required: true, message: '请输入秘钥' }]"
          >
            <a-input v-model:value="form.bingConfig.secret" placeholder="请输入秘钥" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            :name="['bingConfig', 'endpoint']"
            label="节点地址"
            :rules="[{ required: true, message: '请输入节点地址' }]"
          >
            <a-input
              v-model:value="form.bingConfig.endpoint"
              placeholder="请输入节点地址"
            />
          </a-form-item>
        </a-col>
        <!-- tips -->
        <a-col :span="24">
          <div class="tips left">
            <img src="./images/ico_tips.png" class="icon_tips" />
            <div class="tips-content">
              <div class="tips-title">如何获取bing搜索的秘钥？</div>
              <div class="tips-desc">
                请访问微软bing搜索申请：<a
                  href="https://www.microsoft.com/en-us/bing/apis/bing-web-search-api"
                  target="_blank"
                  rel="noopener noreferrer"
                  >https://www.microsoft.com/en-us/bing/apis/bing-web-search-api
                </a>
              </div>
            </div>
          </div>
        </a-col>
      </a-row>
    </a-form>
    <div class="submit iconcenter">
      <a-button
        class="submit-btn"
        type="primary"
        :loading="confirmLoading"
        @click="onSubmit"
        >保存</a-button
      >
    </div>
  </div>
  <model-modal v-model:visible="visible" :editData="editData" @update="tableList" />
</template>
<script setup>
import { ref, reactive, watch, inject, onMounted, computed, nextTick } from "vue";
import { message } from "ant-design-vue";
import { LoadingOutlined, PlusOutlined } from "@ant-design/icons-vue";
import ModelModal from "./components/modelModal.vue";
import setting from "@/utils/setting";
import { InfoStore } from "@/stores/user";
import { storeToRefs } from "pinia";
const infoStore = InfoStore();
const { industryList } = storeToRefs(infoStore);
const axios = inject("axios");
const formRef = ref(null);
const confirmLoading = ref(false);
const columns = [
  {
    title: "展示名称",
    dataIndex: "modelName",
    key: "modelName",
  },
  {
    title: "模型名称",
    dataIndex: "modelLabel",
    key: "modelLabel",
  },
  {
    title: "厂商名称",
    dataIndex: "platformName",
    key: "platformName",
  },

  {
    title: "默认普通模型",
    dataIndex: "isDefault",
    key: "isDefault",
  },
  {
    title: "默认推理模型",
    dataIndex: "isReasoningDefault",
    key: "isReasoningDefault",
  },
  {
    title: "操作",
    key: "action",
    align: "center",
  },
];
//数据源
const dataSource = ref([]);
//当前页
const current = ref(1);
//总数
const total = ref(0);
const limit = 10;
const pagination = computed(() => ({
  pageSize: limit,
  current: current.value,
  total: total.value,
  showSizeChanger: false,
}));
const initData = () => {
  return {
    modelConfig: {
      url: "",
      key: "",
    },
    embeddingConfig: {
      secret: "",
      key: "",
    },
    bingConfig: {
      secret: "",
      endpoint: "",
    },
  };
};
const form = reactive(initData());
//内容
const editData = ref({});
const visible = ref(false);

onMounted(() => {
  tableList();
  initSetting();
});

const onSubmit = () => {
  formRef.value
    .validateFields()
    .then((values) => {
      updateCompanyData();
    })
    .catch((info) => {});
};

const updateCompanyData = async () => {
  confirmLoading.value = true;
  try {
    await axios.post("/api/init/setting", {
      ...form,
    });
    let res = await axios.get("/api/init");
    let data = res.data.data || {};
    infoStore.updateIsInit(data.isInit || 0);
    message.info(data.isInit == 1 ? "初始化成功" : data.errorMsg);
  } catch (err) {
  } finally {
    confirmLoading.value = false;
  }
};

const handleTableChange = (page, filters, sorter) => {
  current.value = page.current;
  nextTick(() => {
    tableList();
  });
};

const onAdd = () => {
  editData.value = {};
  nextTick(() => {
    visible.value = true;
  });
};

const onEdit = async (record) => {
  editData.value = record;
  nextTick(() => {
    visible.value = true;
  });
};

const onDelete = async (record) => {
  await axios.post("/api/init/models/delete", {
    id: record.id,
  });
  tableList();
};

const onStatusChange = async (record = {}, type = "normal") => {
  try {
    let status = type == "normal" ? record.isDefault : record.isReasoningDefault;
    await axios.post(
      type == "normal"
        ? "/api/init/models/setDefault"
        : "/api/init/models/setReasoningDefault",
      {
        id: record.id,
        status: status == 0 ? 1 : 0,
      }
    );
    tableList();
  } catch (err) {}
};

const initSetting = async () => {
  try {
    let res = await axios.get("/api/init/setting", {});
    let data = res.data.data || {};
    Object.assign(form, data);
  } catch (err) {}
};

const tableList = async () => {
  try {
    let res = await axios.get("/api/init/models", {
      params: {
        pageSize: limit,
        pageNum: current.value,
      },
    });
    let data = res.data.data || res.data.rows || [];
    dataSource.value = data;
    total.value = res.data.total || 0;
  } catch (err) {}
};
</script>
<style scoped lang="less">
.companyContent {
  .header {
    font-size: 26px;
    font-weight: 700;
    color: #333333;
    line-height: 38px;
    letter-spacing: 1px;
  }
  .sub_header {
    font-size: 16px;
    font-weight: 500;
    line-height: 28px;
    color: #374151;
    margin-bottom: 10px;
    .upload {
      width: fit-content;
      height: 32px;
      border-radius: 8px;
      background: #5657f6;
      border: 1px solid #5657f6;
      font-size: 14px;
      font-weight: 500;
      line-height: 42px;
      color: #fff;
      border: none;
      transition: all 0.15s ease-in-out;
      margin-right: 10px;
      padding: 0 16px;
      .ico_upload {
        font-size: 16px;
      }

      &:hover {
        background: #3838c0;
      }
    }
  }
  .form_item {
    margin-bottom: 20px;
    .ant-form-item {
      margin-bottom: 10px;
    }
    .ant-input {
      border: none;
    }
    :deep(.ant-select-selector) {
      border: none;
      height: 30px;
    }
    .personMobile {
      border-left: 1px solid #e8e8e9;
      &:focus {
        border: none;
      }
    }
  }

  textarea.ant-input {
    height: 90px;
  }

  .tips {
    width: 100%;
    align-items: flex-start;
    padding: 17px 15px 14px;
    border-radius: 8px;
    background: rgba(86, 87, 246, 0.1);

    .icon_tips {
      display: flex;
      width: 16px;
      height: 16px;
    }
    .tips-content {
      flex: 1;
      min-width: 0;
      padding-left: 8px;
      .tips-title {
        font-size: 14px;
        font-weight: 500;
        line-height: 20px;
        color: #333333;
      }
      .tips-desc {
        font-size: 14px;
        font-weight: normal;
        line-height: 20px;
        color: #9599a5;
        margin-top: 4px;
        > a {
          color: #5657f6;
        }
      }
    }
  }

  .submit {
    width: 100%;
    padding-bottom: 20px;
    .submit-btn {
      width: 162px;
      height: 46px;
      font-size: 16px;
      border-radius: 8px;
    }
  }
}

.modelTable {
  :deep(.ant-table) {
    .ant-table-thead {
      .ant-table-cell {
        background-color: #e8e9eb;
      }
    }
  }
  :deep(.ant-switch-checked) {
    background-color: #5657f6;
  }
}
.ico_delete {
  width: 18px;
  height: 18px;
  background-image: url(../../../assets/images/ico_del.png);
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

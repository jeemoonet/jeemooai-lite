<template>
  <a-modal
    :visible="visible"
    width="690px"
    title="员工管理"
    :closable="true"
    cancelText="取消"
    okText="保存"
    @cancel="closeModal"
    @ok="submit"
    :confirmLoading="confirmLoading"
  >
    <a-form
      ref="formRef"
      :model="form"
      :rules="rules"
      name="form_in_modal"
      layout="horizontal"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 18 }"
    >
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item name="nickName" label="姓名">
            <a-input v-model:value="form.nickName" placeholder="请输入姓名" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item name="userName" label="手机号">
            <a-input-number
              :controls="false"
              v-model:value="form.userName"
              placeholder="请输入手机号"
              style="width: 100%"
            /> </a-form-item
        ></a-col>
        <!-- <a-col :span="12">
          <a-form-item name="email" label="邮箱">
            <a-input v-model:value="form.email" placeholder="请输入邮箱" />
          </a-form-item>
        </a-col> -->
        <!-- <a-col :span="12">
          <a-form-item name="roleIds" label="角色">
            <a-select
              mode="multiple"
              v-model:value="form.roleIds"
              placeholder="请选择角色"
            >
              <a-select-option
                v-for="(item, index) in roleList"
                :key="index"
                :value="item.roleId"
                >{{ item.roleName }}</a-select-option
              >
            </a-select>
          </a-form-item>
        </a-col> -->
        <a-col :span="12">
          <a-form-item name="status" label="状态">
            <a-select v-model:value="form.status" placeholder="请选择状态">
              <a-select-option value="0">正常</a-select-option>
              <a-select-option value="1">禁用</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item name="password" label="密码">
            <a-input-password v-model:value="form.password" placeholder="请输入密码" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item name="verifyPassword" label="确认密码">
            <a-input-password
              v-model:value="form.verifyPassword"
              placeholder="请再次确认密码"
            />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item
            name="remark"
            label="备注"
            :label-col="{ span: 3 }"
            :wrapper-col="{ span: 21 }"
          >
            <a-textarea v-model:value="form.remark" placeholder="请输入备注" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>
<script setup>
import { message } from "ant-design-vue";
import { ref, reactive, onMounted, defineProps, defineEmits, watch, inject } from "vue";
const axios = inject("axios");
const emit = defineEmits(["update:visible", "update"]);
const props = defineProps({
  visible: {
    type: Boolean,
    default: () => false,
  },
  editData: {
    type: Object,
    default: () => {},
  },
});

const formRef = ref(null);
const confirmLoading = ref(false);
const initData = () => {
  return {
    userName: "",
    nickName: "",
    email: "",
    status: "0",
    remark: "",
    userId: null,
    roleIds: [],
    password: null,
  };
};
const form = ref(initData());
const rules = {
  nickName: [{ required: true, message: "请输入姓名" }],
  userName: [{ required: true, message: "请输入手机号" }],
  status: [{ required: true, message: "请选择状态" }],
  roleIds: [{ required: true, message: "请选择角色" }],
  password: [
    {
      required: form.value.userId ? false : true,
      validator: async (rule, value) => {
        if (form.value.userId && !value) {
          return Promise.resolve();
        }
        if (value != form.value.verifyPassword && form.value.verifyPassword) {
          return Promise.reject("密码不一致,请检查密码");
        }
        let lengthRegex = /^(?=.*[a-zA-Z0-9!@#$%^&*()_+{}|:<>?]).{8,14}$/;
        let varietyRegex = /^(?![\d]+$)(?![a-zA-Z]+$)(?![!@#$%^&*()_+{}|:<>?]+$)[\da-zA-Z!@#$%^&*()_+{}|:<>?]+$/;
        let noSpaceChineseRegex = /^(?!.*[\s\u4e00-\u9fa5]).+$/;
        let validatePassword =
          lengthRegex.test(value) &&
          varietyRegex.test(value) &&
          noSpaceChineseRegex.test(value);
        if (!validatePassword) {
          return Promise.reject("密码长度为8~14,字母/数字以及标点符号至少包含2种");
        }

        return Promise.resolve();
      },
    },
  ],
  verifyPassword: [
    {
      required: form.value.userId ? false : true,
      validator: async (rule, value) => {
        if (form.value.userId && !form.value.password) {
          return Promise.resolve();
        }
        if (value != form.value.password && form.value.password) {
          return Promise.reject("密码不一致,请检查密码");
        }
        return Promise.resolve();
      },
    },
  ],
};
const roleList = ref([]);

onMounted(() => {});

const getUserData = async (userId) => {
  try {
    let res = await axios.get(`/api/user`, {
      params: {
        userId: userId,
      },
    });
    let data = res.data.data || {};
    let _data = {
      ...data.user,
      roleIds: data.roleIds || [],
    };

    form.value = Object.assign({}, _data);
  } catch (err) {
  } finally {
  }
};

const submit = () => {
  formRef.value
    .validateFields()
    .then((values) => {
      if (form.value.userId) {
        editSubmit();
      } else {
        addSubmit();
      }
    })
    .catch((info) => {});
};

const addSubmit = async () => {
  confirmLoading.value = true;
  try {
    let res = await axios.post(`/api/user/create`, {
      ...form.value,
    });
    formRef.value.resetFields();
    emit("update");
    closeModal();
  } catch (err) {
  } finally {
    confirmLoading.value = false;
  }
};

const editSubmit = async () => {
  confirmLoading.value = true;
  try {
    let res = await axios.post(`/api/user/update`, {
      ...form.value,
      password: form.value.password || null,
    });
    formRef.value.resetFields();
    emit("update");
    closeModal();
  } catch (err) {
  } finally {
    confirmLoading.value = false;
  }
};

const closeModal = () => {
  formRef.value.resetFields();
  emit("update:visible", false);
};

watch(
  () => props.visible,
  (newVal, oldVal) => {
    if (newVal == true) {
      if (props.editData && props.editData.userId) {
        getUserData(props.editData.userId);
      } else {
        form.value = initData();
      }
    }
  },
  { deep: true, immediate: true }
);
</script>

<style lang="less" scoped>
.modal_content {
  height: 322px;
  overflow-y: auto;
  font-size: 16px;
  font-weight: 400;
  color: #333333;
  line-height: 23px;
}
</style>

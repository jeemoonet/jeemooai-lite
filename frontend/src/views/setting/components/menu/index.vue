<template>
  <div class="menuPage">
    <div class="nav_title_box">
      <div class="nav_title_content">
        <div class="nav_title">企业管理</div>
      </div>
    </div>

    <!-- 窗口列表 -->
    <a-menu @click="selectMenuItem" v-model:selectedKeys="selectedKeys" mode="inline">
      <!-- 企业资料 -->
      <div>
        <a-sub-menu key="companySetting">
          <template #icon>
            <i class="company bg"></i>
          </template>
          <template #title>企业信息</template>
          <!-- 套餐与订单 -->
          <!-- 企业资料 -->
          <div>
            <a-menu-item key="setting">
              <span class="nav-text">模型配置</span>
            </a-menu-item>
          </div>
          <!-- 微信授权 -->

          <!-- APIKey管理 -->

          <!-- MAC地址管理 -->
        </a-sub-menu>
      </div>

      <!-- 员工管理 -->
      <div>
        <a-sub-menu key="userSetting">
          <template #icon>
            <i class="user bg"></i>
          </template>
          <template #title>员工管理</template>
          <div>
            <a-menu-item key="user">
              <span class="nav-text">员工管理</span>
            </a-menu-item>
          </div>
          <!-- 角色管理 -->
        </a-sub-menu>
      </div>

      <!-- 内容管理 -->
      <!-- 应用管理 -->
      <div>
        <a-sub-menu key="applySetting">
          <template #icon>
            <i class="ico_apply bg"></i>
          </template>
          <!-- 应用管理 -->
          <template #title>应用管理</template>
          <div>
            <a-menu-item key="promptCategory">
              <span class="nav-text">助手分类</span>
            </a-menu-item>
          </div>
          <!-- 助手管理 -->
          <div>
            <a-menu-item key="promptSetting">
              <span class="nav-text">助手管理</span>
            </a-menu-item>
          </div>
        </a-sub-menu>
      </div>
      <!-- 数据统计 -->

      <!-- 企业日志 -->
    </a-menu>
  </div>
</template>
<script setup>
import { inject, ref, onMounted, watch } from "vue";
import {
  MessageOutlined,
  DeleteOutlined,
  EditOutlined,
  TeamOutlined,
} from "@ant-design/icons-vue";
import { useRouter } from "vue-router";
import { message } from "ant-design-vue";
const selectedKeys = ref(["setting"]);
const router = useRouter();
onMounted(() => {});

//选择窗口
const selectMenuItem = ({ item, key, keyPath }) => {
  const routerObj = {
    company: "setting",
    user: "user",
    documentCategory: "document-category",
    promptCategory: "prompt-category",
  };
  router.push({
    name: key,
  });
};
watch(
  () => router.currentRoute.value,
  (newVal, oldVal) => {
    if (newVal.name) {
      selectedKeys.value = [newVal.name || ""];
    }
  },
  { deep: true, immediate: true }
);
</script>
<style lang="less" scoped>
.menuPage {
  width: 260px;
  height: 100%;
  overflow-y: auto;
  background-color: #f5f6f8;
  :deep(.ant-menu) {
    max-height: calc(100% - 80px);
    overflow-y: auto;
    background-color: transparent;
    border: none;
    font-size: 16px;
    .ant-menu-item-selected {
      background-color: transparent !important;
      color: #5657f6;
    }
    .ant-menu-sub {
      background-color: transparent;
    }
    .ant-menu-item {
      // display: flex;
      font-size: 16px;
      font-weight: 500;
      height: 44px;
      margin-top: 0;
      margin-bottom: 0;
      .ant-menu-title-content {
        margin-left: 14px;
      }
      &:hover {
        background-color: transparent;
        color: #5657f6;
      }
    }

    .ant-menu-title-content {
      display: flex;
      align-items: center;
      justify-content: flex-start;
    }

    .ant-menu-submenu-title {
      .ant-menu-title-content {
        font-weight: 600;
        margin-left: 22px;
      }
      &:hover {
        color: #5657f6;
        .company {
          background-image: url(../../images/company1.png);
        }
        .user {
          background-image: url(../../images/user1.png);
        }
        .contentManager {
          background-image: url(../../images/contentManager1.png);
        }
        .companyLog {
          background-image: url(../../images/log1.png);
        }
        .ico_apply {
          background-image: url(../../images/apply1.png);
        }

        .ico_dashboard {
          background-image: url(../../images/dashboard1.png);
        }
      }
    }

    .ant-menu-submenu-selected,
    .ant-menu-submenu-active {
      color: #5657f6;
      .company {
        background-image: url(../../images/company1.png);
      }
      .user {
        background-image: url(../../images/user1.png);
      }
      .contentManager {
        background-image: url(../../images/contentManager1.png);
      }
      .companyLog {
        background-image: url(../../images/log1.png);
      }
      .ico_apply {
        background-image: url(../../images/apply1.png);
      }

      .ico_dashboard {
        background-image: url(../../images/dashboard1.png);
      }
    }
  }
}
.nav_title_box {
  padding-left: 30px;
  margin-top: 0px;
  margin-bottom: 16px;
  .nav_title_content {
    padding: 20px 0;
    border-bottom: 1px solid #1d212910;
    .nav_title {
      font-size: 16px;
      font-weight: 600;
      color: #333333;
      line-height: 24px;
    }
  }
}
.title {
  width: fit-content;
  font-size: 26px;
  font-weight: 700;
  color: #ffffff;
  line-height: 38px;
  letter-spacing: 1px;
  position: relative;
  z-index: 2;

  &::after {
    content: "";
    display: block;
    width: 28px;
    height: 28px;
    border-radius: 50%;
    z-index: -1;
    position: absolute;
    right: -10px;
    top: 0px;
    background-color: rgba(86, 87, 246, 0.4);
  }
}

.nav-text {
  display: flex;
  // display: block;
  align-items: center;
  width: 168px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.company {
  width: 18px;
  height: 18px;
  background-image: url(../../images/company.png);
}
.user {
  width: 18px;
  height: 18px;
  background-image: url(../../images/user.png);
}
.contentManager {
  width: 18px;
  height: 18px;
  background-image: url(../../images/contentManager.png);
}
.companyLog {
  width: 18px;
  height: 18px;
  background-image: url(../../images/log.png);
}
.ico_apply {
  width: 18px;
  height: 18px;
  background-image: url(../../images/apply.png);
}

.ico_dashboard {
  width: 18px;
  height: 18px;
  font-size: 18px;
  background-image: url(../../images/dashboard.png);
}
</style>

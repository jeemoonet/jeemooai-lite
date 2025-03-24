<template>
  <div class="nav_box">
    <div class="nav_title_box">
      <div class="nav_title_content">
        <div class="nav_title" v-if="!collapsed">{{ currentNavName }}</div>
        <div class="collapsed" @click="collapsed = !collapsed">
          <MenuUnfoldOutlined v-if="collapsed" />
          <MenuFoldOutlined v-else />
        </div>
      </div>
    </div>
    <Menu
      :inline-collapsed="collapsed"
      class="menu"
      :selectedKeys="selectedKeys"
      @click="handleClick"
      mode="inline"
    >
      <MenuItem key="prompt" class="menu_item">
        <template #icon>
          <div class="icon intellAassist"></div>
        </template>
        <span>智能助手</span>
      </MenuItem>

      <MenuItem key="doce" class="menu_item">
        <template #icon>
          <div class="icon lib"></div>
        </template>
        <span>知识库</span>
      </MenuItem>
    </Menu>
  </div>
</template>

<script setup>
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  PieChartOutlined,
} from "@ant-design/icons-vue";
import { MenuItem, Menu } from "ant-design-vue";
import "ant-design-vue/es/menu/style/css";
import { ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { WorkflowStore } from "@/stores/workflow.js";
const { setCollapsed } = WorkflowStore();
const route = useRoute();
const router = useRouter();
const collapsed = ref(false);
const selectedKeys = ref(["prompt"]);
const handleClick = ({ key }) => {
  selectedKeys.value = [key];
  router.push({ name: key });
};
const currentNavName = ref("智能助手");
watch(
  collapsed,
  (val) => {
    setCollapsed(val);
  },
  { immediate: true }
);
watch(
  () => route.name,
  (newPath) => {
    if (newPath === "workFlow") {
      currentNavName.value = "工作流";
    }
    if (newPath === "doce") {
      currentNavName.value = "知识库";
    }
    if (newPath === "pluginTools" || newPath === "toolList" || newPath === "createTool") {
      currentNavName.value = "添加插件";
    }
    if (newPath === "dataBase") {
      currentNavName.value = "数据库";
    }
    if (["createTool", "pluginTools", "toolList"].includes(newPath)) {
      selectedKeys.value = ["pluginTools"];
      return;
    }
    selectedKeys.value = [newPath];
  },
  { immediate: true }
);
</script>

<style lang="less" scoped>
.nav_box {
  // height: 100%;

  .nav_title_box {
    padding: 0px 30px;
    padding-right: 0;
    margin-bottom: 20px;
    .nav_title_content {
      display: flex;
      justify-content: space-between;
      box-sizing: border-box;
      border-bottom: 1px solid #1d212910;
      padding: 20px 0;
    }
    .nav_title {
      font-family: Alibaba PuHuiTi 3;
      font-size: 16px;
      font-weight: 600;
      line-height: 20px;
      letter-spacing: 0.02em;

      font-variation-settings: "opsz" auto;
      /* 正文色-浅底 */
      color: #333333;
    }
    .collapsed {
      cursor: pointer;
      width: 20px;
      height: 20px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
  &:deep(.menu) {
    background: transparent;
    border-color: transparent;
    &.ant-menu-inline-collapsed {
      .menu_item {
        width: auto;
      }
    }
    .menu_item {
      width: 200px;
      font-size: 14px;
      color: #1d2129;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      padding-left: 30px;
      .ant-menu-item-icon {
        min-width: 20px;
      }
      .ant-menu-title-content {
        margin-left: 22px;
      }
      span {
        font-weight: 500;
      }
      &.ant-menu-item-selected {
        color: #5657f6;
        background: transparent;
        span {
          font-weight: 600;
        }
        .icon {
          &.intellAassist {
            background: url("../images/intellAassist_yes.png") no-repeat center center;
            background-size: 100%;
          }
          &.workFlow {
            background: url("../images/workFlow_icon_active.png") no-repeat center center;
            background-size: 100%;
          }
          &.plugin {
            background: url("../images/plugin_icon_active.png") no-repeat center center;
            background-size: 100%;
          }
          &.lib {
            background: url("../images/libur_icon_active.png") no-repeat center center;
            background-size: 100%;
          }
          &.data_base {
            background: url("../images/data_base_active.png") no-repeat center center;
            background-size: 100%;
          }
        }
      }
      .icon {
        width: 20px;
        height: 20px;
        display: inline-block;
        &.intellAassist {
          background: url("../images/intellAassist.png") no-repeat center center;
          background-size: 100%;
        }
        &.workFlow {
          background: url("../images/workFlow_icon.png") no-repeat center center;
          background-size: 100%;
        }
        &.plugin {
          background: url("../images/plugin_icon.png") no-repeat center center;
          background-size: 100%;
        }
        &.lib {
          background: url("../images/libur_icon.png") no-repeat center center;
          background-size: 100%;
        }
        &.data_base {
          background: url("../images/data_base.png") no-repeat center center;
          background-size: 100%;
        }
      }
    }
  }
}
</style>

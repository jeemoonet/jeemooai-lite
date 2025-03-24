<template>
  <div class="headerMenu theme_blue iconcenter" v-if="headerVisible">
    <div class="headerMenu_w float">
      <!-- logo -->
      <router-link to="/" class="logo left">
        <img :src="getUserInfo.logo" v-if="getUserInfo.logo" class="ico_logo" />
        {{ getUserInfo.companyName }}
      </router-link>
      <!-- menu -->
      <div class="headerMenu_list left">
        <div
          @click.stop="onRoute('/index')"
          class="headerMenu_item iconcenter"
          :class="name == 'index' ? 'active' : ''"
        >
          工作台
        </div>
        <div
          @click.stop="onRoute('/chat')"
          class="headerMenu_item iconcenter"
          :class="name == 'chat' ? 'active' : ''"
        >
          工作助手
        </div>
        <!-- 仅管理员可见 -->
        <template v-if="getUserInfo.admin">
          <div
            @click.stop="onRoute('/intell-aassist')"
            class="headerMenu_item iconcenter"
            :class="routeName ? 'active' : ''"
          >
            智能助手
          </div>
          <div
            @click.stop="onRoute('/setting')"
            class="headerMenu_item iconcenter"
            :class="name == 'setting' ? 'active' : ''"
          >
            企业管理
          </div>
        </template>
      </div>
      <div class="right">
        <div class="help left" @click="onOpen(`${setting.jeemooHelpUrl}`)">
          <i class="ico_help bg"></i>
          帮助
        </div>
        <a-popover
          placement="bottom"
          :arrowPointAtCenter="true"
          overlayClassName="header_user_popover"
        >
          <template #content>
            <router-link class="user_item" to="/center">个人中心</router-link>
            <div class="user_item" @click.stop="outLogin">退出登录</div>
          </template>
          <div class="user left">
            <img :src="getUserInfo.avatar" />
            <span class="one_line">{{ getUserInfo.nickName }}</span>
          </div>
        </a-popover>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted, inject, nextTick, watch, computed } from "vue";
import { useRouter } from "vue-router";
import setting from "@/utils/setting";
const router = useRouter();
import { InfoStore } from "@/stores/user";
import { storeToRefs } from "pinia";
const infoStore = InfoStore();
const { getUserInfo } = storeToRefs(infoStore);
const name = ref("");
onMounted(() => {});
const onPluginItem = (res) => {
  console.log(res);
};
const onRoute = (path) => {
  router.push({
    path: path,
  });
};

const onOpen = (url) => {
  window.open(url);
};
//退出登录
const outLogin = () => {
  infoStore.clearUserInfo();
};

//插件配置 显示控制
const headerVisible = computed(() => {
  let visible = setting.headerHiddenList.indexOf(name.value);
  return visible == -1 ? true : false;
});
const routeName = ref(false);
watch(
  () => router.currentRoute.value,
  (newVal, oldVal) => {
    if (newVal.meta.theme_blue) {
      routeName.value = true;
    } else {
      routeName.value = false;
    }
    if (newVal.meta.parentName) {
      name.value = newVal.meta.parentName || "";
      return;
    }
    name.value = newVal.name || "";
  }
);
</script>
<style lang="less">
.header_user_popover {
  .ant-popover-arrow {
    top: -8px;
  }
  .ant-popover-inner-content {
    .user_item {
      display: block;
      line-height: 20px;
      font-size: 14px;
      font-weight: 400;
      color: #3d3d3d;
      cursor: pointer;
      padding-bottom: 14px;
      border-bottom: 1px solid #e8e8e9;
      padding-top: 14px;
      &:nth-child(1) {
        padding-top: 0;
      }
      &:nth-last-child(1) {
        padding-bottom: 0;
        border-bottom: none;
      }
    }
  }
}
</style>
<style lang="less" scoped>
.headerMenu {
  width: 100%;
  height: 65px;
  background-color: #2c344b;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 3;

  &.hide {
    display: none;
  }
  &.iconcenter {
    display: flex;
  }
  .headerMenu_w {
    width: 100%;
    height: 100%;
    padding: 0 20px;
    overflow: hidden;
    .logo {
      font-size: 20px;
      font-weight: 500;
      color: #ffffff;
      line-height: 23px;
      cursor: pointer;
      .ico_logo {
        width: 24px;
        height: 24px;
        // background-image: url("../../assets/images/logo.png");
        margin-right: 11px;
      }
    }
    .headerMenu_list {
      height: 100%;
      .headerMenu_item {
        min-width: 32px;
        font-size: 16px;
        font-weight: 400;
        color: #9599a5;
        // line-height: 23px;
        padding-left: 26px;
        padding-right: 26px;
        position: relative;
        transition: 0.2s all linear;
        cursor: pointer;
        height: 100%;

        &.active,
        &.ant-dropdown-open {
          color: #ffffff;
          font-weight: 700;
          &::before {
            content: "";
            position: absolute;
            // bottom: -22px;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
            width: calc(100% - 52px);
            height: 100%;
            border-bottom: 4px solid #ffffff;
            transition: 0.2s all linear;
          }
        }
        &:hover {
          color: #ffffff;
          font-weight: 700;
          &::before {
            content: "";
            position: absolute;
            // bottom: -22px;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
            width: calc(100% - 52px);
            height: 100%;
            border-bottom: 4px solid #ffffff;
            transition: 0.2s all linear;
          }
        }

        // &::before {
        //   content: "";
        //   position: absolute;
        //   bottom: -22px;
        //   left: 100%;
        //   width: 0;
        //   height: 100%;
        //   border-bottom: 4px solid #ffffff;
        //   transition: 0.2s all linear;
        // }

        // &:hover::before {
        //   width: calc(100% - 52px);
        //   bottom: -22px;
        //   left: 50%;
        //   transition-delay: 0.1s;
        //   border-bottom-color: #ffffff;
        //   z-index: -1;
        //   transform: translateX(-50%);
        // }
        // &:hover {
        //   color: #ffffff;
        //   font-weight: 700;
        // }

        // &.active {
        //   color: #fff;
        //   font-weight: 700;
        //   &::before {
        //     width: calc(100% - 52px);
        //     bottom: -22px;
        //     left: 50%;
        //     transition-delay: 0.1s;
        //     border-bottom-color: #ffffff;
        //     z-index: -1;
        //     transform: translateX(-50%);
        //   }
        // }
      }

      // .headerMenu_item:hover ~ .headerMenu_item::before {
      //   left: 0;
      // }
    }
    .help {
      font-size: 16px;
      font-weight: 400;
      color: #ffffff;
      line-height: 23px;
      cursor: pointer;
      .ico_help {
        width: 18px;
        height: 18px;
        background-image: url("../../assets/images/ico_question.png");
        margin-right: 5px;
      }
    }

    .user {
      font-size: 16px;
      font-weight: 400;
      color: #ffffff;
      line-height: 23px;
      margin-left: 20px;
      cursor: pointer;
      > img {
        display: block;
        width: 32px;
        height: 32px;
        border-radius: 50%;
        margin-right: 5px;
        background-color: rgba(255, 255, 255, 0.4);
      }
      > span {
        width: 50px;
      }
    }
  }
  &.theme_blue {
    background: linear-gradient(180deg, #5657f6 0%, #4d4eee 100%);
    .headerMenu_w .headerMenu_list .headerMenu_item {
      color: #ffffff;
      &:hover {
        font-weight: normal;
        &::before {
          // display: none;
          opacity: 0;
        }
      }
    }
    .headerMenu_w .headerMenu_list .headerMenu_item.active {
      height: 38px;
      border-radius: 19px;
      background: #ffffff;
      color: #5657f6;
      font-weight: 500;
    }
  }
}
</style>

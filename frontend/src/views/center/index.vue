<template>
  <div class="centerPage headerMenuPadding left">
    <div class="userInfo">
      <h2 class="header">个人信息</h2>
      <img class="avatar" :src="getUserInfo.avatar" />
      <div class="nickName">{{ getUserInfo.nickName }}</div>
      <div class="info_item float">
        <div class="title left"><i class="ico_photo bg"></i>登录账号</div>
        <div class="txt one_line">{{ getUserInfo.userName || "暂无" }}</div>
      </div>
      <div class="info_item float">
        <div class="title left"><i class="ico_email bg"></i>用户邮箱</div>
        <div class="txt one_line">{{ getUserInfo.email || "暂无" }}</div>
      </div>
      <div class="info_item float">
        <div class="title left"><i class="ico_date bg"></i>创建日期</div>
        <div class="txt one_line">{{ getUserInfo.createTime || "暂无" }}</div>
      </div>
      <a class="channel" target="_blank" :href="setting.jeemooHelpUrl">
        <img src="@/assets/images/banner_channel.png" alt="" />
      </a>
    </div>
    <div class="layout">
      <div class="layout-content">
        <h2 class="header">基本资料</h2>
        <div class="tabbar left">
          <div
            class="tabbar_item"
            @click="tabbar = 0"
            :class="tabbar == 0 ? 'active' : ''"
          >
            修改资料
          </div>
          <div
            class="tabbar_item"
            @click="tabbar = 1"
            :class="tabbar == 1 ? 'active' : ''"
          >
            修改密码
          </div>
        </div>
        <template v-if="tabbar == 0">
          <update-info />
        </template>
        <template v-else>
          <update-password />
        </template>
      </div>
    </div>
  </div>
</template>
<script setup>
import { inject, ref, onMounted, watch, computed, nextTick } from "vue";
import { message } from "ant-design-vue";
import UpdateInfo from "./components/info.vue";
import UpdatePassword from "./components/password.vue";
import setting from "@/utils/setting";
import { useRouter, RouterView } from "vue-router";
import { InfoStore } from "@/stores/user";
import { storeToRefs } from "pinia";
const infoStore = InfoStore();
const { getUserInfo } = storeToRefs(infoStore);
const router = useRouter();
const axios = inject("axios");
const tabbar = ref(0);

onMounted(() => {});
</script>

<style scoped lang="less">
.centerPage {
  width: 100%;
  height: 100%;
}
.header {
  font-size: 26px;
  font-weight: 700;
  color: #333333;
  line-height: 38px;
  letter-spacing: 1px;
}
.userInfo {
  width: 420px;
  height: 100%;
  background-color: #fff;
  padding: 36px 20px 0;
  .avatar {
    display: block;
    width: 80px;
    height: 80px;
    border-radius: 50%;
    margin: 20px auto 4px;
  }
  .nickName {
    width: 100%;
    font-size: 18px;
    font-weight: 500;
    color: #333333;
    line-height: 28px;
    text-align: center;
    margin-bottom: 15px;
  }

  .info_item {
    width: 100%;
    border-bottom: 1px solid #e8e8e9;
    padding-bottom: 16px;
    margin-bottom: 17px;
    padding-right: 7px;
    .bg {
      width: 18px;
      height: 18px;
      margin-right: 13px;
    }
    .ico_photo {
      background-image: url(../../assets/images/ico_photo.png);
    }
    .ico_email {
      background-image: url(../../assets/images/ico_email.png);
    }
    .ico_date {
      background-image: url(../../assets/images/ico_date.png);
    }
    .title {
      font-size: 16px;
      font-weight: 400;
      color: #333333;
      line-height: 28px;
    }
    .txt {
      width: 240px;
      font-size: 16px;
      font-weight: 400;
      color: #333333;
      line-height: 28px;
      text-align: right;
    }
  }
  .channel {
    display: flex;
    width: 100%;
    height: 110px;
    margin-top: 30px;
    img {
      display: block;
      width: 100%;
      height: 100%;
    }
  }
}
.layout {
  display: flex;
  flex: auto;
  flex-direction: column;
  width: calc(100% - 420px);
  height: 100%;
  margin: 0 auto;
  position: relative;
  overflow-y: auto;
  .layout-footer {
    flex: 0 0 auto;
    width: 100%;
    height: 40px;
    text-align: center;
    font-size: 14px;
    font-weight: 400;
    color: #999999;
    line-height: 19px;
  }
  .layout-content {
    min-height: auto;
    flex: auto;
    padding: 36px 84px 76px;
  }
}

.tabbar {
  width: 100%;
  padding-bottom: 18px;
  border-bottom: 1px solid #e8e8e9;
  margin-top: 30px;
  margin-bottom: 20px;
  .tabbar_item {
    margin-right: 40px;
    font-size: 18px;
    font-weight: 400;
    color: #333333;
    line-height: 26px;
    position: relative;
    cursor: pointer;
    &.active {
      font-weight: 700;
      &::after {
        content: "";
        width: 100%;
        height: 3px;
        background-color: #5657f6;
        position: absolute;
        bottom: -19px;
        left: 0;
      }
    }
  }
}
</style>

import { createRouter, createWebHistory } from "vue-router";
import { notification } from 'ant-design-vue'
import setting from "../utils/setting";
import HomeView from "../views/login/index.vue";
import { InfoStore } from "../stores/user";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/login",
      name: "login",
      component: HomeView,
    },
    {
      path: "/index",
      name: "index",
      component: () => import("../views/index/index.vue"),
    },
    {
      path: "/chat",
      name: "chat",
      component: () => import("../views/chat/index.vue"),
    },
    {
      path: "/center",
      name: "center",
      component: () => import("../views/center/index.vue"),
    },
    {
      path: "/404",
      name: "404",
      component: () => import("../views/404.vue"),
    },
    {
      path: "/setting",
      meta: {
        parentName: "setting",
        admin: true
      },

      component: () => import("../views/setting/index.vue"),
      children: [
        {
          path: "",
          meta: {},
          name: "setting",
          component: () => import("../views/setting/company/index.vue"),
        },
        {
          path: "/user",
          meta: {

          },
          name: "user",
          component: () => import("../views/setting/user/index.vue"),
        },
        {
          path: "/prompt-category",
          meta: {

          },
          name: "promptCategory",
          component: () =>
            import("../views/setting/contentManager/promptCategory.vue"),
        },
        {
          path: "/prompt-setting",
          meta: {
            parentName: "setting",
          },
          name: "promptSetting",
          component: () =>
            import("../views/setting/contentManager/promptSetting.vue"),
        },


      ],
    },
    {
      path: "/prompt/detail/:id",
      meta: {
        admin: true
      },
      name: "promptDetail",
      component: () => import("../views/prompt/detail.vue"),
    },

    {
      path: "/prompt/test",
      meta: {
        admin: true
      },
      name: "promptTest",
      component: () => import("../views/prompt/test.vue"),
    },
    {
      path: "/doce/detail/:id",
      meta: {
        admin: true
      },
      name: "doceDetail",
      component: () => import("../views/doce/detail.vue"),
    },
    {
      path: "/intell-aassist",
      component: () => import("../views/intellAassist/index.vue"),
      name: "intellAassist",
      meta: {
        theme_blue: true,
        admin: true
      },
      children: [
        {
          path: "",
          name: "prompt",
          component: () => import("../views/prompt/index.vue"),
        },
        {
          path: "/doce",
          name: "doce",
          component: () => import("../views/doce/index.vue"),
        },
      ]
    },

    {
      path: "/:catchAll(.*)",
      redirect: "/",
    },
  ],
});

router.beforeEach((to, from, next) => {
  // 判断是否登录
  if (setting.takeToken()) {
    //获取用户信息
    if (!InfoStore().user) {
      InfoStore()
        .updateUserInfo()
        .then(() => {
          verifyRoleRoute();
        });
    } else {
      verifyRoleRoute();
    }
  } else if (setting.whiteList.includes(to.path)) {
    next();
  } else {
    next({ name: "home", query: to.query });
  }

  function verifyRoleRoute() {
    // 管理员页面
    if (to.meta.admin && !InfoStore().getUserInfo.admin) {
      next({ name: "chat" });
      return;
    }
    if (InfoStore().getUserInfo.admin && InfoStore().isInit != 1 && to.name != "setting") {
      notification.warning({
        message: '温馨提示',
        description:
          '请先配置模型必要参数',
      });
      next({ name: "setting" });
      return;
    }
    if (to.path == "/") {
      next({ name: "chat" });
      return;
    }
    next();
  }
});
router.afterEach((to, form) => {

});

export default router;

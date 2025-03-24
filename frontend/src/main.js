import { createApp } from "vue";
import { createPinia } from "pinia";
import VueAxios from "vue-axios";
import axios from "./utils/request";
import App from "./App.vue";
import router from "./router";
import { has } from "./utils/directive";
import "ant-design-vue/es/message/style/css";
import "ant-design-vue/es/notification/style/css";
//基础样式
import "./assets/css/main.css";
import "highlight.js/styles/atom-one-dark.css"; //样式

const app = createApp(App);

app.directive("has", has);

app.use(createPinia());
app.use(router);
app.use(VueAxios, axios);
app.provide("axios", app.config.globalProperties.axios);

app.mount("#app");
import axios from 'axios'
import router from "../router/index"
import { message } from 'ant-design-vue'
import setting from './setting';
import { isMobile, isWeixin } from '../utils/utils';

// 创建 axios 实例
const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  withCredentials: true,
  timeout: 120000,
})

/* 请求拦截器 */
request.interceptors.request.use((config) => {
  // 添加token到header
  const token = setting.takeToken();
  const routeName = router.currentRoute?.value?.name;
  config.headers['content-language'] = 'zh_CN';
  //pc端且为临时会话页面  不加token
  if (routeName == 'temporary' && !isMobile()) {
    return config;
  }
  if (token) {
    config.headers[setting.tokenHeaderName] = token;
  }

  return config;
}, (error) => {
  return Promise.reject(error);
});

/* 响应拦截器 */
request.interceptors.response.use((res) => {

  const errCodeObj = {
    '401': '登录过期，请重新登录',
    '4002': '个人账号时间到期,请续费',
    '4003': '个人账号条数用尽,请续费',
    '4004': '企业账号时间到期,请续费',
    '4005': '企业账号时间到期,请续费',
  }
  //文件类型
  if (toString.call(res.data) === '[object Blob]') {
    return res;
  }
  // // 登录过期处理
  if (res.data.code === 401) {
    setting.cacheToken("", true);
    router.replace('/login');
    return Promise.reject(new Error(res.data.msg));
  }
  if (res.data.code == 40163) {
    return Promise.reject(res.data.code);
  }
  if (res.data.code != 200) {
    message.error(res.data.msg);
    return Promise.reject(res.data);
  }
  return res;
}, (error) => {
  console.log(error)
  if (!error || error.message != 'cancel') {
    message.error('网络不稳定,请稍后再试');

  }
  return Promise.reject(error);
});

export default request


/**
 * 跳转到登录页面
 */
function goLogin(reload) {
  store.dispatch('user/removeToken').then(() => {
    if (reload) {
      location.replace('/login');  // 这样跳转避免再次登录重复注册动态路由
    } else {
      const path = router.currentRoute.path;
      return router.push({
        path: '/login',
        query: path && path !== '/' ? { form: path } : null
      });
    }
  });
}


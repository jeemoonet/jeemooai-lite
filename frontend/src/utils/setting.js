export default {
  // 不需要登录的路由
  whiteList: [
    "/login",
    "/",
    "/404"
  ],
  headerHiddenList: [
    "home",
    "login",
    "promptTest",
    "promptDetail",
    "doceDetail",
    "",
  ],
  // token传递的header名称
  tokenHeaderName: "Authorization",
  // token存储的名称
  tokenStoreName: "access_token",
  // 用户信息存储的名称
  userStoreName: "user",
  //宣传页-未登录首页地址
  jeemooPublicizeUrl: 'https://www.jeemoo.com',
  jeemooHelpUrl: 'https://jeemoonet.feishu.cn/wiki/space/7303816650862985220?ccm_open_type=lark_wiki_spaceLink&open_tab_from=wiki_home',
  globalData: {
    title: "积木大脑",
    desc: "积木大脑",
  },
  /**
   * 语音转文字
   */
  tencentConfig: {
    secretKey: "",
    secretId: "",
    appId: "",
  },
  workflowNodeIconUrl: "",
  /**
   * 获取缓存的token的方法
   * @returns {string}
   */
  takeToken() {
    let token = localStorage.getItem(this.tokenStoreName);
    if (!token) {
      token = sessionStorage.getItem(this.tokenStoreName);
    }
    return token;
  },
  /**
   * 缓存token的方法
   * @param token
   * @param remember 是否永久存储
   */
  cacheToken(token, remember) {
    localStorage.removeItem(this.tokenStoreName);
    sessionStorage.removeItem(this.tokenStoreName);
    if (token) {
      if (remember) {
        localStorage.setItem(this.tokenStoreName, token);
        sessionStorage.setItem(this.tokenStoreName, token);
      } else {
        sessionStorage.setItem(this.tokenStoreName, token);
      }
    }
  },
  /**
   * 获取缓存的用户信息
   * @returns {object}
   */
  takeUser() {
    try {
      return JSON.parse(sessionStorage.getItem(this.userStoreName)) || null;
    } catch (e) {
      return null;
    }
    return {};
  },
  /**
   * 缓存用户信息
   * @param user
   */
  cacheUser(user) {
    if (user) {
      sessionStorage.setItem(this.userStoreName, JSON.stringify(user));
    } else {
      sessionStorage.removeItem(this.userStoreName);
    }
  },
  cacheOther(storeName, cacheData) {
    localStorage.setItem(storeName, JSON.stringify(cacheData));
  },
  takeOther(storeName) {
    try {
      return JSON.parse(localStorage.getItem(storeName)) || null;
    } catch (e) {
      return null;
    }
    return {};
  },
};

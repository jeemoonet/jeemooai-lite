import { defineStore } from 'pinia'
import axios from '../utils/request'
import setting from '../utils/setting'
import router from "../router/index"
export const InfoStore = defineStore('info', {
    state: () => {
        return {
            permissions: null,
            roles: null,
            user: null,
            isAlert: null,
            isInit: 0, //是否admin角色初始化
        }
    },
    getters: {

        getUserInfo: (state) => {
            return state.user || {};
        },
        getIsAlert: (state) => {
            return state.isAlert || false;
        },


    },
    actions: {
        //退出登录
        clearUserInfo() {
            this.user = null;
            this.currentPackageInfo = null;
            this.roles = null;
            this.permissions = null;
            this.isInit = 0;
            setting.cacheToken("", true);
            setting.cacheUser();
            router.replace('/login');
        },
        updatePackageInfo(res = {}) {
            let data = setting.takeUser();
            data.currentPackageInfo = res;
            setting.cacheUser(data);
            this.currentPackageInfo = res;
        },
        async updateUserInfo(isForceUpdate = false) {
            return new Promise(async (resolve, reject) => {
                if (setting.takeUser() && !isForceUpdate) {
                    let data = setting.takeUser();
                    this.user = data.user || null;
                    this.currentPackageInfo = data.currentPackageInfo || {};
                    this.roles = data.roles || null;
                    this.permissions = data.permissions || null;
                    this.isAlert = data.isAlert || false;
                    //新增初始化
                    this.isInit = data.isInit || 0;
                    resolve()
                    return;
                }
                try {
                    let res = await axios.get("/api/user/getInfo");
                    let data = res.data.data || {};
                    this.user = data.user || null;
                    this.currentPackageInfo = data.currentPackageInfo || {};
                    this.roles = data.roles || null;
                    this.permissions = data.permissions || null;
                    if (!isForceUpdate) {
                        this.isAlert = data.user.isAlert || false;
                    }
                    data.isAlert = true;
                    //新增初始化
                    this.isInit = data.isInit || 0;
                    setting.cacheUser(data);
                    resolve()
                } catch (err) {
                    resolve()
                }
            })

        },
        updateIsInit(res = 0) {
            let data = setting.takeUser();
            data.isInit = res;
            setting.cacheUser(data);
            this.isInit = res || 0;

        },
        updateIsAlert(res = false) {
            this.isAlert = res;
        },
    }

})
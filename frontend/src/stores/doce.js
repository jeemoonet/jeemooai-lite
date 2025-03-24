import { defineStore } from 'pinia'
import axios from '../utils/request'
import { message } from 'ant-design-vue'
export const DoceStore = defineStore('doce', {
    state: () => {
        return {
            doceList: [],
            mydoceList: [],
            doceId: null,
            doceTitle: [],
        }
    },
    getters: {
        //获取共享知识库
        doceMenuList: state => {
            return state.doceList || [];
        },
        //获取我的知识库
        mydoceMenuList: state => {
            return state.mydoceList || [];
        },
        doceMenuId: (state) => {
            return state.doceId || '';
        },
        doceSelectTitle: (state) => {
            return state.doceTitle || [];
        }

    },
    actions: {
        //请求提示器菜单数据
        async getDoceMenuList(res) {
            try {
                let res = await axios.get("/api/document/menu");
                let data = res.data.data || [];
                this.doceList = data || [];
                if (data.length != 0 && !this.doceId) {
                    this.doceTitle = [`${data[0]['name']}`];
                    this.doceId = data[0]['id'];
                }
            } catch (err) { }
        },
        async getMyDoceMenuList(res) {
            try {
                let res = await axios.get("/api/myDocumentCategory/menu");
                let data = res.data.data || [];
                this.mydoceList = data || [];

            } catch (err) { }
        },
        updateDoceMenuId(res) {
            this.doceId = res;
        },
        updateDoceSelectTitle(res) {
            this.doceTitle = res;
        }
    }

})
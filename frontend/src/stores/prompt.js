import { defineStore } from 'pinia'
import axios from '../utils/request'
import { message } from 'ant-design-vue'
export const PromptStore = defineStore('prompt', {
    state: () => {
        return {
            promptList: [],
            promptId: null,
            modelList: null,
        }
    },
    getters: {
        //获取提示器菜单
        promptMenuList: state => {
            return state.promptList || [];
        },
        promptMenuId: (state) => {
            return state.promptId || 0
        },
        getModelInitValue: (state) => {
            if (!state.modelList || state.modelList.length == 0) return null;
            return state.modelList[0]['modelLabel'];
        },
        getModelList: (state) => {
            if (!state.modelList) {
                PromptStore().updateModelList();
            }
            return state.modelList || [];
        },

    },
    actions: {
        //请求提示器菜单数据
        async getPromptMenuList(res) {
            try {
                let res = await axios.get("/api/prompt/menu");
                let data = res.data.data || [];
                this.promptList = data || [];
            } catch (err) { }
        },
        async updateModelList() {
            try {
                let res = await axios.get("/api/home/models");

                let data = res.data.data || [];
                data = data.map((item) => {
                    item.label = item.platformName;
                    item.options = item.models?.map((itm) => {
                        itm.label = itm.modelName;
                        itm.value = itm.modelLabel;
                        return itm;
                    });
                    return item;
                });
                this.modelList = data || [];
            } catch (err) { }
        },
        updatePromptMenuId(res) {
            this.promptId = res;
        }
    }

})
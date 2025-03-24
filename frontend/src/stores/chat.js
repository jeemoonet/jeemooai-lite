import { defineStore } from 'pinia'
import axios from '../utils/request'
import { message } from 'ant-design-vue'
import setting from '../utils/setting';
export const ChatStore = defineStore('chat', {
    state: () => {
        return {
            chatMenuList: [],
            menuItemId: 0,
            isNetWork: 0,
            isReasoning: 0,
        }
    },
    getters: {
        getChatMenuList: (state) => {
            if (state.chatMenuList.length == 0) {
                return [{
                    windowName: '默认对话',
                    windowId: 0,
                }];
            }
            return state.chatMenuList
        },
        getChatMenuId: (state) => {
            return state.menuItemId || 0
        },

        //获取当前窗口
        getCurrentWindow: state => {
            return state.chatMenuList.find(item => item.windowId == state.menuItemId) || {};
        },

    },
    actions: {
        initSendOptions() {
            this.isNetWork = setting.takeOther('isNetWork') || 0;
            this.isReasoning = setting.takeOther('isReasoning') || 0;
        },
        //修改是否需要联网状态
        updateIsNetWork() {
            let isNetWork = this.isNetWork == 0 ? 1 : 0;
            this.isNetWork = isNetWork;
            setting.cacheOther('isNetWork', isNetWork);
        },
        //修改是否需要深度思考
        updateIsReasoning() {
            let isReasoning = this.isReasoning == 0 ? 1 : 0;
            this.isReasoning = isReasoning;
            setting.cacheOther('isReasoning', isReasoning);
        },
        // 添加默认窗口
        addDefaultChatMenu() {
            let chatMenu = this.chatMenuList || [];
            let defaultMenuItemIndex = chatMenu.findIndex(item => item.windowId == 0);
            //没有默认窗口
            if (defaultMenuItemIndex == -1) {
                this.chatMenuList = [{
                    windowName: '默认对话',
                    windowId: 0,
                    isContext: 1,
                }, ...chatMenu]

            }
            this.setChatMenuId(0)

        },
        // 更新默认窗口
        updateChatMenu(res = {}) {
            if (!res.windowId) return;
            this.menuItemId = res.windowId || 0;
            let list = this.chatMenuList.map(item => {
                if (!item.windowId) {
                    item = Object.assign(item, res)
                }
                return item;
            })
            this.chatMenuList = list || [];
        },
        //新增窗口
        addChatMenu(res = {}) {
            if (!res.windowId) return;
            this.menuItemId = res.windowId || 0;
            let defaultMenUItemIndex = this.chatMenuList.findIndex(item => !item.windowId);
            if (defaultMenUItemIndex != -1) {
                this.chatMenuList[defaultMenUItemIndex] = res;
                return;
            }
            this.chatMenuList.push(res);
        },
        setChatMenuId(res) {
            this.menuItemId = res;
        },
        // 更新窗口名
        updateChatMenuItemName(res) {
            return new Promise(async (resolve, reject) => {
                try {
                    await axios.post("/api/window/rename", {
                        windowName: res.windowName,
                        windowId: res.windowId
                    });
                    let itemIndex = this.chatMenuList.findIndex(item => item.windowId == res.windowId);
                    this.chatMenuList[itemIndex]['windowName'] = res.windowName;
                    resolve();
                } catch (err) {
                    reject();
                }
            })
        },
        // 删除窗口
        async deleteChatMenu(res) {
            if (this.chatMenuList.length <= 1) {
                message.warning('已经是最后一个对话窗口');
                return;
            }
            try {
                await axios.delete("/api/window", {
                    params: {
                        windowId: res.windowId
                    }
                });
                this.chatMenuList.splice(this.chatMenuList.findIndex(item => item.windowId == res.windowId), 1);
                if (res.windowId == this.menuItemId) {
                    this.menuItemId = this.chatMenuList[0].windowId;
                }
                message.success('删除成功');
            } catch (err) {
            }
        },
        // 获取窗口列表
        async setChatMenuList() {
            return new Promise(async (resolve, reject) => {
                try {
                    let res = await axios.get("/api/window/list");
                    let data = res.data.data || [];
                    this.chatMenuList = [{
                        windowName: '默认对话',
                        windowId: 0,
                        isContext: 1,
                    }, ...data]
                    resolve();
                } catch (err) {
                    reject();
                }
            })

        },
    }

})
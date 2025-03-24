
import { InfoStore } from '../stores/user';
/**权限指令**/
const has = (el, binding, vnode) => {
    // 获取页面按钮权限
    let btnPermissionsArr = [];

    if (!_has(binding.value)) {
        // el.parentNode.removeChild(el);
        el.style.display = 'none';
    } else {

        el.style.display = '';
    }
}

// 权限检查方法
const _has = function (value, permission = 'view') {
    let isExist = false;
    // 获取用户按钮权限
    let btnPermissionsStr = InfoStore().permissions || [];
    if (!btnPermissionsStr || btnPermissionsStr.length == 0) {
        return false;
    }
    if (btnPermissionsStr.indexOf(value) > -1) {
        isExist = true;
    }
    return isExist;
};

export {
    has
}
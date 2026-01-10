import store from "@/store";
export default {
    inserted(el, binding, vnode) {
        if(!el) return 
        const { value } = binding;
        const permissions = store.getters && store.getters.userInfo.id;

        if (value) {
            const hasPermissions = permissions == value
            if (!hasPermissions) {
                el.parentNode && el.parentNode.removeChild(el);
            }
        } else {
            throw new Error(`请设置操作权限标签值`);
        }
    }
};

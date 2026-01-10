import store from "@/store";
export default {
    inserted(el, binding, vnode) {
        if(!el) retrun
        const { value } = binding;
        const permissions = store.getters && store.getters.roleIds;
        console.log('=================permissions', permissions, value)

        if (value && value instanceof Array && value.length > 0) {
            const hasPermissions = value.some(item => {
                return permissions.includes(item)
            })
            if (!hasPermissions) {
                el.parentNode && el.parentNode.removeChild(el);
            }
        } else {
            throw new Error(`请设置操作权限标签值`);
        }
    }
};

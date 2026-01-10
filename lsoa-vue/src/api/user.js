import request from '@/utils/request';

export function getcode(data) {
    return request({
        url: 'api/verifyCode/getcode',
        method: 'post',
        data,
    });
}
export function login(data) {
    return request({
        url: 'api/login',
        method: 'post',
        data,
    });
}

export function logout() {
    return request({
        url: '/api/logout',
        method: 'post',
    });
}
export function editPassword(data) {
    return request({
        url: '/api/change/password',
        method: 'post',
        data,
    });
}

export function getInfo() {
    return request({
        url: '/api/user',
        method: 'get',
    });
    // return {
    //     status: 200,
    //     data: {
    //         name: 'admin',
    //         roles: ['admin'],
    //         avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif?imageView2/1/w/80/h/80',
    //         introduction: '个人简介信息测试',
    //     },
    // };
}

// 用户列表
export function userPageList(data) {
    return request({
        url: '/api/sysuser/page',
        method: 'post',
        data,
    });
}

// 修改密码
export function userPassword(data) {
    return request({
        url: '/api/sysuser/reset/password',
        method: 'post',
        data,
    });
}

// 角色列表 /api/sysrole/list
export function roleList(data) {
    return request({
        url: '/api/sysrole/list',
        method: 'post',
        data,
    });
}

// 删除用户
export function userDelete(data) {
    return request({
        url: '/api/sysuser/delete',
        method: 'post',
        data,
    });
}
// 用户下发
export function userSyncSystem(data) {
    return request({
        url: '/api/sysuser/sync/system',
        method: 'post',
        data,
    });
}
// 角色下发
export function roleSyncSystem(data) {
    return request({
        url: '/api/sysrole/sync/system',
        method: 'post',
        data,
    });
}
//岗位下发
export function postSyncSystem(data) {
    return request({
        url: '/api/sysPost/sync/system',
        method: 'post',
        data,
    });
}
//部门下发
export function deptSyncSystem(data) {
    return request({
        url: '/api/sysDept/sync/system',
        method: 'post',
        data,
    });
}
// 新增修改用户
export function userSaveUpdate(data) {
    return request({
        url: '/api/sysuser/save',
        method: 'post',
        data,
    });
}
// 部门列表
export function getSysDeptList(params) {
    return request({
        url: '/api/sysDept/getSysDeptList',
        method: 'post',
        data: params,
    });
}
// 岗位列表 /api/sysPost/getSysPostList
export function getSysPostList(params) {
    return request({
        url: '/api/sysPost/getSysPostList',
        method: 'post',
        data: params,
    });
}
// 菜单列表 /api/sysPost/getSysPostList
export function getMenuList(params) {
    return request({
        url: '/api/sysMenu/getMenuList',
        method: 'get',
        params: params,
    });
}

// 部门分页查询 /api/sysDept/getPage
export function getSysDeptPage(params) {
    return request({
        url: '/api/sysDept/getPage',
        method: 'post',
        data: params,
    });
}
// 部门修改 新增 /api/sysDept/saveOrUpdate
export function saveOrUpdate(data) {
    return request({
        url: '/api/sysDept/saveOrUpdate',
        method: 'post',
        data,
    });
}
// 部门删除 /api/sysDept/del
export function deptDel(params) {
    return request({
        url: '/api/sysDept/del',
        method: 'post',
        data:params,
    });
}

// 角色分页 /api/sysrole/page
export function rolePage(data) {
    return request({
        url: '/api/sysrole/page',
        method: 'post',
        data,
    });
}
// 角色新增修改 /api/sysrole/save
export function roleSave(data) {
    return request({
        url: '/api/sysrole/save',
        method: 'post',
        data,
    });
}
// 角色删除 /api/sysrole/delete
export function roleDelete(data) {
    return request({
        url: '/api/sysrole/delete',
        method: 'post',
        data,
    });
}
// 子系统列表 /api/syssystem/list
export function sysSystemList(data) {
    return request({
        url: '/api/syssystem/list',
        method: 'post',
        data,
    });
}
// 日志列表 /api/syslog/page
export function logPage(data) {
    return request({
        url: '/api/syslog/page',
        method: 'post',
        data,
    });
}

// 菜单 树形菜单
export function getSysMenuTree(params) {
    return request({
        url: '/api/sysMenu/getSysMenuTree',
        method: 'get',
        params: params,
    });
}

// 岗位分页 /api/sysPost/getPage
export function getSysPostPage(params) {
    return request({
        url: '/api/sysPost/getPage',
        method: 'get',
        params: params,
    });
}

// 岗位 修改 /api/sysPost/saveOrUpdate
export function postSaveOrUpdate(data) {
    return request({
        url: '/api/sysPost/saveOrUpdate',
        method: 'post',
        data,
    });
}
// 岗位 删除 /api/sysPost/del
export function postDel(params) {
    return request({
        url: '/api/sysPost/del',
        method: 'get',
        params: params,
    });
}
// 角色赋权
export function saveRoleMenu(data) {
    return request({
        url: '/api/sysrole/saveRoleMenu',
        method: 'post',
        data,
    });
}

//查询角色权限 /api/sysrole/getSysRoleMenuList
export function getSysRoleMenuList(params) {
    return request({
        url: '/api/sysrole/getSysRoleMenuList',
        method: 'get',
        params: params,
    });
}

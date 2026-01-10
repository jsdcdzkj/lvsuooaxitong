import request from '@/utils/request';

// 分页查询
export function getPage(params) {
    return request({
        url: '/api/sysMenu/getPage',
        method: 'get',
        params: params,
    });
}

// 下拉树结构
export function getSysMenuTree(params) {
    return request({
        url: '/api/sysMenu/getSysMenuTree',
        method: 'get',
        params: params,
    });
}

//新增或修改
export function saveOrUpdate(data) {
    return request({
        url: '/api/sysMenu/saveOrUpdate',
        method: 'post',
        data,
    });
}

// 删除
export function menuDel(data) {
    return request({
        url: '/api/sysMenu/del',
        method: 'get',
        params: data,
    });
}

// 列表查询
export function getSysMenuList(data) {
    return request({
        url: '/api/sysMenu/getSysMenuList',
        method: 'get',
        params: data,
    });
}

// 列表查询
export function getSysMenu(data) {
    return request({
        url: '/api/sysMenu/getSysMenu',
        method: 'get',
        params: data,
    });
}

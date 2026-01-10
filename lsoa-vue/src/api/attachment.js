import request from '@/utils/request';
// 地址设置和空间配置
export function list(data) {
    return request({
        url: '/api/sys/config/dict/list',
        method: 'get',
        parmas:data,
    });
}
// 修改地址设置和空间配置
export function save(data) {
    return request({
        url: '/api/sys/config/dict/save',
        method: 'post',
        data,
    });
}
// 当前附件类型
export function fileType(data) {
    return request({
        url: '/api/sys/config/fileType',
        method: 'get',
        params:data,
    });
}
// 新增附件类型
export function add(data) {
    return request({
        url: '/api/sys/config/fileType/add',
        method: 'post',
        headers: {
            'Content-Type': 'application/json',
        },
        data,
    });
}
// 删除附件类型
export function del(data) {
    return request({
        url: '/api/sys/config/fileType/del',
        method: 'post',
        headers: {
            'Content-Type': 'application/json',
        },
        data,
    });
}

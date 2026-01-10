import request from '@/utils/request';
// 根据id查询
export function entity(data) {
    return request({
        url: '/api/process/design/entity',
        method: 'get',
        parmas:data,
    });
}

// 流程列表查询
export function list(data) {
    return request({
        url: '/api/process/design/list',
        method: 'post',
        data,
    });
}
// 新增或编辑
export function save(data) {
    return request({
        url: '/api/process/design/save',
        method: 'post',
        data,
    });
}

// 流程设计
// 根据流程节点删除
export function nodeDel(data) {
    return request({
        url: '/api/process/node/del',
        method: 'post',
        data,
    });
}

// 根据流程节点列表
export function nodeList(data) {
    return request({
        url: '/api/process/node/list',
        method: 'get',
        params:data,
    });
}

// 根据流程节点保存
export function nodeSave(data) {
    return request({
        url: '/api/process/node/save',
        method: 'post',
        data,
    });
}
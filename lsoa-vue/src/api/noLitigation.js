import request from '@/utils/request';
// 分页查询
export function page(data) {
    return request({
        url: '/api/non/litigation/business/page',
        method: 'post',
        data,
    });
}
// 删除
export function delById(data) {
    return request({
        url: '/api/non/litigation/business/delById',
        method: 'post',
        data,
    });
}
// 保存
export function save(data) {
    return request({
        url: '/api/non/litigation/business/save',
        method: 'post',
        data,
    });
}

// 提交
export function submit(data) {
    return request({
        url: '/api/non/litigation/business/submit',
        method: 'post',
        data,
    });
}

// 获取草稿
export function getDraft(data) {
    return request({
        url: '/api/non/litigation/business/getDraft',
        method: 'post',
        data,
    });
}

// 审批
export function check(data) {
    return request({
        url: '/api/non/litigation/business/check',
        method: 'post',
        data,
    });
}
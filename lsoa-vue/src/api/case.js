import request from '@/utils/request';
// 分页查询
export function getPageList(data) {
    return request({
        url: '/api/caseInfo/getPageList',
        method: 'post',
        data,
    });
}
// 新增案件
export function saveCaseInfo(data) {
    return request({
        url: '/api/caseInfo/saveCaseInfo',
        method: 'post',
        data,
    });
}

// 编辑保存
export function editCase(data) {
    return request({
        url: '/api/caseInfo/editCase',
        method: 'post',
        data,
    });
}

// 删除案件
export function del(data) {
    return request({
        url: '/api/caseInfo/del',
        method: 'post',
        data,
    });
}
// 详情
export function getEntity(data) {
    return request({
        url: '/api/caseInfo/getEntity',
        method: 'post',
        data,
    });
}
// 编辑案件
export function updateCaseInfo(data) {
    return request({
        url: '/api/caseInfo/updateCaseInfo',
        method: 'post',
        data,
    });
}

// 编辑当事人
export function updateClientInfo(data) {
    return request({
        url: '/api/caseInfo/updateClientInfo',
        method: 'post',
        data,
    });
}

// 编辑案件
export function updateCaseFile(data) {
    return request({
        url: '/api/caseInfo/updateCaseFile',
        method: 'post',
        data,
    });
}

// 是否有开函开票权限
export function toInvoice(data) {
    return request({
        url: '/api/invoiceInfo/toInvoice',
        method: 'get',
        params: data,
    });
}

// 查询协约用户
export function getCustomList(data) {
    return request({
        url: '/api/agreement/customer/list',
        method: 'get',
        params: data,
    });
}

// 行政归档
export function archive(data) {
    return request({
        url: '/api/caseInfo/archive',
        method: 'post',
        data,
    });
}

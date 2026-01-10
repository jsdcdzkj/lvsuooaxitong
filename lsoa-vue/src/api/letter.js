import request from '@/utils/request';
// 审核
export function approveInvoiceInfo(data) {
    return request({
        url: '/api/summonsInfo/approveInvoiceInfo',
        method: 'post',
        data,
    });
}
// 获得详情
export function getEntity(data) {
    return request({
        url: '/api/summonsInfo/getEntity',
        method: 'post',
        data,
    });
}
// 查询列表
export function getPageList(data) {
    return request({
        url: '/api/summonsInfo/getPageList',
        method: 'post',
        data,
    });
}
// 开函
export function saveOrUpdate(data) {
    return request({
        url: '/api/summonsInfo/saveOrUpdate',
        method: 'post',
        data,
    });
}
// 查询委托人
export function getClient(data) {
    return request({
        url: '/api/invoiceInfo/getClient',
        method: 'post',
        data,
    });
}

// 多次开函
export function getLetterList(data) {
    return request({
        url: '/api/summonsInfo/getList',
        method: 'post',
        data,
    });
}

// 开函导出
export function toExportTemplate(data) {
    return request({
        url: '/api/summonsInfo/toExportTemplate',
        method: "get",
        params: data,
        responseType: "blob"
    });
}
import request from '@/utils/request';
// 审核
export function approveInvoiceInfo(data) {
    return request({
        url: '/api/invoiceInfo/approveInvoiceInfo',
        method: 'post',
        data,
    });
}
// 获得详情
export function getEntity(data) {
    return request({
        url: '/api/invoiceInfo/getEntity',
        method: 'post',
        data,
    });
}
// 查询列表
export function getPageList(data) {
    return request({
        url: '/api/invoiceInfo/getPageList',
        method: 'post',
        data,
    });
}
// 开票
export function saveOrUpdate(data) {
    return request({
        url: '/api/invoiceInfo/saveOrUpdate',
        method: 'post',
        data,
    });
}

// 导出
export function toExportTemplate(data) {
    return request({
        url: "/api/invoiceInfo/toExportTemplate",
        method: "get",
        params: data,
        responseType: "blob"
    });
}

// 获取多次开票的列表
export function getInvoiceList(data) {
    return request({
        url: '/api/invoiceInfo/getList',
        method: 'post',
        data,
    });
}

// 修改支付状态
export function payType(data) {
    return request({
        url: '/api/invoiceInfo/payType',
        method: 'post',
        data,
    });
}


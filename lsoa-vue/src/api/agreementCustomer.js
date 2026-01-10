import request from '@/utils/request';
// 分页查询
export function page(data) {
    return request({
        url: '/api/agreement/customer/page',
        method: 'post',
        data,
    });
}
// 删除
export function delById(data) {
    return request({
        url: '/api/agreement/customer/delById',
        method: 'post',
        data,
    });
}
// 新增编辑
export function save(data) {
    return request({
        url: '/api/agreement/customer/save',
        method: 'post',
        data,
    });
}

// 查询详情
export function detail(data) {
    return request({
        url: '/api/agreement/customer/detail',
        method: 'post',
        data,
    });
}


// 开票
export function saveOrUpdateInvoiceInfo(data) {
    return request({
        url: '/api/agreement/customer/saveOrUpdateInvoiceInfo',
        method: 'post',
        data,
    });
}

//  开票详情
export function invoiceDetail(data) {
    return request({
        url: '/api/agreement/customer/invoiceInfo/detail',
        method: 'post',
        data,
    });
}


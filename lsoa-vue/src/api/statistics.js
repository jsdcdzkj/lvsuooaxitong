import request from '@/utils/request';
// 获取律师列表
export function getNormalUserList(data) {
    return request({
        url: '/api/statistical/getNormalUserList',
        method: 'post',
        data,
    });
}
// 案件金额排名
export function caseTypeMoneyRanking(data) {
    return request({
        url: '/api/statistical/CaseTypeMoneyRanking',
        method: 'post',
        data,
    });
}
// 案件数量排名
export function caseTypeRanking(data) {
    return request({
        url: '/api/statistical/CaseTypeRanking',
        method: 'post',
        data,
    });
}
// 律所案件柱状图
export function getStatisticalVoWithZero(data) {
    return request({
        url: '/api/statistical/getStatisticalVoWithZero',
        method: 'post',
        data,
    });
}
// 律所案件柱状图金额
export function getStatisticalVoWithZeroMoney(data) {
    return request({
        url: '/api/statistical/getStatisticalVoWithZeroMoney',
        method: 'post',
        data,
    });
}
// 律所各类案件统计
export function lawFirmCaseStatisticsByType(data) {
    return request({
        url: '/api/statistical/lawFirmCaseStatisticsByType',
        method: 'post',
        data,
    });
}
// 律所各类案件统计金额版
export function lawFirmCaseStatisticsByTypeMoney(data) {
    return request({
        url: '/api/statistical/lawFirmCaseStatisticsByTypeMoney',
        method: 'post',
        data,
    });
}

// 导出报表
export function exportData(data) {
    return request({
        url: "/api/statistical/export",
        method: "get",
        params:data,
        responseType: "blob",
        headers: {
            "Content-Type": "application/x-download"
        },
    });
}

// 导出金额报表
export function exportMoney(data) {
    return request({
        url: "/api/statistical/exportMoney",
        method: "get",
        params: data,
        responseType: "blob",
        headers: {
            "Content-Type": "application/x-download"
        },
    });
}
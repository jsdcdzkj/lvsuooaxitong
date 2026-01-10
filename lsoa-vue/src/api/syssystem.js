import request from '@/utils/request';

export function syssystemPage(data) {
    return request({
        url: '/api/syssystem/page',
        method: 'post',
        data,
    });
}

export function syssystemSaveUpdate(data) {
    return request({
        url: '/api/syssystem/save',
        method: 'post',
        data,
    });
}

export function syssystemList() {
    return request({
        url: '/api/syssystem/list',
        method: 'post',
    });
}

export function syssystemDelete(data) {
    return request({
        url: '/api/sysuser/delete',
        method: 'post',
        data,
    });
}
// 菜单下发
export function syssystemSyncSystem(data) {
    return request({
        url: '/api/sysMenu/sync/system',
        method: 'post',
        data,
    });
}

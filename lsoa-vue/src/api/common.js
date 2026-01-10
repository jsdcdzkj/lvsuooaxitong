import request from '@/utils/request';
import axios from "axios";
import store from '@/store';

export function getYuanQuList(data) {
    return request({
        url: '/api/school/getAll',
        method: 'post',
        data,
    });
}
export function getRenList(data) {
    return request({
        url: '/api/admin/list',
        method: 'post',
        data,
    });
}

export function login(data) {
    return request({
        url: '/api/login',
        method: 'post',
        data,
    });
}

// 字典查询
export function getRedisDictList(data) {
    return request({
        url: 'api/sysDict/getRedisDictList',
        method: 'post',
        data,
    });
}

// 文件类型
export function getCaseFileType(data) {
    return request({
        url: 'api/sysDict/getCaseFileType',
        method: 'post',
        data,
    });
}

// 顾问查询接口
export function byRole(data) {
    return request({
        url: 'api/agreement/customer/list/byRole',
        method: 'get',
        params:data,
    });
}


//头像上传
let urlUpload = process.env.VUE_APP_BASE_API + "/api/sysuser/avatar";

export async function importData(form) {
    console.log("222222222222221111111111112", form);
    var result = {};
    if (null != form.get("file") && "" != form.get("file") && undefined != form.get("file")) {
        await axios
            .post(urlUpload, form, {
                headers: {
                    "Content-Type": "multipart/form-data",
                    accessToken: store.getters.token
                }
            })
            .then((res) => {
                console.log("5555555555555", res);
                result = res;
            });
    }
    return result;
}

// 文件上传
export function upload(data) {
    return request({
        url: `/api/sys/file/upload`,
        method: 'post',
        data,
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });
}

// 文件删除
export function fileDelete(data) {
    return request({
        url: '/api/sys/file/delete',
        method: 'post',
        data,
    });
}

//下载
export function download(data) {
    return request({
      url: '/api/sys/file/download',
      method: 'get',
      params:data,
      responseType: 'blob'
    })
  }
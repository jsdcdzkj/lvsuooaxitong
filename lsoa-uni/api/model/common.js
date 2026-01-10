import {
	request
} from '@/utils/request.js'
import config from '@/config/index.js'

export default {
	// 上传头像
	avatar: function(data, callback) {
		return request({
			url: '/sysuser/avatar',
			method: 'POST',
			data
		}, callback)
	},
	// 案件文件类型
	getCaseFileType: function(data, callback) {
		return request({
			url: '/sysDict/getCaseFileType',
			method: 'POST',
			data
		}, callback)
	},
	// 顾问单位角色
	byRole: function(data, callback) {
		return request({
			url: '/agreement/customer/list/byRole',
			method: 'GET',
			data
		}, callback)
	},
	getRedisDictList: function(data, callback) {
		return request({
			url: '/sysDict/getRedisDictList',
			method: 'POST',
			data
		}, callback)
	},
	getNormalUserList: function(data, callback) {
		return request({
			url: '/caseInfo/getNormalUserList',
			method: 'POST',
			data
		}, callback)
	},
	// 协约客户
	geCustomerList: function(data, callback) {
		return request({
			url: '/agreement/customer/list',
			method: 'GET',
			data
		}, callback)
	},
	// 可以上传的文件类型
	fileTypeList: function(data, callback) {
		return request({
			url: '/sys/config/dict/list',
			method: 'get',
			data
		}, callback)
	},
	// 文件删除
	del: function(data, callback) {
		return request({
			url: '/sys/file/delete',
			method: 'POST',
			data
		}, callback)
	},
	// 文件上传
	onUploadFile: (filePath, extParams) => {
		return new Promise((resolve, reject) => {
			uni.uploadFile({
				header: {
					accessToken: uni.getStorageSync('token')
				},
				filePath,
				formData: {
					...extParams
				},
				name: 'file',
				url: `${config.VUE_APP_API_BASEURL}/sys/file/upload`,
				success: (result) => {
					if (result.statusCode == 200) {
						resolve(JSON.parse(result.data))
					} else {
						resolve(result.errMsg)
					}
				},
				fail: (error) => {
					reject(error)
				}
			})
		})
	},
	uploadAvatar: (filePath) => {
		return new Promise((resolve, reject) => {
			uni.uploadFile({
				header: {
					accessToken: uni.getStorageSync('token')
				},
				filePath,
				name: 'file',
				url: `${config.VUE_APP_API_BASEURL}/sysuser/avatar`,
				success: (result) => {
					if (result.statusCode == 200) {
						resolve(JSON.parse(result.data))
					} else {
						resolve(result.errMsg)
					}
				},
				fail: (error) => {
					reject(error)
				}
			})
		})
	},
	// 文件下载
	downloadFile(url) {
		console.log('22222222222222url', url)
		uni.downloadFile({
			url, //下载地址接口返回
			success: (data) => {
				console.log('33333333333333data', data)
				if (data.statusCode === 200) {
					//文件保存到本地
					uni.saveFile({
						tempFilePath: data.tempFilePath, //临时路径
						success: function(res) {
							uni.showToast({
								icon: 'none',
								mask: true,
								title: '文件已保存：' + res.savedFilePath, //保存路径
								duration: 3000,
							});
						}
					});
				} else {
					uni.showToast({
						icon: 'none',
						mask: true,
						title: '文件下载失败',
					});
				}
			},
			fail: (err) => {
				console.log(err);
				uni.showToast({
					icon: 'none',
					mask: true,
					title: '失败请重新下载',
				});
			},
		})
	},
	// 文件预览
	previewFile(url) {
		uni.downloadFile({
			url, //下载地址接口返回
			success: (data) => {
				console.log('33333333333333data', data)
				if (data.statusCode === 200) {
					uni.openDocument({
						filePath: data.tempFilePath,
						success: function(res) {
							// console.log('打开文档成功');
						}
					});
				} else {
					uni.showToast({
						icon: 'none',
						mask: true,
						title: '文档打开失败', //保存路径
						duration: 3000,
					});
				}
			},
			fail: (err) => {
				console.log(err);
				uni.showToast({
					icon: 'none',
					mask: true,
					title: '文件预览失败，请下载后查看',
				});
			},
		})
	}
}
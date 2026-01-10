import {
	request
} from '@/utils/request.js'

export default {
	onLogin: function(data, callback) {
		return request({
			url: '/login',
			method: 'POST',
			data
		}, callback)
	},
	logout: function(data, callback) {
		return request({
			url: '/logout',
			method: 'POST',
			data
		}, callback)
	},
	getUser: function(data, callback) {
		return request({
			url: '/user',
			method: 'POST',
			data
		}, callback)
	},
	updUser: function(data, callback) {
		return request({
			url: '/user/updatePass',
			method: 'POST',
			data
		}, callback)
	},
	// 更新用户信息
	saveUser: function(data, callback) {
		return request({
			url: '/sysuser/save',
			method: 'POST',
			data
		}, callback)
	},
	// 修改密码
	editPassword: function(data, callback) {
		return request({
			url: '/sysuser/password',
			method: 'POST',
			data
		}, callback)
	},
}
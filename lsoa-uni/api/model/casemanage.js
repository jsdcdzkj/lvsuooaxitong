import {
	request
} from '@/utils/request.js'

export default {
	// 删除案件
	delCase: function(data, callback) {
		return request({
			url: '/caseInfo/del',
			method: 'POST',
			data
		}, callback)
	},
	// 编辑案件
	editCase: function(data, callback) {
		return request({
			url: '/caseInfo/editCase',
			method: 'POST',
			data
		}, callback)
	},
	//查看详情
	getEntity: function(data, callback) {
		return request({
			url: '/caseInfo/getEntity',
			method: 'POST',
			data
		}, callback)
	},
	//归档 
	archive: function(data, callback) {
		return request({
			url: '/api/caseInfo/archive',
			method: 'POST',
			data
		}, callback)
	},
	//分页查询
	getPageList: function(data, callback) {
		return request({
			url: '/caseInfo/getPageList',
			method: 'POST',
			data
		}, callback)
	},
	// 添加案件
	saveCaseInfo: function(data, callback) {
		return request({
			url: '/caseInfo/saveCaseInfo',
			method: 'POST',
			data
		}, callback)
	},
	// 待办接口
	toDo: function(data, callback) {
		return request({
			url: '/caseInfo/toDo',
			method: 'POST',
			data
		}, callback)
	},
	// 已办接口
	finisList: function(data, callback) {
		return request({
			url: '/caseInfo/finish',
			method: 'POST',
			data
		}, callback)
	},
}
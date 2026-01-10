import {
	request
} from '@/utils/request.js'

export default {
	// 案件审批
	approveInvoiceInfo: function(data, callback) {
		return request({
			url: '/summonsInfo/approveInvoiceInfo',
			method: 'POST',
			data
		}, callback)
	},
	// 案件详情
	getEntity: function(data, callback) {
		return request({
			url: '/summonsInfo/getEntity',
			method: 'POST',
			data
		}, callback)
	},
	//案件查询
	getPageList: function(data, callback) {
		return request({
			url: '/summonsInfo/getPageList',
			method: 'POST',
			data
		}, callback)
	},
	//分页查询
	saveOrUpdate: function(data, callback) {
		return request({
			url: '/summonsInfo/saveOrUpdate',
			method: 'POST',
			data
		}, callback)
	},
	// 查看委托人
	getClient: function(data, callback) {
		return request({
			url: '/invoiceInfo/getClient',
			method: 'POST',
			data
		}, callback)
	},
	// 查看开函列表
	getLetterList: function(data, callback) {
		return request({
			url: '/summonsInfo/getList',
			method: 'POST',
			data
		}, callback)
	},
}
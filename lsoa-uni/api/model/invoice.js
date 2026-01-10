import {
	request
} from '@/utils/request.js'

export default {
	// 案件审批
	approveInvoiceInfo: function(data, callback) {
		return request({
			url: '/invoiceInfo/approveInvoiceInfo',
			method: 'POST',
			data
		}, callback)
	},
	// 案件详情
	getEntity: function(data, callback) {
		return request({
			url: '/invoiceInfo/getEntity',
			method: 'POST',
			data
		}, callback)
	},
	//案件查询
	getPageList: function(data, callback) {
		return request({
			url: '/invoiceInfo/getPageList',
			method: 'POST',
			data
		}, callback)
	},
	//分页查询
	saveOrUpdate: function(data, callback) {
		return request({
			url: '/invoiceInfo/saveOrUpdate',
			method: 'POST',
			data
		}, callback)
	},
	// 查看是否有开票权限
	toInvoice: function(data, callback) {
		return request({
			url: '/invoiceInfo/toInvoice',
			method: 'POST',
			data
		}, callback)
	},
	// 查看开票列表
	getInvoiceList: function(data, callback) {
		return request({
			url: '/invoiceInfo/getList',
			method: 'POST',
			data
		}, callback)
	},
}
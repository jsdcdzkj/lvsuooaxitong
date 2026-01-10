import { request } from "../../utils/request"

const URL = {
	stayPageList:"/caseprocess/stayPageList",
	caseprocessDetail: "/caseprocess/details",
	getCourtClerk: "/user/getCourtClerk",
	schedulingUpd: "/caseprocess/schedulingUpd",
	cancelScheUpd: "/caseprocess/cancelScheUpd",
	mediationAgent: "/caseprocess/mediationAgent",
	mediatorScheduledPageList: "/caseprocess/completedPageList",
	stagingPageList: "/caseprocess/stagingPageList",
	judgeArbTime: '/caseprocess/judgeArbTime',
}

export const judgeArbTime = (data) => request({
	url: URL.judgeArbTime,
	method: 'POST',
	data
})

export const stayPageList = (data) => request({
	url: URL.stayPageList,
	method: 'GET',
	data
})

export const caseprocessDetail = (data) => request({
	url: URL.caseprocessDetail,
	method: 'POST',
	data
})


export const getCourtClerk = (data) => request({
	url: URL.getCourtClerk,
	method: 'GET',
	data
})

export const schedulingUpd = (data) => request({
	url: URL.schedulingUpd,
	method: 'POST',
	data
})

export const cancelScheUpd = (data) => request({
	url: URL.cancelScheUpd,
	method: 'POST',
	data
})

export const mediationAgent = (data) => request({
	url: URL.mediationAgent,
	method: 'POST',
	data
})

export const mediatorScheduledPageList = (data) => request({
	url: URL.mediatorScheduledPageList,
	method: 'POST',
	data
})

export const stagingPageList = (data) => request({
	url: URL.stagingPageList,
	method: 'POST',
	data
})

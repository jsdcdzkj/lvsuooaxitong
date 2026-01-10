import {reactive} from "vue"
import {onReachBottom} from "@dcloudio/uni-app"

export const sleep = (t) => {
	return new Promise((resolve) => {
		setTimeout(resolve,t)
	})
}


export function testRequest(){
	return new Promise(async (resolve) => {
			await sleep(1000)
			resolve({
				code:200,
				has_next:true,
				records:[1,2,3,4,5,6,7,8,9,10],
				msg:''
			})
	})
}


export const usePagnation = (apiReq, extra = {}, isReachBottom = true) => {
	const pagnation = reactive({
		page:{
			pageNo:1,
			pageSize:10
		},
		status:'more',
		list:[],
		listStyle: {
			backgroundColor: '#f7f7f7',
			borderRadius: '10rpx',
			padding: '0',
			boxSizing: 'border-box'
		},
	})
	
	const extraParams = reactive(extra)
	
	const list = async () => {
		pagnation.status = 'loading'
		const params = Object.assign({},pagnation.page, extraParams)
		console.log('请求的参数params', params)
		const {pages, current,records = []} =  (await apiReq(params)).data
		if(pages <= current){
			pagnation.status = "noMore"
		}else{
			pagnation.status = 'more'
		}
		if(pagnation.page.pageNo == 1){
			pagnation.list = records
		}else{
			pagnation.list = [...pagnation.list, ...records]
		}
	}
	
	const reset = async () => {
		pagnation.list = []
		await list()
	}
	
	const scrollLoad = (async () => {
		if(pagnation.status==='more' && isReachBottom){
			pagnation.page.pageNo +=1
			console.log('上拉请求的参数params', pagnation)
			await list()
		}
		
	})
	
	
	return {pagnation, list,reset,extraParams, scrollLoad}	
}
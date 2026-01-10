<template>
	<view class="list-wrap">
		<hh-nav-bar image="../../../../static/icon-nav-bg.png" title="查询结果" :show-back="true" status-theme="black"></hh-nav-bar>
		<view class="search-result">
			<view class="tab-filter">
				<picker mode="selector" @change="bindPickerChange" :range="caseTypeList" range-key="label" :value="caseTypeIndex" style="flex: 1">
					<view class="tab-filter__item">
						<view class="item-label">
							<view class="uni-input">{{ caseTypeList[caseTypeIndex]?.label || '案件类型' }}</view>
						</view>
					</view>
				</picker>
				<!-- 顾问单位 -->
				<picker mode="selector" @change="bindPickerChange3" :range="customerList" range-key="label" :value="customerIndex" style="flex: 1">
					<view class="tab-filter__item">
						<view class="item-label">
							<view class="uni-input">{{ customerList[customerIndex]?.label || '顾问单位' }}</view>
						</view>
					</view>
				</picker>
				<view class="choose-label" @click="bindPickerChange2">
					<view v-if="invoiceName" class="name">
						{{invoiceName}}
					</view>
					<view v-else class="label">
						开票状态
					</view>
				</view>
			</view>
			<view class="list-wrap padding-safe-area">
				<scroll-view scroll-y="true" @scrolltolower="onScrollTolower" style="height: 100%">
					<view class="card" v-for="(item, index) in pagnation.list" :key="index" @click="toPage(item)">
						<template v-if="item.cust">
							<view class="tip" :class="{blue: item.cust.invoice == 1, green: item.cust.invoice == 3, orange: item.cust.invoice == 2, red: item.cust.invoice == 4, }">
								{{ invoiceTypeList[item.cust.invoice - 1] }}
							</view>
							<image class="card-bg" src="@/static/card-bg.png" mode=""></image>
							
							<view class="card-info">
								<view class="type">顾问单位</view>
								<!-- <view class="title">{{item.cust.customerName}}</view> -->
								<view class="caseNo">客户编号： {{item.cust.customerCode}}</view>
								<view class="caseNo caseDesc">
									客户地址：{{item.cust.address || ''}}
								</view>
								<view class="info">
									<view class="">申请人：{{item.userName}}</view>
									<view class="">申请时间： {{item.cust.createTime?.slice(0, 10) || "--"}}</view>
								</view>
							</view>
							
						</template>
						<template v-else>
							<view class="tip" :class="{blue: item.invoice == 1, green: item.invoice == 3, orange: item.invoice == 2, red: item.invoice == 4, }">
								{{ invoiceTypeList[item.invoice - 1] }}
							</view>
							<image class="card-bg" src="@/static/card-bg.png" mode=""></image>
							
							<view class="card-info">
								<view class="type">{{item.caseTypeStr}}</view>
								<!-- <view class="title">{{item.caseName}}</view> -->
								<view class="caseNo">案件编号： {{item.caseNum}}</view>
								<view class="caseNo caseDesc">
									{{item.subjectOfAction}}
								</view>
								<view class="info">
									<view class="">申请人：{{item.userName}}</view>
									<view class="">申请时间： {{item.createTime?.slice(0, 10) || "--"}}</view>
								</view>
							</view>
						</template>
					</view>
					<Nodata v-if="pagnation.list.length===0 && pagnation.status != 'loading'"></Nodata>
					<uni-load-more v-else :status="pagnation.status"></uni-load-more>
				</scroll-view>
			</view>
		</view>
		<ChooseMore ref="popup" title="开票状态" @chooseArr="handleChooseArr" :checkboxData="options2" @chooseCancel="chooseCancel" />
	</view>
</template>

<script setup>
import { reactive, ref } from 'vue';
import api from '@/api/index.js';
import { useUserStore } from '@/store/user.js';
import {onLoad, onShow} from "@dcloudio/uni-app";
import Nodata from '@/components/c-nodata.vue';
import ChooseMore from '@/components/c-choose-more.vue';
import {usePagnation,testRequest} from "@/hooks/pagnation.js"
import { invoiceTypeList } from '@/utils/comvariable.js';

let { list, pagnation, reset, extraParams, scrollLoad } = usePagnation(api.invoice.getPageList, {});
const store = useUserStore();

let caseTypeList = ref([]);
let customerList = ref([])
const options2 = ref([
	{
		label: '未开票',
		value: '1'
	},
	{
		label: '待审批',
		value: '2'
	},
	{
		label: '已开票',
		value: '3'
	},
	{
		label: '已回退',
		value: '4'
	}
]);
let init = true
const invoiceName = ref()
// 案件类型
const caseTypeIndex = ref()
onLoad((option) => {
	const params = JSON.parse(option.params)
	// 案件类型
	api.common.getRedisDictList({dictType: 'caseType'}).then(res => {
		caseTypeList.value = res.data.map(val => {
			return {
				value: val.dictValue,
				label: val.dictLabel
			}
		})
		caseTypeList.value.unshift({value: '', label: '全部'})
		caseTypeIndex.value = caseTypeList.value.findIndex(val => val.value == params.caseType)
	})
	
	// 顾问单位
	api.common.byRole({}).then(res => {
		customerList.value = res.data.map(val => {
			return {
				value: val.id,
				label: val.customerName
			}
		})
		customerList.value.unshift({value: '', label: '全部'})
	})
	init = false
	extraParams.caseName = params.caseName
	extraParams.startTime = params.startTime
	extraParams.endTime = params.endTime
	extraParams.caseNum = params.caseNum
	extraParams.caseType = params.caseType
	extraParams.invoice = params.invoice
	const invoiceArr = params.invoice.split(',')
	const temp = []
	options2.value = options2.value.map(val => {
		val.choose = invoiceArr.includes(val.value)
		if(invoiceArr.includes(val.value)) temp.push(val.label)
		return val
	})
	invoiceName.value = temp.join(',')
	console.log('888888888888888xtraParams', extraParams, options2.value, invoiceName.value)
	list()
})
onShow(() => {
	if(init) return
	pagnation.page.pageNo = 1
	pagnation.list = []
	list()
})
// 上拉刷新
const onScrollTolower = () => {
	scrollLoad()
}
const rules = reactive({});

// 是否有审批的权限
const roleIds = []
store.userInfo.roleList?.map(val => {
	roleIds.push(val.id)
})
const isShow = (detailInfo) => {
	const hasPermissions = detailInfo.approveIds?.approverValue.split(',').some(item => {
		return roleIds.includes(Number(item))
	})
	return detailInfo.invoice == 2 && (detailInfo.approveIds?.approverType == 1 && detailInfo.approveIds?.approverValue.split(',').includes(`${store.userInfo.id}`)
	 || (detailInfo.approveIds?.approverType == 2 && hasPermissions))
}
const toPage = (item) => {
	console.log('333333333item', item)
	// let url = '/pages/invoice/addInvoice?caseId=' + item.id +'&status=' + item.invoice +'&id='+item.invoiceId+'&money=' + item.money
	// if(item.invoice == 2 || item.invoice == 3) {
	// 	url = '/pages/invoice/invoiceCheck?id=' + item.invoiceId + '&caseId=' + item.id +'&isShow=' + isShow(item)
	// }
	let url = '/pages/invoice/invoiceCheck?id=' + item.invoiceId + '&caseId=' + item.caseId  + '&isOwner=' + `${item.createUser == store.userInfo.id}` + '&isCust=' + !!item.cust +'&letter=' + item.letter+'&invoice=' + (item.invoice || item.cust.invoice)
	if(item.invoice == 1) {
		api.invoice.toInvoice({id: item.money - 3000 < 0 ? 2 : 3}).then(res => {
			if(res.code == 0) {
				if(!res.data) {
					uni.showToast({
						icon:"none",
						title:'请联系管理员配置权限！'
					})
				} else {
					uni.navigateTo({
						url
					})
				}
			}
		})
	} else {
		uni.navigateTo({
			url
		})
	}
}


const bindPickerChange = (e) => {
	caseTypeIndex.value = Number(e.detail.value);
	extraParams.caseType = caseTypeList.value[e.detail.value].value
	pagnation.page.pageNo = 1
	pagnation.list = []
	list()
};

const customerIndex = ref(0)
const bindPickerChange3 = (e) => {
	customerIndex.value = Number(e.detail.value);
	extraParams.customerId = customerList.value[e.detail.value].value
	pagnation.page.pageNo = 1
	pagnation.list = []
	list()
};

const handleChooseArr = (val, name) => {
	console.log('fanhuide zhi ', val)
	extraParams.invoice = val.join(',')
	invoiceName.value = name.join(',')
	options2.value = options2.value.map(res => {
		res.choose = val.includes(res.value)
		return res
	})
	pagnation.page.pageNo = 1
	pagnation.list = []
	list()
}

const chooseCancel = () => {
	options2.value = options2.value.map(res => {
		res.choose = extraParams.invoice.indexOf(res.value) > -1
		return res
	})
}



const popup = ref(null)
const bindPickerChange2 = (e) => {
	popup.value.openPop()
};

</script>

<style scoped lang="scss">
@import '../../utils/listStyle.scss';
</style>

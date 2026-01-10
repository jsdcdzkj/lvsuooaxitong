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
					<view v-if="letterName" class="name">
						{{letterName}}
					</view>
					<view v-else class="label">
						开函状态
					</view>
				</view>
				<!-- <picker mode="selector" @change="bindPickerChange2" :range="options2" range-key="label" :value="letterIndex" style="flex: 1">
					<view class="tab-filter__item">
						<view class="item-label">
							<view class="uni-input">{{ options2[letterIndex]?.label || '开函状态'}}</view>
						</view>
					</view>
				</picker> -->
			</view>
			<view class="list-wrap padding-safe-area">
				<scroll-view scroll-y="true" @scrolltolower="onScrollTolower" style="height: 100%">
					<view class="card" v-for="(item, index) in pagnation.list" :key="index" @click="toPage(item)">
						<view class="tip" :class="{blue: item.letter == 1, green: item.letter == 3, orange: item.letter == 2, red: item.letter == 4 }">
							{{ letterTypeList[item.letter - 1] }}
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
								<view class="">申请时间： {{item.summonsTime?.slice(0,10) || '--'}}</view>
							</view>
						</view>
					</view>
					<Nodata v-if="pagnation.list.length===0 && pagnation.status != 'loading'"></Nodata>
					<uni-load-more v-else :status="pagnation.status"></uni-load-more>
				</scroll-view>
			</view>
		</view>
		<ChooseMore ref="popup" title="开函状态" @chooseArr="handleChooseArr" :checkboxData="options2" @chooseCancel="chooseCancel" />
	</view>
</template>

<script setup>
import { reactive, ref } from 'vue';
import api from '@/api/index.js';
import { useUserStore } from '@/store/user.js';
import {onLoad, onShow} from "@dcloudio/uni-app";
import Nodata from '@/components/c-nodata.vue';
import {usePagnation,testRequest} from "@/hooks/pagnation.js"
import { letterTypeList } from '@/utils/comvariable.js'
import ChooseMore from '@/components/c-choose-more.vue';

let { list, pagnation, reset, extraParams, scrollLoad } = usePagnation(api.letter.getPageList, {});
const store = useUserStore();

let caseTypeList = ref([]);
let customerList = ref([])
const options2 = ref([
	{
		label: '未开函',
		value: '1',
		choose: false,
	},
	{
		label: '待审批',
		value: '2',
		choose: false,
	},
	{
		label: '已开函',
		value: '3',
		choose: false,
	},
	{
		label: '已回退',
		value: '4',
		choose: false,
	}
]);
const letterName = ref()
let init = true
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
	
	// extraParams.caseName = params.caseName
	extraParams.startTime = params.startTime
	extraParams.endTime = params.endTime
	extraParams.caseNum = params.caseNum
	extraParams.caseType = params.caseType
	extraParams.letter = params.letter
	const letterArr = params.letter.split(',')
	const temp = []
	options2.value = options2.value.map(val => {
		val.choose = letterArr.includes(val.value)
		if(letterArr.includes(val.value)) temp.push(val.label)
		return val
	})
	letterName.value = temp.join(',')
	console.log('8888888888888extraParams', extraParams, options2.value, letterName.value)
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
const form = reactive({
	listType: 0,
	roomStatus: 0
});

const rules = reactive({});

const toPage = (item) => {
	console.log('8888888888888888888item', item, item.createUser, store.userInfo.id, item.createUser != store.userInfo.id)
	// let url = '/pages/letter/addLetter?caseId=' + item.id +'&status=' + item.letter +'&id='+item.summonsId
	let url = '/pages/letter/letterCheck?id=' + item.summonsId + '&caseId=' + item.id  + '&isOwner=' + `${item.createUser == store.userInfo.id}`+'&invoice=' + item.invoice
	// if((item.createUser != store.userInfo.id) || (item.letter == 2 || item.letter == 3)) {
	// 	url = '/pages/letter/letterCheck?id=' + item.summonsId + '&caseId=' + item.id + '&isShow=' + isShow(item) + '&isOwner=' + `${item.createUser == store.userInfo.id}`
	// 	console.log('3333333333333333url',url)
	// }
	if(item.letter == 1) {
		api.invoice.toInvoice({id: 1}).then(res => {
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

const caseTypeIndex = ref()
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
	extraParams.letter = val.join(',')
	letterName.value = name.join(',')
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
		res.choose = extraParams.letter.indexOf(res.value) > -1
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

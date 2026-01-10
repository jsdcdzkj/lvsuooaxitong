<template>
	<view class="list-wrap">
		<hh-nav-bar image="../../../../static/icon-nav-bg.png" title="已办事项" :show-back="true" status-theme="black"></hh-nav-bar>
		<view class="search-result">
			<view class="tab-filter">
				<picker mode="selector" @change="bindPickerChange" :range="options1" range-key="label" :value="extraParams.approveType" style="flex: 1">
					<view class="tab-filter__item">
						<view class="item-label">
							<view class="uni-input">{{ options1[approveTypeIndex]?.label || '审批类型' }}</view>
						</view>
					</view>
				</picker>

				<picker mode="selector" @change="bindPickerChange3" :range="options3" range-key="label" :value="timeIndex" style="flex: 1">
					<view class="tab-filter__item">
						<view class="item-label">
							<view class="uni-input">{{ options3[timeIndex]?.label || '排序状态' }}</view>
						</view>
					</view>
				</picker>
			</view>
			<view class="list-wrap padding-safe-area">
				<scroll-view scroll-y="true" @scrolltolower="onScrollTolower" style="height: 100%">
					<view class="card" v-for="(item, index) in pagnation.list" :key="index" @click="toPage(item)">
						<view v-if="item.processType == 1" class="tip green" :class="{red: item.letter == 4 }">
							{{  item.letter == 4 ?'已退回' : '已通过' }}
						</view>
						<view v-else-if="!item.cust" class="tip green" :class="{red: item.invoice == 4 }">
							{{ item.invoice == 4 ?'已退回' : '已通过' }}
						</view>
						<view v-else-if="item.cust" class="tip green" :class="{red: item.cust.invoice == 4 }">
							{{ item.cust.invoice == 4 ?'已退回' : '已通过' }}
						</view>
						<image class="card-bg" src="@/static/card-bg.png" mode=""></image>
						
						<view class="card-info">
							<view style="display: flex">
								<view class="type" style="margin-right: 16rpx;">{{item.cust ? '顾问单位' :item.caseTypeStr}}</view>
								<view class="type">{{item.processType == 1 ? '开函审核' : '开票审核'}}</view>
							</view>
							<!-- <view class="title">{{item.cust ? item.cust.customerName : item.caseName}}</view> -->
							<view class="caseNo"> {{item.cust ? '客户编号：' + item.cust.customerCode: '案件编号：' + item.caseNum}}</view>
							<view class="caseNo caseDesc">
								{{item.cust ? `客户地址：${item.cust.address || ''}` : item.subjectOfAction}}
							</view>
							<view class="info">
								<view class="">申请人：{{item.userName}}</view>
								<view class="">申请时间： {{item.processType == 1 ? (item.summonsTime?.slice(0,10) || "--") : (item.createTime?.slice(0,10) || item.cust?.createTime?.slice(0,10) || "--")}}</view>
							</view>
						</view>
					</view>
					<Nodata v-if="pagnation.list.length===0 && pagnation.status != 'loading'"></Nodata>
					<uni-load-more v-else :status="pagnation.status"></uni-load-more>
				</scroll-view>
			</view>
		</view>
	</view>
</template>

<script setup>
import { reactive, ref } from 'vue';
import api from '@/api/index.js';
import { useUserStore } from '@/store/user.js';
import {onLoad, onShow} from "@dcloudio/uni-app";
import Nodata from '@/components/c-nodata.vue';
import {usePagnation,testRequest} from "@/hooks/pagnation.js"
import { invoiceTypeList } from '@/utils/comvariable.js'

let { list, pagnation, reset, extraParams, scrollLoad } = usePagnation(api.caseManage.finisList, {approveType: '', order:2});
const store = useUserStore();

onLoad((option) => {
	list()
})

// 上拉刷新
const onScrollTolower = () => {
	scrollLoad()
}
const rules = reactive({});

const toPage = (item) => {
	let url = '/pages/invoice/invoiceCheck?id=' + item.invoiceId + '&caseId=' + item.id + '&isCust=' + !!item.cust
	if(item.processType == 1) {
		url = '/pages/letter/letterCheck?id=' + item.summonsId + '&caseId=' + item.id
	} 
	if(item.cust) {
		url = '/pages/invoice/invoiceCheck?id=' + item.cust.id + '&caseId=' + item.cust.id + '&isCust=' + !!item.cust+ '&invoice=' + item.cust.invoice
	}
	uni.navigateTo({
		url
	})
}

const options1 = ref([
	{
		label: '全部',
		value: ''
	},
	{
		label: '开票审核',
		value: 2
	},
	{
		label: '开函审核',
		value: 1
	}
]);
const approveTypeIndex = ref(0)
const bindPickerChange = (e) => {
	extraParams.approveType = options1.value[e.detail.value].value;
	pagnation.page.pageNo = 1
	pagnation.list = []
	list()
};

const options3 = ref([
	{
		label: '时间正序',
		value: 1
	},
	{
		label: '时间倒序',
		value: 2
	},
]);
const timeIndex = ref(1)
const bindPickerChange3 = (e) => {
	timeIndex.value = e.detail.value
	extraParams.order = options3.value[e.detail.value].value;
	pagnation.page.pageNo = 1
	pagnation.list = []
	list()
};

</script>

<style scoped lang="scss">
@import '../../utils/listStyle.scss';
</style>

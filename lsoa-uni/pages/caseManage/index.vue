<template>
	<view class="content">
		<hh-nav-bar image="../../../../static/icon-nav-bg.png" title="案件查询" :show-back="true" status-theme="black"></hh-nav-bar>
		<view class="searchBox">
			<uni-forms
				ref="baseForm"
				:modelValue="formData"
				:rules="rules"
				label-position="left"
				:border="true"
				label-width="200rpx"
			>
				<uni-forms-item label="登记人" :name="['createUser', 'value']" v-if="roleVal.isShow">
					<picker mode="selector" @change="selectCreateUser" :value="createUserValue" :range="createUserList" range-key="text">
						<input type="text"  placeholder-style="color:#999;font-size: 28rpx" v-model="formData.createUser.value" placeholder="请选择" class="selectItem selectChoose" disabled/>
					</picker>
				</uni-forms-item>
				<uni-forms-item label="登记时间" name="createTime">
					<uni-datetime-picker :border="false" v-model="formData.createTime" type="daterange" @maskClick="maskClick" />
				</uni-forms-item>
				<view class="divider-stripe"></view>
				<uni-forms-item label="案件编号" name="caseNum">
					<input
						v-model="formData.caseNum"
						placeholder-style="color:#999;font-size: 28rpx"
						placeholder="请输入"
						class="selectItem"
					/>
				</uni-forms-item>
				<!-- <uni-forms-item label="案件名称" name="caseName">
					<input
						v-model="formData.caseName"
						placeholder-style="color:#999;font-size: 28rpx"
						placeholder="请输入"
						 class="selectItem"
					/>
				</uni-forms-item> -->
				<view class="divider-stripe"></view>
				<uni-forms-item label="案件类型" :name="['caseType', 'value']">
					<picker mode="selector" @change="selectCaseType" :value="caseTypeValue" :range="caseTypeList" range-key="text">
						<input type="text" placeholder-style="color:#999;font-size: 28rpx" v-model="formData.caseType.value" placeholder="请选择" class="selectItem selectChoose" disabled/>
					</picker>
				</uni-forms-item>
				<uni-forms-item label="当前状态" name="status">
					<view class="serve-wrap">
						<view class="serve-btn" :class="{active: statusIndex.includes(1)}" @click="changeStatus(1)">进行中</view>
						<view class="serve-btn" :class="{active: statusIndex.includes(2)}" @click="changeStatus(2)">已完结</view>
						<view class="serve-btn" :class="{active: statusIndex.includes(3)}" @click="changeStatus(3)">已中止(解除委托或撤销)</view>
					</view>
				</uni-forms-item>
				<view class="divider-stripe"></view>
				<uni-forms-item label="是否开票" name="invoice">
					<view class="serve-wrap">
						<view class="serve-btn" :class="{active: invoice == 1}" @click="changeInvoice(1)">未开票</view>
						<view class="serve-btn" :class="{active: invoice == 2}" @click="changeInvoice(2)">待审批</view>
						<view class="serve-btn" :class="{active: invoice == 3}" @click="changeInvoice(3)">已开票</view>
						<view class="serve-btn" :class="{active: invoice == 4}" @click="changeInvoice(4)">已退回</view>
					</view>
				</uni-forms-item>
				<uni-forms-item label="是否开函" name="letter">
					<view class="serve-wrap">
						<view class="serve-btn" :class="{active: letter == 1}" @click="changeLetter(1)">未开函</view>
						<view class="serve-btn" :class="{active: letter == 2}" @click="changeLetter(2)">待审批</view>
						<view class="serve-btn" :class="{active: letter == 3}" @click="changeLetter(3)">已开函</view>
						<view class="serve-btn" :class="{active: letter == 4}" @click="changeLetter(4)">已退回</view>
					</view>
				</uni-forms-item>
			</uni-forms>
		</view>
		<view class="footer">
			<button class="btntype" @click="handleSearch">查询</button>
		</view>
	</view>
</template>

<script setup>
import { reactive, ref,computed } from 'vue';
import api from "@/api/index.js"
	import {useUserStore} from "@/store/user.js"
	import {onLoad} from "@dcloudio/uni-app"
	const store = useUserStore()
	
	let caseTypeList = ref([]);
	let createUserList = ref([])
	// let litigationList = ref([])
	onLoad(() => {
		// 案件类型
		api.common.getRedisDictList({dictType: 'caseType'}).then(res => {
			caseTypeList.value = res.data.map(val => {
				return {
					value: val.dictValue,
					text: val.dictLabel
				}
			})
			caseTypeList.value.unshift({
				value: '',
				text: '全部'
			})
		})
		// 律师
		api.common.getNormalUserList({}).then(res => {
			createUserList.value = res.data.map(val => {
				return {
					value: val.id,
					text: val.realName
				}
			})
		})
	})
	
	// 是否显示登记人查询条件
	const roleVal = computed(() => {
		const arr = store.userInfo.roleList?.filter(val => val.roleCode == 'zr' || val.roleCode == 'admin1')
		return {
			isShow: arr.length > 0
		}
	})
	
	//案件类型
	const caseTypeValue = ref(1);
	const selectCaseType = (e) => {
		caseTypeValue.value = e.detail.value;
		const { text, value } = caseTypeList.value[e.detail.value];
		formData.caseType.id = value;
		formData.caseType.value = text;
	};
	
	//创建人
	const createUserValue = ref(1);
	const selectCreateUser = (e) => {
		createUserValue.value = e.detail.value;
		const { text, value } = createUserList.value[e.detail.value];
		formData.createUser.id = value;
		formData.createUser.value = text;
	};
	
	const formData = reactive({
		caseType: {
			value: '',
			id: '',
		},
		createUser: {
			value: '',
			id: '',
		},
	});
	// 登记时间
	const maskClick = (val) => {
		console.log('222222222222222', val)
	}
	
	const rules = reactive({})
	const styleInput = reactive({
				borderColor: '#fff',
				borderRadius: '0',
				backgroundColor: '#fff',
			})
	
	const toPage = () => {
		uni.navigateTo({
			url: '/pages/usercenter/resetPassword'
		})
	}
	
	//案件类型
	const urgencyValue = ref()
	const urgencys = reactive([])
	const selectEmergent = (e) => {
		urgencyValue.value = e.detail.value;
		const { text, value } = urgencys[e.detail.value];
		formData.urgency.id = value;
		formData.urgency.value = text;
	}
	
	// 当前状态选择
	const statusIndex  = ref([])
	const changeStatus = (e) => {
		// if( e == statusIndex.value) {
		// 	statusIndex.value = ''
		// } else {
		// 	statusIndex.value = e;
		// }
		if(statusIndex.value.includes(e)) {
			statusIndex.value = statusIndex.value.filter(val => val != e)
		} else {
			statusIndex.value.push(e)
		}
	}
	// 开票选择
	const invoice  = ref()
	const changeInvoice = (e) => {
		if (e != 3)  {
			letter.value = ''
		}
		if(e == invoice.value) {
			if(e==3) letter.value = ''
			invoice.value = ''
		} else {
			invoice.value = e;
		}
	}
	
	// 开函选择
	const letter  = ref()
	const changeLetter = (e) => {
		if(letter.value == e) {
			letter.value = ''
		} else {
			letter.value = e;
		}
		
	}
			
	const  handleSearch = () => {
		// 查询条件
		const params = {
			startTime: formData.createTime?.[0], 
			endTime: formData.createTime?.[1], 
			caseNum: formData.caseNum,
			caseType: formData.caseType.id,
			createUser: formData.createUser.id,
			letter:  letter.value,
			status:  statusIndex.value.join(','),
			invoice:  invoice.value,
		}
		uni.navigateTo({
			url: `/pages/caseManage/searchResult?params=${JSON.stringify(params)}`
		})
	}
</script>

<style scoped lang="scss">
.content {
	position: relative;
	height: 100vh;
	background: #F8F8F8;
	font-size: 28rpx;
	
	.searchBox {
		background: #fff;
	}
	
	.divider-stripe {
		width: 100%;
		height: 16rpx;
		background: #F8F8F8;
	}
	
	.serve-wrap {
		display: flex;
		flex-wrap: wrap;
		height: 100%;
		align-items: center;
		.serve-btn {
			min-width: 104rpx;
			height: 48rpx;
			line-height:48rpx;
			text-align: center;
			background: #f8f8f8;
			border-radius: 8rpx;
			font-size: 24rpx;
			color: rgba(0,0,0,0.65);
			margin-right: 16rpx;
			padding:0 4rpx;
		}
		.active {
			background: #409EFF;
			color: #fff;
		}
	}
}
::v-deep {
	.uni-forms-item__label {
		padding-left: 32rpx;
	}
	.uni-easyinput__content {
		background-color:#fff!important;
	}
}
</style>

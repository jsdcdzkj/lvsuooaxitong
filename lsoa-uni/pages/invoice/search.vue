<template>
	<view class="content">
		<hh-nav-bar image="../../../../static/icon-nav-bg.png" title="开票查询" :show-back="true" status-theme="black"></hh-nav-bar>
		<view class="searchBox">
			<uni-forms
				ref="baseForm"
				:modelValue="formData"
				:rules="rules"
				label-position="left"
				:border="true"
				label-width="232rpx"
			>
				<uni-forms-item label="申请开票时间" name="realName">
					<uni-datetime-picker :border="false" v-model="formData.range" type="daterange" @maskClick="maskClick" />
				</uni-forms-item>
				<uni-forms-item label="案件编号" name="caseNum">
					<input
						v-model="formData.caseNum"
						placeholder="请输入"
						placeholder-style="color:#999;font-size: 28rpx"
						class="selectItem"
					/>
				</uni-forms-item>
				<!-- <uni-forms-item label="案件名称" name="caseName">
					<input
						v-model="formData.caseName"
						placeholder="请输入"
						placeholder-style="color:#999;font-size: 28rpx"
						class="selectItem"
					/>
				</uni-forms-item> -->
				<view class="divider-stripe"></view>
				<uni-forms-item label="案件类型" :name="['caseType', 'value']">
					<picker mode="selector" @change="selectCaseType" :value="caseTypeValue" :range="caseTypeList" range-key="text">
						<input type="text" placeholder-style="color:#999;font-size: 28rpx" v-model="formData.caseType.value" placeholder="请选择" class="selectItem selectChoose" disabled />
					</picker>
				</uni-forms-item>
				<uni-forms-item label="开票状态" name="invoice">
					<view class="serve-wrap">
						<view class="serve-btn" :class="{active: invoice.includes(1)}" @click="changeInvoice(1)">未开票</view>
						<view class="serve-btn" :class="{active: invoice.includes(2)}" @click="changeInvoice(2)">待审批</view>
						<view class="serve-btn" :class="{active: invoice.includes(3)}" @click="changeInvoice(3)">已开票</view>
						<view class="serve-btn" :class="{active: invoice.includes(4)}" @click="changeInvoice(4)">已退回</view>
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
	import { reactive, ref } from 'vue';
	import api from "@/api/index.js"
	import {onLoad} from "@dcloudio/uni-app"
	import {useUserStore} from "@/store/user.js"
	const store = useUserStore()
	
	let caseTypeList = ref([]);

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
	})
	
	//案件类型
	const caseTypeValue = ref();
	const selectCaseType = (e) => {
		caseTypeValue.value = e.detail.value;
		const { text, value } = caseTypeList.value[e.detail.value];
		formData.caseType.id = value;
		formData.caseType.value = text;
	};
	const formData = reactive({
		caseType: {
			value: '',
			id: '',
		},
		range: [],
	});
	const rules = reactive({})
	const styleInput = reactive({
				borderColor: '#fff',
				borderRadius: '0',
				backgroundColor: '#fff',
			})
	
	
	// 开票选择
	const invoice  = ref([])
	const changeInvoice = (e) => {
		if(invoice.value.includes(e)) {
			invoice.value = invoice.value.filter(val => val != e)
		} else {
			invoice.value.push(e)
		}
	}
	
	const  handleSearch = () => {
		// 查询条件
		const params = {
			startTime: formData.range?.[0], 
			endTime: formData.range?.[1], 
			caseNum: formData.caseNum,
			caseType: formData.caseType.id,
			invoice:  invoice.value.join(','),
		}
		uni.navigateTo({
			url: `/pages/invoice/searchResult?params=${JSON.stringify(params)}`
		})
	}
</script>

<style scoped lang="scss">
.content {
	position: relative;
	height: 100vh;
	background: #F8F8F8;
	
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
			width: 112rpx;
			height: 48rpx;
			line-height:48rpx;
			text-align: center;
			background: #f8f8f8;
			border-radius: 8rpx;
			font-size: 24rpx;
			color: rgba(0,0,0,0.65);
			margin-right: 16rpx;
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

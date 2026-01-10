<template>
	<view class="wrap">
		<view class="header">
			<image class="bg" src="@/static/login-bg.png" mode=""></image>
			<view style="position: absolute;bottom:0;width: 100%">
				<view class="title">
					律所OA系统
				</view>
				<view class="user">
					<image v-if="store.userInfo.avatar" class="avater" :src="store.userInfo.avatar" mode=""></image>
					<image v-else class="avater" src="@/static/img_avater.png" mode=""></image>
					<view class="user-info">
						<view class="name">
							{{store.userInfo.realName}}
						</view>
						<view class="work">
							律师事务所
							<text v-if="store.userInfo.dept?.name" class="tip">{{store.userInfo.dept.name}}</text>
						</view>
					</view>
				</view>
			</view>
			<view class="triangle"></view>
		</view>
		<view class="content">
			<view class="box">
				<view class="title">
					审批事件
				</view>
				<view class="matter">
					<view class="item" @click="toPage('/pages/todoMatter/index')">
						<view class="num">{{todoNum}}</view>
						<view class="name">待办事项</view>
					</view>
					<view class="item" @click="toPage('/pages/finishMatter/index')">
						<view class="num">{{doneNum}}</view>
						<view class="name">已办事项</view>
					</view>
				</view>
			</view>
			<view class="box">
				<view class="title">
					案件管理
				</view>
				<view class="case-manage">
					<view class="card" @click="toPage('/pages/caseManage/index')">
						<image src="@/static/icon-case.png" mode=""></image>
						<view class="name">案件查询</view>
					</view>
					<view class="card" @click="toPage('/pages/invoice/search')" style="margin-left:24rpx;">
						<image src="@/static/icon-invoice.png" mode=""></image>
						<view class="name">开票查询</view>
					</view>
					<view class="card" @click="toPage('/pages/letter/search')">
						<image src="@/static/icon-letter.png" mode=""></image>
						<view class="name">开函查询</view>
					</view>
					<view class="card" @click="toPage('/pages/caseManage/addCase')" style="margin-left:24rpx;">
						<image src="@/static/icon-add-case.png" mode=""></image>
						<view class="name">新增案件</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import { reactive, ref } from 'vue';
	import api from "@/api/index.js"
	import {useUserStore} from "@/store/user.js"
	import {onLoad, onShow} from "@dcloudio/uni-app"
	const store = useUserStore()
	
	const toPage = (url) => {
		uni.navigateTo({
			url
		})
	}
	
	// 请求待办，已办数量
	const todoNum = ref(0)
	const doneNum = ref(0)
	onShow(() => {
		api.caseManage.toDo({approveType: ''}).then(res => {
			if(res.code == 0) {
				todoNum.value = res.data.total
			}
		})
		api.caseManage.finisList({approveType: ''}).then(res => {
			if(res.code == 0) {
				doneNum.value = res.data.total
			}
		})
		api.common.fileTypeList({}).then(res => {
                const {fileTypeConfig} = res.data
                fileTypeConfig.dictValue
				uni.setStorageSync('fileType', fileTypeConfig.dictValue)

            })
			
		if(uni.getStorageSync('password') == 'Jsdc@0516') {
			uni.showModal({
			    title: '提示', // 标题
			    content: '您的密码为默认密码，建议您尽快修改密码!', // 内容
			    showCancel: false, // 是否显示取消按钮
			    confirmText: '确定', // 确认按钮文本
			    confirmColor: '#3CC51F', // 确认按钮颜色
			    success: function (res) {
			        if (res.confirm) {
			            uni.navigateTo({
			            	url:'/pages/usercenter/resetPassword'
			            })
			        }
			    }
			});
			
			
		}
	})
	
</script>

<style scoped lang="scss">
	.wrap {
		.header {
			position: relative;
			width: 750rpx;
			height: 384rpx;
			.bg {
				width: 750rpx;
				height: 384rpx;
				position: absolute;
				top:0;
				left:0;
			}
			.triangle {
				border: 20rpx solid transparent;
				border-top: 20rpx solid #2A7DEC;
				position: absolute;
				bottom: -40rpx;
				left: 50%;
				transform: translate(-50%, 0);
			}
			.title {
				font-size: 36rpx;
				color: #FFFFFF;
				line-height: 52rpx;
				text-align: center;
			}
			.user {
				padding: 56rpx 48rpx;
				display: flex;
				
				.avater {
					width: 112rpx;
					height: 112rpx;
					border-radius: 32rpx;
					border: 4rpx solid #FFFFFF;
					overflow: hidden;
					margin-right: 40rpx;
				}
				
				.user-info {
					font-size: 40rpx;
					color: #FFFFFF;
					line-height: 56rpx;
					display: flex;
					flex-direction: column;
					justify-content: space-between;
					
					.work {
						margin-top: 8rpx;
						font-size: 28rpx;
						color: rgba(255,255,255,0.85);
						line-height: 44rpx;
					}
					
					.tip {
						font-size: 24rpx;
						color: #FFFFFF;
						line-height: 40rpx;
						background: rgba(255,255,255,0.2);
						border-radius: 8rpx;
						padding: 8rpx;
						margin-left: 16rpx;
					}
				}
			}
		}
		.box {
			padding: 48rpx 32rpx 0;
			.title {
				font-weight: bold;
				font-size: 32rpx;
				color: rgba(0,0,0,0.85);
				line-height: 48rpx;
				position: relative;
				display: flex;
				align-items: center;
				
				&::before {
					content: '';
					width: 8rpx;
					height: 32rpx;
					display: flex;
					background: #3184F2;
					border-radius: 16rpx;
					margin-right: 16rpx;
				}
			}
			.matter {
				width: 100%;
				background: #FFFFFF;
				box-shadow: 0px 1px 24rpx 0px rgba(32,144,225,0.18);
				border-radius: 24rpx;
				overflow: hidden;
				margin-top: 32rpx;
				display: flex;
				
				.item {
					flex:1;
					display: flex;
					justify-content: center;
					align-items: center;
					flex-direction: column;
					height: 144rpx;
					
					.num {
						font-size: 32rpx;
						color: rgba(0,0,0,0.85);
						line-height: 40rpx;
						font-weight: bold;
					}
					
					.name {
						font-size: 24rpx;
						color: rgba(0,0,0,0.65);
						line-height: 40rpx;
						margin-top: 8rpx;
					}
				}
			}
			.case-manage {
				display: flex;
				flex-wrap: wrap;
				margin-top: 32rpx;
				.card {
					width: calc(50% - 76rpx);
					height: 144rpx;
					background: #FFFFFF;
					box-shadow: 0px 1px 24rpx 0px rgba(32,144,225,0.18);
					border-radius: 24rpx;
					padding:0 32rpx;
					display: flex;
					align-items: center;
					font-size: 28rpx;
					color: rgba(0,0,0,0.85);
					line-height: 44rpx;
					margin-bottom: 24rpx;
					
					image {
						width: 80rpx;
						height: 80rpx;
						margin-right: 32rpx;
					}
				}
			}
		}
	}
</style>

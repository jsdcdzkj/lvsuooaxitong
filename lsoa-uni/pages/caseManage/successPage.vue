<template>
	<view class="content">
		<hh-nav-bar :title="title" :show-back="true" status-theme="black"></hh-nav-bar>
		<view class="success-info">
			<image class="bg" src="@/static/login-bg.png" mode=""></image>
			<view class="info">
				<image class="icon-success" src="@/static/icon-success.png" mode=""></image>
				<text class="tip">{{msg}}</text>
			</view>
			<image class="icon-move" src="@/static/icon-move.png" mode=""></image>
		</view>
		<button class="btntype btn-common-primary" @click="handleBack">返回首页</button>
	</view>
</template>

<script setup>
	import { reactive, ref } from 'vue';
	import api from "@/api/index.js"
	import {useUserStore} from "@/store/user.js"
	import {onLoad} from "@dcloudio/uni-app"
	const store = useUserStore()

	const title= ref("")
	const msg = ref('')
	onLoad((option) => {
		console.log('optionoptionoption', option)
		title.value = option.title
		msg.value = option.msg
	})
	const handleBack = () => {
		uni.reLaunch({
			url: '/pages/index/index'
		});
	}
</script>

<style scoped lang="scss">
.content {
	position: relative;
	height: 100vh;
	background: #F8F8F8;
	font-size: 28rpx;
	
	.bg {
		width: 750rpx;
		height: 574rpx;
		position: absolute;
		top:0;
		left:0;
	}
	.success-info {
		.info {
			position: relative;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			height: 300rpx;
		}
		
		.icon-success {
			width: 112rpx;
			height: 112rpx;
			margin-bottom: 32rpx;
		}
		.tip {
			font-size: 32rpx;
			color: #FFFFFF;
			line-height: 48rpx;
		}
	}
	.icon-move {
		position: absolute;
		left: 0;
		top: 524rpx;
		width: 2110rpx;
		height: 50rpx;
		animation: stripmove 6s alternate infinite;
	}
	
	.btntype {
		margin: 240rpx 32rpx 0;
		border-radius: 72rpx;
		height: 72rpx;
		line-height: 72rpx;
		font-size: 28rpx;
	}
	
	@keyframes stripmove {
		from {
			left: 0;
		}
		to {
			left: -1100rpx;
		}
	}
	
	
}
</style>

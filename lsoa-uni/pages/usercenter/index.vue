<template>
	<view class="content">
		<hh-nav-bar image="../../../../static/icon-nav-bg.png" title="" :show-back="false" status-theme="black"></hh-nav-bar>
		<view class="bgimg">
			<image src="@/static/user-bg.png" mode="widthFix"></image>
			
		</view>
		<view class="user-info">
			<image v-if="store.userInfo.avatar" :src="store.userInfo.avatar" class="avater" mode=""></image>
			<image v-else src="@/static/img_avater.png" class="avater" mode=""></image>
			<text class="name">{{store.userInfo.realName}}</text>
			<text class="post">{{store.userInfo.dept?.name}}</text>
		</view>
		<view class="user-wrap">
			<view class="userbox">
				<view class="item-group">
					<view class="item-cell" @click="toPage('resetPassword')">
						<view class="label">
							<image src="@/static/icon-lock.png" mode="" class="icon"></image>
							<text class="name">密码修改</text></view>
						<view class="content"></view>
					</view>
					<view class="item-cell" @click="toPage('person')">
						<view class="label">
							<image src="@/static/icon-person.png" mode="" class="icon"></image>
							<text class="name">个人信息</text></view>
						<view class="content"></view>
					</view>
					<view class="item-cell" @click="logout">
						<view class="label">
							<image src="@/static/icon-exit.png" mode="" class="icon"></image>
							<text class="name">退出</text></view>
						<view class="content"></view>
					</view>
				</view>
			</view>
		</view>
		
	</view>
</template>

<script setup>
	import api from "@/api/index.js"
	import {useUserStore} from "@/store/user.js"
	const store = useUserStore()
	
	const toPage = (url) => {
		uni.navigateTo({
			url
		})
	}
			
	const  logout = () => {
		api.user.logout().then(res=>  {
			uni.showToast({
				icon:"success",
				title: '退出成功'
			})
			setTimeout(() => {
				store.logout()
				uni.reLaunch({
					url: "/pages/login/index"
				})
			}, 1000)
			
			uni.clearStorageSync(); //清空缓存
		}).catch(e => {
			console.log(e);
			uni.showToast({
				icon:"none",
				title:e
			})
		})
	}
</script>

<style scoped lang="scss">
	.content {
		// width: 100%;
		// height: 100%;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		position: relative;
		
		.user-info {
			display: flex;
			flex-direction: column;
			justify-content: center;
			width: 100%;
			align-items: center;
			position: relative;
			// padding-top: 200rpx;
			
			.avater {
				width: 112rpx;
				height: 112rpx;
				border-radius: 32rpx;
				border: 4rpx solid #FFFFFF;
			}
			
			.name {
				font-size: 36rpx;
				color: #FFFFFF;
				line-height: 52rpx;
				margin-top: 16rpx;
			}
			
			.post {
				font-size: 28rpx;
				color: rgba(255,255,255,0.65);
				line-height: 44rpx;
				margin-bottom: 16rpx;
			}
		}
		
		.bgimg {
			position: absolute;
			width: 100%;
			height: 720rpx;
			top: 0;
			
			image {
				width: 100%;
				height: 100%;
			}
		}
	}

	.logo {
		height: 200rpx;
		width: 200rpx;
		margin-top: 200rpx;
		margin-left: auto;
		margin-right: auto;
		margin-bottom: 50rpx;
	}

	.text-area {
		display: flex;
		justify-content: center;
	}

	.title {
		font-size: 36rpx;
		color: #8f8f94;
	}
	
	.user-wrap {
		width: 100%;
		position: relative;
	}
	
	.userbox {
		padding: 0 32rpx;
		margin-top: 72rpx;
		color: #333;
		/* #ifdef APP-PLUS*/
		padding-bottom: 280rpx;
		/* #endif */
	}
	
	.item-group {
		overflow: hidden;
		background: #fff;
		box-shadow: 0rpx 2rpx 24rpx 0rpx rgba(32, 144, 225, 0.18);
		border-radius: 24rpx;
		padding: 0rpx 30rpx;
		
		
		.item-cell {
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding: 30rpx 30rpx 30rpx 0;
			position: relative;
			border-bottom: 1px solid #eee;
			.label {
				font-size: 28rpx;
				width: 240rpx;
				.iconfont {
					margin-right: 20rpx;
				}
			}
			.content {
				font-size: 24rpx;
				color: #999;
			}
			&:after {
				display: block;
				position: absolute;
				right: 0rpx;
				top: 50%;
				content: '';
				width: 12rpx;
				height: 12rpx;
				margin-top: -6rpx;
				border-top: 2px solid #ddd;
				border-right: 2px solid #ddd;
				-webkit-transform: rotate(45deg);
				transform: rotate(45deg);
			}
		}
		.noArrow {
			&:after {
				content: '';
				display: none;
			}
		}
	
		.item-cell:last-child {
			border-bottom: none;
		}
		
		.label {
			display: flex;
		}
		
		.icon {
			width: 36rpx;
			height: 36rpx;
			margin-right: 16rpx;
		}
	}
	
</style>

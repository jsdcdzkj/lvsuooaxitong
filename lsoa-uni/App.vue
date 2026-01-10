<script setup>
	import {onLaunch, onShow, onHide} from "@dcloudio/uni-app"
	import {useAppStore} from "@/store/app.js"
	import {useUserStore} from "@/store/user.js"
	const appStore = useAppStore()
	const userStore = useUserStore()
	onLaunch((options) => {
		// 获取系统信息
		// #ifdef MP-WEIXIN
		appStore.updateAppConfig({
			...uni.getSystemInfoSync(),
			menuButtonRect: uni.getMenuButtonBoundingClientRect()
		})
		// #endif
		// #ifdef APP
			appStore.updateAppConfig({
				...uni.getSystemInfoSync(),
				menuButtonRect: {
					bottom: 56,
					height: 32,
					left: 320,
					right: 407,
					top: 24,
					width: 87,
				}
			})
		// #endif
		console.log('3333333333333是否登录', userStore.isLogin)
		if(!userStore.isLogin){
			uni.navigateTo({
				url:"/pages/login/index",
				"animationDuration":0,
				"animationType":"none"
			})
		}else{
			uni.switchTab({
				url: "/pages/index/index"
			})
			userStore.updateUserInfo()
		}
		
	})
	
	onShow((options) => {
		
			console.log('App Show',options)
	})
	
	onHide((options) => {
		console.log('App Hide',options)
	})
</script>

<style lang="scss">
	@import "./utils/wxx.scss";
	/*修改按钮样式 */
	/**primary**/
	button[disabled][type=primary] {
	    background-color: $uni-btn-primary-hover;
	}
	
	button[loading][type=primary]{
		background-color: $uni-btn-primary-hover;
	}
	button[type=primary]{
		background-color: $uni-color-primary;
		&.button-hover {
			background-color: $uni-btn-primary-hover;
		}
	}
	button {
		&.radius0{
			border-radius: 0px;
		}
	}
	
	/**default**/
	
	button {
		background-color: #fff;
		color: $uni-color-primary;
		
		&::after{
			border-width: 0px;
		}
	}
	
	page {
		background-color: #F8F8F8;
		// padding-bottom: constant(safe-area-inset-bottom); /*兼容 IOS<11.2*/
		// padding-bottom: env(safe-area-inset-bottom); /*兼容 IOS>11.2*/
	}
	
	.selectItem {
		font-size: 28rpx;
		color:rgba(0,0,0,0.85);
		width: 90%;
	}
	.selectChoose {
		pointer-events: none;
	}
	
	.card-header{
		width: 100%;
		height: 96rpx;
		border-bottom: 2rpx solid rgba(0,0,0,0.06);
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 20rpx;
		box-sizing: border-box;
	}
	.footer {
		padding: 20rpx 32rpx;
		padding-bottom: calc(20rpx + env(safe-area-inset-bottom)) !important;
		background-image: linear-gradient(to right, #fff, #fff);
		left: 0;
		width: 100%;
		box-sizing: border-box;
		position: fixed;
		bottom: 0;
		z-index: 2;
		display: flex;
		
		.btntype {
			flex:1;
			height: 72rpx;
			line-height: 72rpx;
			background: #409EFF;
			text-align: center;
			border: none;
			color: #fff;
			font-size: 32rpx;
			border-radius: 36rpx; 
		}
		
		.btnReject {
			background: #FEF0F0;
			color: #F56C6C;
			flex: 1;
			border: 1px solid #FEF0F0;
			margin-right: 32rpx;
		}
		.btnDefault {
			background: #fff;
			color: #409EFF;
			border: 1px solid #409EFF;
			flex: 1;
			margin-right: 32rpx;
		}

	}
	
	.common-foot {
		padding: 20rpx 32rpx;
		padding-bottom: 0!important;
		padding-bottom: constant(safe-area-inset-bottom)!important;
		padding-bottom: calc(20rpx + env(safe-area-inset-bottom)) !important;
		display: flex;
		align-items: center;
		.btntype {
			flex:1;
			height: 72rpx;
			line-height: 72rpx;
			background: #409EFF;
			text-align: center;
			border: none;
			color: #fff;
			font-size: 32rpx;
			border-radius: 36rpx; 
		}
		
		.btnReject {
			background: #FEF0F0;
			color: #F56C6C;
			flex: 1;
			margin-right: 32rpx;
		}
		
		.btnArchive {
			background: #67C23A;
			color: #fff;
			flex: 1;
			margin-left: 32rpx;
		}
		.btnDefault {
			background: #fff;
			color: #409EFF;
			border: 1px solid #409EFF;
			flex: 1;
			margin-right: 32rpx;
		}
	}
	
	.btn-common-primary {
		background: #409EFF;
		color: #fff;
		flex:1;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	
	.btn-common-default {
		background: #fff;
		color: #409EFF;
		flex:1;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	
</style>

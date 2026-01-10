<template>
	<view class="wrap">
		<div class="login-bg">
			<image src="@/static/login-bg.png" style="width: 100%;height: 100%" mode=""></image>
			<image class="icon-logo" src="@/static/icon-logo.png" mode=""></image>
			<image class="icon-move" src="@/static/icon-move.png" mode=""></image>
		</div>
		<view class="main-mod">
			<!-- <image src="@/static/icon-header.png" class="header-icon" mode="aspectFit"></image> -->
			<uni-forms ref="baseForm" :modelValue="formData" :rules="rules">
				<uni-forms-item name="account">
					<uni-easyinput prefixIcon="person" v-model="formData.account" placeholder="请输入账号" :clearable="false"
						:styles="styleInput" :placeholderStyle="placeholderStyle"></uni-easyinput>	
				</uni-forms-item>
				<uni-forms-item name="password">
					<uni-easyinput prefixIcon="locked" v-model="formData.password" type="password" placeholder="请输入密码" :clearable="false"
						:styles="styleInput" :placeholderStyle="placeholderStyle"></uni-easyinput>
				</uni-forms-item>
			</uni-forms>
			<!-- <view class="rest-pwd">
				<text></text>
				<text @click="$TOOL.toPage('/pages/usercenter/forgotpassword')">忘记密码</text>
				<text style="margin-left: 16rpx;" @click="$TOOL.toPage('/pages/login/register')">用户注册</text>
			</view> -->
			<view class="btn-item">
				<button class="btns" @click="submitLogin" :loading="loading">登录</button>
			</view>
			<!-- <view class="agreement">
				<checkbox-group @change="checkboxChange" class="label-text">
					<label>
						<checkbox style="transform:scale(0.7)" color="#1e95f4" value="1" :checked="isCheck == 1" /> 已阅读并同意</label>
					<navigator url="/pagesOne/usercenter/statement" class="blue">《服务协议》</navigator>
					和<navigator url="/pagesOne/usercenter/privacy" class="blue">《隐私政策》</navigator>
					
				</checkbox-group>
			</view>
			<view class="vistor">
				<text>
					选错了？
					<text class="link" @click="$TOOL.backPage()">重新选择</text>
				</text>
			</view> -->
		</view>
	</view>
</template>

<script>
import {mapActions} from "pinia"
import {useUserStore} from "@/store/user.js"
export default {
	data() {
		return {
			styleInput: {
				borderColor: '#f8f8f8',
				borderRadius: '12rpx',
				backgroundColor: '#f8f8f8',
				color: 'rgba(0,0,0,0.85)'
			},
			placeholderStyle: {
				color: 'rgba(0,0,0,0.25)',
			},
			userName: '',
			pwd: '',
			currentTab: true,
			password: '',
			closed: false,
			closed1: false,
			butStatus: false,
			disabled: true,
			loading: false,
			passwordShow: false,
			setvalue: [],
			setrange: [
				{
					value: 0,
					text: '记住密码'
				}
			],
			// 基础表单数据
			formData: {
				account: '',
				password: '',
				code: ''
				// openid: uni.getStorageSync('openid'),
			},
			path:'',
			// 校验规则
			rules: {
				account: {
					rules: [
						{
							required: true,
							errorMessage: '姓名不能为空'
						}
					]
				},
				password: {
					rules: [
						{
							required: true,
							errorMessage: '密码不能为空'
						}
					]
				}
			},
			backButtonPress:0
		};
	},
	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad(options) {
		if(options.path){
			this.path = decodeURIComponent(options.path)
		}
	},
	onShareAppMessage() {},
	onBackPress() {
		this.backButtonPress++;
		if (this.backButtonPress > 1) { 
			plus.runtime.quit();
		} else {
			plus.nativeUI.toast('再按一次退出应用');
		} 
		setTimeout(function() {
			this.backButtonPress = 0;
		}, 1000);
		return true;
	},
	methods: {
		...mapActions(useUserStore, ['login', 'updateUserInfo']),
		//登录
		submitLogin() {
			this.$refs.baseForm
				.validate()
				.then((res) => {
					uni.showLoading({
						title: '登录中...',
						icon: 'none'
					});
					this.loading = !this.loading;
					
					this.login(this.formData).then(res=>  {
						console.log('登录接口请求结果1111', res)
						this.updateUserInfo()
						uni.switchTab({
							url: "/pages/index/index"
						})
						uni.hideLoading();
						this.loading = !this.loading;
					}).catch(e => {
						console.log('55555555555555登录报错')
						this.loading = !this.loading;
						uni.showToast({
							icon:"none",
							title:e.msg
						})
					})
				})
				.catch((err) => {
					console.log('登录错误信息：', err);
				});
		}
	}
};
</script>
<style lang="scss" scoped>
	.wrap {
		position: relative;
		height: 100vh;
		width: 100vw;
		background:#fff;
	}
	.login-bg {
		position: relative;
		width: 100%;
		height: 482rpx;

		.icon-logo {
			width: 294rpx;
			height: 234rpx;
			position: absolute;
			top: 50%;
			left: 50%;
			z-index:1;
			transform: translate(-50%, -50%);
		}
		
		.icon-move {
			position: absolute;
			left: 0;
			bottom: -2px;
			width: 2110rpx;
			height: 50rpx;
			animation: stripmove 6s alternate infinite;
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
	.main-mod {
		position: relative;
		padding: 80rpx 40rpx 0;
		
		.header-icon {
			width: 100%;
		}
	}
	
	.btns {
		line-height: 48rpx;
		padding: 16rpx 0;
		text-align: center;
		font-size: 32rpx;
		color: #fff;
		opacity: 1;
		width: 100%;
		border: 0;
		box-shadow: 0 8px 16px rgba(10, 60, 120, 0.2);
		background: #409EFF;
		border-radius: 12px 12px 12px 12px;
		// border-radius: ;
	}
	
	.btn-item {
		margin-top: 60rpx;
		display: block;
	}
	
	
	.rest-pwd {
		width: 100%;
		// margin-left: 60rpx;
		text-align: right;
		display: flex;
		justify-content: space-between;
		margin-top: -10rpx;
		text {
			font-family: Source Han Sans CN, Source Han Sans CN;
			font-weight: 400;
			font-size: 28rpx;
			color: rgba(0,0,0,0.65);
			line-height: 44rpx;
		}
	}
	.vistor {
		font-size: 28rpx;
		font-family: Source Han Sans CN, Source Han Sans CN;
		font-weight: 400;
		color: rgba(0, 0, 0, 0.85);
		line-height: 44rpx;
		text-align: center;
		margin-top: 48rpx;
		text {
			// vertical-align: middle;
		}
		.link {
			color: #1e95f4;
			text-decoration-line: underline;
			text-decoration-color: #1e95f4;
			font-weight: 600;
		}
	}
	.agreement {
		font-size: 24rpx;
		color: #aaa;
		text-align: center;
		margin-top: 16rpx;
		
		.label-text {
			display: flex;
			justify-content: center;
			align-items: center;
			width: 100%;
		}
		.blue {
			color: #1e95f4;
		}
	}
	::v-deep {
		.uni-easyinput__content-input {
			height: 88rpx!important;
		}
	}
</style>

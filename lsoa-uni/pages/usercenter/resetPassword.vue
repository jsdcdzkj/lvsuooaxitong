<template>
	<view class="content">
		<hh-nav-bar image="../../../../static/icon-nav-bg.png" title="修改密码" :show-back="true" status-theme="black"></hh-nav-bar>
		<view class="resetpassBox">
			<uni-forms
				ref="baseForm"
				:modelValue="formData"
				:rules="rules"
				label-position="left"
				:border="true"
				label-width="160rpx"
			>
				<uni-forms-item label="旧密码" required name="password">
					<uni-easyinput
						type="password"
						v-model="formData.password"
						placeholder="请输入旧密码"
						:inputBorder="false"
						:style="styleInput"
					></uni-easyinput>
				</uni-forms-item>
				<uni-forms-item label="新密码" required name="newPass">
					<uni-easyinput
						type="password"
						v-model="formData.newPass"
						placeholder="请输入新密码"
						:inputBorder="false"
						:style="styleInput"
					></uni-easyinput>
				</uni-forms-item>
				<uni-forms-item label="确认密码" required name="confirmpass">
					<uni-easyinput
						type="password"
						v-model="formData.confirmpass"
						placeholder="请输入确认密码"
						:inputBorder="false"
						:style="styleInput"
					></uni-easyinput>
				</uni-forms-item>
			</uni-forms>
		</view>
		<view class="tips">提示：新密码需至少8个字符，并包含至少一个大写字母、一个小写字母、一个数字和一个特殊字符。</view>
		<view class="footer">
			<button ref="submitEl" class="btntype" @click="SubmitEvent">保存</button>
		</view>
	</view>
</template>

<script setup>
import api from "@/api/index.js"
import {useUserStore} from "@/store/user.js"
import { ref, reactive } from "vue"
const store = useUserStore()

// 文本框样式
const styleInput = {
	borderColor: '#fff',
	borderRadius: '8rpx',
	backgroundColor: '#fff',
}

const formData = reactive({
	password: '',
	newPass: '',
	confirmpass: ''
})

// 校验规则
const  rules = ref({
	password: {
		rules: [
			{
				required: true,
				errorMessage: '请输入原密码'
			}
		]
	},
	newPass: {
		rules: [
			{
				required: true,
				errorMessage: '请输入新密码'
			},
			{
				minLength: 4,
				maxLength: 20,
				errorMessage: '密码长度在 {minLength} 到 {maxLength} 个字符'
			}
		]
	},
	confirmpass: {
		rules: [
			{
				required: true,
				errorMessage: '请输入确认密码'
			},
			{
				validateFunction: function (rule, value, data, callback) {
					console.log('data', data);
					if (value != data.newPass) {
						callback('两次输入的密码必须相同');
					}
					return true;
				}
			}
		]
	}
})

// 提交代码
const baseForm = ref(null)
const SubmitEvent = () => {
		baseForm.value
			.validate()
			.then((res) => {
				uni.showLoading({
					title: '保存中...',
					icon: 'none'
				});
				api.user.editPassword({
						id: store.userInfo.id,
						oldPassword: formData.password,
						password: formData.newPass,
						confirmPassword: formData.newPass
					})
					.then((result) => {
						if (result.code === 0) {
							uni.hideLoading(); //隐藏
							uni.showToast({
								title: '修改成功，请重新登录',
								icon: 'none'
							});

							setTimeout(() => {
								uni.reLaunch({
									url: '../login/index'
								});
							}, 1000);
						}
					});
			})
			.catch((err) => {
				console.log('登录错误信息：', err);
			});
	}
</script>

<style lang="scss">
.content {
	position: relative;
	height: 100vh;
}
.resetpassBox {
	padding: 0rpx 30rpx;
	background: #fff;
	margin-top: 20rpx;
}

::v-deep {
	.uni-easyinput__content {
		background-color:#fff!important;
	}
}
.tips{
	color: rgba(0,0,0,0.45);
	font-size: 24rpx;
	line-height: 1.8;
	padding: 20rpx;
}
</style>

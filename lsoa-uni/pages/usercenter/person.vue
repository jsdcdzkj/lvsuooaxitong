<template>
	<view class="content">
		<hh-nav-bar image="../../../../static/icon-nav-bg.png" title="个人信息" :show-back="true" status-theme="black"></hh-nav-bar>
		<view class="resetpassBox">
			<uni-forms ref="baseForm" :modelValue="form" :rules="rules" label-position="left" :border="true" label-width="160rpx">
				<uni-forms-item label="用户头像" name="name">
					<view class="photoBox">
						<view class="photo" @click="handleUpImg">
							<view class="ptword">换</view>
							<image src="@/static/img_avater.png" v-if="!form.avatar"></image>
							<image :src="form.avatar" v-else></image>
							<!-- <image :src="formData.avatar"></image> -->
						</view>
					</view>
				</uni-forms-item>
				<!-- <uni-forms-item label="登录名" name="name">
					<uni-easyinput
						v-model="name"
						:inputBorder="false"
						readonly
						:style="styleInput"
					></uni-easyinput>
				</uni-forms-item> -->
				<uni-forms-item label="真实姓名" required name="realName">
					<uni-easyinput v-model="form.realName" :inputBorder="false" :style="styleInput" readonly></uni-easyinput>
				</uni-forms-item>
				<uni-forms-item label="联系方式" required name="phone">
					<uni-easyinput v-model="form.phone" :inputBorder="false" :style="styleInput"></uni-easyinput>
				</uni-forms-item>
			</uni-forms>
		</view>
		<view class="footer">
			<button class="btntype" @click="SubmitEvent">保存</button>
		</view>
	</view>
</template>

<script setup>
import api from '@/api/index.js';
import { useUserStore } from '@/store/user.js';
import { ref, reactive } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
const store = useUserStore();
const form = reactive({
	avatar: '',
	phone: '',
	realName: ''
});

onLoad(() => {
	form.id = store.userInfo.id;
	form.avatar = store.userInfo.avatar;
	form.phone = store.userInfo.phone;
	form.realName = store.userInfo.realName;
});

// 保存
const handleUpImg = () => {
	// 点击图片区域，选择图片并上传
	uni.chooseImage({
		// 选择图片
		count: 1,
		success: (res) => {
			// 图片选择成功的回调（必传），会返回一个对象
			form.avatar = res.tempFilePaths[0]; // 用于更新视图
			api.common.uploadAvatar(form.avatar).then(val => {
				console.log('22222222222222val', val)
				form.avatar = val.data
			})
			// uni.uploadFile({
			// 	url: config.VUE_APP_API_BASEURL + '/fileUpd.json', // 请求地址
			// 	filePath: res.tempFilePaths[0], // 临时文件路径
			// 	fileType: 'image',
			// 	name: 'file', // 文件对应的key值
			// 	header: {
			// 		Authorization: uni.getStorageSync('accessToken')
			// 	},
			// 	formData: {
			// 		// 请求传参Userid
			// 		userId: uni.getStorageSync('userInfo').id
			// 	},
			// 	success: (res) => {
			// 		// 成功后的回调
			// 		console.log('修改成功');
			// 	}
			// });
		}
	});
};

// 保存
const baseForm = ref(null);
const SubmitEvent = () => {
	baseForm.value
		.validate()
		.then((res) => {
			uni.showLoading({
				title: '保存中...',
				icon: 'none'
			});
			api.user.saveUser(form).then((result) => {
				if (result.code === 0) {
					uni.hideLoading(); //隐藏
					uni.showToast({
						title: '修改成功',
						icon: 'none'
					});
					store.updateUserInfo();

					setTimeout(() => {
						uni.reLaunch({
							url: './index'
						});
					}, 1000);
				}
			});
		})
		.catch((err) => {
			console.log('登录错误信息：', err);
		});
};
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
.avater {
	width: 96rpx;
	height: 96rpx;
	border-radius: 24rpx;
}
::v-deep {
	.uni-easyinput__content {
		background-color: #fff !important;
	}
}
	.photoBox {
		float: right;
		position: relative;
		padding-right: 80rpx;
		.photo {
			width: 96rpx;
			height: 96rpx;
			border-radius: 24rpx;
			position: relative;
			overflow: hidden;
			cover-view {
				width: 60rpx;
				height: 40rpx;
				text-align: center;
				font-size: 24rpx;
				color: #fff;
				background-color: rgba(107, 96, 99, 0.4);
				position: absolute;
				left: 50%;
				top: 50%;
				transform: translate(-50%, 0);
			}
			.ptword {
				width: 96rpx;
				line-height: 30rpx;
				text-align: center;
				font-size: 24rpx;
				color: #fff;
				background-color: rgba(107, 96, 99, 0.4);
				position: absolute;
				left: 0%;
				bottom: 0;
				z-index: 1;
			}
			image {
				width: 100%;
				height: 100%;
				border-radius: 24rpx;
			}
		}
	}
</style>

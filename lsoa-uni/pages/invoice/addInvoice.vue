<template>
	<view class="add-content">
		<hh-nav-bar image="../../../../static/icon-nav-bg.png" title="开票" :show-back="true" status-theme="black"></hh-nav-bar>
		<view class="searchBox">
			<view class="progress">
				<view class="step-wrap">
					<view class="step1" :class="{ 'step1-active': step == 1 }">
						<text class="num">1.</text>
						<text class="name">开票人信息</text>
					</view>
					<view class="step2" :class="{ 'step2-over': step > 2, 'step2-active': step == 2 }">
						<text class="num">2.</text>
						<text class="name">发票类型信息</text>
					</view>
					<view class="step2" :class="{ 'step2-over': step > 3, 'step2-active': step == 3 }">
						<text class="num">3.</text>
						<text class="name">联系人信息</text>
					</view>
					<view class="step2" :class="{ 'step2-over': step > 4, 'step2-active': step == 4 }">
						<text class="num">4.</text>
						<text class="name">收件人信息</text>
					</view>
				</view>
			</view>
			<view class="divider-stripe"></view>
			<uni-forms ref="baseForm" :modelValue="formData" label-position="left" :border="true" label-width="250rpx">
				<!-- 基本信息 -->
				<view v-show="step == 1">
					<uni-forms-item label="抬头类型" required name="titleType">
						<radio-group @change="changetitleType" class="serve-wrap">
							<label class="radio">
								<radio value="1" :checked="formData.titleType == 1" color="#409EFF" style="transform: scale(0.7)" />
								个人
							</label>
							<label class="radio">
								<radio value="2" :checked="formData.titleType == 2" color="#409EFF" style="transform: scale(0.7)" />
								单位
							</label>
						</radio-group>
					</uni-forms-item>
					<uni-forms-item label="抬头名称" required name="title" :rules="[{ required: true, errorMessage: '请输入' }]">
						<input class="selectItem" v-model="formData.title" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					<view class="divider-stripe"></view>
					<uni-forms-item label="单位税号" name="number" v-if="formData.titleType == 2">
						<input v-model="formData.number" placeholder="请输入" class="selectItem" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					<uni-forms-item label="注册地址" name="registeredAdd" v-if="formData.titleType == 2">
						<input v-model="formData.registeredAdd" placeholder="请输入" class="selectItem" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					<uni-forms-item label="注册电话" name="registerTel" v-if="formData.titleType == 2">
						<input v-model="formData.registerTel" placeholder="请输入" class="selectItem" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					<uni-forms-item label="开户银行" name="bankDeposit" v-if="formData.titleType == 2">
						<input v-model="formData.bankDeposit" placeholder="请输入" class="selectItem" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					<uni-forms-item label="银行账号" name="bankAccount" v-if="formData.titleType == 2">
						<input v-model="formData.bankAccount" placeholder="请输入" class="selectItem" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
				</view>
				<!-- 发票类型信息 -->
				<view v-show="step == 2">
					<uni-forms-item label="发票类型" required name="invoiceType">
						<radio-group @change="changeInvoiceType" class="serve-wrap">
							<label class="radio">
								<radio value="1" :checked="formData.invoiceType == 1" color="#409EFF" style="transform: scale(0.7)" />
								增值税发票
							</label>
							<label class="radio">
								<radio value="2" :checked="formData.invoiceType == 2" color="#409EFF" style="transform: scale(0.7)" />
								专用发票
							</label>
						</radio-group>
					</uni-forms-item>
					<uni-forms-item label="开票金额" required name="invoiceMoney" :rules="[{ required: true, errorMessage: '请输入' }]">
						<input v-model="formData.invoiceMoney" class="selectItem" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					<uni-forms-item label="开票日期" name="invoiceTime">
						<uni-datetime-picker :border="false" v-model="formData.invoiceTime" type="date" @maskClick="maskClick" />
					</uni-forms-item>
					<uni-forms-item label="发票内容" name="content">
						<input v-model="formData.content" type="textarea" placeholder="请输入" class="selectItem" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
				</view>
				<!-- 联系人信息 -->
				<view v-show="step == 3">
					<uni-forms-item label="联系人姓名" name="contactPerson">
						<input v-model="formData.contactPerson" class="selectItem" placeholder="请输入"  placeholder-style="color:#999;font-size: 28rpx"/>
					</uni-forms-item>
					<uni-forms-item label="联系电话" required name="contactPhone" :rules="[
						{ required: true, errorMessage: '请输入' },
						{
							validator: function (rule, value, callback) {
								if (/^1[3456789]\d{9}$/.test(value) == false && /^((0\d{2,3})-)?(\d{7,8})$/.test(value) == false) {
									callback(new Error('请输入正确的联系方式'));
								} else {
									callback();
								}
							},
							trigger: 'blur'
						}
						]">
						<input v-model="formData.contactPhone" class="selectItem" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					<uni-forms-item label="电子邮箱" name="mail">
						<input v-model="formData.mail" placeholder="请输入" class="selectItem" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
				</view>
				<view v-show="step == 4">
					<uni-forms-item label="收件人地址" name="address">
						<input v-model="formData.address" placeholder="请输入" class="selectItem" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					<uni-forms-item label="收件人" name="addressee">
						<input v-model="formData.addressee" placeholder="请输入" class="selectItem" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					<uni-forms-item label="联系电话" name="addresseePhone">
						<input v-model="formData.addresseePhone" placeholder="请输入" class="selectItem" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
				</view>
			</uni-forms>
		</view>
		<view class="common-foot">
			<button v-if="step > 1" class="btntype btnDefault" @click="handlePre">上一步</button>
			<button class="btntype" :disabled="disbabled" :loading="disbabled" @click="handleNext">{{ step > 3 ? '提交' : '下一步' }}</button>
		</view>
	</view>
</template>

<script setup>
import { reactive, ref } from 'vue';
import api from '@/api/index.js';
import { useUserStore } from '@/store/user.js';
import { onLoad } from '@dcloudio/uni-app';
const store = useUserStore();

let formData = ref({
	titleType: 1,
	invoiceType: 1
});
onLoad((option) => {
	console.log('33333333333333333333option', option)
	formData.value.caseId = option.caseId;
	if (option.status == 4) {
		
		api.invoice.getEntity({ id: option.id }).then((res) => {
			formData.value = res.data;
		});
	} else if(option.status == 3) {
		api.invoice.getInvoiceList({id: option.caseId}).then(res => {
			const { titleType, title, number, invoiceType } = res.data[0]
			formData.value.titleType =titleType
			formData.value.title =title
			formData.value.number =number
			formData.value.invoiceType =invoiceType
		})
	}
});


// 案件的步骤
const step = ref(1);
const styleInput = reactive({
	borderColor: '#fff',
	borderRadius: '0',
	backgroundColor: '#fff'
});
const baseForm = ref(null);
const disbabled = ref(false);
const handleNext = () => {
	if (step.value == 4) {
		baseForm.value
			.validate()
			.then((res) => {
				if (isNaN(Number(formData.value.invoiceMoney))) {
					return uni.showToast({
						icon: 'none',
						title: '开票金额请输入数字'
					});
				}
				var patrn = /^1[3456789]\d{9}$/; //手机电话
				var zjdh = /^((0\d{2,3})-)?(\d{7,8})$/; //座机电话
				if (patrn.test(formData.value.contactPhone) == false && zjdh.test(formData.value.contactPhone) == false) {
					return uni.showToast({
						icon: 'none',
						title: '请输入正确的联系方式!'
					});
				}
				disbabled.value = true;
				console.log('333333333333', formData.value);
				formData.value.invoiceMoney = Math.abs(Number(formData.value.invoiceMoney)).toFixed(2);
				api.invoice
					.saveOrUpdate(formData.value)
					.then((res) => {
						console.log('22222222222', res);
						if (res.code == 0) {
							uni.showToast({
								icon: 'none',
								title: '案件开票申请成功！'
							});
							setTimeout(() => {});
							uni.navigateTo({
								url: '/pages/caseManage/successPage?title=案件开票&msg=案件开票申请成功'
							},2000);
						}
					})
					.finally(() => {
						disbabled.value = false;
					});
			})
			.catch((err) => {
				console.log('err', err);
				uni.showToast({
					icon: 'none',
					title: '请检查是否有未填字段！'
				});
			});
		return;

		return;
	}
	step.value += 1;
};

const handlePre = () => {
	step.value -= 1;
};


// 案件选择
const serveIndex = ref(1);
const changeServe = (e) => {
	serveIndex.value = e;
	formData.serveType = e;
};

//案件类型
const urgencyValue = ref(1);
const urgencys = reactive([]);
const selectEmergent = (e) => {
	urgencyValue.value = e.detail.value;
	const { text, value } = urgencys[e.detail.value];
	formData.urgency.id = value;
	formData.urgency.value = text;
};
// 抬头类型
const changetitleType = (e) => {
	formData.value.titleType = e.detail.value;
};

// 开票类型
const changeInvoiceType = (e) => {
	formData.value.invoiceType = e.detail.value;
};
</script>

<style scoped lang="scss">
@import '../../utils/step.scss';
</style>

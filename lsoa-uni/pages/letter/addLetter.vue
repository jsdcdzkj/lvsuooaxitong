<template>
	<view class="add-content">
		<hh-nav-bar image="../../../../static/icon-nav-bg.png" title="开函" :show-back="true" status-theme="black"></hh-nav-bar>
		<view class="searchBox">
			<!-- <view class="progress">
				<view class="step-wrap">
					<view class="step1" :class="{ 'step1-active': step == 1 }">
						<text class="num">1.</text>
						<text class="name">委托人信息</text>
					</view>
					<view class="step2" :class="{ 'step2-over': step > 2, 'step2-active': step == 2 }">
						<text class="num">2.</text>
						<text class="name">函件信息</text>
					</view>
					<view class="step2" :class="{ 'step2-over': step > 3, 'step2-active': step == 3 }">
						<text class="num">3.</text>
						<text class="name">收件人信息</text>
					</view>
					<view class="step2" :class="{ 'step2-over': step > 4, 'step2-active': step == 4 }">
						<text class="num">4.</text>
						<text class="name">格式要求</text>
					</view>
					<view class="step2" :class="{ 'step2-over': step > 5, 'step2-active': step == 5 }">
						<text class="num">5.</text>
						<text class="name">开函文件</text>
					</view>
				</view>
			</view> -->
			<!-- <view class="divider-stripe"></view> -->
			<uni-forms ref="baseForm" :modelValue="formData" :rules="rules" label-position="left" :border="true" label-width="250rpx">
				<!-- 基本信息 -->
				<view v-if="step == 1">
					<uni-forms-item label="委托人" required name="personType">
						<radio-group @change="changePersonType" class="serve-wrap">
							<!-- 1自然人 2法人 3个体工商户 4受害人 5顾问单位 -->
							<label class="radio"><radio value="1" :disabled="formData.letter == 3 || formData.letter == 4" :checked="formData.personType == 1" color="#409EFF" style="transform:scale(0.7)" />自然人</label>
							<label class="radio"><radio value="2" :disabled="formData.letter == 3 || formData.letter == 4" :checked="formData.personType == 2" color="#409EFF" style="transform:scale(0.7)" />法人</label>
							<label class="radio"><radio value="3" :disabled="formData.letter == 3 || formData.letter == 4" :checked="formData.personType == 3" color="#409EFF" style="transform:scale(0.7)" />个体工商户</label>
							<label class="radio"><radio value="4" :disabled="formData.letter == 3 || formData.letter == 4" :checked="formData.personType == 4" color="#409EFF" style="transform:scale(0.7)" />受害人</label>
							<label class="radio"><radio value="5" :disabled="formData.letter == 3 || formData.letter == 4" :checked="formData.personType == 5" color="#409EFF" style="transform:scale(0.7)" />顾问单位</label>
						</radio-group>
					</uni-forms-item>
					<uni-forms-item label="姓名" required name="name" v-if="formData.personType == 1 || formData.personType == 4" :rules="[{ required: true, errorMessage: '请输入' }]">
						<input class="selectItem" v-model="formData.name" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					<uni-forms-item label="身份证号" name="code" v-if="formData.personType == 1 || formData.personType == 4">
						<input class="selectItem" v-model="formData.code" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					<uni-forms-item label="单位名称" required name="name" v-if="!(formData.personType == 1 || formData.personType == 4)" :rules="[{ required: true, errorMessage: '请输入' }]">
						<input class="selectItem" v-model="formData.name" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					
					<uni-forms-item label="统一代码" name="code" v-if="!(formData.personType == 1 || formData.personType == 4)">
						<input v-model="formData.code" class="selectItem" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					<uni-forms-item label="联系方式" required name="phone" :rules="[
						{ required: true, errorMessage: '请输入' },
						{
							validator: function (rule, value, callback) {
								if (/^1[3456789]\d{9}$/.test(value) == false && /^((0\d{2,3})-)?(\d{7,8})$/.test(value) == false) {
									callback(new Error('请输入正确的联系方式'));
								} else {
									callback();
								}
							},
							trigger: 'blur',
						}
					]">
						<input v-model="formData.phone" class="selectItem" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
				</view>
				<!-- <view v-if="step == 2">
					<uni-forms-item label="函件类型" required name="type">
						<radio-group @change="changeType" class="serve-wrap">
							<label class="radio"><radio value="1" :checked="formData.type == 1" color="#409EFF" style="transform:scale(0.7)" />刑函</label>
							<label class="radio"><radio value="2" :checked="formData.type == 2" color="#409EFF" style="transform:scale(0.7)" />仲裁函</label>
							<label class="radio"><radio value="3" :checked="formData.type == 3" color="#409EFF" style="transform:scale(0.7)" />民函件</label>
							<label class="radio"><radio value="4" :checked="formData.type == 4" color="#409EFF" style="transform:scale(0.7)" />刑函(法援)</label>
							<label class="radio"><radio value="5" :checked="formData.type == 5" color="#409EFF" style="transform:scale(0.7)" />破产清算</label>
						</radio-group>
					</uni-forms-item>
					<uni-forms-item label="相关案件" name="caseName">
						<input v-model="formData.caseName" class="selectItem" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					<uni-forms-item label="事项编码" name="caseCode">
						<input v-model="formData.caseCode" class="selectItem" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					<uni-forms-item label="用印类型" :name="['printType', 'value']" required :rules="[{ required: true, errorMessage: '请输入' }]">
						<picker mode="selector" @change="selectPrintType" :value="printTypeValue" :range="sealTypeList" range-key="text">
							<input type="text"  placeholder-style="color:#999;font-size: 28rpx" v-model="formData.printType.value" placeholder="请选择" class="selectItem selectChoose" disabled />
						</picker>
					</uni-forms-item>
					<uni-forms-item label="用途说明" name="useing">
						<input v-model="formData.useing" class="selectItem" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
				</view>
				<view v-if="step == 3">
					<uni-forms-item label="收件方" name="addressType">
						<radio-group @change="changeAddressType" class="serve-wrap">
							<label class="radio"><radio value="1" :checked="formData.addressType == 1" color="#409EFF" style="transform:scale(0.7)" />个人</label>
							<label class="radio"><radio value="2" :checked="formData.addressType == 2" color="#409EFF" style="transform:scale(0.7)" />单位</label>
						</radio-group>
					</uni-forms-item>
					<uni-forms-item label="姓名" name="addressee" v-if="formData.addressType == 1">
						<input v-model="formData.addressee" class="selectItem" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					<uni-forms-item label="单位名称"name="addressee" v-if="formData.addressType == 2">
						<input class="selectItem" v-model="formData.addressee" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					<uni-forms-item label="收件地址"name="address">
						<input class="selectItem" v-model="formData.address" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx"/>
					</uni-forms-item>
					<uni-forms-item label="联系方式"name="addresseePhone">
						<input v-model="formData.addresseePhone" placeholder="请输入" class="selectItem" placeholder-style="color:#999;font-size: 28rpx"/>
					</uni-forms-item>
				</view>
				<view v-if="step == 4">
					<uni-forms-item label="特殊格式要求" name="specialDemand">
						<input class="selectItem" v-model="formData.specialDemand" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx"/>
					</uni-forms-item>
					<uni-forms-item label="紧急程度" :name="['level', 'value']" required :rules="[{ required: true, errorMessage: '请输入' }]">
						<picker mode="selector" @change="selectLevelType" :value="levelTypeValue" :range="levelTypeList" range-key="text">
							<input type="text"  placeholder-style="color:#999;font-size: 28rpx" v-model="formData.level.value" placeholder="请选择" class="selectItem selectChoose" disabled />
						</picker>
					</uni-forms-item>
					<uni-forms-item label="份数要求" name="num">
						<input v-model="formData.num" placeholder="请输入" class="selectItem" placeholder-style="color:#999;font-size: 28rpx"/>
					</uni-forms-item>
					<uni-forms-item label="送达方式" name="sendType">
						<input v-model="formData.sendType" class="selectItem" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx"/>
					</uni-forms-item>
				</view>
				<view class="client-wrap annex-wrap" v-if="step == 5">
					<view class="">
						<UploadFile ref="uploadFile" @changeFile="handleUpload" :param="{'bizType': 1}">
							<view class="card plus">
								<view class="icon-bg">
									<uni-icons type="plusempty" size="18" color="rgba(0,0,0,0.45)"></uni-icons>
								</view>
								<text>支持扩展名：{{ uploadType }}</text>
								<view class="file-btn">上传</view>
							</view>
						</UploadFile>
						<view class="card" v-for="(item, index) in fileList" :key="index">
							<view class="file-card">
								<image class="icon-file" src="@/static/icon-file-list.png" mode=""></image>
								<text class="name">{{ item.originalFilename }}</text>
							</view>
							<view class="file-btn">
								<view class="del btn" @click="handleDelete(item, key)">删除</view>
								<view class="preview btn" @click="handlePreview(item)">预览</view>
							</view>
						</view>
					</view>
				</view> -->
			</uni-forms>
		</view>
		<view class="common-foot">
			<button class="btntype btnDefault" @click="handlePre">取消</button>
			<button class="btntype" :disabled="disbabled" :loading="disbabled" @click="handleNext">提交</button>
		</view>
	</view>
</template>

<script setup>
import { reactive, ref } from 'vue';
import UploadFile from '@/components/c-upload-file.vue';
import api from '@/api/index.js';
import { useUserStore } from '@/store/user.js';
import {onLoad} from "@dcloudio/uni-app"
const store = useUserStore();

const formData = ref({
	personType: 1,
	type: 1,
	addressType: 1,
	printType: {
		value: '',
		id: ''
	},
	level: {
		value: '',
		id: ''
	},
	name:'',
	code:'',
	phone:'',
});

let clientInfo = {}

const fileList = ref()
onLoad((option) => {
	formData.value.caseId = option.caseId
	formData.value.letter = option.status
	api.letter.getClient({caseId: option.caseId}).then(res => {
		console.log('测试委托人数据', res)
		clientInfo = {
			person: res.data.person || {},
			victim: res.data.victim || {},
			corporation: res.data.corporation|| {},
			households: res.data.households || {},
			customer: res.data.customer || {},
		}
		
		//修改
		if(option.status == 4) {
			api.letter.getEntity({id: option.id}).then(res => {
			   formData.value = res.data
			   formData.value.letter = option.status
			   fileList.value = res.data.fileList
			   formData.value.printType = {
				   value:res.data.printTypeName,
				   id:res.data.printType
			   }
			   formData.value.level = {
				   value: res.data.level,
				   id: res.data.level
			   }
				if(formData.value.personType == 2) { 
				    clientInfo.corporation.name = formData.value.name
					clientInfo.corporation.code = formData.value.code
					clientInfo.corporation.phone = formData.value.phone
				 } else if (formData.value.personType == 3) {
					clientInfo.households.name = formData.value.name
					clientInfo.households.code = formData.value.code
					clientInfo.households.phone = formData.value.phone
				 }else if (formData.value.personType == 5) {
					clientInfo.customer.name = formData.value.name
					clientInfo.customer.code = formData.value.code
					clientInfo.customer.phone = formData.value.phone
				 }else if(formData.value.personType == 1){
					clientInfo.person.name = formData.value.name
					clientInfo.person.code = formData.value.code
				 }else{
					clientInfo.victim.name = formData.value.name
					clientInfo.victim.code = formData.value.code
				 }
				 console.log('333333333333333option', option.status, formData.value.personType, formData.value)
				changePersonType({detail:{value : formData.value.personType}})
			   
		   })
		} else if(option.status == 3) {
			// 案件详情
			api.caseManage.getEntity({id: option.caseId}).then(res => {
				if(res.code == 0) {
					const { caseName, caseNum} = res.data
					formData.value.caseName  = caseName
					formData.value.caseCode  = caseNum
				}
				
			})
			api.letter.getLetterList({id: option.caseId}).then(res => {
				const {personType, name, code, phone, type} = res.data[0]
				formData.value.personType  = personType
				formData.value.name  = name
				formData.value.code  = code
				formData.value.phone  = phone
				formData.value.type  = type
			})
		} else {
			
			// 案件详情
			api.caseManage.getEntity({id: option.caseId}).then(res => {
				if(res.code == 0) {
					const { caseName, caseNum} = res.data
					formData.value.caseName  = caseName
					formData.value.caseCode  = caseNum
				}
				
			})
		}
		 console.log('99999999999999formData.value.letter', formData.value)
		
		changePersonType({detail:{value : 1}})
	})

})
// 案件的步骤
const step = ref(1);
const rules = reactive({});
const styleInput = reactive({
	borderColor: '#fff',
	borderRadius: '0',
	backgroundColor: '#fff',
});

const baseForm = ref(null)
const disbabled = ref(false)
const handleNext = () => {
	console.log('333333333333', formData)
	baseForm.value
			.validate()
			.then((res) => {
				console.log('4444444444444', formData)
				// 身份证号验证
				// if (!formData.value.code) {
				// 	return uni.showToast({
				// 		icon: 'none',
				// 		title: '请输入身份证号'
				// 	});
				// }
				
				// if (!(formData.value.personType == 1 || formData.value.personType == 4) && !formData.value.code) {
				// 	return uni.showToast({
				// 		icon: 'none',
				// 		title: '请输入单位统一代码'
				// 	});
				// }
				
				// if(!(formData.value.personType == 1 || formData.value.personType == 4) && !formData.value.phone) {
				// 	return uni.showToast({
				// 		icon: 'none',
				// 		title: '请输入联系方式'
				// 	});
				// }
				
				// if(!formData.value.type) {
				// 	return uni.showToast({
				// 		icon: 'none',
				// 		title: '请选择函件类型'
				// 	});
				// }
				// if(!formData.value.caseName) {
				// 	return uni.showToast({
				// 		icon: 'none',
				// 		title: '请输入相关案件'
				// 	});
				// }
				// if (!formData.value.caseCode) {
				// 	return uni.showToast({
				// 		icon: 'none',
				// 		title: '请输入事项编码'
				// 	});
				// }
				// if(!formData.value.printType1) {
				// 	return uni.showToast({
				// 		icon: 'none',
				// 		title: '请选择用印方式'
				// 	});
				// }
				
				// if(!formData.value.level) {
				// 	return uni.showToast({
				// 		icon: 'none',
				// 		title: '请选择紧急程度'
				// 	});
				// }
				
				// if(!formData.value.addressType) {
				// 	return uni.showToast({
				// 		icon: 'none',
				// 		title: '请选择收件方'
				// 	});
				// }

				// if(!formData.value.addressee) {
				// 	return uni.showToast({
				// 		icon: 'none',
				// 		title: '请输入收件人信息'
				// 	});
				// }
				
				// if(!formData.value.address) {
				// 	return uni.showToast({
				// 		icon: 'none',
				// 		title: '请输入收件地址'
				// 	});
				// }
				
				// if (/^1[345678]\d{9}$/.test(formData.value.addresseePhone) == false && /^((0\d{2,3})-)?(\d{7,8})$/.test(formData.value.addresseePhone) == false) {
				// 	return uni.showToast({
				// 		icon: 'none',
				// 		title: '请输入正确的收件人联系方式'
				// 	});
				// }
				disbabled.value = true
				api.letter.saveOrUpdate({...formData.value, printType: formData.value.printType1, level:formData.value.level.value }).then(res => {
					console.log('22222222222', res)
					if(res.code == 0) {
						uni.showToast({
							icon:"none",
							title:'案件开函申请成功！'
						})
						setTimeout(() => {})
						uni.navigateTo({
							url: '/pages/caseManage/successPage?title=案件开函&msg=案件开函申请成功'
						}, 2000);
					}
				}).finally(() => {
					disbabled.value = false
				})
			}).catch(err =>{
				console.log('err', err)
				disbabled.value = false
				uni.showToast({
					icon:"none",
					title:'请检查是否有未填字段！'
				})
			})

}

const uploadFile = ref(null)
const handlePre = () => {
	uni.navigateBack()
}

//用印类型
// const printTypeValue = ref();
// const selectPrintType = (e) => {
// 	printTypeValue.value = e.detail.value;
// 	const { text, value } = sealTypeList.value[e.detail.value];
// 	formData.value.printType.id = value;
// 	formData.value.printType.value = text;
// };

// //紧急程度
// const levelTypeValue = ref();
// const selectLevelType = (e) => {
// 	levelTypeValue.value = e.detail.value;
// 	const { text, value } = levelTypeList.value[e.detail.value];
// 	formData.value.level.id = value;
// 	formData.value.level.value = text;
// };

// 委托人类型
const changePersonType = (e) => {
	console.log('22222222222222222e', e)
	let index = e.detail.value
	formData.value.personType = `${index}`
	if(index == 1) {
		const {name, code,phone} = clientInfo.person
		formData.value.name = name
		formData.value.code = code
	}else if(index == 2){
		const {name, code,phone} = clientInfo.corporation
		formData.value.name = name
		formData.value.code = code
		formData.value.phone = phone
	} else if (index == 3) {
		const {name, code,phone} = clientInfo.households
		formData.value.name = name
		formData.value.code = code
		formData.value.phone = phone
	} else if (index == 4) {
		const {name, code, sex,phone} = clientInfo.victim
		formData.value.name = name
		formData.value.code = code
	} else {
		const {name, code,phone} = clientInfo.customer
		formData.value.name = name
		formData.value.code = code
		formData.value.phone = phone
	}
}

// const changeType = (e)  => {
// 	formData.value.type = e.detail.value
// }

// const changeAddressType= (e)  => {
// 	formData.value.addressType = e.detail.value
// }
// 文件预览
// const handlePreview = (item) => {
// 	if(item.fileType.indexOf('image') > -1) {
// 		uni.previewImage({
// 			urls:[item.fileUrl]
// 		})
// 	} else {
// 		api.common.previewFile(item.fileUrl)
// 	}
	
// }
</script>

<style scoped lang="scss">
@import '../../utils/step.scss';
</style>

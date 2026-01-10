<template>
	<view class="add-content">
		<hh-nav-bar image="../../../../static/icon-nav-bg.png" title="新增案件" :show-back="true" status-theme="black"></hh-nav-bar>
		<view class="searchBox">
			<view class="progress">
				<view class="step-wrap">
					<view class="step1" :class="{ 'step1-active': step == 1 }">
						<text class="num">1.</text>
						<text class="name">委托当事人</text>
					</view>
					<view class="step2" :class="{ 'step2-over': step > 2, 'step2-active': step == 2 }">
						<text class="num">2.</text>
						<text class="name">委托人信息</text>
					</view>
					<view class="step2" :class="{ 'step2-over': step > 3, 'step2-active': step == 3 }">
						<text class="num">3.</text>
						<text class="name">对方当事人</text>
					</view>
					<view class="step2" v-if="customerValueFloat.length == 0" :class="{ 'step2-over': step > 4, 'step2-active': step == 4 }">
						<text class="num">4.</text>
						<text class="name">附件管理</text>
					</view>
					<view v-if="!formData.isEdit && customerValueFloat.length == 0" class="step2" :class="{ 'step2-over': step > 5, 'step2-active': step == 5 }">
						<text class="num">5.</text>
						<text class="name">开票</text>
					</view>
				</view>
			</view>
			<view class="divider-stripe"></view>
			<uni-forms ref="baseForm" :modelValue="formData" :rules="rules" label-position="left" :border="true" label-width="250rpx">
				<!-- 基本信息 -->
				<view v-if="step == 2">
					<!-- <uni-forms-item label="案件名称" required :rules="[{ required: true, errorMessage: '请输入' }]" name="caseName">
						<input v-model="formData.caseName"  class="selectItem" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item> -->
					<uni-forms-item label="案件类型" :name="['caseType', 'value']" required>
						<picker mode="selector" @change="selectCaseType" :value="caseTypeValue" :range="caseTypeList" range-key="text">
							<input type="text" v-model="formData.caseType.value"  class="selectItem selectChoose" placeholder="请选择" disabled placeholder-style="color:#999;font-size: 28rpx" />
						</picker>
					</uni-forms-item>
					<uni-forms-item label="业务类型" :name="['businessType', 'value']" required :rules="[{ required: true, errorMessage: '请选择' }]">
						<picker mode="selector" @change="selectBusiness" :value="businessTypeValue" :range="businessList" range-key="text">
							<input type="text" v-model="formData.businessType.value" placeholder="请选择" class="selectItem selectChoose" disabled placeholder-style="color:#999;font-size: 28rpx" />
						</picker>
					</uni-forms-item>
					<uni-forms-item label="费用(元)" v-if="customerValueFloat.length == 0" name="money" required :rules="[{ required: true, errorMessage: '请输入' }]">
						<input v-model="formData.money" placeholder="请输入"  class="selectItem" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					<uni-forms-item label="标的额" required :rules="[{ required: true, errorMessage: '请输入' }]" name="subjectAmount">
						<input v-model="formData.subjectAmount" placeholder="请输入" class="selectItem" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					<uni-forms-item label="诉讼阶段" :name="['stage', 'value']" required :rules="[{ required: true, errorMessage: '请选择' }]">
						<picker mode="selector" @change="selectLitigation" :value="stageValue" :range="litigationList" range-key="text">
							<input type="text" v-model="formData.stage.value" placeholder="请选择" class="selectItem selectChoose" disabled  placeholder-style="color:#999;font-size: 28rpx"/>
						</picker>
					</uni-forms-item>
					<uni-forms-item label="案件状态" :name="['status', 'value']" required :rules="[{ required: true, errorMessage: '请选择' }]">
						<picker mode="selector" @change="selectstatus" :value="statusValue" :range="statusList" range-key="text" style="width:90%">
							<input type="text" v-model="formData.status.value" placeholder="请选择" class="selectItem selectChoose" disabled placeholder-style="color:#999;font-size: 28rpx;" />
						</picker>
					</uni-forms-item>
					<uni-forms-item label="办案机构" required :rules="[{ required: true, errorMessage: '请输入' }]" name="court">
						<input v-model="formData.court"  class="selectItem" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
					<uni-forms-item label="特殊审批" name="riskOrFree">
						<radio-group @change="changeRiskType" class="serve-wrap">
							<label class="radio"><radio value="1" :checked="formData.riskOrFree == 1" color="#409EFF" style="transform:scale(0.7)" />风险代理</label>
							<label class="radio"><radio value="2" :checked="formData.riskOrFree == 2" color="#409EFF" style="transform:scale(0.7)" />费用减免</label>
						</radio-group>
					</uni-forms-item>
					<uni-forms-item label="案由" required :rules="[{ required: true, errorMessage: '请输入' }]" name="subjectOfAction">
						<input v-model="formData.subjectOfAction" type="textarea"  class="selectItem" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx" />
					</uni-forms-item>
				</view>
				<!-- 当事人信息 -->
				<view class="client-wrap" v-if="step == 1">
					<view class="card">
						<uni-forms-item label="委托当事人" :name="['clientType', 'value']" required :rules="[{ required: true, errorMessage: '请选择' }]">
							<picker mode="selector" @change="selectClient" :value="clientTypeValue" :range="clientList" range-key="text">
								<input type="text"  placeholder-style="color:#999;font-size: 28rpx" v-model="formData.clientType.value" placeholder="请选择" class="selectItem selectChoose" disabled />
							</picker>
						</uni-forms-item>
					</view>
					<view class="card" v-for="(item, index) in plainList" :key="index">
						<view v-if="!(item.customerId && formData.temporarily == 2)" class="delete-icon" @click="handleDelClient(index, plainList)">
							<uni-icons type="trash" size="20" color="#F56C6C"></uni-icons>
						</view>
						<view class="icon-man-wrap">
							<image class="icon-man" src="@/static/icon-man.png" mode=""></image>
							<view class="man-tip">当事人{{ index + 1 }}</view>
						</view>
						<view class="">
							<uni-forms-item label="姓名" v-if="item.isPerson == 1 || item.isPerson == 4" required name="name">
								<input v-model="plainList[index].name" :inputBorder="false" placeholder="请输入" :style="styleInput" placeholder-style="color:#999;font-size: 28rpx" />
							</uni-forms-item>
							<uni-forms-item label="公司名称" v-if="item.isPerson == 2 || item.isPerson == 3" required name="name">
								<input v-model="plainList[index].name" :inputBorder="false" placeholder="请输入" :style="styleInput" placeholder-style="color:#999;font-size: 28rpx" />
							</uni-forms-item>
							<uni-forms-item label="公司名称" :name="['customer', 'value']" required v-if="item.isPerson == 5">
								<picker mode="selector" :disabled="item.customerId && formData.temporarily == 2" @change="e=> selectcustomer(e,index)" :value="customerValue[index]" :range="customerList" range-key="text">
									<input type="text"  placeholder-style="color:#999;font-size: 28rpx" v-model="plainList[index].name" placeholder="请选择" class="selectItem selectChoose" disabled/>
								</picker>
							</uni-forms-item>
							<uni-forms-item label="性别" v-if="item.isPerson == 1 || item.isPerson == 4" required name="sex">
								<view class="serve-wrap">
									<view class="serve-btn" :class="{ active: item.sex == 1 }" @click="changeInvoice(item, 1)">男性</view>
									<view class="serve-btn" :class="{ active: item.sex == 2 }" @click="changeInvoice(item, 2)">女性</view>
								</view>
							</uni-forms-item>
							<uni-forms-item label="身份证号" v-if="item.isPerson == 1 || item.isPerson == 4" name="code">
								<input v-model="plainList[index].code" :inputBorder="false" placeholder="请输入" :style="styleInput" placeholder-style="color:#999;font-size: 28rpx" />
							</uni-forms-item>
							<uni-forms-item label="公司统一代码" v-if="!(item.isPerson == 1 || item.isPerson == 4)" required name="code">
								<input v-model="plainList[index].code" :disabled="item.customerId && formData.temporarily == 2" :inputBorder="false" placeholder="请输入" :style="styleInput" placeholder-style="color:#999;font-size: 28rpx" />
							</uni-forms-item>
							<uni-forms-item label="联系方式" required name="phone">
								<input v-model="plainList[index].phone" :disabled="item.customerId && formData.temporarily == 2" :inputBorder="false" placeholder="请输入" :style="styleInput" placeholder-style="color:#999;font-size: 28rpx" />
							</uni-forms-item>
						</view>
					</view>
					<view class="card plus" @click="() => {isAdd = false}">
						<view style="display: flex; align-items: center">
							<view class="icon-bg" @click.stop="handleAddClick">
								<uni-icons type="plusempty" size="18" color="rgba(0,0,0,0.45)"></uni-icons>
							</view>
							<view class="man-btn" v-if="isAdd">
								<!-- 1自然人 2法人 3个体工商户 4受害人 5顾问单位 -->
								<button class="btn" @click.stop="handleMan(1, 1)">自然人</button>
								<button class="btn" @click.stop="handleMan(2, 1)">法人</button>
								<button class="btn" @click.stop="handleMan(3, 1)">个体工商户</button>
								<button class="btn" @click.stop="handleMan(4, 1)">受害人</button>
								<button class="btn" @click.stop="handleMan(5, 1)">顾问单位</button>
							</view>
						</view>

						<view v-if="!isAdd" class="add-man">新增当事人</view>
					</view>
				</view>
				<!-- 对方当事人信息 -->
				<view class="client-wrap" v-if="step == 3">
					<view class="card">
						<!-- <uni-forms-item label="对方当事人" required name="pod">
							<view class="serve-wrap">
								<view class="">
									{{ formData.clientType.id == 1 ? '被告' : '原告' }}
								</view>
							</view>
						</uni-forms-item> -->
						<uni-forms-item label="对方当事人" :name="['clientOtherType', 'value']" required>
							<picker mode="selector" @change="selectOtherClient" :value="clientOtherTypeValue" :range="clientList" range-key="text">
								<input type="text"  placeholder-style="color:#999;font-size: 28rpx" v-model="formData.clientOtherType.value" placeholder="请选择" class="selectItem selectChoose" disabled />
							</picker>
						</uni-forms-item>
					</view>
					<view class="card" v-for="(item, index) in defendList">
						<view class="delete-icon" @click="handleDelClient(index, defendList)">
							<uni-icons type="trash" size="20" color="#F56C6C"></uni-icons>
						</view>
						<view class="icon-man-wrap">
							<image class="icon-man" src="@/static/icon-man.png" mode=""></image>
							<view class="man-tip">当事人{{ index + 1 }}</view>
						</view>
						<view class="">
							<uni-forms-item label="姓名" v-if="item.isPerson == 1 || item.isPerson == 4" required name="name">
								<input v-model="defendList[index].name" :inputBorder="false" placeholder="请输入" :style="styleInput" placeholder-style="color:#999;font-size: 28rpx" />
							</uni-forms-item>
							<uni-forms-item label="公司名称" v-if="!(item.isPerson == 1 || item.isPerson == 4)" required name="name">
								<input v-model="defendList[index].name" :inputBorder="false" placeholder="请输入" :style="styleInput" placeholder-style="color:#999;font-size: 28rpx" />
							</uni-forms-item>
							<uni-forms-item label="性别" v-if="item.isPerson == 1 || item.isPerson == 4" required name="sex">
								<view class="serve-wrap">
									<view class="serve-btn" :class="{ active: defendList[index].sex == 1 }" @click="changeInvoice(item, 1)">男性</view>
									<view class="serve-btn" :class="{ active: defendList[index].sex == 2 }" @click="changeInvoice(item, 2)">女性</view>
								</view>
							</uni-forms-item>
							<uni-forms-item label="身份证号" v-if="item.isPerson == 1 || item.isPerson == 4" name="code">
								<input v-model="defendList[index].code" :inputBorder="false" placeholder="请输入" :style="styleInput" placeholder-style="color:#999;font-size: 28rpx" />
							</uni-forms-item>
							<uni-forms-item label="公司统一代码" v-if="!(item.isPerson == 1 || item.isPerson == 4)" required name="code">
								<input v-model="defendList[index].code" :inputBorder="false" placeholder="请输入" :style="styleInput" placeholder-style="color:#999;font-size: 28rpx" />
							</uni-forms-item>
							<uni-forms-item label="联系方式" required name="phone">
								<input v-model="defendList[index].phone" :inputBorder="false" placeholder="请输入" :style="styleInput" placeholder-style="color:#999;font-size: 28rpx" />
							</uni-forms-item>
						</view>
					</view>
					<view class="card plus" @click="() => {isAdd = false}">
						<view style="display: flex; align-items: center">
							<view class="icon-bg" @click.stop="handleAddClick">
								<uni-icons type="plusempty" size="18" color="rgba(0,0,0,0.45)"></uni-icons>
							</view>
							<view class="man-btn" v-if="isAdd">
								<button class="btn" @click.stop="handleMan(1, 2)">自然人</button>
								<button class="btn" @click.stop="handleMan(2, 2)">法人</button>
								<button class="btn" @click.stop="handleMan(3, 2)">个体工商户</button>
								<button class="btn" @click.stop="handleMan(4, 2)">受害人</button>
							</view>
						</view>

						<view v-if="!isAdd" class="add-man">新增当事人</view>
					</view>
				</view>
				<!-- 附件信息 -->
				<view class="client-wrap annex-wrap"  style="background: #fff;" v-if="step == 4 && customerValueFloat.length == 0">
					<view class="menus-wrap">
						<view class="menus-item" v-for="(el, index) in fileTypeList" :key="index" :class="{ active: activeIndex == el.dictValue }">
							<view class="menus" @click="changeNav(el.dictValue, el.dictLabel)">{{ el.dictLabel }}</view>
						</view>
					</view>
					<UploadFile ref="uploadFile" v-if="activeIndex != -1 && step == 4" @changeFile="handleUpload" :param="{'bizType': activeIndex}">
						<view class="card plus">
							<view class="icon-bg">
								<uni-icons type="plusempty" size="18" color="rgba(0,0,0,0.45)"></uni-icons>
							</view>
							<text>支持扩展名：{{ uploadType }}</text>
							<view class="file-btn">上传</view>
						</view>
					</UploadFile>
					<view class="" v-for="(val, key, index) in fileList" :key="index" :data-rr="key" :data-tt="activeIndex">
						<view v-show="key == activeIndex">
							<view class="card" v-for="(item, j) in val" :key="j">
								<view class="file-card">
									<image class="icon-file" src="@/static/icon-file-list.png" mode=""></image>
									<text class="name">{{ item.originalFilename }}</text>
								</view>
								<view class="file-btn">
									<view class="del btn" @click="handleDelete(item, key)">删除</view>
									<view class="preview btn" @click="handlePreview(item)">预览</view>
									<!-- <view class="download btn">下载</view> -->
								</view>
							</view>
							<!-- <view class="card plus">
								<view class="icon-bg">
									<uni-icons type="plusempty" size="18" color="rgba(0,0,0,0.45)"></uni-icons>
								</view>
								<text>支持扩展名：{{ uploadType }}</text>
								<view class="file-btn">上传</view>
							</view> -->
						</view>
					</view>
					<view v-if="activeIndex == -1">
						<view class="card" v-for="(item, j) in otherFile" :key="j">
							<view class="file-card">
								<image class="icon-file" src="@/static/icon-file-list.png" mode=""></image>
								<text class="name">{{ item.originalFilename }}</text>
							</view>
							<view class="file-btn">
								<view class="del btn" @click="handleDelete(item, '-1')">删除</view>
								<view class="preview btn" @click="handlePreview(item)">预览</view>
								<!-- <view class="download btn">下载</view> -->
							</view>
						</view>
					</view>
					
				</view>
				<view v-if="step == 5">
					<template v-if="customerValueFloat.length == 0">
						<view class="case-info-name" >
							开票信息
						</view>
						<uni-forms-item label="抬头类型" required name="invoiceInfoVo.titleType">
							<radio-group @change="changetitleType" class="serve-wrap">
								<label class="radio">
									<radio value="1" :checked="formData.invoiceInfoVo.titleType == 1" color="#409EFF" style="transform: scale(0.7)" />
									个人
								</label>
								<label class="radio">
									<radio value="2" :checked="formData.invoiceInfoVo.titleType == 2" color="#409EFF" style="transform: scale(0.7)" />
									单位
								</label>
							</radio-group>
						</uni-forms-item>
						<uni-forms-item label="抬头名称" required name="invoiceInfoVo.title" :rules="[{ required: true, errorMessage: '请输入' }]">
							<input class="selectItem" v-model="formData.invoiceInfoVo.title" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx" />
						</uni-forms-item>
						<view class="divider-stripe"></view>
						<uni-forms-item label="税号" name="invoiceInfoVo.number">
							<input v-model="formData.invoiceInfoVo.number" placeholder="请输入" class="selectItem" placeholder-style="color:#999;font-size: 28rpx" />
						</uni-forms-item>
						<uni-forms-item label="联系电话" required name="invoiceInfoVo.contactPhone" :rules="[
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
							<input v-model="formData.invoiceInfoVo.contactPhone" class="selectItem" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx" />
						</uni-forms-item>
						<uni-forms-item label="开票金额" required name="invoiceInfoVo.invoiceMoney" :rules="[{ required: true, errorMessage: '请输入' }]">
							<input v-model="formData.invoiceInfoVo.invoiceMoney" class="selectItem" placeholder="请输入" placeholder-style="color:#999;font-size: 28rpx" />
						</uni-forms-item>
						<uni-forms-item label="发票内容" name="invoiceInfoVo.content">
							<input v-model="formData.invoiceInfoVo.content" type="textarea" placeholder="请输入" class="selectItem" placeholder-style="color:#999;font-size: 28rpx" />
						</uni-forms-item>
					</template>
				</view>
			</uni-forms>
		</view>
		<view class="common-foot">
			<button v-if="step > 1" class="btn-common-default" @click="handlePre">上一步</button>
			<button v-if="step == 5" class="btntype btn-success" :disabled="loading" :loading="loading" @click="handleNext(1)">保存</button>
			<button class="btntype" :disabled="loading" :loading="loading" @click="handleNext(2)">{{ customerValueFloat.length > 0 ? (step == 3 ? '提交' : '下一步') : (formData.isEdit || customerValueFloat.length != 0) ? (step == 4 ? '提交' : '下一步') :(step == 5  ? '提交' : '下一步') }}</button>
		</view>
	</view>
</template>

<script setup>
import { reactive, ref } from 'vue';
import UploadFile from '@/components/c-upload-file.vue';
import api from '@/api/index.js';
import { useUserStore } from '@/store/user.js';
import { onLoad } from '@dcloudio/uni-app';
import config from '@/config/index.js'

const store = useUserStore();
var wvCurrent;

let caseTypeList = ref([]);
let businessList = ref([]);
let litigationList = ref([]);
const uploadType = uni.getStorageSync('fileType');
// 文件类型
const fileTypeList = ref([]);
const activeIndex = ref('');
// 文件列表
const fileList = ref({});
//其他类型的文件
const otherFile = ref([]);
const formData = reactive({
	invoiceInfoVo: {},
	summonsInfoVo: {
	},
	caseType: {
		value: '',
		id: ''
	},
	businessType: {
		value: '',
		id: ''
	},
	stage: {
		value: '',
		id: ''
	},
	clientType: {
		value: '',
		id: ''
	},
	clientOtherType: {
		value: '',
		id: ''
	},
	status: {
		value: '',
		id: ''
	}
});
// 协约客户
const customerValue = ref([]);
const customerValueFloat = ref([])
const clinetAllList = ['', '原告', '被告','犯罪嫌疑人', '第三人(非原被告)', '受害人']
const customerList = ref([])

//文件上传
let fileIdList = [];
// 删除当事人
const clientDelIds = ref([])
onLoad(async(option) => {
	clientDelIds.value = []
	// 协约客户
	api.common.geCustomerList({}).then(res => {
		customerList.value = res.data.map(val => {
			return {
				value: val.id,
				text: val.customerName,
				phone: val.phone,
				creditCode: val.creditCode
			}
		})
	})

	// 文件类型
	const fileVal = await api.common.getCaseFileType({})
		fileTypeList.value = fileVal.data;
		if (option.id) fileTypeList.value.push({ dictValue: '-1', dictLabel: '其他' });
		activeIndex.value = fileVal.data[0].dictValue;
		fileVal.data.map((val) => {
			fileList.value[val.dictValue] = [];
		});
		console.log('fileListfileList', fileList.value);
	if (option.id) {
		formData.isEdit = option.isEdit == 'edit'
		api.caseManage.getEntity({ id: option.id }).then((res) => {
			if (res.code == 0) {
				const { id, caseType, caseTypeStr, businessTypeStr, court,riskOrFree,subjectOfAction,money, status, statusStr, businessType, stage, stageStr, subjectAmount, caseDesc } = res.data;
				formData.caseType = {
					value: caseTypeStr,
					id: caseType
				};
				formData.money = money;
				formData.court = court
				formData.riskOrFree = riskOrFree
				formData.subjectOfAction = subjectOfAction
				formData.businessType = {
					value: businessTypeStr,
					id: businessType
				};
				formData.stage = {
					value: stageStr,
					id: stage
				};
				formData.status = {
					value: statusStr,
					id: status
				};
				formData.id = id;
				formData.summonsInfoVo = res.data.summonsJson ? JSON.parse(res.data.summonsJson) : {},
				console.log('3333333333333333formData.summonsInfoVo',formData.summonsInfoVo)
				formData.temporarily = res.data.temporarily
				formData.invoiceInfoVo = res.data.invoiceJson ? JSON.parse(res.data.invoiceJson) : {}
				formData.subjectAmount = subjectAmount;
				formData.caseDesc = caseDesc;
				fileList.value = res.data.fileList;
				defendList.value = res.data.defendList;
				plainList.value = res.data.plainList;
				plainList.value?.map((val, index) => {
					if(val.customerId) {
						customerValue.value[index]= val.customerId
					}
					
				})
				customerValueFloat.value = customerValue.value.flat()
				formData.clientType = {
					value: clinetAllList[plainList.value?.[0]?.pod],
					id: plainList.value?.[0]?.pod
				};
				formData.clientOtherType = {
					value: clinetAllList[defendList.value?.[0]?.pod],
					id: defendList.value?.[0]?.pod
				};

				// 其他类型的文件
				const keys = Object.keys(res.data.fileList);
				fileIdList = []
				
				keys.map((val) => {
					if (!formData.isEdit) {
						fileList.value[val].map(file => {
							fileIdList.push(file.id)
						})
					}
					console.log('9999999999944444', fileIdList);
					if (!fileTypeList.value.some((item) => item.dictValue == val)) {
						otherFile.value.push(...fileList.value[val]);
						delete fileList.value[val];
					}
					console.log('8888888888888888', otherFile.value, fileList.value);
				});
			}
		});
	}
	// 案件类型
	api.common.getRedisDictList({ dictType: 'caseType' }).then((res) => {
		caseTypeList.value = res.data.map((val) => {
			return {
				value: val.dictValue,
				text: val.dictLabel
			};
		});
	});
	// 业务类型
	api.common.getRedisDictList({ dictType: 'businessType' }).then((res) => {
		businessList.value = res.data.map((val) => {
			return {
				value: val.dictValue,
				text: val.dictLabel
			};
		});
	});
	// 诉讼阶段
	api.common.getRedisDictList({ dictType: 'litigationStage' }).then((res) => {
		litigationList.value = res.data.map((val) => {
			return {
				value: val.dictValue,
				text: val.dictLabel
			};
		});
	});
	
});

// 协约客户
const selectcustomer = (e,index) => {
	customerValue.value[index] = e.detail.value;
	customerValueFloat.value = customerValue.value.flat()
	const { text, value, phone, creditCode } = customerList.value[e.detail.value];
	console.log('1111111111111111xieyue', text, value, phone, creditCode)
	plainList.value[index].customerId= value;
	plainList.value[index].name = text;
	plainList.value[index].code = creditCode;
	plainList.value[index].phone = phone;
};
// 切换tab
const changeNav = (val, label) => {
	activeIndex.value = val;
};

// 案件的步骤
const step = ref(1);
const rules = reactive({});
const styleInput = reactive({
	borderColor: '#fff',
	borderRadius: '0',
	backgroundColor: '#fff'
});
const uploadFile = ref(null)
const handlePre = () => {
	step.value -= 1;
	isAdd.value = false
	uploadFile.value.hide()
};

// tab菜单
const navList = reactive([]);
const active = ref(0);

const toPage = () => {
	uni.navigateTo({
		url: '/pages/usercenter/resetPassword'
	});
};

// 案件选择
const serveIndex = ref(1);
const changeServe = (e) => {
	serveIndex.value = e;
	formData.serveType = e;
};

//案件类型
const caseTypeValue = ref(1);
const selectCaseType = (e) => {
	caseTypeValue.value = e.detail.value;
	const { text, value } = caseTypeList.value[e.detail.value];
	formData.caseType.id = value;
	formData.caseType.value = text;
};

//业务类型
const businessTypeValue = ref(1);
const selectBusiness = (e) => {
	businessTypeValue.value = e.detail.value;
	const { text, value } = businessList.value[e.detail.value];
	formData.businessType.id = value;
	formData.businessType.value = text;
};

//诉讼状态
const stageValue = ref(1);
const selectLitigation = (e) => {
	stageValue.value = e.detail.value;
	const { text, value } = litigationList.value[e.detail.value];
	formData.stage.id = value;
	formData.stage.value = text;
};

// 案件状态
const statusList = [
	{
		value: 1,
		text: '进行中'
	},
	{
		value: 2,
		text: '已完结'
	},
	{
		value: 3,
		text: '已中止（解除委托或撤销）'
	}
];
const statusValue = ref(1);
const selectstatus = (e) => {
	statusValue.value = e.detail.value;
	const { text, value } = statusList[e.detail.value];
	formData.status.id = value;
	formData.status.value = text;
};

// 选择性别
const changeInvoice = (item, index) => {
	item.sex = index;
};


const handleDelClient = (index, list) => {
	const arr = list.splice(index, 1);
	customerValue.value.splice(index,1)
	customerValueFloat.value = customerValue.value.flat()
	const id = arr[0]?.id
	if(id) clientDelIds.value.push(id)
};

// 选择当事人类型
// 1.原告2.被告3.犯罪嫌疑人 4.第三人(非原被告)5.受害人
const clientList = [
	{
		text: '原告',
		value: 1,
	},
	{
		text: '被告',
		value: 2
	},
	{
		text: '犯罪嫌疑人',
		value: 3
	},
	{
		text: '第三人(非原被告)',
		value: 4
	},
	{
		text: '受害人',
		value: 5
	}
];
const clientTypeValue = ref();
const selectClient = (e) => {
	clientTypeValue.value = e.detail.value;
	const { text, value } = clientList[e.detail.value];
	formData.clientType.id = value;
	formData.clientType.value = text;
};

// 选择对方当事人
const clientOtherTypeValue = ref()
const selectOtherClient = (e) => {
	clientOtherTypeValue.value = e.detail.value;
	const { text, value } = clientList[e.detail.value];
	formData.clientOtherType.id = value;
	formData.clientOtherType.value = text;
}
const handleSearch = () => {
	uni.navigateTo({
		url: '/pages/caseManage/searchResult'
	});
};

// 选择当事人类型
const plainList = ref([]);
const defendList = ref([]);

let isAdd = ref(false);
const handleAddClick = () => {
	console.log('22222222222222', isAdd.value);
	isAdd.value = true;
};
const handleMan = (index, type) => {
	if (!isAdd.value) return;
	if (type == 1) {
		if(plainList.value?.length > 0) {
			plainList.value.push({ name: '', sex: '', code: '', phone: '', isPerson: index, plainOrDefend: 1 });
		} else {
			plainList.value = [{ name: '', sex: '', code: '', phone: '', isPerson: index, plainOrDefend: 1 }]
		}
	} else {
		if(defendList.value?.length > 0) {
			defendList.value.push({ name: '', sex: '', code: '', phone: '', isPerson: index, plainOrDefend: 2 });
		} else {
			defendList.value = [{ name: '', sex: '', code: '', phone: '', isPerson: index, plainOrDefend: 2 }]
		}
	}

	isAdd.value = false;
};

// 文件上传
const handleUpload = (file) => {
	console.log('2222222222222222file', file)
	fileIdList.push(file.id);
	if (fileList.value[activeIndex.value]) {
		fileList.value[activeIndex.value].push(file);
	} else {
		fileList.value[activeIndex.value] = [file];
	}
};

// 文件删除接口
const delFileIdList = ref([]);
const handleDelete = (item, key) => {
	console.log('22222222222222item', item);
	if(!formData.isEdit) {
		fileIdList = fileIdList.filter((val) => val != item.id);
	}
	if (formData.id) {
		delFileIdList.value.push(item.id);
		if (key == -1) {
			return (otherFile.value = otherFile.value.filter((val) => val.id != item.id));
		}
		fileList.value[key] = fileList.value[key].filter((val) => val.id != item.id);
		return;
	}
	api.common.del({ id: item.id }).then((val) => {
		if (val.code == 0) {
			fileIdList = fileIdList.filter((val) => val != item.id);
			fileList.value[key] = fileList.value[key].filter((val) => val.id != item.id);
			console.log('删除之后的元素', fileIdList);
			uni.showToast({
				icon: 'none',
				title: '删除成功！'
			});
		}
	});
};

// 抬头类型
const changetitleType = (e) => {
	formData.invoiceInfoVo.titleType = e.detail.value;
};
const changeRiskType = (e)  => {
	formData.riskOrFree = e.detail.value
}

const changeType = (e)  => {
	formData.summonsInfoVo.type = e.detail.value
}

const baseForm = ref(null);
const loading = ref(false);
const handleNext = (type) => {
	if(!formData.isEdit && step.value == 3) {
		// 开函信息
		const { name, code , phone, isPerson } = plainList.value?.[0] || {}
		if(customerValueFloat.value.length == 0) {
			formData.invoiceInfoVo.invoiceMoney = formData.money
			formData.invoiceInfoVo.title = name
			formData.invoiceInfoVo.number = code
			formData.invoiceInfoVo.contactPhone = phone
		}
		
		formData.summonsInfoVo.name = name 
		formData.summonsInfoVo.code = code
		formData.summonsInfoVo.phone = phone
		formData.summonsInfoVo.personType = isPerson
		
	}
	isAdd.value = false
	console.log('9999999999999formData', formData, customerValueFloat.value)
	if ((!formData.isEdit && ((customerValueFloat.value.length == 0 && step.value == 5) || (customerValueFloat.value.length > 0 && step.value == 3))) || (formData.isEdit && ((customerValueFloat.value.length > 0 && step.value == 3) || (customerValueFloat.value.length == 0 && step.value == 4) ) )) {
		if (type == 1) {
			if(!formData.caseType.id || !formData.subjectOfAction) {
				return uni.showToast({
								icon: 'none',
								title: '保存必须填写案件类型、案由!'
							});
			}
			const plainsArr = plainList.value?.map((res) => {
				res.pod = formData.clientType.id;
				return res;
			});
			const defendArr = defendList.value?.map((res) => {
				res.pod = formData.clientOtherType.id;
				return res;
			});
			loading.value = true;
			const customerId = []
			plainsArr?.filter(val => val.customerId).map((item) => {
				customerId.push(item.customerId)
			})
			const params = {
				businessType: formData.businessType.id,
				caseDesc: formData.caseDesc,
				status: formData.status.id,
				caseType: formData.caseType.id,
				stage: formData.stage.id,
				court:  formData.court,
				riskOrFree:  formData.riskOrFree,
				subjectOfAction:  formData.subjectOfAction,
				money: formData.money ? Math.abs(formData.money).toFixed(2) : '',
				subjectAmount: formData.subjectAmount ? Math.abs(formData.subjectAmount).toFixed(2) : '',
				clientInfoVoList: defendArr ? plainsArr?.concat(defendArr) : plainsArr,
				fileIdList,
				id: formData.id,
				customerId: customerId.join(',') || null,
				delegate: customerId.join(',') ? 1 : 2,
				temporarily: type,
				summonsInfoVo: {...formData.summonsInfoVo},
				invoiceInfoVo: {...formData.invoiceInfoVo, invoiceType: 1},
				clientDelIds: clientDelIds.value.length > 0 ? clientDelIds.value : null
			};
			api.caseManage
				.saveCaseInfo(params)
				.then((res) => {
					console.log('22222222222', res);
					if (res.code == 0) {
						uni.navigateTo({
							url: '/pages/caseManage/successPage?title=案件新增&msg=案件保存成功'
						});
					}
				})
				.finally(() => {
					setTimeout(() => {
						loading.value = false;
					}, 2000);
				});
		} else {
			baseForm.value
					.validate()
					.then((res) => {
						
						if(!formData.caseType.id) {
							return uni.showToast({
								icon: 'none',
								title: '请选择案件类型'
							});
						}
						
						if(!formData.businessType.id) {
							return uni.showToast({
								icon: 'none',
								title: '请选择业务类型'
							});
						}
						
						if (customerValueFloat.value.length == 0 && isNaN(Number(formData.money))) {
							return uni.showToast({
								icon: 'none',
								title: '费用请输入数字'
							});
						}
						if (isNaN(Number(formData.subjectAmount))) {
							return uni.showToast({
								icon: 'none',
								title: '标的额请输入数字'
							});
						}
						if(!formData.stage.id) {
							return uni.showToast({
								icon: 'none',
								title: '请选择诉讼阶段'
							});
						}
						
						if(!formData.status.id) {
							return uni.showToast({
								icon: 'none',
								title: '请选择案件状态'
							});
						}
						if(!formData.court) {
							return uni.showToast({
								icon: 'none',
								title: '请输入办案机构'
							});
						}
						if(!formData.subjectOfAction) {
							return uni.showToast({
								icon: 'none',
								title: '请输入案由'
							});
						}
						if(!formData.clientType.id) {
							return uni.showToast({
								icon: 'none',
								title: '请选择委托当事人类型'
							});
						}
						
						if(formData.clientType.id == formData.clientOtherType.id) {
							return uni.showToast({
								icon: 'none',
								title: '委托当事人类型不能与对方当事人类型一致'
							});
						}
						console.log('333333333333', formData, plainList.value, defendList.value);
						
						if (plainList.value.length == 0)
							return uni.showToast({
								icon: 'none',
								title: '至少填写一位委托当事人!'
							});
						for (let i = 0; i < plainList.value.length; i++) {
							if (!plainList.value[i].name) {
								return uni.showToast({
									icon: 'none',
									title: '请输入委托当事人名称!'
								});
							}
							if ([1,4].includes(Number(plainList.value[i].isPerson)) && !plainList.value[i].sex) {
								return uni.showToast({
									icon: 'none',
									title: '请选择委托当事人性别!'
								});
							}
							if ([2,3,5].includes(Number(plainList.value[i].isPerson)) && !plainList.value[i].code) {
								return uni.showToast({
									icon: 'none',
									title: '请选择委托当事人身份证或公司统一代码!'
								});
							}

							if (!plainList.value[i].phone) {
								return uni.showToast({
									icon: 'none',
									title: '请输入委托当事人联系方式!'
								});
							}
							var patrn = /^1[3456789]\d{9}$/; //手机电话
							var zjdh = /^((0\d{2,3})-)?(\d{7,8})$/; //座机电话
							if (patrn.test(plainList.value[i].phone) == false && zjdh.test(plainList.value[i].phone) == false) {
								return uni.showToast({
									icon: 'none',
									title: '请输入正确的委托当事人联系方式!'
								});
							}
						}
						if(formData.caseType.id != 1 ) {
							if(!formData.clientOtherType.id) {
								return uni.showToast({
									icon: 'none',
									title: '请选择对方当事人类型'
								});
							}
							if (defendList.value.length == 0)
								return uni.showToast({
									icon: 'none',
									title: '至少填写一位对方当事人!'
								});
							for (let i = 0; i < defendList.value.length; i++) {
								if (!defendList.value[i].name) {
									return uni.showToast({
										icon: 'none',
										title: '请输入对方当事人名称!'
									});
								}
								if ([1,4].includes(Number(defendList.value[i].isPerson1)) && !defendList.value[i].sex) {
									return uni.showToast({
										icon: 'none',
										title: '请选择对方当事人性别!'
									});
								}
								if ([2,3,5].includes(Number(defendList.value[i].isPerson)) && !defendList.value[i].code) {
									return uni.showToast({
										icon: 'none',
										title: '请选择对方当事人公司统一代码!'
									});
								}
								if (!defendList.value[i].phone) {
									return uni.showToast({
										icon: 'none',
										title: '请输入对方当事人联系方式!'
									});
								}
								var patrn = /^1[3456789]\d{9}$/; //手机电话
								var zjdh = /^((0\d{2,3})-)?(\d{7,8})$/; //座机电话
								if (patrn.test(defendList.value[i].phone) == false && zjdh.test(defendList.value[i].phone) == false) {
									return uni.showToast({
										icon: 'none',
										title: '请输入正确的对方当事人联系方式!'
									});
								}
							}
						}
						
						// 抬头类型
						if (!formData.isEdit && customerValueFloat.value.length == 0 && !formData.invoiceInfoVo.titleType)
							return uni.showToast({
								icon: 'none',
								title: '请选择抬头类型!'
							});
						const plainsArr = plainList.value?.map((res) => {
							res.pod = formData.clientType.id;
							return res;
						});
						const defendArr = defendList.value?.map((res) => {
							res.pod = formData.clientOtherType.id;
							return res;
						});
						loading.value = true;
						const customerId = []
						plainsArr.filter(val => val.customerId).map((item) => {
							customerId.push(item.customerId)
						})
						const params = {
							businessType: formData.businessType.id,
							caseDesc: formData.caseDesc,
							status: formData.status.id,
							caseType: formData.caseType.id,
							stage: formData.stage.id,
							court:  formData.court,
							riskOrFree:  formData.riskOrFree,
							subjectOfAction:  formData.subjectOfAction,
							money: formData.money ? Math.abs(formData.money).toFixed(2) : '',
							subjectAmount: Math.abs(formData.subjectAmount).toFixed(2),
							clientInfoVoList: defendArr ? plainsArr.concat(defendArr) : plainsArr,
							fileIdList,
							id: formData.id,
							customerId: customerId.join(',') || null,
							delegate: customerId.join(',') ? 1 : 2,
							temporarily: type,
							summonsInfoVo: {
								...formData.summonsInfoVo,
							},
							invoiceInfoVo: {...formData.invoiceInfoVo, invoiceType: 1},
							clientDelIds: clientDelIds.value.length > 0 ? clientDelIds.value : null
						};
						if (formData.id && formData.temporarily == 2) {
							api.caseManage
								.editCase({ ...params, newFileIdList: fileIdList, delFileIdList: delFileIdList.value })
								.then((res) => {
									console.log('22222222222', res);
									if (res.code == 0) {
										uni.navigateTo({
											url: '/pages/caseManage/successPage?title=案件编辑&msg=案件编辑成功'
										});
									}
								})
								.finally(() => {
									delFileIdList.value.map((val) => {
										api.common.del({ id: val });
									});
									setTimeout(() => {
										loading.value = false;
									}, 2000);
								});
						} else {
							api.caseManage
								.saveCaseInfo(params)
								.then((res) => {
									console.log('22222222222', res);
									if (res.code == 0) {
										uni.navigateTo({
											url: '/pages/caseManage/successPage?title=案件新增&msg=案件新增成功'
										});
									}
								})
								.finally(() => {
									setTimeout(() => {
										loading.value = false;
									}, 2000);
								});
						}
					})
					.catch((err) => {
						console.log('err', err);
						uni.showToast({
							icon: 'none',
							title: '请检查是否有未填字段！'
						});
					});
				
		}
		return;
	}
	step.value += 1;
	if(step.value == 5) {
		uploadFile.value.hide()
	}
};
// 文件预览
const showPreview = ref(false);
const fileUrl = ref('');
const handlePreview = (item) => {
	if (item.fileType.indexOf('image') > -1) {
		uni.previewImage({
			urls: [item.fileUrl]
		});
	} else {
		api.common.previewFile(item.fileUrl);
	}
};
</script>

<style scoped lang="scss">
@import '../../utils/step.scss';

// tab切换
.menus-wrap {
	display: flex;
	background: #fff;
	padding: 10rpx 32rpx;
	overflow: auto;
	justify-content: center;
	.menus-item {
		display: flex;
		height: 72rpx;
		justify-content: center;
		align-items: center;
		border-bottom: 2rpx solid rgba(0, 0, 0, 0.12);
		margin-bottom: 12rpx;
		white-space: nowrap;
		padding: 0 20rpx;
	}
	.active {
		border-bottom: 4rpx solid #409eff;
	}
}

.case-info-name {
	    font-size: 16px;
	    line-height: 96rpx;
	    margin-left: 40rpx;
	    font-weight: bold;
}

// 按钮样式
.btn-common-default {
	margin-right: 32rpx;
	height: 72rpx;
	line-height: 72rpx;
	border-radius: 36rpx;
	border: 1px solid #409eff;
	font-size: 28rpx;
}

.btn-success {
	background: #67C23A;
	color: #fff;
	margin-right: 32rpx;
}
::v-deep {
	.box {
		padding: 0!important;
	}
	.fileBox {
		width: 686rpx!important;
		height: 350rpx!important;
		button: {
			padding: 0!important;
		}
	}
}
</style>

<template>
	<view class="wrap">
		<hh-nav-bar image="../../../../static/icon-nav-bg.png" title="案件详情" :show-back="true" status-theme="black"></hh-nav-bar>
		<view class="search-result">
			<view class="card-detail">
				<view class="title">案件详情</view>
				<!-- <view class="info">
					<view class="name">案件名称：</view>
					<view class="value">
						{{ detailInfo.caseName }}
					</view>
				</view> -->
				<view class="info">
					<view class="name">案件编号：</view>
					<view class="value">
						{{ detailInfo.caseNum }}
					</view>
				</view>
				<view class="info">
					<view class="name">承办律师：</view>
					<view class="value">
						{{ detailInfo.userName }}
					</view>
				</view>
				<view class="info">
					<view class="name">登记时间：</view>
					<view class="value">
						{{ detailInfo.createTime }}
					</view>
				</view>
				<view class="info">
					<view class="name">费用（元）：</view>
					<view class="value">
						{{ detailInfo.money ? Number(detailInfo.money).toFixed(2) : '--' }}
					</view>
				</view>
				<view class="info">
					<view class="name">诉讼阶段：</view>
					<view class="value">
						{{ detailInfo.stageStr }}
					</view>
				</view>
				<view class="info">
					<view class="name">案件状态：</view>
					<view class="value">
						{{ detailInfo.statusStr }}
					</view>
				</view>
				<view class="info">
					<view class="name">归档状态：</view>
					<view class="value">
						{{ detailInfo.archived == 2? '已归档':'未归档' }}
					</view>
				</view>
				<view class="info">
					<view class="name">办案机构：</view>
					<view class="value">
						{{ detailInfo.court }}
					</view>
				</view>
				<view class="info">
					<view class="name">特殊审批：</view>
					<view class="value">
						{{ detailInfo.riskOrFree == 1 ? '风险代理' : detailInfo.riskOrFree == 2 ?'费用减免' : '' }}
					</view>
				</view>
				<view class="info">
					<view class="name">案由：</view>
					<view class="value">
						{{ detailInfo.subjectOfAction }}
					</view>
				</view>
			</view>
			<view class="card-detail">
				<view class="title">当事人信息</view>
				<view class="info">
					<view class="name" style="font-weight: bold;color:rgba(0,0,0,0.85)">
						委托当事人：
					</view>
					<view class="value">
						{{ clientList[detailInfo.plainList?.[0]?.pod || 0] }}
					</view>
				</view>
				<template v-for="(item, index) in detailInfo.plainList" :key="index">
					<view class="info">
						<view class="name">
							{{ ['1', '4'].includes(item.isPerson) ? '姓名：' : '公司名称：' }}
						</view>
						<view class="value">
							{{ item.name }}
						</view>
					</view>
					<view class="info" v-if="['1', '4'].includes(item.isPerson)">
						<view class="name">性别：</view>
						<view class="value">
							{{ item.sex == 1? '男' :'女' }}
						</view>
					</view>
					<view class="info">
						<view class="name">
							{{ ['1', '4'].includes(item.isPerson) ? '身份证号：' : '公司统一代码：' }}
						</view>
						<view class="value">
							{{ item.code }}
						</view>
					</view>
					<view class="info">
						<view class="name">手机号：</view>
						<view class="value">
							{{ item.phone }}
						</view>
					</view>
				</template>
				<view class="info" v-if="detailInfo.defendList && detailInfo.defendList.length > 0">
					<view class="name" style="font-weight: bold;color:rgba(0,0,0,0.85)">
						对方当事人：
					</view>
					<view class="value">
						{{ clientList[detailInfo.defendList?.[0]?.pod || 0] }}
					</view>
				</view>
				<template v-for="(item, index) in detailInfo.defendList" :key="index">
					<view class="info">
						<view class="name">
							{{ ['1', '4'].includes(item.isPerson) ? '姓名：' : '公司名称：' }}
						</view>
						<view class="value">
							{{ item.name }}
						</view>
					</view>
					<view class="info" v-if="['1', '4'].includes(item.isPerson)">
						<view class="name">性别：</view>
						<view class="value">
							{{ item.sex == 1 ? '男' : '女' }}
						</view>
					</view>
					<view class="info">
						<view class="name">
							{{ ['1', '4'].includes(item.isPerson) ? '身份证号：' : '公司统一代码：' }}
						</view>
						<view class="value">
							{{ item.code }}
						</view>
					</view>
					<view class="info">
						<view class="name">手机号：</view>
						<view class="value">
							{{ item.phone }}
						</view>
					</view>
				</template>
			</view>
			<view class="card-detail">
				<view class="title">合同附件</view>
				<template v-for="(item, i) in fileType" :key="i">
					<view class="info">
						<view class="name">{{item.dictLabel}}：</view>
					</view>
					<view class="file-list">
						<view class="file-card" v-for="(file, j) in detailInfo.fileList[item.dictValue]" :key="j">
							<view class="title">
								<image style="width: 50rpx; height: 50rpx;margin-right: 16rpx;" src="@/static/file-icon.png" mode=""></image>
								<view class="name">{{file.originalFilename}}</view>
							</view>
							<view class="file-btn">
								<view class="preview" @click="handlePreview(file)">预览</view>
								<view class="download" @click="handleDown(file)">
									下载
								</view>
							</view>
						</view>
					</view>
				</template>
				<template v-if="otherFile?.length > 0">
					<view class="info">
						<view class="name">其他：</view>
					</view>
					<view class="file-list">
						<view class="file-card" v-for="(file, i) in otherFile" :key="j">
							<view class="title">
								<image style="width: 50rpx; height: 50rpx;margin-right: 16rpx;" src="@/static/file-icon.png" mode=""></image>
								<view class="name">{{file.originalFilename}}</view>
							</view>
							<view class="file-btn">
								<view class="preview" @click="handlePreview(file)">预览</view>
								<view class="download" @click="handleDown(file)">
									下载
								</view>
							</view>
						</view>
					</view>
				</template>
				
			</view>
			<view class="card-detail" v-if="detailInfo.temporarily == 1 && !detailInfo.customerId">
				<view class="title">开票信息</view>
				<view class="info">
					<view class="name">
						抬头类型：
					</view>
					<view class="value">
						<label class="radio"><radio value="r1" disabled :checked="detailInfo.invoiceInfoVo.titleType == 1" color="#409EFF" style="transform:scale(0.7)" />个人</label>
						<label class="radio"><radio value="r2" disabled :checked="detailInfo.invoiceInfoVo.titleType == 2" color="#409EFF" style="transform:scale(0.7)" />单位</label>
					</view>
				</view>
				<view class="info">
					<view class="name">
						抬头名称：
					</view>
					<view class="value">
						{{detailInfo.invoiceInfoVo.title || '--'}}
					</view>
				</view>
				<view class="info">
					<view class="name">
						税号：
					</view>
					<view class="value">
						{{detailInfo.invoiceInfoVo.number || '--'}}
					</view>
				</view>
				<view class="info">
					<view class="name">
						开票金额：
					</view>
					<view class="value">
						{{ detailInfo.invoiceInfoVo.invoiceMoney ? Number(detailInfo.invoiceInfoVo.invoiceMoney).toFixed(2) : '--'}}
					</view>
				</view>
				<view class="info">
					<view class="name">
						发票内容：
					</view>
					<view class="value">
						{{ detailInfo.invoiceInfoVo.content || '--'}}
					</view>
				</view>
			</view>
			<view class="card-detail" v-if="detailInfo.temporarily == 1">
				<view class="title">开函信息</view>
				<view class="info">
					<view class="name">
						名称：
					</view>
					<view class="value">
						{{ detailInfo.summonsInfoVo.name || '--'}}
					</view>
				</view>
				<view class="info">
					<view class="name">
						身份证/统一代码：
					</view>
					<view class="value">
						{{ detailInfo.summonsInfoVo.code || '--'}}
					</view>
				</view> 
				<view class="info">
					<view class="name">
						联系方式：
					</view>
					<view class="value">
						{{ detailInfo.summonsInfoVo.phone || '--'}}
					</view>
				</view>
			</view>
		</view>
		<view class="common-foot">
			<template v-if="detailInfo.temporarily == 2">
				<template v-if= "isShow">
					<button class="btntype btnReject" :disabled="rejectLoad" :loading="rejectLoad" @click="handleDelete(detailInfo)">删除</button>
					<button class="btntype" :disabled="disabled" :loading="disabled" @click="handleEdit(detailInfo, 'edit')">编辑</button>
				</template>
				<button v-if="isArchiveShow(detailInfo)" class="btntype btnArchive" :disabled="archivedDisabled" :loading="archivedDisabled" @click="handleArchived(detailInfo)">归档</button>
			</template>
			<template v-if="detailInfo.temporarily == 1">
				<button class="btntype btnReject" :disabled="rejectLoad" :loading="rejectLoad" @click="handleDelete(detailInfo)">删除</button>
				<button class="btntype" :disabled="disabled" :loading="disabled" @click="handleEdit(detailInfo)">草稿</button>
			</template>
		</view>
		<ss-download ref="ssdownload" :fileUrl="fileDownUrl" :fileType="fileDownType"></ss-download>
	</view>
</template>

<script setup>
import { reactive, ref } from 'vue';
import api from '@/api/index.js';
import { onLoad } from '@dcloudio/uni-app';
import { useUserStore } from '@/store/user.js';

const store = useUserStore();
// 1.原告2.被告3.犯罪嫌疑人 4.第三人(非原被告)5.受害人
const clientList = ref(['','原告','被告','犯罪嫌疑人','第三人(非原被告)','受害人'])

const detailInfo = ref({
	plainList: [],
	defendList: [],
	fileList: {},
	summonsInfoVo: {},
	invoiceInfoVo: {},
});
const fileType = ref([])
const otherFile = ref([])
const isShow = ref(false)
onLoad((option) => {
	// 案件文件类型
	
	api.common.getCaseFileType({dictType: 'caseType'}).then(res => {
		if(res.code == 0) {
			fileType.value = res.data
		}
	})
	api.caseManage.getEntity({ id: option.id }).then((res) => {
		if (res.code == 0) {
			detailInfo.value = res.data;
			detailInfo.value.invoiceInfoVo = res.data.invoiceJson ? JSON.parse(res.data.invoiceJson) : {}
			detailInfo.value.summonsInfoVo = res.data.summonsJson ? JSON.parse(res.data.summonsJson) : {}
			const keys = Object.keys(res.data.fileList)
			keys.map(val => {
				console.log('9999999999944444', val, fileType.value,!fileType.value.some(item => item.dictValue == val))
				if (!fileType.value.some(item => item.dictValue == val)) {
					otherFile.value.push(...detailInfo.value.fileList[val])
					delete detailInfo.value.fileList[val]
				}
				console.log('8888888888888888', otherFile.value, detailInfo.value.fileList)
			})
			isShow.value = !(detailInfo.value.invoice == 2 || detailInfo.value.letter == 2)
		}
	});
});

// 文件预览
const showPreview = ref(false)
const fileUrl = ref('')
const handlePreview = (item) => {
	if(item.fileType.indexOf('image') > -1) {
		uni.previewImage({
			urls:[item.fileUrl]
		})
	} else {
		api.common.previewFile(item.fileUrl)
	}
}

// 文件下载
const fileDownUrl = ref('')
const fileDownType = ref('')
const handleDown = (item) => {
	console.log('11111111111111111', item)
	// api.common.downloadFile(item.fileUrl)
	fileDownUrl.value = item.fileUrl;
	fileDownType.value = item.fileType.indexOf('image') > -1 ? '1': '2';
	setTimeout(() => {
		fileDownUrl.value = ''
		fileDownType.value = ''
	}, 3000)
}

// 删除
const rejectLoad = ref(false)
const handleDelete = (row) => {
	rejectLoad.value = true
	api.caseManage.delCase({ id: row.id }).then((res) => {
		uni.showToast({
			icon: 'none',
			title: '删除成功！'
		});
	}).finally(() => {
		setTimeout(() => {
			rejectLoad.value = false
			uni.navigateBack()
		}, 1000)
	});
}

// 编辑
const disabled = ref(false)
const handleEdit = (item,type) => {
	uni.navigateTo({
		url: '/pages/caseManage/addCase?id=' + item.id + '&isEdit='+type
	})
	
}
// 归档
const isArchiveShow = (row) => {
	console.log('========row.archived', row.archived, store.userInfo.roleList.some(res => res.roleCode == '012'))
	return row.archived !=2 && store.userInfo.roleList.some(res => res.roleCode == '012')
}
const archivedDisabled = ref(false)
const handleArchived = (item) => {
	archivedDisabled.value = true
	api.caseManage.archive({ id: item.id }).then((res) => {
		uni.showToast({
				icon: 'none',
				title: '归档成功！'
			});
	}).finally(() => {
		setTimeout(() => {
			archivedDisabled.value = false
			uni.navigateBack()
		}, 1000)
	});
}
</script>

<style scoped lang="scss">
.wrap {
	position: relative;
	height: 100vh;
	background: #f8f8f8;
	display: flex;
	flex-direction: column;

	.search-result {
		flex: 1;
		overflow: auto;
		padding: 32rpx;

		.card-detail {
			background: #ffffff;
			border-radius: 24rpx;
			padding: 32rpx;
			margin-bottom: 16rpx;
			box-shadow: 0px 1px 12px 0px rgba(32, 144, 225, 0.18);
			.title {
				font-size: 28rpx;
				color: rgba(0, 0, 0, 0.85);
				line-height: 44rpx;
				font-weight: bold;
			}
			.info {
				margin-top: 32rpx;
				display: flex;
				.name {
					font-size: 26rpx;
					color: rgba(0, 0, 0, 0.65);
					line-height: 40rpx;
					width: 224rpx;
				}
				.value {
					font-size: 26rpx;
					color: rgba(0, 0, 0, 0.85);
					line-height: 40rpx;
					flex:1;
					overflow: hidden;
					word-break: break-all;
				}
			}

			.file-card {
				display: flex;
				justify-content: space-between;
				align-items: center;
				height: 98rpx;
				background: #ffffff;
				padding: 0 32rpx;
				box-shadow: 0px 0px 12px 0px rgba(230, 237, 241, 0.5);
				border-radius: 16px 16px 16px 16px;
				border: 1px solid rgba(0, 0, 0, 0.06);
				margin-top: 16rpx;

				.title {
					display: flex;
					align-items: center;
					font-size: 26rpx;
					color: rgba(0, 0, 0, 0.85);
					line-height: 40rpx;
					margin-left: 16rpx;
				}
				.name {
					width: 200rpx;
					overflow:hidden;
					white-space: nowrap;
					text-overflow: ellipsis;
				}

				
			}
		}
		.file-btn {
			display: flex;
			.preview {
				background: #ECF5FF;
				border-radius: 8px 8px 8px 8px;
				font-size: 24rpx;
				color: #409EFF;
				line-height: 40rpx;
				padding: 4rpx 16rpx;
			}
			.download{
				background: #f0f9eb;
				border-radius: 8px 8px 8px 8px;
				font-size: 24rpx;
				color: #67C23A;
				line-height: 40rpx;
				padding: 4rpx 16rpx;
				margin-left: 12rpx
			}
		}
	}
}
</style>

<template>
	<view class="wrap">
		<hh-nav-bar image="../../../../static/icon-nav-bg.png" title="开票详情" :show-back="true" status-theme="black"></hh-nav-bar>
		<view class="search-result">
			<view class="card-detail" v-if="!detailInfo.isCust">
				<!-- <image v-if="detailInfo.invoice == 3" class="tag" src="@/static/icon-agree.png" mode=""></image>
				<image v-if="detailInfo.invoice == 4" class="tag" src="@/static/icon-reject.png" mode=""></image> -->
				<view class="title">
					案件详情
				</view>
				<!-- <view class="info">
					<view class="name">
						案件名称：
					</view>
					<view class="value">
						{{detailInfo.caseName || '--'}}
					</view>
				</view> -->
				<view class="info">
					<view class="name">
						案件编号：
					</view>
					<view class="value">
						{{detailInfo.caseNum || '--'}}
					</view>
				</view>
				<view class="info">
					<view class="name">
						案件类型：
					</view>
					<view class="value">
						{{detailInfo.caseTypeStr || '--'}}
					</view>
				</view>
				<!-- <view class="info">
					<view class="name">
						登记人：
					</view>
					<view class="value">
						{{detailInfo.userName || '--'}}
					</view>
				</view> -->
				<view class="info">
					<view class="name">
						登记时间：
					</view>
					<view class="value">
						{{detailInfo.createTime || '--'}}
					</view>
				</view>
				<view class="info">
					<view class="name">
						费用（元）：
					</view>
					<view class="value">
						{{detailInfo.money ? Number(detailInfo.money).toFixed(2) : '--'}}
					</view>
				</view>
				<view class="info">
					<view class="name">
						承办律师：
					</view>
					<view class="value">
						{{detailInfo.userName || '--'}}
					</view>
				</view>
				<view class="info">
					<view class="name">
						委托人：
					</view>
					<view class="value">
						{{detailInfo.client || '--'}}
					</view>
				</view>
				<view class="info">
					<view class="name">
						对方当事人：
					</view>
					<view class="value">
						{{detailInfo.tarClient || '--'}}
					</view>
				</view>
				<view class="info">
					<view class="name">
						办案机构：
					</view>
					<view class="value">
						{{detailInfo.court || '--'}}
					</view>
				</view>
				<view class="info">
					<view class="name">
						案由：
					</view>
					<view class="value">
						{{detailInfo.subjectOfAction || '--'}}
					</view>
				</view>
				<template v-if="detailInfo.fileList?.length > 0">
					<view class="info">
						<view class="name">
							合同文件：
						</view>
					</view>
					<view class="file-list">
						<view class="file-card" v-for="(itemFile,index) in detailInfo.fileList">
							<view class="title">
								<image style="width: 50rpx;height: 50rpx;margin-right:16rpx" src="@/static/file-icon.png" mode=""></image>
								<view class="name">
									{{itemFile.originalFilename}}
								</view>	
							</view>
							<view class="file-btn">
								<view class="preview" @click="handlePreview(itemFile)">
									预览
								</view>
								<view class="download" @click="handleDown(itemFile)">
									下载
								</view>
							</view>
							
						</view>
					</view>
				</template>
			</view>
			<view class="card-detail" v-for="(item,index) in detailInfo.list" :key="index">
				<view class="title">
					<view style="display: flex">
						开票信息
						<view class="summons-time">
							{{item.createTime}}
						</view>
					</view>
					<view class="summons-show" @click="handleShow(item, index)">
					</view>
				</view>
				<template v-if="item.show">
				<view class="info" v-if="item.caseOrCustomer != 2">
					<view class="name">
						抬头信息：
					</view>
					<view class="value">
						<label class="radio"><radio value="r1" disabled :checked="item.titleType == 1" color="#409EFF" style="transform:scale(0.7)" />个人</label>
						<label class="radio"><radio value="r2" disabled :checked="item.titleType == 2" color="#409EFF" style="transform:scale(0.7)" />单位</label>
					</view>
				</view>
				<view class="info">
					<view class="name">
						抬头名称：
					</view>
					<view class="value">
						{{item.title || '--'}}
					</view>
				</view>
				<view class="info" v-if="item.titleType == 2">
					<view class="name">
						单位税号：
					</view>
					<view class="value">
						{{item.number || '--'}}
					</view>
				</view>
				<view class="info" v-if="item.titleType == 2">
					<view class="name">
						注册地址：
					</view>
					<view class="value">
						{{item.registeredAdd || '--'}}
					</view>
				</view>
				<view class="info" v-if="item.titleType == 2">
					<view class="name">
						注册电话：
					</view>
					<view class="value">
						{{item.registerTel || '--'}}
					</view>
				</view>
				<view class="info" v-if="item.titleType == 2">
					<view class="name">
						开户银行：
					</view>
					<view class="value">
						{{ item.bankDeposit || '--'}}
					</view>
				</view>
				<view class="info" v-if="item.titleType == 2">
					<view class="name">
						银行账号：
					</view>
					<view class="value">
						{{ item.bankAccount || '--'}}
					</view>
				</view>
				<view class="divider"></view>
				<view class="info">
					<view class="name">
						发票类型：
					</view>
					<view class="value">
						<label class="radio"><radio value="r1" disabled :checked="item.invoiceType == 1" color="#409EFF" style="transform:scale(0.7)" />增值税普通发票</label>
						<label class="radio"><radio value="r2" disabled :checked="item.invoiceType == 2" color="#409EFF" style="transform:scale(0.7)" />专用发票</label>
					</view>
				</view>
				<view class="info">
					<view class="name">
						开票金额：
					</view>
					<view class="value">
						{{ Number(item.invoiceMoney).toFixed(2) || '--'}}
					</view>
				</view>
				<view class="info">
					<view class="name">
						开票日期：
					</view>
					<view class="value">
						{{ item.invoiceTime || '--'}}
					</view>
				</view>
				<view class="info">
					<view class="name">
						发票内容：
					</view>
					<view class="value">
						{{ item.content || '--'}}
					</view>
				</view>
				<view class="divider"></view>
				<view class="info">
					<view class="name">
						联系人姓名：
					</view>
					<view class="value">
						{{ item.contactPerson || '--'}}
					</view>
				</view>
				<view class="info">
					<view class="name">
						联系电话：
					</view>
					<view class="value">
						{{ item.contactPhone || '--'}}
					</view>
				</view>
				<view class="info">
					<view class="name">
						电子邮箱：
					</view>
					<view class="value">
						{{ item.mail || '--'}}
					</view>
				</view>
				<view class="divider"></view>
				<view class="info">
					<view class="name">
						收件地址：
					</view>
					<view class="value">
						{{ item.address || '--'}}
					</view>
				</view>
				<view class="info">
					<view class="name">
						收件人：
					</view>
					<view class="value">
						{{ item.addressee || '--'}}
					</view>
				</view>
				<view class="info">
					<view class="name">
						联系电话：
					</view>
					<view class="value">
						{{ item.addresseePhone || '--'}}
					</view>
				</view>
				<view class="info" v-if="index == 0 && detailInfo.invoice ==4 && detailInfo.back">
					<view class="name">
						退回原因：
					</view>
					<view class="value">
						{{ detailInfo.back || '--'}}
					</view>
				</view>
				<template v-if="item.fileList?.length > 0">
					<view class="divider"></view>
					<view class="info">
						<view class="name">
							发票文件：
						</view>
					</view>
					<view class="file-list">
						<view class="file-card" v-for="(itemFile,index) in item.fileList">
							<view class="title">
								<image style="width: 50rpx;height: 50rpx;margin-right:16rpx" src="@/static/file-icon.png" mode=""></image>
								<view class="name">
									{{itemFile.originalFilename}}
								</view>	
							</view>
							<view class="file-btn">
								<view class="del btn" style="margin-right: 8px;" @click="handleDel(itemFile, key)">删除</view>
								<view class="preview" @click="handlePreview(itemFile)">
									预览
								</view>
								<view class="download" @click="handleDown(itemFile)">
									下载
								</view>
							</view>
							
						</view>
					</view>
				</template>
				<div v-if= "detailInfo.invoice == 3 && detailInfo.permission" style="margin-top: 32rpx">
					<UploadFile ref="uploadFile" @changeFile="handleUpload(file,index)" :param="{ bizType: 4, bizId: item.id}" width="620rpx" height="70rpx">
						<button class="btntype" style="font-size: 14px;height: 64rpx;background: #67C23A;color: #fff">上传发票</button>
					</UploadFile>
				</div>
				</template>
			</view>
			<!-- 开函详情 -->
			<letterList :caseId="detailInfo.caseId" :letter="detailInfo.letter"  :isShow="!detailInfo.isCust && detailInfo.isShow" />
		</view>
		<view class="common-foot">
			<template v-if= "detailInfo.isShow">
				<button class="btntype btnReject" :disabled="rejectLoad" :loading="rejectLoad" @click="handleReject">退回</button>
				<button class="btntype" :disabled="passLoad" :loading="passLoad" @click="handlePass">通过</button>
			</template>
			<button  class="btntype" @click="handleAgain" v-if="detailInfo.isOwner && (detailInfo.invoice == 3 || detailInfo.invoice == 1)">开票</button>
			<button  class="btntype" @click="handleAgain" v-if="detailInfo.isOwner && (detailInfo.invoice == 4)">修改</button>
			
		</view>
		<ss-download ref="ssdownload" :fileUrl="fileDownUrl" :fileType="fileType"></ss-download>
		<uni-popup ref="inputDialog" type="dialog">
			<uni-popup-dialog ref="inputClose"  mode="input" title="退回原因"
				placeholder="请输入退回原因" @confirm="rejectConfirm"></uni-popup-dialog>
		</uni-popup>
	</view>
</template>

<script setup>
import { reactive, ref } from 'vue';
import UploadFile from '@/components/c-upload-file.vue';
import api from '@/api/index.js';
import { useUserStore } from '@/store/user.js';
import {onLoad} from "@dcloudio/uni-app"
import letterList from "@/pages/letter/letterList.vue"

const store = useUserStore();

const goBack = () => {
	uni.navigateBack()
}
const detailInfo = ref({})
const contractType = uni.getStorageSync('contractType')
const getInfo = (id, caseId, isOwner, isCust, invoice) => {
	const index = detailInfo.value.list?.findIndex(val => val.show)
	const permission = store.userInfo.roleList?.filter(val => val.roleCode == "011" || val.roleCode == "010")
	detailInfo.value.permission = permission.length > 0
	
	api.invoice.getInvoiceList({id: caseId}).then(res => {
		detailInfo.value.list = res.data
		detailInfo.value.id = res.data[0]?.id
		detailInfo.value.isCust = isCust
		if(res.data.length == 1) {
			detailInfo.value.list[0].show = true
		}
		if(index === 0 || index > 0) {
			detailInfo.value.list[index].show = true
		}
		
		if(detailInfo.value.isCust) {
			detailInfo.value.invoice = invoice
			detailInfo.value.caseId = caseId
			detailInfo.value.isShow = isShow(detailInfo.value.list[0] || {})
			console.log('3333333333333333333detailInfo.value.invoice', detailInfo.value.invoice,detailInfo.value.isShow)
			return
		}
		api.caseManage.getEntity({id: caseId}).then(res => {
			if(res.code == 0) {
				const { caseNum, money, createTime, userName, caseTypeStr, invoice,client,tarClient, court, subjectOfAction, invoiceBack, letter } = res.data
				// detailInfo.value.caseName  = caseName
				detailInfo.value.caseNum  = caseNum
				detailInfo.value.caseId = caseId
				detailInfo.value.money  = money
				detailInfo.value.client  = client
				detailInfo.value.tarClient  = tarClient
				detailInfo.value.court  = court
				detailInfo.value.subjectOfAction  = subjectOfAction
				detailInfo.value.createTime  = createTime
				detailInfo.value.userName  = userName
				detailInfo.value.caseTypeStr  = caseTypeStr
				detailInfo.value.invoice = invoice
				detailInfo.value.fileList = res.data.fileList?.[contractType]
				detailInfo.value.letter = letter
				detailInfo.value.back = invoiceBack
				detailInfo.value.isShow = isShow(detailInfo.value.list[0] || {})
				detailInfo.value.isOwner = isOwner == 'true'
				console.log('文件类型', detailInfo.value.fileList)
			}
			
		})
	})
}
// 是否有审批的权限
const roleIds = []
store.userInfo.roleList?.map(val => {
	roleIds.push(val.id)
})
const isShow = (val) => {
	const hasPermissions = val.approveIds?.approverValue.split(',').some(item => {
		return roleIds.includes(Number(item))
	})
	console.log(333333333333221, hasPermissions,detailInfo.value.invoice)
	return detailInfo.value.invoice == 2 && ((val.approveIds?.approverType == 1 && val.approveIds?.approverValue.split(',').includes(`${store.userInfo.id}`))
	 || (val.approveIds?.approverType == 2 && hasPermissions))
}

onLoad(option => {
	console.log('4444444444444option', option)
	
	getInfo(option.id, option.caseId, option.isOwner, option.isCust == 'true', option.invoice)
	
})

// 开票
const handleAgain = () => {
	api.invoice.toInvoice({id: detailInfo.value.money - 3000 < 0 ? 2 : 3}).then(res => {
		if(res.code == 0) {
			if(!res.data) {
				uni.showToast({
					icon:"none",
					title:'请联系管理员配置权限！'
				})
			} else {
				uni.navigateTo({
					url:'/pages/invoice/addInvoice?caseId=' + detailInfo.value.caseId+'&status=' + detailInfo.value.invoice +'&id='+detailInfo.value.id+ '&money=' + detailInfo.value.money
				})
			}
		}
	})
}

// 回退
const rejectLoad = ref(false)
const inputDialog = ref()
const handleReject = () => {
	inputDialog.value.open()
}
const rejectConfirm = (val) => {
	rejectLoad.value = true
	if(!val) {
		uni.showToast({
			title: '请输入退回原因',
			icon: 'none'
		});
		setTimeout(() => {
			inputDialog.value.open()
		}, 1000)
		rejectLoad.value = false
		return
	}
	api.invoice.approveInvoiceInfo({id: detailInfo.value.id,approveStatus: '2', back: val}).then(res => {
		if(res.code == 0) {
			uni.showLoading({
				title: '回退成功',
				icon: 'none'
			});
			inputDialog.value.close()
			setTimeout(() => {
				uni.navigateBack()
			})
		}
	}).finally(() => {
		setTimeout(() => {
			rejectLoad.value = false
		}, 1000)
		
	})
}
// const handleReject = () => {
// 	uni.showModal({
// 	    title: '请输入退回原因',
// 	    content: '',
// 	    editable: true,
// 	    placeholderText: '请输入退回原因',
// 	    confirmText: '确认',
// 	    cancelText: '取消',
// 	    success: (res) => {
// 	      if (res.confirm) {
// 	        if (!res.content || res.content.trim() === '') {
// 	          uni.showToast({
// 	            title: '请输入退回原因',
// 	            icon: 'none'
// 	          });
// 	          // 重新打开弹窗
// 	          handleReject();
// 	        } else {
// 	          // 处理有效输入
// 	          console.log('输入退回原因:', res.content);
// 			  api.invoice.approveInvoiceInfo({id: detailInfo.value.id,approveStatus: '2', back: res.content}).then(res => {
// 			  	if(res.code == 0) {
// 			  		uni.showLoading({
// 			  			title: '回退成功',
// 			  			icon: 'none'
// 			  		});
// 			  		setTimeout(() => {
// 			  			uni.navigateBack()
// 			  		})
// 			  	}
// 			  }).finally(() => {
// 			  	setTimeout(() => {
// 			  		rejectLoad.value = false
// 			  	}, 1000)
			  	
// 			  })
// 	        }
// 	      }
// 	    },
// 	})
// }
// 通过
const passLoad = ref(false)
const handlePass = () => {
	passLoad.value = true
	api.invoice.approveInvoiceInfo({id: detailInfo.value.id,approveStatus: '1'}).then(res => {
		if(res.code == 0) {
			uni.showLoading({
				title: '审核成功',
				icon: 'none'
			});
			setTimeout(() => {
				uni.navigateBack()
			})
		}
	}).finally(() => {
		setTimeout(() => {
			passLoad.value = false
		}, 1000)
	})
}
// 删除文件
const handleDel=(item) => {
	api.common.del({ id: item.id }).then((val) => {
		if (val.code == 0) {
			uni.showToast({
				icon: 'none',
				title: '删除成功！'
			});
			getInfo(detailInfo.value.id, detailInfo.value.caseId, detailInfo.value.isOwner, detailInfo.value.isCust, detailInfo.value.invoice)
		}
	});
}
// 收起展开
const handleShow = (item,index) => {
	item.show = !item.show
}
// 上传发票
const handleUpload = async (file, index) => {
	console.log('333333333333',detailInfo.value.list?.[index]?.fileList)
	if(detailInfo.value.list?.[index]?.fileList?.[0]?.id) {
		await api.common.del({ id: detailInfo.value.list[index].fileList[0].id })
	}
	
	getInfo(detailInfo.value.id, detailInfo.value.caseId, detailInfo.value.isOwner, detailInfo.value.isCust, detailInfo.value.invoice)
}
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
const fileType = ref('')
const handleDown = (item) => {
	console.log('11111111111111111', item)
	// api.common.downloadFile(item.fileUrl)
	fileDownUrl.value = item.fileUrl;
	fileType.value = item.fileType.indexOf('image') > -1 ? '1': '2';
	setTimeout(() => {
		fileDownUrl.value = ''
		fileType.value = ''
	}, 3000)
}
</script>

<style scoped lang="scss">
.wrap {
	position: relative;
	height: 100vh;
	background: #f8f8f8;
	display: flex;
	overflow:hidden;
	flex-direction: column;

	.search-result {
		flex: 1;
		overflow: auto;
		padding: 32rpx;
		
		.divider {
			height: 1px;
			background: rgba(0,0,0,0.06);
			margin-top: 32rpx;
		}
		
		.card-detail {
			background: #FFFFFF;
			border-radius: 24rpx;
			padding: 32rpx 32rpx 48rpx;
			margin-bottom: 16rpx;
			box-shadow: 0px 1px 12px 0px rgba(32,144,225,0.18);
			position: relative;
			
			.tag {
				position: absolute;
				width: 230rpx;
				height: 230rpx;
				bottom: 60rpx;
				right: 38rpx;
				transform: rotate(-15deg);
			}
			
			
			.title {
				font-size: 28rpx;
				color: rgba(0,0,0,0.85);
				line-height: 44rpx;
				font-weight: bold;
				display: flex;
				align-items: center;
				justify-content: space-between;
			}
			.info {
				margin-top: 32rpx;
				display: flex;
				.name {
					font-size: 26rpx;
					color: rgba(0,0,0,0.65);
					line-height: 40rpx;
					width: 160rpx;
				}
				.value {
					font-size: 26rpx;
					color: rgba(0,0,0,0.85);
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
				background: #FFFFFF;
				padding: 0 8rpx;
				box-shadow: 0px 0px 12px 0px rgba(230,237,241,0.5);
				border-radius: 16px 16px 16px 16px;
				border: 1px solid rgba(0,0,0,0.06);
				margin-top: 16rpx;
				
				.title {
					display: flex;
					align-items: center;
					font-size: 26rpx;
					color: rgba(0,0,0,0.85);
					line-height: 40rpx;
					margin-left: 16rpx;
				}
				
				.name {
					width: 200rpx;
					overflow:hidden;
					white-space: nowrap;
					text-overflow: ellipsis;
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
					.del {
						background: #fef0f0;
						border-radius: 8px 8px 8px 8px;
						font-size: 24rpx;
						color: #F56C6C;
						line-height: 40rpx;
						padding: 4rpx 16rpx;
						margin-left: 12rpx
					}
				}
			}
		}
	}

}
.summons-time {
		font-size: 24rpx;
		color: rgba(0,0,0,0.45);
		font-weight: 400;
		margin-left: 16rpx;
	}
	.summons-show {
		border-width: 12rpx;
		border-color: rgba(0, 0, 0, 0.45) transparent transparent transparent;
		border-style: solid;
		border-top-width: 6px;
		border-right-width: 6px;
		border-bottom-width: 6px;
		border-left-width: 6px;
		border-top-color: rgba(0, 0, 0, 0.45);
		margin-top: 12rpx;
	}
.invoice-btn {
	display: flex;
	flex-direction: column;
	.btntype {
		width: 100%;
		margin-bottom: 16rpx;
	}
}
</style>

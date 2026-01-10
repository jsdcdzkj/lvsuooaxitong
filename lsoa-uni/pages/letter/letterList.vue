<template>
	<view class="wrap-box" v-if="detailInfo.list && detailInfo.isShow">
		<view class="search-result">
			<view class="card-detail">
				<view class="title">
					开函信息
				</view>
				<template v-for="(item,index) in detailInfo.list" :key="index">
					<view style="display: flex;justify-content:space-between;align-itemscenter;margin-top:16px;">
						<view class="summons-time">
							{{item.summonsTime}}
						</view>
						<view class="summons-show" @click="handleShow(item, index)">
						</view>
					</view>
					<div v-if="item.show" style="position: relative">
						<image v-if="index == 0 && detailInfo.letter == 2" class="tag" src="@/static/icon-checking.png" mode=""></image>
						<image v-if="index == 0 && detailInfo.letter == 4" class="tag" src="@/static/icon-reject.png" mode=""></image>
						<image v-if="(index == 0 && detailInfo.letter == 3)" class="tag" src="@/static/icon-agree.png" mode=""></image>
						<view class="info">
							<view class="name">
								名称：
							</view>
							<view class="value">
								{{ item.name || '--'}}
							</view>
						</view>
						<view class="info">
							<view class="name">
								身份证号/统一代码：
							</view>
							<view class="value">
								{{ item.code || '--'}}
							</view>
						</view> 
						<view class="info">
							<view class="name">
								联系方式：
							</view>
							<view class="value">
								{{ item.phone || '--'}}
							</view>
						</view>
					</div>
					
				</template>
			</view>
		</view>
	</view>
</template>

<script setup>
import { reactive, ref,watch } from 'vue';
import api from '@/api/index.js';
import { useUserStore } from '@/store/user.js';
import {onLoad} from "@dcloudio/uni-app"
const store = useUserStore();
const detailInfo = ref({})
const props = defineProps({
	caseId: {
		type: String,
		default: ''
	},
	letter: {
		type: String,
		default: ''
	},
	isShow: {
		type: Boolean,
		default: false
	}
})
watch(() => props.caseId,() => {
	console.log('555555555555555option', props.caseId)
	detailInfo.value.isShow =props.isShow
	api.letter.getLetterList({id: props.caseId}).then(res => {
		if(res.code == 0) {
			detailInfo.value.list = res.data
			detailInfo.value.id = res.data[0]?.id
			detailInfo.value.letter =props.letter
		}
	})
})

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

// 收起展开
const handleShow = (item,index) => {
	item.show = !item.show
}
</script>

<style scoped lang="scss">
.wrap-box {
	position: relative;
	background: #f8f8f8;
	display: flex;
	flex-direction: column;

	.search-result {
		flex: 1;
		overflow: auto;
		
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
				width: 200rpx;
				height: 200rpx;
				top: -30rpx;
				right: 12rpx;
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
					width: 186rpx;
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
				padding: 0 32rpx;
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
				}

			}
		}
	}
	.summons-time {
		font-size: 24rpx;
		color: rgba(0,0,0,0.45);
		font-weight: 400;
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

}
</style>

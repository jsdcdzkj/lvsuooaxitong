<template>
	<uni-popup ref="showselectPopup"  type="bottom" safe-area background-color="#fff">
		<view class="popup-content">
			<view class="btn-wrap">
				<view class="cancel" @click="onClose">取消</view>
				<view class="confirm" @click="onConfirm">确认</view>
			</view>
			<view class="content">
				<checkbox-group class="block" @change="changeCheckbox">
					<view class="item" v-for="item in list" :key="item.value">
						<view style="display: flex">
							<text class="nameFlag">{{ item.label }}</text>
						</view>
						<checkbox
							style="transform: scale(0.7)"
							color="#54b09d"
							:value="String(item.value)"
							:checked="item.choose"
						></checkbox>
					</view>
				</checkbox-group>
			</view>
		</view>
	</uni-popup>
</template>

<script>
	export default {
		name:"c-nodata",
		props:{
			title:{
				type:String,
				default:'当前状态'
			},
			color:{
				type:String,
				default:'#999'
			},
			checkboxData:{
				type: Array,
				default:[]
			},
			imgColor:{
				type:Boolean,
				default:true
			}
		},
		data() {
			return {
				checkedArr: [],
				checkName: [],
				list: []
			};
		},
		methods: {
			changeCheckbox(e) {
				console.log('333333333333333333eeeee', e, this.list)
				const arr = e.detail.value
				this.checkedArr = arr
				this.checkName = []
				this.list = this.list.map(res => {
					if(arr.includes(res.value)) {
						res.choose = true
						this.checkName.push(res.label)
					} else {
						res.choose = false
					}
					return res
				})
			},
			onClose() {
				this.$emit('chooseCancel')
				this.$refs.showselectPopup.close()
			},
			onConfirm(){
				this.$emit('chooseArr', this.checkedArr, this.checkName)
				this.$refs.showselectPopup.close()
			},
			openPop() {
				this.$refs.showselectPopup.open()
				this.checkedArr = []
				this.checkName = []
				this.list = JSON.parse(JSON.stringify(this.checkboxData))
				this.list.map(res => {
					if(res.choose) {
						this.checkedArr.push(res.value)
						this.checkName.push(res.label)
					}
				})
				
			},
		},
		
	}
</script>

<style lang="scss" scoped>
	.popup-content {
		padding: 0;
		background: #fff;
		border-radius: 20rpx 20rpx 0 0;
		position: relative;
	
		&.dialog {
			border-radius: 20px;
			width: 100%;
			margin: 0 auto;
		}
		
		.btn-wrap {
			padding: 0 32rpx;
			height: 96rpx;
			line-height: 96rpx;
			display: flex;
			justify-content: space-between;
			font-size: 36rpx;
			border: 1px solid rgba(0,0,0,0.06);
			.cancel {
				color: rgb(127,127,127);
			}
			.confirm {
				color: rgb(7,193,96);
			}
		}
		
		.content {
			padding: 32rpx;
			.item {
				display: flex;
				justify-content: space-between;
				align-items: center;
				height:96rpx;
				color: rgba(0,0,0,0.65);
			}
		}
	}
</style>
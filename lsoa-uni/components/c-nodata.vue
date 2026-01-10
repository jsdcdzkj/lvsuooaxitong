<template>
	<view class="wxx_nodata_box" :style="customerStyle">
		<view class="wxx_nodata">
			<image v-if="imgColor" src="../static/no-data.png" mode="widthFix"></image>
			<image v-else src="../static/no-data.png" mode="widthFix"></image>
			<!-- <view class="msg" :style="{color}">{{msg}}</view> -->
		</view>
	</view>
</template>

<script>
	export default {
		name:"c-nodata",
		props:{
			msg:{
				type:String,
				default:'小驰没有找到相关数据'
			},
			color:{
				type:String,
				default:'#999'
			},
			customerStyle:{
				type:Object,
				default:{}
			},
			imgColor:{
				type:Boolean,
				default:true
			}
		},
		data() {
			return {
				
			};
		}
	}
</script>

<style lang="scss" scoped>
	.wxx_nodata_box{
		width: 100%;
		height:750rpx;
		position: relative;
	}
	.wxx_nodata{
		width: 80%;
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		margin:auto;
		z-index: 2;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		image{
			width: 200rpx;
		}
	}
	.msg {
		margin-bottom: 40rpx;
		font-size: 48rpx;
	}
</style>
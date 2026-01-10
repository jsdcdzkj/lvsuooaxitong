<template> 
	<view class="page-container" :style="pageStyle">
		<slot></slot>
		<view class="fixed-bottom" v-if="slots.bottom">
			<slot name="bottom"></slot>
		</view>
		<view class="safe-area" v-if="safeArea"></view>
	</view>
</template>

<script setup>
import { computed } from 'vue';
import {useAppStore} from "@/store/app.js"

/**
 * @property {Boolean} safeArea 安全区域
 */
const props = defineProps({
	status: {
		type:String,
		default:'loading'
	},
	safeArea:{
		type: Boolean,
		default:false
	}
})
	
const slots = defineSlots()
const store = useAppStore()
const pageStyle = computed(() => {
	return {
		paddingBottom:  uni.upx2px( slots.bottom ?  96 : 0) + (props.safeArea ? store.safeAreaInsetsBottom : 0) + 'px'
	}
})

</script>

<style lang="scss">
	.page-container{
		width: 100%;
		height: 100%;
	}
	.safe-area{
		position: fixed;
		bottom: 0;
		left: 0;
		width: 100%;
		background-color: #fff;
		z-index: 999;
		height: constant(safe-area-inset-bottom); /*兼容 IOS<11.2*/
		height: env(safe-area-inset-bottom); /*兼容 IOS>11.2*/
	}
	.fixed-bottom{
		position: fixed;
		bottom: constant(safe-area-inset-bottom); /*兼容 IOS<11.2*/
		bottom: env(safe-area-inset-bottom); /*兼容 IOS>11.2*/
		left: 0;
		background-color: #fff;
		border-top: 1px solid #eee;
		height: 96rpx;
		width: 100%;
	}
</style>
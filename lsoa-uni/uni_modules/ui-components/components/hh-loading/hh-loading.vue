<template>
	<view class="hh-loading">
		<wu-loading-icon></wu-loading-icon>
		<text style="margin-top: 20rpx;font-size: 28rpx;color: #aaa;">加载中</text>
	</view>
</template>

<script setup>
	
</script>

<style lang="scss">
	.hh-loading{
		width: 100%;
		height: 100%;
		min-height: 400rpx;
		position: absolute;
		top: 0;
		left: 0;
		z-index: 100;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}
</style>
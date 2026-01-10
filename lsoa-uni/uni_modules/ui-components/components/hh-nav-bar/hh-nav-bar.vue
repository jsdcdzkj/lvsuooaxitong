<template>
	<view class="nav-bar" :style="Style.nav">
		<view class="nav-bar-container" :style="Style.nav">
			<view class="status" :style="Style.status"></view>
			<view class="nav content" :style="Style.content">
				<uni-icons type="left" v-if="props.showBack" :color="backColor" :size="20" style="position: absolute;left: 10px;" @click="back"></uni-icons>
				<view class="nav-title" :style="{color:props.titleColor}">{{title}}</view>
			</view>
		</view>
		<image :src="image" mode="aspectFill" v-if="image" class="image" :style="{height:`${navBarData.maxHeight}px`}"></image>
	</view>
</template>

<script setup>
/**
 * @property {String} statusBackgroundColor 状态栏样式
 * @property {String} background  背景颜色
 * @property {String} title  标题
 * @property {String} showBack  显示返回按钮
 * @property {String} backColor  返回图标颜色
 * @property {String} statusTheme 状态栏主题 
 * @value black 黑色主题
 * @value white 白色主题
 * @property {String} titleColor 标题颜色
 */
import { computed, reactive } from "vue";
import {useAppStore} from "@/store/app.js"

const props = defineProps({
	statusBackgroundColor:{
		type:String,
		default:'transparent'
	},
	statusTheme:{
		type: String,
		default: ''
	},
	background:{
		type: String,
		default: 'transparent'
	},
	title:{
		type:String,
		default: '标题'
	},
	showBack:{
		type: Boolean,
		default: false
	},
	backColor:{
		type: String,
		default: '#fff'
	},
	titleColor:{
		type: String,
		default: '#fff'
	},
	image:{
		type: String,
		default:''
	}
})


// 背景图
const im =  props.image.startsWith("http") ? props.image : import(props.image)

// 设置状态栏背景和文本颜色
if(props.statusTheme === 'black'){
	uni.setNavigationBarColor({
		backgroundColor:"#000000",
		frontColor:'#ffffff'
	})
}else if(props.statusTheme ==='white'){
	uni.setNavigationBarColor({
		backgroundColor:"#ffffff",
		frontColor:'#000000'
	})
}


const store = useAppStore()
// nav的最大高度
const maxHeight = store.appConfig.menuButtonRect.bottom + 24

const navBarData = reactive({
	// nav内容高度
	height: maxHeight - store.appConfig.statusBarHeight,
	maxHeight
})

const Style = computed(() => {
	return {
		nav:{
			height: navBarData.maxHeight + 'px',
			backgroundColor: props.background
		},
		status:{
			height: store.appConfig.statusBarHeight + 'px',
			backgroundColor: props.statusBackgroundColor
		},
		content:{
			height: navBarData.height + 'px',
		}
	}
})

</script>

<style lang="scss" scoped>
	.nav-title{
		font-weight: bold;
	}
	.nav-bar{
		width: 100%;
		
		.image{
			height: 100%;
			width: 100%;
			position: fixed;
			top: 0;
			left: 0;
		}
		& > .nav{
			&.content{
				position: absolute;
				top: 0;
			}
		}
		.nav-bar-container{
			position: fixed;
			top: 0;
			width: 100%;
			z-index: 999;
			
			.status{
				width: 100%;
			}
			.content {
				padding: 0 80rpx;
				text-align: center;
				display: flex;
				align-items: center;
				justify-content: center;
			}
		}
	}
</style>
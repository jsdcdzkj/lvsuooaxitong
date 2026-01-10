<template>
	<view class="">
		<yt-upload 
		    ref="ytUpload"
		    childId="upload1"
		    :width="width"
		    :height="height"
		    :option="option"
		    :size="size"
		    :formats="formats"
		    :debug="debug"
		    :instantly="instantly"
			@uploadEnd="onuploadEnd"
			@onprogress="onprogress"
		    @change="onChange">
		        <!-- <view class="btn" :style="{width: width,height: height}">选择附件</view> -->
				<slot></slot>
		</yt-upload>
	</view>
</template>

<script>
	import config from '@/config/index.js'
	export default {
		props: {
			param: {
				type: Object,
				default: {},
			},
			width: {
				type: String,
				default:'694rpx'
			},
			height: {
				type: String,
				default: '350rpx'
			},
		},
		watch: {
			param:{
				handler(val) {
					console.log('22222222传入的参数', val)
					this.option.formData = val
				},
				immediate: true
				
			}
		},
	    data() {
	        return {
	            // 上传接口参数
	            option: {
	                // 上传服务器地址，需要替换为你的接口地址
	                url: `${config.VUE_APP_API_BASEURL}/sys/file/upload`, // 该地址非真实路径，需替换为你项目自己的接口地址
	                // 上传附件的key
	                name: 'file',
					type:'post',
					// 根据你接口需求自定义请求头
					header: {
						accessToken: uni.getStorageSync('token'),
						// 'Content-Type': 'multipart/form-data'
					},
	                // 根据你接口需求自定义body参数
	                formData: {}
	            },
	            // 选择文件后是否立即自动上传，true=选择后立即上传
	            instantly: true,
	            // 必传宽高且宽高应与slot宽高保持一致
	            
	            // 限制允许上传的格式，空串=不限制，默认为空
	            formats: '',
	            // 文件上传大小限制
	            size: 30,
	            // 文件数量限制
	            count: 1,
	            // 文件回显列表
	            files: new Map(),
	            // 微信小程序Map对象for循环不显示，所以转成普通数组，不要问为什么，我也不知道
	            wxFiles: [],
	            // 是否打印日志
	            debug: true,
	
	            // 演示用
	            tabIndex: 0,
	            list:[], 
	        }
	    },
	    onReady() {
	    },
	    methods: {
	        // 某文件上传结束回调(成功失败都回调)
	        onuploadEnd(item) {
	            console.log(`${item.name}已上传结束，上传状态=${item.type}`);
				 uni.hideLoading()
	
	            // 更新当前窗口状态变化的文件
	            this.files.set(item.name,item);
				const temp = JSON.parse(item.responseText)
				if(temp.code == 0) {
					setTimeout(() => {
						uni.showToast({
							icon: 'none',
							title: '上传成功'
						});
					})
					
					console.log('1111111111111111changeFile', temp, this.param)
					this.$emit('changeFile', temp.data)
				} else {
					setTimeout(() => {
						uni.showToast({
							icon: 'none',
							title: temp.msg
						});
					})
					
				}
				
	
	            // ---可删除--演示上传完成后取服务端数据
	            if (item['responseText']) {
	                console.log('演示服务器返回的字符串JSON转Object对象');
	                this.files.get(item.name).responseText = JSON.parse(item.responseText);
	            }
	
	            // 微信小程序Map对象for循环不显示，所以转成普通数组，
	            // 如果你用不惯Map对象，也可以像这样转普通数组，组件使用Map主要是避免反复文件去重操作
	            // #ifdef MP-WEIXIN
	            this.wxFiles = [...this.files.values()];
	            // #endif
	
	            // 强制更新视图
	            this.$forceUpdate();
	
	            // ---可删除--演示判断是否所有文件均已上传成功
	            let isAll = [...this.files.values()].find(item=>item.type!=='success');
	            if (!isAll) {
	                console.log('已全部上传完毕');
	            }
	            else {
	                console.log(isAll.name+'待上传');
	            }
	
	        },
	        // 文件选择回调
	        onChange(files) {
	            console.log('当前选择的文件列表：',JSON.stringify([...files.values()]));
	            // 更新选择的文件 
	            this.files = files;
				uni.showLoading({
				  title: '加载中...',
				  mask: true, // 是否显示透明蒙层，防止触摸穿透
				})
				console.log('111111111111111111fileschange', files)
	            // 强制更新视图
	            this.$forceUpdate();
	
	            // 微信小程序Map对象for循环不显示，所以转成普通数组，不要问为什么，我也不知道
	            // #ifdef MP-WEIXIN
	            this.wxFiles = [...this.files.values()];
	            // #endif
	        },
			onprogress(file) {
				console.log('2222222222222222file上传进度', file)
			},
	        /**
	         * ---可删除--演示在组件上方添加新内容DOM变化
	         * DOM重排演示，重排后组件内部updated默认会触发show方法,若特殊情况未能触发updated也可以手动调用一次show()
	         * 什么是DOM重排？自行百度去
	         */
	        add() {
	            this.list.push('DOM重排测试');
	        },
	        /**
	         * ---可删除--演示Tab切换时覆盖层是否能被点击
	         * APP端因为是webview，层级比view高，此时若不希望点击触发选择文件，需要手动调用hide()
	         * 手动调用hide后，需要调用show()才能恢复覆盖层的点击
	         */
			hide(){
				this.$refs.ytUpload.hide();
			},
			show() {
				this.$refs.ytUpload.show();
			},
	        onTab(tabIndex) {
	            this.$refs.ytUpload0.hide();
	            this.$refs.ytUpload1.hide();
	
	            this.tabIndex = tabIndex;
	
	            this.$nextTick(()=>{
	                this.$refs['ytUpload'+this.tabIndex].show();
	            })
	
	        },
	        /**
	         * 打开nvue窗口查看非跟随窗口滚动效果
	         */
	        open() {
	            uni.navigateTo({
	                url: '/pages/nvue-demo/nvue-demo'
	            });
	        }
	    }
	}
</script>
<style>
.wrap {
	margin-top: 40px;
}	
</style>
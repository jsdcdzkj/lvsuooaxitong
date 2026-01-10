export const systemInfo = {
	data: () => ({
		statusBarHeight: 0,
		navigationBarHeight: 0,
		navHeight: 44,
		windowHeight: 0, // 可使用窗口高度
		safeAreaBottom: 0, //底部安全高度
		BottomBarHeight: 0, //底部按钮高度
		wrapPbHeight: 0, //底部按钮高度
	}),

	methods: {
		// 获取设备信息
		getSystemInfo() {
			this.statusBarHeight = getApp().globalData.statusBarHeight
			this.navigationBarHeight = getApp().globalData.navigationBarHeight
			this.windowHeight = uni.getSystemInfoSync().screenHeight
			this.navHeight = getApp().globalData.navHeight
			this.safeAreaBottom = getApp().globalData.safeAreaBottom
			this.BottomBarHeight = getApp().globalData.BottomBarHeight
			this.wrapPbHeight = getApp().globalData.wrapPbHeight
		},
	},
}


// 列表分页
export const listPage = {
	data() {
		return {
			pagination: {
				pageNum: 1,
				pageSize: 10,
			},
			loadStatus: "more",
			list: [],
		};
	},
	computed: {
		hasNextPage() {
			return this.loadStatus == "more";
		},
	},
	methods: {
		async getList(listFunc, extraParams = {}) {
			this.loadStatus = "loading";
			const params = Object.assign({}, this.pagination, extraParams);
			try {
				const {
					pages,
					records
				} = (await listFunc(params)).data;
				
				if (this.pagination.pageNum == 1) {
					this.list = records;
				} else {
					this.list = this.list.concat(records);
				}
				if (this.pagination.pageNum == pages || pages == 0) {
					this.loadStatus = "noMore";
				} else {
					this.pagination.pageNum++;
					this.loadStatus = "more";
				}
			} catch (error) {
				console.log(error.message);
			}
		},

		handlerReachBottom(listFunc, extraParams = {}) {
			if (this.hasNextPage) {
				this.getList(listFunc, extraParams);
			}
		},
	},
};


export const scanCode = {
	methods: {
		openScan() {
			uni.scanCode({
				success: ({
					result
				}) => {
					console.log(result);
					const {
						id
					} = this.getWxMiniProgramUrlParam(result)
					if (id) {
						this.$API.metting.mettingSign({
							id
						}).then(() => {
							this.$TOOL.toast('签到成功', 'success');
						}).catch(err => {
							uni.showToast({
								icon: 'none',
								title: err.msg,
								duration: 2000
							});
						})
					}
				}
			})
		},
		getWxMiniProgramUrlParam(url) {
			let theRequest = {};
			if (url.indexOf("#") != -1) {
				const str = url.split("#")[1];
				const strs = str.split("&");
				for (let i = 0; i < strs.length; i++) {
					theRequest[strs[i].split("=")[0]] = decodeURI(strs[i].split("=")[1]);
				}
			} else if (url.indexOf("?") != -1) {
				const str = url.split("?")[1];
				const strs = str.split("&");
				for (let i = 0; i < strs.length; i++) {
					theRequest[strs[i].split("=")[0]] = decodeURI(strs[i].split("=")[1]);
				}
			}
			return theRequest;
		}
	}
}
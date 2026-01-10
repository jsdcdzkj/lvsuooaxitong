import {defineStore} from "pinia"


export const useAppStore = defineStore("app", {
	state: () => ({
		appConfig:{
			// 安全区域
			safeArea:{
				top:0,
				bottom:0,
				right:0,
				left:0,
				width:0,
				height:0
			},
		
			safeAreaInsets: {
				bottom: 0,
				left: 0,
				right: 0,
				top: 20
			},
			// 屏幕高度
			screenHeight: 0,
			// 屏幕宽度
			screenWidth: 0,
			// 状态栏高度
			statusBarHeight: 0,
			// 系统
			system:'',
			// 设备id
			deviceId:'',
			// 设备型号
			deviceModel:'',
			// 应用名称
			appName:'',
			menuButtonRect:{
				bottom: 56,
				height: 32,
				left: 320,
				right: 407,
				top: 24,
				width: 87,
			}
		},
		pageConfig:{
			navbarHeight: 0
		}
	}),
	actions:{
		updateAppConfig(config){
			// console.log(pr);
			Reflect.ownKeys(this.appConfig).forEach(key => {
				this.appConfig[key] = config[key]
			})
			console.log(this.appConfig);
		},
		
	},
	getters:{
		safeAreaInsetsBottom: (state) => state.appConfig.safeAreaInsets.bottom
	}
})
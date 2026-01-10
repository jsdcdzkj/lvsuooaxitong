
import {defineStore} from "pinia"
import api from "@/api/index.js"

const menusList = [
				{
					type: '调解员工作平台', 
					list: [
						{name: '调解待办', icon: 'mediate.png', url: '/pages/mediate/ediation-work/ediation-work'},
						{name: '调解已办', icon: 'mediated.png', url: '/pages/mediate/mediate-done/mediate-done'},
					],
					role:[1,4]
				},
				{
					type: '仲裁员工作平台', 
					list: [
						{name: '仲裁待办', icon: 'arbitrate.png', url: '/pages/arbitration/ediation-work/ediation-work'},
						{name: '仲裁已办', icon: 'arbitrated.png', url: '/pages/arbitration/mediate-done/mediate-done'},
					],
					role:[2,4]
				},
				{
					type: '书记员工作平台', 
					list: [
						{name: '我的任务', icon: 'task.png', url: '/pages/clerk-work/clerk-work'},
					],
					role:[3,4]
				},
			]

export const useUserStore = defineStore("user", {
	state: () => {
		return {
			token: uni.getStorageSync("token"),
			userInfo: uni.getStorageSync('userInfo') || null,
			menus:menusList,
		}
	},
	actions: {
		async login(formData){
		 	const res =  await api.user.onLogin(formData)
			if(res.code ===0){
				this.userInfo = res.data
				uni.setStorageSync('password', formData.password)
				console.log('用户信息', this.userInfo);
				this.token = res.data.token
				// uni.setStorageSync('userInfo', res.data)
				uni.setStorageSync('token', res.data.tokenValue)
				// this.menus = menusList.filter(item => {
				// 	const roles = this.userInfo.staff_type.split(',')
				// 	for(const it of item.role){
				// 		if(roles.includes(it + '')){
				// 			return true
				// 		}
				// 	}
				// 	return false
				// })
				return res.data
			}
			return Promise.reject(res.msg)
		},
		logout(){
			this.token = null
			this.userInfo = null
		},
		async updateUserInfo(formData){
		 	const res =  await api.user.getUser({id:formData})
			if(res.code ===0){
				// Object.keys(this.userInfo).forEach(key => {
				// 	this.userInfo[key] = res.data[key]
				// })
				this.userInfo = res.data
				this.userInfo.isLogin = true
				uni.setStorageSync('userInfo', res.data)
				// this.menus = menusList.filter(item => {
				// 	const roles = this.userInfo.staff_type.split(',')
				// 	for(const it of item.role){
				// 		if(roles.includes(it + '')){
				// 			return true
				// 		}
				// 	}
				// 	return false
				// })
				return this.userInfo
			}else{
				this.userInfo = null
			}
			return Promise.reject(res.msg)
		},
	},
	getters: {
		isLogin: (state) => !!state.token
	}
})

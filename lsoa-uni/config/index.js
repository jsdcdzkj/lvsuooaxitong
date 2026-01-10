let env = "prd";

// env =  process.env.NODE_ENV === 'development' ? 'dev' : 'prd';//开发环境

const configs = {
	prd: {
		VUE_APP_TITLE: "律所OA(PRD)",
		// 接口地址
		VUE_APP_API_BASEURL: "http://222.187.84.12:3002/app", // 客户线上地址
		// VUE_APP_API_BASEURL: "http://172.168.10.52:7072/app",
		// VUE_APP_API_BASEURL: "http://172.168.95.36:3002/app", // 公司内部线上地址
	},
	dev: {
		VUE_APP_TITLE: "律所OA(DEV)",
		// 接口地址
		// VUE_APP_API_BASEURL: 'http://192.168.0.248:8089/app'
		VUE_APP_API_BASEURL: "http://172.168.10.61:8080/app",
	},
};

let config = configs[env];
export default config;
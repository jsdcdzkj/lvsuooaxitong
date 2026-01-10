import config from "@/config/index.js";
// import weixin from '@/utils/weixin.js'

const baseUrl = config.VUE_APP_API_BASEURL;
// console.log(baseUrl)
let ajaxTimes = 0; // 异步请求的个数
export const request = (
  params,
  callback = "",
  showLoading = false,
  headers = {}
) => {
  let header = {
    ...params.header,
  };
  //console.log(1111);
  // 全局设置token 传params的时候需要携带token的多传一个参数
  const token = uni.getStorageSync("token") || "";
  // const token = ""
  // console.log("token",token);
  header["accessToken"] = token;
  // header['Content-Type'] = 'application/json; charset=utf-8'
  params.method = params.method.toUpperCase();
  if (headers["Content-Type"]) {
    header["Content-Type"] = headers["Content-Type"];
  } else {
    header["Content-Type"] =
      params.method === "POST"
        ? "application/json; charset=UTF-8"
        : "";
  }
  // if (method) {
  // 	method = method.toUpperCase(); //小写改为大写
  // 	if (method == "POST") {
  // 		header = {
  // 			'content-type': "application/x-www-form-urlencoded"
  // 		};
  // 	} else {
  // 		header = {
  // 			'content-type': "application/json"
  // 		};
  // 	}
  // }
  // header['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8'

  if (showLoading != false) {
    ajaxTimes++;
    uni.showLoading({
      title: "处理中…",
      mask: true,
    });
  }

  return new Promise((resolve, reject) => {
    // console.log("header", JSON.stringify(header))
    uni.request({
      ...params,
      header: header,
      url: params.customUrl || baseUrl + params.url, // params.customUrl自定义的url，有些接口可能baseURL也不同
      success: (result) => {
        // console.log("result",JSON.stringify(result))
        if (result.statusCode === 200) {
          if (callback) return callback(result.data);
          // console.info(result.data)
          if (result.data.code === 0) {
            resolve(result.data);
          } else if (result.data.code === -1) {
            if (result.data.msg == "登录状态失效！") {
              uni.reLaunch({
                url: "/pages/login/index",
              });
              uni.clearStorageSync();
            }
			// console.log("213");
            uni.showToast({
              // icon:"error",
			  icon:"none",
              title: result.data.msg,
            });
			reject(result.data);
          } 
		  else if(result.data.code ===401){
			uni.showToast({
				title:"登录状态失效",
				icon:"none",
				complete: () => {
					uni.redirectTo({
						url:"/pages/login/index"
					})
				}
			})
			reject(result.data);
		  }
		  else {
            // console.log(111111111)
            uni.showModal({
              title: "提示",
              content: result.data.msg || "未知错误",
              showCancel: false,
            });
            reject(result.data);
          }
        } else {
          if (result.statusCode === 404) {
            uni.showModal({
              content: "Status:404，正在请求不存在的服务器记录！",
              showCancel: false,
            });
          } else if (result.statusCode === 500) {
            uni.showModal({
              content: "Status:500，服务器发生错误！",
              showCancel: false,
            });
          } else if (result.statusCode === 401 || result.statusCode === 403) {
            uni.reLaunch({
              url: "/pages/login/index",
            });
          } else {
            uni.showModal({
              content:
                error.message || `Status:${error.response.status}，未知错误！`,
              showCancel: false,
            });
          }
          reject();
        }
      },
      fail: (err) => {
        ajaxTimes--;
        uni.showToast({
          title: "服务器开小差啦~",
          icon: "none",
          duration: 3000,
        });
        reject(err.data);
      },
      complete(res) {
        if (showLoading != false) {
          ajaxTimes--;
          //console.log(ajaxTimes)
          if (ajaxTimes < 0) ajaxTimes = 0;
          if (ajaxTimes === 0) {
            uni.hideLoading(); // 当所有的请求都完成时再关闭loading
          }
        }
      },
    });
  });
};

// 拦截请求
function interceptor() {
  uni.addInterceptor((config, cancel) => {
    // 判断请求的url是否需要登录token验证
    if (config.url.indexOf("/app/") !== -1) {
      // 获取登录token
      const token = uni.getStorageSync("accessToken");
      // 判断登录token是否存在
      if (token) {
        // 在请求头中添加登录token
        config.header.Authorization = token;
      } else {
        // 取消该次请求
        cancel("请求拦截");
        // 跳转到登录页面
        uni.navigateTo({
          url: "/pages/login/index",
        });
      }
    }
    return config;
  });
}

function relogin() {
  uni.reLaunch({
    url: "/pages/login/index",
	"animationDuration":0,
	"animationType":"none"
  });
}

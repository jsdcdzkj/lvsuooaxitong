import App from './App'
import * as Pinia from "pinia"
import api from './api/index'
import tool from './utils/tool'
import config from './config/index.js'
// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
  ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  app.config.globalProperties.$TOOL = tool;
  app.config.globalProperties.$API = api;
  app.config.globalProperties.$baeImage = config.VUE_APP_API_BASEURL + '/minio/downMinio?fileName='
  app.config.globalProperties.back = () => {
  	uni.navigateBack()
  }
  app.use(Pinia.createPinia())
  return {
    app
  }
}
// #endif
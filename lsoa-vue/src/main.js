import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/zh-CN' // lang i18n

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import "@/assets/font/font.css";
import '@/icons' // icon
import "flex.css";
import '@/permission' // permission control
import hasPremission from "@/directive/premission";
import hasRolePremission from "@/directive/rolepremission";

// set ElementUI lang to EN
Vue.use(ElementUI, { locale,size:"small" })
// 如果想要中文版 element-ui，按如下方式声明
// Vue.use(ElementUI)
Vue.directive("hasPermi", hasPremission);
Vue.directive("hasRolePermi", hasRolePremission);
Vue.config.productionTip = false
// Vue.prototype.$isHave=function(id){
//   if(JSON.parse(localStorage.getItem('ids')).indexOf(id)!=-1){
//     return true
//   }else{
//     return false
//   }
// }
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})

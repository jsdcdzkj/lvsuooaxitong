<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import { getToken, setToken, removeToken } from '@/utils/auth'
export default {
  name: 'App',
  data() {
    return {
      _beforeUnload_time: '',
      _gap_time: ''
    }
  },
  // mounted() {
  //   window.addEventListener('beforeunload', e => {
  //     this._beforeUnload_time = new Date().getTime()
  //   })
  //   window.addEventListener('unload', async () => {
  //     this._gap_time = new Date().getTime() - this._beforeUnload_time
  //     if (this._gap_time <= 5) {
  //       //关闭
  //       //清除 cookie 或者 localStorage 的方法
  //       removeToken()
  //     } else {
  //       // 刷新
  //     }
  //   })
  // },
  // destroyed() {
  //   window.removeEventListener('beforeunload')
  //   window.removeEventListener('unload')
  // }
  // mounted() {
  //   if (window.name == "") {
  //     console.log("首次被加载");
  //     // 在首次进入页面时我们给window.name设置一个固定值(isRefresh) 
  //     window.name = "firstVisit";
  //     removeToken();
  //   } else if (window.name == "firstVisit") {
  //     console.log("页面被刷新");
  //   }
  // },
  // destroyed() {
  //   window.name = "";
  // }
}
</script>

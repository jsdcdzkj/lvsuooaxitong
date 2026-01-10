<template>
  <div>
    <svg-icon @click="click" :icon-class="isFullscreen?'exit-fullscreen':'fullscreen'"  />

  </div>
</template>

<script>
import screenfull from 'screenfull'

export default {
  name: 'Screenfull',
  data() {
    return {
      isFullscreen: false
    }
  },
  mounted() {
    this.init()
    let that=this;
    window.onresize = function(){
        that.isFullscreen=that.isFullScreen()
        that.$emit('isfull',that.isFullscreen)
    }
  },
  beforeDestroy() {
    this.destroy()
  },
  methods: {
    isFullScreen() {
    if (document.fullscreen) {
       return true;
     } else if (document.mozFullScreen) {
         return true;
     } else if (document.webkitIsFullScreen) {
         return true;
     } else if (document.msFullscreenElement) {
         return true;
     } else if (window.fullScreen) {
         return true;
     }
     return false;
 },
    click() {
      // if (!screenfull.enabled) {
      //   this.$message({
      //     message: 'you browser can not work',
      //     type: 'warning'
      //   })
      //   return false
      // }

      screenfull.toggle()
      this.isFullscreen = !this.isFullscreen;
    },
    change() {
      this.isFullscreen = screenfull.isFullscreen
    },
    init() {
      if (screenfull.enabled) {
        screenfull.on('change', this.change)
      }
    },
    destroy() {
      if (screenfull.enabled) {
        screenfull.off('change', this.change)
      }
    }
  }
}
</script>

<style scoped>
.screenfull-svg {
  display: inline-block;
  cursor: pointer;
  fill: #ffffff;
  width: 20px;
  height: 20px;
  vertical-align: 10px;
}
</style>

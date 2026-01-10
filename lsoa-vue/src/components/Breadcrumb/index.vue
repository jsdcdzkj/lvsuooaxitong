<template>
  <div class="breadcrumb-Box">
<!--    <div class="breadcrumb-title">{{ title }}</div>-->
    <div class="breadcrumb-content">
      <i class="el-icon-location"></i>
      <el-breadcrumb class="app-breadcrumb" separator-class="el-icon-arrow-right">
        <transition-group name="breadcrumb">

          <el-breadcrumb-item v-for="(item,index) in levelList" :key="item.path">
            <span v-if="item.redirect==='noRedirect'||index==levelList.length-1" class="no-redirect">{{ item.meta.title }}</span>
            <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
          </el-breadcrumb-item>
        </transition-group>
      </el-breadcrumb>
    </div>

  </div>

</template>

<script>
import pathToRegexp from 'path-to-regexp'

export default {
  data() {
    return {
      levelList: null,
      title:null
    }
  },
  watch: {
    $route() {
      this.getBreadcrumb()
    }
  },
  created() {
    this.getBreadcrumb()
  },
  methods: {
    getBreadcrumb() {
      // only show routes with meta.title
      let matched = this.$route.matched.filter(item => item.meta && item.meta.title)
      const first = matched[0]

      if (!this.isDashboard(first)) {
        matched = [].concat(matched)
      }

      this.levelList = matched.filter(item => item.meta && item.meta.title && item.meta.breadcrumb !== false)
      // console.log("this.levelList:",this.levelList)
      this.title=this.levelList[this.levelList.length-1].meta.title
    },
    isDashboard(route) {
      const path = route && route.path
      if (!path) {
        return false
      }
      return path.trim().toLocaleLowerCase() === '/dashboard'.toLocaleLowerCase()
    },
    pathCompile(path) {
      // To solve this problem https://github.com/PanJiaChen/vue-element-admin/issues/561
      const { params } = this.$route
      var toPath = pathToRegexp.compile(path)
      return toPath(params)
    },
    handleLink(item) {
      const { redirect, path } = item
      if (redirect) {
        this.$router.push(redirect)
        return
      }
      this.$router.push(this.pathCompile(path))
    }
  }
}
</script>

<style lang="scss" scoped>
.breadcrumb-Box{display:flex;justify-content: space-between;width:100%;box-sizing: border-box;padding:0 20px 0 10px;align-items: center;
.breadcrumb-title{font-size:14px;font-weight:700;color: rgba(0,0,0,0.85);}
.breadcrumb-content{align-items: center;display:flex;i{margin-right:0px;font-size:22px;color:rgba(0,0,0,0.25);}}
}
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 16px;
  line-height: 35px;
  margin-left: 8px;

  .no-redirect {
    cursor: text;
    font-size: 16px;
    color: rgba(0,0,0,0.85);
  }
}
</style>

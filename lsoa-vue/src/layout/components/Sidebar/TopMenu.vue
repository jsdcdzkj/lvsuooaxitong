<template>
  <div class="top-menu-bar">
    <el-scrollbar>
      <el-menu
        :default-active="activeMenu"
        :text-color="variables.menuText"
        :active-text-color="variables.menuActiveText"
        mode="horizontal"
      >
        <template v-for="route in permission_routes">
          <template v-if="!route.hidden && getFirstShowingChild(route.children,route)">
            <el-menu-item v-if="route.meta" :key="route.path" :index="resolvePath(route)" @click="jumpTo(route)">
              <div class="link-img" v-if="route.meta.icon">
                <img :src="require(`@/assets/${route.meta.icon}.png`)" />
              </div>
              <item :title="route.meta.title" />
            </el-menu-item>
          </template>
        </template>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import store from '@/store'
import {mapGetters} from 'vuex'
import path from 'path'
import {isExternal} from '@/utils/validate'
import Item from './Item'
import variables from '@/styles/variables.scss'

export default {
  components: {Item},
  data() {
    return {}
  },
  computed: {
    ...mapGetters([
      'permission_routes',
      'sidebar'
    ]),
    activeMenu() {
      const route = this.$route
      const {meta, matched} = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      store.dispatch('permission/changeSecondRoutes', matched[0])
      return matched[0].path || '/'
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  },
  methods: {
    getFirstShowingChild(children = [], parent) {
      let showing = true
      if (parent.hidden || (typeof parent.path === 'undefined' && typeof parent.redirect === 'undefined')) {
        showing = false
      }
      if (!children) {
        return showing
      }
      const showingChildren = children.find(item => {
        return !item.hidden && item.path
      })
      if (typeof showingChildren !== 'undefined') {
        if (showingChildren.path && parent.alwaysShow && !parent.redirect) {
          parent.redirect = showingChildren.path
        }
        if (typeof parent.meta === 'undefined' && typeof showingChildren.meta !== 'undefined') {
          parent.meta = showingChildren.meta
        }
        showing = true
      }
      return showing
    },
    resolvePath(route) {
      if (isExternal(route.path)) {
        return route.path
      }
      if (isExternal(route.redirect)) {
        return route.redirect
      }
      return route.path
    },
    jumpTo(route) {
      // console.log(route)
      if (isExternal(route.path)) {
        window.open(route.path, '_blank')
        return
      }
      if (isExternal(route.redirect)) {
        window.open(route.redirect, '_blank')
        return
      }
      this.$router.push(path.resolve(route.path, route.redirect || ''))
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep{
  .el-menu{background:transparent!important;}
}
.top-menu-bar {
  .el-menu--horizontal.el-menu {
    display: flex;
    justify-content: center;
    overflow: hidden;
    border-bottom:0!important;
  }

  .el-menu--horizontal > .el-menu-item {
    height: 50px !important;
    line-height: 50px !important;
    color: rgba($color:#ffffff, $alpha: 0.65) !important;
    font-size: 18px !important;
    display:flex;justify-content: center;align-items: center;
    position:relative;
    padding:0!important;
    .link-img{
      margin-left:40px;
      margin-right:10px;
      display:flex;justify-content: center;align-items: center;
      img{width:20px;height:20px;opacity:.8}
    }
    ::v-deep{span{margin-right:40px;display:block;}}
    &:after{
      content:'';
      display:inline-block;
      margin-left:0;
      width:1px;
      height:20px;
      background:rgba($color: #ffffff, $alpha: 0.2);
      vertical-align: -5px;
    }
    &:last-child:after{
      display:none;
    }
  }

  .el-menu--horizontal > .el-menu-item:hover {
    color: rgba($color:#ffffff, $alpha: 1) !important;
    border-bottom:0px solid #fff;
    background: transparent!important;
    .link-img{
      img{opacity:1}
    }
    &:before{
      content:'';
      position:absolute;
      bottom:5px;
      height:2px;
      left:23%;
      width:54%;
      background:#fff;
    }
  }

  .el-menu--horizontal > .el-menu-item.is-active {
    color: #fff !important;
    background: transparent !important;
    border-bottom:0px solid #fff!important;
    .link-img{
      display:flex;justify-content: center;align-items: center;
      img{opacity:1}
    }
    &:before{
      content:'';
      position:absolute;
      bottom:5px;
      height:2px;
      left:25%;
      width:50%;
      background:#fff;
    }
  }

  .el-menu--horizontal > .el-submenu .el-submenu__title {
    // height: 50px !important;
    // line-height: 50px !important;

  }

  .svg-icon {
    margin-right: 5px;
  }

  .el-scrollbar__bar.is-horizontal {
    display: none;
  }

  ::v-deep .el-scrollbar__thumb {
    background-color: rgba(255, 255, 255, 0.5);

    &:hover {
      background-color: rgba(64, 158, 255, 1);
    }
  }
}

.mobile {
  .top-menu-bar {
    ::v-deep .el-menu--horizontal > .el-menu-item {
      padding: 0 10px;
      display: inline-block;

      .sub-el-icon {
        display: none;
      }

      .svg-icon {
        display: none;
      }
    }

    ::v-deep .el-scrollbar__thumb {
      display: none;

      &:hover {
        display: none;
      }
    }
  }
}
</style>

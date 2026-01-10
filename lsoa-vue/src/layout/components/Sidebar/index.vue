<template>

    <div class="sidebar-container">
      <el-scrollbar wrap-class="scrollbar-wrapper">
        <el-menu
          :default-active="activeMenu"
          :background-color="variables.menuBg"
          :text-color="variables.menuText"
          :active-text-color="variables.menuActiveText"
          :collapse="isCollapse"
          :collapse-transition="false"
          :default-openeds="openeds"
          mode="vertical"
        >
          <sidebar-item v-for="route in permission_routes" :key="route.path" :item="route" :base-path="route.path"/>
        </el-menu>
      </el-scrollbar>
    </div>

</template>

<script>
import store from '@/store'
import {mapGetters} from 'vuex'
import path from 'path'
import {isExternal} from '@/utils/validate'
import SidebarItem from './SidebarItem'
import Item from './Item'
import variables from '@/styles/variables.scss'

export default {
  data() {
    return {
      openeds: ['0', '1']
    }
  },
  components: {SidebarItem, Item},
  computed: {
    ...mapGetters([
      'permission_routes',
      'second_routes',
      'third_routes',
      'sidebar'
    ]),
    activeMenu() {
      const route = this.$route
      const {meta, matched} = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      // store.dispatch('permission/changeThirdRoutes', matched[1] || [])
      return matched[1].path || ''
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    },
    device() {
      return this.$store.state.app.device
    }
  },
  methods: {
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
      if (isExternal(route.path)) {
        window.open(route.path, '_blank')
        return
      }
      if (isExternal(route.redirect)) {
        window.open(route.redirect, '_blank')
        return
      }
      if (route.children && route.children.length > 2 && this.device === 'mobile') {
        store.dispatch('permission/changeThirdRoutes', route)
        return
      }
      this.$router.push(path.resolve(route.path, route.redirect || ''))
    }
  }
}
</script>

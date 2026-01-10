<template>
  <div class="navbar">
<!--    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />-->
    <breadcrumb class="breadcrumb-container" />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from "@/components/Hamburger";
export default {
  components: {
    Breadcrumb,
    Hamburger,
  },
  computed: {
    ...mapGetters(["sidebar", "avatar"]),
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch("app/toggleSideBar");
    },
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';
.navbar {
  height: 35px;
  line-height:35px;
  overflow: hidden;
  position: relative;
  display:flex;
  //background: #ffffff;
  //box-shadow: 0 3px 5px rgba(0,0,0,0.05);
  // border-bottom: 1px solid rgba($color:#000000,$alpha:.06);
  border-radius:0 16px 0 0;

  .hamburger-container {
    height: 100%;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

}
</style>

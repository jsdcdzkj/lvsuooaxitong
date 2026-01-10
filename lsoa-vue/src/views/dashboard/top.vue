<template>
  <div class="dptop">
     <div class="logo"><img class="dplogo" src="../../assets/logo.png" >仲裁院联调中心业务平台</div>
    <div class="top-menu">
      <top-menu />
    </div>
     <div class="dptopright">
          <div style="cursor:pointer"><screenfull id="screenfull" v-show="isFullstatus==false" @isfull="isfullEvent" class="right-menu-item hover-effect" /></div>
          <!-- <div style="cursor:pointer"><i class="el-icon-question" style="margin-right:2rem;font-size:20rem;" /> 帮助</div> -->
          <el-dropdown trigger="click" class="dptopuser" v-if="isFullstatus==false">
            <div class="avatar-wrapper">
              <img src="../../assets/touxiangnan.png" class="user-avatar">
              {{loginName}}
            </div>
            <el-dropdown-menu slot="dropdown">
              <router-link to="/">
                <el-dropdown-item>
                  首页
                </el-dropdown-item>
              </router-link>
                <el-dropdown-item @click.native="editpaw">
                  修改密码
                </el-dropdown-item>
              <el-dropdown-item divided @click.native="logout">
                <span style="display:block;">退出登录</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
     </div>
     <el-dialog width="30%" :visible.sync="editPawdialog" title="修改密码">
           <el-form label-width="80px" label-position="right" >
            <el-form-item label="账号">
               <el-input v-model="paw.account" :disabled="true" placeholder="请输入" />
            </el-form-item>
            <el-form-item label="密码">
               <el-input v-model="paw.password" show-password placeholder="请输入" />
            </el-form-item>
             <el-form-item label="验证码">
               <el-input v-model="paw.captcha"  style="width:120px;float:left;margin-right:10px;" placeholder="请输入" />
               <div class="code" @click="showCode">
                    <security :identifyCode="identifyCode"></security>
              </div>
            </el-form-item>
            <el-form-item label="">
                <el-button type="primary" @click="sendPaw">确定修改</el-button>
            </el-form-item>
           </el-form>
       </el-dialog>
  </div>
</template>

<script>
import { setHtmlFontSize } from "@/utils/rem";
import { mapGetters } from 'vuex'
import {getcode,editPassword} from '@/api/user'
import security from '@/components/identify'
import Screenfull from '@/components/Screenfull'
import TopMenu from '@/layout/components/Sidebar/TopMenu.vue'
export default {
  data() {
    return {
      isFullstatus:false,
      editPawdialog:false,
      identifyCode:'',
      loginName:"admin",
      paw:{
        account:"管理员",
        password:'',
        captcha:'',
        token:''
      }
    }
  },
  components: {
    TopMenu,
    security,Screenfull
  },
  computed: {
    ...mapGetters([
      'avatar'
    ]),
  },
  created(){
    setHtmlFontSize();
  },
  methods:{
    isfullEvent(e){
      console.log(e)
      this.isFullstatus=e
    },
     showCode(){
      let that=this;
      getcode().then(res => {
          console.log(res.data)
          if(res.code==0){
            that.identifyCode=res.data.code;
            that.paw.token=res.data.token
          }
       })

    },
    editpaw(){
      this.editPawdialog=true;
      this.showCode()
    },
    sendPaw(){
      let that=this;
      editPassword(this.paw).then(res => {
          if(res.code==0){
            that.editPawdialog=false;
            that.$message({message: "修改成功",type: "success",});
          }
       })
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push('/login')
    }
  }
}
</script>

<style lang="scss" scoped>
.el-dropdown-menu {z-index: 9999999!important}
.dptop {height:88rem;background-size:100% 100%;color:#fff;padding-top:20rem;display:flex;align-items: center;}
.top-menu{flex:1;}
.logo {
  height:40rem;margin-left:86rem;font-size:32rem;font-weight:bold;float:left;
  .dplogo {width:39rem;height:39rem;float:left;margin-right:20rem;}
}
.dptopright {
  float:right;display:flex;align-items: center;font-size:15px;
  margin-right:30rem;
  .el-dropdown {
        color:#ffffff;
  }
  .dptopuser {
     margin-left:20rem;
     .avatar-wrapper {
       display:flex;cursor: pointer;
       align-items: center;
      }
     .user-avatar {
       width:50rem;height:50rem;border-radius: 50%;
       margin-right:10rem;

     }

  }

}
</style>

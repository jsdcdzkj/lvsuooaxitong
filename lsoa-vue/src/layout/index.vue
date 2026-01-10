<template>
    <div :class="classObj" class="app-wrapper">
        <img class="content-bg" src="@/assets/img/bg.png" alt="">
        <div class="top-menu-container">
            <div class="top-logo">
                <logo v-if="showLogo" :collapse="isCollapse" />
            </div>
            <!-- 折叠按钮 -->
            <!--      <div class="top-toolbar">-->
            <!--        <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />-->
            <!--      </div>-->
            <!--      <div class="top-menu">-->
            <!--        <top-menu />-->
            <!--      </div>-->
            <div class="top-avatar">
                <div class="right-menu" style="display: flex; align-items: center; font-size: 15px">
                    <!-- <i class="el-icon-question" style="margin-right:5px;" /> 帮助 -->
                    <el-dropdown class="avatar-container" trigger="click">
                        <div class="avatar-wrapper">
                            <img v-if="avatar" :src="avatar" class="user-avatar" />
                            <img v-else src="@/assets/touxiangnan.png" class="user-avatar" />
                            {{ name }}
                        </div>
                        <el-dropdown-menu slot="dropdown" class="user-dropdown">
                            <router-link to="/">
                                <el-dropdown-item> 首页 </el-dropdown-item>
                            </router-link>
                            <el-dropdown-item @click.native="editpaw"> 修改密码 </el-dropdown-item>
                            <el-dropdown-item divided @click.native="logout">
                                <span style="display: block">退出登录</span>
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
            </div>
        </div>
        <div
            v-if="device === 'mobile' && sidebar.opened && second_routes.length > 0"
            class="drawer-bg"
            @click="handleClickOutside"
        />
        <sidebar />

        <div class="main-container" :class="classSidebar">
            <div :class="{ 'fixed-header': fixedHeader }">
                <navbar />
            </div>
            <app-main />

            <el-dialog
                width="550px"
                :visible.sync="editPawdialog"
                title="修改密码"
                append-to-body
                @close="onClosed"
                :show-close="!weakPassword"
                :close-on-click-modal="false"
                :close-on-press-escape="!weakPassword"
            >
                <el-form
                    :rules="rules"
                    ref="form"
                    label-width="80px"
                    label-position="right"
                    :model="paw"
                >
                    <el-form-item label="账号" prop="account">
                        <el-input v-model="userInfo.realName" :disabled="true" placeholder="请输入" />
                    </el-form-item>
                    <el-form-item label="原密码" prop="oldPassword">
                        <el-input
                            :type="passwordTypeOld ? 'password' : 'text'"
                            v-model="paw.oldPassword"
                            autocomplete="off"
                            placeholder="请输入原密码"
                        >
                            <template v-slot:suffix>
                                <span
                                    class="show-password"
                                    @click="passwordTypeOld = !passwordTypeOld"
                                >
                                    <svg-icon :icon-class="passwordTypeOld ? 'eye' : 'eye-open'" />
                                </span>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password">
                        <el-input
                            placeholder="密码需包含大小写字母、数字和特殊符号，并且长度应在8~16位"
                            v-model="paw.password"
                            :type="passwordType ? 'password' : 'text'"
                        >
                            <template v-slot:suffix>
                                <span class="show-password" @click="passwordType = !passwordType">
                                    <svg-icon :icon-class="passwordType ? 'eye' : 'eye-open'" />
                                </span>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item label="确认密码" prop="confirmPassword">
                        <el-input
                            placeholder="密码需包含大小写字母、数字和特殊符号，并且长度应在8~16位"
                            v-model="paw.confirmPassword"
                            :type="passwordTypeSure ? 'password' : 'text'"
                        >
                            <template v-slot:suffix>
                                <span
                                    class="show-password"
                                    @click="passwordTypeSure = !passwordTypeSure"
                                >
                                    <svg-icon :icon-class="passwordTypeSure ? 'eye' : 'eye-open'" />
                                </span>
                            </template>
                        </el-input>
                    </el-form-item>
                    <!--             <el-form-item label="验证码">-->
                    <!--               <el-input v-model="paw.captcha"  style="width:120px;float:left;margin-right:10px;" placeholder="请输入" />-->
                    <!--               <div class="code" @click="showCode">-->
                    <!--                    <security :identifyCode="identifyCode"></security>-->
                    <!--              </div>-->
                    <!--            </el-form-item>-->
                    <el-form-item label="">
                        <el-button type="primary" @click="sendPaw">确定修改</el-button>
                    </el-form-item>
                </el-form>
            </el-dialog>
        </div>
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
import { Navbar, Sidebar, AppMain } from './components';
import Logo from './components/Sidebar/Logo';
import TopMenu from './components/Sidebar/TopMenu';
import Hamburger from '@/components/Hamburger';
import ResizeMixin from './mixin/ResizeHandler';
import { editPassword } from '@/api/user';
import security from '@/components/identify';
import { async } from 'ezuikit-js';
export default {
    name: 'Layout',
    data() {
        return {
            editPawdialog: false,
            identifyCode: '',
            loginName: 'admin',
            paw: {
                password: '',
                id: '',
                confirmPassword: '',
                oldPassword: '',
            },
            passwordType: true,
            passwordTypeSure: true,
            passwordTypeOld: true,
            rules: {
                oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 8, max: 16, message: '密码长度应在8~16位', trigger: 'blur' },
                    {
                        pattern:
                        /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%#^()*?&,.?":{}|<>\\])[A-Za-z\d@$#^()!%*?&,.?":{}|<>\\]{8,16}$/,
                        message: '密码需包含大小写字母、数字和特殊符号',
                        trigger: 'blur',
                    },
                ],
                confirmPassword: [
                    { required: true, message: '请输入确认密码', trigger: 'blur' },
                    { min: 8, max: 16, message: '密码长度应在8~16位', trigger: 'blur' },
                    {
                        pattern:
                        /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%#^()*?&,.?":{}|<>\\])[A-Za-z\d@$#^()!%*?&,.?":{}|<>\\]{8,16}$/,
                        message: '密码需包含大小写字母、数字和特殊符号',
                        trigger: 'blur',
                    },
                ],
            },
            weakPassword: false,
        };
    },
    components: {
        security,
        Navbar,
        Sidebar,
        AppMain,
        Logo,
        TopMenu,
        Hamburger,
    },
    mixins: [ResizeMixin],
    computed: {
        ...mapGetters([
            'second_routes',
            'third_routes',
            'sidebar',
            'avatar',
            'nameSpelling',
            'name',
            'userInfo'
        ]),
        sidebar() {
            return this.$store.state.app.sidebar;
        },
        device() {
            return this.$store.state.app.device;
        },
        fixedHeader() {
            return this.$store.state.settings.fixedHeader;
        },
        classObj() {
            return {
                hideSidebar: !this.sidebar.opened,
                openSidebar: this.sidebar.opened,
                withoutAnimation: this.sidebar.withoutAnimation,
                mobile: this.device === 'mobile',
            };
        },
        showLogo() {
            return this.$store.state.settings.sidebarLogo;
        },
        isCollapse() {
            return !this.sidebar.opened;
        },
        classSidebar() {
            return {
                sidebarLeft: true,
                // subSidebarLeft: this.second_routes.length > 0 && this.third_routes.length > 0
            };
        },
    },
    methods: {
        editpaw() {
            this.editPawdialog = true;
            // this.showCode()
        },
        sendPaw() {
            let that = this;
            this.$refs.form.validate((valid) => {
                if (valid) {
                    if (this.paw.password != this.paw.confirmPassword) {
                        this.$message.error('两次密码输入不一致');
                        return;
                    }
                    this.paw.id = this.userInfo.id;
                    editPassword(this.paw).then(async(res) => {
                        if (res.code == 0) {
                            that.editPawdialog = false;
                            that.$message({ message: '修改成功', type: 'success' });
                            await this.$store.dispatch('user/logout')
                            this.$router.push('/login')
                        }
                    });
                }
            });
        },
        onClosed() {
            this.$refs['form'].resetFields();
            this.passwordType = true;
            this.passwordTypeSure = true;
            this.passwordTypeOld = true;
        },
        handleClickOutside() {
            this.$store.dispatch('app/closeSideBar', { withoutAnimation: false });
        },
        toggleSideBar() {
            if (this.device === 'mobile' && this.second_routes.length < 1) {
                return;
            }
            this.$store.dispatch('app/toggleSideBar');
        },
        async logout() {
            await this.$store.dispatch('user/logout');
            this.$router.push('/login');
        },
    },
    mounted() {
        this.paw.account = this.nameSpelling;
        const password = localStorage.getItem('password')
        if (password == 'Jsdc@0516') {
            // 判断密码是否初始密码
            this.weakPassword = true;
            this.editPawdialog = true;
            setTimeout(() => {
            this.$message.error("您的密码为默认密码，建议您尽快修改密码！");
            }, 1000);
        }
        
    },
};
</script>

<style lang="scss" scoped>
@import '~@/styles/mixin.scss';
@import '~@/styles/variables.scss';

.app-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;
    box-sizing: border-box;
    padding: 30px;
    overflow: hidden;
    // background: url(../assets/bg.png) no-repeat 50% top;
    &.mobile.openSidebar {
        position: fixed;
        top: 0;
    }

    .content-bg {
        position: absolute;
        bottom:0;
        left:0;
        width:614px;
        height:347px;
    }
    //.hui{position:absolute;left:0px;bottom:0px;width:426px;height:230px;z-index:9999;img{width:100%;}}
}
.drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    top: 0;
    height: 100%;
    position: absolute;
    z-index: 999;
}

.fixed-header {
    position: fixed;
    top: 24px;
    left: 282px;
    z-index: 9;
    width: 100%;
    transition: width 0.28s;
}

.sidebarLeft {
    .fixed-header {
        width: calc(100% - #{$sideBarWidth} - 60px);
    }
}
.subSidebarLeft {
    .fixed-header {
        width: calc(100% - #{$sideBarWidth} - #{$subBarWidth});
    }
}

.hideSidebar {
    .fixed-header {
        width: 100%;
    }
    .sidebarLeft .fixed-header {
        width: calc(100% - 114px);
    }
}

.mobile {
    .fixed-header {
        width: 100%;
    }

    .sidebarLeft .fixed-header {
        width: 100%;
    }
}

.right-menu {
    float: right;
    height: 100%;
    line-height: 60px;
    color: #fff;
    &:focus {
        outline: none;
    }

    .el-dropdown {
        color: rgba(0,0,0,0.85);
    }

    .right-menu-item {
        display: inline-block;
        padding: 0 8px;
        height: 100%;
        font-size: 18px;
        color: #5a5e66;
        vertical-align: text-bottom;

        &.hover-effect {
            cursor: pointer;
            transition: background 0.3s;

            &:hover {
                background: rgba(0, 0, 0, 0.025);
            }
        }
    }

    .avatar-container {
        margin-left: 15px;
        margin-right: 30px;
        height: 60px;
        cursor: pointer;
        .avatar-wrapper {
            display: flex;
            align-items: center;
            position: relative;

            .user-avatar {
                cursor: pointer;
                width: 32px;
                height: 32px;
                border-radius: 32px;
                margin-right: 10px;
            }

            .el-icon-caret-bottom {
                cursor: pointer;
                position: absolute;
                right: -20px;
                top: 25px;
                font-size: 12px;
            }
        }
    }
}
</style>

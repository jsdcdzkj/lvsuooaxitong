<template>
    <div class="login-container">
        <!-- <div class="logowrap">
            <img :src="require('@/assets/logo.png')" alt="" />
        </div> -->
        <img class="logo-bg" src="@/assets/img/logo-bg.png" alt="">
        <div class="loginwrap">

            <img class="logo-img" src="@/assets/img/logo-img.png" alt="">
            <el-form
                ref="loginForm"
                :model="loginForm"
                :rules="loginRules"
                class="login-form"
                auto-complete="on"
                label-position="left"
            >
                <div
                    class="logo-title"
                >
                    <div class="title">欢迎登陆</div>
                    <div>律所OA办公系统</div>
                </div>
                <el-form-item prop="account">
                    <!-- <span class="svg-container">
            <svg-icon icon-class="user" />
          </span> -->
                    <el-input
                        ref="account"
                        v-model="loginForm.account"
                        placeholder="请输入账号"
                        name="account"
                        type="text"
                        tabindex="1"
                        auto-complete="on"
                    />
                </el-form-item>

                <el-form-item prop="password">
                    <!-- <span class="svg-container">
            <svg-icon icon-class="password" />
          </span> -->
                    <el-input
                        :key="passwordType"
                        ref="password"
                        v-model="loginForm.password"
                        :type="passwordType"
                        placeholder="请输入密码"
                        name="password"
                        tabindex="2"
                        auto-complete="on"
                        @keyup.enter.native="handleLogin"
                    />
                    <span class="show-pwd" @click="showPwd">
                        <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
                    </span>
                </el-form-item>
                <!--      <div style="overflow:hidden">-->
                <!--        <el-form-item prop="captcha" style="width:315px;float:left;">-->
                <!--          <el-input-->
                <!--            ref="captcha"-->
                <!--            v-model="loginForm.captcha"-->
                <!--            type="text"-->
                <!--            placeholder="请输入验证码"-->
                <!--            name="captcha"-->
                <!--            tabindex="3"-->
                <!--            @keyup.enter.native="handleLogin"-->
                <!--          />-->
                <!--        </el-form-item>-->
                <!--                <div class="code" style="float:right;" @click="showCode">-->
                <!--                  <security :identify-code="identifyCode" />-->
                <!--                </div>-->
                <!--      </div>-->
                <!-- <div>
                    <el-checkbox v-model="jizhu">记住密码</el-checkbox>
                </div> -->
                <el-button
                    :loading="loading"
                    type="primary"
                    style="
                        background: #409EFF;
                        height: 50px;
                        width: 100%;
                        margin-top: 30px;
                        font-size: 20px;
                        font-weight: bold;
                    "
                    @click.native.prevent="handleLogin"
                    >登录
                </el-button>
            </el-form>
        </div>
        
    </div>
</template>

<script>
import { list } from '@/api/attachment';
export default {
    name: 'Login',
    components: {},
    data() {
        return {
            identifyCode: '',
            jizhu: true,
            loginForm: {
                account: '',
                password: '',
                captcha: '',
                codetoken: '',
                type: 1,
                schoolId: 0,
            },
            loginRules: {
                account: [{ required: true, trigger: 'blur', message: '请输入账号' }],
                password: [{ required: true, trigger: 'blur', message: '请输入密码' }],
                captcha: [{ required: true, trigger: 'blur', message: '请输入验证码' }],
            },
            loading: false,
            passwordType: 'password',
            redirect: undefined,
        };
    },
    watch: {
        $route: {
            handler: function (route) {
                this.redirect = route.query && route.query.redirect;
            },
            immediate: true,
        },
    },
    mounted() {
        // this.showCode()
        // if (localStorage.getItem('account')) {
        //     this.loginForm.account = 'admin';
        // }
        // if (localStorage.getItem('password')) {
        //     this.loginForm.password = '123456';
        // }
    },
    methods: {
        showPwd() {
            if (this.passwordType === 'password') {
                this.passwordType = '';
            } else {
                this.passwordType = 'password';
            }
            this.$nextTick(() => {
                this.$refs.password.focus();
            });
        },
        handleLogin() {
            this.$refs.loginForm.validate((valid) => {
                if (valid) {
                    this.loading = true;
                    this.$store
                        .dispatch('user/login', this.loginForm)
                        .then(() => {
                            this.$router.push({
                                path: this.redirect || '/',
                                query: this.otherQuery,
                            });
                            this.loading = false;
                            if (this.jizhu) {
                                localStorage.setItem('password', this.loginForm.password);
                                localStorage.setItem('account', this.loginForm.account);
                            } else {
                                localStorage.removeItem('password');
                                localStorage.removeItem('account');
                            }

                            // 获取文件大小和路径
                            list({}).then(res => {
                                if(res.code == 0) {
                                    localStorage.setItem('fileInfo',JSON.stringify({type: res.data.fileTypeConfig.dictValue, size:res.data.maxSize.dictValue}))
                                }
                            })
                        })
                        .catch(() => {
                            this.loading = false;
                        });
                    // 无接口对接登录方法

                    // this.loading = true;
                    // this.$store
                    //     .dispatch('user/login', this.loginForm)
                    //     .then(() => {
                    //         this.$router.push({
                    //             path: this.redirect || '/',
                    //             query: this.otherQuery,
                    //         });
                    //         this.loading = false;
                    //     })
                    //     .catch(() => {
                    //         this.loading = false;
                    //     });
                } else {
                    return false;
                }
            });
        },
    },
};
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */

$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
    .login-container .el-input input {
        color: $cursor;
    }
}

/* reset element-ui css */
.login-container {
    position: relative;

    .logo-bg {
        position: absolute;
        height: 50%;
        width: 100%;
        left:0;
        bottom:0;
        z-index:-1;
    }

    .el-checkbox__input.is-checked + .el-checkbox__label {
        color: #0b3055 !important;
    }

    .el-checkbox__input.is-checked .el-checkbox__inner {
        background-color: #0b3055;
        border-color: #0b3055;
    }

    // background: url(../../assets/login_bg.png) no-repeat 50% 50%;
    // background-size: cover;

    .el-input {
        display: inline-block;
        height: 46px;
        width: 85%;
        border: 0px;

        input {
            font-size: 20px;
            color: #333;
            background: transparent;
            border: 0px;
            -webkit-appearance: none;
            border-radius: 0px;
            padding: 12px 5px 12px 15px;
            height: 46px;

            &:-webkit-autofill {
                box-shadow: 0 0 0px 1000px $bg inset !important;
                -webkit-text-fill-color: $cursor !important;
            }
        }
    }

    .el-form-item {
        background: #F8FBFE;
        border-radius: 10px;
        // border: 2px solid rgba($color: #000, $alpha: 0.06);
    }

    .code {
        .s-canvas canvas {
            height: 52px !important;
            width: 116px;
        }
    }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
    height: 100%;
    width: 100%;
    overflow: hidden;
    position: relative;
    .logowrap {
        position: absolute;
        left: 30px;
        top: 30px;
        width: 400px;
        z-index: 1;
        img {
            width: 100%;
        }
    }
    .loginwrap {
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: space-around;
        align-items: center;
    }

    .logo-img {
        width: 56%;
        object-fit: contain;
    }
    .login-form {
        width: 550px;
        position: relative;
        padding: 40px;
        border-radius: 16px;
        background: #fff;
        box-shadow: 0px 1px 4px 0px rgba(0,0,0,0.12);
        border-radius: 20px 20px 20px 20px;
        border: 1px solid rgba(0,0,0,0.06);

        .logo {
            position: absolute;
            left: 50%;
            width: 100px;
            margin-left: -50px;
            top: -50px;
        }

        .logo-title {
            font-weight: normal;
            font-size: 25px;
            color: rgba(0,0,0,0.85);
            width: 186px;
            margin-bottom: 30px;

            .title {
                font-family: Alimama ShuHeiTi, Alimama ShuHeiTi;
                font-weight: bold;
                font-size: 40px;
                color: rgba(0,0,0,0.85);
                line-height: 40px;
                letter-spacing: 4px;
                padding-bottom: 20px;
                margin-bottom: 20px;
                border-bottom: 1px solid rgba(0,0,0,0.06);
            }
        }
    }

    .svg-container {
        padding: 6px 5px 6px 15px;
        color: $dark_gray;
        vertical-align: middle;
        width: 30px;
        display: inline-block;
    }

    .show-pwd {
        position: absolute;
        right: 10px;
        top: 7px;
        font-size: 16px;
        color: $dark_gray;
        cursor: pointer;
        user-select: none;
    }
}
</style>

<template>
    <div style="display: none">
        <template v-if="config.type === 'dialog'">
            <el-dialog
                modal-append-to-body
                append-to-body
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :visible.sync="show"
                :title="config.title"
                :show-close="!btnLoading"
                :width="config.width"
                @closed="onCancel('close')"
            >
                <section v-loading="loading" class="dialog-main">
                    <div class="dialog-container" :style="contentStyle">
                        <slot></slot>
                    </div>
                    <div slot="footer" class="dialog-footer-el" v-if="showFooter">
                        <el-button v-if="config.cancelText" :loading="rejectLoading" @click="onCancel">{{
                            config.cancelText
                        }}</el-button>
                        <el-button v-if="config.confirmText" :loading="btnLoading" type="primary" @click="onConfirm">{{
                            config.confirmText
                        }}</el-button>
                    </div>
                </section>
            </el-dialog>
        </template>
        <template v-else-if="config.type === 'drawer'">
            <el-drawer
                append-to-body
                :wrapperClosable="false"
                :close-on-press-escape="false"
                :visible.sync="show"
                :title="config.title"
                :show-close="!btnLoading"
                :size="config.width"
                @closed="onCancel"
            >
                <section v-loading="loading" class="dialog-main">
                    <div class="dialog-container" style="max-height: calc(100vh - 120px)" :style="contentStyle">
                        <slot></slot>
                    </div>
                    <div slot="footer" class="dialog-footer" v-if="showFooter">
                        <el-button v-if="config.cancelText" :loading="btnLoading" @click="onCancel">{{
                            config.cancelText
                        }}</el-button>
                        <el-button v-if="config.confirmText" :loading="btnLoading" type="primary" @click="onConfirm">{{
                            config.confirmText
                        }}</el-button>
                    </div>
                </section>
            </el-drawer>
        </template>
    </div>
</template>

<script lang="js">
export default {
    props: {
        done: {
            type: Function | null,
            default: null,
        },
        reject: {
            type: Function | null,
            default: null,
        },
        cancel: {
            type: Function | null,
            default: null,
        },
    },
    data() {
        return {
            show: true,
            btnLoading: false,
            rejectLoading: false,
            elFormsInstance: [],
            loading: true,
            config: {
                showCancel: true,
                showConfirm: true,
                confirmText: '确 定',
                cancelText: '取 消',
                title: '标题',
                height: '100%',
                width: '800px',
                type: 'dialog',
            },
        };
    },
    computed: {
        showFooter() {
            return this.config.showCancel || this.config.showConfirm;
        },
        contentStyle() {
            return {
                height: this.height,
            };
        },
    },
    updated() {
        if (this.elFormsInstance.length == 0) {
            this.getForms().then((list) => {
                this.elFormsInstance = list;
                this.loading = false;
            });
        }
    },
    methods: {
        getForms() {
            let elForms = [];
            // 获取所有子孙ElForm组件，用于后续的表单验证
            function getAllChildForm(children) {
                for (let i = 0; i < children.length; i++) {
                    const element = children[i];
                    if (element.$options.name == 'ElForm') {
                        elForms.push(element);
                    }
                    if (element.$children && element.$children.length > 0) {
                        getAllChildForm(element.$children);
                    }
                }
            }
            return new Promise((resolve) => {
                this.$nextTick(() => {
                    getAllChildForm(this.$children);
                    resolve(elForms);
                });
            });
        },
        async onCancel(val) {
            if(this.reject && val != 'close') {
                this.rejectLoading = !this.rejectLoading;
                try {
                    if (this.reject) {
                        const result = await this.reject();

                        // true 需要关闭弹框
                        if (result) {
                            this.show = !this.show;
                            return;
                        }
                        // 否者不关闭弹框，并且取消按钮加载状态
                        else {
                            this.rejectLoading = !this.rejectLoading;
                        }
                    } else {
                        this.show = !this.show;
                    }
                } catch (e) {
                    this.rejectLoading = !this.rejectLoading;
                }
            } else {
                if(this.cancel) await this.cancel()
                this.show = !this.show;
                this.onClosed()
                console.log('3333333333333取消')
            }
            
        },
        async onConfirm() {
            this.btnLoading = !this.btnLoading;
            // 有表单验证或者需要做逻辑处理
            if (this.elFormsInstance.length > 0 || this.done) {
                try {
                    await Promise.all(this.elFormsInstance.map((form) => form.validate()));
                    if (this.done) {
                        const result = await this.done();
                        console.log('888888888888888888result', result)
                        // true 需要关闭弹框
                        if (result) {
                            this.show = !this.show;
                            return;
                        }
                        // 否者不关闭弹框，并且取消按钮加载状态
                        else {
                            this.btnLoading = !this.btnLoading;
                        }
                    } else {
                        this.show = !this.show;
                    }
                } catch (e) {
                    this.btnLoading = !this.btnLoading;
                }
            } else if (this.done) {
                try {
                    const result = await this.done();

                    // true 需要关闭弹框
                    if (result) {
                        this.show = !this.show;
                        return;
                    }
                    // 否者不关闭弹框，并且取消按钮加载状态
                    else {
                        this.btnLoading = !this.btnLoading;
                    }
 
                } catch (e) {
                    this.btnLoading = !this.btnLoading;
                }
            }
            // 关闭弹框
            else {
                this.show = !this.show;
            }
        },
        onClosed() {
            if (this.$parent) {
                // 通知父组件，需要销毁组件
                this.$parent.$emit('destroy');
                this.clearValidate()
            }
        },
        clearValidate() {
            if (this.elFormsInstance.length > 0) {
                this.elFormsInstance.forEach((element) => element.clearValidate());
            }
        },
        validateField(props) {
            const forms = this.elFormsInstance.filter((item) => {
                if (item.rules) {
                    return Reflect.has(item.rules, props);
                }
                return false;
            });
            forms.forEach((item) => {
                item.validateField(props);
            });
        },
        resetFields() {
            if (this.elFormsInstance.length > 0) {
                this.elFormsInstance.forEach((element) => element.resetFields());
            }
        },
    },
    destroyed() {
        document.body.removeChild(this.$el);
    },
};
</script>

<style lang="scss" scoped>
::v-deep .el-dialog {
    border-radius: 12px;
    overflow: hidden;
    .el-dialog__header {
        padding: 0 15px !important;
        height: 56px;
        line-height: 56px;
        font-size: 16px;
        color: #303133;
        background: linear-gradient( 180deg, #F7FAFF 0%, #FFFFFF 25%);
        
        .el-dialog__title {
            display: flex;
            align-items: center;
            font-size: 16px;
            color: rgba(0,0,0,0.85);
            line-height: 56px;
            letter-spacing: 2px;
        }
        .el-dialog__headerbtn {
            top: 16px;
            font-size: 20px;
            font-weight: bold;
            .el-dialog__close {
                color: rgba(0,0,0,0.85);
            }
        }
    }
}
.dialog-footer-el {
    padding-top: 10px;
    display: flex;
    border-top: 1px solid rgba(0,0,0,0.06);
    margin: 0 -16px -10px;
    padding-right: 16px;
    justify-content: flex-end;
    .el-button--small {
        // flex: 1;
        // padding: 14px 15px;
        font-size: 14px;
        border-radius: 8px;
        
    }
    .el-button--primary {
        background: url(../../assets/dialog-btn.png) no-repeat center;
        background-size: cover;
        border-color: none;
        // margin-right: 16px;
    }
    .el-button--primary:focus,
    .el-button--primary:hover {
        background: url(../../assets/dialog-btn.png) no-repeat center;
        background-size: cover;
        border-color: none;
        opacity: 0.9;
    }
}
.dialog-container {
    max-height: 60vh;
    overflow-y: auto;
    overflow-x: hidden;
}
.dialog-footer {
    text-align: right;
}
.el-drawer__body {
    .dialog-main {
        padding-bottom: 54px;
        padding-inline: 20px;
        min-height: 100%;
    }
    .dialog-footer {
        text-align: right;
        position: absolute;
        bottom: 0;
        left: 0;
        width: 100%;
        padding: 10px;
        border: 1px solid #eee;
    }
}
</style>

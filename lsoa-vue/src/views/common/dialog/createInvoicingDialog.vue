<template>
    <el-dialog
        modal-append-to-body
        append-to-body
        :close-on-click-modal="false"
        :close-on-press-escape="false"
        :visible.sync="show"
        :title="config.title"
        :show-close="!btnLoading"
        :width="config.width"
        :before-close="onClosed"
    >
        <section class="dialog-main">
            <div class="dialog-container">
                <div class="open-book-wrap">
                    <div class="step-wrap">
                        <div class="step1" :class="{ 'step1-active': step == 1 }">
                            <span class="num">1</span>
                            <span class="name">开票人信息</span>
                        </div>
                        <div
                            class="step2"
                            :class="{ 'step2-over': step > 2, 'step2-active': step == 2 }"
                        >
                            <span class="num">2</span>
                            <span class="name">发票类型信息</span>
                        </div>
                        <div
                            class="step2"
                            :class="{ 'step2-over': step > 3, 'step2-active': step == 3 }"
                        >
                            <span class="num">3</span>
                            <span class="name">联系人信息</span>
                        </div>
                        <div class="step2" :class="{ 'step2-active': step == 4 }">
                            <span class="num">4</span>
                            <span class="name">收件人信息</span>
                        </div>
                    </div>
                    <div>
                        <el-form
                            ref="formLine"
                            label-position="top"
                            label-width="90px"
                            :model="form"
                            :rules="rules"
                        >
                            <div v-show="step == 1">
                                <el-form-item label="抬头类型" prop="titleType">
                                    <el-radio-group v-model="form.titleType">
                                        <el-radio label="1">个人 </el-radio>
                                        <el-radio label="2"> 单位</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                                <el-form-item label="抬头名称" prop="title">
                                    <el-input
                                        placeholder="请输入抬头名称"
                                        v-model="form.title"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item v-if="form.titleType == 2" label="单位税号" prop="number">
                                    <el-input
                                        placeholder="请输入单位税号"
                                        v-model="form.number"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item v-if="form.titleType == 2" label="注册地址" prop="sort">
                                    <el-input
                                        placeholder="请输入注册地址"
                                        v-model="form.registeredAdd"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item v-if="form.titleType == 2" label="注册电话" prop="sort">
                                    <el-input
                                        placeholder="请输入注册电话"
                                        v-model="form.registerTel"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item
                                    label="开户银行"
                                    v-if="form.titleType == 2"
                                >
                                    <el-input
                                        placeholder="请输入开户银行"
                                        v-model="form.bankDeposit"
                                        v-if="form.titleType == 2"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item
                                    label="银行账号"
                                    v-if="form.titleType == 2"
                                >
                                    <el-input
                                        placeholder="请输入银行账号"
                                        v-model="form.bankAccount"
                                    ></el-input>
                                </el-form-item>
                            </div>
                            <div v-show="step == 2">
                                <el-form-item label="发票类型" prop="invoiceType">
                                    <el-radio-group v-model="form.invoiceType">
                                        <el-radio label="1">增值税普通发票</el-radio>
                                        <el-radio label="2">专用发票</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                                <el-form-item label="开票金额" prop="invoiceMoney">
                                    <el-input-number
                                        v-model="form.invoiceMoney"
                                        size="small"
                                        :controls="false"
                                        :precision="2"
                                        placeholder="请输入开票金额"
                                        :min="1"
                                        clearable
                                    ></el-input-number>
                                </el-form-item>
                                <el-form-item label="开票日期" prop="invoiceTime">
                                    <el-date-picker
                                        v-model="form.invoiceTime"
                                        value-format="yyyy-MM-dd"
                                        style="width: 100%"
                                        type="date"
                                        placeholder="请输入开票日期">
                                    </el-date-picker>
                                </el-form-item>
                                <el-form-item label="发票内容" prop="content">
                                    <el-input
                                        placeholder="请输入发票内容"
                                        v-model="form.content"
                                        type="textarea"
                                        :rows="4"
                                        :maxlength="200"
                                    ></el-input>
                                </el-form-item>
                            </div>
                            <div v-show="step == 3">
                                <el-form-item label="联系人姓名" prop="contactPerson">
                                    <el-input
                                        placeholder="请输入联系人姓名"
                                        v-model="form.contactPerson"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item label="联系电话" prop="contactPhone">
                                    <el-input
                                        placeholder="请输入联系电话"
                                        v-model="form.contactPhone"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item label="电子邮箱" prop="mail">
                                    <el-input
                                        placeholder="请输入电子邮箱"
                                        v-model="form.mail"
                                    ></el-input>
                                </el-form-item>
                            </div>
                            <div v-show="step == 4">
                                <el-form-item label="收件地址" prop="address">
                                    <el-input
                                        placeholder="请输入收件地址"
                                        v-model="form.address"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item label="收件人" prop="addressee">
                                    <el-input
                                        placeholder="请输入收件人"
                                        v-model="form.addressee"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item label="联系电话" prop="addresseePhone">
                                    <el-input
                                        placeholder="请输入联系电话"
                                        v-model="form.addresseePhone"
                                    ></el-input>
                                </el-form-item>
                            </div>
                        </el-form>
                    </div>
                </div>
            </div>
            <div slot="footer" class="dialog-footer-el">
                <el-button @click="onCancel" v-if="step > 1"
                    >上一步</el-button
                >
                <el-button :loading="btnLoading" :disabled="btnLoading" type="primary" @click="onConfirm('formLine')">{{
                    step > 3 ? '提交' : '下一步'
                }}</el-button>
            </div>
        </section>
    </el-dialog>
</template>
<script lang="js">
import { saveOrUpdate, getEntity, getInvoiceList } from '@/api/invoice';

export default {
    data() {
        return {
            form: {
                titleType:'1',
                invoiceType: '1',
                title:'',
                number: '',
                invoiceMoney:undefined,
                invoiceTime:'',
                content:'',
                contactPerson: '',
                contactPhone: '',
                mail:'',
            },
            step: 1,
            show: false,
            btnLoading: false,
            elFormsInstance: [],
            config: {
                showCancel: true,
                showConfirm: true,
                confirmText: '确 定',
                cancelText: '取 消',
                title: '开票信息',
                height: '660px',
                width: '960px',
                type: 'dialog',
            },
            detailInfo: {},
            rules: {
                titleType: [{ required: true, message: '请选择', trigger: 'change' }],
                title: [{ required: true, message: '请输入', trigger: 'blur' }],
                // number: [{ required: true, message: '请输入', trigger: 'blur' }],
                invoiceType: [{ required: true, message: '请选择', trigger: 'change' }],
                invoiceMoney: [{ required: true, message: '请输入', trigger: 'blur' }],
                // invoiceTime: [{ required: true, message: '请输入', trigger: 'blur' }],
                // content: [{ required: true, message: '请输入', trigger: 'blur' }],
                // contactPerson: [{ required: true, message: '请输入', trigger: 'blur' }],
                contactPhone: [
                    { required: true, message: '请输入', trigger: 'blur' },
                    {
                        validator: function (rule, value, callback) {
                            if (/^1[3456789]\d{9}$/.test(value) == false && /^((0\d{2,3})-)?(\d{7,8})$/.test(value) == false) {
                                callback(new Error('请输入正确的联系方式'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur',
                    },
                ],
                // mail: [
                //     { required: true, message: '请输入', trigger: 'blur' },
                //     {
                //         validator: function (rule, value, callback) {
                //             if (/^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/.test(value) == false) {
                //                 callback(new Error('请输入正确的邮箱'));
                //             } else {
                //                 callback();
                //             }
                //         },
                //         trigger: 'blur',
                //     },
                // ],
            },
            id: '',
        };
    },
    methods: {
        edit(row) {
            console.log('9999999999999999row', row)
            this.form = {}
            this.step = 1
            this.show = true;
            this.form.caseId = row.id
            this.form.invoiceMoney = row.money ? row.money : undefined
            if(row.invoice == 4) {
                getEntity({id: row.invoiceId}).then(res => {
                    this.form = res.data
                })
            } else if (row.invoice == 3) {
                // 获取上次开票的代理人信息
                getInvoiceList({id: row.id}).then(res => {
                    const {titleType, title, invoiceType, contactPhone} = res.data[0]
                    this.form  = {
                        titleType,
                        title,
                        invoiceType,
                        contactPhone,
                        caseId: row.id
                    }
                })
            }
        },
        onClosed() {
            this.show = false;
            this.step = 1;
        },
        onConfirm(formName) {
            if(this.step>= 4) {
                this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.btnLoading = true;
                    
                    saveOrUpdate(this.form).then((res) => {
                        if (res.code == 0) {
                            this.$message({
                                type: "success",
                                message: "开票成功!"
                            });
                            this.show = false;
                            this.$emit('submit')
                        }
                    }).finally(() => {
                        setTimeout(() => {
                            this.btnLoading = false;
                        }, 1000)
                    });
                    ;
                } else {
                    this.$message.warning('请检查必填字段是否填写')
                    return false;
                }
            });
            } else {
                this.step = this.step + 1
            }
            
        },
        onCancel() {
            this.step = this.step - 1
        },
    },
};
</script>
<style scoped lang="scss">
::v-deep .el-dialog {
    border-radius: 12px;
    overflow: hidden;
    .el-dialog__header {
        padding: 0 15px !important;
        height: 56px;
        line-height: 56px;
        font-size: 16px;
        color: #303133;
        background: linear-gradient(180deg, #f7faff 0%, #ffffff 25%);

        .el-dialog__title {
            display: flex;
            align-items: center;
            font-size: 16px;
            color: rgba(0, 0, 0, 0.85);
            line-height: 56px;
            letter-spacing: 2px;
        }
        .el-dialog__headerbtn {
            top: 16px;
            font-size: 20px;
            font-weight: bold;
            .el-dialog__close {
                color: rgba(0, 0, 0, 0.85);
            }
        }
    }
}
.dialog-footer-el {
    padding-top: 10px;
    display: flex;
    border-top: 1px solid rgba(0, 0, 0, 0.06);
    margin: 0 -16px -10px;
    justify-content: flex-end;
    .el-button--small {
        // flex: 1;
        // padding: 14px 15px;
        font-size: 14px;
        border-radius: 8px;
    }
    .el-button--primary {
        background: url(../../../assets/dialog-btn.png) no-repeat center;
        background-size: cover;
        border-color: none;
        margin-right: 16px;
    }
    .el-button--primary:focus,
    .el-button--primary:hover {
        background: url(../../../assets/dialog-btn.png) no-repeat center;
        background-size: cover;
        border-color: none;
        opacity: 0.9;
    }
}
.dialog-container {
    height: 60vh;
    overflow: auto;
}
.dialog-footer {
    text-align: right;
}

</style>
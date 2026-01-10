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
                    <!-- <div class="step-wrap">
                        <div class="step1" :class="{ 'step1-active': step == 1 }">
                            <span class="num">1</span>
                            <span class="name">委托人信息</span>
                        </div>
                        <div
                            class="step2"
                            :class="{ 'step2-over': step > 2, 'step2-active': step == 2 }"
                        >
                            <span class="num">2</span>
                            <span class="name">函件信息</span>
                        </div>
                        <div
                            class="step2"
                            :class="{ 'step2-over': step > 3, 'step2-active': step == 3 }"
                        >
                            <span class="num">3</span>
                            <span class="name">收件人信息</span>
                        </div>
                        <div class="step2" :class="{ 'step2-over': step > 4, 'step2-active': step == 4 }">
                            <span class="num">4</span>
                            <span class="name">格式要求</span>
                        </div>
                        <div class="step2" :class="{ 'step2-active': step == 5 }">
                            <span class="num">5</span>
                            <span class="name">开函文件</span>
                        </div>
                    </div> -->
                    <div>
                        <el-form
                            ref="formLine"
                            label-position="top"
                            label-width="90px"
                            :model="form"
                            :rules="rules"
                        >
                            <div>
                                <el-form-item label="委托人" prop="personType">
                                    <!-- 1自然人 2法人 3个体工商户 4受害人 5顾问单位 -->
                                    <el-radio-group @change="handlePerson" v-model="form.personType" :disabled="form.letter == 3 || form.letter == 4">
                                        <el-radio label="1">自然人 </el-radio>
                                        <el-radio label="2">法人</el-radio>
                                        <el-radio label="3">个体工商户</el-radio>
                                        <el-radio label="4">受害人</el-radio>
                                        <el-radio label="5">顾问单位</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                                <el-form-item prop="name" v-if="form.personType == 1 || form.personType == 4" label="名称">
                                    <el-input
                                        placeholder="请输入名称"
                                        v-model="form.name"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item prop="code" v-if="form.personType == 1 || form.personType == 4" label="身份证号">
                                    <el-input
                                        placeholder="请输入身份证号"
                                        v-model="form.code"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item prop="name" v-if="!(form.personType == 1 || form.personType == 4)" label="单位名称">
                                    <el-input
                                        placeholder="请输入单位名称"
                                        v-model="form.name"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item prop="code1" v-if="!(form.personType == 1 || form.personType == 4)" label="统一代码">
                                    <el-input
                                        placeholder="请输入统一代码"
                                        v-model="form.code1"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item prop="phone" label="联系方式">
                                    <el-input
                                        placeholder="请输入联系方式"
                                        v-model="form.phone"
                                    ></el-input>
                                </el-form-item>
                            </div>
                            <!-- <div v-show="step == 2">
                                <el-form-item label="函件类型" prop="type">
                                    <el-radio-group v-model="form.type">
                                        <el-radio label="1">刑函</el-radio>
                                        <el-radio label="2">仲裁函</el-radio>
                                        <el-radio label="3">民函件</el-radio>
                                        <el-radio label="4">刑函(法援)</el-radio>
                                        <el-radio label="5">破产清算</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                                <el-form-item label="相关案件" prop="caseName">
                                    <el-input
                                        placeholder="请输入相关案件"
                                        v-model="form.caseName"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item label="事项编码" prop="caseCode">
                                    <el-input
                                        placeholder="请输入事项编码"
                                        v-model="form.caseCode"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item label="用印类型" prop="printType">
                                    <el-select
                                        v-model="form.printType"
                                        placeholder="请选择用印类型"
                                        clearable
                                        filterable
                                        size="small"
                                    >
                                    <el-option
                                        v-for="(item, index) in printTypeList"
                                        :key="index"
                                        :label="item.dictLabel"
                                        :value="item.dictValue"
                                    ></el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="用途说明" prop="useing">
                                    <el-input
                                        placeholder="请输入用途说明"
                                        v-model="form.useing"
                                        type="textarea"
                                        :maxlength="200"
                                    ></el-input>
                                </el-form-item>
                            </div>
                            <div v-show="step == 3">
                                <el-form-item label="收件方" prop="addressType">
                                    <el-radio-group v-model="form.addressType">
                                        <el-radio label="1">个人 </el-radio>
                                        <el-radio label="2"> 单位</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                                <el-form-item label="姓名" prop="addressee" v-if="form.addressType == 1">
                                    <el-input
                                        placeholder="请输入姓名"
                                        v-model="form.addressee"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item label="单位名称" prop="addressee" v-if="form.addressType == 2">
                                    <el-input
                                        placeholder="请输入单位名称"
                                        v-model="form.addressee"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item label="收件地址" prop="address">
                                    <el-input
                                        placeholder="请输入收件地址"
                                        v-model="form.address"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item label="联系方式" prop="addresseePhone">
                                    <el-input
                                        placeholder="请输入联系方式"
                                        v-model="form.addresseePhone"
                                    ></el-input>
                                </el-form-item>
                            </div>
                            <div v-show="step == 4">
                                <el-form-item label="特殊格式要求" prop="specialDemand">
                                    <el-input
                                        placeholder="请输入特殊格式要求"
                                        v-model="form.specialDemand"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item label="紧急程度" prop="level">
                                    <el-select
                                        v-model="form.level"
                                        placeholder="请选择紧急程度"
                                        clearable
                                        size="small"
                                    >
                                        <el-option label="普通" value="普通" ></el-option>
                                        <el-option label="紧急" value="紧急" ></el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="份数要求" prop="num">
                                    <el-input
                                        placeholder="请输入份数要求"
                                        v-model="form.num"
                                    ></el-input>
                                </el-form-item>
                                <el-form-item label="送达方式" prop="sendType">
                                    <el-input
                                        placeholder="请输入送达方式"
                                        v-model="form.sendType"
                                    ></el-input>
                                </el-form-item>
                            </div>
                            <div v-show="step == 5">
                                <handleRequired id="1" :detailId="`${form.caseId}`" :fileList="form.fileList" @changeFile="handleFiles"></handleRequired>
                            </div> -->
                        </el-form>
                    </div>
                </div>
            </div>
            <div slot="footer" class="dialog-footer-el">
                <el-button @click="onCancel"
                    >取消</el-button
                >
                <el-button :loading="btnLoading" type="primary" @click="onConfirm('formLine')">提交</el-button>
            </div>
        </section>
    </el-dialog>
</template>
<script lang="js">
import { saveOrUpdate, getEntity, getClient, getLetterList } from '@/api/letter';
import handleRequired from '@/views/common/handleRequired.vue';
import { getRedisDictList } from '@/api/common';
export default {
    components: {
        handleRequired
    },
    data() {
        return {
            form: {
                caseCode: '',
                personType: '1',
                type: '1',
                name: '',
                code: '',
                code1: '',
                sex: '',
                phone:'',
                files:[],
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
                title: '开函信息',
                height: '660px',
                width: '960px',
                type: 'dialog',
            },
            detailInfo: {},
            rules: {
                personType: [{ required: true, message: '请选择', trigger: 'change' }],
                name: [{ required: true, message: '请输入', trigger: 'blur' }],
                // code1: [{ required: true, message: '请输入', trigger: 'blur' }],
                phone: [
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
                printType: [{ required: true, message: '请选择', trigger: 'change' }],
                type: [{ required: true, message: '请选择', trigger: 'change' }],
                // addressee: [{ required: true, message: '请输入', trigger: 'blur' }],
                // address: [{ required: true, message: '请输入', trigger: 'blur' }],
                // addresseePhone: [
                //     { required: true, message: '请输入', trigger: 'blur' },
                //     {
                //         validator: function (rule, value, callback) {
                //             if (/^1[345678]\d{9}$/.test(value) == false && /^((0\d{2,3})-)?(\d{7,8})$/.test(value) == false) {
                //                 callback(new Error('请输入正确的联系方式'));
                //             } else {
                //                 callback();
                //             }
                //         },
                //         trigger: 'blur',
                //     },
                // ],
                level: [{ required: true, message: '请选择', trigger: 'change' }],
            },
            id: '',
            printTypeList:[],
            clientInfo:{},
        };
    },
    methods: {
        getInfo() {
            getRedisDictList({ dictType: 'printType' }).then((res) => {
                this.printTypeList = res.data;
            });
        },
        edit(row) {
            console.log('33333333333333row', row)
            this.show = true;
            this.form.caseId = row.id
            this.form.caseCode = row.caseNum
            
            this.getInfo()
            getClient({caseId: this.form.caseId}).then(res => {
                console.log('测试委托人数据', res)
                this.clientInfo = {
                    person: res.data.person || {},
                    victim: res.data.victim || {},
                    corporation: res.data.corporation|| {},
                    households: res.data.households || {},
                    customer: res.data.customer || {},
                }

                if(row.letter == 4) {
                    getEntity({id: row.summonsId}).then(res => {
                        this.form = {...res.data, code1: '', letter: row.letter}
                        if(this.form.personType == 2) { 
                            this.clientInfo.corporation.name = this.form.name
                            this.clientInfo.corporation.code = this.form.code
                            this.clientInfo.corporation.phone = this.form.phone
                        } else if (this.form.personType == 3) {
                            this.clientInfo.households.name = this.form.name
                            this.clientInfo.households.code = this.form.code
                            this.clientInfo.households.phone = this.form.phone
                        }else if (this.form.personType == 5) {
                            this.clientInfo.customer.name = this.form.name
                            this.clientInfo.customer.code = this.form.code
                            this.clientInfo.customer.phone = this.form.phone
                        }else if(this.form.personType == 1){
                            this.clientInfo.person.name = this.form.name
                            this.clientInfo.person.code = this.form.code
                        }else{
                            this.clientInfo.victim.name = this.form.name
                            this.clientInfo.victim.code = this.form.code
                        }
                        console.log('444444444444444letter', this.form, row.letter)
                        this.handlePerson(this.form.personType)
                    })
                } else if(row.letter == 3){
                    getLetterList({id: row.id}).then(res => {
                        const {personType, name, code, phone, type } = res.data[0]
                        this.form = {
                            personType,
                            name,
                            code: ['1', '4'].includes(personType) ? code : '',
                            code1: ['1', '4'].includes(personType) ? '' : code,
                            phone,
                            type,
                            caseId: row.id,
                            letter: row.letter
                        }
                    })
                }
            })
            
        },
        handlePerson(val) {
            if(val == 1) {
                const {name, code, sex,phone} = this.clientInfo.person
                this.form.name = name
                this.form.code = code
            }else if(val == 2){
                const {name, code, sex,phone} = this.clientInfo.corporation
                this.form.name = name
                this.form.code1 = code
                this.form.phone = phone
            }else if(val == 3){
                const {name, code, sex,phone} = this.clientInfo.households
                this.form.name = name
                this.form.code1 = code
                this.form.phone = phone
            }else if(val == 5){
                const {name, code, sex,phone} = this.clientInfo.customer
                this.form.name = name
                this.form.code1 = code
                this.form.phone = phone
            } else {
                const {name, code, sex,phone} = this.clientInfo.victim
                this.form.name = name
                this.form.code = code
            }
        },
        onClosed() {
            this.show = false;
            this.step = 1;
        },
        onConfirm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.btnLoading = true;
                    console.log('4444444444444this.form', this.form)
                    if(this.form.personType == 2 || this.form.personType == 3 || this.form.personType == 5) { 
                        this.form.code = this.form.code1
                    //     this.form.name = this.form.name1
                    } 
                    setTimeout(() => {
                        saveOrUpdate(this.form).then((res) => {
                            if (res.code == 0) {
                                this.$message({
                                    type: "success",
                                    message: "开函成功!"
                                });
                                this.$emit('submit')
                                this.step = 1
                                this.show = false;
                            }
                        });
                        this.btnLoading = false;
                    }, 1000);
                } else {
                    this.$message.warning('请检查必填字段是否填写')
                    return false;
                }
            });
            
        },
        onCancel() {
            this.show = false;
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
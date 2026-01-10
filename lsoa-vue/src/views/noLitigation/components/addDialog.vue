<template>
    <el-dialog
        modal-append-to-body
        append-to-body
        :close-on-click-modal="false"
        :close-on-press-escape="false"
        :visible.sync="show"
        :title="title"
        :show-close="!btnLoading"
        :before-close="onClosed"
    >
    <el-form ref="form" :model="form" label-width="140px" :rules="rules" label-position="top">
            <el-row :gutter="16">
                <el-col :span="8">
                    <el-form-item label="公司名称" prop="companyName">
                        <!-- <el-input v-model="form.companyName" placeholder="请输入公司名称" :disabled="form.isDetail"></el-input> -->
                        <el-autocomplete
                            :disabled="form.isDetail"
                            class="inline-input"
                            v-model="form.companyName"
                            style="width: 100%"
                            :fetch-suggestions="querySearch"
                            placeholder="请输入内容"
                            @select="handleSelect"
                        ></el-autocomplete>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="法人" prop="legalPerson">
                        <el-input v-model="form.legalPerson" placeholder="请输入法人" :disabled="form.isDetail"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="联系电话" prop="phone">
                        <el-input v-model="form.phone" placeholder="请输入联系电话" :disabled="form.isDetail"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="社会统一信用代码" prop="companyCode">
                        <el-input v-model="form.companyCode" :placeholder="form.isDetail ? '' :'请输入社会统一信用代码'" :disabled="form.isDetail"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="负责律师（可多选）" prop="lawyerId">
                        <el-select
                        v-model="form.lawyerId"
                        placeholder="请选择律师"
                        key="userId"
                        clearable
                        multiple
                        filterable
                        size="small"
                        :disabled="form.isDetail"
                    >
                        <el-option v-for="(item,index) in lawyerList" :key="index" :label="item.realName" :value="`${item.id}`"></el-option>
                    </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="非诉讼业务类型" prop="businessType">
                        <el-select
                        v-model="form.businessType"
                        :placeholder="form.isDetail ? '' :'请选择非诉讼业务类型'"
                        key="userId"
                        clearable
                        filterable
                        :disabled="form.isDetail"
                        size="small"
                    >
                        <el-option v-for="(item,index) in businessList" :key="index" :label="item.dictLabel" :value="Number(item.dictValue)"></el-option>
                    </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="24" class="file-name">
                    <el-form-item label="合同文件" prop="contractFile">
                        <handleRequired id="6" :limit="true" :detail="form.isDetail" :fileList="form.contractFile" @changeFile="handleFiles"></handleRequired>
                    </el-form-item>
                </el-col>
                <el-col :span="24" v-if="form.isDetail && form.invoiceFile && form.invoiceFile.length">
                    <el-form-item label="发票文件" prop="invoiceFile">
                        <handleRequired id="6" :detail="form.isDetail" :fileList="form.invoiceFile" @changeFile="handleFiles"></handleRequired>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer-el">
                <el-button @click="onCancel">取消</el-button>
                <el-button v-if="!form.isDetail" :loading="btnLoading" type="success" @click="onSave">保存</el-button>
                <el-button v-if="!form.isDetail" :loading="btnLoading" type="primary" @click="onConfirm">提交</el-button>
                <el-button v-if="form.isDetail === 'check'" :loading="btnLoading" @click="handleCheck(form, 2)">回退</el-button>
                <el-button v-if="form.isDetail === 'check'" :loading="btnLoading" type="primary" @click="handleCheck(form, 1)">通过</el-button>

            </div>
    </el-dialog>
</template>
<script lang="js">
import { save, submit, check } from '@/api/noLitigation';
import { getNormalUserList } from "@/api/statistics";
import { getRedisDictList } from '@/api/common';
import { page } from '@/api/agreementCustomer';
import handleRequired from '@/views/common/handleRequired.vue';
export default {
    components: {
        handleRequired
    },
    data() {
        return {
            title: '新增非诉讼业务',
            form: {
                lawyerId: []
            },
            rules: {
                legalPerson: [{ required: true, message: '请输入法人', trigger: 'blur' }],
                companyName: [{ required: true, message: '请输入客户名称', trigger: 'change' }],
                creditCode: [{ required: true, message: '请输入社会统一信用代码', trigger: 'change' }],
                time: [{ required: true, message: '请选择服务期限', trigger: 'change' }],
                phone: [
                    { required: true, message: "请输入手机号", trigger: "blur" },
                    {
                        validator: function (rule, value, callback) {
                        if (/^1[3456789]\d{9}$/.test(value) == false) {
                            callback(new Error("请输入正确的手机号"));
                        } else {
                            callback();
                        }
                        },
                        trigger: "blur"
                    }
                ],
                // contractFile:[{ required: true, message: '请选择文件', trigger: 'change' }],
                lawyerId: [{ required: true, message: '请选择律师', trigger: 'blur' }],
            },
            show: false,
            businessList: [],
            lawyerList:[],
            customList:[],
            btnLoading: false,
        };
    },
    mounted() {
        // 律师列表
        getNormalUserList({}).then(res => {
            this.lawyerList = res.data
        })
        // 非诉讼业务类型
        getRedisDictList({dictType: 'NonLitigationBusiness'}).then(res => {
            this.businessList = res.data
        })
    },
    methods: {
        showEdit(row, isDetail) {
            // 请求顾问单位的数据
            page({pageSize: 1000}).then((res) => {
                console.log('333333333333333res.data', res.data)
                this.customList = res.data.records.map(val => {
                    val.value = val.customerName
                    return val
                });
            });
            if (row) {
                console.log('8888888888row', row)
                this.form = {...row, lawyerId: row.lawyerId?`${row.lawyerId}`.split(','): [], isDetail}
                this.show = true
                this.$refs.form?.clearValidate()
                this.title = '编辑非诉讼业务'
                if (isDetail == 'detail') {
                    this.title = '详情'
                }else if (isDetail == 'check') {
                    this.title = '审核'
                }
            } else {
                this.form = {
                    lawyerId: [],
                    legalPerson: '',
                    companyCode: '',
                    phone: '',
                }
                this.show = true
                this.$refs.form?.clearValidate()
                this.title = '新增非诉讼业务'
            }

        },
        querySearch(queryString, cb) {
            var restaurants = this.customList;
            var results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
            // 调用 callback 返回建议列表的数据
            cb(results);
        },
        createFilter(queryString) {
            return (restaurant) => {
            return (restaurant.customerName.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
            };
        },
        handleSelect(item) {
            console.log(item);
            // 法人
            this.form.legalPerson = item.legalPerson
            // 联系电话
            this.form.phone = item.phone
            // 社会统一代码
            this.form.companyCode = item.creditCode
            // 负责律师
            this.form.lawyerId = item.lawyers.map(String)
        },
        sleep(time) {
            return new Promise((resolve) => setTimeout(resolve, time));
        },
        handleFiles(val, arr) {
            this.form.contractFile = []
            arr.map(res => {
                this.form.contractFile.push(res)
            })
        },
        onCancel() {
            this.show = false;
        },
        onClosed() {
            this.show = false;
        },
        handleCheck(row,checkResult) {
            check({id: row.id, checkResult}).then((res) => {
                if (res.code == 0) {
                    this.$message({
                        type: "success",
                        message: checkResult == 1 ? "审核成功!" :"回退成功！"
                    });
                    this.$emit('submit')
                    this.step = 1
                    this.show = false;
                }
            });
            setTimeout(() => {
                this.btnLoading = false;
            }, 1000);
        },
        async onSave() {
            if(!this.form.companyName || !this.form.legalPerson) {
                return this.$message({
                    type: 'error',
                    message: '保存必须填写公司名称、法人!',
                })
            }
            save({...this.form, lawyerId: this.form.lawyerId.join(',')}).then((res) => {
                if (res.code == 0) {
                    this.$message({
                        type: "success",
                        message: "保存成功!"
                    });
                    this.$emit('submit')
                    this.step = 1
                    this.show = false;
                }
            });
            setTimeout(() => {
                this.btnLoading = false;
            }, 1000);
        },
        async onConfirm() {
            this.$refs.form.validate((valid) => {
                if (valid) {
                    if(!this.form.contractFile ||  (this.form.contractFile && this.form.contractFile.length == 0)) {
                        return this.$message({
                                type:"warning",
                                message: "请上传合同文件!"
                            });
                    }
                    this.btnLoading = true;
                    submit({...this.form, lawyerId: this.form.lawyerId.join(',')}).then((res) => {
                        if (res.code == 0) {
                            this.$message({
                                type: "success",
                                message: "提交成功!"
                            });
                            this.$emit('submit')
                            this.step = 1
                            this.show = false;
                        }
                    });
                    setTimeout(() => {
                        this.btnLoading = false;
                    }, 1000);
                } else {
                    return false;
                }
            })
        },
    },
};
</script>
<style scoped lang="scss">
::v-deep {
    .el-form {
        .el-form-item__content {
            width: 100%!important;
        }
    }
}
::v-deep {
    .file-name {
        .el-form-item__label {
            &::before {
                content: '*';
                color: #f56c6c;
                margin-right: 4px;
            }
        }
    }
}
</style>

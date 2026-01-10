<template>
    <DialogContainer class="wrap-height" :done="done" ref="DialogContainer">
        <el-form ref="form" :model="form" label-width="140px" :rules="rules" label-position="top">
            <el-row :gutter="16">
                <el-col :span="8">
                    <el-form-item label="客户名称" prop="customerName">
                        <el-input
                            v-model="form.customerName"
                            placeholder="请输入客户名称"
                            :disabled="form.isDetail"
                        ></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="法人" prop="legalPerson">
                        <el-input v-model="form.legalPerson" :disabled="form.isDetail" :placeholder="form.isDetail?'':'请输入法人'"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="手机号" prop="phone">
                        <el-input v-model="form.phone" :disabled="form.isDetail" placeholder="请输入手机号"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="社会统一信用代码" prop="creditCode">
                        <el-input
                            v-model="form.creditCode"
                            placeholder="请输入社会统一信用代码"
                            :disabled="form.isDetail"
                        ></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="服务有效期" prop="time">
                        <el-date-picker
                            v-model="form.time"
                            style="width: 244px"
                            type="daterange"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            range-separator="~"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            :disabled="form.isDetail"
                            :default-time="['00:00:00', '23:59:59']"
                        >
                        </el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="地址" prop="address">
                        <el-input v-model="form.address" :disabled="form.isDetail" :placeholder="form.isDetail?'':'请输入地址'"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="负责律师（可多选）" prop="lawyers">
                        <el-select
                            v-model="form.lawyers"
                            placeholder="请选择律师"
                            key="userId"
                            clearable
                            multiple
                            filterable
                            size="small"
                            :disabled="form.isDetail"
                        >
                            <el-option
                                v-for="(item, index) in lawyerList"
                                :key="index"
                                :label="item.realName"
                                :value="item.id"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="服务费用（元）" prop="serviceFee">
                        <el-input-number
                            :precision="2"
                            :max="99999999.99"
                            :min="1"
                            :controls="false"
                            :disabled="form.isDetail"
                            v-model="form.serviceFee"
                            :placeholder="form.isDetail?'':'请输入服务费用'"
                        ></el-input-number>
                    </el-form-item>
                </el-col>
                <el-col :span="24" class="file-name" v-if="!form.isDetail || (form.isDetail && form.files && form.files.length > 0)">
                    <el-form-item label="合同文件" prop="files">
                        <handleRequired
                            id="5"
                            :limit="true"
                            :detail="form.isDetail"
                            :fileList="form.files"
                            @changeFile="handleFiles"
                        ></handleRequired>
                    </el-form-item>
                </el-col>
                <el-col :span="24" v-if="form.invoiceFile && form.invoiceFile.length > 0">
                    <el-form-item label="发票文件" prop="invoiceFile">
                        <handleRequired
                            id="5"
                            :limit="true"
                            :detail="form.isDetail"
                            :fileList="form.invoiceFile"
                            @changeFile="handleFiles"
                        ></handleRequired>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <div class="relate-title">
                        <span>关联单位</span>
                        <el-button
                        v-if="!type"
                        icon="el-icon-plus"
                        type="success"
                        size="small"
                        @click="handleAdd"
                        >添加关联单位</el-button
                    >
                    </div>
                    <el-table :data="relatedCompany" style="width: 100%">
                        <el-table-column prop="customerName" label="单位名称" align="center"> </el-table-column>
                        <el-table-column prop="legalPerson" label="法人" align="center"> </el-table-column>
                        <el-table-column prop="phone" label="联系电话" align="center"> </el-table-column>
                        <el-table-column prop="creditCode" label="社会统一信用代码" width="160" align="center"> </el-table-column>
                        <el-table-column prop="address" label="地址" align="center"> </el-table-column>
                        <el-table-column label="操作" width="80" v-if="!type">
                            <template slot-scope="scope">
                                <el-button type="danger" size="mini" plain @click="handleDel(scope.row, scope.$index)"
                                    >删除</el-button
                                >
                            </template>
                        </el-table-column>
                    </el-table>
                    
                </el-col>
            </el-row>
        </el-form>
    </DialogContainer>
</template>

<script>
import DialogContainer from '@/components/DialogFactory/dialog-container.vue';
import { save } from '@/api/agreementCustomer';
import { getNormalUserList } from '@/api/statistics';
import handleRequired from '@/views/common/handleRequired.vue';
import { CreateAddCompany } from './index';

export default {
    components: {
        DialogContainer,
        handleRequired,
    },
    data() {
        return {
            form: {},
            type: '',
            relatedCompany: [],
            rules: {
                customerName: [{ required: true, message: '请输入客户名称', trigger: 'blur' }],
                creditCode: [
                    { required: true, message: '请输入社会统一信用代码', trigger: 'change' },
                ],
                // files: [{ required: true, message: '请上传文件', trigger: 'blur' }],
                time: [{ required: true, message: '请选择服务期限', trigger: 'change' }],
                lawyers: [{ required: true, message: '请选择负责律师', trigger: 'blur' }],
                phone: [
                    { required: true, message: '请输入手机号', trigger: 'blur' },
                    {
                        validator: function (rule, value, callback) {
                            if (/^1[3456789]\d{9}$/.test(value) == false) {
                                callback(new Error('请输入正确的手机号'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur',
                    },
                ],
            },
            lawyerList: [],
            delIds:[], // 删除的关联客户
        };
    },
    mounted() {
        // 律师列表
        getNormalUserList({}).then((res) => {
            this.lawyerList = res.data;
        });
    },
    methods: {
        async done() {
            try {
                const addList = this.relatedCompany.filter(val => !val.id)
                const params = {
                    creditCode: this.form.creditCode,
                    customerName: this.form.customerName,
                    phone: this.form.phone,
                    remark: this.form.remark,
                    serviceFee: this.form.serviceFee,
                    starTime: this.form.time?.[0],
                    endTime: this.form.time?.[1],
                    lawyers: this.form.lawyers,
                    legalPerson: this.form.legalPerson,
                    acList: this.id ? addList: this.relatedCompany,
                    address: this.form.address,
                    files: this.form.files,
                    delIds:this.delIds,
                };
                await save(this.id ? Object.assign({}, params, { id: this.id }) : params);
                this.$emit('submit');
                return true;
            } catch (e) {
                return false;
            }
        },
        sleep(time) {
            return new Promise((resolve) => setTimeout(resolve, time));
        },
        handleFiles(val, arr) {
            console.log('222222222222222val',val, arr)
            this.form.files = []
            arr.map(res => {
                this.form.files.push(res)
            }) 
        },
        handleDel(row, index) {
            const delId = this.relatedCompany.splice(index, 1)
            console.log('6666666delId', delId)
            if(delId[0]?.id) {
                this.delIds.push(delId[0].id)
            }
            
        },
        // 添加关联单位
        handleAdd() {
            CreateAddCompany({}, { title: '新增关联单位' }).$on('submit', (res) => {
                this.relatedCompany.push(res)
            });
        }
    },
};
</script>
<style lang="scss" scoped>
.relate-title {
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 56px;
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
<style lang="scss">
.wrap-height {
    .dialog-container {
        max-height: calc(100vh - 100px);
    }
}

</style>

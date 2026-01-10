<template>
    <DialogContainer :done="done" ref="DialogContainer">
        <el-form ref="form" :model="form" label-width="140px" :rules="rules" label-position="top">
            <el-row :gutter="16">
                <el-col :span="24">
                    <el-form-item label="抬头名称" prop="title">
                        <el-input
                            v-model="form.title"
                            placeholder="请输入抬头名称"
                        ></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="单位税号" prop="number">
                        <el-input v-model="form.number" placeholder="请输入单位税号"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="注册地址" prop="registeredAdd">
                        <el-input v-model="form.registeredAdd" placeholder="请输入注册地址"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="发票类型" prop="invoiceType">
                        <el-radio-group v-model="form.invoiceType">
                            <el-radio label="1">增值税普通发票</el-radio>
                            <el-radio label="2">专用发票</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="开票金额" prop="invoiceMoney">
                        <el-input-number
                                    v-model="form.invoiceMoney"
                                    :controls="false"
                                    :precision="2"
                                    size="small"
                                    placeholder="请输入开票金额"
                                    :min="1"
                                    clearable
                                ></el-input-number>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="发票内容" prop="content">
                        <el-input
                            v-model="form.content"
                            size="small"
                            type="textarea"
                            placeholder="请输入发票内容"
                            clearable
                        />
                    </el-form-item>
                </el-col>
                <el-col :span="24" v-if="form.back">
                    <el-form-item label="退回原因" prop="back">
                        <el-input
                            v-model="form.back"
                            disabled
                            size="small"
                            placeholder="请输入退回原因"
                            clearable
                        />
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
    </DialogContainer>
</template>

<script>
import DialogContainer from '@/components/DialogFactory/dialog-container.vue';
import { getNormalUserList } from '@/api/statistics';
import handleRequired from '@/views/common/handleRequired.vue';
import { saveOrUpdateInvoiceInfo } from '@/api/agreementCustomer';

export default {
    components: {
        DialogContainer,
        handleRequired,
    },
    data() {
        return {
            form: {},
            relatedCompany: [],
            rules: {
                title: [{ required: true, message: '请输入抬头名称', trigger: 'blur' }],
                number: [
                    { required: true, message: '请输入单位税号', trigger: 'blur' },
                ],
                invoiceType: [{ required: true, message: '请选择发票类型', trigger: 'change' }],
                invoiceMoney: [{ required: true, message: '请输入开票金额', trigger: 'blur' }],
                content: [{ required: true, message: '请输入发票内容', trigger: 'blur' }],
            },
            lawyerList: [],
            id: '',
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
                await saveOrUpdateInvoiceInfo({...this.form, caseId: this.id});
                this.$emit('submit');
                return true;
            } catch (e) {
                return false;
            }
        },
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
</style>

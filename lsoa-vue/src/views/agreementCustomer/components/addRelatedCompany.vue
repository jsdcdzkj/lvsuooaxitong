<template>
    <DialogContainer :done="done" ref="DialogContainer">
        <el-form ref="form" :model="form" label-width="140px" :rules="rules" label-position="top">
            <el-row :gutter="16">
                <el-col :span="8">
                    <el-form-item label="单位名称" prop="customerName">
                        <el-input
                            v-model="form.customerName"
                            placeholder="请输入单位名称"
                        ></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="法人" prop="legalPerson">
                        <el-input v-model="form.legalPerson" placeholder="请输入法人"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="联系电话" prop="phone">
                        <el-input v-model="form.phone" placeholder="请输入联系电话"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="社会统一信用代码" prop="creditCode">
                        <el-input
                            v-model="form.creditCode"
                            placeholder="请输入社会统一信用代码"
                        ></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="地址" prop="address">
                        <el-input v-model="form.address" placeholder="请输入地址"></el-input>
                    </el-form-item>
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
                customerName: [{ required: true, message: '请输入客户名称', trigger: 'blur' }],
                creditCode: [
                    { required: true, message: '请输入社会统一信用代码', trigger: 'change' },
                ],
                legalPerson: [{ required: true, message: '请输入法人', trigger: 'blur' }],
                address: [{ required: true, message: '请输入地址', trigger: 'blur' }],
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
                this.$emit('submit', this.form);
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

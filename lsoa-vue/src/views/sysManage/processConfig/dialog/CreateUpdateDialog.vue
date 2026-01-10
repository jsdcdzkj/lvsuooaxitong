<template>
    <DialogContainer :done="handlerDone">
        <el-form :model="form" :rules="rules" label-position="top" label-width="80px">
            <el-form-item label="权限类型" prop="sponsorType">
                <el-select
                    v-model="form.sponsorType"
                    @change="handleType"
                    placeholder="请选择权限类型"
                >
                    <el-option label="人员" :value="1"></el-option>
                    <el-option label="角色" :value="2"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item :label="`${form.nodeType == 1 ?'审核人员':'抄送人员'}`" prop="sponsorValue">
                <el-select
                    v-model="form.sponsorValue"
                    v-if="form.sponsorType == 1"
                    multiple
                    :placeholder="`请选择${form.nodeType == 1 ? '审核人员': '抄送人员'}`"
                >
                    <el-option
                        v-for="item in userList"
                        :key="item.id"
                        :label="item.realName"
                        :value="`${item.id}`"
                    >
                    </el-option>
                </el-select>
                <el-select
                    v-model="form.sponsorValue"
                    v-if="form.sponsorType == 2"
                    multiple
                    :placeholder="`请选择${form.nodeType == 1 ? '审核人员': '抄送人员'}`"
                >
                    <el-option
                        v-for="item in rolesList"
                        :key="item.id"
                        :label="item.roleName"
                        :value="`${item.id}`"
                    >
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item v-if="form.nodeType == 1" label="节点类型" prop="nodeType">
                <el-select
                    v-model="form.nodeType"
                    placeholder="请选择节点类型"
                >
                    <el-option label="审批" value="1"></el-option>
                </el-select>
            </el-form-item>
        </el-form>
    </DialogContainer>
</template>
<script lang="js">
import DialogContainer from '@/components/DialogFactory/dialog-container.vue';
import { nodeSave } from "@/api/processConfig";
import { roleList, userPageList } from '@/api/user';
export default {
    components: { DialogContainer },
    data() {
        return {
            form: {
                sponsorType: 1,
                sponsorValue: '',
                nodeType:'1',
                processId: '',
                sort: ''
            },
            userList:[],
            rolesList:[],
            rules: {
                sponsorType: [{ required: true, message: '请选择', trigger: 'blur' }],
                sponsorValue: [{ required: true, message: '请选择', trigger: 'blur' }],
                nodeType: [{ required: true, message: '请选择', trigger: 'blur' }],
            },
        };
    },
    created() {
        roleList({}).then(res => {
            if(res.code == 0) {
                this.rolesList = res.data
            }
        })
        userPageList({pageIndex: 1,pageSize: 1000}).then(res => {
            if(res.code == 0) {
                this.userList = res.data.records
            }
        })
    },
    methods: {
        handleType(val) {
            this.form.sponsorValue = ''
        },
        async handlerDone() {
            try {
                this.form.approverType = this.form.sponsorType
                this.form.approverValue = this.form.sponsorValue.join(',')
                await nodeSave(this.form);
                this.$emit('submit',this.form);
                return true;
            } catch (e) {
                return false;
            }
        },
    },
};
</script>

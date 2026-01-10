<template>
    <DialogContainer :done="handlerDone">
        <el-form :model="form" :rules="rules" label-position="top" label-width="80px">
            <el-form-item label="权限类型" prop="sponsorType">
                <el-select v-model="form.sponsorType" @change="handleType" placeholder="请选择权限类型">
                    <el-option label="人员" value="1"></el-option>
                    <el-option label="角色" value="2"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="谁可发起" prop="sponsorValue">
                <el-select v-model="form.sponsorValue" v-if="form.sponsorType == 1" multiple placeholder="请选择谁可发起">
                    <el-option
                        v-for="item in userList"
                        :key="item.id"
                        :label="item.realName"
                        :value="`${item.id}`"
                    >
                    </el-option>
                </el-select>
                <el-select v-model="form.sponsorValue" v-if="form.sponsorType == 2" multiple placeholder="请选择谁可发起">
                    
                    <el-option
                        v-for="item in rolesList"
                        :key="item.id"
                        :label="item.roleName"
                        :value="`${item.id}`"
                    >
                    </el-option>
                </el-select>
            </el-form-item>
        </el-form>
    </DialogContainer>
</template>
<script lang="js">
import DialogContainer from '@/components/DialogFactory/dialog-container.vue';
import { roleList, userPageList } from '@/api/user';
import { save } from '@/api/processConfig'
export default {
    components: { DialogContainer },
    data() {
        return {
            form: {
                sponsorType: '',
                sponsorValue: '',
            },
            rules: {
                sponsorType: [{ required: true, message: '请选择', trigger: 'blur' }],
                sponsorValue: [{ required: true, message: '请选择', trigger: 'blur' }],
            },
            id: '',
            rolesList: [],
            userList: [],
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
                this.form.sponsorValue = this.form.sponsorValue.join(',')
                await save(this.form);
                this.$emit('submit',this.form);
                return true;
            } catch (e) {
                return false;
            }
        },
    },
};
</script>

<template>
    <DialogContainer :done="done" ref="DialogContainer">
        <el-form ref="form" :model="form" label-width="100px" :rules="rules">
            <el-form-item label="角色名称" prop="roleName">
                <el-input v-model="form.roleName" placeholder="请输入角色名称"></el-input>
            </el-form-item>
            <el-form-item label="角色编码" prop="roleCode">
                <el-input v-model="form.roleCode" :disabled="!!id" placeholder="请输入角色编码"></el-input>
            </el-form-item>
        </el-form>
    </DialogContainer>
</template>

<script>
import DialogContainer from '@/components/DialogFactory/dialog-container.vue';
import { roleSave } from '@/api/user';

export default {
    components: {
        DialogContainer,
    },
    data() {
        return {
            form: {
                roleCode: '',
                roleName: '',
            },
            id: '',
            rules: {
                roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
                roleCode: [{ required: true, message: '请选择角色编码', trigger: 'change' }],
            },
        };
    },
    mounted() {
    },
    methods: {
        async done() {
            try {
                await roleSave(this.id ? Object.assign({}, this.form, { id: this.id }) : this.form);
                this.$emit('submit');
                return true;
            } catch (e) {
                return false;
            }
        },
        sleep(time) {
            return new Promise((resolve) => setTimeout(resolve, time));
        },
    },
};
</script>

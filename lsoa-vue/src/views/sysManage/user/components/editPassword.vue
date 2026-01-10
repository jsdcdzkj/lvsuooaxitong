<template>
    <DialogContainer :done="done" ref="DialogContainer">
        <el-form ref="form" :model="form" label-width="80px" :rules="rules">
            <!-- 新密码 确认密码 -->
            <el-form-item label="新密码" prop="password">
                <el-input
                    v-model="form.password"
                    placeholder="密码需包含大小写字母、数字和特殊符号，并且长度应在8~16位"
                    type="password"
                    :type="passwordType ? 'password' : 'text'"
                >
                    <template v-slot:suffix>
                        <span class="show-password" @click="passwordType = !passwordType">
                            <svg-icon :icon-class="passwordType ? 'eye' : 'eye-open'" />
                        </span>
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
                <el-input
                    v-model="form.confirmPassword"
                    placeholder="密码需包含大小写字母、数字和特殊符号，并且长度应在8~16位"
                    type="password"
                    :type="passwordTypeSure ? 'password' : 'text'"
                >
                    <template v-slot:suffix>
                        <span class="show-password" @click="passwordTypeSure = !passwordTypeSure">
                            <svg-icon :icon-class="passwordTypeSure ? 'eye' : 'eye-open'" />
                        </span>
                    </template>
                </el-input>
            </el-form-item>
        </el-form>
    </DialogContainer>
</template>

<script>
import DialogContainer from '@/components/DialogFactory/dialog-container.vue';
import { userPassword } from '@/api/user';
export default {
    components: {
        DialogContainer,
    },
    data() {
        return {
            form: {
                password: '',
                confirmPassword: '',
            },
            rules: {
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 8, max: 16, message: '密码长度应在8~16位', trigger: 'blur' },
                    {
                        pattern:
                            /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%#^()*?&,.?":{}|<>\\])[A-Za-z\d@$#^()!%*?&,.?":{}|<>\\]{8,16}$/,
                        message: '密码需包含大小写字母、数字和特殊符号',
                        trigger: 'blur',
                    },
                ],
                confirmPassword: [
                    { required: true, message: '请输入确认密码', trigger: 'blur' },
                    { min: 8, max: 16, message: '密码长度应在8~16位', trigger: 'blur' },
                    {
                        pattern:
                            /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%#^*()?&,.?":{}|<>\\])[A-Za-z\d@$#^!()%*?&,.?":{}|<>\\]{8,16}$/,
                        message: '密码需包含大小写字母、数字和特殊符号',
                        trigger: 'blur',
                    },
                ],
            },
            id: '',
            passwordType: true,
            passwordTypeSure: true,
        };
    },
    methods: {
        async done() {
            console.log(this.id);
            await this.sleep(500);
            if (this.form.password !== this.form.confirmPassword) {
                this.$message.error('两次输入的密码不一致');
                return false;
            }
            await userPassword(Object.assign({}, this.form, { id: this.id }));
            this.$emit('submit');
            // this.$refs.DialogContainer.validateField('name');
            return true;
        },
        sleep(time) {
            return new Promise((resolve) => setTimeout(resolve, time));
        },
    },
};
</script>

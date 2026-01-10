<template>
    <DialogContainer :done="handlerDone">
        <el-form :model="form" :rules="rules" label-position="top" label-width="80px">
            <el-form-item label="流程名称" prop="name">
                <el-input v-model="form.name" placeholder="请输入名称"></el-input>
            </el-form-item>
            <el-form-item label="谁可发起" prop="staff">
                <el-select v-model="form.staff" multiple placeholder="请选择谁可发起">
                    <el-option
                        v-for="item in options"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                    >
                    </el-option>
                </el-select>
            </el-form-item>
            <!-- <el-form-item label="请求方式">
                <el-radio-group>
                    <el-radio label="外网" value="1"> </el-radio>
                    <el-radio label="内网" value="2"> </el-radio>
                </el-radio-group>
            </el-form-item> -->
            <el-form-item label="流程说明" prop="publicKey">
                <el-input
                    v-model="form.publicKey"
                    type="textarea"
                    :rows="4"
                    :maxlength="200"
                    placeholder="请输入流程说明"
                >
                </el-input>
            </el-form-item>
        </el-form>
    </DialogContainer>
</template>
<script lang="js">
import DialogContainer from '@/components/DialogFactory/dialog-container.vue';
import { syssystemSaveUpdate } from '@/api/syssystem';
export default {
    components: { DialogContainer },
    data() {
        return {
            form: {
                name: '',
                code: '',
                privateKey: '',
                publicKey: '',
                staff: '',
            },
            options: [{
                value: '选项1',
                label: '黄金糕'
            }, {
                value: '选项2',
                label: '双皮奶'
            }, {
                value: '选项3',
                label: '蚵仔煎'
            }, {
                value: '选项4',
                label: '龙须面'
            }, {
                value: '选项5',
                label: '北京烤鸭'
            }],
            rules: {
                name: [{ required: true, message: '请输入', trigger: 'blur' }],
                staff: [{ required: true, message: '请输入', trigger: 'blur' }],
                publicKey: [{ required: true, message: '请输入', trigger: 'blur' }],
            },
            id: '',
        };
    },
    methods: {
        async handlerDone() {
            try {
                // await syssystemSaveUpdate(
                //     this.id ? Object.assign({}, this.form, { id: this.id }) : this.form,
                // );
                this.$emit('submit',this.form);
                return true;
            } catch (e) {
                return false;
            }
        },
    },
};
</script>

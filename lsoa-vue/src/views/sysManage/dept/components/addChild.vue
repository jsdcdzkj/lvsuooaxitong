<template>
    <DialogContainer :done="done" ref="DialogContainer">
        <el-form ref="form" :model="form" label-width="80px" :rules="rules">
            <!-- 上级部门 名称 部门性质 编码 备注 -->
            <el-form-item label="上级部门" prop="parentId">
                <el-select v-model="form.parentId" placeholder="请选择上级部门" disabled>
                    <el-option
                        v-for="item in deptList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="名称" prop="name">
                <el-input v-model="form.name" placeholder="请输入名称"></el-input>
            </el-form-item>
            <el-form-item label="部门性质" prop="deptType">
                <el-select v-model="form.deptType" placeholder="请选择部门性质">
                    <el-option
                        v-for="item in deptTypeList"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="编码" prop="deptCode">
                <el-input v-model="form.deptCode" placeholder="请输入编码"></el-input>
            </el-form-item>
            <el-form-item label="备注" prop="remark" placeholder="请输入备注">
                <el-input
                    v-model="form.remark"
                    type="textarea"
                    :maxlength="200"
                    :rows="2"
                    placeholder="请输入备注"
                ></el-input>
            </el-form-item>
        </el-form>
    </DialogContainer>
</template>

<script>
import DialogContainer from '@/components/DialogFactory/dialog-container.vue';
import { saveOrUpdate, getSysDeptList } from '@/api/user';
export default {
    components: {
        DialogContainer,
    },
    data() {
        return {
            form: {
                parentId: '',
                parentUUID: '',
                name: '',
                deptType: '',
                deptCode: '',
                remark: '',
            },
            rules: {
                name: [{ required: true, message: '请输入部门名称', trigger: 'blur' }],
                deptType: [{ required: true, message: '请选择部门性质', trigger: 'change' }],
                deptCode: [{ required: true, message: '请输入部门编码', trigger: 'blur' }],
            },
            deptList: [],
            deptTypeList: [
                { label: '内部', value: '0' },
                { label: '外部', value: '1' },
            ],
        };
    },
    mounted() {
        this.getSysDeptList();
    },
    methods: {
        async done() {
            // 如果parentId不为空，根据parentId获取parentUUID
            if (this.form.parentId) {
                const dept = this.deptList.find((item) => item.id === this.form.parentId);
                this.form.parentUUID = dept.uuid;
            }
            try {
                await saveOrUpdate(
                    this.id ? Object.assign({}, this.form, { id: this.id }) : this.form
                );
                this.$emit('submit');
                return true;
            } catch (e) {
                return false;
            }
        },
        sleep(time) {
            return new Promise((resolve) => setTimeout(resolve, time));
        },
        async getSysDeptList() {
            const { data } = await getSysDeptList();
            this.deptList = data;
        },
    },
};
</script>

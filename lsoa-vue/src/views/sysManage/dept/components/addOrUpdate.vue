<template>
    <DialogContainer :done="done" ref="DialogContainer">
        <el-form ref="form" :model="form" label-width="80px" :rules="rules">
            <!-- 上级部门 名称 部门性质 编码 备注 -->
            <el-form-item label="上级部门" prop="parentId">
                <el-select
                    v-model="form.parentId"
                    filterable
                    placeholder="请选择上级部门"
                    clearable
                >
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
                name: '',
            },
            rules: {
                name: [{ required: true, message: '请输入部门名称', trigger: 'blur' }],
            },
            deptList: [],
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
            const { data } = await getSysDeptList({});
            this.deptList = data;
        },
    },
};
</script>

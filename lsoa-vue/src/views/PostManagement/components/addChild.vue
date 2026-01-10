<template>
    <DialogContainer :done="done" ref="DialogContainer">
        <el-form ref="form" :model="form" label-width="80px" :rules="rules">
            <!-- 上级岗位 名称 岗位性质 所属部门 编码 备注 -->
            <el-form-item label="上级岗位">
                <el-select v-model="form.parentId" placeholder="请选择上级岗位" disabled>
                    <el-option
                        v-for="item in postList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="名称" prop="name">
                <el-input v-model="form.name" placeholder="请输入名称"></el-input>
            </el-form-item>
            <el-form-item label="岗位性质" prop="postType">
                <el-select v-model="form.postType" placeholder="请选择岗位性质">
                    <el-option
                        v-for="item in deptTypeList"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                    ></el-option>
                </el-select>
            </el-form-item>
            <!-- 所属部门 -->
            <el-form-item label="所属部门" prop="deptId">
                <el-select v-model="form.deptId" filterable placeholder="请选择所属部门">
                    <el-option
                        v-for="item in deptList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="编码" prop="postCode">
                <el-input v-model="form.postCode" placeholder="请输入编码"></el-input>
            </el-form-item>
            <el-form-item label="备注" prop="remark">
                <el-input v-model="form.remark" type="textarea" :maxlength="200" placeholder="请输入备注"></el-input>
            </el-form-item>
        </el-form>
    </DialogContainer>
</template>

<script>
import DialogContainer from '@/components/DialogFactory/dialog-container.vue';
import { getSysDeptList, getSysPostList, postSaveOrUpdate } from '@/api/user';
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
                postType: '',
                deptId: '',
                postCode: '',
                remark: '',
            },
            rules: {
                name: [{ required: true, message: '请输入岗位名称', trigger: 'blur' }],
                postType: [{ required: true, message: '请选择岗位性质', trigger: 'change' }],
                deptId: [{ required: true, message: '请选择所属部门', trigger: 'change' }],
                postCode: [{ required: true, message: '请输入岗位编码', trigger: 'blur' }],
            },
            deptList: [],
            postList: [],
            deptTypeList: [
                { value: '0', label: '内部' },
                { value: '1', label: '外部' },
            ],
        };
    },
    mounted() {
        this.getDeptList();
        this.getPostList();
    },
    methods: {
        async done() {
            // 如果parentId不为空，根据parentId获取parentUUID
            console.log(this.form.parentId);
            if (this.form.parentId) {
                const dept = this.postList.find((item) => item.id === this.form.parentId);
                this.form.parentUUID = dept.uuid;
            }
            console.log(this.id);
            console.log(this.form);
            try {
                await postSaveOrUpdate(
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
        //部门列表
        async getDeptList() {
            const { data } = await getSysDeptList();
            this.deptList = data;
        },
        // 岗位列表
        async getPostList() {
            const { data } = await getSysPostList();
            this.postList = data;
        },
    },
};
</script>

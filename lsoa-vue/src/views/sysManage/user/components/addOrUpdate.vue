<template>
    <DialogContainer :done="done" ref="DialogContainer">
        <el-form ref="form" :model="form" label-width="80px" :rules="rules">
            <!-- 所属部门 所属角色 所属岗位 名称 用户性质 工号 -->
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
            <el-form-item label="所属角色" prop="roleList">
                <el-select v-model="form.roleList" filterable placeholder="请选择所属角色" multiple>
                    <el-option
                        v-for="item in roleListSel"
                        :key="item.id"
                        :label="item.roleName"
                        :value="item.id"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="用户名称" prop="realName">
                <el-input v-model="form.realName" placeholder="请输入用户名称"></el-input>
            </el-form-item>
            <el-form-item label="用户头像">
                <el-upload class="avatar-uploader" accept=".jpg,.png,.jpeg" action="#" :auto-upload="false"
                        :show-file-list="false" :on-change="handleChange"
                        :before-upload="beforeAvatarUpload">
                <el-image v-show="form.avatar" :src="form.avatar" class="avatar" />
                <i v-show="!form.avatar" class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
            </el-form-item>
            <el-form-item label="工号" prop="workId">
                <el-input v-model="form.workId" placeholder="请输入工号"></el-input>
            </el-form-item>
            <el-form-item label="身份证号" prop="cardId">
                <el-input v-model="form.cardId" placeholder="请输入身份证号"></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
                <el-input v-model="form.phone" placeholder="请输入手机号"></el-input>
            </el-form-item>
            <el-form-item label="用户状态" prop="status">
                <el-radio-group v-model="form.status">
                    <el-radio :label="0">正常</el-radio>
                    <el-radio :label="1">禁用</el-radio>
                </el-radio-group>
            </el-form-item>
        </el-form>
    </DialogContainer>
</template>

<script>
import DialogContainer from '@/components/DialogFactory/dialog-container.vue';
import { roleList, userSaveUpdate, getSysDeptList} from '@/api/user';
import { importData } from '@/api/common'
export default {
    components: {
        DialogContainer,
    },
    data() {
        return {
            form: {
                deptId: '',
                roleList: [],
                realName: '',
                type: '',
                workId: '',
                phone: '',
                avatar: '',
                status: 0,
            },
            rules: {
                deptId: [{ required: true, message: '请选择所属部门', trigger: 'blur' }],
                roleList: [{ required: true, message: '请选择所属角色', trigger: 'blur' }],
                realName: [{ required: true, message: '请输入用户名称', trigger: 'blur' }],
                type: [{ required: true, message: '请选择用户性质', trigger: 'blur' }],
                workId: [{ required: true, message: '请输入工号', trigger: 'blur' }],
                phone: [
                    { required: true, message: '请输入手机号', trigger: 'blur' },
                    { pattern: /^1[3456789]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' },
                ],
                status: [{ required: true, message: '请选择用户状态', trigger: 'blur' }],
            },
            deptList: [],
            roleListSel: [],
            postList: [],
            id: '',
        };
    },
    mounted() {
        if (!this.id) {
            this.getRoleList();
            this.getDeptList();
        }
    },
    methods: {
        async done() {

            try {
                await userSaveUpdate(
                    this.id ? Object.assign({}, this.form, { id: this.id }) : this.form
                );
                this.$emit('submit');
                return true;
            } catch (e) {
                return false;
            }
        },
        beforeAvatarUpload(file) {
            const isJPG = file.type === "image/jpeg" || file.type === "image/png";
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isJPG) {
                this.$message.error("上传头像图片只能是 JPG或PNG 格式!");
            }
            if (!isLt2M) {
                this.$message.error("上传头像图片大小不能超过 2MB!");
            }
            this.loadingUp = this.$loading({
                target: document.getElementsByClassName("el-upload")[0],
                lock: true,
                text: "上传中...",
                spinner: "el-icon-loading",
                background: "rgba(0, 0, 0, 0.7)",
            });
            return isJPG && isLt2M;
        },
        async handleChange(res, fileList) {
            // 文件上传
            let fd = new FormData();
            fd.append("file", res.raw);
            var result = await importData(fd);
            this.form.avatar = result.data.data
        },
        sleep(time) {
            return new Promise((resolve) => setTimeout(resolve, time));
        },
        // 获取角色列表
        async getRoleList() {
            const { data } = await roleList();
            this.roleListSel = data;
        },
        //部门列表
        async getDeptList() {
            const { data } = await getSysDeptList({});
            this.deptList = data;
        },
    },
};
</script>
<style lang="scss" scoped>
::v-deep .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 72px;
    height: 72px;
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>

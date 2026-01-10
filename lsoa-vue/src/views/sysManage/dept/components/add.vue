<template>
    <el-dialog
        :close-on-click-modal="false"
        :title="title"
        :visible.sync="dialogFormVisible"
        @close="close"
        width="540px"
    >
        <span slot="title" class="drawer-title">
            <b>{{ title }}</b>
        </span>
        <div style="padding: 0 20px">
            <el-form ref="formInline" :rules="rules" :model="formInline" label-width="100px">
                <el-form-item label="角色名称" prop="role_name">
                    <el-input v-model="formInline.role_name" placeholder="请输入角色名称"></el-input>
                </el-form-item>
                <el-form-item label="角色编号" prop="role_code">
                    <el-input v-model="formInline.role_code" placeholder="请输入角色编号"></el-input>
                </el-form-item>
                <el-form-item label="是否启用" prop="isswitch">
                    <el-switch v-model="isswitch" active-color="#13ce66"></el-switch>
                </el-form-item>
                <el-form-item label="功能权限" prop="permission">
                    <!-- <el-checkbox-group v-model="formInline.permission">
                        <el-checkbox v-for="(item, index) in warnSetList" :key="index" :label="`${item.value}`">
                            {{ item.label }}
                        </el-checkbox>
                    </el-checkbox-group> -->
                    <el-select
                        v-model="formInline.permission"
                        multiple
                        placeholder="请选择功能权限"
                        style="width: 100%"
                        clearable
                    >
                        <el-option
                            v-for="item in warnSetList"
                            :key="item.value"
                            :label="item.label"
                            :value="`${item.value}`"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="菜单权限" prop="isswitch">
                    <el-tree
                        ref="tree"
                        :props="propsDefault"
                        default-expand-all
                        node-key="id"
                        :data="list"
                        :default-checked-keys="defaultChecked"
                        :check-on-click-node="true"
                        show-checkbox
                    ></el-tree>
                </el-form-item>
                <el-form-item label="备注" prop="remarks">
                    <el-input
                        v-model="formInline.remarks"
                        type="textarea"
                        :maxlength="200"
                        :rows="5"
                        placeholder="请输入"
                    ></el-input>
                </el-form-item>
            </el-form>
        </div>

        <div slot="footer" class="dialog-footer">
            <el-button @click="close">取消</el-button>
            <el-button type="primary" :disabled="disabled" :loading="disabled" @click="save('formInline')">
                保存
            </el-button>
        </div>
    </el-dialog>
</template>

<script>
// import { saveOrUpd, selectRoleById, selectMenuTrees, selectRoleselect } from "@/api/systemSet/role";
export default {
    name: "add",
    components: {},
    data() {
        return {
            loading: false,
            disabled: false,
            title: "",
            list: [],
            dialogFormVisible: false,
            formInline: {
                role_name: "",
                role_code: "",
                remarks: "",
                permission: []
            },
            isswitch: true,
            rules: {
                role_name: [{ required: true, message: "请输入", trigger: "blur" }],
                role_code: [{ required: true, message: "请输入", trigger: "blur" }]
            },
            propsDefault: {
                children: "children",
                label: "menu_name"
            },
            defaultChecked: [],
            warnSetList: []
        };
    },
    mounted() {
        // this.selectMenuTrees();
        // selectRoleselect().then((res) => {
        //     this.warnSetList = res.data || [];
        // });
    },
    methods: {
        showEdit(row) {
            var that = this;
            if (row) {
                that.title = "编辑";
                // const res = await selectRoleById({ id: row.id });
                // const tree = await selectMenuTrees()
                // this.list = row
                // this.$nextTick(() => {
                //     const arr = [];
                //     res.data.menuIds.forEach((item) => {
                //         if (
                //             !that.$refs.tree.getNode(item).childNodes ||
                //             !that.$refs.tree.getNode(item).childNodes.length
                //         ) {
                //             arr.push(item);
                //         }
                //     });
                //
                //     that.formInline = { ...res.data, permission: res.data.function_permission && res.data.function_permission.split(",")};
                //     this.defaultChecked = arr;
                //     this.isswitch = res.data.is_open == 1 ? false : true;
                // });
            } else {
                that.title = "新增";
                that.formInline = { permission: [] };
                that.isswitch = true;
            }
            that.dialogFormVisible = true;
        },
        selectMenuTrees() {
            // selectMenuTrees().then((res) => {
            //     this.list = res.data;
            // });
        },
        close() {
            this.dialogFormVisible = false;
            this.formInline = {
                role_name: "",
                role_code: "",
                remarks: "",
                permission: []
            };
            this.$nextTick(() => {
                this.$refs.tree.setCheckedKeys([]);
            });
        },
        save(formName) {
            var that = this;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    if (this.isswitch) {
                        that.formInline.is_open = 0;
                    } else {
                        that.formInline.is_open = 1;
                    }
                    const menuIds = that.$refs.tree.getCheckedKeys();
                    const parentNode = this.$refs.tree.getHalfCheckedKeys();
                    if (menuIds.length === 0) {
                        return this.$message({
                            type: "error",
                            message: "请选择菜单权限"
                        });
                    }
                    console.log("111111111111111", that.formInline.permission);
                    const params = {
                        id: that.formInline.id || "",
                        role_name: that.formInline.role_name || "",
                        role_code: that.formInline.role_code || "",
                        remarks: that.formInline.remarks || "",
                        is_open: that.formInline.is_open,
                        menuIds: menuIds.concat(parentNode),
                        function_permission: that.formInline.permission?.join(",")
                    };
                    this.disabled = true;
                    console.log("222222222222222", params);
                    saveOrUpd(params)
                        .then((res) => {
                            this.close();
                            this.$emit("fetch-data");
                            setTimeout(() => {
                                this.disabled = false;
                            }, 1000);
                        })
                        .catch(() => {
                            setTimeout(() => {
                                this.disabled = false;
                            }, 1000);
                        });
                } else {
                    return false;
                }
            });
        }
    }
};
</script>
<style scoped lang="scss">
::v-deep {
    .el-tree {
        max-height: 300px;
        border: 1px solid #ccc;
        overflow: auto;
    }
}
</style>

<template>
    <DialogContainer :done="done" ref="DialogContainer">
        <el-form ref="form" :model="form" label-width="90px" :rules="rules">
            <!-- 角色名称 权限编码 所属子系统 功能权限 -->
            <!-- 角色名称 -->
            <el-form-item label="角色名称">
                <el-input v-model="form.roleName" placeholder="请输入角色名称" disabled></el-input>
            </el-form-item>
            <!-- 权限编码 -->
            <!-- <el-form-item label="权限编码" prop="code">
                <el-input v-model="form.code" placeholder="请输入权限编码"></el-input>
            </el-form-item> -->
            <!-- 所属子系统 -->
            <el-form-item label="所属子系统">
                <el-select v-model="form.systemId" placeholder="请选择所属子系统" disabled>
                    <el-option
                        v-for="item in systemList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id"
                    ></el-option>
                </el-select>
            </el-form-item>

            <!-- 功能权限是树形菜单展示 并且头部有三个复选框：全选 全部展开 父子联动  -->
            <!-- 功能权限 -->
            <el-form-item label="功能权限" prop="menuIds">
                <el-checkbox
                    :indeterminate="isIndeterminate"
                    v-model="checkOne"
                    @change="handleCheckOne"
                    >全选</el-checkbox
                >
                <el-checkbox v-model="checkTwo" @change="handleCheckTwo">全部展开</el-checkbox>
                <el-checkbox v-model="checkThree">父子联动</el-checkbox>
                <div class="tree-box">
                    <el-tree
                        :data="treeData"
                        show-checkbox
                        node-key="id"
                        :default-expanded-keys="defaultExpandedKeys"
                        :default-checked-keys="defaultCheckedKeys"
                        :props="defaultProps"
                        :check-strictly="!checkThree"
                        ref="tree"
                        @check-change="handleTreeCheckChange"
                    >
                    </el-tree>
                </div>
            </el-form-item>
        </el-form>
    </DialogContainer>
</template>

<script>
import DialogContainer from '@/components/DialogFactory/dialog-container.vue';
import { sysSystemList, getSysMenuTree, saveRoleMenu, getSysRoleMenuList } from '@/api/user';
export default {
    components: {
        DialogContainer,
    },
    data() {
        return {
            form: {
                roleName: '',
                systemId: '',
                menuIds: [],
            },
            rules: {
                roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
                systemId: [{ required: true, message: '请选择所属子系统', trigger: 'change' }],
            },
            checkOne: false,
            checkTwo: false,
            checkThree: false,
            treeData: [],
            defaultExpandedKeys: [],
            defaultCheckedKeys: [],
            defaultProps: {
                children: 'menuChild',
                label: 'title',
            },
            deptList: [
                { id: '1', name: '总经理' },
                { id: '2', name: '副总经理' },
                { id: '3', name: '部门经理' },
                { id: '4', name: '部门副经理' },
                { id: '5', name: '员工' },
            ],
            systemList: [],
            id: '',
            tags: [],
            isIndeterminate: false,
        };
    },
    mounted() {
        // this.defaultExpandedKeys = this.treeData.map((node) => node.id); // 默认全部展开
        // this.defaultExpandedKeys = this.getAllNodeKeys(this.treeData); // 默认全部展开
        this.getDeptList();
        this.getSysMenuTree();
        this.getSysRoleMenuList();
    },
    methods: {
        // 获取树形菜单
        async getSysMenuTree() {
            const { data } = await getSysMenuTree({ systemId: this.form.systemId });
            this.treeData = data;
            this.$nextTick(() => {
                this.updateCheckOneState();
            });
        },
        async done() {
            // const checkedKeys = this.$refs.tree.getCheckedKeys();
            // this.form.menuIds = checkedKeys;
            const checkedNodes = this.$refs.tree.getCheckedNodes();
            // console.log(checkedNodes); // 这里会打印出所有勾选的节点数据
            this.form.menuIds = checkedNodes.map((node) => node.id); // 获取勾选节点的值
            // console.log(checkedKeys); // 这里会打印出所有勾选的节点值
            // this.form.menuIds = checkedKeys;

            try {
                await saveRoleMenu(
                    this.id ? Object.assign({}, this.form, { roleId: this.id }) : this.form
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

        handleTreeCheckChange(data, checked, indeterminate) {
            // 处理树形结构变化时的逻辑
            this.updateCheckOneState();
        },
        updateCheckOneState() {
            const allCheckedKeys = this.$refs.tree.getCheckedKeys();
            const halfCheckedKeys = this.$refs.tree.getHalfCheckedKeys();
            const allNodeKeys = this.getAllNodeKeys(this.treeData);
            if (allCheckedKeys.length === allNodeKeys.length) {
                this.isIndeterminate = false;
                this.checkOne = true; // 全选
            } else if (halfCheckedKeys.length > 0 || allCheckedKeys.length > 0) {
                this.checkOne = null; // 部分选中
                this.isIndeterminate = true;
            } else {
                this.isIndeterminate = false;
                this.checkOne = false; // 全不选
            }
        },
        handleCheckChange(data, checked, indeterminate) {
            // this.tags = data;
            this.tags.push(data.tenantName);
            //获取所有选中的节点id
            this.tenantUserId = this.$refs.tree
                .getCheckedKeys()
                .concat(this.$refs.tree.getHalfCheckedKeys());
            this.checked = checked;
            // 该节点未选中的时候
            for (var i = 0; i <= this.tags.length; i++) {
                if (this.checked == false) {
                    this.tags.splice(this.tags.indexOf(this.i), 1);
                }
            }
        },
        handleCheckOne(val) {
            if (val) {
                this.$refs.tree.setCheckedKeys(this.getAllNodeKeys(this.treeData));
            } else {
                this.$refs.tree.setCheckedKeys([]);
            }
        },
        handleCheckTwo(val) {
            if (val) {
                // 展开所有节点
                this.defaultExpandedKeys = this.getAllNodeKeys(this.treeData);
            } else {
                // 折叠所有节点
                // this.defaultExpandedKeys = [];
                const nodes = this.$refs.tree.store._getAllNodes();
                nodes.forEach((item) => {
                    item.expanded = false;
                });
            }
        },
        getAllNodeKeys(data) {
            let keys = [];
            data.forEach((item) => {
                keys.push(item.id);
                if (item.menuChild) {
                    keys = keys.concat(this.getAllNodeKeys(item.menuChild));
                }
            });
            return keys;
        },

        // 所属子系统
        async getDeptList() {
            const { data } = await sysSystemList();
            this.systemList = data;
        },

        // 获取角色权限
        async getSysRoleMenuList() {
            const { data } = await getSysRoleMenuList({ roleId: this.id });
            const menuIds = [];
            data.forEach((item) => {
                menuIds.push(item.menuId);
            });
            this.defaultCheckedKeys = menuIds;
        },
    },
    watch: {
        checkThree(val) {
            this.$refs.tree.setCheckedKeys(this.$refs.tree.getCheckedKeys());
        },
    },
};
</script>
<style scoped>
.tree-box {
    height: 300px;
    overflow: auto;
    border: 1px solid #ebeef5;
    border-radius: 4px;
    padding: 10px;
    margin-top: 10px;
}
</style>

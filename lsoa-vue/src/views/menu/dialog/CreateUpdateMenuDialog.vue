<template>
    <DialogContainer :done="handleDone">
        <el-form label-position="right" label-width="90px" :model="form" :rules="rules">
            <el-form-item label="子系统" prop="systemId">
                <el-select v-model="form.systemId" placeholder="选择子系统" @change="handleChange">
                    <el-option
                        v-for="item in systemList"
                        :key="item.id"
                        :value="item.id"
                        :label="item.name"
                    >
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="上级菜单" prop="parentId">
                <treeselect
                    v-model="form.parentId"
                    :options="menuOptions"
                    :normalizer="normalizer"
                    :show-count="true"
                    @select="handeTreeSelect"
                    placeholder="选择上级菜单"
                />
            </el-form-item>
            <el-form-item label="菜单类型" prop="menuType">
                <el-radio-group v-model="form.menuType" @change="handleChangeMenuType">
                    <el-radio :label="1">菜单 </el-radio>
                    <el-radio :label="2"> 按钮</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="菜单图标" prop="icon" v-if="form.menuType != 2">
                <el-input placeholder="请输入菜单图标" v-model="form.icon"></el-input>
            </el-form-item>
            <el-form-item label="菜单名称" prop="title">
                <el-input placeholder="请输入登录菜单名称" v-model="form.title"></el-input>
            </el-form-item>
            <el-form-item label="默认排序" prop="sort">
                <el-input placeholder="请输入默认排序" v-model="form.sort"></el-input>
            </el-form-item>
            <!-- <el-form-item label="是否外链">
                <el-radio-group>
                    <el-radio label="目录" value="1"> </el-radio>
                    <el-radio label="菜单" value="2"> </el-radio>
                    <el-radio label="按钮" value="2"> </el-radio>
                </el-radio-group>
            </el-form-item> -->
            <el-form-item label="路由地址" prop="routeUrl" v-if="form.menuType != 2">
                <el-input placeholder="请输入路由地址" v-model="form.routeUrl"></el-input>
            </el-form-item>
            <el-form-item label="组件地址" prop="vueUrl" v-if="form.menuType == 1">
                <el-input placeholder="请输入路由地址" v-model="form.vueUrl"></el-input>
            </el-form-item>
            <el-form-item label="菜单编号" prop="menuCode">
                <el-input placeholder="请输入菜单编号" v-model="form.menuCode"></el-input>
            </el-form-item>
            <el-form-item label="显示状态" props="isShow">
                <el-radio-group v-model="form.isShow">
                    <el-radio :label="1">正常</el-radio>
                    <el-radio :label="0">隐藏 </el-radio>
                </el-radio-group>
            </el-form-item>
        </el-form>
    </DialogContainer>
</template>
<script lang="js">
import DialogContainer from '@/components/DialogFactory/dialog-container.vue';
import { getSysMenuTree, saveOrUpdate } from '@/api/menu';
import Treeselect from '@riophae/vue-treeselect';
import '@riophae/vue-treeselect/dist/vue-treeselect.css';

export default {
    components: { DialogContainer, Treeselect },
    data() {
        return {
            options: [
                {
                    value: '选项1',
                    label: '黄金糕',
                },
            ],
            menuOptions: [],
            systemList: [],
            form: {},
            rules: {
                title: [{ required: true, message: '菜单名称不能为空', trigger: 'blur' }],
                sort: [{ required: true, message: '菜单顺序不能为空', trigger: 'blur' }],
                routeUrl: [{ required: true, message: '路由地址不能为空', trigger: 'blur' }],
            },
            value: '',
            id: '',
        };
    },
    methods: {
        handeTreeSelect(e) {
            this.form.parentUUID = e.parentUUID;
        },
        handleChangeMenuType() {
            this.form.title = '';
            this.form.routeName = '';
            this.form.routeUrl = '';
            this.form.vueUrl = '';
            this.form.redirectType = '';
            this.form.icon = '';
            this.form.isShow = 1;
            this.form.sort = '';
            this.form.menuCode = '';
        },
        normalizer(node) {
            if (node.menuChild && !node.menuChild.length) {
                delete node.menuChild;
            }
            return {
                id: node.id,
                label: node.title,
                children: node.menuChild,
            };
        },
        async handleDone() {
            try {
                const form = {
                    ...this.form,
                    // isShow: this.form.isShow == 1 ? true : false,
                };
                await saveOrUpdate(this.id ? Object.assign({}, form, { id: this.id }) : form);
                this.$emit('submit');
                return true;
            } catch (e) {
                return false;
            }
        },
        handleChange() {
            getSysMenuTree({ systemId: this.form.systemId }).then((res) => {
                if (res.data.length > 0) {
                    this.menuOptions = res.data;
                } else {
                    this.menuOptions = [{ id: 1, parentId: null, menuChild: [], title: '根菜单' }];
                }
                this.form.parentId = this.menuOptions[0].id;
            });
        },
    },
    created() {
        // 获取目录
        getSysMenuTree({ systemId: this.form.systemId }).then((res) => {
            if (res.data.length > 0) {
                this.menuOptions = [
                    { id: 0, parentId: null, menuChild: res.data, title: '根菜单' },
                ];
            } else {
                this.menuOptions = [{ id: 0, parentId: null, menuChild: [], title: '根菜单' }];
            }
        });
    },
};
</script>

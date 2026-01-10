<template>
    <div class="app-container">
        <div class="formBox">
            <el-form
                size="small"
                :inline="true" class="demo-form-inline topsearch" label-width="110px"
                :model="queryParams"
                @submit.native.prevent
                ref="queryParams"
            >
                        <el-form-item label="用户名称">
                                <el-input
                                    v-model="queryParams.realName"
                                    placeholder="请输入用户名称"
                                    :maxlength="50"
                                    @keyup.enter.native="handlerSearch()"
                                    clearable
                                />
                            </el-form-item>
                            <el-form-item label="工号">
                                <el-input
                                    v-model="queryParams.workId"
                                    placeholder="请输入工号"
                                    @keyup.enter.native="handlerSearch()"
                                    :maxlength="50"
                                    clearable
                                />
                            </el-form-item>
                        
                            <el-form-item label="所属部门">
                                <el-select
                                    v-model="queryParams.deptId"
                                    placeholder="请选择所属部门"
                                >
                                    <el-option
                                        v-for="item in deptList"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id"
                                    ></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="手机号">
                                <el-input
                                    v-model="queryParams.phone"
                                    placeholder="请输入手机号"
                                    @keyup.enter.native="handlerSearch()"
                                    :maxlength="50"
                                    clearable
                                />
                            </el-form-item>
                            <el-form-item label="账号状态">
                                <el-select
                                    v-model="queryParams.status"
                                    placeholder="请选择账号状态"
                                    style="width:100%"
                                    clearable
                                    filterable
                                    size="small"
                                >
                                    <el-option label="正常" :value="0"></el-option>
                                    <el-option label="禁用" :value="1"></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="所属角色">
                                <el-select
                                    v-model="queryParams.roleId"
                                    placeholder="请选择所属角色"
                                    style="width:100%"
                                    clearable
                                    filterable
                                    size="small"
                                >
                                    <el-option v-for="item in roleListSel" :key="item.id" :label="item.roleName" :value="item.id"></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item style="margin-left: 60px">
                                <el-button
                                    icon="el-icon-refresh"
                                    size="small"
                                    @click="handlerRefresh()"
                                    >重置</el-button
                                >
                                <el-button
                                    type="primary"
                                    icon="el-icon-search"
                                    size="small"
                                    @click="handlerSearch()"
                                    >查询</el-button
                                >
                            </el-form-item>
                            <div style="float:right">
                                <el-button icon="el-icon-plus" type="success" size="small" @click="handleAdd()"
                                    >新增</el-button
                                >
                            </div>
            </el-form>
        </div>

        <div class="table-wrap">
            <el-table
                v-loading="table.loading"
                :data="table.records"
                element-loading-text="Loading"
                stripe
                fit
                height="calc(100vh - 380px)"
                class="table-container"
                highlight-current-row
            >
                <el-table-column
                    type="index"
                    label="序号"
                    width="80"
                    align="center"
                    :index="indexMethod"
                >
                </el-table-column>

                <el-table-column
                    show-overflow-tooltip
                    prop="realName"
                    width="120"
                    label="用户名称"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="workId"
                    width="110"
                    label="工号"
                    align="center"
                ></el-table-column>
                <!-- 所属部门 -->
                <el-table-column
                    show-overflow-tooltip
                    prop="dept.name"
                    label="所属部门"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    width="110"
                    prop="phone"
                    label="手机号"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    width="110"
                    prop="cardId"
                    label="身份证号"
                    align="center"
                ></el-table-column>
                <!-- 所属角色 -->
                <el-table-column
                    show-overflow-tooltip
                    prop="roleList"
                    label="所属角色"
                    align="center"
                >
                    <template slot-scope="scope">
                        <el-tag
                            size="mini"
                            type="success"
                            v-for="item in scope.row.roleList"
                            :key="item.id"
                            >{{ item.roleName }}</el-tag
                        >
                    </template>
                </el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    width="160"
                    prop="createTime"
                    label="创建时间"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    width="160"
                    prop="status"
                    label="帐号状态"
                    align="center"
                >
                    <template slot-scope="scope">
                        <el-tag
                            size="mini"
                            v-if="scope.row.status == 0"
                            >正常</el-tag
                        >
                        <el-tag
                            size="mini"
                            type="danger"
                            v-else
                            >禁用</el-tag
                        >
                    </template>
                </el-table-column>
                <el-table-column show-overflow-tooltip label="操作" width="240" align="center">
                    <template slot-scope="scope">
                        <el-button type="danger" size="mini" plain @click="handleDel(scope.row.id)"
                            >删除</el-button
                        >
                        <el-button type="primary" size="mini" plain @click="handleAdd(scope.row)"
                            >编辑</el-button
                        >
                        <el-button
                            type="warning"
                            size="mini"
                            plain
                            @click="handlePassword(scope.row.id)"
                            >修改密码</el-button
                        >
                    </template>
                </el-table-column>
            </el-table>

            <pagination
                :total="table.total"
                :page.sync="page.pageIndex"
                :limit.sync="page.pageSize"
                @pagination="onPaginationChange"
                align="right"
                style="padding-bottom: 10px"
            />
        </div>
    </div>
</template>

<script>
import Pagination from '@/components/Pagination';
import {
    userPageList,
    userDelete,
    getSysDeptList,
    getSysPostList,
    roleList,
    userSyncSystem,
} from '@/api/user';

import pagination from '@/mixins/pagination';
import { CreateAddOrUpdate, CreateEditPassword } from './components/index';
import { mapGetters } from 'vuex';
export default {
    components: {
        Pagination,
    },
    mixins: [pagination],
    data() {
        return {
            queryParams: {
                realName: '',
                workId: '',
                deptId: '',
                phone: '',
            },
            deptList: [],
            roleListSel: [],
            postList: [],
        };
    },
    created() {
        this.bindList(userPageList, 'queryParams');
        this.getRoleList();
        this.getDeptList();
        this.getPostList();
    },
    computed: {
        ...mapGetters(['userInfo'])
    },
    methods: {
        resertFormQrery(queryParams) {
            return {
                ...queryParams,
                // startTime: []
            };
        },

        //新增
        handleAdd(row) {
            if (row) {
                const params = { ...row };
                delete params.id;

                CreateAddOrUpdate(
                    {
                        form: {
                            deptId: row.deptId,
                            roleList:
                                row.roleList != null ? row.roleList.map((item) => item.id) : '',
                            mainPost: row.mainPost != null ? row.mainPost.id : '',
                            deputyPost:
                                row.deputyPost != null ? row.deputyPost.map((item) => item.id) : '',
                            realName: row.realName,
                            type: row.type,
                            workId: row.workId,
                            phone: row.phone,
                            status: row.status,
                            avatar: row.avatar,
                            cardId: row.cardId,
                        },
                        id: row.id,
                        roleListSel: this.roleListSel,
                        deptList: this.deptList,
                        postList: this.postList,
                    },
                    { title: '编辑', width: '600px' }
                ).$on('submit', (res) => {
                    this.fetchTableData();
                    this.$message.success('编辑成功');
                    this.$store
                        .dispatch('user/getInfo')
                });
            } else {
                CreateAddOrUpdate({}, { title: '新增', width: '600px' }).$on('submit', (res) => {
                    this.fetchTableData();
                    this.$message.success('新增成功');
                });
            }
        },
        //删除
        handleDel(id) {
            this.$confirm('您是否确认删除该项', '提示', {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning',
            }).then(() => {
                userDelete({ id }).then((res) => {
                    this.$message({
                        type: 'success',
                        message: '删除成功!',
                    });
                    this.fetchTableData();
                });
            });
        },
        //修改密码
        handlePassword(row) {
            // CreateEditPassword({ id: row.id }, { title: '修改密码', width: '480px' }).$on(
            //     'submit',
            //     (res) => {
            //         console.log(res);
            //     }
            // );
            // const params = { ...row };
            // delete params.id;
            CreateEditPassword(
                {
                    id: row,
                },
                { title: '修改密码', width: '550px' }
            ).$on('submit', async(res) => {
                this.$message.success('修改成功');
                console.log('9999999999999', row.id, this.userInfo)
                if (row != this.userInfo.id) return
                await this.$store.dispatch('user/logout')
                setTimeout(() => {
                    this.$router.push('/login')
                }, 1000)
                
                
                
            });
        },
        // 部门
        async getDeptList() {
            const { data } = await getSysDeptList({});
            this.deptList = data;
        },
        // 获取角色列表
        async getRoleList() {
            const { data } = await roleList({});
            this.roleListSel = data;
        },
        // 岗位列表
        async getPostList() {
            const { data } = await getSysPostList({});
            this.postList = data;
        },
    },
};
</script>
<style scoped>
.el-tag {
    margin-right: 3px;
    margin-left: 3px;
}
</style>

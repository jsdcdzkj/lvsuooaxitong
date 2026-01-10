<template>
    <div class="app-container">
        <div class="formBox">
            <el-form
                label-width="90px"
                :inline="true"
                class="demo-form-inline topsearch"
                size="small"
                :model="queryParams"
                @submit.native.prevent
                ref="queryParams"
            >
                <el-form-item label="角色名称">
                    <el-input
                        v-model="queryParams.roleName"
                        @keyup.enter.native="handlerSearch()"
                        placeholder="请输入角色名称"
                        :maxlength="50"
                        clearable
                    />
                </el-form-item>
                <el-form-item>
                    <el-button icon="el-icon-refresh" size="small" @click="handlerRefresh()"
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
                    <el-button
                        icon="el-icon-plus"
                        type="success"
                        size="small"
                        @click="handleAdd()"
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
                height="calc(100vh - 322px)"
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
                    prop="roleName"
                    label="角色名称"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="roleCode"
                    label="角色编码"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="createTime"
                    label="创建时间"
                    align="center"
                ></el-table-column>
                <el-table-column show-overflow-tooltip label="操作" width="260" align="center">
                    <template slot-scope="scope">
                        <el-button type="danger" size="mini" plain @click="handleDel(scope.row.id)"
                            >删除</el-button
                        >
                        <el-button type="primary" size="mini" plain @click="handleAdd(scope.row)"
                            >编辑</el-button
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
import { rolePage, roleDelete, roleSyncSystem } from '@/api/user';
import pagination from '@/mixins/pagination';
import { CreateAddOrUpdate, CreatePermissionUpdate } from './components/index';

export default {
    components: {
        Pagination,
    },
    mixins: [pagination],
    data() {
        return {
            queryParams: {
                roleName: '',
                systemId: '',
            },
            systemList: [],
            value1: '',
        };
    },
    created() {
        this.bindList(rolePage, 'queryParams');
    },
    methods: {
        resertFormQrery(queryParams) {
            if (this.value1.length == 0) {
                return {
                    ...queryParams,
                    startTime: '',
                    endTIme: '',
                };
            } else {
                return {
                    ...queryParams,
                    startTime: this.value1[0],
                    endTIme: this.value1[1],
                };
            }
        },
        //新增
        handleAdd(row) {
            if (row) {
                const params = { ...row };
                delete params.id;
                CreateAddOrUpdate(
                    {
                        form: { ...params },
                        id: row.id,
                    },
                    { title: '编辑', width: '480px' }
                ).$on('submit', (res) => {
                    this.fetchTableData();
                    this.$message.success('编辑成功');
                });
            } else {
                CreateAddOrUpdate({}, { title: '新增', width: '480px' }).$on('submit', (res) => {
                    this.fetchTableData();
                    this.$message.success('新增成功');
                });
            }

            // .then((res) => {
            //   this.refreshList()
            // });
        },
        handleDel(id) {
            this.$confirm('您是否确认删除该项', '提示', {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning',
            }).then(() => {
                roleDelete({ id }).then((res) => {
                    this.$message({
                        type: 'success',
                        message: '删除成功!',
                    });
                    this.fetchTableData();
                });
            });
        },
    },
};
</script>

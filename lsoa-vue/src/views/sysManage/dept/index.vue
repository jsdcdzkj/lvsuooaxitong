<template>
    <div class="app-container">
        <div class="formBox">
            <el-form
                :inline="true"
                class="demo-form-inline topsearch"
                label-width="90px"
                size="small"
                :model="queryParams"
                @submit.native.prevent
                ref="queryParams"
            >
                <el-form-item label="部门名称">
                    <el-input
                        v-model="queryParams.name"
                        placeholder="请输入部门名称"
                        @keyup.enter.native="handlerSearch(resertFormQrery)"
                        :maxlength="50"
                        clearable
                    />
                </el-form-item>
                <el-form-item label="创建时间">
                    <el-date-picker
                        style="width: 100%"
                        v-model="value1"
                        type="daterange"
                        value-format="yyyy-MM-dd"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                    >
                    </el-date-picker>
                </el-form-item>
                <el-form-item>
                    <el-button
                        icon="el-icon-refresh"
                        size="small"
                        @click="handlerRefresh(resertFormQreryClear)"
                        >重置</el-button
                    >
                    <el-button
                        type="primary"
                        icon="el-icon-search"
                        size="small"
                        @click="handlerSearch(resertFormQrery)"
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
                :data="buildTree(table.records)"
                element-loading-text="Loading"
                stripe
                fit
                height="calc(100vh - 266px)"
                class="table-container"
                highlight-current-row
                row-key="id"
                default-expand-all
                :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
            >
                <el-table-column
                    show-overflow-tooltip
                    prop="name"
                    label="部门名称"
                ></el-table-column>
                <!-- <el-table-column
                    show-overflow-tooltip
                    prop="deptCode"
                    label="部门编码"
                    align="center"
                ></el-table-column> -->
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
        </div>
    </div>
</template>

<script>
import Pagination from '@/components/Pagination';
import Add from './components/add';
import { getSysDeptPage, deptDel, getSysDeptList, deptSyncSystem } from '@/api/user';

import pagination from '@/mixins/pagination';
import { CreateAddOrUpdate, CreateAddChild } from './components/index';
export default {
    components: {
        Pagination,
        Add,
    },
    mixins: [pagination],
    data() {
        return {
            queryParams: {
                name: '',
                deptCode: '',
            },
            value1: '',
        };
    },
    created() {
        this.page.pageSize = 10000;
        this.bindList(getSysDeptPage, 'queryParams');
    },
    methods: {
        resertFormQrery(queryParams) {
            if (this.value1 == null) {
                this.value1 = '';
            }
            if (this.value1.length == 0) {
                return {
                    ...queryParams,
                    start: '',
                    end: '',
                };
            } else {
                return {
                    ...queryParams,
                    start: this.value1[0],
                    end: this.value1[1],
                };
            }
        },
        resertFormQreryClear(queryParams) {
            this.value1 = '';
            return {
                ...queryParams,
                start: '',
                end: '',
            };
        },
        buildTree(items) {
            const map = {};
            const tree = [];
            // 首先创建一个映射，以parentId作为键，存储所有子节点
            items.forEach((item) => {
                map[item.id] = { ...item, children: [] };
            });
            // 然后遍历映射，将子节点添加到对应的父节点下
            items.forEach((item) => {
                if (map[item.parentId]) {
                    // 如果存在父节点，则将当前节点添加到父节点的children数组中
                    map[item.parentId].children.push(map[item.id]);
                } else {
                    // 如果不存在父节点，则当前节点为顶级节点
                    tree.push(map[item.id]);
                }
            });
            return tree;
        },

        //新增
        handleAdd(row) {
            if (row) {
                const params = { ...row };
                delete params.id;
                // 如果parentId为0，设置为空
                if (params.parentId === 0) {
                    params.parentId = '';
                }
                console.log(params);
                CreateAddOrUpdate(
                    {
                        form: { ...params },
                        id: row.id,
                    },
                    { title: '编辑', width: '600px' }
                ).$on('submit', (res) => {
                    this.fetchTableData();
                    this.$message.success('编辑成功');
                });
            } else {
                CreateAddOrUpdate({}, { title: '新增', width: '600px' }).$on('submit', (res) => {
                    this.fetchTableData();
                    this.$message.success('新增成功');
                });
            }
        },
        //新增子部门
        handleChild(row) {
            const params = { ...row };
            delete params.id;
            CreateAddChild(
                {
                    form: { parentId: row.id },
                },
                { title: '新增子部门', width: '480px' }
            ).$on('submit', (res) => {
                this.fetchTableData();
                this.$message.success('新增成功');
            });
        },

        handleDel(id) {
            this.$confirm('您是否确认删除该项', '提示', {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning',
            }).then(() => {
                deptDel({ id }).then((res) => {
                    this.$message({
                        type: 'success',
                        message: '删除成功!',
                    });
                    this.fetchTableData();
                });
            });
        },
        handleIssued() {
            this.$confirm('您是否确认下发', '提示', {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning',
            }).then(() => {
                deptSyncSystem().then((res) => {
                    this.$message({
                        type: 'success',
                        message: '下发成功!',
                    });
                });
            });
        },
    },
};
</script>

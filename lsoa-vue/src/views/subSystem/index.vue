<template>
    <div v-if="!isDetail" class="app-container">
        <div class="formBox">
            <el-form :inline="true" class="demo-form-inline topsearch" label-width="110px">
                <el-form-item label="案件编号/名称">
                    <el-input
                        v-model="listQuery.title"
                        size="small"
                        placeholder="请输入案件编号/名称"
                        :maxlength="50"
                        clearable
                    />
                </el-form-item>
                <el-form-item label="当事人">
                    <el-input
                        v-model="listQuery.title"
                        size="small"
                        placeholder="请输入当事人"
                        :maxlength="50"
                        clearable
                    />
                </el-form-item>
                <el-form-item label="案件类型">
                    <el-select
                        v-model="listQuery.type1"
                        placeholder="请选择案件类型"
                        clearable
                        filterable
                        size="small"
                    >
                        <el-option label="争议类型1" :value="1"></el-option>
                        <el-option label="争议类型2" :value="2"></el-option>
                        <el-option label="争议类型3" :value="3"></el-option>
                        <el-option label="争议类型4" :value="4"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="开票状态">
                    <el-select
                        v-model="listQuery.type1"
                        placeholder="请选择开票状态"
                        clearable
                        filterable
                        size="small"
                    >
                        <el-option label="争议类型1" :value="1"></el-option>
                        <el-option label="争议类型2" :value="2"></el-option>
                        <el-option label="争议类型3" :value="3"></el-option>
                        <el-option label="争议类型4" :value="4"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="是否开函">
                    <el-select
                        v-model="listQuery.type2"
                        placeholder="请选择是否开函"
                        style="width:100%"
                        clearable
                        filterable
                        size="small"
                    >
                        <el-option label="未受理" :value="1"></el-option>
                        <el-option label="已受理" :value="2"></el-option>
                    </el-select>
                </el-form-item>

                <div style="float:right">
                    <el-button size="small" type="default" icon="el-icon-refresh" @click="refresh"
                            >重置</el-button
                        >
                        <el-button size="small" type="primary" icon="el-icon-search" @click="search"
                            >查询</el-button
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
                :height="`calc(100vh - ${tableHeight}px)`"
                class="table-container"
                highlight-current-row
            >
                <el-table-column
                    fixed="left"
                    type="index"
                    label="序号"
                    width="80"
                    align="center"
                    :index="indexMethod"
                >
                </el-table-column>
                <el-table-column prop="code" label="案件编号" align="center"> </el-table-column>
                <el-table-column prop="name" label="案件类型" align="center"> </el-table-column>
                <!-- <el-table-column prop="name" label="案件名称" align="center"> </el-table-column> -->
                <el-table-column prop="url" label="发票内容" align="center"> </el-table-column>
                <el-table-column prop="privateKey" label="合同金额" align="center"> </el-table-column>
                <el-table-column prop="publicKey" label="开票金额" align="center"> </el-table-column>
                <el-table-column prop="publicKey" label="发票类型" align="center"> </el-table-column>
                <el-table-column prop="publicKey" label="联系人姓名" align="center"> </el-table-column>
                <el-table-column prop="publicKey" label="联系电话" align="center"> </el-table-column>
                <el-table-column prop="publicKey" label="电子邮箱" align="center"> </el-table-column>
                <el-table-column prop="publicKey" label="开票状态" align="center"> </el-table-column>
                <el-table-column fixed="right" label="操作" align="center" width="300">
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            type="primary"
                            plain
                            @click="handlerDelete(scope.$index, scope.row)"
                            >详情</el-button
                        >
                        <el-button
                            size="mini"
                            @click="handlerCheck(scope.$index, scope.row)"
                            type="success"
                            plain
                            >审核</el-button
                        >
                        <el-button
                            size="mini"
                            @click="handlerInvoicing(scope.$index, scope.row)"
                            type="warning"
                            plain
                            >开票</el-button
                        >
                        <el-button
                            size="mini"
                            @click="handlerEdit(scope.$index, scope.row)"
                            type="info"
                            plain
                            >下载</el-button
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
                style="padding-bottom: 10px;border-top:1px solid rgba(0,0,0,0.06);"
            />
        </div>
        <createInvoicingDialog ref="invoicingDialog" />
        <createLetterDialog ref="letterDialog" />
    </div>
    <Edit v-else @back="goBack" />
</template>

<script>
import Pagination from '@/components/Pagination';
import Edit from '@/views/common/edit';
// import { getList,add,edit,remove } from "@/api/manyidu";
// import { getRenList } from "@/api/common";
import { syssystemPage, syssystemDelete } from '@/api/syssystem';
import pagination from '@/mixins/pagination';
import createInvoicingDialog from '@/views/common/dialog/createInvoicingDialog.vue';
import createLetterDialog from '@/views/common/dialog/createLetterDialog.vue';
import { createCheckDialog } from '@/views/common/dialog';

export default {
    components: {
        Pagination,
        Edit,
        createInvoicingDialog,
        createLetterDialog
    },
    mixins: [pagination],
    data() {
        return {
            tableHeight: 414,
            query: {},
            listQuery: {},
            isDetail: false,
        };
    },
    created() {
        this.bindList(syssystemPage, '');
    },
    methods: {
        goBack() {
            this.isDetail = false
        },
        handleEdit() {
            this.isDetail = true 
        },
        handlerManger({ id }) {
            this.$router.push({
                path: '/menuIndex',
                query: {
                    id,
                },
            });
        },
        handlerCheck(index,row) {
            const params = { ...row };
            delete params.id;
            createCheckDialog(
                {
                    form: { ...params },
                    id: row.id,
                },
                {title: '开票审核', width: '960px', confirmText: '通过',cancelText: '回退'}
            ).then((res) => {
                console.log('处理数据',res)
            });
        },
        handlerDelete(index, row) {
            this.$confirm('将删除该条信息, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }).then(() => {
                syssystemDelete({ id: row.id }).then((res) => {
                    this.fetchTableData();
                    this.$message({
                        type: 'success',
                        message: '删除成功!',
                    });
                });
            });
        },
        handlerInvoicing(index, row) {
            this.$refs.letterDialog.edit()
        },
    },
};
</script>

<style lang="scss" scoped>
</style>

<template>
    <div>
        <div v-if="isDetail == 'page'" class="app-container">
            <div class="formBox">
                <el-form :inline="true" class="demo-form-inline topsearch" label-width="110px">
                    <el-form-item label="案件编号">
                        <el-input
                            v-model="listQuery.caseNum"
                            @keyup.enter.native="search"
                            size="small"
                            placeholder="请输入案件编号"
                            :maxlength="50"
                            clearable
                        />
                    </el-form-item>
                    <el-form-item label="顾问单位">
                        <el-select
                            v-model="listQuery.customerId"
                            placeholder="请选择顾问单位"
                            clearable
                            filterable
                            size="small"
                        >
                            <el-option
                                v-for="(item, index) in customerList"
                                :key="index"
                                :label="item.customerName"
                                :value="item.id"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="承办律师">
                        <el-select
                            v-model="listQuery.createUser"
                            placeholder="请选择承办律师"
                            key="userId"
                            clearable
                            filterable
                            size="small"
                        >
                            <el-option v-for="(item,index) in lawyerList" :key="index" :label="item.realName" :value="`${item.id}`"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="当事人">
                        <el-input
                            v-model="listQuery.client"
                            size="small"
                            @keyup.enter.native="search"
                            placeholder="请输入当事人"
                            :maxlength="50"
                            clearable
                        />
                    </el-form-item>
                    <el-form-item label="案件类型">
                        <el-select
                            v-model="listQuery.caseType"
                            placeholder="请选择案件类型"
                            clearable
                            filterable
                            size="small"
                        >
                            <el-option
                                v-for="(item, index) in caseList"
                                :key="index"
                                :label="item.dictLabel"
                                :value="item.dictValue"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="业务类型">
                        <el-select
                            v-model="listQuery.businessType"
                            placeholder="请选择业务类型"
                            clearable
                            filterable
                            size="small"
                        >
                            <el-option
                                v-for="(item, index) in businessList"
                                :key="index"
                                :label="item.dictLabel"
                                :value="item.dictValue"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="登记时间">
                        <el-date-picker
                            v-model="listQuery.time"
                            type="daterange"
                            style="width: 100%"
                            @change="changeTime"
                            value-format="yyyy-MM-dd"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                        >
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="案件状态">
                        <el-select
                            v-model="status"
                            style="width: 100%"
                            multiple
                            placeholder="请选择案件状态"
                            clearable
                            filterable
                            size="small"
                        >
                            <el-option label="进行中" :value="1"></el-option>
                            <el-option label="已完结" :value="2"></el-option>
                            <el-option label="已中止（解除委托或撤销）" :value="3"></el-option>
                        </el-select>
                    </el-form-item>
                    
                    <!-- <el-form-item label="案由">
                        <el-input
                            v-model="listQuery.subjectOfAction"
                            size="small"
                            @keyup.enter.native="search"
                            placeholder="请输入案由"
                            :maxlength="50"
                            clearable
                        />
                    </el-form-item> -->
                    <el-form-item label="开票状态">
                        <el-select
                            v-model="listQuery.invoice"
                            placeholder="请选择开票状态"
                            style="width: 100%"
                            clearable
                            filterable
                            size="small"
                        >
                            <el-option label="未开票" :value="1"></el-option>
                            <el-option label="待审批" :value="2"></el-option>
                            <el-option label="已开票" :value="3"></el-option>
                            <el-option label="已退回" :value="4"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="开函状态">
                        <el-select
                            v-model="listQuery.letter"
                            placeholder="请选择开函状态"
                            style="width: 100%"
                            clearable
                            filterable
                            size="small"
                        >
                            <el-option label="未开函" :value="1"></el-option>
                            <el-option label="待审批" :value="2"></el-option>
                            <el-option label="已开函" :value="3"></el-option>
                            <el-option label="已退回" :value="4"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="归档状态">
                        <el-select
                            v-model="listQuery.archived"
                            placeholder="请选择归档状态"
                            style="width: 100%"
                            clearable
                            filterable
                            size="small"
                        >
                            <el-option label="未归档" :value="1"></el-option>
                            <el-option label="已归档" :value="2"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item class="case-btn" style="padding-left: 42px">
                        <el-button
                            size="small"
                            type="default"
                            icon="el-icon-refresh"
                            @click="refresh"
                            >重置</el-button
                        >
                        <el-button size="small" type="primary" icon="el-icon-search" @click="search"
                            >查询</el-button
                        >
                        <div style="float: right">
                            <el-button
                                size="small"
                                type="success"
                                icon="el-icon-plus"
                                @click="handlerAdd('add')"
                                >新增案件</el-button
                            >
                        </div>
                    </el-form-item>
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
                    <el-table-column
                        fixed="left"
                        width="160"
                        prop="caseNum"
                        label="案件编号"
                        align="center"
                    >
                    </el-table-column>
                    <!-- <el-table-column
                        fixed="left"
                        width="240"
                        show-overflow-tooltip
                        prop="caseName"
                        label="案件名称"
                        align="center"
                    >
                    </el-table-column> -->
                    <el-table-column
                        prop="userName"
                        show-overflow-tooltip
                        label="承办律师"
                        width="120"
                        align="center"
                    >
                    </el-table-column>
                    <el-table-column
                        prop="client"
                        show-overflow-tooltip
                        label="委托人"
                        width="120"
                        align="center"
                    >
                    </el-table-column>
                    <el-table-column
                        prop="tarClient"
                        show-overflow-tooltip
                        label="对方当事人"
                        width="120"
                        align="center"
                    >
                    </el-table-column>
                    <el-table-column
                        prop="subjectOfAction"
                        show-overflow-tooltip
                        label="案由"
                        width="120"
                        align="center"
                    >
                    </el-table-column>
                    <el-table-column
                        prop="court"
                        show-overflow-tooltip
                        label="办案机构"
                        width="120"
                        align="center"
                    >
                    </el-table-column>
                    <el-table-column
                        prop="delegate"
                        show-overflow-tooltip
                        label="委托方式"
                        width="120"
                        align="center"
                    >
                        <template slot-scope="scope">
                            {{ scope.row.delegate == 1 ? '顾问单位' : '非顾问单位' }}
                        </template>
                    </el-table-column>
                    <el-table-column
                        prop="caseTypeStr"
                        show-overflow-tooltip
                        width="130"
                        label="案件类型"
                        align="center"
                    >
                    </el-table-column>
                    <el-table-column
                        prop="businessTypeStr"
                        label="业务类型"
                        width="200"
                        align="center"
                    >
                    </el-table-column>
                    <el-table-column prop="money" label="费用" align="center" width="200">
                        <template slot-scope="scope">
                            {{ scope.row.money && Number(scope.row.money).toFixed(2) }}
                        </template>
                    </el-table-column>
                    <el-table-column
                        prop="stageStr"
                        label="诉讼阶段"
                        align="center"
                    ></el-table-column>
                    <el-table-column prop="archived" width="120" label="归档状态" align="center">
                        <template slot-scope="scope">
                            {{ scope.row.archived == 2 ? '已归档' : '未归档' }}
                        </template>
                    </el-table-column>
                    <el-table-column
                        show-overflow-tooltip
                        prop="status"
                        label="案件状态"
                        align="center"
                    >
                        <template slot-scope="scope">
                            {{ caseStatusList[scope.row.status - 1] }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="subjectAmount" label="标的额" width="200" align="center">
                        <template slot-scope="scope">
                            {{
                                scope.row.subjectAmount &&
                                Number(scope.row.subjectAmount).toFixed(2)
                            }}
                        </template>
                    </el-table-column>
                    <el-table-column
                        prop="invoiceStr"
                        label="开票状态"
                        align="center"
                    ></el-table-column>
                    <el-table-column
                        prop="letterStr"
                        label="开函状态"
                        align="center"
                    ></el-table-column>
                    <el-table-column
                        prop="invoiceNum"
                        label="开票次数"
                        align="center"
                        fixed="right"
                    >
                        <template slot-scope="scope">
                            <span class="caseLink" @click="handleOpenInvoiceNum(scope.row)">{{
                                scope.row.invoiceNum
                            }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                        prop="summonsNum"
                        label="开函次数"
                        align="center"
                        fixed="right"
                    >
                        <template slot-scope="scope">
                            <span class="caseLink" @click="handleOpenNum(scope.row)">{{
                                scope.row.summonsNum
                            }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="createTime" width="200" label="登记时间" align="center">
                    </el-table-column>
                    <el-table-column fixed="right" label="操作" align="center" width="280">
                        <template slot-scope="scope">
                            <el-button
                                size="mini"
                                type="primary"
                                plain
                                @click="handlerView(scope.row)"
                                >详情</el-button
                            >
                            <el-button
                                size="mini"
                                v-if="scope.row.archived != 2 && isShow()"
                                plain
                                type="success"
                                @click="handlerOnFile(scope.$index, scope.row)"
                                >归档</el-button
                            >
                            <template v-if="scope.row.createUser == userInfo.id">
                                <template v-if="scope.row.temporarily == 2">
                                    <el-button
                                        size="mini"
                                        @click="handlerEdit('edit', scope.row)"
                                        v-if="!(scope.row.invoice == 2 || scope.row.letter == 2)"
                                        type="success"
                                        plain
                                        >编辑</el-button
                                    >
                                    <el-button
                                        size="mini"
                                        v-if="(scope.row.invoice == 1 || scope.row.invoice == 3) && !scope.row.customerId"
                                        @click="handlerInvoicing(scope.$index, scope.row)"
                                        type="warning"
                                        plain
                                        >开票</el-button
                                    >
                                    <el-button
                                        size="mini"
                                        @click="handlerLetter(scope.$index, scope.row)"
                                        v-if="isShowLetter(scope.row)"
                                        type="info"
                                        plain
                                        >{{ '开函' }}</el-button
                                    >

                                    <el-button
                                        size="mini"
                                        type="danger"
                                        v-if="!(scope.row.invoice == 2 || scope.row.letter == 2)"
                                        plain
                                        @click="handlerDelete(scope.$index, scope.row)"
                                        >删除</el-button
                                    >
                                </template>
                                <template v-if="scope.row.temporarily == 1">
                                    <el-button
                                        size="mini"
                                        @click="handlerAdd('add', scope.row)"
                                        type="success"
                                        plain
                                        >草稿</el-button
                                    >
                                    <el-button
                                        size="mini"
                                        type="danger"
                                        plain
                                        @click="handlerDelete(scope.$index, scope.row)"
                                        >删除</el-button
                                    >
                                </template>
                            </template>
                        </template>
                    </el-table-column>
                </el-table>
                <pagination
                    :total="table.total"
                    :page.sync="page.pageNo"
                    :limit.sync="page.pageSize"
                    @pagination="onPaginationChange"
                    align="right"
                    style="padding-bottom: 10px; border-top: 1px solid rgba(0, 0, 0, 0.06)"
                />
            </div>
            <createInvoicingDialog @submit="search" ref="invoicingDialog" />
            <createLetterDialog @submit="search" ref="letterDialog" />
        </div>
        <Add ref="addEl" v-show="isDetail == 'add'" @back="goBack" />
        <Edit ref="editEl" v-show="isDetail == 'edit'" @back="goBack" />
        <ViewDetail ref="viewEl" v-show="isDetail == 'detail'" @back="goBack" />
    </div>
</template>

<script>
import Pagination from '@/components/Pagination';
import Add from '@/views/common/add';
import Edit from '@/views/common/edit';
import ViewDetail from '@/views/common/view.vue';
import { del, getPageList, toInvoice, archive } from '@/api/case';
import { getRedisDictList, byRole } from '@/api/common';
import createInvoicingDialog from '@/views/common/dialog/createInvoicingDialog.vue';
import createLetterDialog from '@/views/common/dialog/createLetterDialog.vue';
import { createViewLetterNumDialog, createViewInvoiceNumDialog } from '@/views/common/dialog';
import pagination from '@/mixins/pagination';
import { invoiceTypeList, letterTypeList, caseStatusList } from '@/utils/comvariable';
import { mapGetters } from 'vuex';
import { getLetterList } from '@/api/letter';
import { getInvoiceList } from '@/api/invoice';
import { getNormalUserList } from "@/api/statistics";

export default {
    components: {
        Pagination,
        Edit,
        Add,
        ViewDetail,
        createInvoicingDialog,
        createLetterDialog,
    },
    mixins: [pagination],
    data() {
        return {
            tableHeight: 442,
            query: {},
            listQuery: {
                letter: '',
                invoice: '',
            },
            status: [],
            isDetail: 'page',
            caseList: [],
            businessList: [],
            litigationList: [],
            invoiceTypeList,
            letterTypeList,
            caseStatusList,
            lawyerList: [],
            customerList: []
        };
    },
    computed: {
        ...mapGetters(['userInfo', 'roles']),
    },
    created() {
        this.bindList(getPageList, 'listQuery');
        // 案件类型
        getRedisDictList({ dictType: 'caseType' }).then((res) => {
            this.caseList = res.data;
        });
        // 律师列表
        getNormalUserList({}).then(res => {
            this.lawyerList = res.data
        })
        // 业务类型
        getRedisDictList({ dictType: 'businessType' }).then((res) => {
            this.businessList = res.data;
        });
        // 诉讼阶段
        getRedisDictList({ dictType: 'litigationStage' }).then((res) => {
            this.litigationList = res.data;
        });
        // 获取顾问单位查询
        byRole({}).then(res => {
            this.customerList = res.data
        })
    },
    methods: {
        isShow() {
            return this.roles.some((res) => res.roleCode == '012');
        },
        // 是否展示开函
        isShowLetter(row) {
            // 1.协约用户可以不开票直接开函
            // 2.非协约用户必须开票完成才可以开函
            return row.letter == 1 || row.letter == 3;
        },
        changeTime(time) {
            if (time?.length > 0) {
                this.listQuery.startTime = time[0];
                this.listQuery.endTime = time[1];
            } else {
                this.listQuery.startTime = '';
                this.listQuery.endTime = '';
            }
        },
        search() {
            this.listQuery.statusList = this.status?.join(',');
            this.handlerSearch();
        },
        refresh() {
            this.status = [];
            this.handlerRefresh();
        },
        goBack() {
            this.isDetail = 'page';
            this.search();
        },
        handlerAdd(type, row) {
            this.isDetail = 'add';
            this.$refs.addEl.showEdit(row);
        },
        handlerEdit(type, row) {
            this.isDetail = 'edit';
            this.$refs.editEl.showEdit(row);
        },
        handlerView(row) {
            this.isDetail = 'detail';
            this.$refs.viewEl.showEdit(row);
        },
        handlerOnFile(index, row) {
            this.$confirm('是否确认归档？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }).then(() => {
                archive({ id: row.id }).then((res) => {
                    this.fetchTableData();
                    this.$message({
                        type: 'success',
                        message: '归档成功!',
                    });
                });
            });
        },
        handlerDelete(index, row) {
            this.$confirm('将删除该条信息, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }).then(() => {
                del({ id: row.id }).then((res) => {
                    this.fetchTableData();
                    this.$message({
                        type: 'success',
                        message: '删除成功!',
                    });
                });
            });
        },
        handlerInvoicing(index, row) {
            toInvoice({ id: row.money - 3000 < 0 ? 2 : 3 }).then((res) => {
                if (res.code == 0) {
                    console.log('9999999999res', res);
                    if (!res.data) {
                        this.$message.warning(`您没有开票权限，请联系管理员！`);
                    } else {
                        this.$refs.invoicingDialog.edit(row);
                    }
                }
            });
        },
        handlerLetter(index, row) {
            toInvoice({ id: 1 }).then((res) => {
                if (res.code == 0) {
                    console.log('9999999999res', res);
                    if (!res.data) {
                        this.$message.warning(`您没有开函权限，请联系管理员！`);
                    } else {
                        this.$refs.letterDialog.edit(row);
                    }
                }
            });
        },
        // 查看开票次数
        handleOpenInvoiceNum(row) {
            // 请求开票列表数据
            let list = [];
            getInvoiceList({ id: row.id }).then((res) => {
                list = res.data.map((val) => {
                    val.userName = row.userName;
                    val.client = row.client;
                    val.tarClient = row.tarClient;
                    val.subjectOfAction = row.subjectOfAction;
                    val.court = row.court;
                    val.delegate = row.delegate;
                    return val;
                });
                console.log('11111111111list', list);
                createViewInvoiceNumDialog(
                    {
                        list,
                        invoiceInfo: {...row, caseId: row.id},
                        page: 'detail',
                    },
                    { title: '开票记录', width: '960px', confirmText: '', cancelText: '取消' }
                ).then((res) => {
                    console.log('处理数据', res);
                    this.search();
                });
            });
        },
        // 查看开函次数
        handleOpenNum(row) {
            // 请求开函列表数据
            let list = [];
            getLetterList({ id: row.id }).then((res) => {
                list = res.data.map(val => {
                    val.userName = row.userName
                    val.client = row.client
                    val.tarClient = row.tarClient
                    val.subjectOfAction = row.subjectOfAction
                    val.court = row.court
                    val.delegate = row.delegate
                    return val
                });
                createViewLetterNumDialog(
                    {
                        list,
                        letterInfo:  {...row, caseId: row.id},
                        page: 'detail',
                    },
                    { title: '开函记录', width: '960px', confirmText: '', cancelText: '取消' }
                ).then((res) => {
                    console.log('处理数据', res);
                    this.search();
                });
            });
        },
    },
};
</script>

<style lang="scss" scoped>
.caseLink {
    color: #409eff;
    cursor: pointer;
    text-decoration: underline;
}
::v-deep .case-btn {
    width: 25%!important;
    .el-form-item__content {
        width: 100% !important;
    }
}

</style>

<template>
    <div v-if="!isDetail" class="app-container">
        <div class="formBox">
            <el-form :inline="true" class="demo-form-inline topsearch" label-width="110px">
                <el-form-item label="案件编号">
                    <el-input
                        v-model="listQuery.caseNum"
                        size="small"
                        placeholder="请输入案件编号"
                        :maxlength="50"
                        @keyup.enter.native="search"
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
                <!-- <el-form-item label="案件名称">
                    <el-input
                        v-model="listQuery.caseName"
                        size="small"
                        placeholder="请输入案件名称"
                        @keyup.enter.native="search"
                        :maxlength="50"
                        clearable
                    />
                </el-form-item> -->
                <el-form-item label="承办律师">
                        <el-select
                            v-model="listQuery.userName"
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
                            <el-option v-for="(item,index) in caseList" :key="index" :label="item.dictLabel" :value="item.dictValue"></el-option>
                        </el-select>
                    </el-form-item>
                    <!-- <el-form-item label="开票状态">
                        <el-select
                            v-model="listQuery.invoice"
                            placeholder="请选择开票状态"
                            style="width:100%"
                            clearable
                            filterable
                            size="small"
                        >
                            <el-option label="未开票" :value="1"></el-option>
                            <el-option label="待审批" :value="2"></el-option>
                            <el-option label="已开票" :value="3"></el-option>
                            <el-option label="已退回" :value="4"></el-option>
                        </el-select>
                    </el-form-item> -->
                    <el-form-item label="开函状态">
                        <el-select
                            v-model="letter"
                            multiple
                            placeholder="请选择开函状态"
                            style="width:100%"
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

                <el-form-item class="case-btn" style="padding-left: 42px">
                    <el-button size="small" type="default" icon="el-icon-refresh" @click="refresh"
                            >重置</el-button
                        >
                        <el-button size="small" type="primary" icon="el-icon-search" @click="search"
                            >查询</el-button
                        >
                        <div style="float: right">
                            <el-button
                                size="small"
                                type="success"
                                icon="el-icon-download"
                                @click="handlerExport"
                                >导出</el-button
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
                <el-table-column prop="caseNum" width="160" fixed="left" label="案件编号" align="center"> </el-table-column>
                <!-- <el-table-column prop="caseName" width="240" show-overflow-tooltip fixed="left" label="案件名称" align="center"> </el-table-column> -->
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
                <!-- <el-table-column prop="caseTypeStr" width="130" label="开函类型" align="center"> </el-table-column> -->
                <!-- <el-table-column prop="caseName" width="240" show-overflow-tooltip fixed="left" label="案件名称" align="center"> </el-table-column> -->
                <el-table-column prop="invoiceTime" width="120" label="开函日期" align="center">
                    <template slot-scope="scope">
                        {{(scope.row.invoiceTime && scope.row.invoiceTime.substring(0, 10)) || '--'}}
                    </template>
                </el-table-column>
                <!-- <el-table-column prop="content" label="发票内容" align="center"> </el-table-column> -->
                <el-table-column prop="money" label="合同金额" width="200" align="center">
                    <template slot-scope="scope">
                        {{scope.row.money && Number(scope.row.money).toFixed(2)}}
                    </template>
                </el-table-column>
                <el-table-column prop="invoiceMoney" label="开票金额"  width="200" align="center">
                    <template slot-scope="scope">
                        {{scope.row.invoiceMoney && Number(scope.row.invoiceMoney).toFixed(2)}}
                    </template>
                </el-table-column>
                <!-- <el-table-column prop="invoiceStr" width="160" label="发票类型" align="center"> </el-table-column>
                <el-table-column prop="contactPerson" width="120" label="联系人姓名" align="center"> </el-table-column>
                <el-table-column prop="contactPhone" width="120" label="联系电话" align="center"> </el-table-column>
                <el-table-column show-overflow-tooltip width="160px" prop="mail" label="电子邮箱" align="center"> </el-table-column>
                <el-table-column show-overflow-tooltip width="160px" prop="addressS" label="收件地址" align="center">
                    <template  slot-scope="scope">{{scope.row.addressS || '--'}}</template>
                </el-table-column> -->
                <el-table-column fixed="right" prop="letter" label="开函状态" align="center">
                    <template slot-scope="scope">
                        {{letterTypeList[scope.row.letter - 1]}}
                    </template>
                </el-table-column>
                <el-table-column prop="summonsNum" label="开函次数" align="center" fixed="right">
                        <template slot-scope="scope">
                            <span class="caseLink" @click="handleOpenNum(scope.row)">{{ scope.row.summonsNum || 0 }}</span>
                        </template>
                    </el-table-column>
                <el-table-column fixed="right" label="操作" align="center" width="120">
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            type="primary"
                            plain
                            @click="handleOpenNum(scope.row)"
                            >详情</el-button
                        >
                        <template v-if="scope.row.createUser == userInfo.id">
                            <el-button
                                size="mini"
                                v-if="(scope.row.letter == 1 || scope.row.letter == 3)"
                                @click="handlerLetter(scope.$index, scope.row)"
                                type="info"
                                plain
                                >开函</el-button
                            >
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
                style="padding-bottom: 10px;border-top:1px solid rgba(0,0,0,0.06);"
            />
        </div>
        <createLetterDialog @submit="search" ref="letterDialog" />
    </div>
</template>

<script>
import Pagination from '@/components/Pagination';
import { syssystemPage, syssystemDelete } from '@/api/syssystem';
import { approveInvoiceInfo, getEntity, getPageList, saveOrUpdate, getLetterList, toExportTemplate } from '@/api/letter';
import pagination from '@/mixins/pagination';
import createLetterDialog from '@/views/common/dialog/createLetterDialog.vue';
import { getRedisDictList, byRole } from '@/api/common'
import { toInvoice } from '@/api/case'
import { createLetterCheckDialog, downloadListDialog } from '@/views/common/dialog';
import { invoiceTypeList, letterTypeList, caseStatusList } from '@/utils/comvariable'
import { mapGetters } from 'vuex';
import { createViewLetterNumDialog } from '@/views/common/dialog';
import { getNormalUserList } from "@/api/statistics";

export default {
    components: {
        Pagination,
        createLetterDialog,
    },
    mixins: [pagination],
    data() {
        return {
            tableHeight: 380,
            query: {},
            listQuery: {},
            isDetail: false,
            caseList: [],
            businessList: [],
            litigationList: [],
            invoiceTypeList,
            letterTypeList,
            caseStatusList,
            letter:[],
            lawyerList: [],
            customerList: []
        };
    },
    computed: {
        ...mapGetters(["userInfo", "roleIds"]),
    },
    created() {
        this.bindList(getPageList, 'listQuery');
        // 案件类型
        getRedisDictList({dictType: 'caseType'}).then(res => {
            this.caseList = res.data
        })
        // 律师列表
        getNormalUserList({}).then(res => {
            this.lawyerList = res.data
        })
        // 业务类型
        getRedisDictList({dictType: 'businessType'}).then(res => {
            this.businessList = res.data
        })
        // 诉讼阶段
        getRedisDictList({dictType: 'litigationStage'}).then(res => {
            this.litigationList = res.data
        })
        // 获取顾问单位查询
        byRole({}).then(res => {
            this.customerList = res.data
        })
    },
    methods: {
        isShow(row) {
            const hasPermissions = row.approveIds?.approverValue.split(',').some(item => {
                return this.roleIds.includes(Number(item))
            })
            return row.letter == 2 && ((row.approveIds?.approverType == 1 && row.approveIds?.approverValue.split(',').includes(`${this.userInfo.id}`))
             || (row.approveIds?.approverType == 2 && hasPermissions))
        },
        goBack() {
            this.isDetail = false
        },
        search() {
            this.listQuery.letter = this.letter?.join(',')
            this.handlerSearch()
        },
        refresh() {
            this.letter =[]
            this.handlerRefresh()
        },
        handlerView(row) {
            if(row.summonsId) {
                getEntity({id: row.summonsId}).then(res => {
                    console.log('88888888888888res', res)
                    createLetterCheckDialog(
                        {
                            detailInfo: res.data,
                            page: 'detail'
                        },
                        {title: '详情', width: '960px', confirmText: '', cancelText: '取消'}
                    ).then((res) => {
                        console.log('处理数据',res)
                    });
                    
                })
            } else {
                createLetterCheckDialog(
                    {
                        detailInfo: {},
                        page: 'detail'
                    },
                    {title: '详情', width: '960px', confirmText: '', cancelText: '取消'}
                ).then((res) => {
                    console.log('处理数据',res)
                });
            }
        },
        handlerDownload(row) {
            getEntity({id: row.summonsId}).then(res => {
                console.log('88888888888888res', res)
                if(res.data.fileList.length == 0) {
                    return this.$message.warning(`没有可下载的文件`);
                }
                downloadListDialog(
                    {
                        fileList: res.data.fileList.map((item) => {
                                    return {
                                        size: item.fileSize,
                                        name: item.filename,
                                        url: item.fileUrl,
                                        id: item.id,
                                    };
                                }),
                    },
                    {title: '下载', width: '900px', confirmText: '',cancelText: '取消'}
                )
                    
            })
        },
        handlerLetter(index, row) {
            toInvoice({id: 1}).then(res => {
                if (res.code == 0) {
                    console.log('9999999999res', res)
                    if (!res.data) {
                        this.$message.warning(`您没有开函权限，请联系管理员！`);
                    } else {
                        this.$refs.letterDialog.edit(row)
                    }
                }
            })
            
        },
        // 查看开函次数
        handleOpenNum(row) {
            // 请求开函列表数据
            let list = []
            getLetterList({id: row.id}).then(res => {
                list = res.data.map(val => {
                    val.userName = row.userName
                    val.client = row.client
                    val.tarClient = row.tarClient
                    val.subjectOfAction = row.subjectOfAction
                    val.court = row.court
                    val.delegate = row.delegate
                    return val
                })
                createViewLetterNumDialog(
                    {
                        list,
                        letterInfo: row,
                        page: 'detail'
                    },
                    {title: '开函记录', width: '960px', confirmText: '', cancelText: '取消'}
                ).then((res) => {
                    console.log('处理数据',res)
                    this.search()
                }).catch(() => {
                    console.log('quexiaoqquxiao')
                });
            })
            
        },
        handlerExport() {
            this.$confirm(`确认要导出吗？`, "提示", {
                confirmButtonText: "确认",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                this.listLoading = true;
                toExportTemplate({ ...this.listQuery })
                    .then((res) => {
                        let fileName = "开函记录.xls";
                        let objectUrl = URL.createObjectURL(new Blob([res.data], { type: "application/vnd.ms-excel" }));
                        const link = document.createElement("a");
                        link.download = decodeURI(fileName);
                        link.href = objectUrl;
                        link.click();
                        this.$baseMessage("导出成功！", "success");
                    })
                    .finally(() => {
                        this.listLoading = false;
                    });
            }).catch(() => {
               
            })
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
    width: 50%!important;
    .el-form-item__content {
        width: 100% !important;
    }
}
</style>

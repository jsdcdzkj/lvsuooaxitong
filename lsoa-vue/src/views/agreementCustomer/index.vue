<template>
    <div class="app-container">
        <div class="formBox">
            <el-form
                label-width="140px"
                :inline="true"
                class="demo-form-inline topsearch"
                size="small"
                :model="queryParams"
                @submit.native.prevent
                ref="queryParams"
            >
                <el-form-item label="客户编码">
                    <el-input
                        v-model="queryParams.customerCode"
                        @keyup.enter.native="handlerSearch()"
                        placeholder="请输入客户编码"
                        :maxlength="50"
                        clearable
                    />
                </el-form-item>
                <el-form-item label="负责律师">
                    <el-select
                        v-model="queryParams.lawyer"
                        placeholder="请选择负责律师"
                        key="userId"
                        clearable
                        filterable
                        size="small"
                    >
                        <el-option v-for="(item,index) in lawyerList" :key="index" :label="item.realName" :value="`${item.id}`"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="客户名称">
                    <el-input
                        v-model="queryParams.customerName"
                        @keyup.enter.native="handlerSearch()"
                        placeholder="请输入客户名称"
                        :maxlength="50"
                        clearable
                    />
                </el-form-item>
                <el-form-item label="社会统一信用代码">
                    <el-input
                        v-model="queryParams.creditCode"
                        @keyup.enter.native="handlerSearch()"
                        placeholder="请输入社会统一信用代码"
                        :maxlength="50"
                        clearable
                    />
                </el-form-item>
                <el-form-item label="是否在服务期">
                        <el-select
                            v-model="queryParams.inService"
                            placeholder="请选择是否在服务期"
                            style="width:100%"
                            clearable
                            size="small"
                        >
                            <el-option label="是" :value="1"></el-option>
                            <el-option label="否" :value="0"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="服务费区间">
                        <el-input-number
                            v-model="queryParams.feeStart"
                            @keyup.enter.native="handlerSearch()"
                            placeholder="最低费用"
                            :precision="2"
                            :controls="false"
                            clearable
                            style="width:calc(50% - 5px)!important"
                        />
                        <el-input-number
                            v-model="queryParams.feeEnd"
                            @keyup.enter.native="handlerSearch()"
                            placeholder="最高费用"
                            :precision="2"
                            :controls="false"
                            style="width:calc(50% - 5px)!important;margin-left:10px"
                            clearable
                        />
                    </el-form-item>
                <el-form-item class="btn-search" style="padding-left:58px">
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
                        >新增客户</el-button
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
                height="calc(100vh - 386px)"
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
                    prop="customerCode"
                    label="客户编码"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="customerName"
                    label="客户名称"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="legalPerson"
                    label="法人"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="creditCode"
                    label="信用代码"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="serviceFee"
                    label="服务费"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    width="120"
                    prop="inService"
                    label="是否在服务期"
                    align="center"
                >
                    <template slot-scope="scope">
                        <span>{{scope.row.inService == 1 ? '是' : '否'}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="lawyerNames"
                    label="负责律师"
                    align="center"
                ></el-table-column>
                <el-table-column prop="invoice" label="开票状态" align="center">
                    <template slot-scope="scope">
                        {{invoiceTypeList[scope.row.invoice - 1]}}
                    </template>
                </el-table-column>
                <el-table-column prop="invoiceNum" label="开票次数" align="center">
                        <template slot-scope="scope">
                            <span class="caseLink" @click="handleOpenNum(scope.row)">{{ scope.row.invoiceNum }}</span>
                        </template>
                    </el-table-column>
                <el-table-column show-overflow-tooltip label="操作" width="260" align="center">
                    <template slot-scope="scope">
                        <el-button type="primary" size="mini" plain @click="handleAdd(scope.row,'detail')"
                            >详情</el-button
                        >
                        <template v-if="scope.row.lawyers.includes(userInfo.id) || isShow">
                            <el-button type="warning" size="mini" v-if="!scope.row.invoice || scope.row.invoice == 1 || scope.row.invoice == 3" plain @click="handleInvoice(scope.row)"
                                >开票</el-button
                            >
                            <!-- <el-button type="warning" size="mini" v-if="scope.row.invoice == 4" plain @click="handleInvoice(scope.row, 'edit')"
                                >修改</el-button
                            > -->
                            <el-button type="success" size="mini" plain @click="handleAdd(scope.row)"
                                >编辑</el-button
                            >
                            <el-button type="danger" size="mini" plain @click="handleDel(scope.row.id)"
                                >删除</el-button
                            >
                        </template>
                        
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
import { page, delById, save, detail, invoiceDetail } from '@/api/agreementCustomer';
import pagination from '@/mixins/pagination';
import { invoiceTypeList } from '@/utils/comvariable'
import { CreateAddOrUpdate, CreateAddInvoice } from './components/index';
import { mapGetters } from 'vuex';
import { getNormalUserList } from "@/api/statistics";
import { createCustomerInvoiceNumDialog } from '@/views/common/dialog';
import WordPreview from "@/components/WordPreview";

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
            invoiceTypeList,
            value1: '',
            showWord: false,
            realFileList: [],
            lawyerList:[],
        };
    },
    computed: {
        ...mapGetters(["userInfo", "roles"]),
        isShowUpload() {
            return this.roles.some(res => res.roleCode == '011' || res.roleCode == '010')
        },
        isShow() {
            return this.roles.some(res => res.roleCode == 'gwkhgly')
        }
    },
    created() {
        this.bindList(page, 'queryParams');
        // 律师列表
        getNormalUserList({}).then(res => {
            this.lawyerList = res.data
        })
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
        async handleAdd(row, type) {
            if (row) {
                const detailInfo = await detail({id: row.id})
                let invoiceFile = ''
                if(row.invoice == 3) {
                    invoiceFile = await invoiceDetail({id: row.id})
                }
                CreateAddOrUpdate(
                    {
                        form: {
                            ...detailInfo.data,
                            time:[row.starTime, row.endTime],
                            isDetail: true,
                            invoiceFile: invoiceFile?.data?.fileList,
                            serviceFee: detailInfo.data.serviceFee ? detailInfo.data.serviceFee : undefined,
                        },
                        id: row.id,
                        type,
                        relatedCompany: detailInfo.data.acList
                    },
                    { title: type ? '详情' : '编辑客户',type:'drawer', confirmText: type ? '' : '确定', cancelText: '取消' }
                ).$on('submit', (res) => {
                    this.fetchTableData();
                    this.$message.success('编辑成功');
                });
            } else {
                CreateAddOrUpdate({}, { title: '新增客户',type:'drawer' }).$on('submit', (res) => {
                    this.fetchTableData();
                    this.$message.success('新增成功');
                });
            }

            // .then((res) => {
            //   this.refreshList()
            // });
        },
        async handleInvoice(row, type) {
            // 获取顾问单位的开票信息
            let invoiceInfo = {}
            if(type) invoiceInfo = await invoiceDetail({id: row.id})
            CreateAddInvoice(
                {
                    id: row.id,
                    form: {invoiceMoney: row.serviceFee,number:row.creditCode, title:row.customerName, ...invoiceInfo?.data}
                },
                { title: '顾问单位开票'}
            ).$on('submit', (res) => {
                this.fetchTableData();
                this.$message.success('开票成功');
            });
        },
        handlerUpload(row) {
            uploadFileDialog(
                    {
                        info: {id: 7, invoiceId: row.id, fileList: row.invoiceFile}
                    },
                    {title: '上传文件', width: '900px', confirmText: '',cancelText: '取消'}
                ).then(() => {
                    this.search()
                })
        },
        preview(item) {
            console.log('222222222222', item)
            this.realFileList = [
                    {
                        file_name: item.originalFilename,
                        file_path: item.fileUrl,
                        id: item.id
                    }
                ]
                console.log('111111111222', this.realFileList)
                this.showWord = true;
        },
        handleDel(id) {
            this.$confirm('您是否确认删除该项', '提示', {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning',
            }).then(() => {
                delById({ id }).then((res) => {
                    this.$message({
                        type: 'success',
                        message: '删除成功!',
                    });
                    this.fetchTableData();
                });
            });
        },
        // 查看开票次数
        handleOpenNum(row) {
            console.log('4444444444444row', row)
            createCustomerInvoiceNumDialog(
                    {
                        invoiceInfo: row,
                        page: 'detail'
                    },
                    {title: '开票记录', width: '960px', confirmText: '', cancelText: '取消'}
                ).then((res) => {
                    console.log('处理数据',res)
                    this.fetchTableData();
                }).catch(() => {
                    this.fetchTableData();
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
::v-deep .el-form-item__content {
    width: calc(100% - 140px)!important;
}

::v-deep .btn-search {
    .el-form-item__content {
        width: 100%!important;
    }
}
</style>

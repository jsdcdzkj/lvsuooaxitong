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
                <el-form-item label="非诉编号">
                    <el-input
                        v-model="queryParams.code"
                        @keyup.enter.native="search()"
                        placeholder="请输入非诉编号"
                        :maxlength="50"
                        clearable
                    />
                </el-form-item>
                <el-form-item label="承办律师">
                    <el-select
                        v-model="queryParams.lawyerId"
                        placeholder="请选择律师"
                        key="userId"
                        clearable
                        filterable
                        size="small"
                    >
                        <el-option v-for="(item,index) in lawyerList" :key="index" :label="item.realName" :value="`${item.id}`"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="法人">
                    <el-input
                        v-model="queryParams.legalPerson"
                        @keyup.enter.native="search()"
                        placeholder="请输入法人"
                        :maxlength="50"
                        clearable
                    />
                </el-form-item>
                <el-form-item label="非诉讼业务类型">
                    <el-select
                        v-model="queryParams.businessType"
                        placeholder="请选择非诉讼业务类型"
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
                        v-model="queryParams.time"
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
                <el-form-item label="当前状态">
                    <el-select
                        v-model="queryParams.status"
                        style="width: 100%"
                        placeholder="请选择当前状态"
                        clearable
                        filterable
                        size="small"
                    >
                        <el-option label="草稿" :value="0"></el-option>
                        <el-option label="未审核" :value="1"></el-option>
                        <el-option label="审核通过" :value="2"></el-option>
                        <el-option label="审核不通过" :value="3"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item class="btn-search" style="padding-left: 58px">
                    <el-button icon="el-icon-refresh" size="small" @click="refresh()"
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
                <div style="float: right">
                    <el-button icon="el-icon-plus" type="success" size="small" @click="handleAdd()"
                        >新增案件</el-button
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
                    prop="code"
                    label="非诉编号"
                    min-width="120"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="businessName"
                    label="非诉业务类型"
                    min-width="120"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    min-width="180"
                    prop="companyName"
                    label="公司名称"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    width="120"
                    prop="legalPerson"
                    label="法人"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="companyCode"
                    label="统一社会信用代码"
                    min-width="120"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="phone"
                    width="120"
                    label="电话"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="lawyerName"
                    label="承办律师"
                    min-width="120"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    width="90"
                    prop="contractFile"
                    label="合同"
                    align="center"
                >
                <template slot-scope="scope">
                    <div v-for="(item,index) in scope.row.contractFile" :key="index">
                        <el-link type="primary" style="font-size: 12px" @click="preview(item)">预览</el-link>
                    </div>
                </template>
                </el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    width="90"
                    prop="invoiceFile"
                    label="发票"
                    align="center"
                >
                    <template slot-scope="scope">
                        <div v-for="(item,index) in scope.row.invoiceFile" :key="index">
                            <el-link type="primary" style="font-size: 12px" @click="preview(item)">预览</el-link>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    width="120"
                    prop="status"
                    label="状态"
                    align="center"
                >
                    <template slot-scope="scope">
<!--                        <span>{{ scope.row.status == 1 ? '未审核' : scope.row.status == 2 ? '已审核' : '草稿' }}</span>-->
                      <span v-if="scope.row.status == 0">草稿</span>
                      <span v-if="scope.row.status == 1">未审核</span>
                      <span v-if="scope.row.status == 2">审核通过</span>
                      <span v-if="scope.row.status == 3">审核不通过</span>
                    </template>
                </el-table-column>
                <el-table-column show-overflow-tooltip label="操作" width="200" align="center">
                    <template slot-scope="scope">
                        <el-button type="primary" size="mini" plain @click="handleAdd(scope.row, 'detail')"
                            >详情</el-button>
                        <el-button type="warning" size="mini" plain @click="handleAdd(scope.row)" v-if="scope.row.status == 0 && scope.row.createUser == userInfo.id"
                            >草稿</el-button>
                        <el-button type="success" size="mini" plain @click="handleAdd(scope.row)" v-if="scope.row.status == 3 && scope.row.createUser == userInfo.id"
                            >编辑</el-button>
                        <el-button type="success" size="mini" plain @click="handleAdd(scope.row, 'check')" v-if="scope.row.status == 1 && userInfo.nameSpelling === 'wenquan'"
                            >审核</el-button>
                        <el-button type="warning" size="mini" plain @click="handlerUpload(scope.row)" v-if="scope.row.status == 2 && isShowUpload"
                            >上传文件</el-button>
                        <el-button type="danger" size="mini" plain @click="handleDel(scope.row.id)" v-if="scope.row.createUser == userInfo.id"
                            >删除</el-button>
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
        <addDialog ref="addEl" @submit="search"></addDialog>
        <WordPreview :show.sync="showWord" :list="realFileList"></WordPreview>
    </div>
</template>

<script>
import Pagination from '@/components/Pagination';
import { page, delById, save, submit } from '@/api/noLitigation';
import pagination from '@/mixins/pagination';
import { getRedisDictList } from '@/api/common'
import addDialog from './components/addDialog.vue';
import { uploadFileDialog } from '@/views/common/dialog';
import { mapGetters } from 'vuex';
import { getNormalUserList } from "@/api/statistics";
import WordPreview from "@/components/WordPreview";

export default {
    components: {
        Pagination,
        addDialog,
        WordPreview,
    },
    mixins: [pagination],
    data() {
        return {
            queryParams: {
                roleName: '',
                systemId: '',
            },
            showWord: false,
            businessList: [],
            realFileList: [],
            lawyerList:[],
        };
    },
    computed: {
        ...mapGetters(["userInfo", "roles"]),
        isShowUpload() {
            return this.roles.some(res => res.roleCode == '011' || res.roleCode == '010')
        },
    },
    created() {
        this.bindList(page, 'queryParams');
        // 律师列表
        getNormalUserList({}).then(res => {
            this.lawyerList = res.data
        })
        // 非诉讼业务类型
        getRedisDictList({dictType: 'NonLitigationBusiness'}).then(res => {
            this.businessList = res.data
        })
    },
    methods: {
        search() {
            this.handlerSearch()
        },
        refresh() {
            this.invoice = []
            this.handlerRefresh()
            this.queryParams.beginDate = ''
            this.queryParams.endDate = ''
        },
        changeTime(time) {
            if(time?.length > 0) {
                this.queryParams.beginDate = time[0]
                this.queryParams.endDate = time[1]
            } else {
                this.queryParams.beginDate = ''
                this.queryParams.endDate = ''
            }
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
        //新增
        handleAdd(row, type) {
            if (row) {
                const params = { ...row };
                delete params.id;
                this.$refs.addEl.showEdit(row, type)
            } else {
                this.$refs.addEl.showEdit()
            }
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
                    this.search()
                });
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
    },
};
</script>
<style lang="scss" scoped>
::v-deep .el-form-item__content {
    width: calc(100% - 140px) !important;
}

::v-deep .btn-search {
    .el-form-item__content {
        width: 100% !important;
    }
}
</style>

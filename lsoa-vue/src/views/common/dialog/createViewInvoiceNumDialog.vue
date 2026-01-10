<template>
    <DialogContainer :done="handlerDone" :cancel="handleCancel">
        <el-table
                :data="list"
                stripe
                height="440"
                class="table-container"
                highlight-current-row
            >
                <el-table-column
                    fixed="left"
                    type="index"
                    label="序号"
                    width="80"
                    align="center"
                >
                </el-table-column>
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
                <el-table-column show-overflow-tooltip prop="titleType" width="120" label="抬头类型" align="center">
                    <template slot-scope="scope">
                        {{ scope.row.titleType == 1 ? '个人' : '单位' }}
                    </template>
                </el-table-column>
                <el-table-column show-overflow-tooltip prop="title" width="120" label="抬头名称" align="center"> </el-table-column>
                <el-table-column show-overflow-tooltip prop="number" width="120" label="单位税号" align="center"> </el-table-column>
                <el-table-column show-overflow-tooltip prop="invoiceTypeName" label="发票类型" align="center">
                </el-table-column>
                <el-table-column show-overflow-tooltip prop="invoiceMoney" label="开票金额" align="center"> </el-table-column>
                <el-table-column fixed="right" label="操作" align="center" width="200">
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            type="primary"
                            plain
                            @click="handlerView(scope.$index,scope.row)"
                            >详情</el-button
                        >
                        <template v-if="scope.row.createUser == userInfo.id">
                            <el-button
                                size="mini"
                                v-if="isEditShow(scope.$index, scope.row)"
                                @click="handlerInvoicing(scope.$index, scope.row)"
                                type="warning"
                                plain
                                >修改</el-button
                            >
                        </template>
                        <el-button
                                size="mini"
                                v-if="isShowUpload(scope.$index)"
                                @click="handlerUpload(scope.row)"
                                type="warning"
                                plain
                                >上传文件</el-button
                            >
                        <el-button
                                size="mini"
                                :data-rr="userInfo.id"
                                @click="handlerCheck(scope.row)"
                                v-if="isShow(scope.$index, scope.row)"
                                type="success"
                                plain
                                >审核</el-button
                            >
                        <el-button
                            v-if="scope.row.fileList && scope.row.fileList.length > 0"
                            size="mini"
                            @click="handlerDownload(scope.row)"
                            type="info"
                            plain
                            >下载</el-button
                        >
                    </template>
                    </el-table-column>
        </el-table>
        <createInvoicingDialog @submit="getList(() => {
                        invoiceInfo.invoice = 2
                    })" ref="invoicingDialog" />
    </DialogContainer>
</template>
<script lang="js">
import Pagination from '@/components/Pagination';
import DialogContainer from '@/components/DialogFactory/dialog-container.vue';
import { mapGetters } from 'vuex';
import handleRequired from '@/views/common/handleRequired.vue';
import { getEntity, getInvoiceList } from '@/api/invoice';
import { createCheckDialog, downloadListDialog, uploadFileDialog } from '@/views/common/dialog';
import createInvoicingDialog from '@/views/common/dialog/createInvoicingDialog.vue';
import { toInvoice } from '@/api/case';
import { getLetterList } from '@/api/letter';

export default {
    components: { Pagination, DialogContainer, handleRequired, createInvoicingDialog },
    data() {
        return {
            list: [],
            invoiceInfo: {},
            page: 'detail',
        };
    },
    computed: {
        ...mapGetters(["userInfo", "roleIds", "roles"]),
    },
    methods: {
        getList(cb) {
            getInvoiceList({id: this.invoiceInfo.id}).then(res => {
                this.list = res.data.map(val => {
                    val.userName = this.invoiceInfo.userName
                    val.client = this.invoiceInfo.client
                    val.tarClient = this.invoiceInfo.tarClient
                    val.subjectOfAction = this.invoiceInfo.subjectOfAction
                    val.court = this.invoiceInfo.court
                    val.delegate = this.invoiceInfo.delegate
                    return val
                })
                cb?.()
            })
        },
        isShowUpload(index) {
            return ((index == 0 && this.invoiceInfo.invoice == 3)  || index > 0) && this.roles.some(res => res.roleCode == '011' || res.roleCode == '010')
        },
        isShow(index, row) {
            const hasPermissions = row.approveIds?.approverValue.split(',').some(item => {
                return this.roleIds.includes(Number(item))
            })
            return (index == 0) && this.invoiceInfo.invoice == 2 && ((row.approveIds?.approverType == 1 && row.approveIds?.approverValue.split(',').includes(`${this.userInfo.id}`))
             || (row.approveIds?.approverType == 2 && hasPermissions))
        },
        isEditShow(index, row) {
            return (index == 0) && this.invoiceInfo.invoice == 4
        },
        handlerView(index,row) {
            if(row.id) {
                getEntity({id: row.id}).then(res => {
                    console.log('999999999999999row', {...row,...res.data, invoice: this.invoiceInfo.invoice})
                    createCheckDialog(
                        {
                            detailInfo: {...res.data,...row,back:res.data.back, invoice: this.invoiceInfo.invoice},
                            listIndex: index,
                            page:'detail'
                        },
                        {title: '详情', width: '960px', confirmText: '', cancelText: '取消'}
                    ).then((res) => {
                        
                    });
                    
                })
            } else {
                createCheckDialog(
                        {
                            detailInfo: {},
                            listIndex: index,
                            page:'detail'
                        },
                        {title: '详情', width: '960px', confirmText: '', cancelText: '取消'}
                    ).then((res) => {
                        
                    });
            }
            
        },
        async handlerCheck(row) {
            // 查询开函审核的数据
            const letterList = await getLetterList({id: this.invoiceInfo.id})
            getEntity({id: row.id}).then(res => {
                createCheckDialog(
                    {
                        detailInfo: {...res.data,...row,back:res.data.back, letter:this.invoiceInfo.letter},
                        letterList: letterList.data,
                        page:'check'
                    },
                    {title: '开票审核', width: '960px', confirmText: '通过',cancelText: '退回'}
                ).then((res) => {
                    this.getList(() => {
                        this.invoiceInfo.invoice = res
                    })
                });
                    
            })
            
        },
        handlerDownload(row) {
            getEntity({id: row.id}).then(res => {
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
        handlerUpload(row) {
            uploadFileDialog(
                    {
                        info: {invoiceId: row.id, id:4, fileList: row.fileList}
                    },
                    {title: '上传文件', width: '900px', confirmText: '',cancelText: '取消'}
                ).then(() => {
                    this.getList()
                })
        },
        handlerInvoicing(index, row) {
            const params = {invoiceId: row.id,...row }
            this.$refs.invoicingDialog.edit({...this.invoiceInfo,...params,id:this.invoiceInfo.id,invoice:this.invoiceInfo.invoice })
        },
        handlerDone() {
        },
        handleCancel() {
            return new Promise((resolve, reject) => {
                this.$emit('submit',1)
                resolve(true)
            })
        }
    },
};
</script>
<style scoped lang="scss">
.detail-info-box {
    background: #FFFFFF;
    box-shadow: 0px 0px 12px 0px rgba(230,237,241,0.5);
    border-radius: 16px 16px 16px 16px;
    border: 1px solid rgba(0,0,0,0.06);
    padding: 29px 24px 14px;
    margin-bottom: 16px;

    .info-wrap {
        display: flex;
        align-content: center;
        margin-bottom: 16px;

        .name {
            font-size: 14px;
            color: rgba(0,0,0,0.65);
            line-height: 22px;
            width: 100px;
        }

        .value {
            margin-left: 16px;
            font-size: 14px;
            color: rgba(0,0,0,0.85);
            line-height: 22px;
            flex:1;
        }
    }
}
</style>
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
                <el-table-column show-overflow-tooltip prop="name" width="120" label="委托人名称" align="center"> </el-table-column>
                <el-table-column show-overflow-tooltip prop="code" width="120" label="委托人编码" align="center"> </el-table-column>
                <!-- <el-table-column show-overflow-tooltip prop="type" label="函件类型" align="center">
                    <template slot-scope="scope">
                        <span>{{ letterType[scope.row.type]}}</span>
                    </template>
                </el-table-column>
                <el-table-column show-overflow-tooltip prop="caseName" label="相关案件" align="center"> </el-table-column>
                <el-table-column show-overflow-tooltip prop="caseCode" label="事件编码" align="center"> </el-table-column>
                <el-table-column show-overflow-tooltip prop="printTypeName" label="用印方式" align="center">
                </el-table-column>
                <el-table-column show-overflow-tooltip width="180" prop="summonsTime" label="开函日期" align="center"></el-table-column>
                <el-table-column show-overflow-tooltip prop="useing" label="用途说明" align="center"></el-table-column> -->
                <el-table-column fixed="right" label="操作" align="center" width="140">
                        <template slot-scope="scope">
                            <el-button
                            size="mini"
                            type="primary"
                            plain
                            @click="handlerView(scope.$index, scope.row)"
                            >详情</el-button>
                            <el-button
                                size="mini"
                                @click="handlerLetter(scope.$index, scope.row)"
                                v-if="isShowLetter(scope.$index, scope.row)"
                                type="warning"
                                plain
                                >修改</el-button
                            >
                            <el-button
                                    size="mini"
                                    @click="handlerCheck(scope.$index, scope.row)"
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
        <createLetterDialog @submit="getList(() => {
                        letterInfo.letter = 2
                    })" ref="letterDialog" />
    </DialogContainer>
</template>
<script lang="js">
import Pagination from '@/components/Pagination';
import DialogContainer from '@/components/DialogFactory/dialog-container.vue';
import { mapGetters } from 'vuex';
import handleRequired from '@/views/common/handleRequired.vue';
import { createLetterCheckDialog, downloadListDialog } from '@/views/common/dialog';
import createLetterDialog from '@/views/common/dialog/createLetterDialog.vue';
import { getEntity, getLetterList } from '@/api/letter';
import { toInvoice } from '@/api/case';
import { getInvoiceList } from '@/api/invoice';

export default {
    components: { Pagination, DialogContainer, handleRequired, createLetterDialog, },
    data() {
        return {
            list: [],
            letterInfo: {},
            page: 'detail',
            letterType:['','刑函','仲裁函','民函件','刑函(法援)','破产清算']
        };
    },
    computed: {
        ...mapGetters(["userInfo", "roleIds"]),
    },
    methods: {
        getList(cb) {
            getLetterList({id: this.letterInfo.id}).then(res => {
                this.list = res.data.map(val=> {
                    val.userName = this.letterInfo.userName
                    val.client = this.letterInfo.client
                    val.tarClient = this.letterInfo.tarClient
                    val.subjectOfAction = this.letterInfo.subjectOfAction
                    val.court = this.letterInfo.court
                    val.delegate = this.letterInfo.delegate
                    return val
                })
                cb?.()
            })
        },
        isShow(index, row) {
            const hasPermissions = row.approveIds?.approverValue.split(',').some(item => {
                return this.roleIds.includes(Number(item))
            })
            return (index == 0) && this.letterInfo.letter == 2 && ((row.approveIds?.approverType == 1 && row.approveIds?.approverValue.split(',').includes(`${this.userInfo.id}`))
             || (row.approveIds?.approverType == 2 && hasPermissions))
        },
        isShowLetter(index, row) {
            return (index == 0) && this.letterInfo.letter == 4 && row.createUser == this.userInfo.id
        },
        handlerView(index,row) {
            console.log('888888888888888row', row)
            if(row.id) {
                getEntity({id: row.id}).then(res => {
                    createLetterCheckDialog(
                        {
                            detailInfo: {...res.data,...row,back:res.data.back,letter:this.letterInfo.letter},
                            page: 'detail',
                            listIndex: index
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
                        listIndex: index,
                        page: 'detail'
                    },
                    {title: '详情', width: '960px', confirmText: '', cancelText: '取消'}
                ).then((res) => {
                    console.log('处理数据',res)
                });
            }
        },
        async handlerCheck(index,row) {
            // 查询开函审核的数据
            const invoiceList = await getInvoiceList({id: this.letterInfo.id})
            getEntity({id: row.id}).then(res => {
                createLetterCheckDialog(
                    {
                        detailInfo: { ...res.data,...row,back:res.data.back,invoice:this.letterInfo.invoice},
                        invoiceList: invoiceList.data,
                        page: 'check'
                    },
                    {title: '开函审核', width: '960px', confirmText: '通过',cancelText: '退回'}
                ).then((res) => {
                    console.log('33333333333333333res', res)
                    this.getList(() => {
                        this.letterInfo.letter = res
                    })
                    
                });
            })
        },
        handlerDownload(row) {
            getEntity({id: row.id}).then(res => {
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
            const params = {summonsId: row.id,...row }
            console.log('888888888888params', params, row)
            this.$refs.letterDialog.edit({...this.letterInfo,...params,id:this.letterInfo.id})
        },
        handlerDone() {
            return true
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
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
                <el-table-column show-overflow-tooltip prop="titleType" width="120" label="抬头类型" align="center">
                    <template slot-scope="scope">
                        {{ scope.row.titleType == 1 ? '个人' : '单位' }}
                    </template>
                </el-table-column>
                <el-table-column show-overflow-tooltip prop="title" width="120" label="抬头名称" align="center"> </el-table-column>
                <el-table-column show-overflow-tooltip prop="number" width="120" label="单位税号" align="center"> </el-table-column>
                <el-table-column show-overflow-tooltip prop="registeredAdd" width="120" label="注册地址" align="center"> </el-table-column>
                <el-table-column show-overflow-tooltip prop="invoiceType" width="160" show-tool label="发票类型" align="center">
                    <template slot-scope="scope">
                        {{ scope.row.invoiceType == 1 ? '增值税普通发票' : '专用发票' }}
                    </template>
                </el-table-column>
                <el-table-column show-overflow-tooltip prop="invoiceMoney" label="开票金额" align="center"> </el-table-column>
                <el-table-column show-overflow-tooltip prop="content" label="发票内容" align="center"> </el-table-column>
                <el-table-column fixed="right" label="操作" align="center" width="100">
                    <template slot-scope="scope">
                        <template v-if="scope.row.createUser == userInfo.id || isShow">
                            <el-button type="warning" size="mini" v-if="scope.row.invoice == 4" plain @click="handleInvoice(scope.row, 'edit')"
                                >修改</el-button
                            >
                        </template>
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
import { invoiceDetail } from '@/api/agreementCustomer';
import createInvoicingDialog from '@/views/common/dialog/createInvoicingDialog.vue';
import { CreateAddOrUpdate, CreateAddInvoice } from '@/views/agreementCustomer/components';

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
        isShow() {
            return this.roles.some(res => res.roleCode == 'gwkhgly')
        }
    },
    mounted() {
        if(this.invoiceInfo.invoiceNum - 1 >= 0) {
            this.getList()
        }
        
    },
    methods: {
        getList(invoiceStatus) {
            // 请求开票列表数据
            invoiceDetail({id: this.invoiceInfo.id}).then(res => {
                this.list = res.data.map((val,index) => {
                    if(index == 0) {
                        val.invoice = invoiceStatus ? invoiceStatus :this.invoiceInfo.invoice
                    }
                    return val
                })
                
            })
        },
        async handleInvoice(row, type) {
            // 获取顾问单位的开票信息
            CreateAddInvoice(
                {
                    id: row.caseId,
                    form: row,
                },
                { title: '顾问单位开票'}
            ).$on('submit', (res) => {
                this.$message.success('开票成功');
                this.getList(3)
            });
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
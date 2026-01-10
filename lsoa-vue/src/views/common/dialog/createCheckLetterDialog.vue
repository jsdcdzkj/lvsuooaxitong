<template>
    <DialogContainer :done="handlerDone" :reject="page == 'detail' ? null : handleReject">
        <div class="title">案件信息</div>
        <div class="detail-info-box">
            <div class="info-wrap">
                <div class="name">委托人：</div>
                <div class="value">{{ detailInfo.client || '--'}}</div>
            </div>
            <div class="info-wrap">
                <div class="name">对方当事人：</div>
                <div class="value">{{ detailInfo.tarClient || '--'}}</div>
            </div>
            <div class="info-wrap">
                <div class="name">案由：</div>
                <div class="value">{{ detailInfo.subjectOfAction || '--'}}</div>
            </div>
            <div class="info-wrap">
                <div class="name">承办律师：</div>
                <div class="value">{{ detailInfo.userName || '--'}}</div>
            </div>
            <div class="info-wrap">
                <div class="name">委托方式：</div>
                <div class="value">{{ detailInfo.delegate == 1 ? '顾问单位' : detailInfo.delegate == 2 ? '非顾问单位' : '--'}}</div>
            </div>
            <div v-if="detailInfo.caseFiles && detailInfo.caseFiles.length > 0">
                <div class="name">合同文件：</div>
                <div class="value" style="margin-top: 10px">
                    <handleRequired :fileList="detailInfo.caseFiles" detail></handleRequired>
                </div>
            </div>
        </div>
        <div class="title">开函信息</div>
        <div class="detail-info-box">
            <div class="info-wrap">
                <div class="name">委托人类型：</div>
                <div class="value">
                    <el-radio-group v-model="detailInfo.personType">
                        <el-radio label="1" disabled>自然人 </el-radio>
                        <el-radio label="2" disabled>法人</el-radio>
                        <el-radio label="3" disabled>个体工商户</el-radio>
                        <el-radio label="4" disabled>受害人</el-radio>
                        <el-radio label="5" disabled>顾问单位</el-radio>
                    </el-radio-group>
                </div>
            </div>
            <div class="info-wrap">
                <div class="name">名称：</div>
                <div class="value">{{ detailInfo.name || '--'}}</div>
            </div>
            <div class="info-wrap" v-if="['1', '4'].includes(detailInfo.personType)">
                <div class="name">身份证号：</div>
                <div class="value">{{ detailInfo.code || '--'}}</div>
            </div>
            <div class="info-wrap" v-if="['2', '3', '5'].includes(detailInfo.personType)">
                <div class="name">单位统一代码：</div>
                <div class="value">{{ detailInfo.code || '--'}}</div>
            </div>
            <div class="info-wrap">
                <div class="name">联系方式：</div>
                <div class="value">{{ detailInfo.phone || '--'}}</div>
            </div>
            <!-- <div class="info-wrap">
                <div class="name">函件类型：</div>
                <div class="value">
                    <el-radio-group v-model="detailInfo.type">
                        <el-radio label="1" disabled>刑函</el-radio>
                        <el-radio label="2" disabled>仲裁函</el-radio>
                        <el-radio label="3" disabled>民函件</el-radio>
                        <el-radio label="4" disabled>刑函(法援)</el-radio>
                        <el-radio label="5" disabled>破产清算</el-radio>
                    </el-radio-group>
                </div>
            </div>
            <div class="info-wrap">
                <div class="name">相关案件：</div>
                <div class="value">{{ detailInfo.caseName || '--'}}</div>
            </div>
            <div class="info-wrap">
                <div class="name">事项编码：</div>
                <div class="value">{{ detailInfo.caseCode || '--'}}</div>
            </div>
            <div class="info-wrap">
                <div class="name">用印方式：</div>
                <div class="value">{{ detailInfo.printTypeName || '--'}}</div>
            </div>
            <div class="info-wrap">
                <div class="name">用途说明：</div>
                <div class="value">{{ detailInfo.useing || '--'}}</div>
            </div>
            <div class="info-wrap">
                <div class="name">收件方：</div>
                <div class="value">
                    <el-radio-group v-model="detailInfo.addressType">
                        <el-radio label="1" disabled>个人</el-radio>
                        <el-radio label="2" disabled>单位</el-radio>
                    </el-radio-group>
                </div>
            </div>
            <div class="info-wrap">
                <div class="name">名称：</div>
                <div class="value">{{ detailInfo.addressee || '--'}}</div>
            </div>
            <div class="info-wrap">
                <div class="name">收件地址：</div>
                <div class="value">{{ detailInfo.address || '--'}}</div>
            </div>
            <div class="info-wrap">
                <div class="name">联系方式：</div>
                <div class="value">{{ detailInfo.addresseePhone || '--'}}</div>
            </div>
            <div class="info-wrap">
                <div class="name">特殊格式要求：</div>
                <div class="value">{{ detailInfo.specialDemand || '--'}}</div>
            </div>
            <div class="info-wrap">
                <div class="name">紧急程度：</div>
                <div class="value">{{ detailInfo.level || '--'}}</div>
            </div>
            <div class="info-wrap">
                <div class="name">份数要求：</div>
                <div class="value">{{ detailInfo.num || '--'}}</div>
            </div>
            <div class="info-wrap">
                <div class="name">送达方式：</div>
                <div class="value">{{ detailInfo.sendType || '--'}}</div>
            </div> -->
            <div class="info-wrap" v-if="detailInfo.letter == 4 && detailInfo.back && listIndex == 0">
                <div class="name">退回原因：</div>
                <div class="value">{{ detailInfo.back || '--'}}</div>
            </div>
        </div>
        <!-- <div class="detail-info-box" v-if="detailInfo.fileList && detailInfo.fileList.length > 0">
            <div class="name" style="margin-bottom: 16px">开函文件：</div>
            <handleRequired :fileList="detailInfo.fileList" detail></handleRequired>
        </div> -->
        <template v-if="invoiceList.length > 0">
            <div class="title">开票信息</div>
            <div class="detail-info-box" v-for="(item,index) in invoiceList" :key="index">
                <img v-if="index == 0 && detailInfo.invoice == 4" class="check-icon" src="@/assets/icon-reject.png" alt="">
                <img v-else-if="index == 0 && detailInfo.invoice == 2" class="check-icon" src="@/assets/icon-checking.png" alt="">
                <img v-else-if="(index == 0 && detailInfo.invoice == 3)" class="check-icon" src="@/assets/icon-agree.png" alt="">
                
                <div class="info-wrap">
                <div class="name">抬头类型：</div>
                <div class="value">
                    <el-radio-group v-model="item.titleType">
                        <el-radio label="1" disabled>个人</el-radio>
                        <el-radio label="2" disabled>单位</el-radio>
                    </el-radio-group>
                </div>
            </div>
            <div class="info-wrap">
                <div class="name">抬头名称：</div>
                <div class="value">{{ item.title || '--'}}</div>
            </div>
            <div class="info-wrap" v-if="item.titleType == 2">
                <div class="name">单位税号：</div>
                <div class="value">{{ item.number || '--'}}</div>
            </div>
            <div class="info-wrap">
                <div class="name">发票类型：</div>
                <div class="value">
                    <el-radio-group v-model="item.invoiceType">
                        <el-radio label="1" disabled>增值税普通发票</el-radio>
                        <el-radio label="2" disabled>专用发票</el-radio>
                    </el-radio-group>
                </div>
            </div>
            <div class="info-wrap">
                <div class="name">开票金额：</div>
                <div class="value">{{ (item.invoiceMoney && Number(item.invoiceMoney).toFixed(2)) || '--'}}</div>
            </div>
            <div class="info-wrap">
                <div class="name">发票内容：</div>
                <div class="value">{{ item.content || '--'}}</div>
            </div>
            </div>
        </template>
    </DialogContainer>
</template>
<script lang="js">
import DialogContainer from '@/components/DialogFactory/dialog-container.vue';
import { approveInvoiceInfo } from '@/api/letter';
import handleRequired from '@/views/common/handleRequired.vue';

export default {
    components: { DialogContainer, handleRequired },
    data() {
        return {
            detailInfo: {},
            page: 'detail',
            listIndex: 0,
            invoiceList:[],
        };
    },
    methods: {
        async handlerDone() {
            try {
                await approveInvoiceInfo({id: this.detailInfo.id,approveStatus: '1'})
                this.$message({
                    type: 'success',
                    message: '审核成功!',
                });
                this.$emit('submit',3);
                return true;
            } catch (e) {
                console.log('4444444444e',e)
                return false;
            }
        },
        async handleReject() {
            return new Promise((resolve,reject) => {
                this.$prompt('', '退回原因', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    inputPattern: /^(?!\s*$).+/,
                    inputErrorMessage: '请输入退回原因'
                }).then(async({ value }) => {
                    await approveInvoiceInfo({id: this.detailInfo.id,approveStatus: '2', back: value})
                    this.$message({
                        type: 'success',
                        message: '退回成功!',
                    });
                    this.$emit('submit',4);
                    resolve(true);
                }).catch(() => {
                    reject(false) 
                });
            })
        }
    },
};
</script>
<style scoped lang="scss">
.title {
    font-size: 16px;
    line-height: 36px;
    font-weight: bold;
    margin-left: 4px;
}
.detail-info-box {
    background: #FFFFFF;
    box-shadow: 0px 0px 12px 0px rgba(230,237,241,0.5);
    border-radius: 16px 16px 16px 16px;
    border: 1px solid rgba(0,0,0,0.06);
    padding: 29px 24px 14px;
    margin-bottom: 16px;
    position: relative;

    .check-icon {
        position: absolute;
        right: 16px;
        top: 24px;
        width: 150px;
        height: 150px;
        transform: rotate(45deg);
    }

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
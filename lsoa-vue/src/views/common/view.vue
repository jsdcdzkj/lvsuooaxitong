<template>
    <div class="app-container">
        <div class="header-title">
            <div></div>
            <el-button @click="handleBack" size="small">返回</el-button>
        </div>
        <div class="detail-content">
            <div class="common-content">
                <div class="common-title">当事人</div>
                <el-form
                    :inline="true"
                    label-position="right"
                    class="baseInfo litigantInfo"
                    label-width="110px"
                >
                    <el-row :gutter="24">
                        <el-col :span="8" class="client-man">
                            <el-form-item label="委托当事人" prop="caseDesc">
                                <el-select
                                    v-model="listQuery.pod"
                                    style="width: 100%"
                                    placeholder="请选择"
                                    clearable
                                    disabled
                                    filterable
                                    size="small"
                                >
                                    <!-- 1.原告2.被告3.犯罪嫌疑人 4.第三人(非原被告)5.受害人" -->
                                    <el-option label="原告" value="1" ></el-option>
                                    <el-option label="被告" value="2" ></el-option>
                                    <el-option label="犯罪嫌疑人" value="3" ></el-option>
                                    <el-option label="第三人(非原被告)" value="4" ></el-option>
                                    <el-option label="受害人" value="5" ></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row :gutter="24">
                        <el-col :span="8" v-for="(item, index) in listQuery.plainList" :key="index">
                            <div class="card">
                                <div class="avatar">
                                    <img src="@/assets/img/avatar.png" alt="" />
                                    <div class="tip">当事人{{ index + 1 }}</div>
                                </div>

                                <div class="client-man">
                                    <el-form-item label="姓名" v-if="item.isPerson == 1 || item.isPerson == 4">
                                        <el-input
                                            v-model="listQuery.plainList[index].name"
                                            size="small"
                                            placeholder="请输入姓名"
                                            disabled
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                    <el-form-item label="性别" v-if="item.isPerson == 1 || item.isPerson == 4">
                                        <el-radio-group v-model="listQuery.plainList[index].sex">
                                            <el-radio disabled label="1">男</el-radio>
                                            <el-radio disabled label="2">女</el-radio>
                                        </el-radio-group>
                                    </el-form-item>
                                    <el-form-item class="code" label="身份证号" v-if="item.isPerson == 1 || item.isPerson == 4">
                                        <el-input
                                            v-model="listQuery.plainList[index].code"
                                            size="small"
                                            disabled
                                            placeholder="请输入身份证号"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                    <el-form-item label="单位名称" v-if="!(item.isPerson == 1 || item.isPerson == 4)">
                                        <el-input
                                            v-model="listQuery.plainList[index].name"
                                            size="small"
                                            placeholder="请输入单位名称"
                                            :maxlength="50"
                                            disabled
                                            clearable
                                        />
                                    </el-form-item>
                                    <el-form-item label="统一代码" v-if="!(item.isPerson == 1 || item.isPerson == 4)">
                                        <el-input
                                            v-model="listQuery.plainList[index].code"
                                            size="small"
                                            disabled
                                            placeholder="请输入统一代码"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                    <el-form-item label="联系方式">
                                        <el-input
                                            v-model="listQuery.plainList[index].phone"
                                            size="small"
                                            disabled
                                            placeholder="请输入联系方式"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                </div>
                            </div>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
            <div class="common-content">
                <div class="common-title">基本信息</div>
                <el-form ref="formInline" :inline="true" :model="listQuery" label-position="top" :rules="rules" class="baseInfo" label-width="110px">
                    <el-row :gutter="24">
                        <!-- <el-col :span="8">
                            <el-form-item label="案件名称" prop="caseName">
                                <el-input
                                    v-model="listQuery.caseName"
                                    size="small"
                                    placeholder="请输入案件名称"
                                    :maxlength="50"
                                    disabled
                                    clearable
                                />
                            </el-form-item>
                        </el-col> -->
                        <el-col :span="8">
                            <el-form-item label="案件类型" prop="caseType">
                                <el-select
                                    v-model="listQuery.caseType"
                                    placeholder="请选择案件类型"
                                    clearable
                                    filterable
                                    disabled
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
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="业务类型" prop="businessType">
                                <el-select
                                    v-model="listQuery.businessType"
                                    placeholder="请选择业务类型"
                                    clearable
                                    disabled
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
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="费用（元）" prop="money">
                                <el-input
                                    v-model="listQuery.money"
                                    size="small"
                                    placeholder="请输入"
                                    :maxlength="50"
                                    clearable
                                    disabled
                                />
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="标的额" prop="subjectAmount">
                                <el-input
                                    v-model="listQuery.subjectAmount"
                                    size="small"
                                    placeholder="请输入标的额"
                                    :maxlength="50"
                                    clearable
                                    disabled
                                />
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="诉讼阶段" prop="stage">
                                <el-select
                                    v-model="listQuery.stage"
                                    placeholder="请选择诉讼阶段"
                                    clearable
                                    filterable
                                    size="small"
                                    disabled
                                >
                                    <el-option
                                        v-for="(item, index) in litigationList"
                                        :key="index"
                                        :label="item.dictLabel"
                                        :value="item.dictValue"
                                    ></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="案件状态" prop="status">
                                <el-select
                                    v-model="listQuery.status"
                                    style="width: 100%"
                                    placeholder="请选择案件状态"
                                    clearable
                                    filterable
                                    disabled
                                    size="small"
                                >
                                    <el-option
                                        v-for="(item, index) in caseStatusList"
                                        :key="index"
                                        :label="item"
                                        :value="`${index + 1}`"
                                    ></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        
                        <el-col :span="8">
                            <el-form-item label="办案机构" prop="court">
                                <el-input
                                    v-model="listQuery.court"
                                    size="small"
                                    disabled
                                    placeholder="请输入办案机构"
                                    :maxlength="50"
                                    clearable
                                />
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="特殊审批" prop="riskOrFree">
                                <el-radio-group disabled v-model="listQuery.riskOrFree">
                                    <el-radio label="1">风险代理</el-radio>
                                    <el-radio label="2">费用减免</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="24">
                            <el-form-item label="案由" prop="subjectOfAction">
                                <el-input
                                    disabled
                                    v-model="listQuery.subjectOfAction"
                                    size="small"
                                    type="textarea"
                                    placeholder="请输入案由"
                                    clearable
                                />
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
            <div class="common-content" v-if="listQuery.defendList && listQuery.defendList.length > 0">
                <div class="common-title">对方当事人</div>
                <el-form
                    :inline="true"
                    label-position="right"
                    class="baseInfo litigantInfo"
                    label-width="110px"
                    style="border-top: 1px dashed rgba(0,0,0,0.06)"
                >
                    <el-row :gutter="24">
                        <el-col :span="8"  class="client-man">
                            <el-form-item label="对方当事人" prop="caseDesc">
                                <el-select
                                    v-model="listQuery.podOther"
                                    style="width: 100%"
                                    placeholder="请选择"
                                    disabled
                                    clearable
                                    filterable
                                    size="small"
                                >
                                    <el-option label="原告" value="1" ></el-option>
                                    <el-option label="被告" value="2" ></el-option>
                                    <el-option label="犯罪嫌疑人" value="3" ></el-option>
                                    <el-option label="第三人(非原被告)" value="4" ></el-option>
                                    <el-option label="受害人" value="5" ></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>   
                    <el-row :gutter="24">
                        <el-col :span="8" v-for="(item, index) in listQuery.defendList" :key="index">
                            <div class="card">
                                <div class="avatar">
                                    <img src="@/assets/img/avatar.png" alt="" />
                                    <div class="tip">当事人{{ index + 1 }}</div>
                                </div>

                                <div class="client-man">
                                    <el-form-item label="姓名" v-if="item.isPerson == 1 || item.isPerson == 4">
                                        <el-input
                                            v-model="listQuery.defendList[index].name"
                                            size="small"
                                            disabled
                                            placeholder="请输入姓名"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                    <el-form-item label="性别" v-if="item.isPerson == 1 || item.isPerson == 4">
                                        <el-radio-group v-model="listQuery.defendList[index].sex">
                                            <el-radio disabled label="1">男</el-radio>
                                            <el-radio disabled label="2">女</el-radio>
                                        </el-radio-group>
                                    </el-form-item>
                                    <el-form-item class="code" label="身份证号" v-if="item.isPerson == 1 || item.isPerson == 4">
                                        <el-input
                                            v-model="listQuery.defendList[index].code"
                                            size="small"
                                            disabled
                                            placeholder="请输入身份证号"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                    <el-form-item label="单位名称" v-if="!(item.isPerson == 1 || item.isPerson == 4)">
                                        <el-input
                                            v-model="listQuery.defendList[index].name"
                                            size="small"
                                            disabled
                                            placeholder="请输入单位名称"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                    <el-form-item label="统一代码" v-if="!(item.isPerson == 1 || item.isPerson == 4)">
                                        <el-input
                                            v-model="listQuery.defendList[index].code"
                                            size="small"
                                            disabled
                                            placeholder="请输入统一代码"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                    <el-form-item label="联系方式">
                                        <el-input
                                            v-model="listQuery.defendList[index].phone"
                                            size="small"
                                            disabled
                                            placeholder="请输入联系方式"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                </div>
                            </div>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
            <div class="common-content">
                <div class="common-title">附件管理</div>
                <div class="annex-wrap">
                    <el-radio-group v-model="annexType" size="mini">
                        <el-radio-button v-for="(item,index) in fileTypeList" :key="index" @change="handleChange(item.dictValue)" :label="item.dictValue">{{ item.dictLabel }}</el-radio-button>
                        <el-radio-button label="-1" @change="handleChange('-1')">其他</el-radio-button>
                    </el-radio-group>
                    <div style="width: 100%" v-for="(item,index) in fileTypeList" :key="index">
                        <handleRequired detail v-show="annexType == item.dictValue" :id="item.dictValue"
                        :fileList="listQuery.fileList[item.dictValue] || []"
                        />
                    </div>
                    <div style="width: 100%" v-show="annexType == '-1'">
                        <handleRequired detail id="-1"
                        :fileList="otherFile || []"
                         />
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import handleRequired from './handleRequired.vue'
import { getRedisDictList, getCaseFileType } from '@/api/common';
import { getEntity, getCustomList } from '@/api/case'
import { invoiceTypeList, letterTypeList, caseStatusList } from '@/utils/comvariable';


export default {
    components: {handleRequired},
    data() {
        return {
            query: {},
            listQuery: {
                clientInfoVoList: [],
                fileList:[],
            },
            litigantList: [{}, {}],
            disabled: false,
            annexType: 0,
            businessList: [],
            caseList: [],
            litigationListL: [],
            invoiceTypeList,
            letterTypeList,
            caseStatusList,
            litigationList: [],
            fileTypeList: [],
            otherFile: [],
            rules: {
                subjectAmount: [{ required: true, message: '请输入', trigger: 'blur' }],
                caseName: [{ required: true, message: '请输入', trigger: 'blur' }],
                caseType: [{ required: true, message: '请选择', trigger: 'blur' }],
                businessType: [{ required: true, message: '请输入', trigger: 'blur' }],
                money: [{ required: true, trigger: 'blur', message: '请输入' }],
                stage: [{ required: true, trigger: 'blur', message: '请输入' }],
                status:  [{ required: true, message: '请选择', trigger: 'blur' }],
                caseDesc: [{ required: true, message: '请输入', trigger: 'blur' }],
                money: [{ required: true, message: '请输入', trigger: 'blur' }],
                subjectAmount: [{ required: true, message: '请输入', trigger: 'blur' }],
            },
            customList: [],
        };
    },
    mounted() {
        getCustomList({}).then(res => {
            this.customList = res.data
        })
    },
    methods: {
        showEdit(row) {
            this.getInfo()
            this.otherFile = []
            getEntity({id: row.id}).then(res=> {
                this.listQuery = res.data
                this.listQuery.pod = this.listQuery.plainList[0].pod
                this.listQuery.podOther = this.listQuery.defendList[0].pod
                const keys = Object.keys(this.listQuery.fileList)
                keys.map(val => {
                    console.log('9999999999944444', val, this.fileTypeList, this.fileTypeList.some(item => item.dictValue == val))
                    if (!this.fileTypeList.some(item => item.dictValue == val)) {
                        this.otherFile.push(...this.listQuery.fileList[val])
                    }
                })
            })
        },
        handleBack() {
            this.$emit('back')
        },
        close() {},
        save() {},
        getInfo() {
            // 案件类型
            getRedisDictList({ dictType: 'caseType' }).then((res) => {
                this.caseList = res.data;
            });
            // 业务类型
            getRedisDictList({ dictType: 'businessType' }).then((res) => {
                this.businessList = res.data;
            });
            // 诉讼阶段
            getRedisDictList({ dictType: 'litigationStage' }).then((res) => {
                this.litigationList = res.data;
            });
            // 文件类型
            getCaseFileType({}).then(res => {
                this.fileTypeList = res.data
                this.annexType = res.data[0].dictValue
            })
        },
    },
    
};
</script>

<style lang="scss" scoped>
.header-title {
    width: 100%;
    height: 64px;
    background: #ffffff;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 24px;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 10;
    box-shadow: inset 0px 0px 32px 0px rgba(198, 215, 225, 0.64);
}
.detail-content {
    margin-top: 60px;
    .el-form-item--small.el-form-item {
        width: 100%;
    }
}

::v-deep.litigantInfo {
    .el-form-item__content {
        width: calc(100% - 110px);
    }
}

::v-deep {
    .client-man {
        .el-form-item__label {
            &::before {
                content: '*';
                color: #F56C6C;
                margin-right: 4px;
            }
        }
    }

    .code {
        .el-form-item__label {
            &::before {
                content: '';
            }
        }
    }
    
}

.common-content {
    background: #fff;
    margin-bottom: 24px;
    border-radius: 24px;
    .common-title {
        height: 48px;
        line-height: 48px;
        padding-left: 24px;
        background: linear-gradient(0deg, #f1f6ff 0%, #ffffff 100%);
        border-radius: 16px 16px 0px 0px;
        border: 1px solid rgba(0, 0, 0, 0.06);
    }

    .baseInfo {
        padding: 24px 24px 0;
    }

    .card {
        position: relative;
        display: flex;
        padding: 24px;
        height:224px;
        background: #ffffff;
        box-shadow: 0px 0px 12px 0px rgba(230, 237, 241, 0.5);
        border-radius: 16px 16px 16px 16px;
        border: 1px solid rgba(0, 0, 0, 0.06);
        margin-bottom: 24px;

        .delete {
            width: 48px;
            height: 48px;
            background: #FEF0F0;
            border-radius: 0px 16px 0px 16px;
            position: absolute;
            bottom: 0;
            left: 0;
            z-index:1;
            line-height: 48px;
            text-align: center;
        }

        .avatar {
            position: relative;
            width:72px;
            height: 72px;
            border-radius: 72px;
            overflow: hidden;

            img {
                width: 100%;
                height: 100%;
            }

            .tip {
                position: absolute;
                text-align: center;
                left:0;
                bottom: 0;
                width: 72px;
                height: 20px;
                line-height: 20px;
                color: #FFFFFF;
                font-size: 10px;
                background: #409EFF;
            }
        }

        .client-man {
            flex:1;
            .el-form-item__content {
                width: calc(100% - 110px);
            }
        }

        .add-man {
            width: 100%;
            height: 100%;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }
        
    }

    .annex-wrap {
        padding: 24px;
        display: flex;
        flex-direction: column;
        align-items: center;

        
        ::v-deep .el-radio-button--mini .el-radio-button__inner {
            padding: 10px 15px;
            margin-bottom: 24px;
        }
    }
}

</style>

<template>
    <div class="app-container">
        <div class="header-title">
            <div>新增案件</div>
            <el-button @click="handleBack" size="small">返回</el-button>
        </div>

        <div class="detail-content">
            <div class="common-content">
                <div class="common-title">基本信息</div>
                <el-form :inline="true" label-position="top" class="baseInfo" label-width="110px">
                    <el-row :gutter="24">
                        <el-col :span="8">
                            <el-form-item label="案件编号/名称">
                                <el-input
                                    v-model="listQuery.title"
                                    size="small"
                                    placeholder="请输入案件编号/名称"
                                    :maxlength="50"
                                    clearable
                                />
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="案件类型">
                                <el-input
                                    v-model="listQuery.title"
                                    size="small"
                                    placeholder="请输入案件类型"
                                    :maxlength="50"
                                    clearable
                                />
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="业务类型">
                                <el-select
                                    v-model="listQuery.type1"
                                    placeholder="请选择业务类型"
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
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="费用（元）">
                                <el-input
                                    v-model="listQuery.title"
                                    size="small"
                                    placeholder="请输入"
                                    :maxlength="50"
                                    clearable
                                />
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="诉讼阶段">
                                <el-select
                                    v-model="listQuery.type1"
                                    placeholder="请选择诉讼阶段"
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
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="案件状态">
                                <el-select
                                    v-model="listQuery.type2"
                                    style="width: 100%"
                                    placeholder="请选择案件状态"
                                    clearable
                                    filterable
                                    size="small"
                                >
                                    <el-option label="未受理" :value="1"></el-option>
                                    <el-option label="已受理" :value="2"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="标的额">
                                <el-input
                                    v-model="listQuery.title"
                                    size="small"
                                    placeholder="请输入标的额"
                                    :maxlength="50"
                                    clearable
                                />
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="经纪人">
                                <el-input
                                    v-model="listQuery.title"
                                    size="small"
                                    placeholder="请输入经纪人"
                                    clearable
                                />
                            </el-form-item>
                        </el-col>
                        <el-col :span="24">
                            <el-form-item label="案情简介">
                                <el-input
                                    v-model="listQuery.title"
                                    size="small"
                                    type="textarea"
                                    placeholder="请输入案情简介"
                                    :maxlength="200"
                                    clearable
                                />
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
            <div class="common-content">
                <div class="common-title">当事人</div>
                <el-form :inline="true" label-position="right" class="baseInfo" label-width="110px">
                    <el-row :gutter="24">
                        <el-col :span="8" v-for="(item, index) in litigantList" :key="index">
                            <div class="card">
                                <div class="delete" @click="handleDelete(index)">
                                    <i class="el-icon-delete" style="color: #F56C6C;font-size: 20px;"></i>
                                </div>
                                <div class="avatar">
                                    <img src="@/assets/img/avatar.png" alt="" />
                                    <div class="tip">当事人{{ index + 1 }}</div>
                                </div>

                                <div class="client-man">
                                    <el-form-item label="姓名">
                                        <el-input
                                            v-model="listQuery.title"
                                            size="small"
                                            placeholder="请输入姓名"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                    <el-form-item label="性别">
                                        <el-radio-group v-model="listQuery.sex">
                                            <el-radio :label="1">男</el-radio>
                                            <el-radio :label="0">女</el-radio>
                                        </el-radio-group>
                                    </el-form-item>
                                    <el-form-item label="身份证号">
                                        <el-input
                                            v-model="listQuery.title"
                                            size="small"
                                            placeholder="请输入身份证号"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                    <el-form-item label="联系方式">
                                        <el-input
                                            v-model="listQuery.title"
                                            size="small"
                                            placeholder="请输入联系方式"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                </div>
                            </div>
                        </el-col>
                        <el-col :span="8">
                            <div class="card" @click="handleAdd">
                                <div class="add-man">
                                    <i class="el-icon-plus" style="font-size: 28px;font-weight:bold;color: rgba(0,0,0,0.25);margin-bottom:12px"></i>
                                    <div style="color: rgba(0,0,0,0.45);">新增当事人</div>
                                </div>
                                
                            </div>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
            <div class="common-content">
                <div class="common-title">附件管理</div>
                <div class="annex-wrap">
                    <el-radio-group
                        v-model="annexType"
                        @change="handleChangeTime"
                        size="mini"
                        style=""
                    >
                        <el-radio-button label="0">合同上传（0）</el-radio-button>
                        <el-radio-button label="1">附件上传（0）</el-radio-button>
                        <el-radio-button label="2">附件上传（0）</el-radio-button>
                    </el-radio-group>
                    <div style="width: 100%">
                        <handleRequired />
                    </div>
                </div>
            </div>
        </div>
        <div class="foot-wrap">
            <el-button @click="close" size="small">取消</el-button>
            <el-button type="primary" size="small" :disabled="disabled" :loading="disabled" @click="save('formInline')">提交</el-button>
        </div>
    </div>
</template>

<script>
import { syssystemPage, syssystemDelete } from '@/api/syssystem';
import handleRequired from './handleRequired.vue'

export default {
    components: {handleRequired},
    data() {
        return {
            query: {},
            listQuery: {},
            litigantList: [{}, {}],
            disabled: false,
            annexType: 0,
        };
    },
    created() {
        this.bindList(syssystemPage, '');
    },
    methods: {
        handleAdd() {
            this.litigantList.push({})
        },
        handleDelete(index) {
            this.litigantList.splice(index, 1)
        },
        handleBack() {
            this.$emit('back')
        },
        close() {},
        save() {},
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

.foot-wrap {
    width: 100%;
    height: 64px;
    background: #ffffff;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    padding: 24px;
    position: absolute;
    bottom: 0;
    left: 0;
    z-index: 10;
    box-shadow: inset 0px 0px 32px 0px rgba(198, 215, 225, 0.64);
}

.detail-content {
    margin: 60px 0;

    .el-form-item--small.el-form-item {
        width: 100%;
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

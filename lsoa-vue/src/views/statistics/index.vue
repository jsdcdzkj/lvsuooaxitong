<template>
    <div class="app-container">
        <div class="formBox" style="padding-bottom:0">
            <el-radio-group
                v-model="searchType"
                @change="handleChange"
                size="mini"
                style="width: 100%; margin-bottom: 24px"
            >
                <el-radio-button label="0">案件分类统计</el-radio-button>
                <el-radio-button label="1">律师案件统计</el-radio-button>
                <el-radio-button label="2">合同费用统计</el-radio-button>
            </el-radio-group>
            <el-form :inline="true" class="demo-form-inline topsearch" label-width="80px">
                <el-form-item label="统计时间">
                    <el-date-picker
                        key="year-month-range"
                        v-model="timeVal"
                        v-if="listQuery.flag == 'day'"
                        style="width: 100%"
                        type="monthrange"
                        value-format="yyyy-MM"
                        @change="changeRange"
                        range-separator="至"
                        start-placeholder="开始月份"
                        end-placeholder="结束月份">
                    </el-date-picker>
                    <el-date-picker
                        key="year-month"
                        v-model="listQuery.time"
                        v-if="listQuery.flag == 'month'"
                        style="width: 100%"
                        value-format="yyyy-MM"
                        type="month"
                        placeholder="选择月"
                    >
                    </el-date-picker>
                    <el-date-picker
                        key="year"
                        v-model="listQuery.time"
                        v-if="listQuery.flag == 'year'"
                        value-format="yyyy"
                        style="width: 100%"
                        type="year"
                        placeholder="选择年"
                    >
                    </el-date-picker>
                </el-form-item>
                <el-form-item>
                    <el-radio-group
                        v-model="listQuery.flag"
                        @change="handleChangeTime"
                        size="mini"
                    >
                        <el-radio-button label="day">自定义</el-radio-button>
                        <el-radio-button label="month">月</el-radio-button>
                        <el-radio-button label="year">年</el-radio-button>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="律师" v-if="searchType == 1" prop="queryUserId">
                    <el-select
                        v-model="queryUserId"
                        placeholder="请选择律师"
                        key="userId"
                        clearable
                        filterable
                        size="small"
                        @change="changeLawyer"
                    >
                        <el-option v-for="(item,index) in lawyerList" :key="index" :label="item.realName" :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="案件类型" v-if="searchType != 1">
                    <el-select
                            v-model="listQuery.caseType"
                            key="caseType"
                            placeholder="请选择案件类型"
                            clearable
                            filterable
                            size="small"
                        >
                            <el-option v-for="(item,index) in caseTypeList" :key="index" :label="item.dictLabel" :value="item.dictValue"></el-option>
                        </el-select>
                </el-form-item>
                <div style="float: right">
                    <el-button size="small" type="default" icon="el-icon-refresh" @click="refresh"
                        >重置</el-button
                    >
                    <el-button size="small" type="primary" icon="el-icon-search" @click="search"
                        >查询</el-button
                    >
                </div>
            </el-form>
        </div>
        <el-row :gutter="24">
            <el-col :span="16">
                <div class="common-content">
                    <div class="common-title">
                        <span v-if="searchType == 0">案件报表统计</span> 
                        <span v-if="searchType == 1">{{lawyerName}}律师负责的案件报表统计</span> 
                        <span v-if="searchType == 2">合同费用报表统计</span> 
                    </div>
                    <div class="common-data" :style="{'margin-left':searchType != 2 ? '-16px' : '0'}">
                        <barChart :valdata="caseStatisticData" height="100%"></barChart>
                    </div>
                </div>
            </el-col>
            <el-col :span="8">
                <div class="common-content">
                    <div class="common-title">
                        <span v-if="searchType == 0">案件类型数量排名</span> 
                        <span v-if="searchType == 1">{{lawyerName}}案件类型数量统计排名</span> 
                        <span v-if="searchType == 2">各类型案件合同费用排名</span> 
                    </div>
                    <div class="common-data">
                        <div v-for="(item, index) in caseList" :key="index" class="caseRank">
                            <div class="rank-name">
                                <div class="order" :class="{active: index== 0}">{{ index + 1 }}</div>
                                <div>{{ item.caseTypeName }}</div>
                            </div>
                            
                            <div v-if="searchType == 2">{{ (item.totalMoney && Number(item.totalMoney).toFixed(2)) || '--'}}</div>
                            <div v-else>{{ item.caseCount || '--' }}</div>
                        </div>
                    </div>
                </div>
            </el-col>
            <el-col :span="24">
                <div class="common-content" style="margin: 0">
                    <div class="common-title">
                        <div>
                            <span v-if="searchType == 0 && listQuery.flag == 'day'">{{ `${lawTime.startTime} ~ ${lawTime.endTime}律所各类型案件数量总计： ${total}` }}</span> 
                            <span v-if="searchType == 0 && listQuery.flag == 'month'"> {{`在${lawTime.time} ~ ${lawTime.time}律所各类型案件数量总计： ${total}`}}</span> 
                            <span v-if="searchType == 0 && listQuery.flag == 'year'">{{`${lawTime.time}-01 ~ ${lawTime.time}-12律所各类型案件数量总计： ${total}`}}</span>
                            <span v-if="searchType == 1 && listQuery.flag == 'day'">{{ `${lawyerName}${lawTime.startTime} ~ ${lawTime.endTime}时间内各类型案件数量统计： ${total}` }}</span> 
                            <span v-if="searchType == 1 && listQuery.flag == 'month'"> {{`${lawyerName}在${lawTime.time} ~ ${lawTime.time}时间内各类型案件数量统计： ${total}`}}</span> 
                            <span v-if="searchType == 1 && listQuery.flag == 'year'">{{`${lawyerName}${lawTime.time}-01 ~ ${lawTime.time}-12时间内各类型案件数量统计： ${total}`}}</span> 
                            <span v-if="searchType == 2 && listQuery.flag == 'day'">{{ `${lawTime.startTime} ~ ${lawTime.endTime}全部类型案件合同费用总计： ${total}` }}</span> 
                            <span v-if="searchType == 2 && listQuery.flag == 'month'"> {{`在${lawTime.time} ~ ${lawTime.time}全部类型案件合同费用总计： ${total}`}}</span> 
                            <span v-if="searchType == 2 && listQuery.flag == 'year'">{{`${lawTime.time}-01 ~ ${lawTime.time}-12全部类型案件合同费用总计： ${total}`}}</span> 
                        </div>
                        <el-button @click="handleExport" :disabled="listLoading" :loading="listLoading" size="small">导出报表</el-button>
                    </div>
                    <div>
                        <div class="table-wrap">
                            <el-table
                                v-loading="loading"
                                :data="list"
                                element-loading-text="Loading"
                                stripe
                                fit
                                :height="`calc(100vh - ${tableHeight}px)`"
                                class="table-container"
                                highlight-current-row
                            >
                                <!-- <el-table-column
                                    type="index"
                                    label="序号"
                                    width="80"
                                    align="center"
                                >
                                </el-table-column> -->
                                <el-table-column prop="name" v-if="searchType == 1" label="姓名" align="center">
                                    <template>
                                        <span>{{lawyerName}}</span>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="time" label="时间" align="center">
                                    <template slot-scope="scope">
                                        <span style="color: #409EFF">{{scope.row.time}}</span>
                                    </template>
                                </el-table-column>
                                <el-table-column v-if="searchType == 2" prop="totalMoney" label="小计" align="center">
                                    <template slot-scope="scope">
                                        <span>{{Number(scope.row.totalMoney || 0).toFixed(2)}}</span>
                                    </template>
                                </el-table-column>
                                <el-table-column v-else prop="totalCases" label="小计" align="center">
                                    <template slot-scope="scope">
                                        <span>{{scope.row.totalCases}}</span>
                                    </template>
                                </el-table-column>
                                <el-table-column v-for="item in caseTypeList" :key="item.id" :prop="item.dictValue" :label="item.dictLabel" align="center">
                                    <template slot-scope="scope">
                                        <span v-if="searchType == 2">{{listQuery.caseType ? listQuery.caseType == item.dictValue ? Number(scope.row[item.dictValue]).toFixed(2) : '--' : Number(scope.row[item.dictValue]).toFixed(2)}}</span>
                                        <span v-else>{{listQuery.caseType ? listQuery.caseType == item.dictValue ? scope.row[item.dictValue] : '--' : scope.row[item.dictValue]}}</span>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </div>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import Pagination from '@/components/Pagination';
import MiddleBody from '@/components/MiddleBody';
import barChart from '@/components/Charts/barchart.vue';
import {mapGetters} from 'vuex';
import {
    getNormalUserList,
    caseTypeMoneyRanking,
    caseTypeRanking,
    getStatisticalVoWithZero,
    getStatisticalVoWithZeroMoney,
    lawFirmCaseStatisticsByType,
    lawFirmCaseStatisticsByTypeMoney,
    exportData,
    exportMoney,
} from "@/api/statistics";
import { getRedisDictList } from '@/api/common'

export default {
    components: {
        MiddleBody,
        Pagination,
        barChart,
    },
    data() {
        return {
            tableHeight: 666,
            list: [],
            listQuery: {
                flag: 'year',
                time: `${new Date().getFullYear()}`
            },
            queryUserId: '',
            isDetail: false,
            searchType: 0,
            timeType: 0,
            loading: false,
            listLoading: false,
            caseTypeList: [],
            caseStatisticData: { },
            caseList: [],
            lawyerList: [],
            total: 0,
            lawyerName: '',
            tempLawyer: '',
            timeVal: [],
            lawTime: {
                startTime: '',
                endTime:'',
                time: '',
            },
        };
    },
    computed: {
        ...mapGetters(['roles', 'userInfo'])
    },
    created() {
        // 案件类型
        getRedisDictList({dictType: 'caseType'}).then(res => {
            this.caseTypeList = res.data
        })

        // 律师列表
        getNormalUserList({}).then(res => {
            this.lawyerList = res.data
        })

        this.search()
        console.log('999999role', this.roles, this.roles.some(res=> res.roleCode == 'ls'))
        if(this.roles.some(res=> res.roleCode == 'ls')) {
            this.userId = this.userInfo.id
        }
    },
    methods: {
        handleChange() {
            this.listQuery = {flag: 'year',time: `${new Date().getFullYear()}`}
            this.caseList = []
            this.caseStatisticData = {}
            this.total = 0
            this.list = []
            if(this.searchType == 1) {
                if(this.userId) {
                    this.queryUserId = this.userId
                    this.tempLawyer = this.userInfo.realName
                } else {
                    this.queryUserId = this.lawyerList[0].id
                    this.tempLawyer = this.lawyerList[0].realName
                }
            }
            this.search()
        },
        changeLawyer(val) {

            this.queryUserId = val
            if(val) {
                this.lawyerList.map(res => {
                    if(res.id == val) {
                        this.tempLawyer = res.realName
                    }
                })
            } else {
                this.tempLawyer = ''
            }
            
        },
        changeRange() {
            this.listQuery.startTime = this.timeVal[0]
            this.listQuery.endTime = this.timeVal[1]
        },
        // 改变时间类型
        handleChangeTime(val) {
            const date = new Date()
            const year = `${date.getFullYear()}`;
            const month = date.getMonth() - 8 > 0 ? date.getMonth() + 1 : '0' + (date.getMonth() + 1)
            if(val == 'year') {
                this.listQuery.time = year
                this.lawTime.time = year
            } else if(val == 'month') {
                this.listQuery.time = year + '-' + month
                this.lawTime.time =  year + '-' + month
            }else {
                this.timeVal = [year + '-' + month, year + '-' + month]
                this.listQuery.startTime = this.listQuery.endTime = year + '-' + month
                this.lawTime.startTime = this.lawTime.endTime = year + '-' + month
            }
        },
        refresh() {
            this.listQuery = {flag: 'year',time: `${new Date().getFullYear()}`}
            if(this.searchType == 1) {
                if(this.userId) {
                    this.queryUserId = this.userId
                    this.tempLawyer = this.userInfo.realName
                } else {
                    this.queryUserId = this.lawyerList[0].id
                    this.tempLawyer = this.lawyerList[0].realName
                }
            }else {
                this.queryUserId = ''
                this.tempLawyer = ''
            }
            this.search()
        },
        search() {
            if(this.searchType == 1) {
                if (!this.queryUserId) {
                    return this.$message.success('请选择律师');
                }
                this.listQuery.userId = this.queryUserId
            } else {
                this.listQuery.userId = ''
            }
            if(this.listQuery.flag == 'day') {
                this.listQuery.startTime = this.timeVal[0]
                this.listQuery.endTime = this.timeVal[1]
                this.lawTime.startTime = this.timeVal[0]
                this.lawTime.endTime = this.timeVal[1]
            } else {
                this.lawTime.time = this.listQuery.time
                this.timeVal = []
                this.listQuery.startTime = ''
                this.listQuery.endTime = ''
                this.lawTime.startTime = ''
                this.lawTime.endTime = ''
            }
            this.lawyerName = this.tempLawyer
            this.loading = true
            if (this.searchType == 2) {
                // 金额的调用这个接口
                caseTypeMoneyRanking(this.listQuery).then(res => {
                    this.caseList = res.data
                })
                getStatisticalVoWithZeroMoney(this.listQuery).then(res => {
                    this.caseStatisticData = res.data
                })
                lawFirmCaseStatisticsByTypeMoney(this.listQuery).then(res => {
                    this.total = res.data.total
                    this.list = res.data.cases
                    this.loading = false
                })
            } else {
                // 其他的调用这个接口
                caseTypeRanking(this.listQuery).then(res => {
                    this.caseList = res.data
                })
                getStatisticalVoWithZero(this.listQuery).then(res=> {
                    this.caseStatisticData = res.data
                })
                lawFirmCaseStatisticsByType(this.listQuery).then(res => {
                    this.total = res.data.total
                    this.list = res.data.cases
                    this.loading = false
                })
            }
        },
        goBack() {
            this.isDetail = false;
        },
        handleEdit() {
            this.isDetail = true;
        },
        handlerEdit(index, row) {
            const params = { ...row };
            delete params.id;
            createUpdateDialog(
                {
                    form: { ...params },
                    id: row.id,
                },
                {}
            ).then((res) => {
                this.fetchTableData();
                this.$message.success('修改成功');
            });
        },
        handleExport() {
            this.listLoading = true;
            if (this.searchType == 2) {
                exportMoney(this.listQuery)
                .then((res) => {
                    let fileName = "合同费用.xls";
                    let objectUrl = URL.createObjectURL(new Blob([res.data], { type: "application/vnd.ms-excel" }));
                    const link = document.createElement("a");
                    link.download = decodeURI(fileName);
                    link.href = objectUrl;
                    link.click();
                    this.listLoading = false;
                    this.$baseMessage("导出成功！", "success");
                })
                .finally(() => {
                    this.listLoading = false;
                });
            } else {
                exportData(this.listQuery)
                    .then((res) => {
                        let fileName = "案件数量.xls";
                        let objectUrl = URL.createObjectURL(new Blob([res.data], { type: "application/vnd.ms-excel" }));
                        const link = document.createElement("a");
                        link.download = decodeURI(fileName);
                        link.href = objectUrl;
                        link.click();
                        this.listLoading = false;
                        this.$baseMessage("导出成功！", "success");
                    })
                    .finally(() => {
                        this.listLoading = false;
                    });
            }
            
        },
    },
};
</script>

<style lang="scss" scoped>
.common-data {
    height: 246px;
    flex-direction: column;
}
.caseRank {
    height: 42px;
    background: #FFFFFF;
    box-shadow: 0px 0px 12px 0px rgba(230,237,241,0.5);
    border-radius: 16px 16px 16px 16px;
    border: 1px solid rgba(0,0,0,0.06);
    margin-top: 8px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 16px;

    .rank-name {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .order {
        width: 24px;
        height: 24px;
        text-align: center;
        line-height: 24px;
        margin-right: 12px;
        background: rgba(0,0,0,0.06);
        border-radius: 24px 24px 24px 24px;
        color: rgba(0,0,0,0.65);
    }

    .active {
        color: #fff;
        background: linear-gradient( 270deg, #198ED9 0%, #45B6FF 47%, #2386C5 100%), linear-gradient( 180deg, rgba(0,0,0,0) 54%, rgba(0,0,0,0.89) 100%);
    }
}
::v-deep {
    .table-wrap {
        border-radius: 0;
        .el-table th {
            background: rgba(0,0,0,0.06)!important;
        }
    }
}
</style>

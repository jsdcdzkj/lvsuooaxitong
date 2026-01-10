<template>
    <div class="app-container">
        <div class="top-body">
            <el-form
                label-width="90px"
                size="small"
                :model="queryParams"
                @submit.native.prevent
                ref="queryParams"
            >
                <div class="top-body-row">
                    <el-row :gutter="20">
                        <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
                            <el-col :xs="24" :sm="12" :md="8" :lg="5" :xl="5">
                                <!-- 业务子系统: -->
                                <el-form-item label="业务子系统:">
                                    <el-select
                                        v-model="queryParams.systemId"
                                        placeholder="请选择业务子系统"
                                        clearable
                                        style="width: 200px"
                                    >
                                        <el-option
                                            v-for="item in systemList"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :xs="24" :sm="12" :md="8" :lg="5" :xl="5">
                                <!-- 模块名称: -->
                                <!-- <el-form-item label="模块名称:">
                                    <el-select
                                        v-model="queryParams.role_id"
                                        placeholder="请选择模块名称"
                                        clearable
                                        style="width: 200px"
                                    >
                                        <el-option
                                            v-for="item in roleList"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id"
                                        ></el-option>
                                    </el-select>
                                </el-form-item> -->
                                <!-- 输入 -->
                                <el-form-item label="模块名称:">
                                    <el-input
                                        v-model="queryParams.modeName"
                                        @keyup.enter.native="handlerSearch(resertFormQrery)"
                                        placeholder="请输入模块名称"
                                        :maxlength="50"
                                        clearable
                                    />
                                </el-form-item>
                            </el-col>
                            <el-col :xs="24" :sm="12" :md="8" :lg="5" :xl="5">
                                <!-- 操作人: -->
                                <el-form-item label="操作人:">
                                    <el-input
                                        v-model="queryParams.operatorName"
                                        @keyup.enter.native="handlerSearch(resertFormQrery)"
                                        placeholder="请输入操作人"
                                        :maxlength="50"
                                        clearable
                                    />
                                </el-form-item>
                            </el-col>
                            <!-- 日志编号: -->
                            <!-- <el-col :xs="24" :sm="12" :md="8" :lg="5" :xl="5">
                                <el-form-item label="日志编号:">
                                    <el-input
                                        v-model="queryParams.user_name"
                                        placeholder="请输入日志编号"
                                        :maxlength="50"
                                        clearable
                                    />
                                </el-form-item>
                            </el-col> -->
                        </el-col>
                        <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
                            <el-col :xs="24" :sm="12" :md="8" :lg="5" :xl="5">
                                <!-- 请求方式: -->
                                <el-form-item label="请求方式:">
                                    <el-select
                                        v-model="queryParams.method"
                                        placeholder="请选择请求方式"
                                        clearable
                                        style="width: 200px"
                                    >
                                        <el-option
                                            v-for="item in methodList"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id"
                                        ></el-option>
                                    </el-select> </el-form-item
                            ></el-col>
                            <el-col :xs="24" :sm="12" :md="8" :lg="5" :xl="5">
                                <!-- 类型: -->
                                <!-- <el-form-item label="类型:">
                                    <el-select
                                        v-model="queryParams.role_id"
                                        placeholder="请选择类型"
                                        clearable
                                        style="width: 200px"
                                    >
                                        <el-option
                                            v-for="item in roleList"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id"
                                        ></el-option>
                                    </el-select>
                                </el-form-item> -->
                                <!-- 输入 -->
                                <el-form-item label="类型:">
                                    <el-input
                                        v-model="queryParams.type"
                                        @keyup.enter.native="handlerSearch(resertFormQrery)"
                                        placeholder="请输入类型"
                                        :maxlength="50"
                                        clearable
                                    />
                                </el-form-item>
                            </el-col>
                            <el-col :xs="24" :sm="12" :md="8" :lg="5" :xl="5">
                                <!-- 状态: -->
                                <el-form-item label="状态:">
                                    <el-select
                                        v-model="queryParams.status"
                                        placeholder="请选择状态"
                                        clearable
                                        style="width: 200px"
                                    >
                                        <el-option
                                            v-for="item in statusList"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :xs="24" :sm="12" :md="8" :lg="5" :xl="5">
                                <el-form-item label="操作时间:">
                                    <el-date-picker
                                        style="width: 100%"
                                        v-model="value1"
                                        type="daterange"
                                        value-format="yyyy-MM-dd"
                                        range-separator="至"
                                        start-placeholder="开始日期"
                                        end-placeholder="结束日期"
                                    >
                                    </el-date-picker>
                                </el-form-item>
                            </el-col>
                            <el-col :xs="24" :sm="12" :md="8" :lg="4" :xl="4"
                                ><el-button
                                    type="primary"
                                    icon="el-icon-search"
                                    size="small"
                                    @click="handlerSearch(resertFormQrery)"
                                    >查询</el-button
                                >
                                <el-button
                                    icon="el-icon-s-promotion"
                                    size="small"
                                    @click="handlerRefresh(resertFormQreryClear)"
                                    >重置</el-button
                                >
                            </el-col>
                        </el-col>
                    </el-row>
                </div>
            </el-form>
        </div>

        <middle-body title="日志列表">
            <el-table
                v-loading="table.loading"
                :data="table.records"
                element-loading-text="Loading"
                stripe
                border
                fit
                height="calc(100vh - 453px)"
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
                    prop="systemName"
                    label="业务子系统(平台)"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="modeName"
                    label="模块名称"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="type"
                    label="操作类型"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="method"
                    label="请求方式"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="operatorName"
                    label="操作人员"
                    align="center"
                ></el-table-column>
                <el-table-column
                    show-overflow-tooltip
                    prop="api"
                    label="操作地址"
                    align="center"
                ></el-table-column>
                <!-- status 状态 0：成功，1：失败 -->
                <el-table-column show-overflow-tooltip prop="status" label="状态" align="center">
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.status === 0" type="success" size="mini"
                            >成功</el-tag
                        >
                        <el-tag v-else type="danger" size="mini">失败</el-tag>
                    </template>
                </el-table-column>

                <el-table-column
                    show-overflow-tooltip
                    prop="operateTime"
                    label="操作日期"
                    align="center"
                ></el-table-column>
                <!-- 日志内容 字段内容很长，弹出框显示 -->
                <el-table-column
                    show-overflow-tooltip
                    prop="responseBody"
                    label="日志内容"
                    align="center"
                >
                    <template slot-scope="scope">
                        <el-button type="primary" size="mini" plain @click="handleView(scope.row)"
                            >查看</el-button
                        >
                        <!-- <el-popover placement="bottom" title="日志" width="560" trigger="hover">
                            <slot name="content">
                                <div class="log-con">{{ scope.row.responseBody }}</div>
                            </slot>
                            <el-button size="mini" slot="reference">查看</el-button>
                        </el-popover> -->
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
        </middle-body>
        <el-dialog
            :close-on-click-modal="true"
            title="查看日志"
            :visible.sync="dialogFormVisible"
            width="640px"
        >
            <div style="padding: 0 20px 0 0; min-height: 200px; max-height: 70vh; overflow-y: auto">
                {{ logInfo }}
            </div>
        </el-dialog>
    </div>
</template>

<script>
import Pagination from '@/components/Pagination';
import MiddleBody from '@/components/MiddleBody/index.vue';
import { logPage, sysSystemList } from '@/api/user';
const _temp = {
    id: '',
    content: '',
    title: '',
};
import pagination from '@/mixins/pagination';
export default {
    components: {
        MiddleBody,
        Pagination,
    },
    mixins: [pagination],
    data() {
        return {
            queryParams: {
                systemId: '',
                modeName: '',
                operatorName: '',
                type: '',
                status: '',
                method: '',
            },
            logInfo: '',
            dialogFormVisible: false,
            value1: '',
            systemList: [],
            methodList: [
                { id: 'GET', name: 'GET' },
                { id: 'POST', name: 'POST' },
            ],
            statusList: [
                { id: 0, name: '成功' },
                { id: 1, name: '失败' },
            ],
        };
    },
    created() {
        this.bindList(logPage, 'queryParams');
        this.getSystemList();
    },
    methods: {
        resertFormQrery(queryParams) {
            if (this.value1 == null) {
                this.value1 = '';
            }
            if (this.value1.length == 0) {
                return {
                    ...queryParams,
                    startTime: '',
                    endTime: '',
                };
            } else {
                return {
                    ...queryParams,
                    startTime: this.value1[0] + ' 00:00:00',
                    endTime: this.value1[1] + ' 23:59:59',
                };
            }
        },
        resertFormQreryClear(queryParams) {
            this.value1 = '';
            return {
                ...queryParams,
                start: '',
                end: '',
            };
        },
        async getSystemList() {
            const { data } = await sysSystemList();
            this.systemList = data;
        },
        handleView(row) {
            this.logInfo = row.responseBody;
            this.dialogFormVisible = true;
        },
    },
};
</script>
<style>
.log-con {
    max-height: 600px;
    overflow: auto;
}
</style>

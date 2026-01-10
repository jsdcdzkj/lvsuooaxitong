<template>
    <div class="app-container">
        <div class="top-body">
            <el-form label-width="90px" size="small " @submit.native.prevent>
                <div class="top-body-row">
                    <el-row :gutter="20">
                        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
                            <el-form-item label="菜单名称">
                                <el-input
                                    v-model="listQuery.title"
                                    placeholder="菜单名称"
                                    :maxlength="50"
                                    clearable
                                    @keyup.enter.native="handleSearch()"
                                />
                            </el-form-item>
                        </el-col>
                        <!-- <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
                            <el-form-item label="菜单编号">
                                <el-input
                                    v-model="listQuery.menuCode"
                                    placeholder="菜单编号"
                                    :maxlength="50"
                                    clearable
                                    @keyup.enter.native="handlerSearch()"
                                />
                            </el-form-item>
                        </el-col> -->
                        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
                            <el-form-item label="创建时间:">
                                <el-date-picker
                                    v-model="timeRange"
                                    style="width: 100%"
                                    type="daterange"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期"
                                    value-format="yyyy-MM-dd"
                                >
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
                            <el-button
                                type="primary"
                                icon="el-icon-search"
                                size="small"
                                @click="handleSearch()"
                                >查询</el-button
                            >
                            <el-button
                                icon="el-icon-refresh-left"
                                size="small"
                                @click="handleReset()"
                                >重置</el-button
                            >
                        </el-col>
                    </el-row>
                </div>
            </el-form>
        </div>

        <middle-body title="子系统管理" showRight buttonText="新增">
            <div class="tabs" slot="title">
                <div
                    :class="['tabs-item', tabSelect == index ? 'checked' : '']"
                    v-for="(item, index) in systemList"
                    :key="index"
                    @click="handlerClick(index)"
                >
                    {{ item.name }}
                </div>
            </div>
            <template slot="right">
                <!-- 新增 下发 两个按钮 -->
                <el-button icon="el-icon-plus" type="primary" size="small" @click="handeAdd()"
                    >新增</el-button
                >
                <el-button icon="el-icon-s-promotion" size="small" @click="handleIssued()"
                    >下发</el-button
                >
            </template>

            <el-table
                v-loading="table.loading"
                :data="table.records"
                element-loading-text="Loading"
                stripe
                border
                fit
                :height="`calc(100vh - ${tableHeight}px)`"
                class="table-container"
                :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
                highlight-current-row
                row-key="id"
                :default-expand-all="true"
            >
                <el-table-column prop="title" label="菜单名称" width="260"> </el-table-column>
                <el-table-column prop="menuCode" label="菜单编号" align="center"> </el-table-column>
                <el-table-column prop="menuType" label="菜单类型" align="center">
                    <template slot-scope="scope">
                        <span type="success" v-if="scope.row.menuType == 1"> 菜单 </span>
                        <span type="success" v-if="scope.row.menuType == 2"> 按钮 </span>
                    </template>
                </el-table-column>
                <el-table-column prop="icon" label="图标" align="center"> </el-table-column>
                <el-table-column prop="vueUrl" label="组件地址" align="center"> </el-table-column>
                <el-table-column prop="isShow" label="状态" align="center" width="100">
                    <template slot-scope="scope">
                        <el-tag type="success" v-if="scope.row.isShow == 1"> 显示 </el-tag>
                        <el-tag type="danger" v-else> 隐藏 </el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" align="center" width="180">
                </el-table-column>
                <el-table-column prop="sort" label="排序" align="center" width="80">
                </el-table-column>
                <el-table-column fixed="right" label="操作" align="center" width="180">
                    <template slot-scope="scope">
                        <el-button size="mini" plain type="primary" @click="handleEdit(scope.row)"
                            >编辑</el-button
                        >
                        <el-button size="mini" plain type="primary" @click="handeDel(scope.row)"
                            >删除</el-button
                        >
                        <!-- <el-link type="success" @click="(scope.row)">提交拟办审批</el-link> -->
                    </template>
                </el-table-column>
            </el-table>
            <!-- <pagination
                :total="table.total"
                :page.sync="page.pageIndex"
                :limit.sync="page.pageSize"
                @pagination="onPaginationChange"
                align="right"
                style="padding-bottom: 10px"
            /> -->
        </middle-body>
    </div>
</template>

<script>
import Pagination from '@/components/Pagination';
import MiddleBody from '@/components/MiddleBody';
import { menuDel, getSysMenuList } from '@/api/menu';
import { syssystemList, syssystemSyncSystem } from '@/api/syssystem';
import pagination from '@/mixins/pagination';
import { createUpdateMenuDialog } from './dialog';

export default {
    components: {
        MiddleBody,
        Pagination,
    },
    mixins: [pagination],
    data() {
        return {
            tabSelect: -1,
            listQuery: {
                title: '',
                start: '',
                end: '',
                systemId: '',
                // menuCode: '',
            },
            timeRange: '',
            tableHeight: 330,
            systemList: [],
        };
    },
    created() {
        syssystemList().then((res) => {
            this.systemList = res.data;
            const id = this.$route.query.id;
            if (id) {
                this.tabSelect = this.systemList.findIndex((item) => item.id == Number(id));
            } else {
                this.tabSelect = 0;
            }
            this.getSysMenuList();
        });
    },
    methods: {
        // 获取列表
        getSysMenuList() {
            if (this.timeRange.length > 0) {
                this.listQuery.start = this.timeRange[0] + ' 00:00:00';
                this.listQuery.end = this.timeRange[1] + ' 23:59:59';
            } else {
                this.listQuery.start = '';
                this.listQuery.end = '';
            }
            //;
            this.listQuery.systemId = this.systemList[this.tabSelect].id;
            getSysMenuList(Object.assign({}, { ...this.listQuery })).then((res) => {
                this.table.records = this.handleTree(res.data, 'id');
            });
        },
        // 搜索
        handleSearch() {
            this.page.pageIndex = 1;
            this.getSysMenuList();
        },
        // 重置
        handleReset() {
            this.timeRange = [];
            this.listQuery = {
                title: '',
                start: '',
                end: '',
                systemId: '',
            };
            this.handleSearch();
        },

        handlerClick(index) {
            this.tabSelect = index;
            this.handleSearch();
        },
        handeAdd() {
            const form = {
                // 父级id
                parentId: 0,
                // 名称
                title: '',
                // 路由名称
                routeName: '',
                // 路由地址
                routeUrl: '',
                // 页面地址
                vueUrl: '',
                // 重定向类型
                redirectType: '',
                // 鼠标
                icon: '',
                // 是否显示
                isShow: 1,
                // 排序
                sort: '',
                // 菜单类型
                menuType: 1,
                parentUUID: '',
                menuCode: '',
                // 所属子系统
                systemId: this.listQuery.systemId,
            };
            createUpdateMenuDialog(
                { systemList: this.systemList, form: { ...form } },
                { title: '添加菜单' }
            ).then((res) => {
                this.$message.success('添加成功');
                this.handleSearch();
            });
        },
        // 下发
        handleIssued() {
            this.$confirm('您是否确认下发', '提示', {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning',
            }).then(() => {
                syssystemSyncSystem().then((res) => {
                    this.$message({
                        type: 'success',
                        message: '下发成功!',
                    });
                });
            });
        },
        handleEdit(row) {
            // 父级id
            const {
                parentId,
                // 名称
                title,
                // 路由名称
                routeName,
                // 路由地址
                routeUrl,
                // 页面地址
                vueUrl,
                // 重定向类型
                redirectType,
                // 鼠标
                icon,
                // 排序
                sort,
                // 所属子系统
                systemId,
                parentUUID,
                menuCode,
                isShow,
            } = row;

            const form = {
                // systemId: this.listQuery.systemId,
                parentId,
                parentUUID,
                // 名称
                title,
                // 路由名称
                routeName,
                // 路由地址
                routeUrl,
                // 页面地址
                vueUrl,
                // 重定向类型
                redirectType,
                // 鼠标
                icon,
                // 排序
                sort,
                // 所属子系统
                systemId,
                menuCode,
                isShow: Number(isShow),
                menuType: Number(row.menuType),
            };
            delete form.id;

            createUpdateMenuDialog(
                { systemList: this.systemList, form: { ...form }, id: row.id },
                { title: '编辑菜单' }
            ).then((res) => {
                this.handleSearch();
                this.$message.success('编辑成功');
            });
        },
        handeDel(row) {
            this.$confirm('将删除该条信息, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }).then(() => {
                menuDel({ id: row.id }).then((res) => {
                    this.handleSearch();
                    this.$message({
                        type: 'success',
                        message: '删除成功!',
                    });
                });
            });
        },
        /**
         * 构造树型结构数据
         * @param {*} data 数据源
         * @param {*} id id字段 默认 'id'
         * @param {*} parentId 父节点字段 默认 'parentId'
         * @param {*} children 孩子节点字段 默认 'children'
         */
        handleTree(data, id, parentId, children) {
            let config = {
                id: id || 'id',
                parentId: parentId || 'parentId',
                childrenList: children || 'children',
            };

            var childrenListMap = {};
            var nodeIds = {};
            var tree = [];

            for (let d of data) {
                let parentId = d[config.parentId];
                if (childrenListMap[parentId] == null) {
                    childrenListMap[parentId] = [];
                }
                nodeIds[d[config.id]] = d;
                childrenListMap[parentId].push(d);
            }

            for (let d of data) {
                let parentId = d[config.parentId];
                if (nodeIds[parentId] == null) {
                    tree.push(d);
                }
            }

            for (let t of tree) {
                adaptToChildrenList(t);
            }

            function adaptToChildrenList(o) {
                if (childrenListMap[o[config.id]] !== null) {
                    o[config.childrenList] = childrenListMap[o[config.id]];
                }
                if (o[config.childrenList]) {
                    for (let c of o[config.childrenList]) {
                        adaptToChildrenList(c);
                    }
                }
            }
            return tree;
        },
    },
};
</script>

<style lang="scss" scoped>
.tabs {
    display: inline-flex;
    height: 40px;
    border-radius: 12px 12px 12px 12px;
    border: 1px solid rgba(0, 0, 0, 0.15);
    overflow: hidden;
    cursor: pointer;

    .tabs-item {
        height: 38px;
        padding: 0 20px;
        font-family: Alibaba PuHuiTi 3, Alibaba PuHuiTi 30;
        font-weight: normal;
        font-size: 14px;
        color: rgba(0, 0, 0, 0.65);
        display: flex;
        align-items: center;
        justify-content: center;
        text-indent: 0;
        &.checked {
            background: linear-gradient(180deg, #478ff9 0%, #236cd8 100%);
            color: #ffffff;
        }
        border-left: 1px solid rgba(0, 0, 0, 0.15);
        &:first-child {
            border: none;
        }
    }
}
</style>

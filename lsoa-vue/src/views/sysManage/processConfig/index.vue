<template>
    <div class="app-container">
        <div class="common-content" style="margin:0" v-if="!isDetail">
            <div class="common-title">流程管理</div>
            <div class="common-data" style="flex-direction: column; height: calc(100vh - 186px);overflow: auto">
                <div v-for="(item,index) in processList" :key="index" class="process">
                    <div class="item name">
                        <img src="@/assets/img/bill.png" style="width: 60px;height: 62px;" alt="">
                        <span>{{ item.name }}</span>
                    </div>
                    <div class="item range">
                        发起权限：<span class="sponsor-name">{{ item.sponsorName }}</span> <span class="edit" @click="handleModify(item)">修改</span>
                    </div>
                    <div class="item time">最后更新时间：{{ item.updateTime }}</div>
                    <div class="btn">
                        <el-button type="primary" @click="handleEdit(item)" plain>编辑</el-button>
                        <el-button v-if="item.isEnable == 'Y'" type="danger" plain  @click="handleControl(item.id, 'N')">停用</el-button>
                        <el-button v-if="item.isEnable == 'N'" type="success" plain  @click="handleControl(item.id, 'Y')">启用</el-button>
                    </div>
                </div>
            </div>
        </div>
        <div v-else>
            <div class="header-title">
                <div>流程编辑</div>
                <el-button @click="handleBack" size="small">返回</el-button>
            </div>
            <div class="common-data" style="flex-direction: column; height: calc(100vh - 236px);margin-top: 60px;">
                <el-radio-group v-model="tabIndex" @change="changeTab" style="position:relative;left:50%;transform:translateX(-50%);margin-bottom:30px">
                    <el-radio-button label="1">基本信息</el-radio-button>
                    <el-radio-button label="2">流程设计</el-radio-button>
                </el-radio-group>
                <div style="width: 600px;margin:0 auto">
                    <div label="基本信息" v-if="tabIndex == 1">
                        <el-form ref="formInline" :model="form" :rules="rules" label-position="top" label-width="80px">
                            <el-form-item label="流程名称" prop="name">
                                <el-input v-model="form.name" placeholder="请输入名称"></el-input>
                            </el-form-item>
                            <el-form-item label="类型" prop="sponsorType">
                                <el-select v-model="form.sponsorType" @change="handleType" placeholder="请选择类型">
                                    <el-option label="人员" :value="1"></el-option>
                                    <el-option label="角色" :value="2"></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="谁可发起" prop="sponsorValue">
                                <el-select v-model="form.sponsorValue" v-if="form.sponsorType == 1" multiple placeholder="请选择谁可发起">
                                    <el-option
                                        v-for="item in userList"
                                        :key="item.id"
                                        :label="item.realName"
                                        :value="`${item.id}`"
                                    >
                                    </el-option>
                                </el-select>
                                <el-select v-model="form.sponsorValue" v-if="form.sponsorType == 2" multiple placeholder="请选择谁可发起">
                                    <el-option
                                        v-for="item in rolesList"
                                        :key="item.id"
                                        :label="item.roleName"
                                        :value="`${item.id}`"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="流程说明" prop="description">
                                <el-input
                                    v-model="form.description"
                                    type="textarea"
                                    :rows="4"
                                    placeholder="请输入流程说明"
                                >
                                </el-input>
                            </el-form-item>
                        </el-form>
                    </div>
                    <div  v-if="tabIndex == 2">
                        <div v-for="(item, index) in processStep" :key="index" class="step-card">
                                <div class="step" @click="handleCard(item,index)">
                                    <div class="step-name" :style="{background: item.color}">
                                        <span>{{ item.name }}</span>
                                        <span v-if="index> 0 && index < processStep.length - 1"><i class="el-icon-close" @click.stop="delNode(item.id)"></i></span>
                                    </div>
                                    <div class="step-man">
                                        <span class="man-list">{{ item.sponsorName }}</span>
                                        <i class="el-icon-arrow-right" style="font-size: 16px;color: rgba(0,0,0,0.25);"></i>
                                    </div>
                                </div>
                                <div class="line-wrap">
                                    <i v-if="index < processStep.length - 1" class="el-icon-circle-plus" @click.stop="handleAdd(index)" style="color:#409EFF;font-size: 30px;background-color: #fff;"></i>
                                </div>
                        </div>
                        <div class="end">流程结束</div>
                    </div>
                </div>
               
               
            </div>
            <div class="foot-wrap" v-if="tabIndex == 1">
                <el-button @click="handleBack" size="small">取消</el-button>
                <el-button
                    type="primary"
                    size="small"
                    :disabled="disabled"
                    :loading="disabled"
                    @click="save('formInline')"
                    >保存</el-button
                >
            </div>
        </div>
    </div>
</template>

<script>
import { list, nodeList, nodeDel, save } from "@/api/processConfig";
import { roleList, userPageList } from '@/api/user';
import { createUpdateDialog, createVisibilityDialog } from './dialog';

export default {
    data() {
        return {
            tableHeight: 840,
            query: {},
            listQuery: {},
            isDetail: false,
            disabled: false,
            processList: [],
            processStep: [],
            colorList: [
                'linear-gradient( 180deg, #5EA4ED 0%, #337ECC 100%)',
                'linear-gradient( 180deg, #F5A250 0%, #F57150 100%)',
                'linear-gradient( 180deg, #40BFA8 0%, #30B9BC 100%)',
                'linear-gradient( 180deg, #FF8E8E 0%, #F56C6C 100%)',
                'linear-gradient( 180deg, #7FDB52 0%, #67C23A 100%)',
                'linear-gradient( 180deg, #EC8BC0 0%, #D96BA7 100%)',
            ],
            form: {},
            rules: {
                name: [{ required: true, message: '请输入', trigger: 'blur' }],
            },
            tabIndex: '1',
        };
    },
    created() {
        this.getList()
        roleList({}).then(res => {
            if(res.code == 0) {
                this.rolesList = res.data
            }
        })
        userPageList({pageIndex: 1,pageSize: 1000}).then(res => {
            if(res.code == 0) {
                this.userList = res.data.records
            }
        })
    },
    methods: {
        getList() {
            list({}).then(res => {
                if (res.code == 0) {
                    this.processList = res.data
                }
            })
        },
        getNodeList(id) {
            this.processStep = [{
                name: '发起人',
                sponsorName: this.form.sponsorName,
                color: this.colorList[0],
            }]
            nodeList({processId: id}).then(res => {
                if (res.code == 0) {
                    res.data.map(val => {
                        this.processStep.push({
                            name: val.nodeType ==1 ? '审批' : '抄送',
                            sponsorName: val.approverName,
                            sponsorType: val.approverType,
                            sponsorValue: val.approverValue?.split(','),
                            nodeType: `${val.nodeType}`,
                            id: val.id,
                            color: this.colorList[val.nodeType]
                        })
                    })
                }
            })
        },
        changeTab(val) {
            if(val == 2) {
                this.getNodeList(this.form.id)
                this.processStep = [{
                    name: '发起人',
                    sponsorName: this.form.sponsorName,
                    sponsorType: this.form.sponsorType,
                    sponsorvalue: this.form.sponsorValue,
                    color: this.colorList[0],
                }]
            }
        },
        handleType(val) {
            this.form.sponsorValue = ''
        },
        handleCard(item,index) {
            if (index == 0) return
            createUpdateDialog(
                { form: {...item, processId: this.form.id} },
                { title: '流程设计' }
            ).then((res) => {
                this.getNodeList(this.form.id)
                this.$message.success('编辑成功');
            });
        },
        handleAdd(index) {
            createUpdateDialog(
                { form: {processId: this.form.id, sort: index + 1} },
                { title: '流程设计' }
            ).then((res) => {
                this.getNodeList(this.form.id)
                this.$message.success('添加成功');
            });
        },
        handleDelete(index) {
            this.typeList.splice(index,1);
        },
        handleBack() {
            this.isDetail = false
        },
        handleEdit(row) {
            this.isDetail = true
            this.tabIndex = '1'
            this.form = row
            this.form.sponsorValue = this.form.sponsorValue?.split(','); 
        },
        // 停用或者启用
        handleControl(id,isEnable) {
            save({id,isEnable}).then(res => {
                console.log('22222222222', res)
                if(res.code == 0) {
                    this.getList()
                }
            })
        },
        // 修改
        handleModify(item) {
            createVisibilityDialog(
                { form: {id: item.id, sponsorType: `${item.sponsorType}`,sponsorValue: item.sponsorValue?.split(',')} },
                { title: '修改发起权限' }
            ).then((res) => {
                this.getList()
            });
        },
        save(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    save({
                        id: this.form.id,
                        name: this.form.name,
                        sponsorType: this.form.sponsorType,
                        sponsorValue: this.form.sponsorValue.join(','),
                        description:this.form.description
                    }).then(res => {
                        if(res.code == 0) {
                            this.isDetail = false
                            this.getList()
                        }
                    })
                } else {
                    return false
                }
            })
        },
        delNode(id) {
            console.log('2222222222222', id)
            nodeDel({id}).then(res => {
                if(res.code == 0) {
                    console.log('999999999res', res)
                    this.getNodeList(this.form.id)
                }
            })
        },
    },
};
</script>

<style lang="scss" scoped>
.sponsor-name {
    width: 80px;
    text-overflow: ellipsis;
    white-space: nowrap;
    display: inline-block;
    overflow: hidden;
}
.process {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 96px;
    padding: 0 24px;
    font-size: 16px;
    background: #FFFFFF;
    color: rgba(0,0,0,0.65);
    box-shadow: 0px 0px 12px 0px rgba(230,237,241,0.5);
    border-radius: 16px 16px 16px 16px;
    border: 1px solid rgba(0,0,0,0.06);
    margin-bottom: 16px;

    .item {
        flex:1;
        flex-grow: 1;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .name {
        display: flex;
        align-items: center;
    }

    .edit {
        color: #409EFF;
        text-decoration: underline;
        margin-left: 8px;
    }

    .range {
        display: flex;
    }

    .btn {
        width: 120px;
    }
}

.step-card {
    width: 308px;
    margin: 0 auto;
    .step {
        width: 308px;
        height: 120px;
        background: #FFFFFF;
        box-shadow: 0px 0px 12px 0px #E6EDF1;
        border-radius: 16px 16px 16px 16px;
        border: 1px solid rgba(0,0,0,0.06);
        overflow:hidden;

        .step-name {
            height: 48px;
            font-size: 16px;
            color: #FFFFFF;
            padding: 0 16px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .step-man {
            
            height: 72px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            color:rgba(0,0,0,0.85);
            padding: 0 16px;
        }

        .man-list {
            width: 90%;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
    }
}
.line-wrap {
    width: 26px;
    height: 72px;
    background:url('../../../assets/img/line.png') no-repeat;
    background: contain;
    margin: 16px auto;
    display: flex;
    justify-content: center;
    align-items: center;
}
.end {
    font-size: 16px;
    color: rgba(0,0,0,0.65);
    text-align: center;
}

// 编辑详情
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
</style>

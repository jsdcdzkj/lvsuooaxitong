<template>
    <div class="app-container">
        <div class="common-content">
            <div class="common-title">附件类型</div>
            <div class="common-data">
                <img class="setting-icon" src="@/assets/img/file-type.png" alt="" />
                <div class="annex-wrap">
                    <el-form
                        :inline="true"
                        label-position="top"
                        class="baseInfo"
                        label-width="110px"
                    >
                        <el-row>
                            <el-col :span="24">
                                <el-form-item label="当前附件类型" style="width: 100%">
                                    <div class="choose-type">
                                        <span class="item" @click="handleDelete(item)" v-for="(item,index) in typeList" :key="index">{{item}}</span>
                                    </div>
                                    <span class="tip">此处展示所有已经创建的附件类型名称，点击名称后的“X”即可删除该附件类型</span>
                                </el-form-item>
                                
                            </el-col>
                            <el-col :span="24">
                                <el-form-item label="添加附件类型" style="width: 100%">
                                    <div style="display: flex">
                                        <el-input
                                            v-model="listQuery.type"
                                            size="small"
                                            placeholder="请输入附件类型"
                                            :maxlength="50"
                                            clearable
                                        />
                                        <el-button style="margin-left: 8px;" @click="handleAdd">添加</el-button>
                                    </div>
                                    
                                    <span class="tip">输入新的附件类型名称后，点击添加“添加”按钮确认后即可完成添加操作</span>
                                </el-form-item>
                                
                            </el-col>
                        </el-row>
                    </el-form>
                </div>
            </div>
        </div>
        <div class="common-content">
            <div class="common-title">
                <div>地址设置</div>
                <el-button
                        type="primary"
                        size="small"
                        plain
                        :disabled="disabled"
                        :loading="disabled"
                        @click="save(1)"
                        >保存</el-button>
            </div>
            <div class="common-data">
                <img class="setting-icon" src="@/assets/img/address.png" alt="">
                <div class="annex-wrap">
                    <el-form
                        :inline="true"
                        label-position="top"
                        class="baseInfo"
                        label-width="110px"
                    >
                        <el-row>
                            <el-col :span="24">
                                <el-form-item label="附件上传目录" style="width: 100%;">
                                    <el-input
                                        v-model="listQuery.diskPath.dictValue"
                                        size="small"
                                        placeholder="请输入附件上传目录"
                                        :maxlength="50"
                                        clearable
                                    />
                                    <span class="tip">此目录必须有读写权限，绝对路径请以“/”开头</span>
                                </el-form-item>
                            </el-col>
                            <el-col :span="24">
                                <el-form-item label="附件URL地址" style="width: 100%;">
                                    <el-input
                                        v-model="listQuery.domain.dictValue"
                                        size="small"
                                        placeholder="请输入附件URL地址"
                                        :maxlength="50"
                                        clearable
                                    />
                                    <span class="tip">可以为附件目录指定域名，用于分离附件，留空标示默认本站地址(站外保存时必须指定域名)</span>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-form>
                </div>
            </div>
        </div>
        <div class="common-content">
            <div class="common-title">
                <div>空间配置</div>
                <el-button
                    type="primary"
                    size="small"
                    plain
                    :disabled="disabled"
                    :loading="disabled"
                    @click="save(2)"
                >保存</el-button>
            </div>
            <div class="common-data">
                <img class="setting-icon" src="@/assets/img/setting.png" alt="">
                <div class="annex-wrap">
                    <el-form
                        :inline="true"
                        label-position="top"
                        class="baseInfo"
                        label-width="110px"
                    >
                        <el-row>
                            <el-col :span="24">
                                <el-form-item label="允许上传大小" style="width: 100%;">
                                    <el-input
                                        v-model="listQuery.maxSize.dictValue"
                                        size="small"
                                        placeholder="请输入允许上传大小"
                                        :maxlength="50"
                                        clearable
                                    />
                                    <span class="tip">单个附件存储时上传文件不能大于10M</span>
                                </el-form-item>
                            </el-col>
                            <el-col :span="24">
                                <el-form-item label="上传格式设置" style="width: 100%;">
                                    <el-select v-model="fileConfig" clearable multiple placeholder="请选择上传格式">
                                        <el-option
                                        v-for="item in options"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value">
                                        </el-option>
                                    </el-select>
                                    <span class="tip">点击选择所需要限制的格式类型，可多选</span>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-form>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
// import { getList,add,edit,remove } from "@/api/manyidu";
// import { getRenList } from "@/api/common";
import { list, save, fileType, add, del } from '@/api/attachment';

export default {
    data() {
        return {
            tableHeight: 840,
            query: {},
            listQuery: {},
            options: [
                {label: '.jpg', value:'.jpg'},
                {label: '.png', value:'.png'},
                {label: '.pdf', value:'.pdf'},
                {label: '.docx', value:'.docx'},
                {label: '.xlsx', value:'.xlsx'},
                {label: '.txt', value:'.txt'},
                {label: '.doc', value:'.doc'},
            ],
            typeList: [],
            fileConfig:[],
        };
    },
    created() {
        this.getList()
        this.getFileType()
    },
    methods: {
        getFileType() {
            // 附件类型
            fileType({}).then(res => {
                this.typeList = res.data
            })
        },
        getList() {
            // 地址和空间配置
            list({}).then(res => {
                const { domain, diskPath, fileTypeConfig, maxSize} = res.data
                this.listQuery={
                    domain:{id: domain.id, dictValue: domain.dictValue},
                    diskPath: {id: diskPath.id, dictValue: diskPath.dictValue},
                    fileTypeConfig: {id: fileTypeConfig.id, dictValue: fileTypeConfig.dictValue},
                    maxSize: {id: maxSize.id, dictValue: maxSize.dictValue},
                }
                localStorage.setItem('fileInfo',JSON.stringify({type: res.data.fileTypeConfig.dictValue, size:res.data.maxSize.dictValue}))
                this.fileConfig = fileTypeConfig.dictValue.split(',')

            })
        },
        handleAdd() {
            if (this.listQuery.type) {
                add(this.listQuery.type).then(res => {
                    if(res.code == 0) {
                        this.getFileType()
                        this.listQuery.type = ''
                    }
                })
            } else {
                this.$message({
                    type: 'error',
                    message: '请输入要添加的附件类型!'
                });
            }
        },
        handleDelete(value) {
            this.$confirm('确定删除该附件类型吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }).then(() => {
                del(value).then((res) => {
                    this.getFileType()
                    this.$message({
                        type: 'success',
                        message: '删除成功!',
                    });
                });
            });
        },
        save(type) {
            let temp = [this.listQuery.domain,this.listQuery.diskPath]

            if(type == 2) {
                temp = [{...this.listQuery.fileTypeConfig, dictValue:this.fileConfig.join(',')},this.listQuery.maxSize]
            }
            save(temp).then(res=> {
                if(res.code == 0) {
                    localStorage.setItem('fileInfo',JSON.stringify({type: this.fileConfig.join(','), size:this.listQuery.maxSize}))
                    this.$message({
                        type: 'success',
                        message: '保存成功!',
                    });
                    this.getList()
                }
            })
        },
    },
};
</script>

<style lang="scss" scoped>
.common-data {
    padding: 24px 24px 0;
}
.choose-type {
    display: flex;
    align-items: center;
    height: 32px;
    background: #FFFFFF;
    border-radius: 8px 8px 8px 8px;
    border: 1px solid rgba(0,0,0,0.15);

    .item {
        height: 24px;
        line-height: 24px;
        color: rgba(0,0,0,0.85);
        background: #F8F8F8;
        border-radius: 4px 4px 4px 4px;
        border: 1px solid rgba(0,0,0,0.06);
        position: relative;
        padding: 0 24px 0 8px;
        margin-left: 4px;

        &::before {
            content: '×';
            position: absolute;
            width: 16px;
            height: 16px;
            right: 0px;
            top: -2px;
            font-size: 16px;
            color: rgba(0,0,0,0.25);
        }
    }
}

.annex-wrap {
    flex:1;
}
.setting-icon {
    width: 88px;
    height: 88px;
    margin: 0 32px 0 8px;
}

.tip {
    font-size: 12px;
    color: rgba(0,0,0,0.65);
}
</style>

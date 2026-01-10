<template>
    <el-row>
        <el-col :span="24">
            <el-form label-position="top" :model="form">
                <el-form-item prop="fileList" v-if="(!detail && id != -1) && ((limit && form.fileList.length === 0) || !limit)">
                    <div style="display: flex">
                        <el-upload
                            class="upload-demo"
                            ref="upload"
                            :on-change="fileChange"
                            action="#"
                            multiple
                            :file-list="form.fileList"
                            :show-file-list="false"
                            :auto-upload="false"
                        >
                            <el-button slot="trigger" size="small" icon="el-icon-upload2"
                                >上传文件</el-button
                            >
                        </el-upload>
                        <div
                            class="el-upload__tip"
                            style="margin-left: 10px; color: rgba(0, 0, 0, 0.45)"
                        >
                            支持扩展名：{{ type }}
                        </div>
                    </div>
                </el-form-item>
                <div class="file-wrap">
                    <div v-for="(item, index) in form.fileList" :key="index" class="file-list">
                        <div @click="viewPdf(row.url)" class="file-name">
                            <img src="@/assets/img/file-icon.png" alt="" />
                            <span>{{ item.name }}</span>
                        </div>
                        <div>
                            <el-button
                                size="mini"
                                type="primary"
                                plain
                                @click="preview(item)"
                                >预览</el-button
                            >
                            <el-button
                                size="mini"
                                type="success"
                                plain
                                :disabled="listLoading"
                                :loading="listLoading"
                                @click="downFile(item)"
                                >下载</el-button
                            >
                            <el-button
                                size="mini"
                                type="danger"
                                @click="delVersion(item,index)"
                                plain
                                v-if="!detail"
                                >删除</el-button
                            >
                        </div>
                    </div>
                </div>
            </el-form>
        </el-col>
        <WordPreview :show.sync="showWord" :list="realFileList"></WordPreview>
    </el-row>
</template>

<script lang="js">
import { upload, fileDelete, download } from "@/api/common";

import WordPreview from "@/components/WordPreview";
export default {
    components: { WordPreview },
    props: {
        id: {
            type: String,
            default: '',
        },
        fileList: {
            type: Array,
            default: () => [],
        },
        detail: {
            type: Boolean,
            default: false
        },
        edit: {
            type: Boolean,
            default: false
        },
        detailId: {
            type: String,
            default: ''
        },
        limit: {
            type: Boolean,
            default: false
        }
    },
    data() {
        return {
            loading: false,
            form: {
                fileList: [],
                proposedRequest: '',
            },
            oldIds: [],
            addIds: [],
            delIds: [],
            showWord: false,
            currentIndex: 0,
            size: 0,
            type: '',
            realFileList:[],
            listLoading: false,
        };
    },
    watch: {
        fileList: {
            handler(val) {
                this.form.fileList = val.map((item) => {
                    return {
                        size: item.fileSize,
                        name: item.originalFilename,
                        url: item.fileUrl,
                        originalFilename: item.originalFilename,
                        fileUrl: item.fileUrl,
                        id: item.id,
                    };
                });
            },
            deep: true
        }
    },
    created() {
        console.log('1111111111111文件', this.fileList, this.form.fileList)
        this.form.fileList = this.fileList.map((item) => {
                    return {
                        size: item.fileSize,
                        name: item.originalFilename,
                        originalFilename: item.originalFilename,
                        fileUrl: item.fileUrl,
                        url: item.fileUrl,
                        id: item.id,
                    };
                });
        this.loading = this.id != '';
        const info = JSON.parse(localStorage.getItem('fileInfo'))
        this.type = info.type
        this.size = info.size
    },
    methods: {
        downFile(item) {
            this.listLoading = true;
            download({id: item.id}).then(res => {
                let fileName = item.name;
                let objectUrl = URL.createObjectURL(new Blob([res.data]));
                const link = document.createElement("a");
                link.download = decodeURI(fileName);
                link.href = objectUrl;
                link.click();
                this.$baseMessage("下载成功！", "success");
            }).finally(() => {
                this.listLoading = false;
            });
        },
        async fileChange(file, fileList) {
            console.log('9999999999999fileList', file, fileList)
            if(fileList.some(val => val.uid == file.uid)) {
                const formData = new FormData();
                formData.append('file', file.raw);
                formData.append('bizType', this.id);
                if(this.detailId) formData.append('bizId', this.detailId);
                const res = await upload(formData);
                this.form.fileList.push({
                    size: res.data.fileSize,
                    name: res.data.originalFilename,
                    url: res.data.fileUrl,
                    originalFilename: res.data.originalFilename,
                    fileUrl: res.data.fileUrl,
                    id: res.data.id,
                })
                this.$emit('add', res.data.id);
                this.$emit('changeFile', this.id, this.form.fileList)
                this.$message.success('操作成功');
            }
        },
        handleBeforeUpload(file) {
            const isLt2M = file.size / 1024 / 1024 < this.size;
            if (!isLt2M) {
                this.$message.warning(`上传文件大小不能超过 ${this.size}MB`);
                return false;
            }
        },
        //删除
        delVersion(row, index) {
            this.$confirm('此操作将删除该文件, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            })
                .then(() => {
                    const del = this.form.fileList.splice(index, 1);
                    if(!this.edit) {
                        fileDelete({id: del[0].id})
                    }
                    this.$emit('del', del[0].id);
                    this.$emit('changeFile', this.id, this.form.fileList)
                })
                .catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除',
                    });
                });
        },
        preview(item) {
            console.log('222222222222', item)
            this.realFileList = [
                    {
                        file_name: item.name,
                        file_path: item.url,
                        id: item.id
                    }
                ]
                console.log('111111111222', this.realFileList)
                this.showWord = true;
        }
    },
};
</script>
<style lang="scss" scoped>
.file-wrap {
    .file-list {
        padding: 24px;
        border: 1px solid rgba(0,0,0,0.06);
        border-radius: 16px;
        display:flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 24px;

        .file-name {
            display: flex;
            align-items: center;
            font-size: 16px;
            color: rgba(0,0,0,0.65);

            img {
                width: 36px;
                height: 36px;
                margin-right: 8px;
            }
        }
    }
}


</style>

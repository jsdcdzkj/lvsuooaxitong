<template>
    <el-row>
        <el-col :span="24">
            <el-form label-position="top" :model="form" :rules="rules">
                <el-form-item prop="fileList" v-if="!isView">
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
                            支持扩展名：doc、docx、pdf、jpg
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
                                >预览</el-button
                            >
                            <el-button
                                size="mini"
                                type="success"
                                plain
                                >下载</el-button
                            >
                            <el-button
                                size="mini"
                                type="danger"
                                plain
                                >删除</el-button
                            >
                        </div>
                    </div>
                </div>
                <!-- <el-table
                    ref="list"
                    :data="form.fileList"
                    height="calc(100vh - 740px)"
                    fit
                    border
                    v-loading="loading"
                >
                    <el-table-column
                        show-overflow-tooltip
                        prop="name"
                        label="文件名"
                        align="center"
                        min-width="220"
                    >
                        <template slot-scope="{ row }">
                            <el-link type="primary" @click="viewPdf(row.url)">{{
                                row.name
                            }}</el-link>
                        </template>
                    </el-table-column>
                    <el-table-column
                        show-overflow-tooltip
                        prop="size"
                        label="大小"
                        align="center"
                        min-width="140"
                    >
                        <template slot-scope="{ row }">
                            <span>{{ (row.size / 1024).toFixed(0) }}kb</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                        show-overflow-tooltip
                        label="操作"
                        width="80"
                        align="center"
                        v-if="!isView"
                    >
                        <template slot-scope="scope">
                            <el-button
                                type="info"
                                size="mini"
                                @click="delVersion(scope.row, scope.$index)"
                                >删除</el-button
                            >
                        </template>
                    </el-table-column>
                </el-table> -->
            </el-form>
        </el-col>
    </el-row>
</template>

<script lang="js">
export default {
    data() {
        return {
            loading: false,
            form: {
                fileList: [],
                proposedRequest: '',
            },
            id: '',
            isView: false,
        };
    },
    created() {
        this.loading = this.id != '';
        // caseDetail({ id: this.id }).then((res) => {
        //     // console.log(res);
        //     this.form.fileList = res.data.replenishmentFileList.map((item) => {
        //         return {
        //             size: item.fileSize,
        //             name: item.fileName,
        //             url: item.fileUrl,
        //         };
        //     });
        //     this.loading = false;
        // });
    },
    methods: {
        viewPdf(url) {
            window.open(process.env.VUE_APP_FILE_URL + url, '_blank');
        },
        fileChange(file, fileList) {
            let fileSize = 0;
            fileList.map((item) => {
                fileSize = fileSize + Number(item.size);
            });
            const isLt2M = fileSize / 1024 / 1024 < 50;
            // console.log('上传文件大小不能超过 20MB');
            if (!isLt2M) {
                this.$message.warning('上传文件总体大小不能超过 50MB');
                fileList.splice(fileList.length - 1, 1);
                return false;
            } else {
                this.form.fileList = fileList;
            }
        },
        handleBeforeUpload(file) {
            const isLt2M = file.size / 1024 / 1024 < 20;
            if (!isLt2M) {
                this.$message.warning('上传文件大小不能超过 20MB');
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
                    this.form.fileList.splice(index, 1);
                    if (row.id) {
                        this.delIds.push(row.id);
                    }
                    // console.log(this.delIds);
                    this.$message({
                        type: 'success',
                        message: '删除成功!',
                    });
                })
                .catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除',
                    });
                });
        },
        async handleDone() {
            try {
                const formData = new FormData();
                for (let i = 0; i < this.form.fileList.length; i++) {
                    const element = this.form.fileList[i];

                    formData.append('file', element.raw);
                }
                formData.append('caseId', this.id);
                if (this.form.proposedRequest) {
                    formData.append('proposedRequest', this.form.proposedRequest);
                }
                await updHandlingRequest(formData);
                this.$emit('submit', this.form);
                this.$message.success('操作成功');
                return true;
            } catch (e) {
                console.log(e);

                return false;
            }
        },
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

<template>
    <DialogContainer>
        <div class="file-wrap">
            <div v-for="(item, index) in fileList" :key="index" class="file-list">
                <div @click="viewPdf(row.url)" class="file-name">
                    <img src="@/assets/img/file-icon.png" alt="" />
                    <span>{{ item.name }}</span>
                </div>
                <div>
                    <!-- <el-button
                        size="mini"
                        type="primary"
                        plain
                        @click="preview(item)"
                        >预览</el-button
                    > -->
                    <el-button
                        size="mini"
                        type="success"
                        plain
                        @click="download(item.url)"
                        >下载</el-button
                    >
                </div>
            </div>
        </div>
    </DialogContainer>
</template>
<script lang="js">
import DialogContainer from '@/components/DialogFactory/dialog-container.vue';

export default {
    components: { DialogContainer },
    data() {
        return {
            fileList: []
        };
    },
    methods: {
        download(url) {
            window.open(url, '_blank');
        },
    },
};
</script>
<style scoped lang="scss">
.file-wrap {
    .file-list {
        padding: 16px;
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
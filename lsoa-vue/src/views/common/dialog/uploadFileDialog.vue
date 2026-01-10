<template>
    <DialogContainer :cancel="handleCancel">
        <handleRequired :id="info.id" :detailId="`${info.invoiceId}`" limit :fileList="info.fileList" @changeFile="handleFiles"></handleRequired>
    </DialogContainer>
</template>
<script lang="js">
import DialogContainer from '@/components/DialogFactory/dialog-container.vue';
import handleRequired from '@/views/common/handleRequired.vue';
import { fileDelete } from "@/api/common"

export default {
    components: { DialogContainer, handleRequired },
    data() {
        return {
            info: {id: 4}
        };
    },
    methods: {
        handleFiles(val) {
            console.log('1111111111', val)
        },
        handleCancel() {
            return new Promise((resolve, reject) => {
                this.$emit('submit',1)
                resolve(true)
            })
        }
    },
};
</script>
<style scoped lang="scss">

</style>
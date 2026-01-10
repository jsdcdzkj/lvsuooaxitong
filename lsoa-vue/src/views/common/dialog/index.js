import { dialogFactoryInstance } from '@/components/DialogFactory/factory';
import CreateCheckDialog from './createCheckDialog.vue';
import CreateCheckLetterDialog from './createCheckLetterDialog.vue';
import CreateViewLetterNumDialog from './createViewLetterNumDialog.vue';
import CreateViewInvoiceNumDialog from './createViewInvoiceNumDialog.vue';
import CreateCustomerInvoiceNumDialog from './createCustomerInvoiceNumDialog.vue';
import DownloadListDialog from './downloadListDialog.vue';
import UploadFileDialog from './uploadFileDialog.vue';

export const createCheckDialog = (props, config = {}) =>
    dialogFactoryInstance.createDialogAsync(CreateCheckDialog, props, config);

export const createLetterCheckDialog = (props, config = {}) =>
    dialogFactoryInstance.createDialogAsync(CreateCheckLetterDialog, props, config);

export const createViewLetterNumDialog = (props, config = {}) =>
    dialogFactoryInstance.createDialogAsync(CreateViewLetterNumDialog, props, config);

export const createViewInvoiceNumDialog = (props, config = {}) =>
    dialogFactoryInstance.createDialogAsync(CreateViewInvoiceNumDialog, props, config);

// 顾问单位的开票列表
export const createCustomerInvoiceNumDialog = (props, config = {}) =>
    dialogFactoryInstance.createDialogAsync(CreateCustomerInvoiceNumDialog, props, config);

export const downloadListDialog = (props, config = {}) =>
    dialogFactoryInstance.createDialogAsync(DownloadListDialog, props, config);

export const uploadFileDialog = (props, config = {}) =>
    dialogFactoryInstance.createDialogAsync(UploadFileDialog, props, config);

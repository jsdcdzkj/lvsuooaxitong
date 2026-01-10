import { dialogFactoryInstance } from '@/components/DialogFactory/factory';
import CreateUpdateDialog from './CreateUpdateDialog.vue';

export const createUpdateDialog = (props, config = {}) =>
    dialogFactoryInstance.createDialogAsync(CreateUpdateDialog, props, config);

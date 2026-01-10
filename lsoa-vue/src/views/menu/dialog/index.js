import { dialogFactoryInstance } from '@/components/DialogFactory/factory';
import CreateUpdateMenuDialog from './CreateUpdateMenuDialog.vue';

export const createUpdateMenuDialog = (props, config = {}) =>
    dialogFactoryInstance.createDialogAsync(CreateUpdateMenuDialog, props, config);

import { dialogFactoryInstance } from '@/components/DialogFactory/factory';
import CreateUpdateDialog from './CreateUpdateDialog.vue';
import CreateVisibilityDialog from './CreateVisibilityDialog.vue';

export const createUpdateDialog = (props, config = {}) =>
    dialogFactoryInstance.createDialogAsync(CreateUpdateDialog, props, config);

export const createVisibilityDialog = (props, config = {}) =>
    dialogFactoryInstance.createDialogAsync(CreateVisibilityDialog, props, config);

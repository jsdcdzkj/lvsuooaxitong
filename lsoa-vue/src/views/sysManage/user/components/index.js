import { dialogFactoryInstance } from '@/components/DialogFactory/factory';

import AddOrUpdate from './addOrUpdate';
import editPassword from './editPassword.vue';
export const CreateAddOrUpdate = (props, config) =>
    dialogFactoryInstance.createDialog(AddOrUpdate, props, config);
export const CreateEditPassword = (props, config) =>
    dialogFactoryInstance.createDialog(editPassword, props, config);

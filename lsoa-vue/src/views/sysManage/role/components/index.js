import { dialogFactoryInstance } from '@/components/DialogFactory/factory';

import AddOrUpdate from './addOrUpdate';

import permissionUpdate from './permissionUpdate.vue';

export const CreateAddOrUpdate = (props, config) =>
    dialogFactoryInstance.createDialog(AddOrUpdate, props, config);

export const CreatePermissionUpdate = (props, config) =>
    dialogFactoryInstance.createDialog(permissionUpdate, props, config);

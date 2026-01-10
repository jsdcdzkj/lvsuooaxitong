import { dialogFactoryInstance } from '@/components/DialogFactory/factory';

import AddOrUpdate from './addOrUpdate';
import addChild from './addChild.vue';

export const CreateAddOrUpdate = (props, config) =>
    dialogFactoryInstance.createDialog(AddOrUpdate, props, config);

export const CreateAddChild = (props, config) =>
    dialogFactoryInstance.createDialog(addChild, props, config);

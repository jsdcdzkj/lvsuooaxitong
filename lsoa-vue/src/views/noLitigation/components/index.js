import { dialogFactoryInstance } from '@/components/DialogFactory/factory';

import AddOrUpdate from './addOrUpdate';

export const CreateAddOrUpdate = (props, config) =>
    dialogFactoryInstance.createDialog(AddOrUpdate, props, config);


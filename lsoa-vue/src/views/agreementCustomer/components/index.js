import { dialogFactoryInstance } from '@/components/DialogFactory/factory';

import AddOrUpdate from './addOrUpdate';
import AddCompany from './addRelatedCompany';
import AddInvoice from './addInvoice';

export const CreateAddOrUpdate = (props, config) =>
    dialogFactoryInstance.createDialog(AddOrUpdate, props, config);

export const CreateAddCompany = (props, config) =>
    dialogFactoryInstance.createDialog(AddCompany, props, config);

export const CreateAddInvoice = (props, config) =>
    dialogFactoryInstance.createDialog(AddInvoice, props, config);


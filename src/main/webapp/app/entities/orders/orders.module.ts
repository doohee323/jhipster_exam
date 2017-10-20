import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSharedModule } from '../../shared';
import { JhipsterAdminModule } from '../../admin/admin.module';
import {
    OrdersService,
    OrdersPopupService,
    OrdersComponent,
    OrdersDetailComponent,
    OrdersDialogComponent,
    OrdersPopupComponent,
    OrdersDeletePopupComponent,
    OrdersDeleteDialogComponent,
    ordersRoute,
    ordersPopupRoute,
} from './';

const ENTITY_STATES = [
    ...ordersRoute,
    ...ordersPopupRoute,
];

@NgModule({
    imports: [
        JhipsterSharedModule,
        JhipsterAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        OrdersComponent,
        OrdersDetailComponent,
        OrdersDialogComponent,
        OrdersDeleteDialogComponent,
        OrdersPopupComponent,
        OrdersDeletePopupComponent,
    ],
    entryComponents: [
        OrdersComponent,
        OrdersDialogComponent,
        OrdersPopupComponent,
        OrdersDeleteDialogComponent,
        OrdersDeletePopupComponent,
    ],
    providers: [
        OrdersService,
        OrdersPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterOrdersModule {}

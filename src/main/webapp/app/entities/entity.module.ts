import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { JhipsterCustomerModule } from './customer/customer.module';
import { JhipsterOrdersModule } from './orders/orders.module';
import { JhipsterOrderItemsModule } from './order-items/order-items.module';
import { JhipsterProductModule } from './product/product.module';
import { JhipsterCartItemsModule } from './cart-items/cart-items.module';
import { JhipsterTagModule } from './tag/tag.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        JhipsterCustomerModule,
        JhipsterOrdersModule,
        JhipsterOrderItemsModule,
        JhipsterProductModule,
        JhipsterCartItemsModule,
        JhipsterTagModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterEntityModule {}

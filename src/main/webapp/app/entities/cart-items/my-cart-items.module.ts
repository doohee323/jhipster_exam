import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSharedModule } from '../../shared';
import {MyCartItemsComponent} from './my-cart-items.component';
import {myCartItemsRoute} from './my-cart-items.route';
import {MyCartItemsService} from './my-cart-items.service';

const ENTITY_STATES = [
    ...myCartItemsRoute,
];

@NgModule({
    imports: [
        JhipsterSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    exports: [
      MyCartItemsComponent,
    ],
    declarations: [
        MyCartItemsComponent,
    ],
    entryComponents: [
        MyCartItemsComponent,
    ],
    providers: [
        MyCartItemsService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterMyCartItemsModule {}

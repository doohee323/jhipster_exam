import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSharedModule } from '../shared';

import { HOME_ROUTE, HomeComponent } from './';
import {ProductService} from '../entities/product/product.service';
import {JhipsterMyCartItemsModule} from '../entities/cart-items/my-cart-items.module';
import {CartItemsService} from '../entities/cart-items/cart-items.service';
import {LoginModalService} from '../shared/login/login-modal.service';

@NgModule({
    imports: [
        JhipsterSharedModule,
        JhipsterMyCartItemsModule,
        RouterModule.forRoot([ HOME_ROUTE ], { useHash: true })
    ],
    declarations: [
        HomeComponent,
    ],
    entryComponents: [
    ],
    providers: [
        ProductService,
        CartItemsService,
        LoginModalService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterHomeModule {}

import {Routes} from '@angular/router';
import {MyCartItemsComponent} from './my-cart-items.component';

export const myCartItemsRoute: Routes = [
    {
        path: 'my-cart-items',
        component: MyCartItemsComponent,
        data: {
            authorities: [],
            pageTitle: 'jhipsterApp.cartItems.home.title'
        }
    }
];

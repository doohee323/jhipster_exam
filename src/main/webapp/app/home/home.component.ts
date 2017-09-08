import {Component, OnInit} from '@angular/core';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiDateUtils, JhiEventManager} from 'ng-jhipster';

import {Account, LoginModalService, Principal} from '../shared';
import {ProductService} from '../entities/product/product.service';
import {ResponseWrapper} from '../shared/model/response-wrapper.model';
import {Product} from '../entities/product/product.model';
import {CartItems} from '../entities/cart-items/cart-items.model';
import {Response} from '@angular/http';
import {Observable} from 'rxjs';
import {User} from '../shared/user/user.model';
import {CartItemsService} from '../entities/cart-items/cart-items.service';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.scss'
    ]

})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    products: Product[];
    totalItems: number;

    cartItems: CartItems;
    isSaving: boolean;
    users: User[];

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private alertService: JhiAlertService,
        private productService: ProductService,
        private cartItemsService: CartItemsService
    ) {
        this.products = [];
    }

    loadAll() {
        this.productService.query({
            page: 0,
            size: 20,
            sort: ['id,asc']
        }).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    private onSuccess(data, headers) {
        this.totalItems = headers.get('X-Total-Count');
        for (let i = 0; i < data.length; i++) {
            this.products.push(data[i]);
        }
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
        this.loadAll();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }


    save(productId) {

        this.isSaving = true;
        this.cartItems = new CartItems();
        this.cartItems.createDt = JhiDateUtils.toLocaleString();
        this.cartItems.product = new Product(productId);

        if(this.isAuthenticated()) {
            this.cartItems.user = this.account;
            this.subscribeToSaveResponse(
                this.cartItemsService.create(this.cartItems));
        }else {
            this.login();
            this.isSaving = false;
        }

    }

    private subscribeToSaveResponse(result: Observable<CartItems>) {
        result.subscribe((res: CartItems) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: CartItems) {
        this.eventManager.broadcast({ name: 'cartItemsListModification', content: 'OK'});
        this.isSaving = false;
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

}

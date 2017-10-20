import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CartItems } from './cart-items.model';
import { CartItemsPopupService } from './cart-items-popup.service';
import { CartItemsService } from './cart-items.service';
import { Product, ProductService } from '../product';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-cart-items-dialog',
    templateUrl: './cart-items-dialog.component.html'
})
export class CartItemsDialogComponent implements OnInit {

    cartItems: CartItems;
    isSaving: boolean;

    products: Product[];

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private cartItemsService: CartItemsService,
        private productService: ProductService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.productService.query()
            .subscribe((res: ResponseWrapper) => { this.products = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.cartItems.id !== undefined) {
            this.subscribeToSaveResponse(
                this.cartItemsService.update(this.cartItems));
        } else {
            this.subscribeToSaveResponse(
                this.cartItemsService.create(this.cartItems));
        }
    }

    private subscribeToSaveResponse(result: Observable<CartItems>) {
        result.subscribe((res: CartItems) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: CartItems) {
        this.eventManager.broadcast({ name: 'cartItemsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
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

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackProductById(index: number, item: Product) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-cart-items-popup',
    template: ''
})
export class CartItemsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cartItemsPopupService: CartItemsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.cartItemsPopupService
                    .open(CartItemsDialogComponent as Component, params['id']);
            } else {
                this.cartItemsPopupService
                    .open(CartItemsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

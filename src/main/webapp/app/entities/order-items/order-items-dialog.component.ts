import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { OrderItems } from './order-items.model';
import { OrderItemsPopupService } from './order-items-popup.service';
import { OrderItemsService } from './order-items.service';
import { Orders, OrdersService } from '../orders';
import { Product, ProductService } from '../product';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-order-items-dialog',
    templateUrl: './order-items-dialog.component.html'
})
export class OrderItemsDialogComponent implements OnInit {

    orderItems: OrderItems;
    isSaving: boolean;

    orders: Orders[];

    products: Product[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private orderItemsService: OrderItemsService,
        private ordersService: OrdersService,
        private productService: ProductService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.ordersService.query()
            .subscribe((res: ResponseWrapper) => { this.orders = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.productService.query()
            .subscribe((res: ResponseWrapper) => { this.products = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.orderItems.id !== undefined) {
            this.subscribeToSaveResponse(
                this.orderItemsService.update(this.orderItems));
        } else {
            this.subscribeToSaveResponse(
                this.orderItemsService.create(this.orderItems));
        }
    }

    private subscribeToSaveResponse(result: Observable<OrderItems>) {
        result.subscribe((res: OrderItems) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: OrderItems) {
        this.eventManager.broadcast({ name: 'orderItemsListModification', content: 'OK'});
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

    trackOrdersById(index: number, item: Orders) {
        return item.id;
    }

    trackProductById(index: number, item: Product) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-order-items-popup',
    template: ''
})
export class OrderItemsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private orderItemsPopupService: OrderItemsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.orderItemsPopupService
                    .open(OrderItemsDialogComponent as Component, params['id']);
            } else {
                this.orderItemsPopupService
                    .open(OrderItemsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

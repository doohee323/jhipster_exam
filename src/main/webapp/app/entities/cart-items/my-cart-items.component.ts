import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {JhiEventManager, JhiParseLinks, JhiAlertService} from 'ng-jhipster';
import {CartItems} from './cart-items.model';
import {Principal, ResponseWrapper} from '../../shared';
import {MyCartItemsService} from './my-cart-items.service';
import {CartItemsComponent} from './cart-items.component';

@Component({
    selector: 'jhi-my-cart-items',
    templateUrl: './my-cart-items.component.html'
})
export class MyCartItemsComponent extends CartItemsComponent implements OnInit, OnDestroy {

    constructor(
         private myCartItemsService: MyCartItemsService,
         alertService: JhiAlertService,
         eventManager: JhiEventManager,
         parseLinks: JhiParseLinks,
         activatedRoute: ActivatedRoute,
         principal: Principal
    ) {
        super(myCartItemsService, alertService, eventManager, parseLinks, activatedRoute, principal);
    }

    loadAll() {
        if (this.currentSearch) {
            this.myCartItemsService.search({
                query: this.currentSearch,
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort()
            }).subscribe(
                (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
                (res: ResponseWrapper) => this.onError(res.json)
            );
            return;
        }
        this.myCartItemsService.query({
            page: this.page,
            size: this.itemsPerPage,
            sort: this.sort()
        }).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    reset() {
        this.page = 0;
        this.cartItems = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    clear() {
        this.cartItems = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch = '';
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.cartItems = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = '_score';
        this.reverse = false;
        this.currentSearch = query;
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInCartItems();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CartItems) {
        return item.id;
    }
    registerChangeInCartItems() {
        this.eventSubscriber = this.eventManager.subscribe('cartItemsListModification', (response) => this.reset());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

     onSuccess(data, headers) {

        // this.links = this.parseLinks.parse(headers.get('link'));
        // this.totalItems = headers.get('X-Total-Count');
        for (let i = 0; i < data.length; i++) {
            this.cartItems.push(data[i]);
        }
    }

     onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

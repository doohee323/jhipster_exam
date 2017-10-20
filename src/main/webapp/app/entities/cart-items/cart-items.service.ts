import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { CartItems } from './cart-items.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CartItemsService {

    protected resourceUrl = 'api/cart-items';
    protected resourceSearchUrl = 'api/_search/cart-items';

    constructor(protected http: Http, protected dateUtils: JhiDateUtils) { }

    create(cartItems: CartItems): Observable<CartItems> {
        const copy = this.convert(cartItems);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(cartItems: CartItems): Observable<CartItems> {
        const copy = this.convert(cartItems);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<CartItems> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    protected convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    protected convertItemFromServer(entity: any) {
        entity.createDt = this.dateUtils
            .convertDateTimeFromServer(entity.createDt);
    }

    protected convert(cartItems: CartItems): CartItems {
        const copy: CartItems = Object.assign({}, cartItems);

        copy.createDt = this.dateUtils.toDate(cartItems.createDt);
        return copy;
    }
}

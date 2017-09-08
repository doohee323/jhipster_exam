import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { CartItems } from './cart-items.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import {CartItemsService} from './cart-items.service';

@Injectable()
export class MyCartItemsService extends CartItemsService {

    resourceUrl = 'api/my/cart-items';

    constructor( http: Http, dateUtils: JhiDateUtils) {
        super(http, dateUtils);
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => super.convertResponse(res));
    }

}

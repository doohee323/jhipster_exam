import { BaseEntity } from './../../shared';

export class CartItems implements BaseEntity {
    constructor(
        public id?: number,
        public createDt?: any,
        public product?: BaseEntity,
    ) {
    }
}

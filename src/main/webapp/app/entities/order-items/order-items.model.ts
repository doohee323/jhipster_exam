import { BaseEntity } from './../../shared';

export class OrderItems implements BaseEntity {
    constructor(
        public id?: number,
        public unitPrice?: number,
        public quantity?: number,
        public orders?: BaseEntity,
        public product?: BaseEntity,
    ) {
    }
}

import { BaseEntity, User } from './../../shared';

export class Orders implements BaseEntity {
    constructor(
        public id?: number,
        public orderCode?: string,
        public orderDt?: any,
        public user?: User,
    ) {
    }
}

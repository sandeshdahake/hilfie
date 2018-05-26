import { BaseEntity } from './../../shared';

export class School implements BaseEntity {
    constructor(
        public id?: number,
        public schoolName?: string,
        public schoolAddress?: any,
        public schoolPhone?: string,
        public schoolFax?: string,
        public startDate?: any,
        public endDate?: any,
        public activate?: boolean,
        public classNames?: BaseEntity[],
        public ids?: BaseEntity[],
    ) {
        this.activate = false;
    }
}

import { BaseEntity, User } from './../../shared';

export class Answers implements BaseEntity {
    constructor(
        public id?: number,
        public answer?: any,
        public dateCreated?: any,
        public dateUpdated?: any,
        public isAnonymous?: boolean,
        public active?: boolean,
        public user?: User,
        public classroom?: BaseEntity,
        public questions?: BaseEntity,
    ) {
        this.isAnonymous = false;
        this.active = false;
    }
}

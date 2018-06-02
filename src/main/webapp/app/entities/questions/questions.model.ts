import { BaseEntity, User } from './../../shared';

export class Questions implements BaseEntity {
    constructor(
        public id?: number,
        public questionlabel?: string,
        public question?: any,
        public dateCreated?: any,
        public dateUpdated?: any,
        public isAnonymous?: boolean,
        public answerCount?: number,
        public active?: boolean,
        public user?: User,
        public classroom?: BaseEntity,
        public answers?: BaseEntity[],
    ) {
        this.isAnonymous = false;
        this.active = false;
    }
}

import { BaseEntity, User } from './../../shared';

export class UserProfile implements BaseEntity {
    constructor(
        public id?: number,
        public userPhone?: string,
        public userDob?: any,
        public userBloodGroup?: string,
        public userImageContentType?: string,
        public userImage?: any,
        public activate?: boolean,
        public user?: User,
        public school?: BaseEntity,
        public classroom?: BaseEntity,
    ) {
        this.activate = false;
    }
}

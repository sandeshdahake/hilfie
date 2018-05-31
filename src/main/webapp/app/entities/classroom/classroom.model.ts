import { BaseEntity } from './../../shared';

export class Classroom implements BaseEntity {
    constructor(
        public id?: number,
        public className?: string,
        public description?: string,
        public schoolName?: BaseEntity,
        public questions?: BaseEntity[],
        public answers?: BaseEntity[],
    ) {
    }
}

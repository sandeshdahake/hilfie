import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { HilfieSchoolModule } from './school/school.module';
import { HilfieClassroomModule } from './classroom/classroom.module';
import { HilfieUserProfileModule } from './user-profile/user-profile.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        HilfieSchoolModule,
        HilfieClassroomModule,
        HilfieUserProfileModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HilfieEntityModule {}

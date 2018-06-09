import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HilfieSharedModule } from '../../shared';
import { HilfieAdminModule } from '../../admin/admin.module';
import {
    UserProfileService,
    UserProfilePopupService,
    UserProfileComponent,
    UserProfileDetailComponent,
    UserProfileDialogComponent,
    UserProfilePopupComponent,
    UserProfileDeletePopupComponent,
    UserProfileDeleteDialogComponent,
    userProfileRoute,
    userProfilePopupRoute,
} from './';

const ENTITY_STATES = [
    ...userProfileRoute,
    ...userProfilePopupRoute,
];

@NgModule({
    imports: [
        HilfieSharedModule,
        HilfieAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        UserProfileComponent,
        UserProfileDetailComponent,
        UserProfileDialogComponent,
        UserProfileDeleteDialogComponent,
        UserProfilePopupComponent,
        UserProfileDeletePopupComponent,
    ],
    entryComponents: [
        UserProfileComponent,
        UserProfileDialogComponent,
        UserProfilePopupComponent,
        UserProfileDeleteDialogComponent,
        UserProfileDeletePopupComponent,
    ],
    providers: [
        UserProfileService,
        UserProfilePopupService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HilfieUserProfileModule {}

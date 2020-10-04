import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { UserProfileComponent } from './user-profile.component';
import { UserProfileDetailComponent } from './user-profile-detail.component';
import { UserProfilePopupComponent } from './user-profile-dialog.component';
import { UserProfileDeletePopupComponent } from './user-profile-delete-dialog.component';

export const userProfileRoute: Routes = [
    {
        path: 'user-profile',
        component: UserProfileComponent,
        data: {
            authorities: ['ROLE_ADMIN','ROLE_INCHARGE','ROLE_PRINCIPAL'],
            pageTitle: 'UserProfiles'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'user-profile/:id',
        component: UserProfileDetailComponent,
        data: {
            authorities: ['ROLE_ADMIN','ROLE_INCHARGE','ROLE_PRINCIPAL'],
            pageTitle: 'UserProfiles'
        },
        canActivate: [UserRouteAccessService]
    },{
        path: 'user-profile/login/:login',
        component: UserProfileDetailComponent,
        data: {
            authorities: ['ROLE_STUDENT','ROLE_ANONYMOUS','ROLE_TEACHER','ROLE_PRINCIPAL','ROLE_PARENTS','ROLE_GUARDIAN','ROLE_INCHARGE'],
            pageTitle: 'UserProfiles'
        },
    }

];

export const userProfilePopupRoute: Routes = [
    {
        path: 'user-profile-new',
        component: UserProfilePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserProfiles'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-profile/:id/edit',
        component: UserProfilePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserProfiles'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-profile/:id/delete',
        component: UserProfileDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserProfiles'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

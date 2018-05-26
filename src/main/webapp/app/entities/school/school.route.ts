import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { SchoolComponent } from './school.component';
import { SchoolDetailComponent } from './school-detail.component';
import { SchoolPopupComponent } from './school-dialog.component';
import { SchoolDeletePopupComponent } from './school-delete-dialog.component';

export const schoolRoute: Routes = [
    {
        path: 'school',
        component: SchoolComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Schools'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'school/:id',
        component: SchoolDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Schools'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const schoolPopupRoute: Routes = [
    {
        path: 'school-new',
        component: SchoolPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Schools'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'school/:id/edit',
        component: SchoolPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Schools'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'school/:id/delete',
        component: SchoolDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Schools'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

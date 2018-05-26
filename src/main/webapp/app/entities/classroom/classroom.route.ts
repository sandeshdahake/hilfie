import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ClassroomComponent } from './classroom.component';
import { ClassroomDetailComponent } from './classroom-detail.component';
import { ClassroomPopupComponent } from './classroom-dialog.component';
import { ClassroomDeletePopupComponent } from './classroom-delete-dialog.component';

export const classroomRoute: Routes = [
    {
        path: 'classroom',
        component: ClassroomComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Classrooms'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'classroom/:id',
        component: ClassroomDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Classrooms'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const classroomPopupRoute: Routes = [
    {
        path: 'classroom-new',
        component: ClassroomPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Classrooms'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'classroom/:id/edit',
        component: ClassroomPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Classrooms'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'classroom/:id/delete',
        component: ClassroomDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Classrooms'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { AnswersComponent } from './answers.component';
import { AnswersDetailComponent } from './answers-detail.component';
import { AnswersPopupComponent } from './answers-dialog.component';
import { AnswersDeletePopupComponent } from './answers-delete-dialog.component';

export const answersRoute: Routes = [
    {
        path: 'answers',
        component: AnswersComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Answers'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'answers/:id',
        component: AnswersDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Answers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const answersPopupRoute: Routes = [
    {
        path: 'answers-new',
        component: AnswersPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Answers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'answers/:id/edit',
        component: AnswersPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Answers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'answers/:id/delete',
        component: AnswersDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Answers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

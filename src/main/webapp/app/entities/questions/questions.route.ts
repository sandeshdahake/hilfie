import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { QuestionsComponent } from './questions.component';
import { QuestionsDetailComponent } from './questions-detail.component';
import { QuestionsPopupComponent } from './questions-dialog.component';
import { QuestionsDeletePopupComponent } from './questions-delete-dialog.component';

export const questionsRoute: Routes = [
    {
        path: 'questions',
        component: QuestionsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Questions'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'questions/:id',
        component: QuestionsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Questions'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const questionsPopupRoute: Routes = [
    {
        path: 'questions-new',
        component: QuestionsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Questions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'questions/:id/edit',
        component: QuestionsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Questions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'questions/:id/delete',
        component: QuestionsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Questions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

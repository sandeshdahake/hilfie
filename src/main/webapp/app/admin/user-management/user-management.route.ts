import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { JhiPaginationUtil } from 'ng-jhipster';

import { UserMgmtComponent } from './user-management.component';
import { UserMgmtDetailComponent } from './user-management-detail.component';
import { UserDialogComponent } from './user-management-dialog.component';
import { UserDeleteDialogComponent } from './user-management-delete-dialog.component';
import { UserProfilePopupComponent } from '../../entities/user-profile/user-profile-dialog.component';


import { Principal } from '../../shared';
import { UserRouteAccessService } from '../../shared';

@Injectable()
export class UserResolve implements CanActivate {

    constructor(private principal: Principal) { }

    canActivate() {
        return this.principal.identity().then((account) => this.principal.hasAnyAuthority(['ROLE_ADMIN']));
    }
}

@Injectable()
export class UserResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
        };
    }
}

export const userMgmtRoute: Routes = [
    {
        path: 'user-management',
        component: UserMgmtComponent,
        resolve: {
            'pagingParams': UserResolvePagingParams
        },
        data: {
            pageTitle: 'Users'
        }
    },
    {
        path: 'user-management/:login',
        component: UserMgmtDetailComponent,
        data: {
            pageTitle: 'Users'
        }
    }
];

export const userDialogRoute: Routes = [
    {
        path: 'user-management-new',
        component: UserDialogComponent,
        outlet: 'popup'
    },
    {
        path: 'user-management/:login/edit',
        component: UserDialogComponent,
        outlet: 'popup'
    },
    {
        path: 'user-management/:login/delete',
        component: UserDeleteDialogComponent,
        outlet: 'popup'
    },
    {
        path: 'user-profile-new',
        component: UserProfilePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserProfiles'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
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
    }
];

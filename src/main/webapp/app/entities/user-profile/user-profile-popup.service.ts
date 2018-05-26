import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { UserProfile } from './user-profile.model';
import { UserProfileService } from './user-profile.service';

@Injectable()
export class UserProfilePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private userProfileService: UserProfileService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.userProfileService.find(id)
                    .subscribe((userProfileResponse: HttpResponse<UserProfile>) => {
                        const userProfile: UserProfile = userProfileResponse.body;
                        if (userProfile.userDob) {
                            userProfile.userDob = {
                                year: userProfile.userDob.getFullYear(),
                                month: userProfile.userDob.getMonth() + 1,
                                day: userProfile.userDob.getDate()
                            };
                        }
                        this.ngbModalRef = this.userProfileModalRef(component, userProfile);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.userProfileModalRef(component, new UserProfile());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    userProfileModalRef(component: Component, userProfile: UserProfile): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.userProfile = userProfile;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}

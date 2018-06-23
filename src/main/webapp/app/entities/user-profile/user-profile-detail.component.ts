import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';
import { UserProfile } from './user-profile.model';
import { UserProfileService } from './user-profile.service';

@Component({
    selector: 'jhi-user-profile-detail',
    templateUrl: './user-profile-detail.component.html'
})
export class UserProfileDetailComponent implements OnInit, OnDestroy {

    userProfile: UserProfile;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private userProfileService: UserProfileService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
          //  this.load(params['id']);
            this.findByLogin(params['login']);

        });
        this.registerChangeInUserProfiles();
    }

    load(id) {
        this.userProfileService.find(id)
            .subscribe((userProfileResponse: HttpResponse<UserProfile>) => {
                this.userProfile = userProfileResponse.body;
            });
    }

    findByLogin(login) {
        this.userProfileService.findByLogin(login)
            .subscribe((userProfileResponse: HttpResponse<UserProfile>) => {
                this.userProfile = userProfileResponse.body;
            });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInUserProfiles() {
        this.eventSubscriber = this.eventManager.subscribe(
            'userProfileListModification',
            (response) => this.load(this.userProfile.id)
        );
    }
}

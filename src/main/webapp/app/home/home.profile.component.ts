import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';
import { UserProfile } from '../entities/user-profile/user-profile.model';
import { UserProfileService } from '../entities/user-profile/user-profile.service';
@Component({
    selector: 'home-profile-comp',
    templateUrl: './home.profile.component.html',
    styleUrls: [
        'home.scss'
    ]

})
export class HomeProfileComponent implements OnInit{

    @Input() inputLogin: String;

    userProfile: UserProfile;
    login:String;

    constructor(private userProfileService: UserProfileService) {
        this.login = this.inputLogin ;
    }

    ngOnInit() {
        this.login = this.inputLogin ;
        this.userProfileService.findByLogin(this.login)
            .subscribe((userProfileResponse: HttpResponse<UserProfile>) => {
                this.userProfile = userProfileResponse.body;
            });
    }

}

import { Component, OnInit, OnDestroy,ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { UserProfile } from './user-profile.model';
import { UserProfilePopupService } from './user-profile-popup.service';
import { UserProfileService } from './user-profile.service';
import { User, UserService } from '../../shared';
import { School, SchoolService } from '../school';
import { Classroom, ClassroomService } from '../classroom';
import { ImageCropperComponent, CropperSettings } from "ngx-img-cropper";
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
    selector: 'jhi-user-profile-dialog',
    templateUrl: './user-profile-dialog.component.html'
})
export class UserProfileDialogComponent implements OnInit {

    userProfile: UserProfile;
    isSaving: boolean;

    users: User[];

    schools: School[];

    classrooms: Classroom[];
    userDobDp: any;
    cropperSettings: CropperSettings;
    data:any;
    @ViewChild('cropper', undefined)
    cropper:ImageCropperComponent;
    file:File;
    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private userProfileService: UserProfileService,
        private userService: UserService,
        private schoolService: SchoolService,
        private classroomService: ClassroomService,
        private eventManager: JhiEventManager,
        private spinner: NgxSpinnerService
    ) {
        this.cropperSettings = new CropperSettings();

            this.cropperSettings.noFileInput = true;
      this.cropperSettings.canvasWidth = 500;
      this.cropperSettings.canvasHeight = 300;

      this.cropperSettings.minWidth = 10;
      this.cropperSettings.minHeight = 10;

      this.cropperSettings.rounded = true;
      this.cropperSettings.keepAspect = true;

      this.cropperSettings.cropperDrawSettings.strokeColor = 'rgba(255,255,255,1)';
      this.cropperSettings.cropperDrawSettings.strokeWidth = 2;

        this.data = {};

    }

    fileChangeListener($event) {
        var image:any = new Image();
        var file:File = $event.target.files[0];
        var myReader:FileReader = new FileReader();
        var that = this;
        myReader.onloadend = function (loadEvent:any) {
            image.src = loadEvent.target.result;
            that.cropper.setImage(image, null);
        };
       myReader.readAsDataURL(file);
       this.file = file;
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.schoolService.query()
            .subscribe((res: HttpResponse<School[]>) => { this.schools = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.classroomService.query()
            .subscribe((res: HttpResponse<Classroom[]>) => { this.classrooms = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    profileHandler() {
            this.spinner.show()
            this.userProfileService.sendFileToServer(this.file).subscribe((url: string) => {
                this.userProfile.userImage = url;
                 alert(url);
                 this.spinner.hide()
                });
    }
profileHandler1(){
    alert("sandesh");
}
    save() {
        this.isSaving = true;
        if (this.userProfile.id !== undefined) {
            this.subscribeToSaveResponse(
                this.userProfileService.update(this.userProfile));
        } else {
            this.subscribeToSaveResponse(
                this.userProfileService.create(this.userProfile));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<UserProfile>>) {
        result.subscribe((res: HttpResponse<UserProfile>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: UserProfile) {
        this.eventManager.broadcast({ name: 'userProfileListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }
    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackSchoolById(index: number, item: School) {
        return item.id;
    }

    trackClassroomById(index: number, item: Classroom) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-user-profile-popup',
    template: ''
})
export class UserProfilePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userProfilePopupService: UserProfilePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.userProfilePopupService
                    .open(UserProfileDialogComponent as Component, params['id']);
            } else {
                this.userProfilePopupService
                    .open(UserProfileDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

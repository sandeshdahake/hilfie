import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Classroom } from './classroom.model';
import { ClassroomPopupService } from './classroom-popup.service';
import { ClassroomService } from './classroom.service';
import { School, SchoolService } from '../school';

@Component({
    selector: 'jhi-classroom-dialog',
    templateUrl: './classroom-dialog.component.html'
})
export class ClassroomDialogComponent implements OnInit {

    classroom: Classroom;
    isSaving: boolean;

    schools: School[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private classroomService: ClassroomService,
        private schoolService: SchoolService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.schoolService.query()
            .subscribe((res: HttpResponse<School[]>) => { this.schools = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.classroom.id !== undefined) {
            this.subscribeToSaveResponse(
                this.classroomService.update(this.classroom));
        } else {
            this.subscribeToSaveResponse(
                this.classroomService.create(this.classroom));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Classroom>>) {
        result.subscribe((res: HttpResponse<Classroom>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Classroom) {
        this.eventManager.broadcast({ name: 'classroomListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackSchoolById(index: number, item: School) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-classroom-popup',
    template: ''
})
export class ClassroomPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private classroomPopupService: ClassroomPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.classroomPopupService
                    .open(ClassroomDialogComponent as Component, params['id']);
            } else {
                this.classroomPopupService
                    .open(ClassroomDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

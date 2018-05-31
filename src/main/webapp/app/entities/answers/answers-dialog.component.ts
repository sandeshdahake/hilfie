import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Answers } from './answers.model';
import { AnswersPopupService } from './answers-popup.service';
import { AnswersService } from './answers.service';
import { User, UserService } from '../../shared';
import { Classroom, ClassroomService } from '../classroom';
import { Questions, QuestionsService } from '../questions';

@Component({
    selector: 'jhi-answers-dialog',
    templateUrl: './answers-dialog.component.html'
})
export class AnswersDialogComponent implements OnInit {

    answers: Answers;
    isSaving: boolean;

    users: User[];

    classrooms: Classroom[];

    questions: Questions[];
    dateCreatedDp: any;
    dateUpdatedDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private answersService: AnswersService,
        private userService: UserService,
        private classroomService: ClassroomService,
        private questionsService: QuestionsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.classroomService.query()
            .subscribe((res: HttpResponse<Classroom[]>) => { this.classrooms = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.questionsService.query()
            .subscribe((res: HttpResponse<Questions[]>) => { this.questions = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.answers.id !== undefined) {
            this.subscribeToSaveResponse(
                this.answersService.update(this.answers));
        } else {
            this.subscribeToSaveResponse(
                this.answersService.create(this.answers));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Answers>>) {
        result.subscribe((res: HttpResponse<Answers>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Answers) {
        this.eventManager.broadcast({ name: 'answersListModification', content: 'OK'});
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

    trackClassroomById(index: number, item: Classroom) {
        return item.id;
    }

    trackQuestionsById(index: number, item: Questions) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-answers-popup',
    template: ''
})
export class AnswersPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private answersPopupService: AnswersPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.answersPopupService
                    .open(AnswersDialogComponent as Component, params['id']);
            } else {
                this.answersPopupService
                    .open(AnswersDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

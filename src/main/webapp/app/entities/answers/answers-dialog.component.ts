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
import { Subscription } from 'rxjs/Subscription';

@Component({
    selector: 'jhi-answers-dialog',
    templateUrl: './answers-dialog.component.html'
})
export class AnswersDialogComponent implements OnInit {
    qid:number;
    answers: Answers;
    isSaving: boolean;
    users: User[];
    classrooms: Classroom[];
    editor: any;
    returnedURL: any;
    questions: Questions;
    dateCreatedDp: any;
    dateUpdatedDp: any;

    private subscription: Subscription;
    private eventSubscriber: Subscription;
    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private answersService: AnswersService,
        private userService: UserService,
        private classroomService: ClassroomService,
        private questionsService: QuestionsService,
        private eventManager: JhiEventManager,
        private route: ActivatedRoute

    ) {
    }

    ngOnInit() {
        this.isSaving = false;
/*
        this.subscription = this.route.params.subscribe((params) => {

            alert(this.qid);
            this.qid = this.qid;
        });
*/
        alert(this.qid);
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.classroomService.query()
            .subscribe((res: HttpResponse<Classroom[]>) => { this.classrooms = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.questionsService.find(this.qid)
            .subscribe((res: HttpResponse<Questions>) => { this.questions = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
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
            this.answers.questions = this.questions;
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

     editorCreated(event) {
        const toolbar = event.editor.getModule('toolbar');
        toolbar.addHandler('image', this.imageHandler.bind(this));
        this.editor = event.editor;
        this.editor.addContainer('ql-video');
    }

    imageHandler() {
      const Imageinput = document.createElement('input');
      Imageinput.setAttribute('type', 'file');
      Imageinput.setAttribute('accept', 'image/png, image/gif, image/jpeg, image/bmp, image/x-icon');
      Imageinput.classList.add('ql-image');

      Imageinput.addEventListener('change', () =>  {
        const file = Imageinput.files[0];
        if (Imageinput.files != null && Imageinput.files[0] != null) {
            this.questionsService.sendFileToServer(file).subscribe((url: String) => {
            this.returnedURL = url;
            this.pushImageToEditor();
            });
        }
    });

      Imageinput.click();
    }

    pushImageToEditor() {
      const range = this.editor.getSelection(true);
      const index = range.index + range.length;
      this.editor.insertEmbed(range.index, 'image', this.returnedURL);
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
                    .open(AnswersDialogComponent as Component, params['id'],params['qid']);
            } else {
                    this.answersPopupService
                    .open(AnswersDialogComponent as Component, null ,params['qid']);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

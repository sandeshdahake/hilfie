import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { Cloudinary } from '@cloudinary/angular-5.x';

import { Questions } from './questions.model';
import { QuestionsPopupService } from './questions-popup.service';
import { QuestionsService } from './questions.service';
import { User, UserService } from '../../shared';
import { Classroom, ClassroomService } from '../classroom';

@Component({
    selector: 'jhi-questions-dialog',
    templateUrl: './questions-dialog.component.html'
})
export class QuestionsDialogComponent implements OnInit {
    questions: Questions;
    isSaving: boolean;
    editor: any;
    users: User[];
    returnedURL:any;

    classrooms: Classroom[];
    dateCreatedDp: any;
    dateUpdatedDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private questionsService: QuestionsService,
        private userService: UserService,
        private classroomService: ClassroomService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.classroomService.query()
            .subscribe((res: HttpResponse<Classroom[]>) => { this.classrooms = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
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
        if (this.questions.id !== undefined) {
            this.subscribeToSaveResponse(
                this.questionsService.update(this.questions));
        } else {
            this.subscribeToSaveResponse(
                this.questionsService.create(this.questions));
        }
    }

     editorCreated(event) {
        const toolbar = event.editor.getModule('toolbar');
        toolbar.addHandler('image', this.imageHandler.bind(this));
        this.editor = event.editor;
    }

    imageHandler() {
      const Imageinput = document.createElement('input');
      Imageinput.setAttribute('type', 'file');
      Imageinput.setAttribute('accept', 'image/png, image/gif, image/jpeg, image/bmp, image/x-icon');
      Imageinput.classList.add('ql-image');

      Imageinput.addEventListener('change', () =>  {
        const file = Imageinput.files[0];

        //alert(file);
        if (Imageinput.files != null && Imageinput.files[0] != null) {
            this.questionsService.sendFileToServer(file).subscribe((url: String) => {
            alert(url);
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
    private subscribeToSaveResponse(result: Observable<HttpResponse<Questions>>) {
        result.subscribe((res: HttpResponse<Questions>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Questions) {
        this.eventManager.broadcast({ name: 'questionsListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-questions-popup',
    template: ''
})
export class QuestionsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private questionsPopupService: QuestionsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.questionsPopupService
                    .open(QuestionsDialogComponent as Component, params['id']);
            } else {
                this.questionsPopupService
                    .open(QuestionsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

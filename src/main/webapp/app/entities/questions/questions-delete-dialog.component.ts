import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Questions } from './questions.model';
import { QuestionsPopupService } from './questions-popup.service';
import { QuestionsService } from './questions.service';

@Component({
    selector: 'jhi-questions-delete-dialog',
    templateUrl: './questions-delete-dialog.component.html'
})
export class QuestionsDeleteDialogComponent {

    questions: Questions;

    constructor(
        private questionsService: QuestionsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.questionsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'questionsListModification',
                content: 'Deleted an questions'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-questions-delete-popup',
    template: ''
})
export class QuestionsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private questionsPopupService: QuestionsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.questionsPopupService
                .open(QuestionsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Answers } from './answers.model';
import { AnswersPopupService } from './answers-popup.service';
import { AnswersService } from './answers.service';

@Component({
    selector: 'jhi-answers-delete-dialog',
    templateUrl: './answers-delete-dialog.component.html'
})
export class AnswersDeleteDialogComponent {

    answers: Answers;

    constructor(
        private answersService: AnswersService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.answersService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'answersListModification',
                content: 'Deleted an answers'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-answers-delete-popup',
    template: ''
})
export class AnswersDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private answersPopupService: AnswersPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.answersPopupService
                .open(AnswersDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

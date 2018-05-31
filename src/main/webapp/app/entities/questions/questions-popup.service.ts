import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Questions } from './questions.model';
import { QuestionsService } from './questions.service';

@Injectable()
export class QuestionsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private questionsService: QuestionsService

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
                this.questionsService.find(id)
                    .subscribe((questionsResponse: HttpResponse<Questions>) => {
                        const questions: Questions = questionsResponse.body;
                        if (questions.dateCreated) {
                            questions.dateCreated = {
                                year: questions.dateCreated.getFullYear(),
                                month: questions.dateCreated.getMonth() + 1,
                                day: questions.dateCreated.getDate()
                            };
                        }
                        if (questions.dateUpdated) {
                            questions.dateUpdated = {
                                year: questions.dateUpdated.getFullYear(),
                                month: questions.dateUpdated.getMonth() + 1,
                                day: questions.dateUpdated.getDate()
                            };
                        }
                        this.ngbModalRef = this.questionsModalRef(component, questions);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.questionsModalRef(component, new Questions());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    questionsModalRef(component: Component, questions: Questions): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.questions = questions;
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

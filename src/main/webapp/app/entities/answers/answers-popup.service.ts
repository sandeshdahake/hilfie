import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Answers } from './answers.model';
import { AnswersService } from './answers.service';

@Injectable()
export class AnswersPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private answersService: AnswersService

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
                this.answersService.find(id)
                    .subscribe((answersResponse: HttpResponse<Answers>) => {
                        const answers: Answers = answersResponse.body;
                        if (answers.dateCreated) {
                            answers.dateCreated = {
                                year: answers.dateCreated.getFullYear(),
                                month: answers.dateCreated.getMonth() + 1,
                                day: answers.dateCreated.getDate()
                            };
                        }
                        if (answers.dateUpdated) {
                            answers.dateUpdated = {
                                year: answers.dateUpdated.getFullYear(),
                                month: answers.dateUpdated.getMonth() + 1,
                                day: answers.dateUpdated.getDate()
                            };
                        }
                        this.ngbModalRef = this.answersModalRef(component, answers);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.answersModalRef(component, new Answers());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    answersModalRef(component: Component, answers: Answers): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.answers = answers;
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

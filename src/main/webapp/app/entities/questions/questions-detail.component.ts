import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { Questions } from './questions.model';
import { QuestionsService } from './questions.service';
import { Answers } from '../answers/answers.model';
import { AnswersService } from '../answers/answers.service';
import {BrowserModule, DomSanitizer} from '@angular/platform-browser'
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import {  JhiParseLinks, JhiAlertService } from 'ng-jhipster';


@Component({
    selector: 'jhi-questions-detail',
    templateUrl: './questions-detail.component.html'
})
export class QuestionsDetailComponent implements OnInit, OnDestroy {

    questions: Questions;
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    totalItems: number;

    answers: Answers[];
    currentAccount: any;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    queryCount: any;
    reverse: any;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private questionsService: QuestionsService,
        private route: ActivatedRoute,
        private sanitizer: DomSanitizer,
        private answersService: AnswersService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
    ) {
        this.answers = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = 'id';
        this.reverse = true;

    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInQuestions();
        this.registerChangeInAnswers();

    }

    load(id) {
        this.questionsService.find(id)
            .subscribe((questionsResponse: HttpResponse<Questions>) => {
                let response:Questions = questionsResponse.body;
                response.safeQuestion  = this.sanitizer.bypassSecurityTrustHtml(response.question);
                this.questions = response;
            this.loadAllByQuestion( );

            });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

        registerChangeInQuestions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'questionsListModification',
            (response) => this.load(this.questions.id)
        );
    }

    registerChangeInAnswers() {
        this.eventSubscriber = this.eventManager.subscribe('answersListModification', (response) => this.reset());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        for (let i = 0; i < data.length; i++) {
            data[i].safeAnswer=data[i].answer;
            this.answers.push(data[i]);
        }
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
    reset() {
        this.page = 0;
        this.answers = [];
        this.loadAllByQuestion();
    }
 loadPage(page) {
        this.page = page;
        this.loadAllByQuestion();
    }

    loadAllByQuestion() {
    alert("test");
    this.answersService.findByQuestion({
                        id: this.questions.id,
                        page: this.page,
                        size: this.itemsPerPage,
                        sort: this.sort()
                    }).subscribe(
                        (res: HttpResponse<Answers[]>) => this.onSuccess(res.body, res.headers),
                        (res: HttpErrorResponse) => this.onError(res.message)
                    );
    }
}

/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { HilfieTestModule } from '../../../test.module';
import { QuestionsComponent } from '../../../../../../main/webapp/app/entities/questions/questions.component';
import { QuestionsService } from '../../../../../../main/webapp/app/entities/questions/questions.service';
import { Questions } from '../../../../../../main/webapp/app/entities/questions/questions.model';

describe('Component Tests', () => {

    describe('Questions Management Component', () => {
        let comp: QuestionsComponent;
        let fixture: ComponentFixture<QuestionsComponent>;
        let service: QuestionsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HilfieTestModule],
                declarations: [QuestionsComponent],
                providers: [
                    QuestionsService
                ]
            })
            .overrideTemplate(QuestionsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(QuestionsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(QuestionsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Questions(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.questions[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});

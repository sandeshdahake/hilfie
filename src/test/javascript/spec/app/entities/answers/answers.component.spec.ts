/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { HilfieTestModule } from '../../../test.module';
import { AnswersComponent } from '../../../../../../main/webapp/app/entities/answers/answers.component';
import { AnswersService } from '../../../../../../main/webapp/app/entities/answers/answers.service';
import { Answers } from '../../../../../../main/webapp/app/entities/answers/answers.model';

describe('Component Tests', () => {

    describe('Answers Management Component', () => {
        let comp: AnswersComponent;
        let fixture: ComponentFixture<AnswersComponent>;
        let service: AnswersService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HilfieTestModule],
                declarations: [AnswersComponent],
                providers: [
                    AnswersService
                ]
            })
            .overrideTemplate(AnswersComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AnswersComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnswersService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Answers(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.answers[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});

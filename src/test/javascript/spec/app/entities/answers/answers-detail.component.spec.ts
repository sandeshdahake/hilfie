/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { HilfieTestModule } from '../../../test.module';
import { AnswersDetailComponent } from '../../../../../../main/webapp/app/entities/answers/answers-detail.component';
import { AnswersService } from '../../../../../../main/webapp/app/entities/answers/answers.service';
import { Answers } from '../../../../../../main/webapp/app/entities/answers/answers.model';

describe('Component Tests', () => {

    describe('Answers Management Detail Component', () => {
        let comp: AnswersDetailComponent;
        let fixture: ComponentFixture<AnswersDetailComponent>;
        let service: AnswersService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HilfieTestModule],
                declarations: [AnswersDetailComponent],
                providers: [
                    AnswersService
                ]
            })
            .overrideTemplate(AnswersDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AnswersDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnswersService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Answers(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.answers).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});

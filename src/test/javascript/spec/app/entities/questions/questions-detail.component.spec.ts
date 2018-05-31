/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { HilfieTestModule } from '../../../test.module';
import { QuestionsDetailComponent } from '../../../../../../main/webapp/app/entities/questions/questions-detail.component';
import { QuestionsService } from '../../../../../../main/webapp/app/entities/questions/questions.service';
import { Questions } from '../../../../../../main/webapp/app/entities/questions/questions.model';

describe('Component Tests', () => {

    describe('Questions Management Detail Component', () => {
        let comp: QuestionsDetailComponent;
        let fixture: ComponentFixture<QuestionsDetailComponent>;
        let service: QuestionsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HilfieTestModule],
                declarations: [QuestionsDetailComponent],
                providers: [
                    QuestionsService
                ]
            })
            .overrideTemplate(QuestionsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(QuestionsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(QuestionsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Questions(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.questions).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});

/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { HilfieTestModule } from '../../../test.module';
import { AnswersDialogComponent } from '../../../../../../main/webapp/app/entities/answers/answers-dialog.component';
import { AnswersService } from '../../../../../../main/webapp/app/entities/answers/answers.service';
import { Answers } from '../../../../../../main/webapp/app/entities/answers/answers.model';
import { UserService } from '../../../../../../main/webapp/app/shared';
import { ClassroomService } from '../../../../../../main/webapp/app/entities/classroom';
import { QuestionsService } from '../../../../../../main/webapp/app/entities/questions';

describe('Component Tests', () => {

    describe('Answers Management Dialog Component', () => {
        let comp: AnswersDialogComponent;
        let fixture: ComponentFixture<AnswersDialogComponent>;
        let service: AnswersService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HilfieTestModule],
                declarations: [AnswersDialogComponent],
                providers: [
                    UserService,
                    ClassroomService,
                    QuestionsService,
                    AnswersService
                ]
            })
            .overrideTemplate(AnswersDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AnswersDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnswersService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Answers(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.answers = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'answersListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Answers();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.answers = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'answersListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});

/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { HilfieTestModule } from '../../../test.module';
import { ClassroomDialogComponent } from '../../../../../../main/webapp/app/entities/classroom/classroom-dialog.component';
import { ClassroomService } from '../../../../../../main/webapp/app/entities/classroom/classroom.service';
import { Classroom } from '../../../../../../main/webapp/app/entities/classroom/classroom.model';
import { SchoolService } from '../../../../../../main/webapp/app/entities/school';

describe('Component Tests', () => {

    describe('Classroom Management Dialog Component', () => {
        let comp: ClassroomDialogComponent;
        let fixture: ComponentFixture<ClassroomDialogComponent>;
        let service: ClassroomService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HilfieTestModule],
                declarations: [ClassroomDialogComponent],
                providers: [
                    SchoolService,
                    ClassroomService
                ]
            })
            .overrideTemplate(ClassroomDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ClassroomDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ClassroomService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Classroom(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.classroom = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'classroomListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Classroom();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.classroom = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'classroomListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});

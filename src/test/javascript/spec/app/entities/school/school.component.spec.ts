/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { HilfieTestModule } from '../../../test.module';
import { SchoolComponent } from '../../../../../../main/webapp/app/entities/school/school.component';
import { SchoolService } from '../../../../../../main/webapp/app/entities/school/school.service';
import { School } from '../../../../../../main/webapp/app/entities/school/school.model';

describe('Component Tests', () => {

    describe('School Management Component', () => {
        let comp: SchoolComponent;
        let fixture: ComponentFixture<SchoolComponent>;
        let service: SchoolService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HilfieTestModule],
                declarations: [SchoolComponent],
                providers: [
                    SchoolService
                ]
            })
            .overrideTemplate(SchoolComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SchoolComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SchoolService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new School(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.schools[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});

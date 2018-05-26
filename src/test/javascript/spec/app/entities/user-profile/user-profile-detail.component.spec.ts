/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { HilfieTestModule } from '../../../test.module';
import { UserProfileDetailComponent } from '../../../../../../main/webapp/app/entities/user-profile/user-profile-detail.component';
import { UserProfileService } from '../../../../../../main/webapp/app/entities/user-profile/user-profile.service';
import { UserProfile } from '../../../../../../main/webapp/app/entities/user-profile/user-profile.model';

describe('Component Tests', () => {

    describe('UserProfile Management Detail Component', () => {
        let comp: UserProfileDetailComponent;
        let fixture: ComponentFixture<UserProfileDetailComponent>;
        let service: UserProfileService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HilfieTestModule],
                declarations: [UserProfileDetailComponent],
                providers: [
                    UserProfileService
                ]
            })
            .overrideTemplate(UserProfileDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(UserProfileDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserProfileService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new UserProfile(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.userProfile).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});

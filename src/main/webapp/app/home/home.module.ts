import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HilfieSharedModule } from '../shared';
import { HOME_ROUTE, HomeComponent } from './';
import {  AdvComponent } from './adv.component';
import {CarouselModule} from 'primeng/carousel';
import {  HomeProfileComponent } from './home.profile.component';
import {  HomeQuestionsComponent } from './home.question.component';

@NgModule({
    imports: [
        HilfieSharedModule,
        CarouselModule,
        RouterModule.forChild([ HOME_ROUTE ])
    ],
    declarations: [
        HomeComponent, AdvComponent,HomeProfileComponent, HomeQuestionsComponent
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HilfieHomeModule {}

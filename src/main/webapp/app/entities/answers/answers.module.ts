import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {EditorModule} from 'primeng/editor';
import { HilfieSharedModule } from '../../shared';
import { HilfieAdminModule } from '../../admin/admin.module';
import {
    AnswersService,
    AnswersPopupService,
    AnswersComponent,
    AnswersDetailComponent,
    AnswersDialogComponent,
    AnswersPopupComponent,
    AnswersDeletePopupComponent,
    AnswersDeleteDialogComponent,
    answersRoute,
    answersPopupRoute,
} from './';

const ENTITY_STATES = [
    ...answersRoute,
    ...answersPopupRoute,
];

@NgModule({
    imports: [
        HilfieSharedModule,
        HilfieAdminModule,
        RouterModule.forChild(ENTITY_STATES),
        EditorModule
    ],
    declarations: [
        AnswersComponent,
        AnswersDetailComponent,
        AnswersDialogComponent,
        AnswersDeleteDialogComponent,
        AnswersPopupComponent,
        AnswersDeletePopupComponent,
    ],
    entryComponents: [
        AnswersComponent,
        AnswersDialogComponent,
        AnswersPopupComponent,
        AnswersDeleteDialogComponent,
        AnswersDeletePopupComponent,
    ],
    providers: [
        AnswersService,
        AnswersPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HilfieAnswersModule {}

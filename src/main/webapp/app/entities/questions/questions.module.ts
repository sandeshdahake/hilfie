import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {EditorModule} from 'primeng/editor';

import { HilfieSharedModule } from '../../shared';
import { HilfieAdminModule } from '../../admin/admin.module';

import {
    QuestionsService,
    QuestionsPopupService,
    QuestionsComponent,
    QuestionsDetailComponent,
    QuestionsDialogComponent,
    QuestionsPopupComponent,
    QuestionsDeletePopupComponent,
    QuestionsDeleteDialogComponent,
    questionsRoute,
    questionsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...questionsRoute,
    ...questionsPopupRoute,
];

@NgModule({
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HilfieSharedModule,
        HilfieAdminModule,
        RouterModule.forChild(ENTITY_STATES),
        EditorModule
    ],
    declarations: [
        QuestionsComponent,
        QuestionsDetailComponent,
        QuestionsDialogComponent,
        QuestionsDeleteDialogComponent,
        QuestionsPopupComponent,
        QuestionsDeletePopupComponent,
    ],
    entryComponents: [
        QuestionsComponent,
        QuestionsDialogComponent,
        QuestionsPopupComponent,
        QuestionsDeleteDialogComponent,
        QuestionsDeletePopupComponent,
    ],
    providers: [
        QuestionsService,
        QuestionsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HilfieQuestionsModule {}

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Questions } from './questions.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Questions>;
export type EntityResponseStringType = HttpResponse<String>;

@Injectable()
export class QuestionsService {

    private resourceUrl =  SERVER_API_URL + 'api/questions';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/questions';
    private cloudName = 'sandeshdahake';
    private unsignedUploadPreset = 'ucappsk7';

    private imageUploadUrl ='https://api.cloudinary.com/v1_1/${cloudName}/upload';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(questions: Questions): Observable<EntityResponseType> {
        const copy = this.convert(questions);
        return this.http.post<Questions>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    sendFileToServer(file:File):Observable<String>{
         const copy = this.convertToObject(file);
         const httpOptions = {
           headers: new HttpHeaders({
             'responseType':  'text'

           })}
        return this.http.post(this.resourceUrl+'/imageUpload', copy, {responseType: 'text'})
        //.map((res: EntityResponseStringType) => res.body)

    }

    update(questions: Questions): Observable<EntityResponseType> {
        const copy = this.convert(questions);
        return this.http.put<Questions>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Questions>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Questions[]>> {
        const options = createRequestOption(req);
        return this.http.get<Questions[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Questions[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Questions[]>> {
        const options = createRequestOption(req);
        return this.http.get<Questions[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Questions[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Questions = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Questions[]>): HttpResponse<Questions[]> {
        const jsonResponse: Questions[] = res.body;
        const body: Questions[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Questions.
     */
    private convertItemFromServer(questions: Questions): Questions {
        const copy: Questions = Object.assign({}, questions);
        copy.dateCreated = this.dateUtils
            .convertLocalDateFromServer(questions.dateCreated);
        copy.dateUpdated = this.dateUtils
            .convertLocalDateFromServer(questions.dateUpdated);
        return copy;
    }

    /**
     * Convert a Questions to a JSON which can be sent to the server.
     */
    private convert(questions: Questions): Questions {
        const copy: Questions = Object.assign({}, questions);
        copy.dateCreated = this.dateUtils
            .convertLocalDateToServer(questions.dateCreated);
        copy.dateUpdated = this.dateUtils
            .convertLocalDateToServer(questions.dateUpdated);
        return copy;
    }

    private convertToObject(file:File):FormData{
    let formData: FormData = new FormData();
    formData.append('file', file);

            return formData;
    }
}

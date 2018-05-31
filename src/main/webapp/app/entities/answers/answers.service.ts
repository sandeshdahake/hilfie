import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Answers } from './answers.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Answers>;

@Injectable()
export class AnswersService {

    private resourceUrl =  SERVER_API_URL + 'api/answers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/answers';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(answers: Answers): Observable<EntityResponseType> {
        const copy = this.convert(answers);
        return this.http.post<Answers>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(answers: Answers): Observable<EntityResponseType> {
        const copy = this.convert(answers);
        return this.http.put<Answers>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Answers>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Answers[]>> {
        const options = createRequestOption(req);
        return this.http.get<Answers[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Answers[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Answers[]>> {
        const options = createRequestOption(req);
        return this.http.get<Answers[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Answers[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Answers = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Answers[]>): HttpResponse<Answers[]> {
        const jsonResponse: Answers[] = res.body;
        const body: Answers[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Answers.
     */
    private convertItemFromServer(answers: Answers): Answers {
        const copy: Answers = Object.assign({}, answers);
        copy.dateCreated = this.dateUtils
            .convertLocalDateFromServer(answers.dateCreated);
        copy.dateUpdated = this.dateUtils
            .convertLocalDateFromServer(answers.dateUpdated);
        return copy;
    }

    /**
     * Convert a Answers to a JSON which can be sent to the server.
     */
    private convert(answers: Answers): Answers {
        const copy: Answers = Object.assign({}, answers);
        copy.dateCreated = this.dateUtils
            .convertLocalDateToServer(answers.dateCreated);
        copy.dateUpdated = this.dateUtils
            .convertLocalDateToServer(answers.dateUpdated);
        return copy;
    }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { School } from './school.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<School>;

@Injectable()
export class SchoolService {

    private resourceUrl =  SERVER_API_URL + 'api/schools';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/schools';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(school: School): Observable<EntityResponseType> {
        const copy = this.convert(school);
        return this.http.post<School>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(school: School): Observable<EntityResponseType> {
        const copy = this.convert(school);
        return this.http.put<School>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<School>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<School[]>> {
        const options = createRequestOption(req);
        return this.http.get<School[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<School[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<School[]>> {
        const options = createRequestOption(req);
        return this.http.get<School[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<School[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: School = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<School[]>): HttpResponse<School[]> {
        const jsonResponse: School[] = res.body;
        const body: School[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to School.
     */
    private convertItemFromServer(school: School): School {
        const copy: School = Object.assign({}, school);
        copy.startDate = this.dateUtils
            .convertLocalDateFromServer(school.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateFromServer(school.endDate);
        return copy;
    }

    /**
     * Convert a School to a JSON which can be sent to the server.
     */
    private convert(school: School): School {
        const copy: School = Object.assign({}, school);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(school.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(school.endDate);
        return copy;
    }
}

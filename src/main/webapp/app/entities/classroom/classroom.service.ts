import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Classroom } from './classroom.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Classroom>;

@Injectable()
export class ClassroomService {

    private resourceUrl =  SERVER_API_URL + 'api/classrooms';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/classrooms';

    constructor(private http: HttpClient) { }

    create(classroom: Classroom): Observable<EntityResponseType> {
        const copy = this.convert(classroom);
        return this.http.post<Classroom>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(classroom: Classroom): Observable<EntityResponseType> {
        const copy = this.convert(classroom);
        return this.http.put<Classroom>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Classroom>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Classroom[]>> {
        const options = createRequestOption(req);
        return this.http.get<Classroom[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Classroom[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Classroom[]>> {
        const options = createRequestOption(req);
        return this.http.get<Classroom[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Classroom[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Classroom = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Classroom[]>): HttpResponse<Classroom[]> {
        const jsonResponse: Classroom[] = res.body;
        const body: Classroom[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Classroom.
     */
    private convertItemFromServer(classroom: Classroom): Classroom {
        const copy: Classroom = Object.assign({}, classroom);
        return copy;
    }

    /**
     * Convert a Classroom to a JSON which can be sent to the server.
     */
    private convert(classroom: Classroom): Classroom {
        const copy: Classroom = Object.assign({}, classroom);
        return copy;
    }
}

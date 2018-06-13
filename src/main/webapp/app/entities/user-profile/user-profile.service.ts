import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { UserProfile } from './user-profile.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<UserProfile>;

@Injectable()
export class UserProfileService {

    private resourceUrl =  SERVER_API_URL + 'api/user-profiles';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/user-profiles';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(userProfile: UserProfile): Observable<EntityResponseType> {
        const copy = this.convert(userProfile);
        return this.http.post<UserProfile>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(userProfile: UserProfile): Observable<EntityResponseType> {
        const copy = this.convert(userProfile);
        return this.http.put<UserProfile>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<UserProfile>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<UserProfile[]>> {
        const options = createRequestOption(req);
        return this.http.get<UserProfile[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<UserProfile[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<UserProfile[]>> {
        const options = createRequestOption(req);
        return this.http.get<UserProfile[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<UserProfile[]>) => this.convertArrayResponse(res));
    }
    
    sendFileToServer(file: File): Observable<String> {
        const copy = this.convertToObject(file);
       return this.http.post(this.resourceUrl+'/imageUpload', copy, {responseType: 'text'});
   }
   private convertToObject(file: File): FormData{
    let formData: FormData = new FormData();
    formData.append('file', file);
    return formData;
   }

   private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: UserProfile = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<UserProfile[]>): HttpResponse<UserProfile[]> {
        const jsonResponse: UserProfile[] = res.body;
        const body: UserProfile[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to UserProfile.
     */
    private convertItemFromServer(userProfile: UserProfile): UserProfile {
        const copy: UserProfile = Object.assign({}, userProfile);
        copy.userDob = this.dateUtils
            .convertLocalDateFromServer(userProfile.userDob);
        return copy;
    }

    /**
     * Convert a UserProfile to a JSON which can be sent to the server.
     */
    private convert(userProfile: UserProfile): UserProfile {
        const copy: UserProfile = Object.assign({}, userProfile);
        copy.userDob = this.dateUtils
            .convertLocalDateToServer(userProfile.userDob);
        return copy;
    }
}

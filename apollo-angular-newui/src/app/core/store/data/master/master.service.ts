import {Injectable} from '@angular/core';
import {
    UserCompanyEntityDto,
    UserRequestEntityDto
} from '../../../../../../../apollo-webs/src/app/shared/models/master-data-details';
import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {AllBatchInvoiceDetailsDto, STA10PersonnelDto} from '../qa/qa.model';
import {ApiEndpointService} from '../../../services/endpoints/api-endpoint.service';
import {catchError, map} from 'rxjs/operators';
import {UserDetailsDto, UserEntityDto} from '../users';
import {UserSearchValues} from './master.model';

@Injectable({
    providedIn: 'root'
})
export class MasterService {

    constructor(private http: HttpClient) {
    }


    loadUsers(): Observable<UserEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.USERS_LIST);
        return this.http.get<UserEntityDto[]>(url).pipe(
            map(function (response: UserEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    // loadUsersRequests(): any {
    //   const params = new HttpParams()
    //       .set('page', '0')
    //       .set('page', '20');
    //   return this.http.get<any>(`${this.urlToUsers}load/users-requests`);
    // }

    assignRoleToUser(userId: bigint, roleId: bigint, status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ASSIGN_ROLE_TO_USER);
        const urlAndPathVariables = `${url}${userId}/${roleId}/${status}`;
        return this.http.post<any>(urlAndPathVariables, null).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    revokeRoleFromUser(userId: bigint, roleId: bigint, status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.REVOKE_ROLE_FROM_USER);
        const urlAndPathVariables = `${url}${userId}/${roleId}/${status}`;
        return this.http.post<any>(urlAndPathVariables, null).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    assignCfsToUser(userProfileId: bigint, cfsId: bigint, status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ASSIGN_CFS_TO_USER);
        const urlAndPathVariables = `${url}${userProfileId}/${cfsId}/${status}`;
        return this.http.post<any>(urlAndPathVariables, null).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    revokeCfsFromUser(userProfileId: bigint, cfsId: bigint, status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.REVOKE_CFS_FROM_USER);
        const urlAndPathVariables = `${url}${userProfileId}/${cfsId}/${status}`;
        return this.http.post<any>(urlAndPathVariables, null).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadUserSearch(data: UserSearchValues): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.USER_SEARCH);
        return this.http.post<any>(url, data).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public loadUserDetails(userID: string): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.USER_SELECTED_DETAILS);
        const params = new HttpParams()
            .set('userID', userID);
        return this.http.get<UserDetailsDto>(url, {params}).pipe(
            map(function (response: UserDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public registerEmployee(data: UserEntityDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.USER_CREATE_EMPLOYEE);
        return this.http.post<UserEntityDto>(url, data).pipe(
            map(function (response: UserEntityDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }
}

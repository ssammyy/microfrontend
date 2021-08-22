import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {ApiEndpointService} from '../../../services/endpoints/api-endpoint.service';
import {catchError, map} from 'rxjs/operators';
import {
    AuthoritiesEntityDto,
    CountiesDto, DepartmentDto, DesignationEntityDto, DirectoratesEntityDto, DivisionDetailsDto,
    RegionsEntityDto, RolesEntityDto,
    SectionsEntityDto,
    SubSectionsL1EntityDto,
    SubSectionsL2EntityDto, TitlesEntityDto, TownsDto,
    UserSearchValues
} from './master.model';
import {UserEntityDto} from "../users";

@Injectable({
    providedIn: 'root'
})
export class MasterService {

    constructor(private http: HttpClient) {
    }

    loadDepartmentsSystemAdmin(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_DEPARTMENTS);
        return this.http.get<DepartmentDto[]>(url).pipe(
            map(function (response: DepartmentDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadDivisionsSystemAdmin(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_DIVISIONS);
        return this.http.get<DivisionDetailsDto[]>(url).pipe(
            map(function (response: DivisionDetailsDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadDirectorateSystemAdmin(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_DIRECTORATE);
        return this.http.get<DirectoratesEntityDto[]>(url).pipe(
            map(function (response: DirectoratesEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadTitlesSystemAdmin(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_TITLE_LIST);
        return this.http.get<TitlesEntityDto[]>(url).pipe(
            map(function (response: TitlesEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadRolesSystemAdmin(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_ROLES_LIST);
        return this.http.get<RolesEntityDto[]>(url).pipe(
            map(function (response: RolesEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadAuthoritiesSystemAdmin(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_AUTHORITIES_LIST);
        return this.http.get<AuthoritiesEntityDto[]>(url).pipe(
            map(function (response: AuthoritiesEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadDesignationsSystemAdmin(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_DESIGNATIONS);
        return this.http.get<DesignationEntityDto[]>(url).pipe(
            map(function (response: DesignationEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadSectionSystemAdmin(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_SECTIONS);
        return this.http.get<SectionsEntityDto[]>(url).pipe(
            map(function (response: SectionsEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadL1SubSubSectionSystemAdmin(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_SUB_SECTIONS_L1);
        return this.http.get<SubSectionsL1EntityDto[]>(url).pipe(
            map(function (response: SubSectionsL1EntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadL2SubSubSectionSystemAdmin(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_SUB_SECTIONS_L2);
        return this.http.get<SubSectionsL2EntityDto[]>(url).pipe(
            map(function (response: SubSectionsL2EntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadCfsSystemAdmin(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_FREIGHT_STATIONS);
        return this.http.get<any>(url).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadRegionsSystemAdmin(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_REGIONS);
        return this.http.get<RegionsEntityDto[]>(url).pipe(
            map(function (response: RegionsEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadCountiesSystemAdmin(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_COUNTIES);
        return this.http.get<CountiesDto[]>(url).pipe(
            map(function (response: CountiesDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadTownsSystemAdmin(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_TOWNS);
        return this.http.get<TownsDto[]>(url).pipe(
            map(function (response: TownsDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }


    loadUsers(): Observable<UserEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_USERS_LIST);
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

    loadUsersAssignedRoles(userId: bigint, status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LIST_ACTIVE_RBAC_USERS_ROLES);
        const urlAndPathVariables = `${url}${userId}/${status}`;
        return this.http.get<any>(urlAndPathVariables).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadUsersAssignedCFS(userProfileId: bigint, status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LIST_ACTIVE_RBAC_USERS_CFS);
        const urlAndPathVariables = `${url}${userProfileId}/${status}`;
        return this.http.get<any>(urlAndPathVariables).pipe(
            map(function (response: any) {
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

    revokeRoleFromUser(userId: bigint, roleId: string, status: number): any {
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

    revokeCfsFromUser(userProfileId: bigint, cfsId: string, status: number): any {
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
        return this.http.get<UserEntityDto>(url, {params}).pipe(
            map(function (response: UserEntityDto) {
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

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
    UserSearchValues, UserTypeEntityDto
} from './master.model';
import {UserEntityDto} from "../users";

@Injectable({
    providedIn: 'root'
})
export class MasterService {

    constructor(private http: HttpClient) {
    }

    loadDepartmentsSystemAdmin(status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_DEPARTMENTS_BY_STATUS);
        const urlAndPathVariables = `${url}/${status}`;
        return this.http.get<DepartmentDto[]>(urlAndPathVariables).pipe(
            map(function (response: DepartmentDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadDivisionsSystemAdmin(status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_DIVISIONS_BY_STATUS);
        const urlAndPathVariables = `${url}/${status}`;
        return this.http.get<DivisionDetailsDto[]>(urlAndPathVariables).pipe(
            map(function (response: DivisionDetailsDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadDirectorateSystemAdmin(status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_DIRECTORATE_BY_STATUS);
        const urlAndPathVariables = `${url}/${status}`;
        return this.http.get<DirectoratesEntityDto[]>(urlAndPathVariables).pipe(
            map(function (response: DirectoratesEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadUserTypes(status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_USER_TYPES);
        return this.http.get<UserTypeEntityDto[]>(url).pipe(
            map(function (response: UserTypeEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadTitlesSystemAdmin(status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.TITLE_LIST_BY_STATUS);
        const urlAndPathVariables = `${url}/${status}`;
        return this.http.get<TitlesEntityDto[]>(urlAndPathVariables).pipe(
            map(function (response: TitlesEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadRolesSystemAdmin(status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ROLES_LIST_BY_STATUS);
        const urlAndPathVariables = `${url}/${status}`;
        return this.http.get<RolesEntityDto[]>(urlAndPathVariables).pipe(
            map(function (response: RolesEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadAuthoritiesSystemAdmin(status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.AUTHORITIES_LIST_BY_STATUS);
        const urlAndPathVariables = `${url}/${status}`;
        return this.http.get<AuthoritiesEntityDto[]>(urlAndPathVariables).pipe(
            map(function (response: AuthoritiesEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadDesignationsSystemAdmin(status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_DESIGNATIONS_BY_STATUS);
        const urlAndPathVariables = `${url}/${status}`;
        return this.http.get<DesignationEntityDto[]>(urlAndPathVariables).pipe(
            map(function (response: DesignationEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadSectionSystemAdmin(status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_SECTIONS_BY_STATUS);
        const urlAndPathVariables = `${url}/${status}`;
        return this.http.get<SectionsEntityDto[]>(urlAndPathVariables).pipe(
            map(function (response: SectionsEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadL1SubSubSectionSystemAdmin(status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_SUB_SECTIONS_L1_BY_STATUS);
        const urlAndPathVariables = `${url}/${status}`;
        return this.http.get<SubSectionsL1EntityDto[]>(urlAndPathVariables).pipe(
            map(function (response: SubSectionsL1EntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadL2SubSubSectionSystemAdmin(status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_SUB_SECTIONS_L2_BY_STATUS);
        const urlAndPathVariables = `${url}/${status}`;
        return this.http.get<SubSectionsL2EntityDto[]>(urlAndPathVariables).pipe(
            map(function (response: SubSectionsL2EntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadCfsSystemAdmin(status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_FREIGHT_STATIONS_BY_STATUS);
        const urlAndPathVariables = `${url}/${status}`;
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

    loadRegionsSystemAdmin(status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_REGIONS_BY_STATUS);
        const urlAndPathVariables = `${url}/${status}`;
        return this.http.get<RegionsEntityDto[]>(urlAndPathVariables).pipe(
            map(function (response: RegionsEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadCountiesSystemAdmin(status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_COUNTIES_BY_STATUS);
        const urlAndPathVariables = `${url}/${status}`;
        return this.http.get<CountiesDto[]>(urlAndPathVariables).pipe(
            map(function (response: CountiesDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    loadTownsSystemAdmin(status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_TOWNS_BY_STATUS);
        const urlAndPathVariables = `${url}/${status}`;
        return this.http.get<TownsDto[]>(urlAndPathVariables).pipe(
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
    loadUsersAssignedTypes(userId: bigint, status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LIST_USER_TYPE);
        const urlAndPathVariables = `${url}${userId}`;
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

    loadUsersSectionRoles(userId: bigint, status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LIST_ACTIVE_RBAC_USERS_SECTION);
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

    assignSectionToUser(userId: bigint, sectionId: bigint, status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ASSIGN_SECTION_TO_USER);
        const urlAndPathVariables = `${url}${userId}/${sectionId}/${status}`;
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

    revokeSectionFromUser(userId: bigint, sectionId: string, status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.REVOKE_SECTION_FROM_USER);
        const urlAndPathVariables = `${url}${userId}/${sectionId}/${status}`;
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
    assignUserTypeToUser(userProfileId: bigint, userTypeId: bigint, status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ASSIGN_USER_TYPE_TO_USER);
        const urlAndPathVariables = `${url}${userProfileId}/${userTypeId}/${status}`;
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

    revokeUserTypeFromUser(userProfileId: bigint, userTypeId: string, status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.REVOKE_USER_TYPE_TO_USER);
        const urlAndPathVariables = `${url}${userProfileId}/${userTypeId}/${status}`;
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

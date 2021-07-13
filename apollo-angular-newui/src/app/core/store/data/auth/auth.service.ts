import {Injectable} from '@angular/core';
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {catchError, map} from "rxjs/operators";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {
    CompanyInfoDto,
    LoggedInUser,
    LoginCredentials,
    SendTokenRequestDto,
    ValidateTokenRequestDto
} from "./auth.model";
import {ApiResponse} from "../../../domain/response.model";

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    constructor(private http: HttpClient) {
    }

    public resetUserCredentials(payload: LoginCredentials): Observable<ApiResponse> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.RESET);
        return this.http.post<ApiResponse>(url, payload).pipe(
            map((r: ApiResponse): ApiResponse => r),
            catchError((fault: HttpErrorResponse) => throwError(fault))
        );
    }

    public fetchCompanyDetails(): Observable<CompanyInfoDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SEND_TOKEN_FOR_USER);
        return this.http.post<CompanyInfoDto>(url, null).pipe(
            map((r: CompanyInfoDto): CompanyInfoDto => r),
            catchError((fault: HttpErrorResponse) => throwError(fault))
        );
    }

    public sendTokenForUser(payload: SendTokenRequestDto): Observable<ApiResponse> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SEND_TOKEN_FOR_USER);
        return this.http.post<ApiResponse>(url, payload).pipe(
            map((r: ApiResponse): ApiResponse => r),
            catchError((fault: HttpErrorResponse) => throwError(fault))
        );
    }

    public validateTokenForUser(payload: ValidateTokenRequestDto): Observable<ApiResponse> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.VALIDATE_TOKEN_FOR_USER);
        return this.http.post<ApiResponse>(url, payload).pipe(
            map((r: ApiResponse): ApiResponse => r),
            catchError((fault: HttpErrorResponse) => throwError(fault))
        );
    }

    public logout(): Observable<ApiResponse> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOGOUT_URL);
        return this.http.post<ApiResponse>(url, null).pipe(
            map((r: ApiResponse): ApiResponse => r),
            catchError((fault: HttpErrorResponse) => throwError(fault))
        );
    }

    public login(data: LoginCredentials): Observable<LoggedInUser> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOGIN_PAGE);
        return this.http.post<LoggedInUser>(url, data).pipe(
            map(function (response: LoggedInUser) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }
}

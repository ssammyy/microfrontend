import {Injectable} from '@angular/core';
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {catchError, map} from "rxjs/operators";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {LoggedInUser, LoginCredentials} from "./auth.model";
import {ApiResponse} from "../../../domain/response.model";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {
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

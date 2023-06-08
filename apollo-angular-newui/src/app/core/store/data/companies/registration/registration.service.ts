import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../../services/endpoints/api-endpoint.service";
import {catchError, map} from "rxjs/operators";
import {BrsLookUpRequest, RegistrationPayload, SendTokenToPhone, ValidateTokenAndPhone} from "./registration.models";
import {Company} from "../company";
import {ApiResponse} from "../../../../domain/response.model";

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(private http: HttpClient) {
  }

  public brsValidation(data: BrsLookUpRequest): Observable<Company> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.VALIDATE_BRS);
    return this.http.post<Company>(url, data).pipe(
      map(function (response: Company) {
        return response;
      }),
      catchError((fault: HttpErrorResponse) => {
        // console.warn(`getAllFault( ${fault.message} )`);
        return throwError(fault);
      })
    );
  }

  public sendTokenToPhone(data: SendTokenToPhone): Observable<ApiResponse> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SEND_TOKEN);
    return this.http.post<ApiResponse>(url, data).pipe(
      map(function (response: ApiResponse) {
        return response;
      }),
      catchError((fault: HttpErrorResponse) => {
        // console.warn(`getAllFault( ${fault.message} )`);
        return throwError(fault);
      })
    );
  }

  public registerCompany(data: RegistrationPayload): Observable<ApiResponse> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.REGISTER_COMPANY);
    return this.http.post<ApiResponse>(url, data).pipe(
      map(function (response: ApiResponse) {
        return response;
      }),
      catchError((fault: HttpErrorResponse) => {
        // console.warn(`getAllFault( ${fault.message} )`);
        return throwError(fault);
      })
    );
  }

  public registerTivet(data: RegistrationPayload): Observable<ApiResponse> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.REGISTER_TIVET);
    return this.http.post<ApiResponse>(url, data).pipe(
        map(function (response: ApiResponse) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          // console.warn(`getAllFault( ${fault.message} )`);
          return throwError(fault);
        })
    );
  }

  public validateTokenAndPhone(data: ValidateTokenAndPhone): Observable<ApiResponse> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.VALIDATE_TOKEN);
    return this.http.post<ApiResponse>(url, data).pipe(
      map(function (response: ApiResponse) {
        return response;
      }),
      catchError((fault: HttpErrorResponse) => {
        // console.warn(`getAllFault( ${fault.message} )`);
        return throwError(fault);
      })
    );
  }
}

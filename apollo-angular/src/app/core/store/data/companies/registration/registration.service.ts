import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../../services/endpoints/api-endpoint.service";
import {catchError, map} from "rxjs/operators";
import {BrsLookUpRequest} from "./registration.models";
import {Company} from "../company.model";

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
}

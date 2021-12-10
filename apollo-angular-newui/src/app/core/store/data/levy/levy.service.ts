import { Injectable } from '@angular/core';
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";
import {CompanyModel, ManufactureInfo, ManufacturePenalty, PaidLevy, SLevySL1} from "./levy.model";

@Injectable({
  providedIn: 'root'
})
export class LevyService {

  constructor(private http: HttpClient) { }

  public addSL1Details(sLevySL1: SLevySL1): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_SL10_FORM);
    const params = new HttpParams();
    return this.http.post<SLevySL1>(url, sLevySL1, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
    public getManufacturerPenaltyHistory(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_PENALTY_DETAILS);
        const params = new HttpParams();
        return this.http.get<ManufacturePenalty>(url, {params}).pipe();
    }
    public getPaidLevies(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_PAID_DETAILS);
        const params = new HttpParams();
        return this.http.get<PaidLevy>(url, {params}).pipe();
    }
    public getCompanyProfile(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_COMPANY_DETAILS);
        const params = new HttpParams();
        return this.http.get<CompanyModel>(url, {params}).pipe();
    }

}

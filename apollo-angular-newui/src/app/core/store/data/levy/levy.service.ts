import { Injectable } from '@angular/core';
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";
import {CompanyModel, ManufactureInfo, ManufacturePenalty, PaidLevy} from "./levy.model";

@Injectable({
  providedIn: 'root'
})
export class LevyService {

  constructor(private http: HttpClient) { }

  public addManufactureDetails(manufactureInfo: ManufactureInfo): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.REG_MANUFACTURE_DETAILS);
    const params = new HttpParams();
    return this.http.post<ManufactureInfo>(url, manufactureInfo, {params}).pipe(
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

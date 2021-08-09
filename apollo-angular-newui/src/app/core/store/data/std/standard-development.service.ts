import { Injectable } from '@angular/core';
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Department} from "./std.model";

@Injectable({
  providedIn: 'root'
})
export class StandardDevelopmentService {

  constructor(private http: HttpClient) { }

  public getDepartments(): any {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_GET_DEPARTMENT);
    const params = new HttpParams();
    return this.http.get<Department>(url, {params}).pipe();
  }

public getTechnicalCommittee(id: bigint): any {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_GET_TC_COMMITTEE);
    const params = new HttpParams();
    return this.http.get<any>(url, {params}).pipe();
  }
}

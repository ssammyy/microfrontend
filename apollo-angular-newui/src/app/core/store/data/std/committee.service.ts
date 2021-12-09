import {Injectable} from '@angular/core';
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class CommitteeService {

  protocol = `https://`;
  baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV

  private apiServerUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/committee/`;

  constructor(private http: HttpClient) {
  }

  public getAllNWIS(): any {
    return this.http.get<any>(`${this.apiServerUrl}getAllNwis`)
  }

}

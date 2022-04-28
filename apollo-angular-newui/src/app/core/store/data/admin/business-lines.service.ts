import {Injectable} from '@angular/core';
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient} from "@angular/common/http";
import {BusinessLines} from "../business";

@Injectable({
  providedIn: 'root'
})
export class BusinessLinesService {
  protocol = `https://`;
  baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV

  private apiServerUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/admin/`

  constructor(private http: HttpClient) {
  }

  public getAllBusinessLines(): any {
    return this.http.get<BusinessLines[]>(`${this.apiServerUrl}` + 'getAllBusinessLines')

  }
}

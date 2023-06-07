import {Injectable} from '@angular/core';
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {BusinessNatures} from "../business";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class BusinessNatureService {
  protocol = `https://`;
  baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV

  private apiServerUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/admin/`

  constructor(private http: HttpClient) {
  }

  public getAllBusinessNature(): any {
    return this.http.get<BusinessNatures[]>(`${this.apiServerUrl}` + 'getAllBusinessNature')

  }

}

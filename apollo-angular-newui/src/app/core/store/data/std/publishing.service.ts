import {Injectable} from '@angular/core';
import {StandardDraft} from "./request_std.model";
import {Observable} from "rxjs";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class PublishingService {
  protocol = `https://`;
  baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV
  private apiPublishingUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/publishing/`;

  constructor(private http: HttpClient) {
  }

  public uploadStdDraft(uploadStdDraft: StandardDraft): Observable<any> {
    return this.http.post<StandardDraft>(`${this.apiPublishingUrl}` + 'submit', uploadStdDraft)
  }
}

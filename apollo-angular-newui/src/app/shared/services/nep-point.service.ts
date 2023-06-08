import { Injectable } from '@angular/core';
import {DepartmentResponse, FeedbackEmail, InfoAvailableYes, RootObject} from "../models/standard-development";
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class NepPointService {

  private nep_notification_url = 'http://localhost:8080/National_enquiry_point/';
  constructor(private http: HttpClient) { }
  public sendGetRequest(): any{
    return this.http.get<Notification[]>(`${this.nep_notification_url}` + 'nep_officer/tasks');
  }
  public sendGetRequestDivision(): any{
    return this.http.get<Notification[]>(`${this.nep_notification_url}` + 'division/tasks');
  }

  public makeEnquiry(rootObject: RootObject): Observable<RootObject>{
    return this.http.post<RootObject>(`${this.nep_notification_url}` + 'notification_request', rootObject);
  }

  public infoAvailableYes(infoAvailableYes: InfoAvailableYes): Observable<InfoAvailableYes>{
    return this.http.post<InfoAvailableYes>(`${this.nep_notification_url}` + 'nep_officer/is_available', infoAvailableYes);
  }
  public reviewTasks(infoAvailableYes: InfoAvailableYes): Observable<InfoAvailableYes>{
    return this.http.post<InfoAvailableYes>(`${this.nep_notification_url}` + 'nep_officer/is_available', infoAvailableYes);
  }
  public deptResponse(departmentResponse: DepartmentResponse): Observable<DepartmentResponse>{
    return this.http.post<DepartmentResponse>(`${this.nep_notification_url}` + 'division_response/send_response', departmentResponse);
  }
  public feedbackEmail(feedbackEmail: FeedbackEmail): Observable<FeedbackEmail>{
    return this.http.post<FeedbackEmail>(`${this.nep_notification_url}` + 'information_available/send_email', feedbackEmail);
  }
}

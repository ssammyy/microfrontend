import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {
  Product,
  StandardRequest,
  StandardTasks,
  Stdtsectask,
  TechnicalCommittee,
  Department,
  StdTCTask,
  RootObject,
  InfoAvailableYes,
  DepartmentResponse,
  FeedbackEmail
} from "../models/standard-development";

@Injectable({
  providedIn: 'root'
})
export class StandardDevelopmentService {

  private apiServerUrl = 'http://localhost:8080/standard/';
  private nep_notification_url = 'http://localhost:8080/National_enquiry_point/';
  constructor(private http: HttpClient) { }

  public getStandards(): any{
    return this.http.get<Product[]>(`${this.apiServerUrl}`+'getProducts')
  }

  public getDepartments(): any{
    return this.http.get<Department[]>(`${this.apiServerUrl}`+'getDepartments')
  }

  public getTechnicalCommittee(): any{
    return this.http.get<TechnicalCommittee[]>(`${this.apiServerUrl}`+'getTechnicalCommittee')
  }

  public getProductSubcategory(id: bigint): any{
    return this.http.get<any>(`${this.apiServerUrl}getProductCategories/${id}`)
  }

  public addStandardRequest(standardRequest: StandardRequest) : Observable<StandardRequest>{
    return this.http.post<StandardRequest>(`${this.apiServerUrl}`+'request',standardRequest)

  }
  public getHOFTasks(): Observable<StandardTasks[]>{
    return this.http.get<StandardTasks[]>(`${this.apiServerUrl}`+'getHOFTasks')
  }


  public reviewTask(reviewTask: StandardTasks): Observable<StandardTasks>{
    return this.http.post<StandardTasks>(`${this.apiServerUrl}`+'hof/review', reviewTask)
  }
  public getTCSECTasks(): Observable<Stdtsectask[]>{
    return this.http.get<Stdtsectask[]>(`${this.apiServerUrl}`+'getTCSECTasks')
  }
  public uploadNWI(uploadNWI: Stdtsectask): Observable<Stdtsectask>{
    return this.http.post<Stdtsectask>(`${this.apiServerUrl}`+'uploadNWI', uploadNWI)
  }
  public getTCTasks(): Observable<StdTCTask[]>{
    return this.http.get<StdTCTask[]>(`${this.apiServerUrl}`+'getTCTasks')
  }
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

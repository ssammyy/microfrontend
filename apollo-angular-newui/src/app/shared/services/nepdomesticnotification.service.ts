import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent, HttpRequest} from "@angular/common/http";
import {InfoAvailableNo, InfoAvailableYes, Product} from "../models/standard-development";
import {DraftNotification, finalSubmit, InboundNotification, NepNotification} from "../models/nepNotification";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class NepdomesticnotificationService {

  private apiServerUrl = 'http://localhost:8080/Domestic_notification/';
  private baseUrl = 'http://localhost:8080';
  constructor(private http: HttpClient) { }

  public getNotifications(): any{
    return this.http.get<NepNotification[]>(`${this.apiServerUrl}`+'nep_officer/tasks')
  }
  public getManagerNotifications(): any{
    return this.http.get<InboundNotification[]>(`${this.apiServerUrl}`+'manager/tasks')
  }

  public reviewTasks(infoAvailableYes: InfoAvailableYes): Observable<InfoAvailableYes>{
    return this.http.post<InfoAvailableYes>(`${this.apiServerUrl}` + 'nep_officer/is_accepted', infoAvailableYes);
  }
  public submitNo(infoAvailableNo: InfoAvailableNo): Observable<InfoAvailableNo>{
    return this.http.post<InfoAvailableNo>(`${this.apiServerUrl}` + 'nep_officer/is_accepted', infoAvailableNo);
  }
  public submitFinal(infoAvailableNo: finalSubmit): Observable<finalSubmit>{
    return this.http.post<finalSubmit>(`${this.apiServerUrl}` + 'nep_officer/upload_final', infoAvailableNo);
  }
  public managerAccept(infoAvailableNo: InfoAvailableNo): Observable<InfoAvailableNo>{
    return this.http.post<InfoAvailableNo>(`${this.apiServerUrl}` + 'manager/is_accepted', infoAvailableNo);
  }

  //file upload
  public uploadFile(draftNotification: DraftNotification): Observable<DraftNotification>{
    return this.http.post<DraftNotification>(`${this.apiServerUrl}` + 'nep_officer/draft_notification', draftNotification);
  }

  upload(file: File): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();

    formData.append('file', file);

    const req = new HttpRequest('POST', `${this.baseUrl}/upload`, formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  getFiles(): Observable<any> {
    return this.http.get(`${this.baseUrl}/files`);
  }

}

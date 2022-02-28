import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Committee_Draft, Preliminary_Draft} from "../../../shared/models/committee_module";

@Injectable({
  providedIn: 'root'
})
export class CommitteeDraftService {

  private baseUrl = 'http://localhost:8080/committee/';


  constructor(private http: HttpClient) {
  }
  getCommitteeDraft(id: number | undefined): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  createCommitteeDraft(committee_draft: Committee_Draft): Observable<Committee_Draft> {
    return this.http.post<Committee_Draft>(`${this.baseUrl}`+'prepareCD', committee_draft);
  }


  public getPDS(): any{
    return this.http.get<Committee_Draft[]>(`${this.baseUrl}`+'getPds')
  }

  updateCommitteeDraft(id: number, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrl}/${id}`, value);
  }

  deleteCommitteeDraft(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, {responseType: 'text'});
  }

  getCommitteeDraftList(): Observable<any> {
    return this.http.get<Committee_Draft[]>(`${this.baseUrl}`+'getCds')
  }

}

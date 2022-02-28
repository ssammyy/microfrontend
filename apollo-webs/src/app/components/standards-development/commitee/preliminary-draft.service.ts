import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Department, Product} from "../../../shared/models/standard-development";
import {Preliminary_Draft} from "../../../shared/models/committee_module";

@Injectable({
  providedIn: 'root'
})
export class PreliminaryDraftService {
  private baseUrl = 'http://localhost:8080/committee/';


  constructor(private http: HttpClient) {
  }

  getPreliminaryDraft(id: number | undefined): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  createPreliminaryDraft(preliminary_draft: Preliminary_Draft): Observable<Preliminary_Draft> {
    return this.http.post<Preliminary_Draft>(`${this.baseUrl}`+'preparePD', preliminary_draft);
  }


  public getNWIS(): any{
    return this.http.get<Preliminary_Draft[]>(`${this.baseUrl}`+'getnwis')
  }

  updatePreliminaryDraft(id: number, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrl}/${id}`, value);
  }

  deletePreliminaryDraft(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, {responseType: 'text'});
  }

  getPreliminaryDraftList(): Observable<any> {
    return this.http.get<Preliminary_Draft[]>(`${this.baseUrl}`+'getPds')
  }
}

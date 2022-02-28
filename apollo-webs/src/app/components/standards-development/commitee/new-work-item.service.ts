import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import { HttpClient } from '@angular/common/http';

import {TaskData} from "../../../shared/models/committee_module";

@Injectable({
  providedIn: 'root'
})
export class NewWorkItemService {

  private baseUrl = 'http://localhost:8080/committee/';


  constructor(private http: HttpClient) {
  }

  getNWI(id: number | undefined): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  createNWI(taskData: TaskData): Observable<TaskData> {
    return this.http.post<TaskData>(`${this.baseUrl}` + 'prepareNWI', taskData);

  }

  updateNWI(id: number, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrl}/${id}`, value);
  }

  deleteNWI(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, {responseType: 'text'});
  }

  getNWIList(): Observable<any> {
    return this.http.get(`${this.baseUrl}`+ 'getnwis');
  }
}

import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Stdtsectask} from "../../components/standards-development/std-tsc-sec-tasks/stdtsectask";

@Injectable({
  providedIn: 'root'
})
export class StdTscSecTasksService {

  private apiServerUrl = 'http://localhost:8080/standard/';
  constructor(private http: HttpClient) { }

  public getTCSECTasks(): Observable<Stdtsectask[]>{
    return this.http.get<Stdtsectask[]>(`${this.apiServerUrl}`+'getTCSECTasks')
  }
}

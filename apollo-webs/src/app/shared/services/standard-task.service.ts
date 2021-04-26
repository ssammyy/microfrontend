import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import { StandardTasks} from "../../components/standards-development/standard-task/standardtasks";

@Injectable({
  providedIn: 'root'
})
export class StandardTaskService {

  private apiServerUrl = 'http://localhost:8080/standard/';
  constructor(private http: HttpClient) { }

  public getHOFTasks(): Observable<StandardTasks[]>{
    return this.http.get<StandardTasks[]>(`${this.apiServerUrl}`+'getHOFTasks')
  }


  public reviewTask(reviewTask: StandardTasks): Observable<StandardTasks>{
    return this.http.post<StandardTasks>(`${this.apiServerUrl}`+'hof/review', reviewTask)
  }

}

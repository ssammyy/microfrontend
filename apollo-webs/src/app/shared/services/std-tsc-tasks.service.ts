import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class StdTscTasksService {

  private apiServerUrl = 'http://localhost:8080/standard/';
  constructor(private http: HttpClient) { }


}

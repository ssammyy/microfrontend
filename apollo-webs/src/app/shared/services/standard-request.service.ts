import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {dev} from '../dev/std-dev';
import {Observable} from "rxjs";
import {Product, StandardRequest} from "../../components/standards-development/standard-request/standardrequest";

@Injectable({
  providedIn: 'root'
})

export class StandardRequestService {

  private apiServerUrl = 'http://localhost:8080/standard/';

  constructor(private http: HttpClient) { }

  public getStandards(): Observable<Product[]>{
    return this.http.get<Product[]>(`${this.apiServerUrl}`+'getProducts')
  }

  public addStandardRequest(standardRequest: StandardRequest) : Observable<StandardRequest>{
    return this.http.post<StandardRequest>(`${this.apiServerUrl}`+'request',standardRequest)

  }
}

import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {dev} from '../dev/std-dev';
import {Observable} from "rxjs";
import {
  Department,
  Product,
  StandardRequest, TechnicalCommittee
} from "../../components/standards-development/standard-request/standardrequest";


@Injectable({
  providedIn: 'root'
})

export class StandardRequestService {

  private apiServerUrl = 'http://localhost:8080/standard/';

  constructor(private http: HttpClient) { }

  public getStandards(): Observable<Product[]>{
    return this.http.get<Product[]>(`${this.apiServerUrl}`+'getProducts')
  }

  public getDepartments(): Observable<Department[]>{
    return this.http.get<Department[]>(`${this.apiServerUrl}`+'getDepartments')
  }

  public getTechnicalCommittee(): Observable<TechnicalCommittee[]>{
    return this.http.get<TechnicalCommittee[]>(`${this.apiServerUrl}`+'getTechnicalCommittee')
  }

  public addStandardRequest(standardRequest: StandardRequest) : Observable<StandardRequest>{
    return this.http.post<StandardRequest>(`${this.apiServerUrl}`+'request',standardRequest)

  }
}

import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Product, StandardRequest, StandardTasks, Stdtsectask, TechnicalCommittee,Department,StdTCTask} from "../models/standard-development";

@Injectable({
  providedIn: 'root'
})
export class StandardDevelopmentService {

  private apiServerUrl = 'http://localhost:8080/standard/';
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
}

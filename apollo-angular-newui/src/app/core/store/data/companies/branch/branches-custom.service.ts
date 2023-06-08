import {Injectable} from '@angular/core';
import {DefaultDataService, HttpUrlGenerator, Logger} from "@ngrx/data";
import {Branches} from "./branches.model";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {select, Store} from "@ngrx/store";
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../../services/endpoints/api-endpoint.service";
import {catchError, map} from "rxjs/operators";
import {selectCompanyIdData} from "../companies.selectors";
import {Update} from "@ngrx/entity";

@Injectable({
  providedIn: 'root'
})
export class BranchesCustomService extends DefaultDataService<Branches> {
  baseUrl = `${ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.COMPANY_LIST)}`;

  constructor(http: HttpClient,
              httpUrlGenerator: HttpUrlGenerator,
              logger: Logger,
              private store$: Store<any>
  ) {
    super('Branches', http, httpUrlGenerator);
  }

  add(entity: Branches): Observable<Branches> {


    let myUrl = '';
    let data = 0;
    this.store$.pipe(select(selectCompanyIdData)).subscribe(
      (d) => {
        if (d) return data = d; else return throwError('Invalid request, Company id is required inside');
      });


    if (data) {
      myUrl = `${this.baseUrl}${data}/branches/`;
      // console.log(`Revised url = ${baseUrl}${data}/branches/ `)
      return this.http.post<Branches>(myUrl, entity).pipe(
        map((response: Branches) => {
          super.add(response)
          return response;
        }),
        catchError((fault: HttpErrorResponse) => throwError(fault))
      );
    } else return throwError('Invalid request, Organization id is required outside');
    // return super.add(entity);
  }

  update(update: Update<Branches>): Observable<Branches> {


    let myUrl = '';
    let data = 0;
    this.store$.pipe(select(selectCompanyIdData)).subscribe(
      (d) => {
        if (d) return data = d; else return throwError('Invalid request, Company id is required inside');
      });


    if (data) {
      myUrl = `${this.baseUrl}${data}/branches/${update.id}`;
      // console.log(`Revised url = ${baseUrl}${data}/branches/ `)
      return this.http.put<Branches>(myUrl, update.changes).pipe(
        map((response: Branches) => {
          update = {...update, ...response};
          super.update(update);
          return response;
        }),
        catchError((fault: HttpErrorResponse) => throwError(fault))
      );
    } else return throwError('Invalid request, Organization id is required outside');

    // return super.update(update);
  }


  getAll(): Observable<Branches[]> {


    let myUrl = '';
    let data = 0;
    this.store$.pipe(select(selectCompanyIdData)).subscribe(
      (d) => {
        if (d) return data = d; else return throwError('Invalid request, Company id is required');
      });


    if (data) {
      myUrl = `${this.baseUrl}${data}/branches/`;
      // console.log(`Revised url = ${baseUrl}${data}/branches/ `)
      return this.http.get<Branches[]>(myUrl).pipe(
        map((response: Branches[]) => response),
        catchError((fault: HttpErrorResponse) => throwError(fault))
      );
    } else return throwError('Invalid request, Organization id is required');

  }
}

import {Injectable} from '@angular/core';
import {DefaultDataService, HttpUrlGenerator, Logger} from '@ngrx/data';
import {selectBranchIdData, selectCompanyIdData} from '../companies';
import {ApiEndpointService} from '../../../services/endpoints/api-endpoint.service';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {select, Store} from '@ngrx/store';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {Update} from '@ngrx/entity';
import {User} from './user.model';

@Injectable({
  providedIn: 'root'
})
export class UsersCustomService extends DefaultDataService<User> {
  baseUrl = `${ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.COMPANY_LIST)}`;

  constructor(http: HttpClient,
              httpUrlGenerator: HttpUrlGenerator,
              logger: Logger,
              private store$: Store<any>
  ) {
    super('User', http, httpUrlGenerator);
  }

  add(entity: User): Observable<User> {


    let myUrl = '';
    let data = 0;
    let branch = 0;
    this.store$.pipe(select(selectCompanyIdData)).subscribe(
      (d) => {
        if (d) { return data = d; } else { return throwError('Invalid request, Company id is required inside'); }
      });
    this.store$.pipe(select(selectBranchIdData)).subscribe(
      (d) => {
        if (d) { return branch = d; } else { return throwError('Invalid request, Company id is required inside'); }
      });


    if (data) {
      myUrl = `${this.baseUrl}${data}/branches/${branch}/users`;
      // console.log(`Revised url = ${baseUrl}${data}/branches/ `)
      return this.http.post<User>(myUrl, entity).pipe(
        map((response: User) => {
          super.add(response);
          return response;
        }),
        catchError((fault: HttpErrorResponse) => throwError(fault))
      );
    } else { return throwError('Invalid request, Organization id is required outside'); }
    // return super.add(entity);
  }

  update(update: Update<User>): Observable<User> {


    let myUrl = '';
    let data = 0;
    let branch = 0;
    this.store$.pipe(select(selectCompanyIdData)).subscribe(
      (d) => {
        if (d) { return data = d; } else { return throwError('Invalid request, Company id is required inside'); }
      });
    this.store$.pipe(select(selectBranchIdData)).subscribe(
      (d) => {
        if (d) { return branch = d; } else { return throwError('Invalid request, Company id is required inside'); }
      });


    if (data) {
      myUrl = `${this.baseUrl}${data}/branches/${branch}/users/${update.id}`;
      // console.log(`Revised url = ${baseUrl}${data}/branches/ `)
      return this.http.put<User>(myUrl, update.changes).pipe(
        map((response: User) => {
          update = {...update, ...response};
          super.update(update);
          return response;
        }),
        catchError((fault: HttpErrorResponse) => throwError(fault))
      );
    } else { return throwError('Invalid request, Organization id is required outside'); }

    // return super.update(update);
  }


  getAll(): Observable<User[]> {


    let myUrl = '';
    let company = 0;
    let branch = 0;
    this.store$.pipe(select(selectCompanyIdData)).subscribe(
      (d) => {
        if (d) { return company = d; } else { return throwError('Invalid request, Company id is required'); }
      });
    this.store$.pipe(select(selectBranchIdData)).subscribe(
      (d) => {
        if (d) { return branch = d; } else { return throwError('Invalid request, Company id is required inside'); }
      });


    if (company) {
      myUrl = `${this.baseUrl}${company}/branches/${branch}/users`;
      // console.log(`Revised url = ${baseUrl}${data}/branches/ `)
      return this.http.get<User[]>(myUrl).pipe(
        map((response: User[]) => response),
        catchError((fault: HttpErrorResponse) => throwError(fault))
      );
    } else { return throwError('Invalid request, Organization id is required'); }

  }
}

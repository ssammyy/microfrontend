import {Injectable} from '@angular/core';
import {DefaultDataService, HttpUrlGenerator, Logger} from "@ngrx/data";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {select, Store} from "@ngrx/store";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {Town} from "./town.model";
import {selectCountyIdData} from "./town.selectors";

@Injectable({
  providedIn: 'root'
})
export class TownCustomService extends DefaultDataService<Town> {
  baseUrl = `${ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.COUNTY_LIST)}`;

  constructor(http: HttpClient,
              httpUrlGenerator: HttpUrlGenerator,
              logger: Logger,
              private store$: Store<any>
  ) {
    super('Town', http, httpUrlGenerator);
  }


  getAll(): Observable<Town[]> {


    let myUrl = '';
    let data = 0;
    this.store$.pipe(select(selectCountyIdData)).subscribe(
      (d) => {
        if (d) return data = d; else return throwError('Invalid request, Company id is required');
      });


    if (data) {
      myUrl = `${this.baseUrl}${data}/towns/`;
      console.log(`Revised url = ${this.baseUrl}${data}/towns/`)
      return this.http.get<Town[]>(myUrl).pipe(
        map((response: Town[]) => response),
        catchError((fault: HttpErrorResponse) => throwError(fault))
      );
    } else return throwError('Invalid request, Organization id is required');

  }
}

import {Injectable} from '@angular/core';
import {
  DefaultDataService,
  DefaultHttpUrlGenerator,
  DefaultPluralizer,
  HttpMethods,
  HttpUrlGenerator,
  Logger
} from "@ngrx/data";
import {Branches} from "./branches.model";
import {HttpClient} from "@angular/common/http";
import {select, Store} from "@ngrx/store";
import {Observable} from "rxjs";
import {pluralNames} from "../../../entity-store.module";
import {ApiEndpointService} from "../../../../services/endpoints/api-endpoint.service";
import {first, map} from "rxjs/operators";
import {selectCompanyIdData} from "../companies.selectors";

@Injectable({
  providedIn: 'root'
})
export class BranchesCustomService extends DefaultDataService<Branches> {

  // @ts-ignore
  constructor(http: HttpClient,
              httpUrlGenerator: HttpUrlGenerator,
              logger: Logger,
              private store$: Store<any>
  ) {
    // super('Application', http, httpUrlGenerator);
    logger.log(http, httpUrlGenerator);
    const url = new DefaultHttpUrlGenerator(new DefaultPluralizer([pluralNames]));
    url.registerHttpResourceUrls({Label: {entityResourceUrl: 'branches', collectionResourceUrl: 'branches'}});
    logger.log('Created custom ApplicationService');
    super('Branches', http, url);
  }

  protected execute(method: HttpMethods, url: string, data?: any, options?: any): Observable<any> {
    const baseUrl = `${ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.COMPANY_LIST)}`;
    this.store$.pipe(select(selectCompanyIdData)).pipe(
      map((d) => {
        if (d) {
          if (method === 'GET' || method === 'PUT') {
            url = `${baseUrl}/${d}/branches`;
            console.log(`Revised url = ${url}`)
            return url
          }
        }
        return url
      }),
      first());


    return super.execute(method, url, data, options);
  }
}

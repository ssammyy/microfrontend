import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Store} from "@ngrx/store";
import {HandleErrorService} from "../services/errors/handle-error.service";
import {ApiEndpointService} from "../services/endpoints/api-endpoint.service";
import {Observable, of, throwError} from "rxjs";
import {catchError, first, mergeMap} from "rxjs/operators";

import {Injectable} from "@angular/core";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private store$: Store<any>, private error: HandleErrorService) {
  }

  /**
   * Intercepts all HTTP requests and adds the JWT token to the request's header if the URL
   * is a REST endpoint and not login or logout.
   */
  public intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const isApiEndpoint: boolean = ApiEndpointService.isApiEndpoint(request.url);
    const isAuthEndpoint: boolean = ApiEndpointService.isAuthEndpoint(request.url);

    // NOTE: Only add the auth token to non-Auth REST endpoints.
    if (isApiEndpoint && !isAuthEndpoint) {
      return this.addToken(request).pipe(
        first(),
        mergeMap((requestWithToken: HttpRequest<any>) => next.handle(requestWithToken)
          .pipe(
            catchError((err: HttpErrorResponse) => {
              const errorMsg = (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.message}`;
              // console.log(errorMsg);
              this.error.handleError(err);
              return throwError(errorMsg);
            })
          )
        )
      );
    } else {
      return next.handle(request).pipe(
        catchError((err: HttpErrorResponse) => {
          // if ([401, 403].indexOf(err.status) !== -1) {
          //   // auto logout if 401 Unauthorized or 403 Forbidden response returned from api
          //   this.store$.dispatch(fromState.navigateToLogin());
          //   location.reload();
          // }
          // console.log(`The response code is ${err.status}`);
          const errorMsg = (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.message}`;
          // console.log(errorMsg);
          this.error.handleError(err);
          return throwError(errorMsg);
        })
      );
    }
  }

  /**
   * Adds the JWT token to the request's header.
   */
  private addToken(request: HttpRequest<any>): Observable<HttpRequest<any>> {
    return of(request);
    // // @ts-ignore
    // return this.store$
    //   .pipe(
    //     select({'selectResponseDataStr'}),
    //     switchMap((token: string) => {
    //       if (!token) {
    //         // console.warn(`addToken( Invalid token!!! Cannot use token "${token}" for endpoint: ${request.url} ).`);
    //       } else {
    //         request = request.clone({
    //           headers: request.headers.set('Authorization', `${token}`),
    //           // TODO: revert to true when going live
    //           withCredentials: false
    //         });
    //       }
    //       return of(request);
    //     })
    //   );
  }
}

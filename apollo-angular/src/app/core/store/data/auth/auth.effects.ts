import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {loadAuths, loadAuthsSuccess, loadLogout, loadLogoutSuccess} from "./auth.actions";
import {Observable, of} from "rxjs";
import {Action} from "@ngrx/store";
import {HttpErrorResponse} from "@angular/common/http";
import {catchError, mergeMap, switchMap} from "rxjs/operators";
import {Go} from "../route";
import {AuthService} from "./auth.service";
import {loadResponsesFailure} from "../response";


@Injectable()
export class AuthEffects {

  doLogout: Observable<Action> = createEffect(
    () =>
      this.actions$.pipe(
        ofType(loadLogout),
        switchMap((action) => this.service.logout()
          .pipe(
            mergeMap((data) => {
              return [
                loadLogoutSuccess({data: data}),
                Go({payload: null, link: action.loginUrl, redirectUrl: ''})
              ];
            }),
            catchError(
              (err: HttpErrorResponse) => {
                return of(Go({payload: err, link: action.loginUrl, redirectUrl: ''}));
                // return of(loadResponsesFailure({
                //   error: {
                //     payload: err.error,
                //     status: err.status,
                //     response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                //   }
                // }));
              })
          )
        )
      ),
    {dispatch: true}
  );


  doLogin: Observable<Action> = createEffect(
    () =>
      this.actions$.pipe(
        ofType(loadAuths),
        switchMap((action) => this.service.login(action.payload)
          .pipe(
            mergeMap((data) => {
              return [
                loadAuthsSuccess({profile: data, loggedIn: true}),
                Go({payload: action.redirectUrl, link: action.redirectUrl, redirectUrl: action.redirectUrl})
              ];
            }),
            catchError(
              (err: HttpErrorResponse) => of(loadResponsesFailure({
                error: {
                  payload: err.error,
                  status: err.status,
                  response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                }
              })))
          )
        )
      ),
    {dispatch: true}
  );

  constructor(
    private actions$: Actions,
    private service: AuthService
  ) {
  }


}

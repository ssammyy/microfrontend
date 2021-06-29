import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {
  doSendTokenForUser,
  doSendTokenForUserSuccess,
  doValidateTokenForUser,
  doValidateTokenForUserSuccess,
  loadAuths,
  loadAuthsSuccess,
  loadLogout,
  loadLogoutSuccess,
  loadResetAuths
} from "./auth.actions";
import {Observable, of} from "rxjs";
import {Action} from "@ngrx/store";
import {HttpErrorResponse} from "@angular/common/http";
import {catchError, mergeMap, switchMap} from "rxjs/operators";
import {Go} from "../route";
import {AuthService} from "./auth.service";
import {loadResponsesFailure, loadResponsesSuccess} from "../response";


@Injectable()
export class AuthEffects {

  loadResetAuths: Observable<Action> = createEffect(
    () =>
      this.actions$.pipe(
        ofType(loadResetAuths),
        switchMap((action) => this.service.resetUserCredentials(action.payload)
          .pipe(
            mergeMap((data) => {
              if (data.status == 200) {
                return [
                  loadResponsesSuccess({message: data}),
                  Go({payload: null, link: 'login', redirectUrl: action.redirectUrl})
                ];
              } else {
                return [
                  loadResponsesFailure({error: data})
                ];
              }

            }),
            catchError(
              (err: HttpErrorResponse) => {
                return of(loadResponsesFailure({
                  error: {
                    payload: err.error,
                    status: err.status,
                    response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                  }
                }));
              })
          )
        )
      ),
    {dispatch: true}
  );
  doSendTokenForUser: Observable<Action> = createEffect(
    () =>
      this.actions$.pipe(
        ofType(doSendTokenForUser),
        switchMap((action) => this.service.sendTokenForUser(action.payload)
          .pipe(
            mergeMap((data) => {
              if (data.status == 200) {
                return [
                  doSendTokenForUserSuccess({data: data, otpSent: true}),
                  loadResponsesSuccess({message: data})
                ];
              } else {
                return [
                  loadResponsesFailure({error: data})
                ];
              }

            }),
            catchError(
              (err: HttpErrorResponse) => {
                return of(loadResponsesFailure({
                  error: {
                    payload: err.error,
                    status: err.status,
                    response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                  }
                }));
              })
          )
        )
      ),
    {dispatch: true}
  );

  doValidateTokenForUser: Observable<Action> = createEffect(
    () =>
      this.actions$.pipe(
        ofType(doValidateTokenForUser),
        switchMap((action) => this.service.validateTokenForUser(action.payload)
          .pipe(
            mergeMap((data) => {
              if (data.status == 200) {
                return [
                  doValidateTokenForUserSuccess({data: data, validated: true}),
                  loadResponsesSuccess({message: data})
                ];
              } else {
                return [
                  loadResponsesFailure({error: data})
                ];
              }
            }),
            catchError(
              (err: HttpErrorResponse) => {
                return of(loadResponsesFailure({
                  error: {
                    payload: err.error,
                    status: err.status,
                    response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                  }
                }));
              })
          )
        )
      ),
    {dispatch: true}
  );

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

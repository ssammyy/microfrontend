import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action} from "@ngrx/store";
import {Observable, of} from "rxjs";
import {loadBrsValidations, loadBrsValidationsSuccess} from "./registration.actions";
import {catchError, mergeMap, switchMap} from "rxjs/operators";
import {RegistrationService} from "./registration.service";
import {loadResponsesFailure} from "../../response";
import {HttpErrorResponse} from "@angular/common/http";


@Injectable()
export class RegistrationEffects {


  doBrsValidation: Observable<Action> = createEffect(
    () =>
      this.actions$.pipe(
        ofType(loadBrsValidations),
        switchMap((action) => this.service.brsValidation(action.payload)
          .pipe(
            mergeMap((data) => [
              loadBrsValidationsSuccess({data: data, step: 1})
            ]),
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
    private service: RegistrationService
  ) {
  }

}

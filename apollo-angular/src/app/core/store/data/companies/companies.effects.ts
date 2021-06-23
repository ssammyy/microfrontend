import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Observable} from "rxjs";
import {Action} from "@ngrx/store";
import {switchMap} from "rxjs/operators";
import {
  loadBranchId,
  loadBranchIdSuccess,
  loadCompanyId,
  loadCompanyIdSuccess,
  loadDirectorId,
  loadDirectorIdSuccess,
  loadUserId,
  loadUserIdSuccess
} from "./companies.actions";


@Injectable()
export class CompaniesEffects {


  loadCompanyId: Observable<Action> = createEffect(
    () =>
      this.actions$.pipe(
        ofType(loadCompanyId),
        switchMap((action) => {
            return [
              loadCompanyIdSuccess({companyId: action.payload})
            ];
          }
        )
      ),
    {dispatch: true}
  );
  loadBranchId: Observable<Action> = createEffect(
    () =>
      this.actions$.pipe(
        ofType(loadBranchId),
        switchMap((action) => {
            return [
              loadBranchIdSuccess({branchId: action.payload})
            ];
          }
        )
      ),
    {dispatch: true}
  );
  loadDirectorId: Observable<Action> = createEffect(
    () =>
      this.actions$.pipe(
        ofType(loadDirectorId),
        switchMap((action) => {
            return [
              loadDirectorIdSuccess({directorId: action.payload})
            ];
          }
        )
      ),
    {dispatch: true}
  );
  loadUserId: Observable<Action> = createEffect(
    () =>
      this.actions$.pipe(
        ofType(loadUserId),
        switchMap((action) => {
            return [
              loadUserIdSuccess({userId: action.payload})
            ];
          }
        )
      ),
    {dispatch: true}
  );

  constructor(private actions$: Actions) {
  }

}

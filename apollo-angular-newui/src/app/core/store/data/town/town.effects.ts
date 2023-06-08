import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Observable} from "rxjs";
import {Action} from "@ngrx/store";
import {switchMap} from "rxjs/operators";
import {loadCountyId, loadCountyIdSuccess} from "./town.actions";


@Injectable()
export class TownEffects {


  loadCountyId: Observable<Action> = createEffect(
    () =>
      this.actions$.pipe(
        ofType(loadCountyId),
        switchMap((action) => {
            return [
              loadCountyIdSuccess({countyId: action.payload})
            ];
          }
        )
      ),
    {dispatch: true}
  );

  constructor(private actions$: Actions) {
  }

}

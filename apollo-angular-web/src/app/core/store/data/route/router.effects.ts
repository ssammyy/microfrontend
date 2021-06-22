import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Observable} from "rxjs";
import {Router} from "@angular/router";
import {Go} from "./router.actions";
import {map, tap} from "rxjs/operators";
import {Action} from "@ngrx/store";


@Injectable()
export class RouterEffects {


  /**
   * Navigate the user to the requested route AND preserve the query string params.
   */
  navigate$: Observable<Action> = createEffect(() => this.actions$.pipe(
    ofType(Go),
    map((action) => action),
    tap((data) => {
      // const extras: NavigationExtras = {state: data.payload};
      this.router.navigate([data.link], {queryParams: {returnUrl: data.payload}});
    })
  ), {dispatch: false});

  constructor(
    private actions$: Actions,
    private router: Router) {
  }

}

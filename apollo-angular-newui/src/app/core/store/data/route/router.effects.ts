import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Observable} from 'rxjs';
import {Router} from '@angular/router';
import {Back, Forward, Go} from './router.actions';
import {map, tap} from 'rxjs/operators';
import {Action} from '@ngrx/store';
import {Location} from '@angular/common';


@Injectable()
export class RouterEffects {


  navigateBack$: Observable<Action> = createEffect(() => {
    return this.actions$.pipe(
        ofType(Back),
        tap(() => {
          this.location.back();
        })
    );
  }, {dispatch: false});

  navigateForward$: Observable<Action> = createEffect(() => {
    return this.actions$.pipe(
        ofType(Forward),
        tap(() => {
          this.location.forward();
        })
    );
  }, {dispatch: false});


  /**
   * Navigate the user to the requested route AND preserve the query string params.
   */
  navigate$: Observable<Action> = createEffect(() => this.actions$.pipe(
    ofType(Go),
    map((action) => action),
    tap((data) => {
        // const extras: NavigationExtras = {state: data.payload};
        if (data.redirectUrl) {
            this.router.navigate([data.link], {queryParams: {returnUrl: data.redirectUrl}});
        } else {
            this.router.navigate([data.link]);

        }
    })
  ), {dispatch: false});

  constructor(
    private actions$: Actions,
    private router: Router,
    private location: Location,
    ) {
  }

}

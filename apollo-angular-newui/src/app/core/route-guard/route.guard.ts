import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable, of} from 'rxjs';
import {select, Store} from '@ngrx/store';
import {Go, loadResponsesFailure, selectCountyIdData, selectIsAuthenticated, selectUserInfo} from '../store';
import {first, map} from 'rxjs/operators';
import {catchError} from 'rxjs/internal/operators/catchError';
import {HttpErrorResponse} from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class RouteGuard implements CanActivate {
    constructor(
        private store$: Store<any>
    ) {
    }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {


    return this.checkStoreAuthentication().pipe(
      map((authed) => {
          if (!authed) {
              // this.store$.dispatch(fromStore.logout({payload: ''}));
              console.log(`Authd = ${authed} redirecting to login date =${Date.now()} expiry = ${new Date('26/08/2021 16:41:43.367').getTime()}`);
              this.store$.dispatch(Go({link: 'login', payload: null, redirectUrl: null}));
              // this.store$.dispatch(Go({link: 'login', payload: null, redirectUrl: state.url}));
              console.log(`canActivate( No. Redirect the user back to login. )`);
              return false;
          }

          console.log(`canActivate( Yes. Navigate the user to the requested route. )`);
        return true;
      }),
      first()
    );
  }

  /**
   * Determine if the user is logged by checking the Redux store.
   */
  private checkStoreAuthentication(): Observable<boolean> {
      let auth = false;
      this.store$.select(selectIsAuthenticated).subscribe(
          (a) => {
              console.log(`Authd = ${a} redirecting to login`);
              return auth = a;
          }
      );
      this.store$.select(selectUserInfo).subscribe(
          (a) => {
              console.log(`login date =${Date.now()} expiry = ${(new Date(a.expiry)).getTime()}`);
              console.log(`login date =${Date.now()} expiry = ${new Date(a.expiry).getTime()}`);
          }
      );
      return of(auth);
      // return this.store$.pipe(select(selectIsAuthenticated)).pipe(first());
  }

}

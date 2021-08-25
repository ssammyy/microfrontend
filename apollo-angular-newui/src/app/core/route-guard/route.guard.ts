import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {select, Store} from "@ngrx/store";
import {Go, selectIsAuthenticated} from "../store";
import {first, map} from "rxjs/operators";

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
        if (authed) {
            // this.store$.dispatch(fromStore.logout({payload: ''}));
            console.log(`Authd = ${authed} redirecting to login`);
            this.store$.dispatch(Go({link: 'login', payload: null, redirectUrl: state.url}));
            console.log(`canActivate( No. Redirect the user back to login. )`);
            return false;
        }

          console.log(`Authd = ${authed} redirecting to Dashboard`);
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
    return this.store$.pipe(select(selectIsAuthenticated)).pipe(first());
  }

}

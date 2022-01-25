import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable, of} from 'rxjs';
import {Store} from '@ngrx/store';
import {Go, selectIsAuthenticated, selectUserInfo} from '../store';
import {first, map} from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class RouteGuard implements CanActivate {
    constructor(
        private store$: Store<any>
    ) {
    }

    tokenExpired(token: string) {
        if(token) {
            const expiry = (JSON.parse(atob(token.split('.')[1]))).exp;
            // console.log(`MY TOKEN VALUE = ${expiry}`);
            return (Math.floor((new Date).getTime() / 1000)) >= expiry;
        }
        else
        {
            return false
        }

    }

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {


        return this.checkStoreAuthentication().pipe(
            map((authed) => {
                if (!authed) {
                    // this.store$.dispatch(fromStore.logout({payload: ''}));
                    // console.log(`Authd = ${authed} redirecting to login date =${Date.now()}`);
                    // this.store$.dispatch(Go({link: 'login', payload: null, redirectUrl: null}));
                    this.store$.dispatch(Go({link: 'login', payload: null, redirectUrl: state.url}));
                    // console.log(`canActivate( No. Redirect the user back to login. )`);
                    return false;
                }

                // console.log(`Authd = ${authed} redirecting Dashboard =${Date.now()}`);
                // console.log(`canActivate( Yes. Navigate the user to the requested route. )`);
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
                //   console.log(`Authd = ${a} redirecting to login`);
                auth = a;
            }
        );
        // this.store$.select(selectUserInfo).subscribe(
        //     (a) => {
        //         return auth = RouteGuard.tokenExpired(a.accessToken);
        //     }
        // );
        // return of(auth);
        this.store$.select(selectIsAuthenticated).subscribe(
            (b) => {
                if (b) {
                    this.store$.select(selectUserInfo).subscribe(
                        (a) => {
                            auth = !this.tokenExpired(a.accessToken);
                        }
                    );
                }
            }
        );

        return of(auth);
        // return this.store$.pipe(select(selectIsAuthenticated)).pipe(first());
    }


}

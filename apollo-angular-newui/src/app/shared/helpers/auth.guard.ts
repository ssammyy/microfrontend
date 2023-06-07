import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {AccountService} from '../services/account.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private accountService: AccountService
  ) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): any {
    const user = this.accountService.userValue;
    if (user) {
      // check if route is restricted by role
      // if (route.data.roles && route.data.roles.indexOf(user.userType) === -1) {
      //   // role not authorised so redirect to home page
      //   this.router.navigate(['/']);
      //   return false;
      // }
      // authorised so return true
      return true;
    }

    // Not logged in so redirect to login page with the return url
    this.router.navigate(['login'], {queryParams: {returnUrl: state.url}});
    return false;
  }
}

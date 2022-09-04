import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {dev} from '../dev/dev';
import {AccountService} from '../services/account.service';
import {AuthenticationService} from '../services/authentication.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private accountService: AccountService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // add auth header with jwt if user is logged in and request is to the api url
    const user = this.accountService.userValue;
    const isLoggedIn = user && user.accessToken;
    const isLoginUrl = request.url.replace(dev.connect, '') === AuthenticationService.loginUrl;
    const isApiUrl = request.url.startsWith(dev.connect);
    if (isLoggedIn && isApiUrl && !isLoginUrl) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${user.accessToken}`
        }
      });
    }

    return next.handle(request);
  }
}


import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {User} from '../models/user';
import {HttpClient} from '@angular/common/http';
import {dev} from '../dev/dev';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser') as string));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User {
    return this.currentUserSubject.value;
  }

  static loginUrl =  'login';
  public url = `${dev.connect}`;
  private currentUserSubject: BehaviorSubject<User>;
  public currentUser: Observable<User>;

  login(loginData: any): any {
    return this.http.post<any>(this.url + AuthenticationService.loginUrl, loginData);
      // .pipe(map(user => {
      //   // store user details and jwt token in local storage to keep user logged in between page refreshes
      //   localStorage.setItem('currentUser', JSON.stringify(user));
      //   this.currentUserSubject.next(user);
      //   return user;
      // }));
  }

  logout(): void {
    // remove user from local storage to log user out
    localStorage.removeItem('currentUser');
    localStorage.removeItem('loggedInUserEmail');
    localStorage.removeItem('loggedInUserId');
    localStorage.removeItem('loggedInUserToken');
    localStorage.removeItem('loggedInUserType');
    localStorage.removeItem('loggedInFullName');
    // @ts-ignore
    this.currentUserSubject.next(null);
  }
}

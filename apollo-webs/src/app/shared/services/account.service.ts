import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {User} from '../models/user';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {dev} from '../dev/dev';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private userSubject: BehaviorSubject<User>;
  public user: Observable<User>;

  public apiUrl = `${dev.connect}`;

  constructor(
    private router: Router,
    private http: HttpClient
  ) {
    this.userSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('user') as string));
    this.user = this.userSubject.asObservable();
  }

  public get userValue(): User {
    return this.userSubject.value;
  }

  // tslint:disable-next-line:typedef
  login(loginData: any) {
    return this.http.post<any>(`${this.apiUrl}login`, loginData)
      .pipe(map(user => {
        // store user details and jwt token in local storage to keep user logged in between page refreshes
        localStorage.setItem('user', JSON.stringify(user));
        this.userSubject.next(user);
        return user;
      }));
  }

  logout(): any {
    // remove user from local storage and set current user to null
    localStorage.removeItem('user');
    // @ts-ignore
    this.userSubject.next(null);
    this.router.navigate(['login']);
  }

  register(UserEntityDto: any): any {
    console.log(UserEntityDto);
    return this.http.post(`${this.apiUrl}auth/signup/user`, UserEntityDto);
  }

  passwordActivation(UserEntity: any): any {
    console.log(UserEntity);
    return this.http.put(`${this.apiUrl}auth/signup/authorize`, UserEntity);
  }

  passwordReset(passwordResetDto: any): any {
    console.log(passwordResetDto);
    return this.http.put(`${this.apiUrl}auth/signup/forgot-password`, passwordResetDto);
  }

  getAll(): any {
    return this.http.get<User[]>(`${this.apiUrl}/users`);
  }

  getById(id: string): any {
    return this.http.get<User>(`${this.apiUrl}/users/${id}`);
  }

  // update(id, params): any {
  //   return this.http.put(`${this.apiUrl}/users/${id}`, params)
  //     .pipe(map(x => {
  //       // update stored user if the logged in user updated their own record
  //       if (id === this.userValue.id) {
  //         // update local storage
  //         const user = { ...this.userValue, ...params };
  //         localStorage.setItem('user', JSON.stringify(user));
  //
  //         // publish updated user to subscribers
  //         this.userSubject.next(user);
  //       }
  //       return x;
  //     }));
  // }
  //
  // delete(id: string): any {
  //   return this.http.delete(`${this.apiUrl}/users/${id}`)
  //     .pipe(map(x => {
  //       // auto logout if the logged in user deleted their own record
  //       if (id == this.userValue.id) {
  //         this.logout();
  //       }
  //       return x;
  //     }));
  // }
}

import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {
  doValidateTokenForUser,
  loadResponsesFailure,
  LoginCredentials,
  selectTokenValidatedStateValidated, selectUserInfo
} from '../../../core/store';
import {select, Store} from '@ngrx/store';
import {ActivatedRoute, Router} from '@angular/router';
import {throwError} from 'rxjs';
import swal from 'sweetalert2';

@Component({
  selector: 'app-otp',
  templateUrl: './otp.component.html',
  styleUrls: ['./otp.component.css']
})
export class OtpComponent implements OnInit {

  loginForm: FormGroup;
  public credential: LoginCredentials;
  returnUrl: string;
  otpSent = false;
  tokenValidated = false;
  username = '';

  constructor(
      private store$: Store<any>,
      private route: ActivatedRoute,
      private router: Router
  ) {
    this.credential = {username: '', password: ''};
    this.loginForm = new FormGroup({});
    this.returnUrl = '';
  }

  ngOnInit(): void {
    this.loginForm = new FormGroup(
        {
          // username: new FormControl('', [Validators.required]),
          otp: new FormControl('', [Validators.required]),
        }
    );

    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      return this.username = u.username;
    });

    // this.returnUrl = this.route.snapshot.queryParams[`returnUrl`] || `/dashboard`;
  }

  onClickValidateOtp() {
    console.log(`user name ${this.username}`);


    this.tokenValidated = true;
    if (
        this.loginForm?.get('otp')?.value === undefined ||
        this.loginForm?.get('otp')?.value === null ||
        this.loginForm?.get('otp')?.value === ''
    ) {
      this.store$.dispatch(loadResponsesFailure({
        error: {
          payload: 'OTP is required',
          status: 100,
          response: '05'
        }
      }));

    } else {
      this.store$.dispatch(doValidateTokenForUser({
        payload: {
          username: this.username,
          token: this.loginForm?.get('otp')?.value
        }
      }));
      this.store$.pipe(select(selectTokenValidatedStateValidated)).subscribe((d) => {
        console.log(`value of inside is ${d}`);
        if (d) {
          this.username = this.loginForm?.get('username')?.value;
          // this.step = 1;
          this.router.navigate(['dashboard']);
          return this.tokenValidated = d;
        } else {
          this.otpSent = false;
          this.loginForm?.get('otp')?.reset();
          this.store$.dispatch(loadResponsesFailure({
            error: {
              payload: 'OTP is invalid!, Could not validate token',
              status: 100,
              response: '05'
            }
          }));
          // return throwError('Could not validate token');
        }

      });
    }
    // if (this.tokenValidated) {
    //   console.log(`Token validation ${this.tokenValidated}`);
    // }

  }

  public onClickLogin() {
    // if (valid) {
    this.router.navigate(['dashboard']);
    // }

  }

}

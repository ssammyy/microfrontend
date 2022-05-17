import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {
  doValidateTokenForUser,
  loadResponsesFailure,
  LoginCredentials,
  selectTokenValidatedStateValidated,
  selectUserInfo,
} from '../../../core/store';
import {select, Store} from '@ngrx/store';
import {ActivatedRoute, Router} from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import {NotificationService} from '../../../core/store/data/std/notification.service';

@Component({
  selector: 'app-otp',
  templateUrl: './otp.component.html',
  styleUrls: ['./otp.component.css'],
})
export class OtpComponent implements OnInit {

  loginForm: FormGroup;
  public credential: LoginCredentials;
  returnUrl: string;
  otpSent = false;
  tokenValidated = false;
  username = '';
  redirectUrl: string;
  loadingText: string;

  constructor(
      private store$: Store<any>,
      private route: ActivatedRoute,
      private router: Router,
      private notifyService: NotificationService,
      private SpinnerService: NgxSpinnerService) {
    this.credential = {username: '', password: ''};
    this.loginForm = new FormGroup({});
    this.returnUrl = '';
  }

  ngOnInit(): void {
    this.loginForm = new FormGroup(
        {
          // username: new FormControl('', [Validators.required]),
          otp: new FormControl('', [Validators.required]),
        },
    );

    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      return this.username = u.email;
    });

    // this.returnUrl = this.route.snapshot.queryParams[`returnUrl`] || `/dashboard`;
  }

  showToasterError(title: string, message: string) {
    this.notifyService.showError(message, title);

  }

  onClickValidateOtp() {
    this.loadingText = 'Validating OTP....';
    // this.SpinnerService.show();
    // console.log(`user name ${this.username}`);
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
          response: '05',
        },
      }));

    } else {
      this.store$.dispatch(doValidateTokenForUser({
        payload: {
          username: this.username,
          token: this.loginForm?.get('otp')?.value,
        },
      }));



      this.store$.pipe(select(selectTokenValidatedStateValidated)).subscribe((d) => {
        console.log(`value of inside is ${d}`);
        if (d) {
          // this.username = this.loginForm?.get('username')?.value;
          // console.log(this.username)
          // console.log("redirecturl ni hii " + this.redirectUrl)
          // this.step = 1;
          this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            return this.redirectUrl = u.redirectUrl;
          });

          if (!(this.redirectUrl) || this.redirectUrl === '') {
            this.router.navigate(['dashboard']);
          } else {
            this.router.navigate([this.redirectUrl]);
          }
          return this.tokenValidated = d;
        } else {
          this.otpSent = false;
          // this.SpinnerService.hide()

          this.loginForm?.get('otp')?.reset();
          this.loginForm.controls.otp.setErrors({invalidNumber: true});  // <--- Set invalidNumber to true

          // this.store$.dispatch(loadResponsesFailure({
          //   error: {
          //     payload: 'OTP is invalid!, Could not validate token',
          //     status: 100,
          //     response: '05'
          //   }
          // }));
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

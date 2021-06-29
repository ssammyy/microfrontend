import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {
  doSendTokenForUser,
  doValidateTokenForUser,
  loadResetAuths,
  loadResponsesFailure,
  selectTokenSentStateOtpSent,
  selectTokenValidatedStateValidated
} from "../../core/store";
import {select, Store} from "@ngrx/store";
import {throwError} from "rxjs";

@Component({
  selector: 'app-reset-credentials',
  templateUrl: './reset-credentials.component.html',
  styles: []
})
export class ResetCredentialsComponent implements OnInit {
  step = 0;
  stepZeroForm: FormGroup = new FormGroup({});
  stepOneForm: FormGroup = new FormGroup({});
  otpSent = false;
  tokenValidated = false;
  username = '';

  constructor(
    private store$: Store<any>,
  ) {
  }

  ngOnInit(): void {
    this.stepZeroForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      otp: new FormControl('', [Validators.required]),
    });
    this.stepOneForm = new FormGroup({
        credentials: new FormControl('', [Validators.required, Validators.minLength(6)]),
        confirmCredentials: new FormControl('', [Validators.required, Validators.minLength(6)]),
      }
    );


  }



  onClickSendOtp() {
    this.otpSent = true;
    if (
      this.stepZeroForm?.get('username')?.value === undefined ||
      this.stepZeroForm?.get('username')?.value === null ||
      this.stepZeroForm?.get('username')?.value === ''
    ) {
      this.store$.dispatch(loadResponsesFailure({
        error: {
          payload: 'Username is required',
          status: 100,
          response: '05'
        }
      }));

    } else {
      this.store$.dispatch(doSendTokenForUser({payload: {username: this.stepZeroForm?.get('username')?.value}}));

    }

    this.store$.pipe(select(selectTokenSentStateOtpSent)).subscribe((d) => {
      console.log(`value of inside is ${d}`)
      if (d) {
        return this.otpSent = d;
      } else {
        return throwError("Unable to send token");
      }
    });

  }

  onClickValidateOtp() {
    this.tokenValidated = true;
    if (
      this.stepZeroForm?.get('otp')?.value === undefined ||
      this.stepZeroForm?.get('otp')?.value === null ||
      this.stepZeroForm?.get('otp')?.value === ''
    ) {
      this.store$.dispatch(loadResponsesFailure({
        error: {
          payload: 'Username and/or OTP is required',
          status: 100,
          response: '05'
        }
      }));

    } else {
      this.store$.dispatch(doValidateTokenForUser({
        payload: {
          username: this.stepZeroForm?.get('username')?.value,
          token: this.stepZeroForm?.get('otp')?.value
        }
      }));

    }

    this.store$.pipe(select(selectTokenValidatedStateValidated)).subscribe((d) => {
      console.log(`value of inside is ${d}`)
      if (d) {
        this.username = this.stepZeroForm?.get('username')?.value
        this.step = 1;
        return this.tokenValidated = d;
      } else {
        return throwError("Could not validate token");
      }

    });


  }

  onTokenEntered() {
    // if (this.stepZeroForm.get('otp')?.value === undefined ||
    //   this.stepZeroForm.get('otp')?.value === null||
    //   this.stepZeroForm.get('otp')?.value === ''||
    // )
    // console.log(`The size is ${this.stepZeroForm.get('otp')?.value.size}`)
    // return
  }

  onClickResetCredentials(valid: boolean) {
    if (valid) {
      if (
        this.stepOneForm?.get('credentials')?.value === undefined ||
        this.stepOneForm?.get('confirmCredentials')?.value === undefined ||
        this.stepOneForm?.get('credentials')?.value === null ||
        this.stepOneForm?.get('confirmCredentials')?.value === null ||
        this.stepOneForm?.get('credentials')?.value === '' ||
        this.stepOneForm?.get('confirmCredentials')?.value === ''
      ) {
        this.store$.dispatch(loadResponsesFailure({
          error: {
            payload: 'Provide valid credentials and confirmation',
            status: 100,
            response: '05'
          }
        }));

      } else {
        if (this.stepOneForm?.get('confirmCredentials')?.value === this.stepOneForm?.get('credentials')?.value) {
          this.store$.dispatch(loadResetAuths({
            payload: {
              username: this.username,
              password: this.stepOneForm?.get('credentials')?.value
            }, redirectUrl: ''
          }))
        } else {
          this.store$.dispatch(loadResponsesFailure({
            error: {
              payload: 'Password and confirmation must match',
              status: 100,
              response: '05'
            }
          }));

        }


      }

    } else {
      this.store$.dispatch(loadResponsesFailure({
        error: {
          payload: 'All fields require to be filled in',
          status: 100,
          response: '05'
        }
      }));

    }


  }

  passwordMatchValidator = (g: FormGroup) => g.get('credentials')?.value === g.get('confirmCredentials')?.value ? null : {'mismatch': true};
}

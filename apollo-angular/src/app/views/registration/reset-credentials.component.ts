import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-reset-credentials',
  templateUrl: './reset-credentials.component.html',
  styles: []
})
export class ResetCredentialsComponent implements OnInit {
  step = 0;
  stepZeroForm: FormGroup = new FormGroup({});
  otpSent = false;

  constructor() {
  }

  ngOnInit(): void {
    this.stepZeroForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      otp: new FormControl('', [Validators.required]),
    });
  }

  onClickValidateToken(valid: boolean) {

  }

  onClickSendOtp() {
    console.log(`Clicked with inputs as ${this.stepZeroForm.get('otp')?.value}`)

  }

  onClickValidateOtp() {

  }

  onTokenEntered() {
    // if (this.stepZeroForm.get('otp')?.value === undefined ||
    //   this.stepZeroForm.get('otp')?.value === null||
    //   this.stepZeroForm.get('otp')?.value === ''||
    // )
    // console.log(`The size is ${this.stepZeroForm.get('otp')?.value.size}`)
    // return
  }
}

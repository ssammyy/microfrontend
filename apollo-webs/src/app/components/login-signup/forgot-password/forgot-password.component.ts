import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import {AccountService} from '../../../shared/services/account.service';
import {AlertService} from '../../../shared/services/alert.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {
  passwordResetForm!: FormGroup;


  loading = false;
  submitted = false;
  returnUrl!: string;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private spinner: NgxSpinnerService,
    private router: Router,
    private accountService: AccountService,
    private alertService: AlertService
  ) {
  }

  // convenience getter for easy access to form fields
  get formPasswordReset(): any {
    return this.passwordResetForm.controls;
  }

  ngOnInit(): any {
    this.passwordResetForm = this.formBuilder.group({
      emailUsername: ['', Validators.required]
      // username: ['', Validators.required]
    });

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
  }

  onSubmitPassword(): any {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.passwordResetForm.invalid) {
      return;
    }
    this.loading = true;
    this.accountService.passwordReset(this.passwordResetForm.value).subscribe(
      (data: any) => {
        console.log(data);
        this.router.navigate(['/otpVerification']);
      },
      (error: { error: { message: any; }; }) => {
        this.alertService.error(error.error.message, 'Access Denied');
        // this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      });
  }

}

import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import {AccountService} from '../../../shared/services/account.service';
import {AlertService} from '../../../shared/services/alert.service';
import {UserRegister} from '../../../shared/models/user';
import {ConfirmedValidator} from '../../../shared/models/confirmed.validator';

@Component({
  selector: 'app-complite-registration',
  templateUrl: './complete-registration.component.html',
  styleUrls: ['./complete-registration.component.css']
})
export class CompleteRegistrationComponent implements OnInit {
  passwordForm!: FormGroup;


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
  get formPassword(): any {
    return this.passwordForm.controls;
  }

  ngOnInit(): any {
    this.passwordForm = this.formBuilder.group({
      otpVerification: ['', Validators.required],
      // emailUsername: ['', Validators.required],
      password: ['', Validators.required],
      passwordConfirmation: ['', [Validators.required]]
    }, {
      validator: ConfirmedValidator('password', 'passwordConfirmation')
    });

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
  }

  onSubmitPassword(): any {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.passwordForm.invalid) {
      return;
    }
    this.loading = true;
    this.accountService.passwordActivation(this.passwordForm.value).subscribe(
      (data: UserRegister) => {
        console.log(data);
        this.router.navigate([this.returnUrl]);
      },
      (error: { error: { message: any; }; }) => {
        this.alertService.error(error.error.message, 'Access Denied');
        // this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      });
  }

}


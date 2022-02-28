import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {AccountService} from '../../../shared/services/account.service';
import {AlertService} from '../../../shared/services/alert.service';
import {UserRegister} from '../../../shared/models/user';
import {NgxSpinnerService} from 'ngx-spinner';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;


  loading = false;
  submitted = false;
  returnUrl!: string;
  selectedType!: number;

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
  get formRegister(): any {
    return this.registerForm.controls;
  }

  ngOnInit(): any {
    this.registerForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      personalContactNumber: ['', Validators.required],
      typeOfUser: ['', Validators.required],
      userPinIdNumber: ['', Validators.required]
    });

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
  }

  onSubmit(): any {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.registerForm.invalid) {
      return;
    }
    this.loading = true;
    this.accountService.register(this.registerForm.value).subscribe(
      (data: UserRegister) => {
        console.log(data);
        this.router.navigate(['/otpVerification']);
      },
      (error: { error: { message: any; }; }) => {
        this.alertService.error(error.error.message, 'Access Denied');
        // this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      });
  }

  onChange(event: number): void{
    this.selectedType = event;
  }
}


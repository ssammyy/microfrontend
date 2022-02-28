import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {AccountService} from '../../../shared/services/account.service';
import {AlertService} from '../../../shared/services/alert.service';
import {first} from 'rxjs/operators';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  // loading = false;
  // submitted = false;
  // togglePassword = 'password';
  // showPasswordIcon!: boolean;
  // hidePasswordIcon!: boolean;
  //
  // constructor(
  //   private formBuilder: FormBuilder,
  //   private route: ActivatedRoute,
  //   private router: Router,
  //   // private loginService: LoginService,
  //   private accountService: AccountService,
  //   // private authenticationService: AuthenticationService,
  //   private spinner: NgxSpinnerService,
  //   private notificationService: NotificationService
  // ) {
  //   // redirect to home if already logged in
  //   if (this.accountService.user) {
  //     this.router.navigate(['/dashboard']);
  //   }
  // }
  //
  // // public MyProfile: any;
  // private MyProfile: any;
  //
  // ngOnInit(): void {
  //   this.showPasswordIcon = false;
  //   this.hidePasswordIcon = true;
  //   this.togglePassword = 'password';
  //
  //   this.loginForm = this.formBuilder.group({
  //     username: ['', Validators.required],
  //     password: ['', Validators.required],
  //   });
  // }
  //
  // get formSignIn(): any {
  //   return this.loginForm.controls;
  // }
  //
  // onSubmit(): void {
  //   this.spinner.show();
  //   this.authenticationService.login(this.loginForm.value).subscribe(
  //     (data: { fullName: string; userType: string; email: string; id: string; accessToken: string; }) => {
  //       localStorage.setItem('loggedInUserEmail', data.email);
  //       localStorage.setItem('loggedInUserId', data.id);
  //       localStorage.setItem('loggedInUserToken', data.accessToken);
  //       localStorage.setItem('loggedInUserType', data.userType);
  //       localStorage.setItem('loggedInFullName', data.fullName);
  //
  //       this.spinner.hide();
  //       this.router.navigate(['/dashboard']);
  //       // if (data.userType === 'Employee') {
  //       //   this.router.navigate(['/dashboard']);
  //       // }
  //       // if (data.userType === 'Manufacturer') {
  //       //   this.router.navigate(['/admin-dashboard']);
  //       // }
  //       // if (data.userType === 'Importer') {
  //       //   this.router.navigate(['/admin-dashboard']);
  //       // }
  //       // if (data.userType === 'EPRA') {
  //       //   this.router.navigate(['/admin-dashboard']);
  //       // }
  //
  //     },
  //     // tslint:disable-next-line:max-line-length
  //     (error: { error: { message: any; }; }) => {
  //       this.notificationService.showError(error.error.message, 'Access Denied');
  //       this.spinner.hide();
  //     }
  //   );
  // }

  loading = false;
  submitted = false;
  returnUrl!: string;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private accountService: AccountService,
    private alertService: AlertService
  ) { }

  ngOnInit(): any {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
  }

  // convenience getter for easy access to form fields
  get formSignIn(): any { return this.loginForm.controls; }

  onSubmit(): any {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    console.log(this.loginForm.value);
    this.accountService.login(this.loginForm.value)
      .pipe(first())
      .subscribe(
        data => {
          this.router.navigate([this.returnUrl]);
        },
        error => {
          this.alertService.error(error);
          this.loading = false;
        });
  }

}

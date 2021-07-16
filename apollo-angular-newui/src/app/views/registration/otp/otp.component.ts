import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LoginCredentials} from "../../../core/store";
import {Store} from "@ngrx/store";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-otp',
  templateUrl: './otp.component.html',
  styleUrls: ['./otp.component.css']
})
export class OtpComponent implements OnInit {

  loginForm: FormGroup;
  public credential: LoginCredentials;
  returnUrl: string;

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
          username: new FormControl('', [Validators.required]),
        }
    );
    this.returnUrl = this.route.snapshot.queryParams[`returnUrl`] || `/dashboard`;
  }

  public onClickLogin() {
    // if (valid) {
    this.router.navigate(['dashboard']);
    //}

  }

}

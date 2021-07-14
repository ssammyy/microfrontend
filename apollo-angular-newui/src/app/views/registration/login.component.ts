import {Component, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {ActivatedRoute} from "@angular/router";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {loadAuths, LoginCredentials} from "../../core/store";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styles: []
})

export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  public credential: LoginCredentials;
  returnUrl: string;

  constructor(
    private store$: Store<any>,
    private route: ActivatedRoute
  ) {
    this.credential = {username: '', password: ''};
    this.loginForm = new FormGroup({});
    this.returnUrl = '';
  }

  ngOnInit(): void {
    this.loginForm = new FormGroup(
        {
          username: new FormControl('', [Validators.required]),
          password: new FormControl('', [Validators.required]),
        }
    );
    this.returnUrl = this.route.snapshot.queryParams[`returnUrl`] || `login/otp`;
  }

  public onClickLogin(valid: Boolean) {
    if (valid) {
      this.credential = this.loginForm.value;
      this.store$.dispatch(loadAuths({payload: this.credential, redirectUrl: this.returnUrl}));
    }

  }

}

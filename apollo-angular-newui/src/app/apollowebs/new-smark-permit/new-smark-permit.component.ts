import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {loadAuths, LoginCredentials} from "../../core/store";
import {Store} from "@ngrx/store";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-new-smark-permit',
  templateUrl: './new-smark-permit.component.html',
  styleUrls: ['./new-smark-permit.component.css']
})
export class NewSmarkPermitComponent implements OnInit {
  loginForm: FormGroup;
  public credential: LoginCredentials;
  returnUrl: string;
  constructor(private store$: Store<any>,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.loginForm = new FormGroup(
        {
          username: new FormControl('', [Validators.required]),
          password: new FormControl('', [Validators.required]),
        }
    );
    this.returnUrl = this.route.snapshot.queryParams[`returnUrl`] || `/dashboard`;
  }

  public onClickLogin(valid: Boolean) {
    if (valid) {
      this.credential = this.loginForm.value;
      this.store$.dispatch(loadAuths({payload: this.credential, redirectUrl: this.returnUrl}));
    }

  }

}

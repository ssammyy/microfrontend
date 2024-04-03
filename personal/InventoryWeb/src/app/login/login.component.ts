// login.component.ts
import { Component } from '@angular/core';
import {ApiService} from "../Utils/ApiService";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {MessageService} from "primeng/api";
// import { AuthService } from 'path_to_your_auth_service'; // Import your AuthService here

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  value: string | undefined;
  loginError: boolean = false
  loading = false



  constructor(private authService: ApiService,
              private http : HttpClient,
              private router : Router,
  ) {}




  onSubmit() {
    this.loading= true
    const credentials = {
      username: this.username,
      password: this.password
    };

    this.http.post(this.authService.authenticate, credentials )    .subscribe(
        (response: any) => {
          this.authService.setToken(response.token)
          this.router.navigate(['/dashBoard']);
        },
        error => {
          this.loading = false
          this.loginError= true
          // this.showWarn('select whether order is cleared or on credit')

        }
      );

  }


}

import {Component, OnInit} from '@angular/core';
import Typed from 'typed.js';
import {HttpClient} from "@angular/common/http";
import {ApiService} from "../utilities/ApiService";
import {Router} from "@angular/router";
import { NgxSpinnerService } from 'ngx-spinner';




@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit{
  username: string ='';
  password: string ='';

  constructor(
    private http : HttpClient,
    private apiService : ApiService,
    private router : Router,
    private spinner : NgxSpinnerService
  ) {
  }



  ngOnInit() {
    this.animateText()
  }

  animateText() {
    const options = {
      strings: ["Key in your details to log in to the system"],
      typeSpeed: 50,
    };

    const typed = new Typed('.typing-text', options);
    setTimeout(() => {
      typed.reset();
      typed.start();
    }, 30000);
  }


  /***
   * Login Request
   */
  login() {
    const loginRequest = {
      username: this.username,
      password: this.password
    };
    this.spinner.show()

    this.http.post(this.apiService.authenticate, loginRequest).subscribe(
      (response) => {
        console.log('Login successful:', response);
        this.router.navigate(['dashBoard'])
      },
      (error) => {
        console.error('Login error:', error);
      },
      ()=>{
        this.spinner.hide();
      }
    );
  }


}

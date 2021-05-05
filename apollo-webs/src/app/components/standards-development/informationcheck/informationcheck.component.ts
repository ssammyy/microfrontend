import { Component, OnInit } from '@angular/core';
import {FeedbackEmail, InfoAvailableYes, Notification, RootObject} from '../../../shared/models/standard-development';
import {NepPointService} from "../../../shared/services/nep-point.service";
import {HttpErrorResponse} from '@angular/common/http';
import {FormBuilder, FormGroup, NgForm} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-informationcheck',
  templateUrl: './informationcheck.component.html',
  styleUrls: ['./informationcheck.component.css']
})
export class InformationcheckComponent implements OnInit {
  public notifications: Notification[] | undefined;
  public actionRequest: Notification | undefined;
  public stdRequestFormGroup!: FormGroup;
  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private notificationService: NepPointService) { }
  ngOnInit(): void {
    this.getRequests();
  }
  public getRequests(): void{
    this.notificationService.sendGetRequest().subscribe(
      (response: Notification[]) => {
        this.notifications = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onReSubmit(notification: InfoAvailableYes): void{
    this.notificationService.reviewTasks(notification).subscribe(
      (response: InfoAvailableYes) => {
        console.log(response);
        this.getRequests();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public sendEmailFeedback(notification: FeedbackEmail): void{
    this.notificationService.feedbackEmail(notification).subscribe(
      (response: FeedbackEmail) => {
        console.log(response);
        this.getRequests();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onSubmit(notification: InfoAvailableYes): void{
    this.notificationService.reviewTasks(notification).subscribe(
      (response: InfoAvailableYes) => {
        console.log(response);
        this.getRequests();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onOpenModal(notification: Notification, mode: string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'yes'){
      this.actionRequest = notification;
      button.setAttribute('data-target', '#yesmodal');
    }
    if (mode === 'yes2'){
      this.actionRequest = notification;
      button.setAttribute('data-target', '#yesmodals');
    }
    if (mode === 'SendEmail'){
      this.actionRequest = notification;
      button.setAttribute('data-target', '#sendemail');
    }
    if (mode === 'no'){
      this.actionRequest = notification;
      button.setAttribute('data-target', '#noModal');
    }


    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  sendEnquiry(): void{

    this.notificationService.infoAvailableYes(this.stdRequestFormGroup.value).subscribe(
      (response: InfoAvailableYes) => {
        console.log(response);
        this.router.navigate(['/sendfeedback']);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }


}

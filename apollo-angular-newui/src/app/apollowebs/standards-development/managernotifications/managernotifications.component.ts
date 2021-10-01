import { Component, OnInit } from '@angular/core';
import {InboundNotification, InfoAvailableNo, InfoAvailableYes} from "../../../core/store/data/std/std.model";
import {NepdomesticnotificationServiceService} from "../../../core/store/data/std/nepdomesticnotification-service.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-managernotifications',
  templateUrl: './managernotifications.component.html',
  styleUrls: ['./managernotifications.component.css']
})
export class ManagernotificationsComponent implements OnInit {
  public notifications: InboundNotification[] | undefined;
  public actionRequest: InboundNotification | undefined;

  constructor(private nepNotification :NepdomesticnotificationServiceService) { }

  ngOnInit(): void {
    this.getRequests();
  }
  public getRequests(): void {
    this.nepNotification.getManagerNotifications().subscribe(
        (response: InboundNotification[]) => {
          this.notifications = response;
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }

  public onOpenModal(notification: InboundNotification, mode: string): void {
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
    if (mode === 'no'){
      this.actionRequest = notification;
      button.setAttribute('data-target', '#noModal');
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();
  }
  public onReSubmit(notification: InfoAvailableYes): void{
    this.nepNotification.managerAccept(notification).subscribe(
        (response: InfoAvailableYes) => {
          console.log(response);
          this.getRequests();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }
  public onNoSubmit(notification: InfoAvailableNo): void{
    this.nepNotification.managerAccept(notification).subscribe(
        (response: InfoAvailableNo) => {
          console.log(response);
          this.getRequests();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }
}

import { Component, OnInit } from '@angular/core';
import {StandardDevelopmentService} from "../../../../shared/services/standard-development.service";
import {HttpErrorResponse} from '@angular/common/http';
import {DepartmentResponse, InfoAvailableYes, Notification} from '../../../../shared/models/standard-development';

@Component({
  selector: 'app-divisionresponse',
  templateUrl: './divisionresponse.component.html',
  styleUrls: ['./divisionresponse.component.css']
})
export class DivisionresponseComponent implements OnInit {
  public notifications: Notification[] | undefined;
  public actionRequest: Notification | undefined;

  constructor(private standardDevelopmentService:StandardDevelopmentService) {
  }

  ngOnInit(): void {
    this.getRequests();
  }

  public getRequests(): void {
    this.standardDevelopmentService.sendGetRequestDivision().subscribe(
      (response: Notification[]) => {
        this.notifications = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onOpenModal(notification: Notification, mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'yes') {
      this.actionRequest = notification;
      button.setAttribute('data-target', '#myModal');
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();
  }
  public onReSubmit(notification: DepartmentResponse): void{
    this.standardDevelopmentService.deptResponse(notification).subscribe(
      (response: DepartmentResponse) => {
        console.log(response);
        this.getRequests();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
}

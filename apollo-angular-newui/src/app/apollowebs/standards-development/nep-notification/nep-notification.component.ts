import { Component, OnInit } from '@angular/core';
import {
    DraftNotification,
    InfoAvailableNo,
    InfoAvailableYes,
    finalSubmit,
    NepNotification
} from "../../../core/store/data/std/std.model";
import {Observable} from "rxjs";
import {HttpErrorResponse, HttpEventType, HttpResponse} from "@angular/common/http";
import {NepdomesticnotificationServiceService} from "../../../core/store/data/std/nepdomesticnotification-service.service";

@Component({
  selector: 'app-nep-notification',
  templateUrl: './nep-notification.component.html',
  styleUrls: ['./nep-notification.component.css']
})
export class NepNotificationComponent implements OnInit {
  public notifications: NepNotification[] | undefined;
  public actionRequest: NepNotification | undefined;
  selectedFiles?: FileList;
  currentFile?: File;
  progress = 0;
  message = '';
  name = 'Angular';
  fileName = null;



  fileInfos?: Observable<any>;

  constructor(private nepNotification :NepdomesticnotificationServiceService) { }

  ngOnInit(): void {
    this.getNotifications();
    this.fileInfos = this.nepNotification.getFiles();
  }

  public getNotifications(): void {
    this.nepNotification.getNotifications().subscribe(
        (response: NepNotification[]) => {
          this.notifications = response;
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }
  public onOpenModal(notification: NepNotification, mode: string): void {

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
    if (mode === 'draftNotification'){
      this.actionRequest = notification;
      button.setAttribute('data-target', '#draftNotification');
    }
    if (mode === 'no'){
      this.actionRequest = notification;
      button.setAttribute('data-target', '#noModal');
    }
    if (mode === 'final'){
      this.actionRequest = notification;
      button.setAttribute('data-target', '#final');
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();
  }

  public onReSubmit(notification: InfoAvailableYes): void{
    this.nepNotification.reviewTasks(notification).subscribe(
        (response: InfoAvailableYes) => {
          console.log(response);
          this.getNotifications();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }

  public uploadNotification(notification: DraftNotification): void{
    this.progress = 0;

    if (this.selectedFiles) {
      const file: File | null = this.selectedFiles.item(0);

      if (file) {
        this.currentFile = file;

        this.nepNotification.upload(this.currentFile).subscribe(
            (event: any) => {
              if (event.type === HttpEventType.UploadProgress) {
                this.progress = Math.round(100 * event.loaded / event.total);
              } else if (event instanceof HttpResponse) {
                this.message = event.body.message;
                this.fileInfos = this.nepNotification.getFiles();
                this.nepNotification.uploadFile(notification).subscribe(
                    (response: DraftNotification) => {
                      console.log(response);
                      this.getNotifications();
                    },
                    (error: HttpErrorResponse) => {
                      alert(error.message);
                    }
                );
              }
            },
            (err: any) => {
              console.log(err);
              this.progress = 0;

              if (err.error && err.error.message) {
                this.message = err.error.message;
              } else {
                this.message = 'Could not upload the file!';
              }

              this.currentFile = undefined;
            });
      }

      this.selectedFiles = undefined;
    }
  }

  public onNoSubmit(notification: InfoAvailableNo): void{
    this.nepNotification.submitNo(notification).subscribe(
        (response: InfoAvailableNo) => {
          console.log(response);
          this.getNotifications();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }

  public finalSubmit(notification: finalSubmit): void{
    this.nepNotification.submitFinal(notification).subscribe(
        (response: finalSubmit) => {
          console.log(response);
          this.getNotifications();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }

  public onSubmit(notification: InfoAvailableYes): void{
    this.nepNotification.reviewTasks(notification).subscribe(
        (response: InfoAvailableYes) => {
          console.log(response);
          this.getNotifications();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }
// file uploads

  selectFile(event: any): void {
    this.selectedFiles = event.target.files;
    this.fileName = event.target.files[0].name;
  }
}

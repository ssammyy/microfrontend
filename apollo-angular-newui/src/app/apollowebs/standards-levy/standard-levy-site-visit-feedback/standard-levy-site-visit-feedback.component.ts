import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {ManufacturePendingTask} from "../../../core/store/data/levy/levy.model";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-standard-levy-site-visit-feedback',
  templateUrl: './standard-levy-site-visit-feedback.component.html',
  styleUrls: ['./standard-levy-site-visit-feedback.component.css']
})
export class StandardLevySiteVisitFeedbackComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  manufacturePendingTasks: ManufacturePendingTask[] = [];
  public actionRequestPending: ManufacturePendingTask | undefined;
  blob: Blob;
  constructor(
      private levyService: LevyService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.viewFeedBackReport();
  }
  public viewFeedBackReport(): void{
    this.SpinnerService.show();
    this.levyService.viewFeedBackReport().subscribe(
        (response: ManufacturePendingTask[])=> {
          this.dtTrigger.next();
          this.manufacturePendingTasks = response;
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }
  public onOpenModalPending(manufacturePendingTask: ManufacturePendingTask,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='viewPending'){
      this.actionRequestPending=manufacturePendingTask;
      button.setAttribute('data-target','#viewPending');
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.levyService.viewReportDoc(pdfId).subscribe(
        (dataPdf: any) => {
          this.SpinnerService.hide();
          this.blob = new Blob([dataPdf], {type: applicationType});

          // tslint:disable-next-line:prefer-const
          let downloadURL = window.URL.createObjectURL(this.blob);
          const link = document.createElement('a');
          link.href = downloadURL;
          link.download = fileName;
          link.click();
          // this.pdfUploadsView = dataPdf;
        },
    );
  }

}

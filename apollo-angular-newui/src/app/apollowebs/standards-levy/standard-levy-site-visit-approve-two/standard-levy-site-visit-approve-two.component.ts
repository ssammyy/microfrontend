import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {
  ApproveVisitTask,
  ReportDecisionLevelOne,
  ReportDecisionLevelTwo
} from "../../../core/store/data/levy/levy.model";
import {Router} from "@angular/router";
import {FormBuilder} from "@angular/forms";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-standard-levy-site-visit-approve-two',
  templateUrl: './standard-levy-site-visit-approve-two.component.html',
  styleUrls: ['./standard-levy-site-visit-approve-two.component.css']
})
export class StandardLevySiteVisitApproveTwoComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  displayTable: boolean = false;
  blob: Blob;
  tasks: ApproveVisitTask[] = [];
  public actionRequest: ApproveVisitTask | undefined;
  public uploadedFiles:  FileList;
  constructor(
      private router: Router,
      private formBuilder: FormBuilder,
      private levyService: LevyService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getSiteReportLevelTwo();
  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }
  showToasterWarning(title:string,message:string){
    this.notifyService.showWarning(message, title)

  }
  public getSiteReportLevelTwo(): void{
    this.SpinnerService.show();
    this.levyService.getSiteReportLevelTwo().subscribe(
        (response: ApproveVisitTask[])=> {
          this.tasks = response;
          this.dtTrigger.next();
          this.displayTable = true;
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  public onDecision(reportDecisionLevelTwo: ReportDecisionLevelTwo): void{
    this.SpinnerService.show();
    this.levyService.decisionOnSiteReportLevelTwo(reportDecisionLevelTwo).subscribe(
        (response: ApproveVisitTask) => {
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Report Approved`);
          console.log(response);
          this.getSiteReportLevelTwo();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Report Was Not Approved; Try Again`);
          console.log(error.message);
          this.getSiteReportLevelTwo();
          //alert(error.message);
        }
    );
  }
  public onDecisionReject(reportDecisionLevelTwo: ReportDecisionLevelTwo): void{
    this.SpinnerService.show();
    this.levyService.decisionOnSiteReportLevelTwo(reportDecisionLevelTwo).subscribe(
        (response: ApproveVisitTask) => {
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Report Rejected`);
          console.log(response);
          this.getSiteReportLevelTwo();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Report Was Not Approved; Try Again`);
          console.log(error.message);
          this.getSiteReportLevelTwo();
          //alert(error.message);
        }
    );
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

  public onOpenModal(task: ApproveVisitTask,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='approveModal'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approveModal');
    }

    if (mode==='rejectModal'){
      this.actionRequest=task;
      button.setAttribute('data-target','#rejectModal');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

}

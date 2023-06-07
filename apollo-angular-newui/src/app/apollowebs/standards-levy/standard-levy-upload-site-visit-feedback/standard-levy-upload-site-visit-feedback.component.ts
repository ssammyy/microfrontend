import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {ApproveVisitTask, VisitTask} from "../../../core/store/data/levy/levy.model";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-standard-levy-upload-site-visit-feedback',
  templateUrl: './standard-levy-upload-site-visit-feedback.component.html',
  styleUrls: ['./standard-levy-upload-site-visit-feedback.component.css']
})
export class StandardLevyUploadSiteVisitFeedbackComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  displayTable: boolean = false;
  blob: Blob;
  tasks: VisitTask[] = [];
  public actionRequest: VisitTask | undefined;
  public uploadedFiles:  FileList;
  public prepareFeedBackFormGroup!: FormGroup;
  constructor(
      private router: Router,
      private formBuilder: FormBuilder,
      private levyService: LevyService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getScheduledVisits();

    this.prepareFeedBackFormGroup = this.formBuilder.group({
      visitDate: ['', Validators.required],
      purpose: ['', Validators.required],
      personMet: ['', Validators.required],
      actionTaken: ['', Validators.required],
      taskId: [],
      visitID: []

    });
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
  get formPrepareFeedBack(): any {
    return this.prepareFeedBackFormGroup.controls;
  }
  public getScheduledVisits(): void{
    this.SpinnerService.show();
    this.levyService.getScheduledVisits().subscribe(
        (response: VisitTask[])=> {
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
  saveFeedBack(): void {
    this.SpinnerService.show();
    this.levyService.saveSiteVisitFeedback(this.prepareFeedBackFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.SpinnerService.hide();
          this.prepareFeedBackFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);
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

  public onOpenModal(task: VisitTask,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='uploadFeedback'){
      this.actionRequest=task;
      button.setAttribute('data-target','#uploadFeedback');
    }
    if (mode==='rejectJustification'){
      this.actionRequest=task;
      button.setAttribute('data-target','#rejectJustification');
    }
    if (mode==='prepDiSdt'){
      this.actionRequest=task;
      button.setAttribute('data-target','#prepDiSdt');
    }

    if (mode==='prepPd'){
      this.actionRequest=task;
      button.setAttribute('data-target','#prepPd');
    }
    if (mode==='approvePd'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approvePd');
    }

    if (mode==='rejectPd'){
      this.actionRequest=task;
      button.setAttribute('data-target','#rejectPd');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }


}

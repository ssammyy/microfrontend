import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {FormBuilder, FormGroup} from "@angular/forms";
import {DecisionOnNotification, NepDraftView} from "../../../../core/store/data/std/std.model";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NepPointService} from "../../../../core/store/data/std/nep-point.service";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";

@Component({
  selector: 'app-nep-upload-notification',
  templateUrl: './nep-upload-notification.component.html',
  styleUrls: ['./nep-upload-notification.component.css']
})
export class NepUploadNotificationComponent implements OnInit {
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  public approveFormGroup!: FormGroup;
  tasks: NepDraftView[]=[];
  public actionRequest: NepDraftView | undefined;
  decisionText: "";
  loadingText: string;
  blob: Blob;
  public uploadedFiles:  FileList;
  selectedOption = '';
  constructor(
      private store$: Store<any>,
      private router: Router,
      private formBuilder: FormBuilder,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private notificationService: NepPointService
  ) { }

  ngOnInit(): void {
    this.approveFormGroup = this.formBuilder.group({
      comments: [],
      accentTo: [],
      notification: [],
      id:[]

    });
    this.getDraftNotificationForUpload();
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }

  public getDraftNotificationForUpload(): void {
    this.loadingText = "Retrieving Notifications...";
    this.SpinnerService.show();
    this.notificationService.getDraftNotificationForUpload().subscribe(
        (response: NepDraftView[]) => {
          this.tasks = response;
          this.rerender();
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }

  rerender(): void {
    this.dtElements.forEach((dtElement: DataTableDirective) => {
      if (dtElement.dtInstance)
        dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
          dtInstance.destroy();
        });
    });
    setTimeout(() => {
      this.dtTrigger.next();
    });
  }

  viewDraftUpload(pdfId: number, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.notificationService.viewDraftUpload(pdfId).subscribe(
        (dataPdf: any) => {
          this.SpinnerService.hide();
          this.blob = new Blob([dataPdf], {type: applicationType});
          let downloadURL = window.URL.createObjectURL(this.blob);
          const link = document.createElement('a');
          link.href = downloadURL;
          link.download = fileName;
          link.click();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Request`);
          console.log(error.message);
        }
    );
  }

  public onOpenModal(task: NepDraftView,mode:string,draftId: number): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='approveDraft'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approveDraft');
      this.approveFormGroup.patchValue(
          {
            id: this.actionRequest.id,
            notification: this.actionRequest.notification
          }
      );
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  @ViewChild('closeModalUploadJustification') private closeModalUploadJustification: ElementRef | undefined;

  public hideModalUploadDraft() {
    this.closeModalUploadJustification?.nativeElement.click();
  }

  public uploadNotification(approveDraft: DecisionOnNotification): void{
    this.SpinnerService.show();
    this.notificationService.uploadNotification(approveDraft).subscribe(
        (response) => {
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, response.body.responseMessage);
          swal.fire({
            text: response.body.responseMessage,
            buttonsStyling: false,
            customClass: {
              confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'success'
          });
          this.getDraftNotificationForUpload();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Action`);
          console.log(error.message);
          this.getDraftNotificationForUpload();
          //alert(error.message);
        }
    );
    this.hideModalUploadDraft();
  }

}

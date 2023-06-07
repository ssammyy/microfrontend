import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {
  COMPreliminaryDraft,
  DecisionOnNotification,
  DecisionOnStdDraft,
  NepDraftView, NepNotificationForm
} from "../../../../core/store/data/std/std.model";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {StdNwaService} from "../../../../core/store/data/std/std-nwa.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {NepPointService} from "../../../../core/store/data/std/nep-point.service";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
declare const $: any;

@Component({
  selector: 'app-nep-notification-view',
  templateUrl: './nep-notification-view.component.html',
  styleUrls: ['./nep-notification-view.component.css']
})
export class NepNotificationViewComponent implements OnInit {
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  public approveFormGroup!: FormGroup;
  public preparePreliminaryDraftFormGroup!: FormGroup;
  tasks: NepNotificationForm[]=[];
  public actionRequest: NepNotificationForm | undefined;

  loadingText: string;
  blob: Blob;
  public uploadedFiles:  FileList;
    decisionText: "";
  selectedOption = '';
  draftDecision : string;
  constructor(
      private store$: Store<any>,
      private router: Router,
      private formBuilder: FormBuilder,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private notificationService: NepPointService
  ) { }

  ngOnInit(): void {
    this.preparePreliminaryDraftFormGroup = this.formBuilder.group({
      notifyingMember : ['', Validators.required],
      agencyResponsible : ['', Validators.required],
      addressOfAgency : ['', Validators.required],
      telephoneOfAgency : ['', Validators.required],
      faxOfAgency : ['', Validators.required],
      emailOfAgency : ['', Validators.required],
      websiteOfAgency : ['', Validators.required],
      notifiedUnderArticle : ['', Validators.required],
      productsCovered : ['', Validators.required],
      descriptionOfNotifiedDoc : ['', Validators.required],
      descriptionOfContent : ['', Validators.required],
      objectiveAndRationale : ['', Validators.required],
      relevantDocuments : ['', Validators.required],
      proposedDateOfAdoption : ['', Validators.required],
      proposedDateOfEntryIntoForce : [],
      textAvailableFrom : ['', Validators.required],
      finalDateForComments : ['', Validators.required],
      comments : [],
      accentTo : ['', Validators.required],
      draftId : ['', Validators.required],

    });
    this.approveFormGroup = this.formBuilder.group({
      comments: [],
      accentTo: [],
      notification: [],
      id:[]

    });
    this.getDraftNotification();
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  onSelected(value:string): void {
    this.selectedOption = value;
  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }
  get formPreparePD(): any {
    return this.preparePreliminaryDraftFormGroup.controls;
  }

  public getDraftNotification(): void {
    this.loadingText = "Retrieving Notifications...";
    this.SpinnerService.show();
    this.notificationService.getDraftNotification().subscribe(
        (response: NepNotificationForm[]) => {
          this.tasks = response;
          this.rerender();
          console.log(response)
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

  public onOpenModal(task: NepNotificationForm,mode:string,draftId: number): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='approveDraft'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approveDraft');
      this.preparePreliminaryDraftFormGroup.patchValue(
          {
            draftId: this.actionRequest.id,
            notifyingMember: this.actionRequest.notifyingMember,
            agencyResponsible: this.actionRequest.agencyResponsible,
            addressOfAgency: this.actionRequest.addressOfAgency,
            telephoneOfAgency: this.actionRequest.telephoneOfAgency,
            faxOfAgency: this.actionRequest.faxOfAgency,
            emailOfAgency: this.actionRequest.emailOfAgency,
            websiteOfAgency: this.actionRequest.websiteOfAgency,
            notifiedUnderArticle: this.actionRequest.notifiedUnderArticle,
            productsCovered: this.actionRequest.productsCovered,
            descriptionOfNotifiedDoc: this.actionRequest.descriptionOfNotifiedDoc,
            descriptionOfContent: this.actionRequest.descriptionOfContent,
            objectiveAndRationale: this.actionRequest.objectiveAndRationale,
            relevantDocuments: this.actionRequest.relevantDocuments,
            proposedDateOfAdoption: this.actionRequest.proposedDateOfAdoption,
            proposedDateOfEntryIntoForce: this.actionRequest.proposedDateOfEntryIntoForce,
              finalDateForComments: this.actionRequest.finalDateForComments,
            textAvailableFrom: this.actionRequest.textAvailableFrom

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
  decisionOnReviewDraft(): void {
    this.loadingText = "Saving...";
    this.SpinnerService.show();
    const valueString=this.preparePreliminaryDraftFormGroup.get("accentTo").value
    if (valueString=="Yes"){
      this.draftDecision="Notification Approved"
      console.log(this.draftDecision)
    }else if(valueString=="No"){
      this.draftDecision="Notification was not approved"
      //console.log(this.draftDecision)

    }
    this.notificationService.decisionOnReviewDraft(this.preparePreliminaryDraftFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, this.draftDecision);
          this.preparePreliminaryDraftFormGroup.reset();

        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Try Again`);
          console.log(error.message);
        }
    );
    this.hideModalUploadDraft();
  }

  // public decisionOnReviewDraft(approveDraft: DecisionOnNotification): void{
  //   this.SpinnerService.show();
  //   this.notificationService.decisionOnReviewDraft(approveDraft).subscribe(
  //       (response) => {
  //         this.SpinnerService.hide();
  //         this.showToasterSuccess(response.httpStatus, response.body.responseMessage);
  //         swal.fire({
  //           text: response.body.responseMessage,
  //           buttonsStyling: false,
  //           customClass: {
  //             confirmButton: 'btn btn-success form-wizard-next-btn ',
  //           },
  //           icon: 'success'
  //         });
  //         this.getDraftNotification();
  //       },
  //       (error: HttpErrorResponse) => {
  //         this.SpinnerService.hide();
  //         this.showToasterError('Error', `Error Processing Action`);
  //         console.log(error.message);
  //         this.getDraftNotification();
  //         //alert(error.message);
  //       }
  //   );
  //   this.hideModalUploadDraft();
  // }

  showNotification(from: any, align: any) {
    const type = ['', 'info', 'success', 'warning', 'danger', 'rose', 'primary'];

    const color = Math.floor((Math.random() * 6) + 1);

    $.notify({
      icon: 'notifications',
      message: 'Welcome to <b>Material Dashboard</b> - a beautiful dashboard for every web developer.'
    }, {
      type: type[color],
      timer: 3000,
      placement: {
        from: from,
        align: align
      },
      template: '<div data-notify="container" class="col-xs-11 col-sm-3 alert alert-{0} alert-with-icon" role="alert">' +
          '<button mat-raised-button type="button" aria-hidden="true" class="close" data-notify="dismiss">  <i class="material-icons">close</i></button>' +
          '<i class="material-icons" data-notify="icon">notifications</i> ' +
          '<span data-notify="title"></span> ' +
          '<span data-notify="message">Ensure all required fields and items have been filled</span>' +
          '<div class="progress" data-notify="progressbar">' +
          '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" ></div>' +
          '</div>' +
          '<a href="{3}" target="{4}" data-notify="url"></a>' +
          '</div>'
    });
  }

}

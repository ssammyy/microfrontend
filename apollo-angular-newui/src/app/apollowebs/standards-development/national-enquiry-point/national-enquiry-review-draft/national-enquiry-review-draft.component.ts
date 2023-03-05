import { Component, OnInit } from '@angular/core';
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
import {NepPointService} from "../../../../core/store/data/std/nep-point.service";
declare const $: any;

@Component({
  selector: 'app-national-enquiry-review-draft',
  templateUrl: './national-enquiry-review-draft.component.html',
  styleUrls: ['./national-enquiry-review-draft.component.css']
})
export class NationalEnquiryReviewDraftComponent implements OnInit {
  blob: Blob;
  public uploadedFiles:  FileList;
  loadingText: string;
  public preparePreliminaryDraftFormGroup!: FormGroup;
  addressOfAgency: string;
  telephoneOfAgency: string;
  faxOfAgency: string;
  emailOfAgency: string;
  websiteOfAgency: string;

  constructor(
      private store$: Store<any>,
      private router: Router,
      private formBuilder: FormBuilder,
      private notificationService: NepPointService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
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
      finalDateForComments : ['', Validators.required],
      textAvailableFrom : ['', Validators.required],

    });

    this.addressOfAgency="P.O. Box: 54974-00200, Nairobi, Kenya"
    this.telephoneOfAgency="+ (254) 020 605490, 605506/6948258"
    this.faxOfAgency="+ (254) 020 609660/609665"
    this.emailOfAgency="info@kebs.org"
    this.websiteOfAgency="Website: http://www.kebs.org"
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
  get formPreparePD(): any {
    return this.preparePreliminaryDraftFormGroup.controls;
  }
  notificationOfReview(): void {
    this.loadingText = "Saving...";
    this.SpinnerService.show();
    this.notificationService.notificationOfReview(this.preparePreliminaryDraftFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Preliminary Draft  Uploaded`);
          this.preparePreliminaryDraftFormGroup.reset();
          this.onClickSaveUploads(response.body.id)

        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Preliminary Draft Was Not Prepared`);
          console.log(error.message);
        }
    );
  }

  onClickSaveUploads(draftId: string) {
    if (this.uploadedFiles.length > 0) {
      const file = this.uploadedFiles;
      const formData = new FormData();
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }
      this.SpinnerService.show();
      this.notificationService.uploadDraft(draftId, formData).subscribe(
          (data: any) => {
            this.SpinnerService.hide();
            this.uploadedFiles = null;
            console.log(data);
            swal.fire({
              title: 'Thank you....',
              html:'Uploaded',
              buttonsStyling: false,
              customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
              },
              icon: 'success'
            }).then(r => this.preparePreliminaryDraftFormGroup.reset());
          },
      );
    }

  }

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

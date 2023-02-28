import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {NepPointService} from "../../../../core/store/data/std/nep-point.service";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {ComStdRequest, Countries} from "../../../../core/store/data/std/std.model";
declare const $: any;
@Component({
  selector: 'app-make-enquiry',
  templateUrl: './make-enquiry.component.html',
  styleUrls: ['./make-enquiry.component.css']
})
export class MakeEnquiryComponent implements OnInit {
  blob: Blob;
  public uploadedFiles:  FileList;
  loadingText: string;
  selectedUser: number;
  @ViewChild('uploadFile') myInputVariable: ElementRef;
  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private notificationService: NepPointService,
              private notifyService : NotificationService,
              private SpinnerService: NgxSpinnerService,
  ) { }
  public enquiryFormGroup!: FormGroup;
  public demoFormGroup!: FormGroup;

  ngOnInit(): void {
    this.demoFormGroup = this.formBuilder.group({
      uploadedFiles: ['']
    });
    this.enquiryFormGroup = this.formBuilder.group({
      requesterName: ['', Validators.required],
      requesterComment: ['', Validators.required],
      requesterCountry: [],
      requesterEmail: ['', Validators.required],
      requesterInstitution: ['', Validators.required],
      requesterPhone: ['', Validators.required],
      requesterSubject: ['', Validators.required]
    });
  }

  get formEnquiryFormGroup(): any {
    return this.enquiryFormGroup.controls;
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

  sendEnquiry(): void{
    this.loadingText = "Saving Enquiry ...."
    this.SpinnerService.show();
    this.notificationService.makeEnquiry(this.enquiryFormGroup.value).subscribe(
        (response) => {
          this.showToasterSuccess(response.httpStatus, `Enquiry Sent`);
          this.SpinnerService.hide();
          swal.fire({
            title: 'Thank you....',
            html:'Your enquiry has been successfully sent. A response shall be made to your E-mail Address',
            buttonsStyling: false,
            customClass: {
              confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'success'
          }).then(r => this.enquiryFormGroup.reset());
          this.onClickSaveUploads(response.body.id)

        },
        (error: HttpErrorResponse) => {
          alert(error.message);
          this.SpinnerService.hide();
        }
    );
  }

  onClickSaveUploads(enquiryId: string) {
    if (this.uploadedFiles.length > 0) {
      const file = this.uploadedFiles;
      const formData = new FormData();
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }
      this.SpinnerService.show();
      this.notificationService.uploadAttachment(enquiryId, formData).subscribe(
          (data: any) => {
            this.SpinnerService.hide();
            this.uploadedFiles = null;
            swal.fire({
              title: 'Thank you....',
              html:'Attachment has been uploaded',
              buttonsStyling: false,
              customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
              },
              icon: 'success'
            }).then(r => this.enquiryFormGroup.reset());

            this.myInputVariable.nativeElement.value = '';
          },
          (error: HttpErrorResponse) => {
            alert(error.message);
            this.SpinnerService.hide();
          }
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

import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {NepPointService} from "../../../../core/store/data/std/nep-point.service";
import {RootObject} from "../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
declare const $: any;
@Component({
  selector: 'app-make-enquiry',
  templateUrl: './make-enquiry.component.html',
  styleUrls: ['./make-enquiry.component.css']
})
export class MakeEnquiryComponent implements OnInit {
  blob: Blob;
  public uploadedFiles:  FileList;

  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private notificationService: NepPointService,
              private notifyService : NotificationService,
              private SpinnerService: NgxSpinnerService,
  ) { }
  public enquiryFormGroup!: FormGroup;

  ngOnInit(): void {
    this.enquiryFormGroup = this.formBuilder.group({
      requesterName: [''],
      requesterComment: [''],
      requesterCountry: [''],
      requesterEmail: [''],
      requesterInstitution: [''],
      requesterPhone: [''],
      requesterSubject: ['']
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

  sendEnquiry(): void{

    this.notificationService.makeEnquiry(this.enquiryFormGroup.value).subscribe(
        (response) => {
          this.showToasterSuccess(response.httpStatus, `Enquiry Sent`);
          this.onClickSaveUploads(response.body.id)
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
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
            console.log(data);
            swal.fire({
              title: 'Thank you....',
              html:'Your enquiry has been successfully sent. A response shall be made to your E-mail Address',
              buttonsStyling: false,
              customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
              },
              icon: 'success'
            }).then(r => this.enquiryFormGroup.reset());
          },
      );
    }

  }

}

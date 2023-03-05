import {Component, Input, OnInit} from '@angular/core';
import {ErrorStateMatcher} from "@angular/material/core";
import {FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {SchemeMembershipService} from "../../../../core/store/data/std/scheme-membership.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import swal from "sweetalert2";
import {HttpErrorResponse} from "@angular/common/http";
import {QaService} from "../../../../core/store/data/qa/qa.service";

export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}
@Component({
  selector: 'app-request-for-standard',
  templateUrl: './request-for-standard.component.html',
  styleUrls: ['./request-for-standard.component.css']
})


export class RequestForStandardComponent implements OnInit {

  @Input() errorMsg: string;
  @Input() displayError: boolean;

  levelOfStandards: string[] = ['Corporate Membership', 'Individual Membership']
  title = 'toaster-not';

  emailFormControl = new FormControl('', [
    Validators.required,
    Validators.email,
  ]);

  validEmailRegister: boolean = false;

  validTextType: boolean = false;
  validNumberType: boolean = false;
  pattern = "https?://.+";


  matcher = new MyErrorStateMatcher();
  register: FormGroup;
  stdRequestFormGroup: FormGroup;


  isFormSubmitted = false;

  public uploadedFiles: Array<File> = [];


  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private schemeMembershipService: SchemeMembershipService,
              private SpinnerService: NgxSpinnerService,

              private qaService:QaService,
              private notifyService: NotificationService) {
  }

  isFieldValid(form: FormGroup, field: string) {
    return !form.get(field).valid && form.get(field).touched;
  }

  displayFieldCss(form: FormGroup, field: string) {
    return {
      'has-error': this.isFieldValid(form, field),
      'has-feedback': this.isFieldValid(form, field)
    };
  }


  validateAllFormFields(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach(field => {
      const control = formGroup.get(field);
      if (control instanceof FormControl) {
        control.markAsTouched({onlySelf: true});
      } else if (control instanceof FormGroup) {
        this.validateAllFormFields(control);
      }
    });
  }

  ngOnInit(): void {
    this.stdRequestFormGroup = this.formBuilder.group({

      email: [null, [Validators.required, Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$")]],
      name: ['', Validators.required],
      designationOccupation: ['', Validators.required],
      phone: ['', Validators.required],
      address: ['', Validators.required],
      natureOfRequest: ['', Validators.required],
      companyName: ['', Validators.required],
    });
  }

  showToasterSuccess(title: string, message: string) {
    this.notifyService.showSuccess(message, title)

  }

  showToasterError(title: string, message: string) {
    this.notifyService.showError(message, title)

  }

  get formStdRequest(): any {
    return this.stdRequestFormGroup.controls;
  }

  emailValidationRegister(e) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if (re.test(String(e).toLowerCase())) {
      this.validEmailRegister = true;
    } else {
      this.validEmailRegister = false;
    }
  }


  textValidationType(e) {
    if (e) {
      this.validTextType = true;
    } else {
      this.validTextType = false;
    }
  }

  numberValidationType(e) {
    if (e) {
      this.validNumberType = true;
    } else {
      this.validNumberType = false;
    }
  }

  saveStandard(formDirective): void {
    this.isFormSubmitted = true;

    this.qaService.showSuccess("Request Submitted Successfully");
    this.stdRequestFormGroup.reset()

    // if (this.stdRequestFormGroup.valid) {
    //
    //   // if (this.uploadedFiles != null && this.uploadedFiles.length > 0) {
    //   //     this.onClickSaveUploads("568","Marvin")
    //   // }
    //
    //   this.SpinnerService.show();
    //   this.schemeMembershipService.addStandardRequest(this.stdRequestFormGroup.value).subscribe(
    //       (response) => {
    //
    //         if (response.body == 'You Have Already Registered') {
    //           this.showToasterError(response.httpStatus, `You Have Already Registered`);
    //           this.SpinnerService.hide();
    //
    //         } else {
    //           this.showToasterSuccess(response.httpStatus, `Successfully submitted your application`);
    //           this.SpinnerService.hide();
    //           formDirective.resetForm();
    //           this.isFormSubmitted = false;
    //           this.stdRequestFormGroup.reset()
    //           swal.fire({
    //             title: 'Your Application Has Been Submitted.',
    //             buttonsStyling: false,
    //             customClass: {
    //               confirmButton: 'btn btn-success form-wizard-next-btn ',
    //             },
    //             icon: 'success'
    //           });
    //         }
    //
    //       },
    //       (error: HttpErrorResponse) => {
    //         this.SpinnerService.hide();
    //
    //         alert(error.message);
    //       }
    //   );
    //
    // } else {
    //   this.validateAllFormFields(this.stdRequestFormGroup);
    // }


  }

}


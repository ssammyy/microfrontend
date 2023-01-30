import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, NgForm, Validators} from "@angular/forms";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {MembershipToTcService} from "../../../../core/store/data/std/membership-to-tc.service";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {CallForApplication} from "../../../../core/store/data/std/request_std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {DatePipe} from "@angular/common";
import swal from "sweetalert2";
import {TechnicalCommittee} from "../../../../core/store/data/std/std.model";

@Component({
  selector: 'app-submit-application',
  templateUrl: './submit-application.component.html',
  styleUrls: ['./submit-application.component.css']
})
export class SubmitApplicationComponent implements OnInit {
  public submitApplicationsTask: TechnicalCommittee[] = [];
  public actionRequest: TechnicalCommittee | undefined;
  public prepareDraftStandardFormGroup!: FormGroup;
  public uploadedFiles: FileList;
  loadingText: string;
  @ViewChild('demoForm', {
    static: false
  }) editForm: NgForm;


  constructor(private formBuilder: FormBuilder,
              private standardDevelopmentService: StandardDevelopmentService,
              private membershipToTcService: MembershipToTcService,
              private store$: Store<any>,
              private router: Router,
              private datePipe: DatePipe,
              private notifyService: NotificationService,
              private SpinnerService: NgxSpinnerService) {
  }

  ngOnInit(): void {
    this.getApplicantTasks();
    this.prepareDraftStandardFormGroup = this.formBuilder.group({
      name: this.formBuilder.group({
        firstName: ['', Validators.required],
        lastName: ['', Validators.required],
      }),
      nomineeName: new FormControl(null, Validators.required),
      technicalCommittee: ['', Validators.required],
      email: ['', Validators.required],
      mobileNumber: ['', Validators.required],
      postalAddress: ['', Validators.required],
      taskId: ['', Validators.required],
      tcApplicationId: ['', Validators.required],
      authorisingPersonPosition: ['', Validators.required],
      authorizingName: ['', Validators.required],

    })
    this.prepareDraftStandardFormGroup.get('name').valueChanges.subscribe((value) => {
      this.prepareDraftStandardFormGroup.get('nomineeName').setValue(this.prepareDraftStandardFormGroup.get('name').get('firstName').value + " " + this.prepareDraftStandardFormGroup.get('name').get('lastName').value)
    });
  }

  public getApplicantTasks(): void {
    this.membershipToTcService.getApplicantTasks().subscribe(
        (response: TechnicalCommittee[]) => {
          console.log(response);
          this.submitApplicationsTask = response;
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    )
  }

  formatDate(date: string): string {


      const myArray = date.split("T");
      return myArray[0] // syntax for datepipe

    // return date
  }

  showToasterSuccess(title: string, message: string) {
    this.notifyService.showSuccess(message, title)
  }

  showToasterError(title: string, message: string) {
    this.notifyService.showError(message, title)

  }

  get formPrepareJustification(): any {
    return this.prepareDraftStandardFormGroup.controls;
  }


  public onOpenModal(task: TechnicalCommittee, mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    console.log(task.id);
    if (mode === 'edit') {
      this.actionRequest = task;
      button.setAttribute('data-target', '#justificationDecisionModal');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  onUpload(): void {
    if (this.uploadedFiles != null) {
      if (this.uploadedFiles.length > 0) {
        this.SpinnerService.show();
        this.membershipToTcService.onSubmitApplication(this.prepareDraftStandardFormGroup.value).subscribe(
            (response) => {
              console.log(response);
              this.SpinnerService.hide();
              this.showToasterSuccess(response.httpStatus, `Successfully submitted your application`);
              this.onClickSaveUploads(response.body.savedRowID, this.prepareDraftStandardFormGroup.get("nomineeName").value.toString())
              this.prepareDraftStandardFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
              this.SpinnerService.hide();
              console.log(error.message);
            }
        );
      } else {
        this.showToasterError("Error", `Please Upload Your CV.`);
      }
    } else {
      this.showToasterError("Error", `Please Upload Your CV.`);
    }
  }

  onClickSaveUploads(draftStandardID: string, nomineeName: string) {
    if (this.uploadedFiles.length > 0) {
      const file = this.uploadedFiles;
      const formData = new FormData();
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }
      this.SpinnerService.show();
      this.membershipToTcService.uploadFileDetails(draftStandardID, formData, "ApplicantCV", nomineeName).subscribe(
          (data: any) => {
            this.SpinnerService.hide();
            this.uploadedFiles = null;
            console.log(data);
            this.editForm.reset;

            swal.fire({
              title: 'Your Application Has Been Submitted.',
              buttonsStyling: false,
              customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
              },
              icon: 'success'
            });
          },
      );
    }

  }

}

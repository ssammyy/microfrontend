import {Component, OnInit, ViewChild} from '@angular/core';
import {SACSummaryTask, StandardDraft} from "../../../../core/store/data/std/request_std.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
import {AdoptionOfEaStdsService} from "../../../../core/store/data/std/adoption-of-ea-stds.service";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {Department, TechnicalCommittee} from "../../../../core/store/data/std/std.model";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";

declare const $: any;

@Component({
  selector: 'app-upload-sac-summary',
  templateUrl: './upload-sac-summary.component.html',
  styleUrls: ['./upload-sac-summary.component.css']
})

export class UploadSacSummaryComponent implements OnInit {

  public tasks: SACSummaryTask[] = [];
  public actionRequest: SACSummaryTask | undefined;
  public groupId: string = "draft";
  public type: string = "DraftDocument";
  public stdDraft !: StandardDraft | undefined;
  public prepareSacSummaryFormGroup!: FormGroup;
  public uploadedFiles: FileList;
  fullname = '';
  title = 'toaster-not';
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false
  public itemId: number;
  loadingText: string;
  blob: Blob;
  public technicalCommittees !: TechnicalCommittee[];
  public departments !: Department[];


  constructor(private  adoptionOfEaStdsService: AdoptionOfEaStdsService,
              private formBuilder: FormBuilder,
              private standardDevelopmentService: StandardDevelopmentService,
              private notifyService: NotificationService,
              private SpinnerService: NgxSpinnerService,
  ) {
  }

  ngOnInit(): void {
    this.getSACTasks();
    this.getDepartments();


    this.prepareSacSummaryFormGroup = this.formBuilder.group({
      sl: ['', Validators.required],
      ks: ['', Validators.required],
      title: ['', Validators.required],
      edition: ['', Validators.required],
      departmentId: ['', Validators.required],
      technicalCommitteeId: ['', Validators.required],
      issuesAddressed: ['', Validators.required],
      backgroundInformation: ['', Validators.required],
      feedback: ['', Validators.required],
      referenceMaterial: ['', Validators.required]


    });
  }

  public getDepartments(): void {
    this.standardDevelopmentService.getDepartmentsb().subscribe(
        (response: Department[]) => {
          this.departments = response;
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    )
  }

  onSelectDepartment(value: any): any {
    this.standardDevelopmentService.getTechnicalCommitteeb(value).subscribe(
        (response: TechnicalCommittee[]) => {
          this.technicalCommittees = response
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }

  public getSACTasks(): void {
    this.loadingText = "Retrieving Data Please Wait ...."
    this.SpinnerService.show();
    this.adoptionOfEaStdsService.getSACSummaryTask().subscribe(
        (response: SACSummaryTask[]) => {
          this.tasks = response;
          console.log(response)
          this.SpinnerService.hide();

          if (this.isDtInitialized) {
            this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
              dtInstance.destroy();
              this.dtTrigger.next();
            });
          } else {
            this.isDtInitialized = true
            this.dtTrigger.next();
          }
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
          this.SpinnerService.hide();
        }
    );
  }

  showToasterSuccess(title: string, message: string) {
    this.notifyService.showSuccess(message, title)
  }

  showToasterError(title: string, message: string) {
    this.notifyService.showError(message, title)

  }

  get formPrepareSacSummary(): any {
    return this.prepareSacSummaryFormGroup.controls;
  }

  saveSacSummary(): void {
    console.log(this.uploadedFiles.length)
    if (this.uploadedFiles != null) {
      if (this.uploadedFiles.length > 0 || this.uploadedFiles.length != 2) {
        this.SpinnerService.show();
        this.adoptionOfEaStdsService.uploadSACSummary(this.prepareSacSummaryFormGroup.value).subscribe(
            (response) => {
              console.log(response);
              this.SpinnerService.hide();
              this.showToasterSuccess(response.httpStatus, `Successfully submitted SAC Summary`);
              this.onClickSaveUploads(response.body.savedRowID)
              this.prepareSacSummaryFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
              this.SpinnerService.hide();
              console.log(error.message);
            }
        );
      } else {
        this.showToasterError("Error", `Please Upload all the documents.`);
      }
    } else {
      this.showToasterError("Error", `Please Upload all the documents.`);
    }
  }

  onClickSaveUploads(draftStandardID: string) {
    if (this.uploadedFiles.length > 0) {
      const file = this.uploadedFiles;
      const formData = new FormData();
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }
      this.SpinnerService.show();
      this.adoptionOfEaStdsService.uploadFileDetails(draftStandardID, formData, "EAC Gazette", "EAC Gazette").subscribe(
          (data: any) => {
            this.SpinnerService.hide();
            this.uploadedFiles = null;
            console.log(data);
            swal.fire({
              title: 'SAC Summary Submitted To SPC For Approval.',
              buttonsStyling: false,
              customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
              },
              icon: 'success'
            });
            // this.router.navigate(['/draftStandard']);
          },
      );
    }

  }

  showNotification(from: any, align: any) {
    const type = ['', 'info', 'success', 'warning', 'danger', 'rose', 'primary'];

    const color = Math.floor((Math.random() * 6) + 1);

    $.notify({
      icon: 'notifications',
      message: 'KEBS QAIMSS'
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
          '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>' +
          '</div>' +
          '<a href="{3}" target="{4}" data-notify="url"></a>' +
          '</div>'
    });
  }
}


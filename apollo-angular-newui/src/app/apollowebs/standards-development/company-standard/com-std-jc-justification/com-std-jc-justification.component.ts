import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import Swal from "sweetalert2";
import {HttpErrorResponse} from "@angular/common/http";
import {ComHodTasks, ComJcJustification, ComJcJustificationAction} from "../../../../core/store/data/std/std.model";
import {User} from "../../../../core/store";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import swal from "sweetalert2";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

declare const $: any;
@Component({
  selector: 'app-com-std-jc-justification',
  templateUrl: './com-std-jc-justification.component.html',
  styleUrls: ['./com-std-jc-justification.component.css']
})
export class ComStdJcJustificationComponent implements OnInit, OnDestroy {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ComHodTasks[] = [];
  public actionRequest: ComHodTasks | undefined;
  public prepareJustificationFormGroup!: FormGroup;
  public preparePreliminaryDraftFormGroup!: FormGroup;
  user?: User;
  public uploadedFiles: FileList;
  constructor(
     // private usersService: UsersService,
      private router: Router,
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
     private formBuilder: FormBuilder,
  ) {
    //this.user = this.usersService.userValue;
  }

  ngOnInit(): void {
    this.getPlTasks();

    this.prepareJustificationFormGroup = this.formBuilder.group({
      meetingDate: ['', Validators.required],
      slNumber: ['', Validators.required],
      requestNumber: [],
      requestedBy: [],
      status: [],
      remarks: [],
      preparedBy: [],
      department: [],
      projectLeader: [],
      taskId: [],
      issuesAddressed: ['', Validators.required],
      tcAcceptanceDate: ['', Validators.required],
      reason: ['', Validators.required],
      uploadedFiles: []
      // postalAddress: ['', [Validators.required, Validators.pattern('P.O.BOX [0-9]{5}')]]
    });
    this.preparePreliminaryDraftFormGroup = this.formBuilder.group({
      title: ['', Validators.required],
      scope: ['', Validators.required],
      normativeReference: ['', Validators.required],
      symbolsAbbreviatedTerms: ['', Validators.required],
      clause: ['', Validators.required],
      special: ['', Validators.required],
      taskId: [],
      diJNumber: []

    });
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
  get formPrepareJustification(): any {
    return this.prepareJustificationFormGroup.controls;
  }
  get formPreparePD(): any {
    return this.preparePreliminaryDraftFormGroup.controls;
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
  public getPlTasks(): void{
    this.SpinnerService.show();
    this.stdComStandardService.getPlTasks().subscribe(
        (response: ComHodTasks[])=> {
          this.SpinnerService.hide();
          this.dtTrigger.next();
          this.tasks = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  public onOpenModal(task: ComHodTasks,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='prepJcJustification'){
      this.actionRequest=task;
      button.setAttribute('data-target','#prepJcJustification');
    }
    if (mode==='prepPd'){
      this.actionRequest=task;
      button.setAttribute('data-target','#prepPd');
    }
    if (mode==='approvePd'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approvePd');
    }

    if (mode==='reject'){
      this.actionRequest=task;
      button.setAttribute('data-target','#reject');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  saveJustification(): void {
    this.SpinnerService.show();
    this.stdComStandardService.prepareJustification(this.prepareJustificationFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.SpinnerService.hide();
          //this.showToasterSuccess(response.httpStatus, `Request Number is ${response.body.requestNumber}`);
          this.onClickSaveUploads(response.body.savedRowID)
          this.prepareJustificationFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }


  onClickSaveUploads(comJustificationID: string) {
    if (this.uploadedFiles.length > 0) {
      const file = this.uploadedFiles;
      const formData = new FormData();
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }

      this.SpinnerService.show();
      this.stdComStandardService.uploadFileDetails(comJustificationID, formData).subscribe(
          (data: any) => {
            this.SpinnerService.hide();
            this.uploadedFiles = null;
            console.log(data);
            swal.fire({
              title: 'Justification Prepared.',
              buttonsStyling: false,
              customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
              },
              icon: 'success'
            });
            this.router.navigate(['/comStdJustification']);
          },
      );
    }

  }
  uploadPreliminaryDraft(): void {
    this.SpinnerService.show();
    //console.log(this.preparePreliminaryDraftFormGroup.value);
    this.stdComStandardService.prepareCompanyPreliminaryDraft(this.preparePreliminaryDraftFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Company Draft Preparation Process Started`);
          this.onClickSaveUPLOADS(response.body.savedRowID)
          this.preparePreliminaryDraftFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Company Draft Was Not Prepared`);
          console.log(error.message);
        }
    );
  }
  onClickSaveUPLOADS(comPreID: string) {
    if (this.uploadedFiles.length > 0) {
      const file = this.uploadedFiles;
      const formData = new FormData();
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }

      this.SpinnerService.show();
      this.stdComStandardService.uploadPDFileDetails(comPreID, formData).subscribe(
          (data: any) => {
            this.SpinnerService.hide();
            this.showToasterSuccess(data.httpStatus, `Company Draft Prepared`);
            this.uploadedFiles = null;
            console.log(data);
            swal.fire({
              title: 'Company Draft Prepared.',
              buttonsStyling: false,
              customClass: {
                confirmButton: 'btn btn-success ',
              },
              icon: 'success'
            });
           // this.router.navigate(['/comStdJustification']);
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

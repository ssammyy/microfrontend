import {Component, OnDestroy, OnInit} from '@angular/core';
import {StdNwaService} from "../../../../core/store/data/std/std-nwa.service";
import {NgxSpinnerService} from "ngx-spinner";
import {
    KnwSecTasks,
    NWAPDDecision,
    NWAPreliminaryDraft
} from "../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import { Subject} from "rxjs";
import swal from "sweetalert2";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {Router} from "@angular/router";

declare const $: any;

@Component({
  selector: 'app-nwa-knw-sec-tasks',
  templateUrl: './nwa-knw-sec-tasks.component.html',
  styleUrls: ['./nwa-knw-sec-tasks.component.css']
})
export class NwaKnwSecTasksComponent implements OnInit,OnDestroy {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  p = 1;
  p2 = 1;
  tasks: KnwSecTasks[] = [];
  public actionRequest: KnwSecTasks | undefined;
    public uploadedFiles:  FileList;
    public prepareDIJustificationFormGroup!: FormGroup;
    public preparePreliminaryDraftFormGroup!: FormGroup;
  constructor(
      private router: Router,
      private formBuilder: FormBuilder,
      private stdNwaService: StdNwaService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.knwtasks();

      this.prepareDIJustificationFormGroup = this.formBuilder.group({
          cost: ['', Validators.required],
          numberOfMeetings: ['', Validators.required],
          identifiedNeed: ['', Validators.required],
          dateOfApproval: ['', Validators.required],
          taskId: [],
          jstNumber: []

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
    showToasterSuccess(title:string,message:string){
        this.notifyService.showSuccess(message, title)

    }
    showToasterError(title:string,message:string){
        this.notifyService.showError(message, title)

    }
    showToasterWarning(title:string,message:string){
        this.notifyService.showWarning(message, title)

    }
    get formPrepareJustification(): any {
        return this.prepareDIJustificationFormGroup.controls;
    }
    get formPreparePD(): any {
        return this.preparePreliminaryDraftFormGroup.controls;
    }
  public knwtasks(): void{
    this.SpinnerService.show();
    this.stdNwaService.knwtasks().subscribe(
        (response: KnwSecTasks[])=> {
          this.tasks = response;
          this.dtTrigger.next();
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
            this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  public onOpenModal(task: KnwSecTasks,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='prepareJustification'){
      this.actionRequest=task;
      button.setAttribute('data-target','#prepareJustification');
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

    if (mode==='reject'){
      this.actionRequest=task;
      button.setAttribute('data-target','#reject');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

    uploadDiSdt(): void {

        this.SpinnerService.show();
        this.stdNwaService.prepareDisDtJustification(this.prepareDIJustificationFormGroup.value).subscribe(
            (response ) => {
                console.log(response);

                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Justification For Di-SDT Approval  Process Started`);
                this.onClickSaveUPLOADS(response.body.savedRowID)
                this.prepareDIJustificationFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Justification Was Not Prepared`);
                console.log(error.message);
            }
        );
    }

    uploadPreliminaryDraft(): void {
        this.SpinnerService.show();
        //console.log(this.preparePreliminaryDraftFormGroup.value);
        this.stdNwaService.preparePreliminaryDraft(this.preparePreliminaryDraftFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Preliminary Draft Preparation Process Started`);
                this.onClickPDUPLOADs(response.body.savedRowID)
                this.preparePreliminaryDraftFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Preliminary Draft Was Not Prepared`);
                console.log(error.message);
            }
        );
    }


  public approvePreliminaryDraft(nwaPDDecision: NWAPDDecision): void{
    this.SpinnerService.show();
    this.stdNwaService.decisionOnPD(nwaPDDecision).subscribe(
        (response: NWAPreliminaryDraft) => {
          console.log(response);
            this.showToasterSuccess('Success', `Preliminary Draft Approved`);
          this.SpinnerService.hide();
          this.knwtasks();
        },
        (error: HttpErrorResponse) => {
            this.SpinnerService.hide();
            this.showToasterError('Error', `Preliminary Draft Was Not Approved`);
            this.knwtasks();
          alert(error.message);
        }
    );
  }
    public rejectPreliminaryDraft(nwaPDDecision: NWAPDDecision): void{
        this.SpinnerService.show();
        this.stdNwaService.decisionOnPD(nwaPDDecision).subscribe(
            (response: NWAPreliminaryDraft) => {
                console.log(response);
                this.showToasterWarning('Success', `Preliminary Draft Rejected`);
                this.SpinnerService.hide();
                this.knwtasks();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Try again`);
                this.knwtasks();
                alert(error.message);
            }
        );
    }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }


    onClickSaveUPLOADS(nwaDiSdtJustificationID: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }

            this.SpinnerService.show();
            this.stdNwaService.uploadDIFileDetails(nwaDiSdtJustificationID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.showToasterSuccess(data.httpStatus, `Justification For Di-SDT Approval Prepared`);
                    this.uploadedFiles = null;
                    console.log(data);
                    swal.fire({
                        title: 'Justification For Di-SDT Approval Prepared.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success ',
                        },
                        icon: 'success'
                    });
                    this.router.navigate(['/nwaKnwSecTasks']);
                },
            );
        }

    }
    onClickPDUPLOADs(nwaPDid: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }

            this.SpinnerService.show();
            this.stdNwaService.uploadPDFileDetails(nwaPDid, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.showToasterSuccess(data.httpStatus, `Preliminary Draft Prepared`);
                    this.uploadedFiles = null;
                    console.log(data);
                    swal.fire({
                        title: 'Preliminary Draft Prepared.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success ',
                        },
                        icon: 'success'
                    });

                },
            );
        }

    }


    // onClickSaveUPLOADS(nwaJustificationID: string) {
    //
    //     const file = this.uploadedFiles;
    //     this.fileName = this.uploadedFiles[0].name;
    //     console.log(this.uploadedFiles);
    //
    //
    //    if (this.uploadedFiles) {
    //        const formData = new FormData();
    //        formData.append('docFile', file[0], file[0].name);
    //
    //         //const formData = new FormData();
    //         // for (let i = 0; i < file.length; i++) {
    //         //     console.log(file[i]);
    //         //     formData.append('docFile', file[i], file[i].name);
    //         // }
    //
    //
    //     }
    //
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

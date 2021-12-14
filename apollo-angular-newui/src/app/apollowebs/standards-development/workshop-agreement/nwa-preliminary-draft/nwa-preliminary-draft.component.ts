import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {PreliminaryDraftTasks} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {StdNwaService} from "../../../../core/store/data/std/std-nwa.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";

declare const $: any;

@Component({
  selector: 'app-nwa-preliminary-draft',
  templateUrl: './nwa-preliminary-draft.component.html',
  styleUrls: ['./nwa-preliminary-draft.component.css']
})
export class NwaPreliminaryDraftComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  displayTable: boolean = false;
  blob: Blob;
  tasks: PreliminaryDraftTasks[]=[];
  public actionRequest: PreliminaryDraftTasks | undefined;
  public uploadedFiles:  FileList;
  public preparePreliminaryDraftFormGroup!: FormGroup;
  constructor(
      private router: Router,
      private formBuilder: FormBuilder,
      private stdNwaService: StdNwaService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getTCSeCTasks();

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
  get formPreparePD(): any {
    return this.preparePreliminaryDraftFormGroup.controls;
  }
  public getTCSeCTasks(): void{
    this.SpinnerService.show();
    this.stdNwaService.getTCSeCTasks().subscribe(
        (response: PreliminaryDraftTasks[])=> {
          this.tasks = response;
          this.dtTrigger.next();
          this.displayTable = true;
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);
          //alert(error.message);
        }
    );
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
  public onOpenModal(task: PreliminaryDraftTasks,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');

    if (mode==='preparePD'){
      this.actionRequest=task;
      button.setAttribute('data-target','#prepPd');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

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
  viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.stdNwaService.viewDIJustificationPDF(pdfId).subscribe(
        (dataPdf: any) => {
          this.SpinnerService.hide();
          this.blob = new Blob([dataPdf], {type: applicationType});

          // tslint:disable-next-line:prefer-const
          let downloadURL = window.URL.createObjectURL(this.blob);
          const link = document.createElement('a');
          link.href = downloadURL;
          link.download = fileName;
          link.click();
          // this.pdfUploadsView = dataPdf;
        },
    );
  }

}

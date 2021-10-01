import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {ComJcJustificationDec} from "../../../../core/store/data/std/std.model";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
declare const $: any;

@Component({
  selector: 'app-com-std-upload',
  templateUrl: './com-std-upload.component.html',
  styleUrls: ['./com-std-upload.component.css']
})
export class ComStdUploadComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ComJcJustificationDec[] = [];
  public actionRequest: ComJcJustificationDec | undefined;
  public prepareJustificationFormGroup!: FormGroup;
  public prepareStandardFormGroup!: FormGroup;
  public uploadedFiles: FileList;
  constructor(
      private router: Router,
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder
  ){}

  ngOnInit(): void {
    this.getComSecTasks();
    this.prepareStandardFormGroup = this.formBuilder.group({
      title: ['', Validators.required],
      scope: ['', Validators.required],
      normativeReference: ['', Validators.required],
      referenceMaterial: ['', Validators.required],
      clause: ['', Validators.required],
      special: ['', Validators.required],
      taskId: []

    });
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)
  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  get formPrepareSD(): any {
    return this.prepareStandardFormGroup.controls;
  }
  public getComSecTasks(): void{
    this.SpinnerService.show();
    this.stdComStandardService.getComSecTasks().subscribe(
        (response: ComJcJustificationDec[])=> {
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
  public onOpenModal(task: ComJcJustificationDec,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='uploadStandard'){
      this.actionRequest=task;
      button.setAttribute('data-target','#uploadStandard');
    }
    if (mode==='prepPd'){
      this.actionRequest=task;
      button.setAttribute('data-target','#prepPd');
    }
    if (mode==='approveDraft'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approveDraft');
    }

    if (mode==='reject'){
      this.actionRequest=task;
      button.setAttribute('data-target','#reject');
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  onSaveStandard(): void {
    this.SpinnerService.show();
    //console.log(this.preparePreliminaryDraftFormGroup.value);
    this.stdComStandardService.prepareCompanyStandard(this.prepareStandardFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Preliminary Draft Preparation Process Started`);
          this.onClickSaveUPLOADS(response.body.savedRowID)
          this.prepareStandardFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Preliminary Draft Was Not Prepared`);
          console.log(error.message);
        }
    );
  }
  onClickSaveUPLOADS(comStdID: string) {
    if (this.uploadedFiles.length > 0) {
      const file = this.uploadedFiles;
      const formData = new FormData();
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }

      this.SpinnerService.show();
      this.stdComStandardService.uploadSDFileDetails(comStdID, formData).subscribe(
          (data: any) => {
            this.SpinnerService.hide();
            this.showToasterSuccess(data.httpStatus, `Company Standard Prepared`);
            this.uploadedFiles = null;
            console.log(data);
            swal.fire({
              title: 'Company Standard Prepared.',
              buttonsStyling: false,
              customClass: {
                confirmButton: 'btn btn-success ',
              },
              icon: 'success'
            });
            this.router.navigate(['/comStdJustification']);
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

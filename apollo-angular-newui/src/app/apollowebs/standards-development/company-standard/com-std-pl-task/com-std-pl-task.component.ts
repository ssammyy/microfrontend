import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Subject} from "rxjs";
import {
  ComHodTasks,
  ComStandardJC,
  ComStdAction,
  UserEntity,
  UsersEntity
} from "../../../../core/store/data/std/std.model";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import swal from "sweetalert2";
import {Store} from "@ngrx/store";
import {selectUserInfo} from "../../../../core/store";

declare const $: any;
@Component({
  selector: 'app-com-std-pl-task',
  templateUrl: './com-std-pl-task.component.html',
  styleUrls: ['./com-std-pl-task.component.css','../../../../../../node_modules/@ng-select/ng-select/themes/default.theme.css'],
  encapsulation: ViewEncapsulation.None
})
export class ComStdPlTaskComponent implements OnInit {
  user_id: number ;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  public users !: UserEntity[] ;
  selectedUser: number;
  tasks: ComHodTasks[] = [];
  public actionRequest: ComHodTasks | undefined;
  public preparePreliminaryDraftFormGroup!: FormGroup;
  public uploadedFiles: FileList;
  constructor(
      private store$: Store<any>,
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder,
      private router: Router,
  ) { }

  ngOnInit(): void {
    this.getPlTasks();
    this.getUserList();
    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      return this.user_id = u.id;
    });

    this.preparePreliminaryDraftFormGroup = this.formBuilder.group({
      title: ['', Validators.required],
      scope: ['', Validators.required],
      normativeReference: ['', Validators.required],
      symbolsAbbreviatedTerms: ['', Validators.required],
      clause: ['', Validators.required],
      special: ['', Validators.required],
      taskId: [],
      diJNumber: [],
      uploadedBy:[]

    });
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
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
    if (mode==='formJc'){
      this.actionRequest=task;
      button.setAttribute('data-target','#formJc');
    }
    if (mode==='prepPd'){
      this.actionRequest=task;
      button.setAttribute('data-target','#prepPd');
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  public getUserList(): void {
    this.SpinnerService.show();
    this.stdComStandardService.getUserList().subscribe(
        (response: UserEntity[]) => {
          this.SpinnerService.hide();
          this.users = response;
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  uploadPreliminaryDraft(): void {
    this.SpinnerService.show();
    //console.log(this.preparePreliminaryDraftFormGroup.value);
    this.stdComStandardService.prepareCompanyPreliminaryDraft(this.preparePreliminaryDraftFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Preliminary Draft Preparation Process Started`);
          this.onClickSaveUPLOADS(response.body.savedRowID)
          this.preparePreliminaryDraftFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Preliminary Draft Was Not Prepared`);
          console.log(error.message);
        }
    );
  }

  onClickSaveUPLOADS(comStdDraftID: string) {
    if (this.uploadedFiles.length > 0) {
      const file = this.uploadedFiles;
      const formData = new FormData();
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }
      this.SpinnerService.show();
      this.stdComStandardService.uploadPDFileDetails(comStdDraftID, formData).subscribe(
          (data: any) => {
            this.SpinnerService.hide();
            this.uploadedFiles = null;
            console.log(data);
            swal.fire({
              title: 'Company Draft Prepared.',
              buttonsStyling: false,
              customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
              },
              icon: 'success'
            });
            //this.router.navigate(['/nwaJustification']);
          },
      );
    }

  }


  //Assign Project Leader
  public onAssign(comStandardJC: ComStandardJC): void{
    this.SpinnerService.show();
    this.stdComStandardService.formJointCommittee(comStandardJC).subscribe(
        (response: ComStandardJC) => {
          this.SpinnerService.hide();
          console.log(response);
          this.getPlTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
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

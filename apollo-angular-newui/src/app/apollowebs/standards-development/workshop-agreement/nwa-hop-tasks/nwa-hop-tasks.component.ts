import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {StdNwaService} from "../../../../core/store/data/std/std-nwa.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HOPTasks, KnwSecTasks, NWAStandard} from "../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import swal from "sweetalert2";

declare const $: any;

@Component({
  selector: 'app-nwa-hop-tasks',
  templateUrl: './nwa-hop-tasks.component.html',
  styleUrls: ['./nwa-hop-tasks.component.css']
})
export class NwaHopTasksComponent implements OnInit ,OnDestroy {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: HOPTasks[] = [];
    public uploadedFiles:  FileList;
  standards: NWAStandard[] = [];
  public actionRequest: HOPTasks | undefined;
    public prepareWorkShopDraftFormGroup!: FormGroup;
    public prepareStandardFormGroup!: FormGroup;
  constructor(
      private formBuilder: FormBuilder,
      private stdNwaService: StdNwaService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getHOPTasks();
      this.prepareWorkShopDraftFormGroup = this.formBuilder.group({
          title: ['', Validators.required],
          scope: ['', Validators.required],
          normativeReference: ['', Validators.required],
          referenceMaterial: ['', Validators.required],
          clause: ['', Validators.required],
          special: ['', Validators.required],
          taskId: []

      });

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
    get formPrepareWD(): any {
        return this.prepareWorkShopDraftFormGroup.controls;
    }
    get formStandard(): any {
        return this.prepareStandardFormGroup.controls;
    }
    showToasterSuccess(title:string,message:string){
        this.notifyService.showSuccess(message, title)

    }
    showToasterError(title:string,message:string){
        this.notifyService.showError(message, title)

    }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  public getHOPTasks(): void{
    this.SpinnerService.show();
    this.stdNwaService.getHOPTasks().subscribe(
        (response: HOPTasks[])=> {
            this.tasks = response;
            this.dtTrigger.next();
            this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
            console.log(error.message);
        }
    );
  }

  public onOpenModal(task: HOPTasks,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='edit'){
      this.actionRequest=task;
      button.setAttribute('data-target','#editModal');
    }
    if (mode==='upload'){
      this.actionRequest=task;
      button.setAttribute('data-target','#uploadModal');
    }



    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
    onEdit(): void {
        this.SpinnerService.show();
        //console.log(this.prepareWorkShopDraftFormGroup.value);
        this.stdNwaService.editWorkshopDraft(this.prepareWorkShopDraftFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Uploading WorkShop Draft`);
                this.onClickSaveWD(response.body.savedRowID)
                this.prepareWorkShopDraftFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Workshop Draft Was Not uploaded`);
                console.log(error.message);
            }
        );
    }
    onClickSaveWD(nwaWDid: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }

            this.SpinnerService.show();
            this.stdNwaService.uploadWDFileDetails(nwaWDid, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.showToasterSuccess(data.httpStatus, `WorkShop Draft Uploaded`);
                    this.uploadedFiles = null;
                    console.log(data);
                    this.dtTrigger.next();
                    swal.fire({
                        title: 'WorkShop Draft Uploaded.',
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


  // public onEdit(nwaWorkShopDraft: NWAWorkShopDraft): void{
  //   this.SpinnerService.show();
  //   this.stdNwaService.editWorkshopDraft(nwaWorkShopDraft).subscribe(
  //       (response: NWAWorkShopDraft) => {
  //         this.SpinnerService.hide();
  //           this.showToasterSuccess('Success', `Uploading WorkShop Draft`);
  //           this.onClickSaveUPLOADS(response.body.savedRowID)
  //         console.log(response);
  //         this.getHOPTasks();
  //       },
  //       (error: HttpErrorResponse) => {
  //         this.SpinnerService.hide();
  //         alert(error.message);
  //       }
  //   );
  // }
  // public onNwaUpload(nWAStandard: NWAStandard): void{
  //   this.SpinnerService.show();
  //   this.stdNwaService.uploadNwaStandard(nWAStandard).subscribe(
  //       (response: NWAStandard) => {
  //         this.SpinnerService.hide();
  //         console.log(response);
  //         this.getHOPTasks();
  //       },
  //       (error: HttpErrorResponse) => {
  //         this.SpinnerService.hide();
  //         alert(error.message);
  //       }
  //   );
  // }

  // Upload Standard
    onNwaUpload(): void {
        this.SpinnerService.show();
        //console.log(this.prepareStandardFormGroup.value);
        this.stdNwaService.uploadNwaStandard(this.prepareStandardFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Uploading Standard`);
                this.onClickSaveSTD(response.body.savedRowID)
                this.prepareStandardFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Standard Was Not uploaded`);
                console.log(error.message);
            }
        );
    }

    onClickSaveSTD(nwaSTDid: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }

            this.SpinnerService.show();
            this.stdNwaService.uploadSTDFileDetails(nwaSTDid, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.showToasterSuccess(data.httpStatus, `Standard Uploaded`);
                    this.uploadedFiles = null;
                    console.log(data);
                    this.dtTrigger.next();
                    swal.fire({
                        title: 'Standard Uploaded.',
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

import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {StdNwaService} from "../../../../core/store/data/std/std-nwa.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HOPTasks, NWAStandard} from "../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import swal from "sweetalert2";

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
          title: [],
          scope: [],
          normativeReference: [],
          referenceMaterial: [],
          clause: [],
          special: [],
          taskId: []

      });

      this.prepareStandardFormGroup = this.formBuilder.group({
          title: [],
          scope: [],
          normativeReference: [],
          referenceMaterial: [],
          clause: [],
          special: [],
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
          this.SpinnerService.hide();
          this.tasks = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
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
}

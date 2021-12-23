import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {ISHopTASKS} from "../../../../core/store/data/std/std.model";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import swal from "sweetalert2";

@Component({
  selector: 'app-int-std-upload-standard',
  templateUrl: './int-std-upload-standard.component.html',
  styleUrls: ['./int-std-upload-standard.component.css']
})
export class IntStdUploadStandardComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ISHopTASKS[] = [];
  blob: Blob;
  public uploadedFiles:  FileList;
  public actionRequest: ISHopTASKS | undefined;
  public prepareStandardFormGroup!: FormGroup;
  constructor(
      private formBuilder: FormBuilder,
      private stdIntStandardService : StdIntStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getHOPTasks();
    this.prepareStandardFormGroup = this.formBuilder.group({
      title: ['', Validators.required],
      scope: ['', Validators.required],
      normativeReference: ['', Validators.required],
      referenceMaterial: ['', Validators.required],
      clause: ['', Validators.required],
      special: ['', Validators.required],
      symbolsAbbreviatedTerms: ['', Validators.required],
      taskId: []

    });
  }
  get formPrepareWD(): any {
    return this.prepareStandardFormGroup.controls;
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
  public getHOPTasks(): void{
    this.SpinnerService.show();
    this.stdIntStandardService.getHOPTasks().subscribe(
        (response: ISHopTASKS[])=> {
          this.tasks = response;
          this.SpinnerService.hide();
          this.dtTrigger.next();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.stdIntStandardService.viewJustificationPDF(pdfId).subscribe(
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
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error opening document`);
          alert(error.message);
        }
    );
  }
  public onOpenModal(task: ISHopTASKS,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='upload'){
      this.actionRequest=task;
      button.setAttribute('data-target','#uploadModal');
    }
    if (mode==='reject'){
      this.actionRequest=task;
      button.setAttribute('data-target','#rejectModal');
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  // Upload Standard
  onUpload(): void {
    this.SpinnerService.show();
    //console.log(this.prepareStandardFormGroup.value);
    this.stdIntStandardService.uploadISStandard(this.prepareStandardFormGroup.value).subscribe(
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
  onClickSaveSTD(isStandardID: string) {
    if (this.uploadedFiles.length > 0) {
      const file = this.uploadedFiles;
      const formData = new FormData();
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }

      this.SpinnerService.show();
      this.stdIntStandardService.uploadSDFile(isStandardID, formData).subscribe(
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

}

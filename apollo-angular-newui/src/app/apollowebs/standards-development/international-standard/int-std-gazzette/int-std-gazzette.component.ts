import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {ISHopTASKS, ISHosSicTASKS} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {HttpErrorResponse} from "@angular/common/http";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import swal from "sweetalert2";

@Component({
  selector: 'app-int-std-gazzette',
  templateUrl: './int-std-gazzette.component.html',
  styleUrls: ['./int-std-gazzette.component.css']
})
export class IntStdGazzetteComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ISHosSicTASKS[] = [];
  blob: Blob;
  public uploadedFiles:  FileList;
  public actionRequest: ISHosSicTASKS | undefined;
  public prepareGazetteNotice!: FormGroup;
  constructor(
      private formBuilder: FormBuilder,
      private stdIntStandardService : StdIntStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getHoSiCTasks();
    this.prepareGazetteNotice = this.formBuilder.group({
      description: ['', Validators.required],
      taskId: [],
      iSNumber: []

    });
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
  get formPrepareGN(): any {
    return this.prepareGazetteNotice.controls;
  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  public getHoSiCTasks(): void{
    this.SpinnerService.show();
    this.stdIntStandardService.getHoSiCTasks().subscribe(
        (response: ISHosSicTASKS[])=> {
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
    this.stdIntStandardService.viewStandardPDF(pdfId).subscribe(
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
    viewPdDfFile(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.stdIntStandardService.viewStandardDPDF(pdfId).subscribe(
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
  public onOpenModal(task: ISHosSicTASKS,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='upload'){
      this.actionRequest=task;
      button.setAttribute('data-target','#uploadModal');
    }
    if (mode==='update'){
      this.actionRequest=task;
      button.setAttribute('data-target','#updateModal');
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  // Upload Standard
  onUpload(): void {
    this.SpinnerService.show();
    //console.log(this.prepareStandardFormGroup.value);
    this.stdIntStandardService.uploadGazetteNotice(this.prepareGazetteNotice.value).subscribe(
        (response ) => {
          console.log(response);
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Gazette Notice Uploaded`);
            this.onClickSaveSTD(response.body.savedRowID)
          this.prepareGazetteNotice.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Gazette Notice Was Not uploaded`);
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
            this.stdIntStandardService.uploadSDGZFile(isStandardID, formData).subscribe(
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
  onUpdate(): void {
    this.SpinnerService.show();
    //console.log(this.prepareStandardFormGroup.value);
    this.stdIntStandardService.updateGazetteDate(this.prepareGazetteNotice.value).subscribe(
        (response ) => {
          console.log(response);
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Gazettement Date Updated`);
          this.prepareGazetteNotice.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Gazettement Date Was Not Updated`);
          console.log(error.message);
        }
    );
  }
}

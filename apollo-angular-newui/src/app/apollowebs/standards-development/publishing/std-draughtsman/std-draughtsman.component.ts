import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {DraughtsmanTask, StandardDraft, StdTCDecision} from "../../../../core/store/data/std/request_std.model";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {PublishingService} from "../../../../core/store/data/std/publishing.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";

@Component({
  selector: 'app-std-draughtsman',
  templateUrl: './std-draughtsman.component.html',
  styleUrls: ['./std-draughtsman.component.css']
})
export class StdDraughtsmanComponent implements OnInit {
  p = 1;
  p2 = 1;
  tasks: DraughtsmanTask[] = [];
  public actionRequest: DraughtsmanTask | undefined;
  public itemId: string = "1";
  public editorDocuments: string = "EditorDocuments";

  public formActionRequest: StdTCDecision | undefined;
  blob: Blob;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false
  loadingText: string;
  public uploadedFiles: FileList;

  constructor(private publishingService: PublishingService, private notifyService: NotificationService,
              private SpinnerService: NgxSpinnerService, private router: Router,
  ) {
  }

  ngOnInit(): void {
    this.getDraughtsmanTasks();

  }

  public getDraughtsmanTasks(): void {
    this.loadingText = "Retrieving Draft Standards Please Wait ...."
    this.SpinnerService.show();
    this.publishingService.getDraughtsmanTasks().subscribe(
        (response: DraughtsmanTask[]) => {
          this.tasks = response;
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
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  public onOpenModal(task: DraughtsmanTask, mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    console.log(task.taskId);
    if (mode === 'edit') {
      this.actionRequest = task;
      button.setAttribute('data-target', '#justificationDecisionModal');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  showToasterSuccess(title: string, message: string) {
    this.notifyService.showSuccess(message, title)

  }

  showToasterError(title: string, message: string) {
    this.notifyService.showError(message, title)

  }


  public uploadStdDraught(standardDraft: StandardDraft,draftId: number): void {
    if (this.uploadedFiles != null) {
      if (this.uploadedFiles.length > 0) {
        this.loadingText = "Submitting  Draft Standard To Head Of Publishing...."
        this.SpinnerService.show();

        this.publishingService.uploadStdDraught(standardDraft,draftId).subscribe(
            (response) => {
              console.log(response);
              this.showToasterSuccess(response.httpStatus, `Draft Standard Submitted To Head Of Publishing.`);
              this.onClickSaveUploads(String(draftId))
              this.getDraughtsmanTasks();
            },
            (error: HttpErrorResponse) => {
              alert(error.message);
            }
        )
      } else {
        this.showToasterError("Error", `Please Upload a Draughted Draft Standard.`);

      }
    } else {
      this.showToasterError("Error", `Please Upload a Draughted Draft Standard.`);

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
      this.publishingService.uploadFileDetails(draftStandardID, formData, "DraughtingDraftStandard").subscribe(
          (data: any) => {
            this.SpinnerService.hide();
            this.uploadedFiles = null;
            console.log(data);
            swal.fire({
              title: 'Draft Standard Submitted To Head Of Publishing.',
              buttonsStyling: false,
              customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
              },
              icon: 'success'
            });
            this.router.navigate(['/draughtsmanTasks']);
          },
      );
    }

  }
  viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.publishingService.viewDEditedApplicationPDF(pdfId,"ProofReadDraftStandard").subscribe(
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

  @ViewChild('closeModal') private closeModal: ElementRef | undefined;

  public hideModel() {
    this.closeModal?.nativeElement.click();
  }

}

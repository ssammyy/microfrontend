import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ReviewApplicationTask} from "../../../../core/store/data/std/request_std.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {MembershipToTcService} from "../../../../core/store/data/std/membership-to-tc.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-members-created-credentials',
  templateUrl: './members-created-credentials.component.html',
  styleUrls: ['./members-created-credentials.component.css']
})
export class MembersCreatedCredentialsComponent implements OnInit {

  p = 1;
  p2 = 1;
  public tcTasks: ReviewApplicationTask[] = [];
  public actionRequest: ReviewApplicationTask | undefined;
  loadingText: string;
  public uploadedFiles: FileList;

  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;

  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  isDtInitialized: boolean = false
  blob: Blob;
  display = 'none'; //default Variable


  constructor(private membershipToTcService: MembershipToTcService, private notifyService: NotificationService,
              private SpinnerService: NgxSpinnerService) {
  }

  ngOnInit(): void {
    this.getApplicationsForReview();

  }


  public getApplicationsForReview(): void {
    this.loadingText = "Retrieving  Applicants Please Wait ...."
    this.SpinnerService.show();
    this.membershipToTcService.getMembersCreatedCredentials().subscribe(
        (response: ReviewApplicationTask[]) => {
          console.log(response);
          this.tcTasks = response;
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
    )
  }

  public onOpenModal(task: ReviewApplicationTask): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    console.log(task.taskId);
    this.actionRequest = task;
    button.setAttribute('data-target', '#decisionModal');

    // @ts-ignore
    container.appendChild(button);
    button.click();
  }

  @ViewChild('closeViewModal') private closeModal: ElementRef;

  public hideModel() {
    this.closeModal?.nativeElement.click();
  }

  @ViewChild('editForm') private mytemplateForm: NgForm;

  public clearForm() {
    this.mytemplateForm?.resetForm();
  }

  showToasterError(title: string, message: string) {
    this.notifyService.showError(message, title)

  }

  showToasterSuccess(title: string, message: string) {
    this.notifyService.showSuccess(message, title)

  }

  public decisionOnApplications(reviewApplicationTask: ReviewApplicationTask, tCApplicationId: number, decision: string): void {


    this.SpinnerService.show();
    this.loadingText = "Sending Induction Email";
    this.membershipToTcService.sendInductionEmail(reviewApplicationTask, tCApplicationId).subscribe(
        (response) => {
          console.log(response);
          this.getApplicationsForReview();
          this.SpinnerService.hide();
          this.notifyService.showSuccess("Success", 'Induction Email Sent')

        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    )

    this.hideModel();
    this.clearForm();


  }


  viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.membershipToTcService.viewDEditedApplicationPDF(pdfId, "ApplicantCV").subscribe(
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

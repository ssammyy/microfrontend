import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {ReviewApplicationTask} from "../../../../../core/store/data/std/request_std.model";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {MembershipToTcService} from "../../../../../core/store/data/std/membership-to-tc.service";
import {NotificationService} from "../../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-all-applications',
  templateUrl: './all-applications.component.html',
  styleUrls: ['./all-applications.component.css']
})
export class AllApplicationsComponent implements OnInit {
  p = 1;
  p2 = 1;
  public tcTasks: ReviewApplicationTask[] = [];
  public actionRequest: ReviewApplicationTask | undefined;
  loadingText: string;
  public uploadedFiles: FileList;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  isDtInitialized: boolean = false
  blob: Blob;
  display = 'none'; //default Variable
  loading = false;
  constructor(private membershipToTcService: MembershipToTcService, private notifyService: NotificationService,
              private SpinnerService: NgxSpinnerService) {
  }
  ngOnInit(): void {
    this.getApplicationsForReview();

  }
  public getApplicationsForReview(): void {
    this.loading = true
    this.loadingText = "Retrieving Applications Please Wait ...."
    this.SpinnerService.show();
    this.membershipToTcService.getApplicationsForReview().subscribe(
        (response: ReviewApplicationTask[]) => {
          console.log(response);
          this.tcTasks = response;
          this.rerender()

          this.SpinnerService.hide();
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
  pdfSrc: any;

  public clearForm() {
    this.mytemplateForm?.resetForm();
  }

  showToasterError(title: string, message: string) {
    this.notifyService.showError(message, title)

  }

  public decisionOnApplications(reviewApplicationTask: ReviewApplicationTask, tCApplicationId: number): void {

    if (reviewApplicationTask.comments_by_hof === "") {
      this.showToasterError("Error", `Please Enter A Comment.`);

    } else {
      this.SpinnerService.show();
      this.loadingText = "Approving Applicant";
      this.membershipToTcService.decisionOnApplications(reviewApplicationTask, tCApplicationId).subscribe(
          (response) => {
            console.log(response);
            this.getApplicationsForReview();
            this.SpinnerService.hide();

          },
          (error: HttpErrorResponse) => {
            alert(error.message);
          }
      )

      this.hideModel();
      this.clearForm();
    }

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
          this.pdfSrc=downloadURL
          console.log(downloadURL)
          link.download = fileName;
          link.click();
          // this.pdfUploadsView = dataPdf;
        },
    );
  }


  rerender(): void {
    this.dtElements.forEach((dtElement: DataTableDirective) => {
      if (dtElement.dtInstance)
        dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
          dtInstance.destroy();
        });
    });
    setTimeout(() => {
      this.dtTrigger.next();


    });

  }

  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event
    this.dtTrigger.unsubscribe();


  }




}

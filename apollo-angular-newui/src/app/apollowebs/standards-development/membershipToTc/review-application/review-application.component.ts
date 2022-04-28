import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ReviewApplicationTask} from "../../../../core/store/data/std/request_std.model";
import {MembershipToTcService} from "../../../../core/store/data/std/membership-to-tc.service";
import {HttpErrorResponse} from "@angular/common/http";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NgForm} from '@angular/forms';

@Component({
    selector: 'app-review-application',
    templateUrl: './review-application.component.html',
    styleUrls: ['./review-application.component.css']
})
export class ReviewApplicationComponent implements OnInit {
    p = 1;
    p2 = 1;
    public tcTasks: ReviewApplicationTask[] = [];
    public actionRequest: ReviewApplicationTask | undefined;
    loadingText: string;
    public uploadedFiles: FileList;
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;
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
        this.loadingText = "Retrieving Applications Please Wait ...."
        this.SpinnerService.show();
        this.membershipToTcService.getApplicationsForReview().subscribe(
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

}

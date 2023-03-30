import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Document, ReviewApplicationTask} from "../../../../core/store/data/std/request_std.model";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {MembershipToTcService} from "../../../../core/store/data/std/membership-to-tc.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {NgForm} from "@angular/forms";

@Component({
    selector: 'app-review-recommendation-of-spc-component',
    templateUrl: './review-recommendation-of-spc-component.component.html',
    styleUrls: ['./review-recommendation-of-spc-component.component.css']
})
export class ReviewRecommendationOfSpcComponentComponent implements OnInit {

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

    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;

    dtOptionsB: DataTables.Settings = {};
    dtTrigger2: Subject<any> = new Subject<any>();

    docs !: Document[];


    constructor(private membershipToTcService: MembershipToTcService, private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService) {
    }

    ngOnInit(): void {
        this.getApplicationsForReview();

    }


    public getApplicationsForReview(): void {
        this.loadingText = "Retrieving Applications Please Wait ...."
        this.SpinnerService.show();
        this.membershipToTcService.getRecommendationsFromSPC().subscribe(
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
        this.getAllDocs(String(this.actionRequest.id))

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

        if (reviewApplicationTask.commentsBySac === "") {
            this.showToasterError("Error", `Please Enter Your Recommendation.`);

        } else {
            this.SpinnerService.show();
            this.loadingText = "Sending Recommendation";
            this.membershipToTcService.decisionOnSPCRecommendation(reviewApplicationTask, tCApplicationId, decision).subscribe(
                (response) => {
                    console.log(response);
                    this.getApplicationsForReview();
                    this.SpinnerService.hide();
                    this.notifyService.showSuccess("Success", 'Your Recommendation Has Been Sent')

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

                let downloadURL = window.URL.createObjectURL(this.blob);
                window.open(downloadURL, fileName);
            },
        );
    }

    public getAllDocs(applicationId: string): void {
        this.membershipToTcService.getUserCV(applicationId, "UPLOADS", "ApplicantCV").subscribe(
            (response: Document[]) => {
                this.docs = response;
                this.rerender()


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
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
            this.dtTrigger2.next();


        });

    }

}

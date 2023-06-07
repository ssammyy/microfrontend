import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {DecisionFeedback, SACSummary, StdTCDecision} from "../../../../core/store/data/std/request_std.model";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {PublishingService} from "../../../../core/store/data/std/publishing.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {AdoptionOfEaStdsService} from "../../../../core/store/data/std/adoption-of-ea-stds.service";

@Component({
    selector: 'app-view-sac-summary',
    templateUrl: './view-sac-summary.component.html',
    styleUrls: ['./view-sac-summary.component.css']
})
export class ViewSacSummaryComponent implements OnInit {

    p = 1;
    p2 = 1;
    tasks: SACSummary[] = [];
    public actionRequest: SACSummary | undefined;
    public formActionRequest: StdTCDecision | undefined;
    fullname = '';
    title = 'toaster-not';
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;
    isDtInitialized: boolean = false
    public itemId: number;
    loadingText: string;
    blob: Blob;
    public uploadedFiles: FileList;


    constructor(private publishingService: PublishingService, private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService, private  adoptionOfEaStdsService: AdoptionOfEaStdsService,) {
    }

    ngOnInit(): void {
        this.getSPCTasks();
    }

    public getSPCTasks(): void {
        this.loadingText = "Retrieving Data Please Wait ...."
        this.SpinnerService.show();
        this.adoptionOfEaStdsService.getSACSECTask().subscribe(
            (response: SACSummary[]) => {
                this.tasks = response;
                console.log(response)
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
                alert(error.message);
                this.SpinnerService.hide();
            }
        );
    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)

    }

    public onOpenModal(task: SACSummary, mode: string): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        console.log(task.id);
        if (mode === 'edit') {
            this.actionRequest = task;
            button.setAttribute('data-target', '#decisionModal');
        }

        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    public decisionOnSACSummary(decisionFeedback: DecisionFeedback, decision: boolean, sacSummaryId: number): void {
        if (this.uploadedFiles != null) {
            if (this.uploadedFiles.length > 0 || this.uploadedFiles.length != 1) {

                this.loadingText = "Submitting Decision On SAC Summary ...."

                this.SpinnerService.show();
                decisionFeedback.status = decision;
                this.adoptionOfEaStdsService.decisionOnSACSummary(decisionFeedback, sacSummaryId).subscribe(
                    (response) => {
                        console.log(response);
                        this.SpinnerService.hide()
                        this.showToasterSuccess(response.httpStatus, `Your Decision Has Been Submitted.`);
                        this.onClickSaveUploads(String(sacSummaryId))
                    },
                    (error: HttpErrorResponse) => {
                        alert(error.message);
                        this.SpinnerService.hide()

                    }
                )
            } else {
                this.showToasterError("Error", `Please Upload the minutes.`);
            }
        } else {
            this.showToasterError("Error", `Please Upload the minutes.`);
        }

    }

    onClickSaveUploads(sacSummaryID: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.adoptionOfEaStdsService.uploadFileDetails(sacSummaryID, formData, "Minutes", "Minutes").subscribe(
                (data: any) => {
                    this.getSPCTasks();
                    this.hideModel();
                },
            );
        } else {
            this.showToasterError("Error", `Please Upload the minutes.`);
        }

    }

    @ViewChild('closeModal') private closeModal: ElementRef | undefined;
    display = 'none'; //default Variable

    public hideModel() {
        this.closeModal?.nativeElement.click();
    }

    viewPdfFile(pdfId: number, fileName: string, applicationType: string, doctype: string): void {
        this.SpinnerService.show();
        this.adoptionOfEaStdsService.viewDocs(pdfId, doctype).subscribe(
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


    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

    }


}

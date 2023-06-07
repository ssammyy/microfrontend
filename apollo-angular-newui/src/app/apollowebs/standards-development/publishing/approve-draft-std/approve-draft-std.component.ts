import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {PublishingService} from "../../../../core/store/data/std/publishing.service";
import {DraftPublishing, StandardDraft, StdTCDecision} from "../../../../core/store/data/std/request_std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {Router} from "@angular/router";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";

@Component({
    selector: 'app-approve-draft-std',
    templateUrl: './approve-draft-std.component.html',
    styleUrls: ['./approve-draft-std.component.css']
})
export class ApproveDraftStdComponent implements OnInit {

    p = 1;
    p2 = 1;
    blob: Blob;

    tasks: DraftPublishing[] = [];
    public actionRequest: DraftPublishing | undefined;
    public formActionRequest: StdTCDecision | undefined;
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;
    isDtInitialized: boolean = false
    public itemId: number;
    loadingText: string;
    public uploadedFiles: FileList;

    constructor(private publishingService: PublishingService, private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService, private router: Router) {
    }

    ngOnInit(): void {
        this.getHOPTasks();
    }

    public getHOPTasks(): void {
        this.loadingText = "Retrieving Draft Standards Please Wait ...."
        this.SpinnerService.show();
        this.publishingService.getHOPTasks().subscribe(
            (response: DraftPublishing[]) => {
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
                this.SpinnerService.hide();

                alert(error.message);
            }
        );
    }

    public onOpenModal(task: DraftPublishing, mode: string): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        console.log(task.id);
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

    public uploadFinishedStd(standardDraft: StandardDraft): void {
        console.log(standardDraft);
        this.loadingText = "Approving Draft Standard ...."
        this.SpinnerService.show();
        this.publishingService.uploadFinishedStd(standardDraft).subscribe(
            (response) => {
                console.log(response);
                this.showToasterSuccess(response.httpStatus, `Draft Standard Approved.`);

                this.getHOPTasks();
                this.SpinnerService.hide();

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();

                alert(error.message);
            }
        )
    }

    viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.publishingService.viewDEditedApplicationPDF(pdfId, "DraftStandard").subscribe(
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

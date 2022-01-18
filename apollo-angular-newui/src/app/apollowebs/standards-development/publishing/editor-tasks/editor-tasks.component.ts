import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {EditorTask, StdTCDecision} from "../../../../core/store/data/std/request_std.model";
import {PublishingService} from "../../../../core/store/data/std/publishing.service";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import swal from "sweetalert2";
import {Router} from "@angular/router";

@Component({
    selector: 'app-editor-tasks',
    templateUrl: './editor-tasks.component.html',
    styleUrls: ['./editor-tasks.component.css']
})
export class EditorTasksComponent implements OnInit {
    p = 1;
    p2 = 1;
    tasks: EditorTask[] = [];
    public actionRequest: EditorTask | undefined;
    blob: Blob;
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
                private SpinnerService: NgxSpinnerService, private router: Router,
    ) {
    }

    ngOnInit(): void {
        this.getEditorTasks();
    }

    public getEditorTasks(): void {
        this.loadingText = "Retrieving Draft Standards Please Wait ...."
        this.SpinnerService.show();
        this.publishingService.getEditorTasks().subscribe(
            (response: EditorTask[]) => {
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

    public onOpenModal(task: EditorTask, mode: string): void {
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

    public decisionOnDraft(stdTCDecision: StdTCDecision, decision: string): void {
        this.loadingText = "Submitting Edited Draft Standard For Proofreading...."

        this.SpinnerService.show();
        stdTCDecision.decision = decision;
        console.log(stdTCDecision);
        this.publishingService.decisionOnKSDraft(stdTCDecision).subscribe(
            (response) => {
                console.log(response);
                this.showToasterSuccess(response.httpStatus, `Draft Standard Submitted For Proofreading.`);
                this.getEditorTasks();
                this.SpinnerService.hide();

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();

                alert(error.message);
            }
        )

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
            this.publishingService.uploadFileDetails(draftStandardID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = null;
                    console.log(data);
                    swal.fire({
                        title: 'Draft Standard Submitted To Proofreader.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    this.router.navigate(['/editorTasks']);
                },
            );
        }

    }

    viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.publishingService.viewDEditedApplicationPDF(pdfId).subscribe(
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

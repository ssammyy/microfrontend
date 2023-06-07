import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {DraftEditing, EditorTask, StdTCDecision} from "../../../../core/store/data/std/request_std.model";
import {PublishingService} from "../../../../core/store/data/std/publishing.service";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import swal from "sweetalert2";
import Swal from "sweetalert2";
import {Router} from "@angular/router";

@Component({
    selector: 'app-editor-tasks',
    templateUrl: './editor-tasks.component.html',
    styleUrls: ['./editor-tasks.component.css']
})
export class EditorTasksComponent implements OnInit {
    p = 1;
    p2 = 1;
    tasks: DraftEditing[] = [];
    public actionRequest: DraftEditing | undefined;
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
            (response: DraftEditing[]) => {
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

    public onOpenModal(task: DraftEditing, mode: string): void {
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

    public decisionOnDraft(uploadStdDraft: EditorTask, decision: string, draftId: number): void {

        if (this.uploadedFiles != null) {
            if (this.uploadedFiles.length > 0) {
                this.loadingText = "Submitting Edited Draft...."
                this.SpinnerService.show();
                //stdTCDecision.decision = decision;
                // console.log(stdTCDecision);
                this.publishingService.completeEditing(uploadStdDraft, draftId).subscribe(
                    (response) => {
                        console.log(response);
                        this.showToasterSuccess(response.httpStatus, `Draft Standard Edited....`);
                        this.onClickSaveUploads(String(draftId))

                        this.getEditorTasks();
                        this.SpinnerService.hide();

                    },
                    (error: HttpErrorResponse) => {
                        this.SpinnerService.hide();

                        alert(error.message);
                    }
                )
            } else {
                this.showToasterError("Error", `Please Upload an Edited Draft Standard.`);

            }
        } else {
            this.showToasterError("Error", `Please Upload an Edited Draft Standard.`);

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
            this.publishingService.uploadFileDetails(draftStandardID, formData, "EditedDraftStandard").subscribe(
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
        this.publishingService.viewDEditedApplicationPDF(pdfId, "DraftStandard").subscribe(
            (dataPdf: any) => {
                this.SpinnerService.hide();
                this.blob = new Blob([dataPdf], {type: applicationType});

                // tslint:disable-next-line:prefer-const
                // let downloadURL = window.URL.createObjectURL(this.blob);
                // const link = document.createElement('a');
                // link.href = downloadURL;
                // link.download = fileName;
                // link.click();
                // this.pdfUploadsView = dataPdf;
                // tslint:disable-next-line:prefer-const
                let downloadURL = window.URL.createObjectURL(this.blob);
                window.open(downloadURL, '_blank');
            },
        );
    }

    @ViewChild('closeModal') private closeModal: ElementRef | undefined;

    public hideModel() {
        this.closeModal?.nativeElement.click();
    }

    approveForDraughting(uploadStdDraft: DraftEditing): void {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-danger'
            },
            buttonsStyling: false
        });

        swalWithBootstrapButtons.fire({
            title: 'Are you sure your want to approve for draughting?',
            text: 'You won\'t be able to reverse this!',
            icon: 'success',
            showCancelButton: true,
            confirmButtonText: 'Yes!',
            cancelButtonText: 'No Need For Draughting!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.SpinnerService.show();
                this.publishingService.sendForDraughting(uploadStdDraft, "Approved").subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Approved!',
                            'Approved For Draughting!',
                            'success'
                        );
                        this.SpinnerService.hide();
                        this.showToasterSuccess(response.httpStatus, 'Approved For Draughting');
                        this.getEditorTasks();
                    },
                );
            } else if (
                /* Read more about handling dismissals below */
                result.dismiss === swal.DismissReason.cancel
            ) {
                this.SpinnerService.show();
                this.publishingService.sendForDraughting(uploadStdDraft, "No Need For Draughting").subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'No Need For Draughting!',
                            'Ballot Draft Successfully Rejected!',
                            'success'
                        );
                        this.SpinnerService.hide();
                        this.showToasterSuccess(response.httpStatus, "Draft Doesn't need to be draughted");
                        this.getEditorTasks();
                    },
                );
            }
        });
    }

    approveForProofReading(uploadStdDraft: DraftEditing): void {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-success'
            },
            buttonsStyling: false
        });

        swalWithBootstrapButtons.fire({
            title: 'Send for proofreading?',
            text: 'Are you sure your want to approve for proofreading?',
            icon: 'success',
            showCancelButton: true,
            confirmButtonText: 'Yes!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.SpinnerService.show();
                this.publishingService.sendForProofreading(uploadStdDraft, "Approved").subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Approved!',
                            'Approved For Proofreading!',
                            'success'
                        );
                        this.SpinnerService.hide();
                        this.showToasterSuccess(response.httpStatus, 'Approved For Proofreading');
                        this.getEditorTasks();
                    },
                );
            }
        });
    }

    sendToOfficer(uploadStdDraft: DraftEditing): void {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-success'
            },
            buttonsStyling: false
        });

        swalWithBootstrapButtons.fire({
            title: 'Send to Officer?',
            text: 'Are you sure your want send to Standard Officer?',
            icon: 'success',
            showCancelButton: true,
            confirmButtonText: 'Yes!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.SpinnerService.show();
                this.publishingService.sendToOfficer(uploadStdDraft, "Approved").subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Sent!',
                            'Sent To Officer!',
                            'success'
                        );
                        this.SpinnerService.hide();
                        this.showToasterSuccess(response.httpStatus, 'Sent To Officer');
                        this.getEditorTasks();
                    },
                );
            }
        });
    }


}

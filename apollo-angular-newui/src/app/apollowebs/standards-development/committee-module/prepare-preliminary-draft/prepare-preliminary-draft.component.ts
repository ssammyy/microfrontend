import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ApprovedNwiS, Preliminary_Draft} from "../../../../core/store/data/std/commitee-model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";

declare const $: any;

@Component({
    selector: 'app-prepare-preliminary-draft',
    templateUrl: './prepare-preliminary-draft.component.html',
    styleUrls: ['./prepare-preliminary-draft.component.css']
})
export class PreparePreliminaryDraftComponent implements OnInit {
    fullname = '';
    title = 'toaster-not';
    preliminary_draft: Preliminary_Draft | undefined;
    submitted = false;
    public preliminary_draftFormGroup!: FormGroup;
    public approvedNwiS !: ApprovedNwiS[];
    public approvedNwiSB !: ApprovedNwiS | undefined;
    public uploadedFiles: FileList;
    public uploadedFilesB: FileList;
    public uploadedFilesC: FileList;

    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;
    isDtInitialized: boolean = false

    constructor(private formBuilder: FormBuilder,
                private committeeService: CommitteeService,
                private store$: Store<any>,
                private router: Router,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService) {
    }

    ngOnInit(): void {
        this.getNWIS();

        this.preliminary_draftFormGroup = this.formBuilder.group({
            pdName: ['', Validators.required]
        });

    }

    public getNWIS(): void {
        this.committeeService.getAllNWIS().subscribe(
            (response: ApprovedNwiS[]) => {
                console.log(response);

                this.approvedNwiS = response;
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
            }
        );

    }

    public onOpenModal(approvedNwiS: ApprovedNwiS, mode: string): void {

        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'edit') {
            this.approvedNwiSB = approvedNwiS;
            button.setAttribute('data-target', '#updateNWIModal');
        }
        if (mode === 'preparePd') {
            this.approvedNwiSB = approvedNwiS;
            button.setAttribute('data-target', '#preparePd');
        }
        if (mode === 'uploadMinutes') {
            this.approvedNwiSB = approvedNwiS;
            button.setAttribute('data-target', '#uploadMinutes');
        }
        if (mode === 'workPlan') {
            this.approvedNwiSB = approvedNwiS;
            button.setAttribute('data-target', '#workPlan');
        }

        if (mode === 'uploadDraftsAndOtherRelevantDocuments') {
            this.approvedNwiSB = approvedNwiS;
            button.setAttribute('data-target', '#uploadDraftsAndOtherRelevantDocuments');
        }

        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    public uploadMinutes(nwiID: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.committeeService.uploadMinutesForPd(nwiID, formData, "Minutes PD", "Minutes PD").subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = null;
                    console.log(data);
                    swal.fire({
                        title: 'Minutes Uploaded Successfully.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    this.hideModel()
                    this.getNWIS();

                    // this.router.navigate(['/draftStandard']);
                    // this.getSACTasks();

                },
            );
        }
    }

    public uploadDraftsAndOtherDocs(nwiID: string) {
        if (this.uploadedFilesB.length > 0) {
            const file = this.uploadedFilesB;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.committeeService.uploadDraftDocumentsForPd(nwiID, formData, "Drafts And Other Relevant Documents PD", "Drafts And Other Relevant Documents PD").subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFilesB = null;
                    console.log(data);
                    this.hideModel()
                    swal.fire({
                        title: 'Draft Documents Uploaded Successfully.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    this.getNWIS();

                    // this.router.navigate(['/draftStandard']);
                    // this.getSACTasks();

                },
            );
        }
    }



    uploadPreliminaryDraft() : void {
        if (this.uploadedFilesC != null) {
                this.SpinnerService.show();
                this.committeeService.preparePreliminaryDraft(this.preliminary_draftFormGroup.value,this.approvedNwiSB.id).subscribe(
                    (response) => {
                        console.log(response)
                        this.SpinnerService.hide();
                        this.showToasterSuccess(response.httpStatus, `Successfully submitted Preliminary Draft`);
                        this.uploadPDDoc(response.body.savedRowID)
                        this.preliminary_draftFormGroup.reset();
                    },
                    (error: HttpErrorResponse) => {
                        this.SpinnerService.hide();
                        console.log(error.message);
                    }
                );
            } else {
                this.showToasterError("Error", `Please Upload all the documents.`);
            }
    }

    public uploadPDDoc(pdID: string) {
        if (this.uploadedFilesC.length > 0) {
            const file = this.uploadedFilesC;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.committeeService.uploadPDDocument(pdID, formData, "PD Document", "PD Document").subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFilesC = null;
                    console.log(data);
                    this.hideModel()
                    swal.fire({
                        title: 'Preliminary Draft Document Uploaded Successfully.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    this.getNWIS();

                    // this.router.navigate(['/draftStandard']);
                    // this.getSACTasks();

                },
            );
        }
    }



    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)
    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

    }
    showNotification(from: any, align: any) {
        const type = ['', 'info', 'success', 'warning', 'danger', 'rose', 'primary'];

        const color = Math.floor((Math.random() * 6) + 1);

        $.notify({
            icon: 'notifications',
            message: 'KEBS QAIMSS'
        }, {
            type: type[color],
            timer: 3000,
            placement: {
                from: from,
                align: align
            },
            template: '<div data-notify="container" class="col-xs-11 col-sm-3 alert alert-{0} alert-with-icon" role="alert">' +
                '<button mat-raised-button type="button" aria-hidden="true" class="close" data-notify="dismiss">  <i class="material-icons">close</i></button>' +
                '<i class="material-icons" data-notify="icon">notifications</i> ' +
                '<span data-notify="title"></span> ' +
                '<span data-notify="message">Ensure all required fields and items have been filled</span>' +
                '<div class="progress" data-notify="progressbar">' +
                '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>' +
                '</div>' +
                '<a href="{3}" target="{4}" data-notify="url"></a>' +
                '</div>'
        });
    }

    @ViewChild('closeModal') private closeModal: ElementRef | undefined;

    public hideModel() {
        this.closeModal?.nativeElement.click();
    }


}

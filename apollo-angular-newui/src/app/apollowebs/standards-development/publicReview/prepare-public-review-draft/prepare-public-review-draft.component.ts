import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {
    CommentMadeRetrieved,
    Committee_Draft_With_Name,
    StandardDocuments
} from "../../../../core/store/data/std/commitee-model";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
import Swal from "sweetalert2";
import {formatDate} from "@angular/common";
import {PublicReviewService} from "../../../../core/store/data/std/publicReview.service";

declare const $: any;

@Component({
    selector: 'app-prepare-public-review-draft',
    templateUrl: './prepare-public-review-draft.component.html',
    styleUrls: ['./prepare-public-review-draft.component.css']
})
export class PreparePublicReviewDraftComponent implements OnInit {


    public publicReview_draftFormGroup!: FormGroup;
    committee_draft: Committee_Draft_With_Name | undefined;
    committee_drafts !: Committee_Draft_With_Name[];
    committee_draftsB !: Committee_Draft_With_Name | undefined;
    standardDocuments !: StandardDocuments[];

    commentMadeRetrievedS !: CommentMadeRetrieved[];
    commentMadeRetrievedB !: CommentMadeRetrieved | undefined;


    dateFormat = "yyyy-MM-dd";
    language = "en";

    blob: Blob;
    public uploadedFiles: Array<File> = [];
    public uploadedFilesB: Array<File> = [];
    public uploadedFilesC: Array<File> = [];

    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    dtTrigger3: Subject<any> = new Subject<any>();
    displayUsers: boolean = false;

    loadingDocsTable = false;

    loading = false;
    loadingText: string;
    validTextType: boolean = false;



    constructor(private formBuilder: FormBuilder,
                private committeeService: CommitteeService,
                private publicReviewService: PublicReviewService,
                private store$: Store<any>,
                private router: Router,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService) {
    }
    isFieldValid(form: FormGroup, field: string) {
        return !form.get(field).valid && form.get(field).touched;
    }

    displayFieldCss(form: FormGroup, field: string) {
        return {
            'has-error': this.isFieldValid(form, field),
            'has-feedback': this.isFieldValid(form, field)
        };
    }
    textValidationType(e) {
        this.validTextType = !!e;
    }


    ngOnInit(): void {
        this.getAllCds();
        this.publicReview_draftFormGroup = this.formBuilder.group({
            prdName: ['', Validators.required],
            ksNumber: ['', Validators.required]
        });

    }


    validateAllFormFields(formGroup: FormGroup) {
        Object.keys(formGroup.controls).forEach(field => {
            const control = formGroup.get(field);
            if (control instanceof FormControl) {
                control.markAsTouched({onlySelf: true});
            } else if (control instanceof FormGroup) {
                this.validateAllFormFields(control);
            }
        });
    }

    public getAllCds(): void {
        this.publicReviewService.getAllApprovedCommitteeDrafts().subscribe(
            (response: Committee_Draft_With_Name[]) => {
                console.log(response);

                this.committee_drafts = response;
                this.rerender()

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );

    }

    public getAllCdDocs(cdID: number) {
        this.loadingDocsTable = true
        this.displayUsers = false;
        this.SpinnerService.show("loader2");


        this.committeeService.getDocsOnCd(cdID).subscribe(
            (response: StandardDocuments[]) => {
                console.log(response)
                this.standardDocuments = response;
                // this.rerender()
                this.displayUsers = true;
                this.loadingDocsTable = false
                this.SpinnerService.hide();


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.displayUsers = true;
                this.loadingDocsTable = false
                this.SpinnerService.hide();
            }
        );
    }

    public getAllComments(pdID: number) {
        this.committeeService.getCommentsOnCd(pdID).subscribe(
            (response: CommentMadeRetrieved[]) => {

                this.commentMadeRetrievedS = response;
                this.rerender()

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    public uploadMinutesForPrd(cdId: number) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.publicReviewService.uploadMinutesForPrd(String(cdId), formData, "Minutes PRD", "Minutes PRD").subscribe(
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
                    this.hideModelE()
                    this.getAllCds();

                    // this.router.navigate(['/draftStandard']);
                    // this.getSACTasks();

                },
            );
        }
    }

    public uploadDraftsAndOtherDocs(cdId: number) {
        if (this.uploadedFilesB.length > 0) {
            const file = this.uploadedFilesB;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.publicReviewService.uploadPrdDraftDocuments(String(cdId), formData, "Drafts And Other Relevant Documents PRD", "Drafts And Other Relevant Documents PRD").subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFilesB = null;
                    console.log(data);
                    this.hideModelD()
                    swal.fire({
                        title: 'Draft Documents Uploaded Successfully.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    this.getAllCds();

                    // this.router.navigate(['/draftStandard']);
                    // this.getSACTasks();

                },
            );
        }
    }


    uploadPublicReviewDraft(formDirective): void {
        if (this.publicReview_draftFormGroup.valid) {

            if (this.uploadedFilesC != null && this.uploadedFilesC.length > 0) {
                this.loading = true
                this.loadingText = "Submitting  ...."
                this.SpinnerService.show();

                this.publicReviewService.preparePublicReviewDraft(this.publicReview_draftFormGroup.value, String(this.committee_draftsB.cdid)).subscribe(
                    (response) => {
                        console.log(response)
                        this.SpinnerService.hide();
                        this.showToasterSuccess(response.httpStatus, `Successfully Submitted Public Review Draft`);
                        this.uploadPrDDoc(response.body.savedRowID)
                        this.publicReview_draftFormGroup.reset();
                        formDirective.reset();

                    },
                    (error: HttpErrorResponse) => {
                        this.SpinnerService.hide();
                        console.log(error.message);
                    }
                );
            } else {
                this.showToasterError("Error", `Please Upload all the documents.`);
            }
        } else {
            this.validateAllFormFields(this.publicReview_draftFormGroup);
        }
    }

    public uploadPrDDoc(prdId: string) {

        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                formData.append('docFile', file[i], file[i].name);
            }
            this.publicReviewService.uploadMinutesForPrd(String(prdId), formData, "Minutes PRD", "Minutes PRD").subscribe();
        }
        if (this.uploadedFilesB.length > 0) {
            const file = this.uploadedFilesB;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                formData.append('docFile', file[i], file[i].name);
            }
            this.publicReviewService.uploadPrdDraftDocuments(String(prdId), formData, "Drafts And Other Relevant Documents PRD", "Drafts And Other Relevant Documents PRD").subscribe();
        }


        if (this.uploadedFilesC.length > 0) {
            const file = this.uploadedFilesC;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.publicReviewService.uploadPRDDocument(prdId, formData, "PRD Document", "PRD Document").subscribe(
                (data: any) => {
                    this.loading = false
                    this.SpinnerService.hide();
                    this.uploadedFiles = [];
                    this.uploadedFilesB = [];
                    this.uploadedFilesC = [];
                    this.publicReview_draftFormGroup.reset()
                    this.hideModelC()
                    swal.fire({
                        title: 'Public Review Draft Document Uploaded Successfully.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    this.getAllCds();

                },
            );
        }
    }

    approveCD(committeeDraft): void {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-danger'
            },
            buttonsStyling: false
        });

        swalWithBootstrapButtons.fire({
            title: 'Are you sure your want to approve this committee draft?',
            text: 'You won\'t be able to reverse this!',
            icon: 'success',
            showCancelButton: true,
            confirmButtonText: 'Approve!',
            cancelButtonText: 'Reject!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.SpinnerService.show();
                this.committeeService.approveCommitteeDraft(committeeDraft).subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Approved!',
                            'Committee Draft Successfully Approved!',
                            'success'
                        );
                        this.SpinnerService.hide();
                        this.showToasterSuccess(response.httpStatus, 'Committee Draft Successfully Approved');
                        this.getAllCds();
                    },
                );
            } else if (
                /* Read more about handling dismissals below */
                result.dismiss === swal.DismissReason.cancel
            ) {
                swalWithBootstrapButtons.fire(
                    'Cancelled',
                    'You have cancelled this operation',
                    'error'
                );
            }
        });
    }


    public onOpenModal(committeeDraft: Committee_Draft_With_Name, mode: string): void {

        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'comments') {
            this.committee_draftsB = committeeDraft;
            button.setAttribute('data-target', '#comments');
            this.getAllComments(committeeDraft.cdid)
        }
        if (mode === 'prepareCd') {
            this.committee_draftsB = committeeDraft;
            button.setAttribute('data-target', '#prepareCd');
        }
        if (mode === 'preparePrd') {
            this.committee_draftsB = committeeDraft;
            button.setAttribute('data-target', '#preparePrd');
        }
        if (mode === 'moreDetails') {
            this.committee_draftsB = committeeDraft;
            button.setAttribute('data-target', '#moreDetails');
            this.getAllCdDocs(committeeDraft.cdid)
        }
        if (mode === 'uploadDraftsAndOtherRelevantDocuments') {
            this.committee_draftsB = committeeDraft;
            button.setAttribute('data-target', '#uploadDraftsAndOtherRelevantDocuments');
        }
        // @ts-ignore
        container.appendChild(button);
        button.click();
    }

    public onOpenModalB(commentMadeRetrieved: CommentMadeRetrieved, mode: string): void {

        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'viewComment') {
            this.commentMadeRetrievedB = commentMadeRetrieved;
            button.setAttribute('data-target', '#viewComment');

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
    @ViewChild('closeModalB') private closeModalB: ElementRef | undefined;
    @ViewChild('closeModalC') private closeModalC: ElementRef | undefined;
    @ViewChild('closeModalD') private closeModalD: ElementRef | undefined;
    @ViewChild('closeModalE') private closeModalE: ElementRef | undefined;

    display = 'none'; //default Variable

    public hideModel() {
        this.closeModal?.nativeElement.click();
    }

    public hideModelB() {
        this.closeModalB?.nativeElement.click();
    }

    public hideModelC() {
        this.closeModalC?.nativeElement.click();
    }

    public hideModelD() {
        this.closeModalD?.nativeElement.click();
    }

    public hideModelE() {
        this.closeModalE?.nativeElement.click();
    }

    viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
        this.loading = true
        this.loadingText = "Downloading Document"
        this.SpinnerService.show();
        this.committeeService.viewDocsById(pdfId).subscribe(
            (dataPdf: any) => {
                this.loading = false
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

    viewPdfFileById(pdfId: number, fileName: string, applicationType: string, doctype: string): void {
        this.SpinnerService.show();
        this.committeeService.viewDocsById(pdfId).subscribe(
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

    rerender(): void {
        this.dtElements.forEach((dtElement: DataTableDirective) => {
            if (dtElement.dtInstance)
                dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
                    dtInstance.destroy();
                });
        });
        setTimeout(() => {
            this.dtTrigger1.next();
            this.dtTrigger2.next();
            this.dtTrigger3.next();

        });

    }


    ngOnDestroy(): void {
        // Do not forget to unsubscribe the event
        this.dtTrigger1.unsubscribe();
        this.dtTrigger2.unsubscribe();
        this.dtTrigger3.unsubscribe();

    }

    formatFormDate(date: string) {
        return formatDate(date, this.dateFormat, this.language);
    }
}

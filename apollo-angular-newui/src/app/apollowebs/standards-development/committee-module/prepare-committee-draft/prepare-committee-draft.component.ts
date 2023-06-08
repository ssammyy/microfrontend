import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {
    CommentMadeRetrieved,
    Preliminary_Draft_With_Name,
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
import {formatDate} from "@angular/common";
import swal from "sweetalert2";

declare const $: any;

@Component({
    selector: 'app-prepare-committee-draft',
    templateUrl: './prepare-committee-draft.component.html',
    styleUrls: ['./prepare-committee-draft.component.css']
})

export class PrepareCommitteeDraftComponent implements OnInit {

    preliminary_draft: Preliminary_Draft_With_Name | undefined;
    preliminary_drafts !: Preliminary_Draft_With_Name[];
    preliminary_draftsB !: Preliminary_Draft_With_Name | undefined;
    public committee_draftFormGroup!: FormGroup;

    standardDocuments !: StandardDocuments[];

    commentMadeRetrievedS !: CommentMadeRetrieved[];
    commentMadeRetrievedB !: CommentMadeRetrieved | undefined;

    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;
    isDtInitialized: boolean = false
    @ViewChild(DataTableDirective, {static: false})
    dtElementB: DataTableDirective;

    dateFormat = "yyyy-MM-dd";
    language = "en";

    blob: Blob;
    public uploadedFiles: Array<File> = [];
    public uploadedFilesB: Array<File> = [];
    public uploadedFilesC: Array<File> = [];


    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    dtTrigger3: Subject<any> = new Subject<any>();

    loading = false;
    loadingText: string;
    validTextType: boolean = false;



    constructor(private formBuilder: FormBuilder,
                private committeeService: CommitteeService,
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
    textValidationType(e) {
        this.validTextType = !!e;
    }


    ngOnInit(): void {
        this.getAllPds();
        this.committee_draftFormGroup = this.formBuilder.group({
            cdName: ['', Validators.required]
        });

    }

    id: any = 'Pending Review';

    tabChange(ids: any) {
        this.id = ids;
        if (this.id == "Pending Review") {
            this.reloadCurrentRoute()
        }
    }

    public getAllPds(): void {
        this.loading = true
        this.loadingText = "Retrieving Drafts Please Wait ...."
        this.SpinnerService.show()

        this.committeeService.getAllPdPendingCds().subscribe(
            (response: Preliminary_Draft_With_Name[]) => {

                this.preliminary_drafts = response;
                this.rerender()
                this.loading = false
                this.SpinnerService.hide();


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.loading = false
                this.SpinnerService.hide();
            }
        );

    }

    public getAllPdDocs(pdID: number) {
        this.committeeService.getDocsOnPd(pdID).subscribe(
            (response: StandardDocuments[]) => {

                this.standardDocuments = response;
                this.rerender()

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    public getAllComments(pdID: number) {
        this.committeeService.getCommentsOnPd(pdID).subscribe(
            (response: CommentMadeRetrieved[]) => {

                this.commentMadeRetrievedS = response;
                this.rerender()

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }
    public uploadCDDoc(cdId: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.committeeService.uploadMinutesForCd(String(cdId), formData, "Minutes CD", "Minutes CD").subscribe();
        }
        if (this.uploadedFilesB.length > 0) {
            const file = this.uploadedFilesB;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.committeeService.uploadCdDraftDocuments(String(cdId), formData, "Drafts And Other Relevant Documents CD", "Drafts And Other Relevant Documents CD").subscribe();
        }




        if (this.uploadedFilesC.length > 0) {
            const file = this.uploadedFilesC;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.committeeService.uploadCDDocument(cdId, formData, "CD Document", "CD Document").subscribe(
                (data: any) => {
                    this.loading = false
                    this.SpinnerService.hide();
                    this.uploadedFiles = [];
                    this.uploadedFilesB = [];
                    this.uploadedFilesC = [];

                    this.committee_draftFormGroup.reset()

                    this.hideModel()
                    swal.fire({
                        title: 'Committee Draft Document Uploaded Successfully.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    this.getAllPds();

                },
            );
        }
    }

    uploadCommitteeDraft(formDirective): void {
        if (this.committee_draftFormGroup.valid) {
            if (this.uploadedFilesC != null && this.uploadedFilesC.length > 0) {


                this.loading = true
                this.loadingText = "Submitting  ...."
                this.SpinnerService.show();



                this.committeeService.prepareCommitteeDraft(this.committee_draftFormGroup.value,  String(this.preliminary_draftsB.id)).subscribe(
                    (response) => {
                        console.log(response)
                        this.showToasterSuccess(response.httpStatus, `Successfully submitted Committee Draft`);
                        this.uploadCDDoc(response.body.savedRowID)
                        formDirective.reset();
                    },
                    (error: HttpErrorResponse) => {
                        this.SpinnerService.hide();
                        console.log(error.message);
                    }
                );
            } else {
                this.loading = false
                this.SpinnerService.hide();
                this.showToasterError("Error", `Please Upload The Committee Draft document.`);
            }
        } else {
            this.validateAllFormFields(this.committee_draftFormGroup);
        }
    }



    public onOpenModal(preliminaryDraft: Preliminary_Draft_With_Name, mode: string): void {

        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'comments') {
            this.preliminary_draftsB = preliminaryDraft;
            button.setAttribute('data-target', '#comments');
            this.getAllComments(preliminaryDraft.id)
        }
        if (mode === 'prepareCd') {
            this.preliminary_draftsB = preliminaryDraft;
            button.setAttribute('data-target', '#prepareCd');
        }
        if (mode === 'uploadMinutes') {
            this.preliminary_draftsB = preliminaryDraft;
            button.setAttribute('data-target', '#uploadMinutes');
        }
        if (mode === 'moreDetails') {
            this.preliminary_draftsB = preliminaryDraft;
            button.setAttribute('data-target', '#moreDetails');
            this.getAllPdDocs(preliminaryDraft.id)
        }
        if (mode === 'uploadDraftsAndOtherRelevantDocuments') {
            this.preliminary_draftsB = preliminaryDraft;
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

    viewPdfFile(pdfId: number, fileName: string, applicationType: string, doctype: string): void {
        this.SpinnerService.show();
        this.committeeService.viewDocs(pdfId, doctype).subscribe(
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

    reloadCurrentRoute() {
        let currentUrl = this.router.url;
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
            this.router.navigate([currentUrl]);
        });
    }
}

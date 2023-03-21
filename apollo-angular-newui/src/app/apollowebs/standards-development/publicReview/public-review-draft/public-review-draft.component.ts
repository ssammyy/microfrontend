import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {
    CommentMadeRetrieved,
    Preliminary_Draft, PublicReviewDraftWithName, StandardDocuments
} from "../../../../core/store/data/std/commitee-model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {PublicReviewService} from "../../../../core/store/data/std/publicReview.service";
import {formatDate} from "@angular/common";
import Swal from "sweetalert2";
import swal from "sweetalert2";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {
    DataHolderB,
    Department
} from "../../../../core/store/data/std/request_std.model";
import {IDropdownSettings} from "ng-multiselect-dropdown";

@Component({
    selector: 'app-public-review-draft',
    templateUrl: './public-review-draft.component.html',
    styleUrls: ['./public-review-draft.component.css']
})
export class PublicReviewDraftComponent implements OnInit {

    fullname = '';
    title = 'toaster-not';
    submitted = false;

    public editedPublicReview_draftFormGroup!: FormGroup;

    public publicReviewDrafts !: PublicReviewDraftWithName[];
    public publicReviewDraftsB !: PublicReviewDraftWithName | undefined;
    standardDocuments !: StandardDocuments[];

    dateFormat = "yyyy-MM-dd";
    language = "en";
    commentMadeRetrievedS !: CommentMadeRetrieved[];
    commentMadeRetrievedB !: CommentMadeRetrieved | undefined;

    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    dtTrigger3: Subject<any> = new Subject<any>();

    public departments !: Department[];
    dropdownList: any[] = [];
    selectedItems?: Department;
    public dropdownSettings: IDropdownSettings = {};

    blob: Blob;
    public uploadedFiles: FileList;

    constructor(private formBuilder: FormBuilder,
                private committeeService: CommitteeService,
                private store$: Store<any>,
                private router: Router,
                private standardDevelopmentService: StandardDevelopmentService,
                private publicReviewService: PublicReviewService,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService) {
    }

    ngOnInit(): void {
        this.getAllPrdS();
        this.getAllDepartments()
        this.editedPublicReview_draftFormGroup = this.formBuilder.group({
            prdName: ['', Validators.required],
            ksNumber:['', Validators.required]
        });
        this.dropdownSettings = {
            singleSelection: false,
            idField: 'id',
            textField: 'name',
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            allowSearchFilter: true
        };

    }

    public getAllPrdS(): void {
        this.publicReviewService.getAllPublicReviewDrafts().subscribe(
            (response: PublicReviewDraftWithName[]) => {
                console.log(response);

                this.publicReviewDrafts = response;
                this.rerender()
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );

    }

    public getAllPrddDocs(pdID: number) {
        this.publicReviewService.getAllDocumentsOnPrd(pdID).subscribe(
            (response: StandardDocuments[]) => {

                this.standardDocuments = response;
                this.rerender()

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    public getAllComments(prdID: number) {
        this.publicReviewService.getAllCommentsOnPrd(prdID).subscribe(
            (response: CommentMadeRetrieved[]) => {

                this.commentMadeRetrievedS = response;
                this.rerender()

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    public getAllDepartments(): void {
        this.standardDevelopmentService.getDepartmentsb().subscribe(
            (response: Department[]) => {
                this.departments = response;
                this.dropdownList = response;

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );


    }

    public sendToHeadOfAffairs(publicReviewDrafts: PublicReviewDraftWithName) {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-danger'
            },
            buttonsStyling: false
        });

        swalWithBootstrapButtons.fire({
            title: 'Are you sure your want to Sent To Head Of Trade Affairs?',
            text: 'You won\'t be able to reverse this!',
            icon: 'success',
            showCancelButton: true,
            confirmButtonText: 'YES!',
            cancelButtonText: 'NO!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.SpinnerService.show();
                this.publicReviewService.sendToOrganisation(publicReviewDrafts).subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Success!',
                            'Successfully Sent!',
                            'success'
                        );
                        this.SpinnerService.hide();
                        this.getAllPrdS();
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
    public postToWebsite(publicReviewDrafts: PublicReviewDraftWithName) {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-danger'
            },
            buttonsStyling: false
        });

        swalWithBootstrapButtons.fire({
            title: 'Are you sure your want to Post On The KEBS Website?',
            text: 'You won\'t be able to reverse this!',
            icon: 'success',
            showCancelButton: true,
            confirmButtonText: 'YES!',
            cancelButtonText: 'NO!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.SpinnerService.show();
                this.publicReviewService.postToWebsite(publicReviewDrafts).subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Success!',
                            'Successfully Posted!',
                            'success'
                        );
                        this.SpinnerService.hide();
                        this.getAllPrdS();
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

    public sendToDepartment(secTask: DataHolderB, id: number): void {

        if (secTask.departmentData != null) {
            //console.log(JSON.stringify(secTask.liaisonOrganisationData.name));
            secTask.department = JSON.stringify(secTask.departmentData);


            const swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                    confirmButton: 'btn btn-success',
                    cancelButton: 'btn btn-danger'
                },
                buttonsStyling: false
            });

            swalWithBootstrapButtons.fire({
                title: 'Are you sure your want to Sent To Interested Departments/Organisations?',
                text: 'You won\'t be able to reverse this!',
                icon: 'success',
                showCancelButton: true,
                confirmButtonText: 'Yes!',
                cancelButtonText: 'No!',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    this.SpinnerService.show();
                    this.publicReviewService.sendToDepartments(secTask, String(id)).subscribe(
                        (response) => {
                            this.SpinnerService.hide();
                            swalWithBootstrapButtons.fire(
                                'Success!',
                                'Successfully Sent!',
                                'success'
                            );
                            this.SpinnerService.hide();
                            this.getAllPrdS();
                            this.hideModelB()
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
        } else {
            this.showToasterError("Error", `Please Select An Organisation.`);

        }

    }

    uploadPublicReviewDraft(): void {
        if (this.uploadedFiles != null) {
            this.SpinnerService.show();
            this.publicReviewService.prepareEditedPublicReviewDraft(this.editedPublicReview_draftFormGroup.value, String(this.publicReviewDraftsB.id)).subscribe(
                (response) => {
                    console.log(response)
                    this.SpinnerService.hide();
                    this.showToasterSuccess(response.httpStatus, `Successfully Submitted Edited Public Review Draft`);
                    this.uploadEditedPrDDoc(response.body.savedRowID)
                    this.editedPublicReview_draftFormGroup.reset();
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

    public uploadEditedPrDDoc(prdId: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.publicReviewService.uploadEditedPRDDocument(prdId, formData, "PRD Document", "EDITED PRD Document").subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = null;
                    console.log(data);
                    this.hideModelC()
                    swal.fire({
                        title: 'Edited Public Review Draft Document Uploaded Successfully.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    this.getAllPrdS();

                },
            );
        }
    }

    public approveDraft(publicReviewDrafts: PublicReviewDraftWithName) {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-danger'
            },
            buttonsStyling: false
        });

        swalWithBootstrapButtons.fire({
            title: 'Are you sure your want to Approve This Public Review Draft?',
            text: 'You won\'t be able to reverse this!',
            icon: 'success',
            showCancelButton: true,
            confirmButtonText: 'Approve!',
            cancelButtonText: 'Reject!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.SpinnerService.show();
                this.publicReviewService.approvePublicReviewDraft(publicReviewDrafts).subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Success!',
                            'Successfully Approved!',
                            'success'
                        );
                        this.SpinnerService.hide();
                        this.getAllPrdS();
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

    public onOpenModal(publicReviewDrafts: PublicReviewDraftWithName, mode: string): void {

        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'comments') {
            console.log(publicReviewDrafts)
            this.publicReviewDraftsB = publicReviewDrafts;
            this.getAllComments(publicReviewDrafts.id)

            button.setAttribute('data-target', '#comments');
        }
        if (mode === 'editPublicReviewDraft') {
            this.publicReviewDraftsB = publicReviewDrafts
            button.setAttribute('data-target', '#editPublicReviewDraft');
        }
        if (mode === 'sendToOrganisations') {
            this.publicReviewDraftsB = publicReviewDrafts

            button.setAttribute('data-target', '#sendToOrganisations');
        }
        if (mode === 'moreDetails') {
            this.publicReviewDraftsB = publicReviewDrafts
            this.getAllPrddDocs(publicReviewDrafts.id)

            button.setAttribute('data-target', '#moreDetails');
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

    display = 'none'; //default Variable
    @ViewChild('closeModal') private closeModal: ElementRef | undefined;
    @ViewChild('closeModalB') private closeModalB: ElementRef | undefined;
    @ViewChild('closeModalC') private closeModalC: ElementRef | undefined;
    @ViewChild('closeModalD') private closeModalD: ElementRef | undefined;
    @ViewChild('closeModalE') private closeModalE: ElementRef | undefined;

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

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)
    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

    }
}

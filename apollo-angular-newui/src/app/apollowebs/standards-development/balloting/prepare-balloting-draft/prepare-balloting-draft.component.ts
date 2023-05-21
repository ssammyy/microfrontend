import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {
    CommentMadeRetrieved,
    PublicReviewDraftWithName,
    StandardDocuments
} from "../../../../core/store/data/std/commitee-model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {PublicReviewService} from "../../../../core/store/data/std/publicReview.service";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
import {formatDate} from "@angular/common";
import {Department} from "../../../../core/store/data/std/request_std.model";
import {IDropdownSettings} from "ng-multiselect-dropdown";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {BallotService} from "../../../../core/store/data/std/ballot.service";

declare const $: any;

@Component({
    selector: 'app-prepare-balloting-draft',
    templateUrl: './prepare-balloting-draft.component.html',
    styleUrls: ['./prepare-balloting-draft.component.css']
})
export class PrepareBallotingDraftComponent implements OnInit {
    fullname = '';
    title = 'toaster-not';
    submitted = false;

    public publicReview_draftFormGroup!: FormGroup;

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
    public uploadedFilesB: FileList;
    public uploadedFilesC: FileList;
    public uploadedFilesD: FileList;

    constructor(private formBuilder: FormBuilder,
                private committeeService: CommitteeService,
                private store$: Store<any>,
                private router: Router,
                private standardDevelopmentService: StandardDevelopmentService,
                private publicReviewService: PublicReviewService,
                private ballotService: BallotService,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService) {
    }

    ngOnInit(): void {
        this.getAllPrdS();
        this.getAllDepartments()
        this.publicReview_draftFormGroup = this.formBuilder.group({
            ballotName: ['', Validators.required],
            ksNumber: ['', Validators.required],
            prdID: ['', Validators.required]

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
        this.publicReviewService.getApprovePublicReviewDraft().subscribe(
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

    public getAllPrdDocs(pdID: number) {
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

    public uploadMinutesForBallot(prdId: number) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.ballotService.uploadMinutesForBallot(String(prdId), formData, "Minutes BALLOT", "Minutes BALLOT").subscribe(
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
                    this.getAllPrdS();

                    // this.router.navigate(['/draftStandard']);
                    // this.getSACTasks();

                },
            );
        }
    }

    public uploadDraftsAndOtherDocsBallot(prdId: number) {
        if (this.uploadedFilesB.length > 0) {
            const file = this.uploadedFilesB;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.ballotService.uploadBallotDraftDocuments(String(prdId), formData, "Drafts And Other Relevant Documents BALLOT", "Drafts And Other Relevant Documents BALLOT").subscribe(
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
                    this.getAllPrdS();

                    // this.router.navigate(['/draftStandard']);
                    // this.getSACTasks();

                },
            );
        }
    }


    uploadBallotDraft(): void {
        if (this.uploadedFilesC != null) {
            this.SpinnerService.show();
            this.ballotService.prepareBallotDraft(this.publicReview_draftFormGroup.value).subscribe(
                (response) => {
                    console.log(response)
                    this.SpinnerService.hide();
                    this.showToasterSuccess(response.httpStatus, `Successfully Submitted Ballot Draft`);
                    this.uploadBallotDoc(response.body.savedRowID)
                    this.publicReview_draftFormGroup.reset();
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

    public uploadBallotDoc(ballotId: string) {
        if (this.uploadedFilesC.length > 0) {
            const file = this.uploadedFilesC;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.ballotService.uploadBallotDocument(ballotId, formData, "BALLOT Document", "BALLOT Document").subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFilesC = null;
                    console.log(data);
                    this.hideModelC()
                    swal.fire({
                        title: 'Ballot Draft Uploaded Successfully.',
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


    public getAllBallotDocs(pdID: number) {
        this.ballotService.getAllDocumentsOnPrd(pdID).subscribe(
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
        if (mode === 'uploadMinutes') {
            this.publicReviewDraftsB = publicReviewDrafts
            button.setAttribute('data-target', '#uploadMinutes');
        }
        if (mode === 'uploadDraftsAndOtherRelevantDocuments') {
            this.publicReviewDraftsB = publicReviewDrafts

            button.setAttribute('data-target', '#uploadDraftsAndOtherRelevantDocuments');
        }
        if (mode === 'prepareBallot') {
            this.publicReviewDraftsB = publicReviewDrafts

            button.setAttribute('data-target', '#prepareBallot');
        }
        if (mode === 'moreDetails') {
            this.publicReviewDraftsB = publicReviewDrafts
            this.getAllPrdDocs(publicReviewDrafts.id)

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

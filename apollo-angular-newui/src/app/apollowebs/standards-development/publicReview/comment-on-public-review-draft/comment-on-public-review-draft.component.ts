import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {
    CommentMadeRetrieved,
    PublicReviewDraftWithName, StandardDocuments,
} from "../../../../core/store/data/std/commitee-model";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {formatDate} from "@angular/common";
import {HttpErrorResponse} from "@angular/common/http";
import Swal from "sweetalert2";
import swal from "sweetalert2";
import {PublicReviewService} from "../../../../core/store/data/std/publicReview.service";
import {CommentsDto} from "../../../../core/store/data/std/std.model";

declare const $: any;

@Component({
    selector: 'app-comment-on-public-review-draft',
    templateUrl: './comment-on-public-review-draft.component.html',
    styleUrls: ['./comment-on-public-review-draft.component.css']
})

export class CommentOnPublicReviewDraftComponent implements OnInit {
    public publicReviewDrafts !: PublicReviewDraftWithName[];
    publicReviewDraftsB !: PublicReviewDraftWithName | undefined;

    commentMadeRetrievedS !: CommentMadeRetrieved[];
    commentMadeRetrievedB !: CommentMadeRetrieved | undefined;

    dateFormat = "yyyy-MM-dd";
    language = "en";

    blob: Blob;
    public commentFormGroup!: FormGroup;
    public editCommentFormGroup!: FormGroup;

    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    dtTrigger3: Subject<any> = new Subject<any>();

    loading = false;
    loadingText: string;

    commentsMadeFormGroup: FormGroup;

    commentsDto: CommentsDto

    commentsDtos: CommentsDto[] = [];

    levelOfStandards: string[] = ['General', 'Technical', 'Editorial']

    standardDocuments !: StandardDocuments[];

    documentType = "Public Review Draft";

    displayUsers: boolean = false;

    loadingDocsTable= false;


    constructor(private formBuilder: FormBuilder,
                private committeeService: CommitteeService,
                private store$: Store<any>,
                private router: Router,
                private publicReviewService: PublicReviewService,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService) {
    }

    ngOnInit(): void {
        this.getAllPrds();
        this.getAllUserLoggedInCommentsMadeOnPd();

        this.commentsMadeFormGroup = this.formBuilder.group({
            clause: ['', Validators.required],
            paragraph: ['', Validators.required],
            typeOfComment: ['', Validators.required],
            proposedChange: ['', Validators.required],
            comment: ['', Validators.required],
        });


        this.editCommentFormGroup = this.formBuilder.group({
            recipientId: ['', Validators.required],
            title: ['', Validators.required],
            documentType: ['', Validators.required],
            circulationDate: ['', Validators.required],
            closingDate: ['', Validators.required],
            organization: ['', Validators.required],
            clause: ['', Validators.required],
            paragraph: ['', Validators.required],
            commentType: ['', Validators.required],
            proposedChange: ['', Validators.required],
            observation: ['', Validators.required],
            commentsMade: ['', Validators.required],
            id: ['', Validators.required],
        });
        this.dtOptions = {
            processing: true,
            lengthMenu: [10, 100, 500],
            destroy: true,
            lengthChange: false,
            searching: true,
            order: [[1, "desc"]],
        };
    }

    get formMakeComment(): any {
        return this.commentFormGroup.controls;
    }

    get formEditComment(): any {
        return this.editCommentFormGroup.controls;
    }

    formatFormDate(date: string) {
        return formatDate(date, this.dateFormat, this.language);
    }


    public getAllPrds(): void {
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

    saveComment(): void {
        this.SpinnerService.show();
        this.committeeService.makeComment(this.commentFormGroup.value, "PRD").subscribe(
            (response) => {
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, 'Successfully Submitted Comment');
                this.commentFormGroup.reset()
                this.hideModel()
                this.getAllUserLoggedInCommentsMadeOnPd();
                this.getAllPrds();

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
    }

    onClickAddComment() {

        if (this.commentsMadeFormGroup?.get('clause')?.value == null || this.commentsMadeFormGroup?.get('clause')?.value == '') {
            this.showToasterError("Error", "Please Enter Clause")
        } else if (this.commentsMadeFormGroup?.get('paragraph')?.value == null || this.commentsMadeFormGroup?.get('paragraph')?.value == '') {
            this.showToasterError("Error", "Please Enter Paragraph")
        } else if (this.commentsMadeFormGroup?.get('typeOfComment')?.value == null || this.commentsMadeFormGroup?.get('typeOfComment')?.value == '') {
            this.showToasterError("Error", "Please Select Comment Type")
        } else if (this.commentsMadeFormGroup?.get('comment')?.value == null || this.commentsMadeFormGroup?.get('comment')?.value == '') {
            this.showToasterError("Error", "Please Enter Your Comment")
        } else if (this.commentsMadeFormGroup?.get('proposedChange')?.value == null || this.commentsMadeFormGroup?.get('proposedChange')?.value == '') {
            this.showToasterError("Error", "Please Enter Proposed Change")
        } else {
            this.commentsDto = this.commentsMadeFormGroup.value;
            this.commentsDtos.push(this.commentsDto);
            this.commentsMadeFormGroup?.get('clause')?.reset();
            this.commentsMadeFormGroup?.get('paragraph')?.reset();
            this.commentsMadeFormGroup?.get('typeOfComment')?.reset();
            this.commentsMadeFormGroup?.get('comment')?.reset();
            this.commentsMadeFormGroup?.get('proposedChange')?.reset();
        }
        // this.sta10FormA.reset();
    }

    removeProductLabelling(index) {
        //(index);
        if (index === 0) {
            this.commentsDtos.splice(index, 1);
        } else {
            this.commentsDtos.splice(index, index);
        }
    }

    onClickSaveCommentsMade(public_review_draft_id: number) {
        this.loading = true
        if (this.commentsDtos.length > 0) {
            this.loadingText = "Saving Comment"
            this.SpinnerService.show();

            //(this.sta10Details.id.toString());
            this.committeeService.makeCommentB(this.commentsDtos, public_review_draft_id, "PRD").subscribe(
                (data) => {

                    this.SpinnerService.hide();
                    this.loading = false
                    this.commentsMadeFormGroup.reset()
                    this.hideModel()
                    this.commentsDtos = []
                    this.getAllUserLoggedInCommentsMadeOnPd();

                    swal.fire({
                        title: 'Comments Saved!',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                },
            );
        } else {
            this.loading = false

            swal.fire({
                title: 'Comments missing!',
                buttonsStyling: false,
                customClass: {
                    confirmButton: 'btn btn-success form-wizard-next-btn ',
                },
                icon: 'error'
            });
        }
    }

    editComment(): void {
        this.SpinnerService.show();
        this.committeeService.editComment(this.editCommentFormGroup.value).subscribe(
            (response) => {
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, 'Successfully Edited Comment');
                this.hideModelB()
                this.getAllUserLoggedInCommentsMadeOnPd();

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
    }

    deleteComment(comment): void {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-danger'
            },
            buttonsStyling: false
        });

        swalWithBootstrapButtons.fire({
            title: 'Are you sure your want to delete this comment?',
            text: 'You won\'t be able to reverse this!',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Approve!',
            cancelButtonText: 'Reject!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.SpinnerService.show();
                this.committeeService.deleteComment(comment).subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Deleted!',
                            'Comment Successfully Deleted!',
                            'success'
                        );
                        this.SpinnerService.hide();
                        this.showToasterSuccess(response.httpStatus, 'Successfully Deleted Comment');
                        this.getAllUserLoggedInCommentsMadeOnPd();
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

    public getAllUserLoggedInCommentsMadeOnPd(): void {
        this.publicReviewService.retrieveComment().subscribe(
            (response: CommentMadeRetrieved[]) => {
                console.log(response);

                this.commentMadeRetrievedS = response;

                this.rerender()
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );

    }


    public onOpenModal(publicReviewDraft: PublicReviewDraftWithName, mode: string): void {

        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'makeComment') {
            this.publicReviewDraftsB = publicReviewDraft;
            button.setAttribute('data-target', '#makeComment');
        }
        if (mode === 'viewCd') {
            this.publicReviewDraftsB = publicReviewDraft;
            this.getAllCdDocs(publicReviewDraft.id)
            button.setAttribute('data-target', '#viewCd');
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
        if (mode === 'editComment') {
            this.commentMadeRetrievedB = commentMadeRetrieved;
            button.setAttribute('data-target', '#editComment');

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

    display = 'none'; //default Variable

    public hideModel() {
        this.closeModal?.nativeElement.click();
    }

    public hideModelB() {
        this.closeModalB?.nativeElement.click();
    }


    viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
        this.loading= true
        this.loadingText="Downloading Document"
        this.SpinnerService.show();
        this.committeeService.viewDocsById(pdfId).subscribe(
            (dataPdf: any) => {
                this.loading=false
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
        });

    }


    ngOnDestroy(): void {
        // Do not forget to unsubscribe the event
        this.dtTrigger1.unsubscribe();
        this.dtTrigger2.unsubscribe();
    }

    public getAllCdDocs(cdID: number) {
        this.loadingDocsTable = true
        this.displayUsers = false;
        this.SpinnerService.show("loader2");


        this.publicReviewService.getAllDocumentsOnPrd(cdID).subscribe(
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

}

import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {
    CommentMadeRetrieved,
    Preliminary_Draft_With_Name,
    StandardDocuments
} from "../../../../core/store/data/std/commitee-model";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {formatDate} from '@angular/common';
import Swal from "sweetalert2";
import swal from "sweetalert2";
import {CommentsDto} from "../../../../core/store/data/std/std.model";

declare const $: any;

@Component({
    selector: 'app-review-preliminary-draft',
    templateUrl: './review-preliminary-draft.component.html',
    styleUrls: ['./review-preliminary-draft.component.css']
})
export class ReviewPreliminaryDraftComponent implements OnInit {
    preliminary_draft: Preliminary_Draft_With_Name | undefined;
    preliminary_drafts !: Preliminary_Draft_With_Name[];
    preliminary_draftsB !: Preliminary_Draft_With_Name | undefined;

    commentMadeRetrievedS !: CommentMadeRetrieved[];
    commentMadeRetrievedB !: CommentMadeRetrieved | undefined;


    dateFormat = "yyyy-MM-dd";
    language = "en";

    documentType = "Preliminary Draft";
    standardDocuments !: StandardDocuments[];


    blob: Blob;
    public commentFormGroup!: FormGroup;
    public editCommentFormGroup!: FormGroup;

    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    dtTrigger3: Subject<any> = new Subject<any>();

    commentsMadeFormGroup: FormGroup;

    commentsDto: CommentsDto

    commentsDtos: CommentsDto[] = [];

    levelOfStandards: string[] = ['General', 'Technical', 'Editorial']

    loading = false;
    loadingText: string;

    constructor(private formBuilder: FormBuilder,
                private committeeService: CommitteeService,
                private store$: Store<any>,
                private router: Router,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService) {
    }

    ngOnInit(): void {
        this.getAllPds();
        this.getAllCommentsMade();


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


    public getAllPds(): void {
        this.committeeService.getAllPreliminaryDrafts().subscribe(
            (response: Preliminary_Draft_With_Name[]) => {
                console.log(response);

                this.preliminary_drafts = response;
                this.rerender()

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );

    }

    editComment(): void {
        this.loading = true
        this.SpinnerService.show();
        this.committeeService.editComment(this.editCommentFormGroup.value).subscribe(
            (response) => {
                this.loading = false
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, 'Successfully Edited Comment');
                this.hideModelB()
                this.getAllCommentsMade();

            },
            (error: HttpErrorResponse) => {
                this.loading = false
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
            confirmButtonText: 'Yes!',
            cancelButtonText: 'No!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.loading = true
                this.SpinnerService.show();
                this.committeeService.deleteComment(comment).subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Deleted!',
                            'Comment Successfully Deleted!',
                            'success'
                        );
                        this.loading = false
                        this.SpinnerService.hide();
                        this.showToasterSuccess(response.httpStatus, 'Successfully Deleted Comment');
                        this.getAllCommentsMade();
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

    public getAllCommentsMade(): void {
        this.committeeService.retrieveComment().subscribe(
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


    public onOpenModal(preliminaryDraft: Preliminary_Draft_With_Name, mode: string): void {

        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'makeComment') {
            this.preliminary_draftsB = preliminaryDraft;
            button.setAttribute('data-target', '#makeComment');
        }

        if (mode === 'viewPD') {
            this.preliminary_draftsB = preliminaryDraft;
            this.getAllPdDocs(preliminaryDraft.id)

            button.setAttribute('data-target', '#viewPD');
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


    onClickAddComment() {

        console.log(this.commentsMadeFormGroup?.get('clause')?.value)


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
            console.log(this.commentsDtos.length)
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

    onClickSaveCommentsMade(preliminary_draft_id: number) {
        this.loading = true
        if (this.commentsDtos.length > 0) {
            this.loadingText="Saving Comment"
            this.SpinnerService.show();

            //(this.sta10Details.id.toString());
            this.committeeService.makeComment(this.commentsDtos, preliminary_draft_id, "PD").subscribe(
                (data) => {

                    this.SpinnerService.hide();
                    this.loading = false
                    this.commentsMadeFormGroup.reset()
                    this.hideModel()
                    this.commentsDtos=[]
                    this.getAllCommentsMade();

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


    public getAllPdDocs(pdID: number) {
        this.committeeService.getDocsOnPd(pdID).subscribe(
            (response: StandardDocuments[]) => {
                console.log(response)
                this.standardDocuments = response;
                // this.rerender()

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }


}

import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {
    ApprovedNwiS,
    CommentMadeRetrieved,
    Preliminary_Draft,
    Preliminary_Draft_With_Name
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
import {DatePipe} from '@angular/common';
import {formatDate} from "@angular/common";
import Swal from "sweetalert2";
import {AllPermitDetailsDto} from "../../../../core/store/data/qa/qa.model";
import swal from "sweetalert2";

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
    public commentFormGroup!: FormGroup;
    public editCommentFormGroup!: FormGroup;

    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();


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


        this.commentFormGroup = this.formBuilder.group({
            pdId: ['', Validators.required],
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

    saveComment(): void {
        this.SpinnerService.show();
        this.committeeService.makeComment(this.commentFormGroup.value,"PD").subscribe(
            (response) => {
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, 'Successfully Submitted Comment');
                this.commentFormGroup.reset()
                this.hideModel()
                this.getAllCommentsMade();
                this.getAllPds();

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
    }

    editComment(): void {
        this.SpinnerService.show();
        this.committeeService.editComment(this.editCommentFormGroup.value).subscribe(
            (response) => {
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, 'Successfully Edited Comment');
                this.hideModelB()
                this.getAllCommentsMade();

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
            confirmButtonText: 'Yes!',
            cancelButtonText: 'No!',
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


}

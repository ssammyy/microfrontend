import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {
    Ballot_Draft_With_Name,
    CommentMadeRetrieved,
    PublicReviewDraftWithName, VoteRetrieved
} from "../../../../core/store/data/std/commitee-model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {PublicReviewService} from "../../../../core/store/data/std/publicReview.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {formatDate} from "@angular/common";
import {HttpErrorResponse} from "@angular/common/http";
import Swal from "sweetalert2";
import swal from "sweetalert2";
import {BallotService} from "../../../../core/store/data/std/ballot.service";

declare const $: any;

@Component({
    selector: 'app-vote-on-ballot-draft',
    templateUrl: './vote-on-ballot-draft.component.html',
    styleUrls: ['./vote-on-ballot-draft.component.css']
})
export class VoteOnBallotDraftComponent implements OnInit {

    public ballotDrafts !: Ballot_Draft_With_Name[];
    ballotDraftB !: Ballot_Draft_With_Name | undefined;

    commentMadeRetrievedS !: VoteRetrieved[];
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


    constructor(private formBuilder: FormBuilder,
                private committeeService: CommitteeService,
                private store$: Store<any>,
                private router: Router,
                private ballotService: BallotService,
                private publicReviewService: PublicReviewService,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService) {
    }

    ngOnInit(): void {
        this.getAllBallots();
        this.getAllUserLoggedInCommentsMadeOnPd();


        this.commentFormGroup = this.formBuilder.group({
            approvalStatus: ['', Validators.required],
            comment: [''],
            ballotId:['',Validators.required]

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


    public getAllBallots(): void {
        this.ballotService.getAllBallotDrafts().subscribe(
            (response: Ballot_Draft_With_Name[]) => {
                console.log(response);

                this.ballotDrafts = response;
                this.rerender()
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    saveComment(): void {
        if (this.commentFormGroup.controls['approvalStatus'].value == "2" || this.commentFormGroup.controls['approvalStatus'].value == "3") {
            if (this.commentFormGroup.controls['comment'].value == "") {
                this.showToasterError("Reason", 'Please Provide A Reason');
            } else {
                this.vote();
            }
        } else {
            this.vote();
        }
    }

    vote(): void {
        this.SpinnerService.show();
        this.ballotService.vote(this.commentFormGroup.value).subscribe(
            (response) => {
                this.SpinnerService.hide();

                if(response.body=="You Have Already Voted")
                {
                    this.showToasterError("Error", response.body);

                }
                else {                    this.showToasterSuccess(response.httpStatus, response.body);
                }

                this.commentFormGroup.reset()
                this.hideModel()
                this.getAllUserLoggedInCommentsMadeOnPd();
                this.getAllBallots();

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
        this.ballotService.retrieveVote().subscribe(
            (response: VoteRetrieved[]) => {
                console.log(response);

                this.commentMadeRetrievedS = response;

                this.rerender()
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );

    }


    public onOpenModal(ballotDraftWithName: Ballot_Draft_With_Name, mode: string): void {

        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'makeComment') {
            this.ballotDraftB = ballotDraftWithName;
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
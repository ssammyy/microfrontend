import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {
    Ballot_Draft,
    CommentMadeRetrieved,
    PublicReviewDraftWithName,
    StandardDocuments, VoteRetrieved, VotesTally
} from "../../../../core/store/data/std/commitee-model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {Department} from "../../../../core/store/data/std/request_std.model";
import {IDropdownSettings} from "ng-multiselect-dropdown";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {PublicReviewService} from "../../../../core/store/data/std/publicReview.service";
import {BallotService} from "../../../../core/store/data/std/ballot.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {formatDate} from "@angular/common";
import Swal from "sweetalert2";
import swal from "sweetalert2";

@Component({
    selector: 'app-review-ballot-draft',
    templateUrl: './review-ballot-draft.component.html',
    styleUrls: ['./review-ballot-draft.component.css']
})
export class ReviewBallotDraftComponent implements OnInit {

    fullname = '';
    title = 'toaster-not';
    submitted = false;


    public votesTallies !: VotesTally[];
    public publicReviewDraftsB !: PublicReviewDraftWithName | undefined;
    standardDocuments !: StandardDocuments[];

    dateFormat = "yyyy-MM-dd";
    language = "en";
    voteRetrieved !: VoteRetrieved[];
    commentMadeRetrievedB !: CommentMadeRetrieved | undefined;

    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    dtTrigger3: Subject<any> = new Subject<any>();

    public ballotDrafts !: Ballot_Draft;
    selectedItems?: Department;
    blob: Blob;
    public uploadedFiles: FileList;
    public uploadedFilesB: FileList;

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
        this.getAllVotesOnBallots()

    }


    public getAllVotesOnBallots(): void {
        this.ballotService.getAllVotesTally().subscribe(
            (response: VotesTally[]) => {

                this.votesTallies = response;
                this.rerender()
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            });
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

    public onOpenModal(votes: VotesTally, mode: string): void {

        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'viewVotes') {
            this.getAllVotes(votes.ballot_ID)

            button.setAttribute('data-target', '#viewVotes');
        }


        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    display = 'none'; //default Variable
    @ViewChild('closeModal') private closeModal: ElementRef | undefined;

    public hideModel() {
        this.closeModal?.nativeElement.click();
    }


    private getAllVotes(ballotId: number) {
        this.ballotService.getAllVotesOnBallot(ballotId).subscribe(
            (response: VoteRetrieved[]) => {

                this.voteRetrieved = response;
                this.rerender()

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    approveBallotDraft(ballotId: number): void {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-success'
            },
            buttonsStyling: false
        });

        swalWithBootstrapButtons.fire({
            title: 'Are you sure your want to approve this ballot draft?',
            text: 'You won\'t be able to reverse this!',
            icon: 'success',
            showCancelButton: true,
            confirmButtonText: 'Yes!',
            cancelButtonText: 'No!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.SpinnerService.show();

                this.ballotDrafts = {
                    id: ballotId, slNo: ballotId,
                    cdNo: ballotId,
                    pdName: 'ballotId',
                    cdName: 'ballotId',
                    cdBy: 'ballotId',
                    approvalStatus: 'Approved',
                    status: 'ballotId'
                }
                console.log(this.ballotDrafts)
                this.ballotService.approveBallotDraft(this.ballotDrafts).subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Approved!',
                            'Ballot Draft Successfully Approved!',
                            'success'
                        );
                        this.SpinnerService.hide();
                        this.showToasterSuccess(response.httpStatus, 'Ballot Draft Successfully Approved');
                        this.getAllVotesOnBallots();
                    },
                );
            } else if (
                /* Read more about handling dismissals below */
                result.dismiss === swal.DismissReason.cancel


            ) {
                this.SpinnerService.show();

                this.ballotDrafts = {
                    id: ballotId, slNo: ballotId,
                    cdNo: ballotId,
                    pdName: 'ballotId',
                    cdName: 'ballotId',
                    cdBy: 'ballotId',
                    approvalStatus: 'Not Approved',
                    status: 'ballotId'
                }
                console.log(this.ballotDrafts)
                this.ballotService.approveBallotDraft(this.ballotDrafts).subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Rejected!',
                            'Ballot Draft Successfully Rejected!',
                            'success'
                        );
                        this.SpinnerService.hide();
                        this.showToasterSuccess(response.httpStatus, 'Ballot Draft Successfully Rejected');
                        this.getAllVotesOnBallots();
                    },
                );
                // swalWithBootstrapButtons.fire(
                //     'Cancelled',
                //     'You have cancelled this operation',
                //     'error'
                // );
            }
        });
    }


}


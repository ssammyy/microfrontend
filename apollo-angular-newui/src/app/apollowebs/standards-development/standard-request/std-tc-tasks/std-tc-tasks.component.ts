import {Component, ElementRef, Input, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {NWIsForVoting, StdTCDecision, VoteOnNWI} from "../../../../core/store/data/std/request_std.model";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {selectUserInfo, UserEntityService} from "../../../../core/store";
import {Store} from "@ngrx/store";
import {formatDate} from "@angular/common";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
    selector: 'app-std-tc-tasks',
    templateUrl: './std-tc-tasks.component.html',
    styleUrls: ['./std-tc-tasks.component.css']
})
export class StdTcTasksComponent implements OnInit {
    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    dtTrigger3: Subject<any> = new Subject<any>();
    dtTrigger4: Subject<any> = new Subject<any>();
    dtTrigger5: Subject<any> = new Subject<any>();
    dtTrigger6: Subject<any> = new Subject<any>();
    p = 1;
    p2 = 1;


    dateFormat = "yyyy-MM-dd";
    language = "en";

    public tcTasks: NWIsForVoting[] = [];
    // selected item
    decision: string;
    @Input() selectSeason: string;

    ps: any;
    userId: number = Number('');

    public stdTCDecisions: StdTCDecision[] = [];
    public formActionRequest: VoteOnNWI | undefined;

    public itemId: string = "";
    public filePurposeAnnex: string = "FilePurposeAnnex";
    public relevantDocumentsNWI: string = "RelevantDocumentsNWI";

    stdHOFReview: FormGroup;
    selectedNwi: string;


    public actionRequest: NWIsForVoting | undefined;

    constructor(
        private standardDevelopmentService: StandardDevelopmentService,
        private SpinnerService: NgxSpinnerService,
        private notifyService: NotificationService,
        private store$: Store<any>,
        private service: UserEntityService,
        private formBuilder: FormBuilder,
    ) {
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

    ngOnInit(): void {
        this.getTCTasks();
        this.decision = this.selectSeason;
        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            return this.userId = u.id;
        });
        this.stdHOFReview = this.formBuilder.group({
            decision: ['', Validators.required],
            position: ['', Validators.required],
            organization: ['', Validators.required],
            nwiId: ['', Validators.required],
            reason: ['', Validators.required],

        });

    }

    public getTCTasks(): void {
        this.standardDevelopmentService.getAllNwisForVoting().subscribe(
            (response: NWIsForVoting[]) => {
                console.log(response);
                this.tcTasks = response;
                this.rerender()


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
    }

    public onOpenModal(task: NWIsForVoting, mode: string): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        console.log(task.id)

        this.itemId = task.id;
        if (mode === 'edit') {
            this.actionRequest = task;

            this.selectedNwi = this.actionRequest.id

            button.setAttribute('data-target', '#voteDecisionModal');
        }

        if (mode === 'reject') {
            this.actionRequest = task;
            button.setAttribute('data-target', '#rejectionModal');
        }

        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)

    }

    public onReviewTask(): void {


        if (this.stdHOFReview.valid) {
            this.SpinnerService.show();

            this.standardDevelopmentService.decisionOnNWI(this.stdHOFReview.value).subscribe(
                (response) => {
                    if (response.body == "You Have Already Voted") {
                        this.showToasterError("Error", response.body);

                    } else {
                        this.showToasterSuccess(response.httpStatus, response.body);
                    }
                    this.SpinnerService.hide();

                    this.getTCTasks();
                    this.stdHOFReview.reset();
                    this.hideModel()
                },
                (error: HttpErrorResponse) => {
                    alert(error.message);
                    this.SpinnerService.hide();

                }
            )
        } else {
            this.showToasterError("Error", `Please Fill In All The Fields.`);

        }
    }


    @ViewChild('closeModal') private closeModal: ElementRef | undefined;

    public hideModel() {
        this.closeModal?.nativeElement.click();
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
            this.dtTrigger4.next();
            this.dtTrigger5.next();
            this.dtTrigger6.next();

        });

    }

    ngOnDestroy(): void {
        // Do not forget to unsubscribe the event
        this.dtTrigger1.unsubscribe();
        this.dtTrigger2.unsubscribe();
        this.dtTrigger3.unsubscribe();
        this.dtTrigger4.unsubscribe();
        this.dtTrigger5.unsubscribe();
        this.dtTrigger6.unsubscribe();

    }

    formatFormDate(date: Date) {
        return formatDate(date, this.dateFormat, this.language);
    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

    }

    // viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
    //     this.SpinnerService.show();
    //     this.committeeService.viewDocsById(pdfId).subscribe(
    //         (dataPdf: any) => {
    //             this.SpinnerService.hide();
    //             this.blob = new Blob([dataPdf], {type: applicationType});
    //
    //             // tslint:disable-next-line:prefer-const
    //             let downloadURL = window.URL.createObjectURL(this.blob);
    //             window.open(downloadURL, '_blank');
    //
    //             // this.pdfUploadsView = dataPdf;
    //         },
    //     );
    // }
}

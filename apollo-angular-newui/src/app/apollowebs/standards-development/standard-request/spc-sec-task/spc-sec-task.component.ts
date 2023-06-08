import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Document, StdJustification, StdJustificationDecision} from "../../../../core/store/data/std/request_std.model";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {UserRegister} from "../../../../shared/models/user";
import {ActivatedRoute} from "@angular/router";
import {MasterService} from "../../../../core/store/data/master/master.service";
import {formatDate} from "@angular/common";

@Component({
    selector: 'app-spc-sec-task',
    templateUrl: './spc-sec-task.component.html',
    styleUrls: ['./spc-sec-task.component.css']
})
export class SpcSecTaskComponent implements OnInit {

    p = 1;
    p2 = 1;
    public tcTasks: StdJustification[] = [];
    public actionRequest: StdJustification | undefined;
    dateFormat = "yyyy-MM-dd";
    language = "en";

    docs !: Document[];
    blob: Blob;

    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    dtOptions: DataTables.Settings = {};
    public userDetails!: UserRegister;

    loading = false;
    loadingText: string;

    constructor(
        private standardDevelopmentService: StandardDevelopmentService,
        private SpinnerService: NgxSpinnerService,
        private notifyService: NotificationService,
        private committeeService: CommitteeService,
        private route: ActivatedRoute,
        private masterService: MasterService,
    ) {
    }

    ngOnInit(): void {
        this.getSPCSECTasks();
    }

    public getSPCSECTasks(): void {
        this.loading = true
        this.loadingText = "Retrieving Justifications Please Wait ...."
        this.SpinnerService.show()
        this.standardDevelopmentService.getJustificationsPendingDecision().subscribe(
            (response: StdJustification[]) => {
                // console.log(response);
                this.tcTasks = response;
                this.rerender()
                this.loading = false
                this.SpinnerService.hide();
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.loading = false
                this.SpinnerService.hide();
            }
        )
    }


    public onOpenModal(task: StdJustification, mode: string): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        console.log(task.taskId)


        if (mode === 'edit') {
            this.actionRequest = task;
            this.getSelectedUser(task.tcSecretary)
            this.getAllDocs(String(task.id), "Justification Documents")
            button.setAttribute('data-target', '#justificationDecisionModal');
        }


        if (mode === 'reject') {
            this.actionRequest = task;
            button.setAttribute('data-target', '#rejectDecisionModal');
        }
        if (mode === 'approve') {
            this.actionRequest = task;
            button.setAttribute('data-target', '#approveDecisionModal');
        }
        if (mode === 'rejectWithAmendments') {
            this.actionRequest = task;
            button.setAttribute('data-target', '#rejectWithAmendmentsDecisionModal');
        }

        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)

    }

    public decisionOnJustification(stdJustificationDecision: StdJustificationDecision): void {
        this.loading = true
        this.loadingText = "Submitting Please Wait ...."
        this.SpinnerService.show();
        this.standardDevelopmentService.decisionOnJustification(stdJustificationDecision).subscribe(
            (response) => {
                this.showToasterSuccess(response.httpStatus, `Your Decision Has Been Submitted.`);
                this.loading = false
                this.SpinnerService.hide();
                this.hideModel();
                this.hideModelB();
                this.hideModelC();
                this.hideModelD();

                this.getSPCSECTasks();
            },
            (error: HttpErrorResponse) => {
                this.loading = false
                this.SpinnerService.hide();

                alert(error.message);

            }
        )
    }

    @ViewChild('closeViewModal') private closeModal: ElementRef | undefined;
    @ViewChild('closeViewModalB') private closeModalB: ElementRef | undefined;
    @ViewChild('closeViewModalC') private closeModalC: ElementRef | undefined;
    @ViewChild('closeViewModalD') private closeModalD: ElementRef | undefined;

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

    public getAllDocs(nwiId: string, processName: string): void {
        this.loading = true
        this.loadingText = "Retrieving Document ...."

        this.SpinnerService.show();

        this.standardDevelopmentService.getAdditionalDocumentsByProcess(nwiId, processName).subscribe(
            (response: Document[]) => {
                this.docs = response;
                this.loading = false
                this.SpinnerService.hide();


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
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

    viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.committeeService.viewDocsById(pdfId).subscribe(
            (dataPdf: any) => {
                this.SpinnerService.hide();
                this.blob = new Blob([dataPdf], {type: applicationType});

                // tslint:disable-next-line:prefer-const
                let downloadURL = window.URL.createObjectURL(this.blob);
                window.open(downloadURL, '_blank');

                // this.pdfUploadsView = dataPdf;
            },
        );
    }

    private getSelectedUser(userId) {
        this.route.fragment.subscribe(params => {
            console.log(userId);
            this.masterService.loadUserDetails(userId).subscribe(
                (data: UserRegister) => {
                    this.userDetails = data;
                }
            );

        });
    }

    formatFormDate(date: Date) {
        return formatDate(date, this.dateFormat, this.language);
    }

}

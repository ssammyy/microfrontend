import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {DraftPublishing, StdTCDecision} from "../../../../core/store/data/std/request_std.model";
import {PublishingService} from "../../../../core/store/data/std/publishing.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";

@Component({
    selector: 'app-std-hop-tasks',
    templateUrl: './std-hop-tasks.component.html',
    styleUrls: ['./std-hop-tasks.component.css']
})
export class StdHopTasksComponent implements OnInit {
    p = 1;
    p2 = 1;
    tasks: DraftPublishing[] = [];
    approvedTasks: DraftPublishing[] = [];
    rejectedTasks: DraftPublishing[] = [];
    public actionRequest: DraftPublishing | undefined;
    public formActionRequest: StdTCDecision | undefined;
    fullname = '';
    title = 'toaster-not';
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;
    isDtInitialized: boolean = false
    public itemId: number;
    loadingText: string;
    blob: Blob;

    displayedColumns: string[] = ['id', 'title', 'status', 'reason', 'actions'];
    dataSource!: MatTableDataSource<DraftPublishing>;
    dataSourceB!: MatTableDataSource<DraftPublishing>;
    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;


    constructor(private publishingService: PublishingService, private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService, private committeeService: CommitteeService,
    ) {
    }

    ngOnInit(): void {
        this.getHOPTasks();
        this.getApprovedTasks();
        this.getRejectedTasks();

    }

    public getHOPTasks(): void {
        this.loadingText = "Retrieving Data Please Wait ...."
        this.SpinnerService.show();
        this.publishingService.getHOPTasks().subscribe(
            (response: DraftPublishing[]) => {
                this.tasks = response;
                console.log(response)
                this.SpinnerService.hide();

                if (this.isDtInitialized) {
                    this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
                        dtInstance.destroy();
                        this.dtTrigger.next();
                    });
                } else {
                    this.isDtInitialized = true
                    this.dtTrigger.next();
                }
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.SpinnerService.hide();
            }
        );
    }

    public getApprovedTasks(): void {
        this.publishingService.getHOPTasksApproved().subscribe(
            (response: DraftPublishing[]) => {
                this.approvedTasks = response;
                this.dataSource = new MatTableDataSource(this.approvedTasks);

                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    public getRejectedTasks(): void {
        this.publishingService.getHOPTasksRejected().subscribe(
            (response: DraftPublishing[]) => {
                this.rejectedTasks = response;
                this.dataSourceB = new MatTableDataSource(this.rejectedTasks);

                this.dataSourceB.paginator = this.paginator;
                this.dataSourceB.sort = this.sort;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)

    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

    }

    public onOpenModal(task: DraftPublishing, mode: string): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        console.log(task.id);
        if (mode === 'edit') {
            this.actionRequest = task;
            button.setAttribute('data-target', '#justificationDecisionModal');
        }

        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    public decisionOnDraft(stdTCDecision: DraftPublishing, decision: string, reason: DraftPublishing): void {

        if (reason.varField1 != "") {
            this.loadingText = "Submitting ...."
            this.SpinnerService.show();
            stdTCDecision.varField1 = reason.varField1
            this.publishingService.decisionOnKSDraft(stdTCDecision, decision).subscribe(
                (response) => {
                    console.log(response);
                    this.SpinnerService.hide()
                    this.showToasterSuccess(response.httpStatus, `Successfully Submitted.`);
                    this.getHOPTasks();
                },
                (error: HttpErrorResponse) => {
                    alert(error.message);
                    this.SpinnerService.hide()

                }
            )
        } else {
            this.showToasterError("Error", `Please Provide A Reason`);

        }

    }

    @ViewChild('closeModal') private closeModal: ElementRef | undefined;

    public hideModel() {
        this.closeModal?.nativeElement.click();
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

    applyFilter(event: Event) {
        const filterValue = (event.target as HTMLInputElement).value;
        this.dataSource.filter = filterValue.trim().toLowerCase();
        this.dataSourceB.filter = filterValue.trim().toLowerCase();

        if (this.dataSource.paginator) {
            this.dataSource.paginator.firstPage();
        }
        if (this.dataSourceB.paginator) {
            this.dataSourceB.paginator.firstPage();
        }

    }
}

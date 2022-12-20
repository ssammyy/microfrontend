import {Component, ElementRef, Input, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {Document, HOFFeedback, StandardRequestB, TaskData} from "../../../../core/store/data/std/request_std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {Department, StandardRequest, UsersEntity} from "../../../../core/store/data/std/std.model";
import {MatSelect} from "@angular/material/select";
import {MatOption} from "@angular/material/core";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {formatDate} from "@angular/common";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";

@Component({
    selector: 'app-standard-task',
    templateUrl: './standard-task.component.html',
    styleUrls: ['./standard-task.component.css']
})
export class StandardTaskComponent implements OnInit {
    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    dtTrigger3: Subject<any> = new Subject<any>();
    dtTrigger4: Subject<any> = new Subject<any>();
    dtTrigger5: Subject<any> = new Subject<any>();
    dtTrigger6: Subject<any> = new Subject<any>();

    // data source for the radio buttons:
    seasons: string[] = ['Develop a standard through committee draft', 'Adopt existing International Standard', 'Review existing Kenyan Standard',
        'Development of publicly available specification', 'Development of national workshop agreement', 'Adoption of EA and other regions standards'];

    outputs: string[] = ['Approve For Review', 'Reject For Review'];


    // selected item
    sdOutput: string;
    blob: Blob;
    selectedDepartment: string;
    selectedStandard: number;
    sdResult: string;

    dateFormat = "yyyy-MM-dd";
    language = "en";

    // to dynamically (by code) select item
    // from the calling component add:
    @Input() selectSeason: string;
    public departments !: Department[];
    public tcSecs !: UsersEntity[];
    @Input() selectDesiredResult: string;

    p = 1;
    p2 = 1;
    countLine = 0;
    tasks: StandardRequestB[] = [];
    approvedTasks: StandardRequestB[] = [];
    rejectedTasks: StandardRequestB[] = [];


    displayedColumns: string[] = ['requestNumber', 'departmentName', 'subject', 'name', 'actions'];
    displayedColumn: string[] = ['requestNumber', 'departmentName', 'subject', 'name', 'actions', 'action'];
    dataSource!: MatTableDataSource<StandardRequestB>;
    dataSourceB!: MatTableDataSource<StandardRequestB>;
    dataSourceC!: MatTableDataSource<StandardRequestB>;
    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;


    public actionRequest: StandardRequestB;

    public hofFeedback: HOFFeedback | undefined;
    public standardRequest: StandardRequest | undefined;
    stdDepartmentChange: FormGroup;
    stdHOFReview: FormGroup;

    public technicalName = "";
    docs !: Document[];
    dtOptionsB: DataTables.Settings = {};

    public taskData: TaskData | undefined;

    constructor(private standardDevelopmentService: StandardDevelopmentService,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService,
                private formBuilder: FormBuilder,
                private committeeService: CommitteeService,
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
        this.getHOFTasks();
        this.getApprovedTasks();
        this.getRejectedTasks();

        this.stdDepartmentChange = this.formBuilder.group({
            departmentId: ['', Validators.required],
            id: ['', Validators.required],

        });
        this.stdHOFReview = this.formBuilder.group({
            isTcSec: ['', Validators.required],
            isTc: ['', Validators.required],
            sdOutput: ['', Validators.required],
            id: ['', Validators.required],
            sdRequestID: ['', Validators.required],
            sdResult: ['', Validators.required],
            reason: ['', Validators.required],

        });


    }

    ngAfterViewInit(): void {


        this.sdOutput = this.selectSeason;
        this.sdResult = this.selectDesiredResult;


    }

    @ViewChild('closeModal') private closeModal: ElementRef | undefined;
    @ViewChild('closeModalB') private closeModalB: ElementRef | undefined;
    @ViewChild('closeModalC') private closeModalC: ElementRef | undefined;

    public hideModel() {
        this.closeModal?.nativeElement.click();
    }

    public hideModelB() {
        this.closeModalB?.nativeElement.click();
    }

    public hideModelC() {
        this.closeModalC?.nativeElement.click();
    }

    get formStdRequest(): any {
        return this.stdDepartmentChange.controls;
    }

    public getHOFTasks(): void {
        this.SpinnerService.show()
        this.standardDevelopmentService.getHOFTasks().subscribe(
            (response: StandardRequestB[]) => {
                this.tasks = response;
                this.SpinnerService.hide()
                this.dataSource = new MatTableDataSource(this.tasks);

                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.SpinnerService.hide()

            }
        );
    }

    public getApprovedTasks(): void {
        this.standardDevelopmentService.getTCSECTasks().subscribe(
            (response: StandardRequestB[]) => {
                this.approvedTasks = response;
                this.dataSourceB = new MatTableDataSource(this.approvedTasks);

                this.dataSourceB.paginator = this.paginator;
                this.dataSourceB.sort = this.sort;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    public getRejectedTasks(): void {
        this.standardDevelopmentService.getRejectedReviewsForStandards().subscribe(
            (response: StandardRequestB[]) => {
                this.rejectedTasks = response;
                this.dataSourceC = new MatTableDataSource(this.rejectedTasks);

                this.dataSourceC.paginator = this.paginator;
                this.dataSourceC.sort = this.sort;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    public getTechnicalCommitteeName(id: number): void {
        this.standardDevelopmentService.getTechnicalCommitteeName(id).subscribe(
            (response: string) => {
                this.technicalName = response;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    public onReviewTask(): void {

        if (this.stdHOFReview.valid) {
            this.SpinnerService.show();

            this.standardDevelopmentService.reviewTask(this.stdHOFReview.value).subscribe(
                (response) => {
                    this.showToasterSuccess(response.httpStatus, `Your Feedback Has Been Submitted to the TC Secretary.`);
                    this.SpinnerService.hide();
                    this.getHOFTasks();
                    this.getApprovedTasks();
                    this.getRejectedTasks();
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

    public updateDepartment(): void {
        console.log(this.stdDepartmentChange.value)

        if (this.stdDepartmentChange.valid) {
            this.SpinnerService.show();

            this.standardDevelopmentService.updateDepartmentStandardRequest(this.stdDepartmentChange.value).subscribe(
                (response) => {
                    this.showToasterSuccess(response.httpStatus, `Section Reassigned`);
                    this.getHOFTasks();

                    this.hideModelB()
                    this.clear()

                    this.SpinnerService.hide();
                },
                (error: HttpErrorResponse) => {
                    alert(error.message);
                    this.SpinnerService.hide();

                }
            )
        } else {
            this.showToasterError("Error", `Please Select A Sector.`);

        }

    }

    public onOpenModal(task: StandardRequestB, mode: string): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'edit') {
            this.actionRequest = task;
            button.setAttribute('data-target', '#updateRequestModal');
            this.getAllDocs(String(this.actionRequest.id))
            this.getTcSecs()
            this.selectedStandard = this.actionRequest.id


        }
        if (mode === 'divisions') {
            this.actionRequest = task;
            button.setAttribute('data-target', '#divisionChange');
            this.getDepartments()
            this.selectedDepartment = this.actionRequest.departmentName
            this.selectedStandard = this.actionRequest.id


        }
        if (mode === 'view') {
            this.actionRequest = task;
            button.setAttribute('data-target', '#viewRequestModal');
            this.getAllDocs(String(this.actionRequest.id))
            this.getTcSecs()
            this.selectedStandard = this.actionRequest.id


        }

        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)

    }

    rerender(): void {
        this.dtElements.forEach((dtElement: DataTableDirective) => {
            if (dtElement.dtInstance)
                dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
                    dtInstance.clear();
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

    public getAllDocs(standardId: string): void {
        this.standardDevelopmentService.getAdditionalDocuments(standardId).subscribe(
            (response: Document[]) => {
                this.docs = response;
                this.rerender()


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
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

    public getDepartments(): void {
        this.standardDevelopmentService.getDepartmentsb().subscribe(
            (response: Department[]) => {
                this.departments = response;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    public getTcSecs(): void {
        this.standardDevelopmentService.getTcSec().subscribe(
            (response: UsersEntity[]) => {
                this.tcSecs = response;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    @ViewChild('matRef') matRef: MatSelect;

    clear() {
        this.matRef.options.forEach((data: MatOption) => data.deselect());
    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

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

    applyFilter(event: Event) {
        const filterValue = (event.target as HTMLInputElement).value;
        this.dataSource.filter = filterValue.trim().toLowerCase();
        this.dataSourceB.filter = filterValue.trim().toLowerCase();
        this.dataSourceC.filter = filterValue.trim().toLowerCase();

        if (this.dataSource.paginator) {
            this.dataSource.paginator.firstPage();
        }
        if (this.dataSourceB.paginator) {
            this.dataSourceB.paginator.firstPage();
        }
        if (this.dataSourceC.paginator) {
            this.dataSourceC.paginator.firstPage();
        }
    }


}


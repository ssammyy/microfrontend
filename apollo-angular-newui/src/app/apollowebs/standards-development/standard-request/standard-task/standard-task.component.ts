import {Component, ElementRef, Input, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {
    DataHolder,
    Document,
    HOFFeedback,
    StandardRequestB,
    TaskData
} from "../../../../core/store/data/std/request_std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {ReplaySubject, Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {Department, StandardRequest, UsersEntity} from "../../../../core/store/data/std/std.model";
import {MatSelect} from "@angular/material/select";
import {MatOption} from "@angular/material/core";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {formatDate} from "@angular/common";
import {MatRadioChange} from '@angular/material/radio';
import {Router} from "@angular/router";
import {takeUntil} from "rxjs/operators";

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

    dtTrigger4: Subject<any> = new Subject<any>();


    onApproveApplication: boolean = false;
    onDeclineApplication: boolean = false;

    tcSecAvailabilty: boolean = false;

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
    onHoldTasks: StandardRequestB[] = [];

    loading = false;
    loadingText: string;


    public actionRequest: StandardRequestB;

    public hofFeedback: HOFFeedback | undefined;
    public standardRequest: StandardRequest | undefined;
    stdDepartmentChange: FormGroup;
    stdHOFReview: FormGroup;

    public technicalName = "";
    docs !: Document[];
    dtOptionsB: DataTables.Settings = {};

    public taskData: TaskData | undefined;

    @ViewChild('singleSelect', {static: true}) singleSelect: MatSelect;
    public websiteFilterCtrl: FormControl = new FormControl();
    public filteredTcs: ReplaySubject<any> = new ReplaySubject(1);
    protected _onDestroy = new Subject();

    public tcs: DataHolder[] = [];


    constructor(private standardDevelopmentService: StandardDevelopmentService,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService,
                private formBuilder: FormBuilder,
                private router: Router,
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
        this.getOnHoldTasks();
        this.getTechnicalCommittee();

        this.stdDepartmentChange = this.formBuilder.group({
            departmentId: ['', Validators.required],
            id: ['', Validators.required],

        });
        this.stdHOFReview = this.formBuilder.group({
            isTcSec: ['', Validators.required],
            isTc: [''],
            sdOutput: [''],
            id: ['', Validators.required],
            sdRequestID: ['', Validators.required],
            sdResult: ['', Validators.required],
            reason: [''],
            rejectionReason: [''],
            link: [''],
            tcId: [''],


        });
        this.filteredTcs.next(this.tcs.slice());
        this.websiteFilterCtrl.valueChanges
            .pipe(takeUntil(this._onDestroy))
            .subscribe(() => {
                this.filteredTc();
            });
    }

    id: any = 'Pending Review';

    tabChange(ids: any) {
        this.id = ids;
        if (this.id == "Pending Review") {
            this.reloadCurrentRoute()
        }
    }

    ngAfterViewInit(): void {
        this.sdOutput = this.selectSeason;
        this.sdResult = this.selectDesiredResult;
    }

    @ViewChild('closeModal') private closeModal: ElementRef | undefined;
    @ViewChild('closeModalB') private closeModalB: ElementRef | undefined;
    @ViewChild('closeModalC') private closeModalC: ElementRef | undefined;
    @ViewChild('searchInput', { static: false }) searchInput: ElementRef;


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
        this.loading = true
        this.loadingText = "Retrieving Applications Please Wait ...."
        this.SpinnerService.show()
        this.standardDevelopmentService.getHOFTasks().subscribe(
            (response: StandardRequestB[]) => {
                this.tasks = response;
                this.loading = false
                this.SpinnerService.hide()
                this.rerender()

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.loading = false
                this.SpinnerService.hide()
            }
        );
    }


    public getOnHoldTasks(): void {
        this.standardDevelopmentService.getOnHoldReviewsForStandards().subscribe(
            (response: StandardRequestB[]) => {
                this.onHoldTasks = response;

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
        this.SpinnerService.show();

        if (this.stdHOFReview.controls['sdResult'].value == 'On Hold' && this.stdHOFReview.controls['reason'].value == '') {
            this.showToasterError("Error", `Please Enter A Reason For Putting On Hold.`);
            this.SpinnerService.hide();
            return;
        }

        if (this.stdHOFReview.controls['sdResult'].value == 'Reject For Review') {
            if (this.stdHOFReview.controls['rejectionReason'].value == '') {
                this.showToasterError("Error", `Please Select An Option For Rejection.`);
            } else if (this.stdHOFReview.controls['reason'].value == '') {
                this.showToasterError("Error", `Please Enter A Reason For Rejection.`);
            } else {
                this.submitTask();
            }
            this.SpinnerService.hide();
            return;
        }

        if (!this.stdHOFReview.valid) {
            this.showToasterError("Error", `Please Fill In The Necessary  Fields.`);
            this.SpinnerService.hide();
            return;
        }

        if (this.stdHOFReview.controls['isTcSec'].value == '') {
            this.showToasterError("Error", `Please Select Whether A TC SEC Is Available Or Not.`);
            this.SpinnerService.hide();
            return;
        }

        if (this.stdHOFReview.controls['isTcSec'].value == '1' && this.stdHOFReview.controls['tcId'].value == '') {
            this.showToasterError("Error", `Please Select A Technical Committee`);
            this.SpinnerService.hide();
            return;
        }

        if (this.stdHOFReview.controls['isTcSec'].value == '1' && this.stdHOFReview.controls['isTc'].value == '') {
            this.showToasterError("Error", `Please Select A Technical Committee Secretary`);
            this.SpinnerService.hide();
            return;
        }

        this.submitTask();
    }

    private submitTask(): void {
        this.standardDevelopmentService.reviewTask(this.stdHOFReview.value).subscribe(
            (response) => {
                this.showToasterSuccess(response.httpStatus, `Your Feedback Has Been Submitted to the TC Secretary.`);
                this.SpinnerService.hide();
                this.getOnHoldTasks();
                this.getHOFTasks();
                this.stdHOFReview.reset();
                this.hideModel();
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.SpinnerService.hide();
            }
        );
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
        event.stopPropagation();

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
                    dtInstance.destroy();
                });
        });
        setTimeout(() => {
            this.dtTrigger1.next();


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
    }

    formatFormDate(date: Date) {
        return formatDate(date, this.dateFormat, this.language);
    }

    onRadiobuttonchange($event: MatRadioChange) {
        if ($event.value === 'Reject For Review') {
            this.onDeclineApplication = true;
            this.onApproveApplication = false;

        } else if ($event.value === 'On Hold') {
            this.onApproveApplication = true;
            this.onDeclineApplication = false;

        } else {
            this.onApproveApplication = false;
            this.onDeclineApplication = false;

        }

    }

    onTcSecAvailability($event: MatRadioChange) {
        this.tcSecAvailabilty = $event.value === '1';

    }

    reloadCurrentRoute() {
        let currentUrl = this.router.url;
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
            this.router.navigate([currentUrl]);
        });
    }

    public getTechnicalCommittee(): void {
        this.standardDevelopmentService.getAllTcsForApplication().subscribe(
            (response: DataHolder[]) => {
                this.tcs = response;
                this.filteredTcs.next(this.tcs);

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
    }

    protected filteredTc = () => {
        if (!this.tcs) {
            return;
        }
        let search = this.websiteFilterCtrl.value;
        if (!search) {
            this.filteredTcs.next(this.tcs.slice());
            return;
        } else {
            search = search.toLowerCase();
        }
        this.filteredTcs.next(
            this.tcs.filter(tc => tc.tc_Title.toLowerCase().indexOf(search) > -1)
        );
    };



}


import {Component, ElementRef, Input, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Document, StandardRequestB, TaskData} from "../../../../../core/store/data/std/request_std.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {Department, UsersEntity} from "../../../../../core/store/data/std/std.model";
import {FormBuilder} from "@angular/forms";
import {StandardDevelopmentService} from "../../../../../core/store/data/std/standard-development.service";
import {NotificationService} from "../../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {CommitteeService} from "../../../../../core/store/data/std/committee.service";
import {HttpErrorResponse} from "@angular/common/http";
import {formatDate} from "@angular/common";

@Component({
    selector: 'app-rejected-for-nwi',
    templateUrl: './rejected-for-nwi.component.html',
    styleUrls: ['./rejected-for-nwi.component.css']
})
export class RejectedForNwiComponent implements OnInit {

    dtOptions: DataTables.Settings = {};
    dtTrigger1: Subject<any> = new Subject<any>();
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger2: Subject<any> = new Subject<any>();


    // data source for the radio buttons:
// selected item
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
    rejectedTasks: StandardRequestB[] = [];


    docs !: Document[];
    dtOptionsB: DataTables.Settings = {};

    public taskData: TaskData | undefined;
    public actionRequest: StandardRequestB;


    loading = false;
    loadingText: string;

    constructor(private standardDevelopmentService: StandardDevelopmentService,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService,
                private formBuilder: FormBuilder,
                private committeeService: CommitteeService,
    ) {
    }


    ngOnInit(): void {
        this.getRejectedTasks();


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

    public getRejectedTasks(): void {
        this.loading = true
        this.loadingText = "Retrieving Applications Please Wait ...."
        this.SpinnerService.show();
        this.standardDevelopmentService.getRejectedReviewsForStandards().subscribe(
            (response: StandardRequestB[]) => {
                this.rejectedTasks = response;
                this.rerender()
                this.SpinnerService.hide();
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.SpinnerService.hide();

            }
        );
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
    ngOnDestroy(): void {
        // Do not forget to unsubscribe the event
        this.dtTrigger1.unsubscribe();
        this.dtTrigger2.unsubscribe();
    }

    formatFormDate(date: Date) {
        return formatDate(date, this.dateFormat, this.language);
    }


}

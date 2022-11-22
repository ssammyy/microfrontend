import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {ApprovedNwiS, Preliminary_Draft} from "../../../../core/store/data/std/commitee-model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {Store} from "@ngrx/store";
import {ActivatedRoute, Router} from "@angular/router";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
import {
    Document,
    NwiItem,
    StandardRequestB,
    StdJustification,
    StdJustificationDecision
} from "../../../../core/store/data/std/request_std.model";
import {formatDate} from "@angular/common";
import {UserRegister} from "../../../../shared/models/user";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {MasterService} from "../../../../core/store/data/master/master.service";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";

declare const $: any;

@Component({
    selector: 'app-prepare-preliminary-draft',
    templateUrl: './prepare-preliminary-draft.component.html',
    styleUrls: ['./prepare-preliminary-draft.component.css']
})
export class PreparePreliminaryDraftComponent implements OnInit {
    fullname = '';
    title = 'toaster-not';
    preliminary_draft: Preliminary_Draft | undefined;
    submitted = false;
    public preliminary_draftFormGroup!: FormGroup;
    public approvedNwiS !: ApprovedNwiS[];
    public approvedNwiSB !: ApprovedNwiS | undefined;

    public approvedNwiSForPd !: NwiItem[];
    public approvedNwiSForPdB !: NwiItem | undefined;

    public uploadedFiles: FileList;
    public uploadedFilesB: FileList;
    public uploadedFilesC: FileList;

    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;
    isDtInitialized: boolean = false


    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    dtTrigger3: Subject<any> = new Subject<any>();
    dtTrigger4: Subject<any> = new Subject<any>();
    dtTrigger5: Subject<any> = new Subject<any>();
    dtTrigger6: Subject<any> = new Subject<any>();

    public nwiItem!: NwiItem[];
    public stdJustificationDecision!: StdJustificationDecision[];
    docs !: Document[];
    blob: Blob;

    dateFormat = "yyyy-MM-dd";
    language = "en";

    displayedColumns: string[] = ['sl', 'edition', 'title', 'spcMeetingDate', 'departmentId', 'actions'];
    dataSource!: MatTableDataSource<StdJustification>;
    dataSourceB!: MatTableDataSource<StdJustification>;
    dataSourceC!: MatTableDataSource<StdJustification>;
    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;

    public userDetails!: UserRegister;
    tasks: StandardRequestB[] = [];


    constructor(private formBuilder: FormBuilder,
                private committeeService: CommitteeService,
                private store$: Store<any>,
                private router: Router,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService,
                private route: ActivatedRoute,
                private masterService: MasterService,
                private standardDevelopmentService: StandardDevelopmentService,
    ) {
    }

    ngOnInit(): void {
        this.getNWIS();

        this.preliminary_draftFormGroup = this.formBuilder.group({
            pdName: ['', Validators.required]
        });

    }

    public getNWIS(): void {
        this.committeeService.getAllNwiSForPd().subscribe(
            (response: NwiItem[]) => {
                console.log(response);

                this.approvedNwiSForPd = response;
                this.rerender()
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );

    }

    public onOpenModal(approvedNwiS: NwiItem, mode: string): void {

        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'edit') {
            this.approvedNwiSForPdB = approvedNwiS;
            button.setAttribute('data-target', '#updateNWIModal');
        }
        if (mode === 'preparePd') {
            this.approvedNwiSForPdB = approvedNwiS;
            button.setAttribute('data-target', '#preparePd');
        }
        if (mode === 'uploadMinutes') {
            this.approvedNwiSForPdB = approvedNwiS;
            button.setAttribute('data-target', '#uploadMinutes');
        }
        if (mode === 'workPlan') {
            this.approvedNwiSForPdB = approvedNwiS;
            button.setAttribute('data-target', '#workPlan');
        }

        if (mode === 'uploadDraftsAndOtherRelevantDocuments') {
            this.approvedNwiSForPdB = approvedNwiS;
            button.setAttribute('data-target', '#uploadDraftsAndOtherRelevantDocuments');
        }

        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    public uploadMinutes(nwiID: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.committeeService.uploadMinutesForPd(nwiID, formData, "Minutes PD", "Minutes PD").subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = null;
                    console.log(data);
                    swal.fire({
                        title: 'Minutes Uploaded Successfully.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    this.hideModel()
                    this.getNWIS();

                    // this.router.navigate(['/draftStandard']);
                    // this.getSACTasks();

                },
            );
        }
    }

    public uploadDraftsAndOtherDocs(nwiID: string) {
        if (this.uploadedFilesB.length > 0) {
            const file = this.uploadedFilesB;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.committeeService.uploadDraftDocumentsForPd(nwiID, formData, "Drafts And Other Relevant Documents PD", "Drafts And Other Relevant Documents PD").subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFilesB = null;
                    console.log(data);
                    this.hideModel()
                    swal.fire({
                        title: 'Draft Documents Uploaded Successfully.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    this.getNWIS();

                    // this.router.navigate(['/draftStandard']);
                    // this.getSACTasks();

                },
            );
        }
    }


    uploadPreliminaryDraft(): void {
        if (this.uploadedFilesC != null) {
            this.SpinnerService.show();
            this.committeeService.preparePreliminaryDraft(this.preliminary_draftFormGroup.value, this.approvedNwiSB.id).subscribe(
                (response) => {
                    console.log(response)
                    this.SpinnerService.hide();
                    this.showToasterSuccess(response.httpStatus, `Successfully submitted Preliminary Draft`);
                    this.uploadPDDoc(response.body.savedRowID)
                    this.preliminary_draftFormGroup.reset();
                },
                (error: HttpErrorResponse) => {
                    this.SpinnerService.hide();
                    console.log(error.message);
                }
            );
        } else {
            this.showToasterError("Error", `Please Upload all the documents.`);
        }
    }

    public uploadPDDoc(pdID: string) {
        if (this.uploadedFilesC.length > 0) {
            const file = this.uploadedFilesC;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.committeeService.uploadPDDocument(pdID, formData, "PD Document", "PD Document").subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFilesC = null;
                    console.log(data);
                    this.hideModel()
                    swal.fire({
                        title: 'Preliminary Draft Document Uploaded Successfully.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    this.getNWIS();

                    // this.router.navigate(['/draftStandard']);
                    // this.getSACTasks();

                },
            );
        }
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

    public hideModel() {
        this.closeModal?.nativeElement.click();
    }

    @ViewChild('closeModalC') private closeModalC: ElementRef | undefined;

    public hideModelC() {
        this.closeModalC?.nativeElement.click();
    }

    public onOpenModalViewNwi(nwiId: string, mode: string): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');

        if (mode === 'edit') {
            this.getSpecificNwi(String(nwiId))
            this.getAllDocs(String(nwiId), "NWI Documents")
            button.setAttribute('data-target', '#viewNwi');
        }
        if (mode === 'viewRequest') {
            this.getSpecificStandardRequest(String(nwiId))
            this.getAllRequestDocs(String(nwiId))
            button.setAttribute('data-target', '#viewRequestModal');
        }


        // @ts-ignore
        container.appendChild(button);
        button.click();

    }


    public getSpecificNwi(nwiId: string): void {
        this.standardDevelopmentService.getNwiById(nwiId).subscribe(
            (response: NwiItem[]) => {
                this.nwiItem = response;

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
    }

    public getAllDocs(nwiId: string, processName: string): void {
        this.standardDevelopmentService.getAdditionalDocumentsByProcess(nwiId, processName).subscribe(
            (response: Document[]) => {
                this.docs = response;
                this.rerender()


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    public getSpecificStandardRequest(standardRequestId: string): void {
        this.standardDevelopmentService.getRequestById(standardRequestId).subscribe(
            (response: StandardRequestB[]) => {
                this.tasks = response;

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
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

    public getAllRequestDocs(standardId: string): void {
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


    formatFormDate(date: Date) {
        return formatDate(date, this.dateFormat, this.language);
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

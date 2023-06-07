import {Component, ElementRef, Input, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {ApprovedNwiS, Preliminary_Draft} from "../../../../core/store/data/std/commitee-model";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
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
    DataHolder,
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
import {ListItem} from "ng-multiselect-dropdown/multiselect.model";
import {IDropdownSettings} from "ng-multiselect-dropdown";

declare const $: any;

@Component({
    selector: 'app-prepare-preliminary-draft',
    templateUrl: './prepare-preliminary-draft.component.html',
    styleUrls: ['./prepare-preliminary-draft.component.css']
})
export class PreparePreliminaryDraftComponent implements OnInit {
    @Input() errorMsg: string;
    @Input() displayError: boolean;
    fullname = '';
    title = 'toaster-not';
    preliminary_draft: Preliminary_Draft | undefined;
    submitted = false;
    public preliminary_draftFormGroup!: FormGroup;
    public approvedNwiS !: ApprovedNwiS[];
    public approvedNwiSB !: ApprovedNwiS | undefined;
    public minutes_preliminary_draftFormGroup!: FormGroup;
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
    validTextType: boolean = false;
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
    public justificationItem!: StdJustification[];
    public justificationItemB !: StdJustification | undefined;
    dateFormat = "yyyy-MM-dd";
    language = "en";
    displayedColumns: string[] = ['sl', 'edition', 'title', 'spcMeetingDate', 'departmentId', 'actions', '', ''];
    dataSource!: MatTableDataSource<StdJustification>;
    dataSourceB!: MatTableDataSource<StdJustification>;
    dataSourceC!: MatTableDataSource<StdJustification>;
    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;
    public userDetails!: UserRegister;
    tasks: StandardRequestB[] = [];
    public allSdUsers !: UserRegister[];
    dropdownList: any[] = [];
    public tcs !: DataHolder[];
    public dropdownSettings: IDropdownSettings = {};
    public dropdownSettingsB: IDropdownSettings = {};
    selectedItems?: UserRegister;

    loading = false;
    loadingText: string;

    isFormSubmitted = false;


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

    isFieldValid(form: FormGroup, field: string) {
        return !form.get(field).valid && form.get(field).touched;
    }

    displayFieldCss(form: FormGroup, field: string) {
        return {
            'has-error': this.isFieldValid(form, field),
            'has-feedback': this.isFieldValid(form, field)
        };
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
        this.getNWIS();
        this.getAllSdUsers()
        this.getTechnicalCommittee()


        this.preliminary_draftFormGroup = this.formBuilder.group({
            pdName: ['', Validators.required],
            nwiId: ['', Validators.required],

        });

        this.dropdownSettings = {
            singleSelection: false,
            idField: 'id',
            textField: 'full_name',
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            allowSearchFilter: true
        };
        this.dropdownSettingsB = {
            singleSelection: true,
            idField: 'id',
            textField: 'full_name',
            allowSearchFilter: true
        };

    }

    id: any = 'Pending Review';

    tabChange(ids: any) {
        this.id = ids;
        if (this.id == "Pending Review") {
            this.reloadCurrentRoute()
        }
    }

    get formMinutesPd(): any {
        return this.minutes_preliminary_draftFormGroup.controls;
    }

    public getNWIS(): void {
        this.loading = true
        this.loadingText = "Retrieving Drafts Please Wait ...."
        this.SpinnerService.show()
        this.committeeService.getAllNwiSApprovedForPdPendingTasks().subscribe(
            (response: NwiItem[]) => {
                this.loading = false
                this.approvedNwiSForPd = response;
                this.rerender()
                this.SpinnerService.hide();

            },
            (error: HttpErrorResponse) => {
                this.loading = false
                this.SpinnerService.hide();
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
        if (mode === 'uploadMinutes') {
            this.approvedNwiSForPdB = approvedNwiS;
            this.preliminary_draftFormGroup.controls.nwiId.setValue(approvedNwiS.id);

            button.setAttribute('data-target', '#uploadMinutes');
        }
        container.appendChild(button);
        button.click();

    }

    uploadPreliminaryDraft(formDirective): void {
        this.isFormSubmitted = true;
        if (this.preliminary_draftFormGroup.valid) {
            if (this.uploadedFilesC != null && this.uploadedFilesC.length > 0) {


                this.loading = true
                this.loadingText = "Submitting  ...."
                this.SpinnerService.show();



                this.committeeService.preparePreliminaryDraft(this.preliminary_draftFormGroup.value, this.approvedNwiSForPdB.id).subscribe(
                    (response) => {
                        console.log(response)
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
                this.loading = false
                this.SpinnerService.hide();
                this.showToasterError("Error", `Please Upload The Preliminary Draft document.`);
            }
        } else {
            this.validateAllFormFields(this.preliminary_draftFormGroup);
        }
    }

    public uploadPDDoc(pdID: string) {

        //check if minutes are available
        if (this.uploadedFiles != null && this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.committeeService.uploadMinutesForPd(pdID, formData, "Minutes PD", "Minutes PD").subscribe(

            );
        }

        //check draft documents
        if (this.uploadedFilesB != null && this.uploadedFilesB.length > 0) {
            const fileDraft = this.uploadedFilesB;
            const formData = new FormData();
            for (let i = 0; i < fileDraft.length; i++) {
                console.log(fileDraft[i]);
                formData.append('docFile', fileDraft[i], fileDraft[i].name);
            }
            this.committeeService.uploadDraftDocumentsForPd(pdID, formData, "Drafts And Other Relevant Documents PD", "Drafts And Other Relevant Documents PD").subscribe(
            );
        }




        if (this.uploadedFilesC.length > 0) {
            const file = this.uploadedFilesC;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.committeeService.uploadPDDocument(pdID, formData, "PD Document", "PD Document").subscribe(
                (data: any) => {
                    this.loading = false
                    this.SpinnerService.hide();
                    this.uploadedFilesC = null;
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
        if (mode === 'justification') {
            this.getSpecificJustification(String(nwiId))
            button.setAttribute('data-target', '#justificationDecisionModal');
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

    public getSpecificJustification(nwiId: string): void {
        this.standardDevelopmentService.getJustificationByNwiId(nwiId).subscribe(
            (response: StdJustification[]) => {
                this.justificationItem = response;

                for (let product of response) {
                    this.getDecisionOnJustification(product.id)
                    this.getSelectedUser(product.tcSecretary)

                }

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

    public getDecisionOnJustification(justificationId: string): void {
        this.standardDevelopmentService.getJustificationDecisionById(justificationId).subscribe(
            (response: StdJustificationDecision[]) => {
                this.stdJustificationDecision = response;

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
    }

    textValidationType(e) {
        this.validTextType = !!e;
    }

    public getAllSdUsers(): void {
        this.committeeService.getAllSdUsers().subscribe(
            (response: UserRegister[]) => {
                this.allSdUsers = response;

                this.dropdownList = response.map((person) => {
                    return {
                        id: person.id,
                        full_name: `${person.firstName} ${person.lastName}`
                    }
                });
                this.dropdownList = response.map((person) => {
                    return {
                        id: person.id,
                        full_name: `${person.firstName} ${person.lastName}`
                    }
                });


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    public getTechnicalCommittee(): void {
        this.standardDevelopmentService.getTechnicalCommittees().subscribe(
            (response: DataHolder[]) => {
                this.tcs = response;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
    }

    onItemSelect(item: ListItem) {
        console.log(item);
    }

    onSelectAll(items: any) {
        console.log(items);
    }

    viewPdfFileById(pdfId: number, fileName: string, applicationType: string, doctype: string): void {
        this.SpinnerService.show();
        this.committeeService.viewDocsById(pdfId).subscribe(
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

    reloadCurrentRoute() {
        let currentUrl = this.router.url;
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
            this.router.navigate([currentUrl]);
        });
    }


}

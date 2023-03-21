import {
    Component,
    ElementRef,
    Input,
    OnInit,
    QueryList,
    ViewChild,
    ViewChildren,
    ViewEncapsulation
} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, Validators} from "@angular/forms";
import {
    Document,
    LiaisonOrganization,
    NwiItem,
    NWIsForVoting,
    StandardRequestB,
    Stdtsectask
} from "../../../../core/store/data/std/request_std.model";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {HttpErrorResponse} from "@angular/common/http";
import {IDropdownSettings} from 'ng-multiselect-dropdown';
import {ListItem} from "ng-multiselect-dropdown/multiselect.model";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {ErrorStateMatcher} from "@angular/material/core";
import swal from "sweetalert2";
import Swal from "sweetalert2";
import {formatDate} from "@angular/common";
import {VoteNwiRetrieved, VotesNwiTally} from "../../../../core/store/data/std/commitee-model";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {StandardsDto} from "../../../../core/store/data/master/master.model";
import {QaService} from "../../../../core/store/data/qa/qa.service";
import {MsService} from "../../../../core/store/data/ms/ms.service";
import {Department, UsersEntity} from '../../../../core/store/data/std/std.model';

export class MyErrorStateMatcher implements ErrorStateMatcher {
    isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
        const isSubmitted = form && form.submitted;
        return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
    }
}

@Component({
    selector: 'app-std-tsc-sec-tasks-component',
    templateUrl: './std-tsc-sec-tasks-component.component.html',
    styleUrls: ['../../../../../../node_modules/@ng-select/ng-select/themes/material.theme.css'],
    encapsulation: ViewEncapsulation.None,

})
export class StdTscSecTasksComponentComponent implements OnInit {
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

    docs !: Document[];
    blob: Blob;
    voteRetrieved !: VoteNwiRetrieved[];

    public actionRequest: StandardRequestB;

    dateFormat = "yyyy-MM-dd";
    language = "en";

    public itemId: string = "";
    public filePurposeAnnex: string = "FilePurposeAnnex";
    public relevantDocumentsNWI: string = "RelevantDocumentsNWI";

    public secTasks: StandardRequestB[] = [];
    public tscsecRequest !: Stdtsectask | undefined;
    public nwiItem!: NwiItem[];
    approvedNwiS: NwiItem[] = [];
    rejectedNwiS: NwiItem[] = [];
    public nwiForVotes: VotesNwiTally[] = [];

    loadedStandards: StandardsDto[] = [];
    loading = false;
    loadingText: string;

    displayedColumns: string[] = ['proposalTitle', 'scope', 'nameOfProposer', 'referenceNumber', 'actions'];
    dataSource!: MatTableDataSource<NwiItem>;
    dataSourceB!: MatTableDataSource<NwiItem>;
    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;

    @Input() errorMsg: string;
    @Input() displayError: boolean;
    stdNwiFormGroup: FormGroup;
    // public uploadedFiles: Array<File> = [];
    // public uploadedFilesB: Array<File> = [];
    public uploadedFilesC: Array<File> = [];
    validTextType: boolean = false;
    validNumberType: boolean = false;

    public nwiRequest !: StandardRequestB | undefined;

    //public stdTSecFormGroup!: FormGroup;

    public liaisonOrganizations !: LiaisonOrganization[];
    selectedDepartment: string;
    selectedStandard: number;
    public departments !: Department[];
    public tcSecs !: UsersEntity[];
    dtOptionsB: DataTables.Settings = {};
    dropdownList: any[] = [];
    selectedItems?: LiaisonOrganization;

    //selectedItems = "";

    public dropdownSettings: IDropdownSettings = {};

    constructor(
        private formBuilder: FormBuilder,
        private standardDevelopmentService: StandardDevelopmentService,
        private SpinnerService: NgxSpinnerService,
        private notifyService: NotificationService,
        private committeeService: CommitteeService,
        private msService: MsService,

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
        this.getTCSECTasks();
        this.getAllNwisUnderVote();
        this.getRejectedNwis();
        this.getApprovedNwis();
        this.loadAllStandards();

        this.getLiasisonOrganization();
        this.dropdownSettings = {
            singleSelection: false,
            idField: 'id',
            textField: 'name',
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            itemsShowLimit: 3,
            allowSearchFilter: true
        };

        this.stdNwiFormGroup = this.formBuilder.group({

            proposalTitle: ['', Validators.required],
            scope: ['', Validators.required],
            purpose: ['', Validators.required],
            targetDate: ['', Validators.required],
            similarStandards: [''],
            liaisonOrganisationData: [this.selectedItems, Validators.required],
            // draftAttached: [''],
            // outlineAttached: [''],
            // draftOutlineImpossible: [''],
            // outlineSentLater: [''],
            nameOfProposer: ['', Validators.required],
            organization: ['', Validators.required],
            circulationDate: ['', Validators.required],
            closingDate: ['', Validators.required],
            // dateOfPresentation: ['', Validators.required],
            nameOfTC: ['', Validators.required],
            referenceNumber: ['', Validators.required],
            standardId: ['', Validators.required],

        });


    }
    @ViewChild('closeModalC') private closeModalC: ElementRef | undefined;
    public hideModelC() {
        this.closeModalC?.nativeElement.click();
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
    public onOpenModalTask(task: StandardRequestB, mode: string): void {
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

    id: any = 'Prepare NWI';

    tabChange(ids: any) {
        this.id = ids;
        console.log(this.id);
    }

    get formStdRequest(): any {
        return this.stdNwiFormGroup.controls;
    }

    public getLiasisonOrganization(): void {
        this.standardDevelopmentService.getLiaisonOrganization().subscribe(
            (response: LiaisonOrganization[]) => {
                this.liaisonOrganizations = response;
                this.dropdownList = response;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    onItemSelect(item: ListItem) {
        console.log(item);
    }

    onSelectAll(items: any) {
        console.log(items);
    }

    // get formStdTSec(): any {
    //return this.stdTSecFormGroup.controls;
    // }

    public getTCSECTasks(): void {
        this.loading = true;
        this.SpinnerService.show()
        this.standardDevelopmentService.getTCSECTasks().subscribe(
            (response: StandardRequestB[]) => {
                this.secTasks = response;
                this.rerender()
                this.SpinnerService.hide()
                this.loading = false;


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.SpinnerService.hide()
                this.loading = false;


            }
        )
    }


    public getAllNwisUnderVote(): void {
        this.standardDevelopmentService.getAllVotesTally().subscribe(
            (response: VotesNwiTally[]) => {
                this.nwiForVotes = response;
                this.rerender()
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
    }

    @ViewChild('closeModal') private closeModal: ElementRef | undefined;

    public hideModel() {
        this.closeModal?.nativeElement.click();
    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)

    }


    public onUpload(secTask: Stdtsectask): void {
        this.SpinnerService.show();

        if (secTask.liaisonOrganisationData != null) {
            //console.log(JSON.stringify(secTask.liaisonOrganisationData.name));
            secTask.liaisonOrganisation = JSON.stringify(secTask.liaisonOrganisationData);
        }

        this.standardDevelopmentService.uploadNWI(secTask).subscribe(
            (response) => {
                console.log(response.body.savedRowID);
                this.showToasterSuccess(response.httpStatus, `New Work Item Uploaded`);

                // if (this.uploadedFiles.length > 0) {
                //     this.uploadDocuments(response.body.savedRowID, "Annex Documents")
                // }
                // if (this.uploadedFilesB.length > 0) {
                //     this.uploadDocuments(response.body.savedRowID
                //         , "Relevant Documents")
                // }
                if (this.uploadedFilesC.length > 0) {
                    this.uploadDocuments(response.body.savedRowID, "Reference Documents")
                } else {
                    this.hideModel();
                    this.SpinnerService.hide();
                    this.getTCSECTasks();
                    this.getApprovedNwis()
                    this.getRejectedNwis()


                }
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.SpinnerService.hide();

            }
        )
    }

    public onOpenModal(secTask: StandardRequestB, mode: string): void {


        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'edit') {
            this.stdNwiFormGroup.reset()
            // this.uploadedFiles = [];
            // this.uploadedFilesB = [];
            this.uploadedFilesC = [];
            console.log(secTask.id)
            this.itemId = String(secTask.id);
            this.nwiRequest = secTask
            button.setAttribute('data-target', '#updateNWIModal');
        }


        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    public onOpenModalVote(task: VotesNwiTally, mode: string): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');

        if (mode === 'edit') {

            this.getSpecificNwi(String(task.nwi_ID))
            this.getAllDocs(String(task.nwi_ID))

            button.setAttribute('data-target', '#viewNwi');
        }
        if (mode === 'viewVotes') {
            this.getAllVotes(task.nwi_ID)

            button.setAttribute('data-target', '#viewVotes');
        }


        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    public onOpenModalViewNwi(nwiId: string, mode: string): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');

        if (mode === 'edit') {
            this.getSpecificNwi(String(nwiId))
            this.getAllDocs(String(nwiId))
            button.setAttribute('data-target', '#viewNwi');
        }


        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    textValidationType(e) {
        this.validTextType = !!e;
    }

    numberValidationType(e) {
        this.validNumberType = !!e;
    }

    saveNwi(formDirective): void {
        if (this.stdNwiFormGroup.invalid) {
            this.stdNwiFormGroup.markAllAsTouched();
            this.validateAllFormFields(this.stdNwiFormGroup);

            return;
        } else {
            this.onUpload(this.stdNwiFormGroup.value)
            formDirective.resetForm();
            this.stdNwiFormGroup.reset()
        }
    }


    public uploadDocuments(nwiId: number, additionalInfo: string) {

        let file = null;
        // if (additionalInfo == "Annex Documents") {
        //     file = this.uploadedFiles;
        // }
        // if (additionalInfo == "Relevant Documents") {
        //     file = this.uploadedFilesB;
        // }
        if (additionalInfo == "Reference Documents") {
            file = this.uploadedFilesC;
        }
        if (file != null) {
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.standardDevelopmentService.uploadFileDetailsNwi(String(nwiId), formData, additionalInfo, additionalInfo).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();

                    // this.uploadedFiles = [];
                    // this.uploadedFilesB = [];
                    this.uploadedFilesC = [];


                    console.log(data);
                    swal.fire({
                        title: 'Uploaded Successfully.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    this.hideModel();
                    this.getTCSECTasks();

                    this.SpinnerService.hide();

                    // this.router.navigate(['/draftStandard']);
                    // this.getSACTasks();

                },
            );
        }
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

    public getAllDocs(nwiId: string): void {
        this.standardDevelopmentService.getAdditionalDocumentsByProcess(nwiId, "NWI Documents").subscribe(
            (response: Document[]) => {
                this.docs = response;
                this.rerender()


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
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

    public getApprovedNwis(): void {
        this.standardDevelopmentService.getApprovedNwiS().subscribe(
            (response: NwiItem[]) => {
                console.log(response);
                this.approvedNwiS = response;
                this.dataSourceB = new MatTableDataSource(this.approvedNwiS);
                this.dataSourceB.paginator = this.paginator;
                this.dataSourceB.sort = this.sort;


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
    }

    public getRejectedNwis(): void {
        this.standardDevelopmentService.getRejectedNwiS().subscribe(
            (response: NwiItem[]) => {
                console.log(response);
                this.rejectedNwiS = response;
                this.dataSource = new MatTableDataSource(this.rejectedNwiS);
                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
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

    private getAllVotes(nwiId: number) {
        this.standardDevelopmentService.getAllVotesOnNwi(nwiId).subscribe(
            (response: VoteNwiRetrieved[]) => {

                this.voteRetrieved = response;
                this.rerender()

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    loadAllStandards() {
        this.msService.msStandardsListDetails().subscribe(
            (standardsList: StandardsDto[]) => {
                this.loadedStandards = standardsList;
            },
            error => {
                console.log(error);
                this.msService.showError('AN ERROR OCCURRED');
            },
        );
    }

    approveNwi(nwiId: number): void {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-success'
            },
            buttonsStyling: false
        });

        swalWithBootstrapButtons.fire({
            title: 'You are about to approve/reject this New Work Item?',
            text: 'You won\'t be able to reverse this!',
            icon: 'success',
            showCancelButton: true,
            confirmButtonText: 'Approve!',
            cancelButtonText: 'Reject!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.SpinnerService.show();
                this.standardDevelopmentService.approveNwi(String(nwiId)).subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Approved!',
                            'New Work Item Successfully Approved!',
                            'success'
                        );
                        this.getAllNwisUnderVote();
                        this.SpinnerService.hide();
                        this.showToasterSuccess(response.httpStatus, 'New Work Item Successfully Approved');
                    },
                );
            } else if (
                /* Read more about handling dismissals below */
                result.dismiss === swal.DismissReason.cancel
            ) {
                this.SpinnerService.show();
                this.standardDevelopmentService.rejectNwi(String(nwiId)).subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Rejected!',
                            'New Work Item Successfully Rejected!',
                            'success'
                        );
                        this.getAllNwisUnderVote();
                        this.SpinnerService.hide();
                        this.showToasterSuccess(response.httpStatus, 'New Work Item Successfully Rejected');
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

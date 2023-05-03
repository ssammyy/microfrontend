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
import {DatePipe, formatDate} from "@angular/common";
import {VoteNwiRetrieved, VotesNwiTally} from "../../../../core/store/data/std/commitee-model";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {StandardsDto} from "../../../../core/store/data/master/master.model";
import {MsService} from "../../../../core/store/data/ms/ms.service";
import {Department, UsersEntity} from '../../../../core/store/data/std/std.model';
import {Router} from "@angular/router";

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
    private datePipe: DatePipe;

    constructor(
        private formBuilder: FormBuilder,
        private standardDevelopmentService: StandardDevelopmentService,
        private SpinnerService: NgxSpinnerService,
        private notifyService: NotificationService,
        private committeeService: CommitteeService,
        private msService: MsService,
        private router: Router,
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
            // liaisonOrganisationData: [this.selectedItems, Validators.required],
            // draftAttached: [''],
            // outlineAttached: [''],
            // draftOutlineImpossible: [''],
            // outlineSentLater: [''],
            nameOfProposer: ['', Validators.required],
            organization: ['', Validators.required],
            circulationDate: ['', Validators.required],
            closingDate: [''],
            // dateOfPresentation: ['', Validators.required],
            nameOfTC: ['', Validators.required],
            referenceNumber: ['', Validators.required],
            standardId: ['', Validators.required],

        });
        this.stdNwiFormGroup.get('circulationDate').valueChanges.subscribe(circulationDate => {
            const closingDate = new Date(circulationDate);
            closingDate.setDate(closingDate.getDate() + 30);
            const closingDateString = closingDate.toISOString().substring(0, 10);

            this.stdNwiFormGroup.patchValue({
                closingDate: closingDateString
            });
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
            this.getAllRequestDocs(String(this.actionRequest.id))
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
            this.getAllRequestDocs(String(this.actionRequest.id))
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
        if (this.id == "Prepare NWI") {
            this.reloadCurrentRoute()
        }
        // console.log(this.id);
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
        this.loading = true
        this.loadingText = "Uploading NWI"
        this.SpinnerService.show();

        // if (secTask.liaisonOrganisationData != null) {
        //     //console.log(JSON.stringify(secTask.liaisonOrganisationData.name));
        //     secTask.liaisonOrganisation = JSON.stringify(secTask.liaisonOrganisationData);
        // }

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
        });

    }

    ngOnDestroy(): void {
        // Do not forget to unsubscribe the event
        this.dtTrigger1.unsubscribe();

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


    public getAllRequestDocs(standardId: string): void {
        this.standardDevelopmentService.getAdditionalDocuments(standardId).subscribe(
            (response: Document[]) => {
                this.docs = response;
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


    reloadCurrentRoute() {
        let currentUrl = this.router.url;
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
            this.router.navigate([currentUrl]);
        });
    }


    todayDate = new DatePipe("En-US").transform(new Date(), "dd-MM-yyyy")



}

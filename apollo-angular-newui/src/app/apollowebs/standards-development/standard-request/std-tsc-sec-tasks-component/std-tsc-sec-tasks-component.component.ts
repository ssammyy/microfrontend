import {Component, ElementRef, Input, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, Validators} from "@angular/forms";
import {LiaisonOrganization, StandardRequestB, Stdtsectask} from "../../../../core/store/data/std/request_std.model";
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
import {formatDate} from "@angular/common";
import {VotesNwiTally} from "../../../../core/store/data/std/commitee-model";

export class MyErrorStateMatcher implements ErrorStateMatcher {
    isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
        const isSubmitted = form && form.submitted;
        return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
    }
}

@Component({
    selector: 'app-std-tsc-sec-tasks-component',
    templateUrl: './std-tsc-sec-tasks-component.component.html',
    styleUrls: ['./std-tsc-sec-tasks-component.component.css']
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


    dateFormat = "yyyy-MM-dd";
    language = "en";

    public itemId: string = "";
    public filePurposeAnnex: string = "FilePurposeAnnex";
    public relevantDocumentsNWI: string = "RelevantDocumentsNWI";

    public secTasks: StandardRequestB[] = [];
    public tscsecRequest !: Stdtsectask | undefined;

    public nwiForVotes: VotesNwiTally[] = [];




    @Input() errorMsg: string;
    @Input() displayError: boolean;
    stdNwiFormGroup: FormGroup;
    public uploadedFiles: FileList;
    public uploadedFilesB: FileList;
    public uploadedFilesC: FileList;
    validTextType: boolean = false;
    validNumberType: boolean = false;

    public nwiRequest !: StandardRequestB | undefined;

    //public stdTSecFormGroup!: FormGroup;

    public liaisonOrganizations !: LiaisonOrganization[];

    dropdownList: any[] = [];
    selectedItems?: LiaisonOrganization;

    //selectedItems = "";

    public dropdownSettings: IDropdownSettings = {};

    constructor(
        private formBuilder: FormBuilder,
        private standardDevelopmentService: StandardDevelopmentService,
        private SpinnerService: NgxSpinnerService,
        private notifyService: NotificationService,
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
            similarStandards: ['', Validators.required],
            liaisonOrganisationData: [this.selectedItems, Validators.required],
            draftAttached: [''],
            outlineAttached: [''],
            draftOutlineImpossible: [''],
            outlineSentLater: [''],
            nameOfProposer: ['', Validators.required],
            organization: ['', Validators.required],
            circulationDate: ['', Validators.required],
            closingDate: ['', Validators.required],
            dateOfPresentation: ['', Validators.required],
            nameOfTC: ['', Validators.required],
            referenceNumber: ['', Validators.required],
            standardId: ['', Validators.required],

        });


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
        this.standardDevelopmentService.getTCSECTasks().subscribe(
            (response: StandardRequestB[]) => {
                this.secTasks = response;
                this.rerender()
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
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

                if (this.uploadedFiles.length > 0) {
                    this.uploadDocuments(response.body.savedRowID, "Annex Documents")
                }
                if (this.uploadedFilesB.length > 0) {
                    this.uploadDocuments(response.body.savedRowID
                        , "Relevant Documents")
                }
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
            console.log(secTask.id)
            this.itemId = String(secTask.id);
            this.nwiRequest = secTask
            button.setAttribute('data-target', '#updateNWIModal');
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
        if (additionalInfo == "Annex Documents") {
            file = this.uploadedFiles;
        }
        if (additionalInfo == "Relevant Documents") {
            file = this.uploadedFilesB;
        }
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
                    this.uploadedFiles = null;
                    this.uploadedFilesB = null;
                    this.uploadedFilesC = null;

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


}

import {Component, ElementRef, Input, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import {FormationOfTcService} from "../../../../core/store/data/std/formation-of-tc.service";
import {NgxSpinnerService} from "ngx-spinner";
import {FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, Validators} from "@angular/forms";
import {MatTableDataSource} from "@angular/material/table";
import {JustificationForTc} from "../../../../core/store/data/std/formation_of_tc.model";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {ErrorStateMatcher} from "@angular/material/core";
import swal from "sweetalert2";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {Document} from "../../../../core/store/data/std/request_std.model";
import {Department} from "../../../../core/store/data/std/std.model";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {UserRegister} from "../../../../shared/models/user";
import {MasterService} from "../../../../core/store/data/master/master.service";
import {ActivatedRoute} from "@angular/router";
import {formatDate} from "@angular/common";
import {ListItem} from "ng-multiselect-dropdown/multiselect.model";
import {IDropdownSettings} from 'ng-multiselect-dropdown';
import {LoggedInUser, selectUserInfo} from "../../../../core/store";
import {Store} from "@ngrx/store";

interface FormField {
    id: string;
    label: string;
    control: FormControl;
}

export class MyErrorStateMatcher implements ErrorStateMatcher {
    isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
        const isSubmitted = form && form.submitted;
        return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
    }
}

@Component({
    selector: 'app-request-for-formation-of-tc',
    templateUrl: './request-for-formation-of-tc.component.html',
    styleUrls: ['./request-for-formation-of-tc.component.css']
})
export class RequestForFormationOfTCComponent implements OnInit {
    @Input() errorMsg: string;
    @Input() displayError: boolean;

    public itemId: string = "1";
    public type: string = "TCRelevantDocument";

    public dropdownSettings: IDropdownSettings = {};
    dropdownList: any[] = ['Government Lead Agency/Regulatory Authority', 'Manufacturers, producers or service providers',
        'Major corporate consumers', 'University, Research and other Technical Institutions', 'Industry Association', 'Trade Association',
        'Professional Body', 'Consumer Organization', 'Non-Governmental Organization (NGO)', 'Renown Professionals/experts',
        'Renown Professionals/experts', 'Small and Medium Enterprises (SMEs)', 'SME trade associations '];

    options = [
        {value: 'Government Lead Agency/Regulatory Authority', label: 'Government Lead Agency/Regulatory Authority'},
        {
            value: 'Manufacturers, producers or service providers',
            label: 'Manufacturers, producers or service providers'
        },
        {
            value: 'University, Research and other Technical Institutions',
            label: 'University, Research and other Technical Institutions'
        },
        {value: 'Professional Body', label: 'Professional Body'},
        {value: 'Consumer Organization', label: 'Consumer Organization'},
        {value: 'Non-Governmental Organization (NGO)', label: 'Non-Governmental Organization (NGO)'},
        {value: 'Renown Professionals/experts', label: 'Renown Professionals/experts'},
        {value: 'Small and Medium Enterprises (SMEs)', label: 'Small and Medium Enterprises (SMEs)'},
        {value: 'SME trade associations', label: 'SME trade associations'},
        {value: 'Major corporate consumers', label: 'Major corporate consumers'}
    ];

    selectedOptions: string[] = [];

    formFields: FormField[] = [];



    tasks: JustificationForTc[] = [];
  public uploadedFiles: Array<File> = [];

    emailFormControl = new FormControl('', [
        Validators.required,
        Validators.email,
    ]);

    validEmailRegister: boolean = false;

    validTextType: boolean = false;
    validNumberType: boolean = false;
    pattern = "https?://.+";
    public userDetails!: UserRegister;
    public sacDetails!: UserRegister;
    public spcDetails!: UserRegister;


    matcher = new MyErrorStateMatcher();
    register: FormGroup;
    formationRequestFormGroup: FormGroup;
    editFormationRequestFormGroup: FormGroup;


    isFormSubmitted = false;
    loading = false;
    loadingText: string;
    proposalRetrieved: JustificationForTc;


    dtOptions: DataTables.Settings = {};
    dtOptionsB: DataTables.Settings = {};

    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger: Subject<any> = new Subject<any>();
    dtTrigger4: Subject<any> = new Subject<any>();

    docs !: Document[];
    blob: Blob;

    selectedDepartment: string;

    public departments !: Department[];
    public departmentSelected !: Department[];

    dateFormat = "yyyy-MM-dd  hh:mm";
    language = "en";

    roles: string[];
    userLoggedInID: number;
    userProfile: LoggedInUser;



    constructor(private formationOfTcService: FormationOfTcService,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService,
                private formBuilder: FormBuilder,
                private store$: Store<any>,
                private standardDevelopmentService: StandardDevelopmentService,
                private masterService: MasterService,
                private route: ActivatedRoute,
    ) {


    }

    id: any = 'New Proposals';

    tabChange(ids: any) {
        this.id = ids;
        if (this.id == "New Proposals") {
            this.standardDevelopmentService.reloadCurrentRoute()
        }
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

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)
    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)
    }


    ngOnInit(): void {
        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            this.userLoggedInID = u.id;
            this.userProfile = u;
            return this.roles = u.roles;
        });

        this.getAllHofJustifications(true);
        this.getDepartments();

        this.dropdownSettings = {
            singleSelection: false,
            idField: 'id',
            textField: 'name',
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            itemsShowLimit: 3,
            allowSearchFilter: true
        };

        this.formationRequestFormGroup = this.formBuilder.group({
            nameOfTC: ['', Validators.required],
            scope: ['', Validators.required],
            purpose: ['', Validators.required],
            proposedRepresentationArray: [[], Validators.minLength(3)],
            // organization: ['', [Validators.required,Validators.pattern(/^([^,]+,){5,}[^,]+$/)]],
            proposedRepresentation: [''],
            departmentId: ['', Validators.required],
            isoCommittee: [''],


        });

        this.editFormationRequestFormGroup = this.formBuilder.group({
            dateOfPresentation: ['', Validators.required],
            userDetails: ['', Validators.required],
            referenceNumber: ['', Validators.required],
            nameOfTC: ['', Validators.required],
            scope: ['', Validators.required],
            purpose: ['', Validators.required],
            proposedRepresentationArray: [[], Validators.minLength(3)],
            proposedRepresentation: [''],
            departmentId: ['', Validators.required],
            isoCommittee: [''],
            id: ['', Validators.required],
            departmentIdOriginal: ['', Validators.required],


        });

    }


    onItemSelect(item: ListItem) {
        // console.log(item);
    }


    onSelectAll(items: any) {
        // console.log(items);
    }

    onSelectItem(item: ListItem) {
        // console.log(item);
    }


    public uploadProposalForTC(formDirective): void {
        this.isFormSubmitted = true;
        if (this.formationRequestFormGroup.valid) {
            this.loadingText = "Saving Please Wait ...."

            this.loading = true;
            this.SpinnerService.show();
            const arrayTest = this.formationRequestFormGroup.controls['proposedRepresentationArray'].value;
            const myVar1 = arrayTest.toString()
            const formValues = {};
            let numValues = 0;
            for (const field of this.formFields) {
                if (field.control.value?.trim()) {
                    const value = field.control.value.trim();
                    if (value) {
                        numValues += value.split(',').length;
                        formValues[field.id] = value;
                    }
                }
                else {
                    this.showToasterError("Error", "Please enter proposed representations organisations")
                    this.isFormSubmitted = false;
                    this.loading = false;
                    this.SpinnerService.hide();
                    return;
                }
            }
            if (numValues < 6) {
                this.showToasterError("Error", "Please enter at least six proposed representations organisations")
                this.isFormSubmitted = false;
                this.loading = false;
                this.SpinnerService.hide();
                return;
            }
            let jsonString = JSON.stringify(formValues);
            let cleanedJsonString = jsonString.replace(/\\/g, '').replace(/^{/, '').replace(/}$/, '');


            this.formationRequestFormGroup.controls['proposedRepresentation'].setValue(cleanedJsonString);
            this.formationOfTcService.uploadProposalForTC(this.formationRequestFormGroup.value).subscribe(
                (response) => {
                    this.showToasterSuccess("Success", "Successfully submitted proposal for formation of TC")
                    if (this.uploadedFiles != null && this.uploadedFiles.length > 0) {
                        this.onClickSaveUploads(response.body.savedRowID, this.formationRequestFormGroup.get("nameOfTC").value.toString())
                        formDirective.resetForm();
                        this.formationRequestFormGroup.reset()
                        this.isFormSubmitted = false;
                        this.formFields = []


                    } else {
                        this.SpinnerService.hide();
                        formDirective.resetForm();
                        this.isFormSubmitted = false;
                        this.formationRequestFormGroup.reset()
                        this.formFields = []

                        swal.fire({
                            title: 'Your Proposal Has Been Submitted.',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        });
                        this.getAllHofJustifications(false);
                        this.hideModel()
                    }
                },
                (error: HttpErrorResponse) => {
                    alert(error.message);
                }
            )
        }
    }
    public editProposalForTC(formDirective): void {
        this.isFormSubmitted = true;


        if (this.editFormationRequestFormGroup.valid) {
            this.loadingText = "Resubmitting Please Wait ...."

            this.loading = true;
            this.SpinnerService.show();
            const departmentIdOriginal = this.editFormationRequestFormGroup.controls['departmentIdOriginal'].value;
            const departmentIdNew = this.editFormationRequestFormGroup.controls['departmentId'].value;
            if (departmentIdOriginal != departmentIdNew.id) {
                let str = this.editFormationRequestFormGroup.controls['referenceNumber'].value;
                str = str.replace(/^[^/]+/, departmentIdNew.name);
                this.editFormationRequestFormGroup.controls['referenceNumber'].setValue(str);
                this.editFormationRequestFormGroup.controls['departmentId'].setValue(departmentIdNew.id);
            }
            this.formationOfTcService.editProposalForTC(this.editFormationRequestFormGroup.value).subscribe(
                (response) => {
                    this.showToasterSuccess("Success", "Successfully edited proposal for formation of TC")
                    if (this.uploadedFiles != null && this.uploadedFiles.length > 0) {
                        this.onClickSaveUploads(response.body.savedRowID, this.editFormationRequestFormGroup.get("nameOfTC").value.toString())
                        formDirective.resetForm();
                        this.editFormationRequestFormGroup.reset()
                        this.isFormSubmitted = false;

                    } else {
                        this.SpinnerService.hide();
                        formDirective.resetForm();
                        this.isFormSubmitted = false;
                        this.editFormationRequestFormGroup.reset()
                        swal.fire({
                            title: 'Your Proposal Has Been Resubmitted.',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        });
                        this.getAllHofJustifications(false);
                        this.hideModelB()
                    }
                },
                (error: HttpErrorResponse) => {
                    alert(error.message);
                }
            )
        }
    }


    public getAllHofJustifications(pageRefresh: boolean): void {

        this.loadingText = "Retrieving Please Wait ...."

        this.loading = pageRefresh;
        this.SpinnerService.show()
        this.formationOfTcService.getAllHofJustifications().subscribe(
            (response: JustificationForTc[]) => {
                this.tasks = response;
                this.SpinnerService.hide()
                this.rerender()

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.SpinnerService.hide()

            }
        );
    }




    get formFormationRequest(): any {
        return this.formationRequestFormGroup.controls;
    }

    get formEditFormationRequest(): any {
        return this.editFormationRequestFormGroup.controls;
    }



    public openModal(): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        button.setAttribute('data-target', '#requestModal');

        this.formationRequestFormGroup.controls['proposedRepresentationArray'].valueChanges.subscribe((selectedOptionsB: string[]) => {
            this.formFields = selectedOptionsB?.map(option => ({
                id: option,
                label: ` ${option}`,
                control: new FormControl()
            }));
        });

        // @ts-ignore
        container.appendChild(button);
        button.click();
    }

    @ViewChild('closeModal') private closeModal: ElementRef | undefined;

    public hideModel() {
        this.closeModal?.nativeElement.click();
    }

    @ViewChild('closeModalB') private closeModalB: ElementRef | undefined;

    public hideModelB() {
        this.closeModalB?.nativeElement.click();
    }

    @ViewChild('closeModalC') private closeModalC: ElementRef | undefined;

    public hideModelC() {
        this.closeModalC?.nativeElement.click();
    }

    textValidationType(e) {
        this.validTextType = !!e;
    }


    onClickSaveUploads(proposalId: string, nameOfTc: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                formData.append('docFile', file[i], file[i].name);
            }
            this.loadingText = "Saving Please Wait ...."

            this.SpinnerService.show();
            this.formationOfTcService.uploadAdditionalDocuments(proposalId, formData, "AdditionalInformation", nameOfTc).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = [];
                    this.getAllHofJustifications(false);
                    this.hideModel()

                    swal.fire({
                        title: 'Your Proposal Has Been Submitted.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                },
            );
        }

    }


    public openModalToView(proposal: JustificationForTc): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        this.proposalRetrieved = proposal;

        button.setAttribute('data-toggle', 'modal');
        button.setAttribute('data-target', '#editModal');

        this.getDepartmentName(String(this.proposalRetrieved.departmentId))
        this.getSelectedUser(proposal.createdBy)

        this.selectedOptions = proposal.proposedRepresentation.split(',').slice(0, 3);
        this.editFormationRequestFormGroup.controls['departmentId'].setValue(this.proposalRetrieved.departmentId);
        // if(proposal.hofId!="null") {
        //
        //     this.getSelectedUser(proposal.hofId)
        // }

        // this.getSelectedSpc(proposal.spcId)
        if (proposal.status == "7" || proposal.status == "6") {
            this.getSelectedSac(proposal.sacId)

        }

        // @ts-ignore
        container.appendChild(button);
        button.click();
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
            this.dtTrigger.next();
            this.dtTrigger4.next();
        });

    }

    public getAllDocs(proposalId: string): void {
        this.formationOfTcService.getAdditionalDocuments(proposalId).subscribe(
            (response: Document[]) => {
                this.docs = response;
                this.rerender()


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    public getDepartmentName(proposalId: string): void {
        this.standardDevelopmentService.getDepartmentById(proposalId).subscribe(
            (response: Department[]) => {
                this.departmentSelected = response;
                for (let h = 0; h < response.length; h++) {
                    this.selectedDepartment = response[h].name;
                }


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }


    viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.formationOfTcService.viewDocsById(pdfId).subscribe(
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


    private getSelectedUser(userId) {
        this.route.fragment.subscribe(params => {
            this.masterService.loadUserDetails(userId).subscribe(
                (data: UserRegister) => {
                    this.userDetails = data;
                }
            );

        });
    }

    private getSelectedSpc(userId) {
        this.route.fragment.subscribe(params => {
            this.masterService.loadUserDetails(userId).subscribe(
                (data: UserRegister) => {
                    this.spcDetails = data;
                }
            );

        });
    }

    private getSelectedSac(userId) {
        this.route.fragment.subscribe(params => {
            this.masterService.loadUserDetails(userId).subscribe(
                (data: UserRegister) => {
                    this.sacDetails = data;
                }
            );

        });
    }

    formatFormDate(date: Date) {
        return formatDate(date, this.dateFormat, this.language);
    }

    minTextsSeparatedByCommaValidator(control: FormControl): { [s: string]: boolean } {
        const value: string = control.value;
        const texts: string[] = value ? value.split(',') : [];

        if (texts.length < 6) {
            return {minTextsSeparatedByComma: true};
        }

        return null;
    }


}

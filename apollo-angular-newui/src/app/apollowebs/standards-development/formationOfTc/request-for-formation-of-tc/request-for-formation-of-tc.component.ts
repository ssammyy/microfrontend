import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
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
    public groupId: string = "draft";
    public type: string = "TCRelevantDocument";

    tasks: JustificationForTc[] = [];
    displayedColumns: string[] = ['subject', 'proposer', 'purpose', 'nameOfTC', 'status', 'actions'];
    displayedColumn: string[] = ['subject', 'proposer', 'purpose', 'nameOfTC', 'status', 'actions'];

    dataSource!: MatTableDataSource<JustificationForTc>;
    dataSourceB!: MatTableDataSource<JustificationForTc>;
    dataSourceC!: MatTableDataSource<JustificationForTc>;

    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;

    public uploadedFiles: Array<File> = [];

    emailFormControl = new FormControl('', [
        Validators.required,
        Validators.email,
    ]);

    validEmailRegister: boolean = false;

    validTextType: boolean = false;
    validNumberType: boolean = false;
    pattern = "https?://.+";


    matcher = new MyErrorStateMatcher();
    register: FormGroup;
    formationRequestFormGroup: FormGroup;


    isFormSubmitted = false;


    constructor(private formationOfTcService: FormationOfTcService,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService,
                private formBuilder: FormBuilder,
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

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)
    }


    ngOnInit(): void {

        this.getAllHofJustifications();
        this.getAllHofJustificationsApproved()
        this.getAllHofJustificationsRejected()

        this.formationRequestFormGroup = this.formBuilder.group({
            dateOfPresentation: ['', Validators.required],
            nameOfTC: ['', Validators.required],
            proposer: ['', Validators.required],
            referenceNumber: ['', Validators.required],
            subject: ['', Validators.required],
            scope: ['', Validators.required],
            purpose: ['', Validators.required],
            proposedRepresentation: ['', Validators.required],
            targetDate: ['', Validators.required],
            programmeOfWork: ['', Validators.required],
            liaisonOrganization: ['', Validators.required],
            organization: ['', Validators.required],
        });
    }

    public uploadProposalForTC(formDirective): void {
        this.isFormSubmitted = true;

        if (this.formationRequestFormGroup.valid) {
            this.SpinnerService.show();


            this.formationOfTcService.uploadProposalForTC(this.formationRequestFormGroup.value).subscribe(
                (response) => {
                    this.showToasterSuccess("Success", "Successfully submitted proposal for formation of TC")
                    if (this.uploadedFiles != null && this.uploadedFiles.length > 0) {
                        this.onClickSaveUploads(response.body.savedRowID, this.formationRequestFormGroup.get("nameOfTC").value.toString())
                        formDirective.resetForm();
                        this.formationRequestFormGroup.reset()
                        this.isFormSubmitted = false;


                    } else {
                        this.SpinnerService.hide();
                        formDirective.resetForm();
                        this.isFormSubmitted = false;
                        this.formationRequestFormGroup.reset()
                        swal.fire({
                            title: 'Your Proposal Has Been Submitted.',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        });
                        this.getAllHofJustifications();
                        this.getAllHofJustificationsApproved()
                        this.getAllHofJustificationsRejected()
                        this.hideModel()
                    }
                },
                (error: HttpErrorResponse) => {
                    alert(error.message);
                }
            )
        }
    }

    public getAllHofJustifications(): void {
        this.SpinnerService.show()
        this.formationOfTcService.getAllHofJustifications().subscribe(
            (response: JustificationForTc[]) => {
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

    public getAllHofJustificationsApproved(): void {
        this.SpinnerService.show()
        this.formationOfTcService.getAllSpcJustifications().subscribe(
            (response: JustificationForTc[]) => {
                this.tasks = response;
                this.SpinnerService.hide()
                this.dataSourceB = new MatTableDataSource(this.tasks);

                this.dataSourceB.paginator = this.paginator;
                this.dataSourceB.sort = this.sort;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.SpinnerService.hide()

            }
        );
    }


    public getAllHofJustificationsRejected(): void {
        this.SpinnerService.show()
        this.formationOfTcService.getAllJustificationsRejectedBySpc().subscribe(
            (response: JustificationForTc[]) => {
                this.tasks = response;
                this.SpinnerService.hide()
                this.dataSourceC = new MatTableDataSource(this.tasks);

                this.dataSourceC.paginator = this.paginator;
                this.dataSourceC.sort = this.sort;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.SpinnerService.hide()

            }
        );
    }


    applyFilter(event: Event) {
        const filterValue = (event.target as HTMLInputElement).value;
        this.dataSource.filter = filterValue.trim().toLowerCase();

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

    public openModal(): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        button.setAttribute('data-target', '#requestModal');

        // @ts-ignore
        container.appendChild(button);
        button.click();
    }

    @ViewChild('closeModal') private closeModal: ElementRef | undefined;
    public hideModel() {
        this.closeModal?.nativeElement.click();
    }

    textValidationType(e) {
        this.validTextType = !!e;
    }


    onClickSaveUploads(proposalId: string, nameOfTc: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.formationOfTcService.uploadAdditionalDocuments(proposalId, formData, "AdditionalInformation", nameOfTc).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = [];
                    this.getAllHofJustifications();
                    this.getAllHofJustificationsApproved()
                    this.getAllHofJustificationsRejected()
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


}

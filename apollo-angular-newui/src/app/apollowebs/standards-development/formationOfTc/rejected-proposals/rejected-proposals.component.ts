import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {JustificationForTc} from "../../../../core/store/data/std/formation_of_tc.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {Department} from "../../../../core/store/data/std/std.model";
import {UserRegister} from "../../../../shared/models/user";
import {FormationOfTcService} from "../../../../core/store/data/std/formation-of-tc.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {MasterService} from "../../../../core/store/data/master/master.service";
import {ActivatedRoute} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";
import {formatDate} from "@angular/common";
import swal from "sweetalert2";
import {ErrorStateMatcher} from "@angular/material/core";
import {LoggedInUser, selectUserInfo} from "../../../../core/store";

export class MyErrorStateMatcher implements ErrorStateMatcher {
    isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
        const isSubmitted = form && form.submitted;
        return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
    }
}

@Component({
    selector: 'app-rejected-proposals',
    templateUrl: './rejected-proposals.component.html',
    styleUrls: ['./rejected-proposals.component.css']
})
export class RejectedProposalsComponent implements OnInit {
    tasks: JustificationForTc[] = [];
    dtOptions: DataTables.Settings = {};
    dtOptionsB: DataTables.Settings = {};

    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger: Subject<any> = new Subject<any>();
    dtTrigger4: Subject<any> = new Subject<any>();
    proposalRetrieved: JustificationForTc;

    public departments !: Department[];
    public departmentSelected !: Department[];

    dateFormat = "yyyy-MM-dd  hh:mm";
    language = "en";
    public userDetails!: UserRegister;
    public sacDetails!: UserRegister;
    public spcDetails!: UserRegister;

    selectedDepartment: string;

    selectedOptions: string[] = [];

    editFormationRequestFormGroup: FormGroup;

    isFormSubmitted = false;

    loading = false;
    loadingText: string;

    validTextType: boolean = false;

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

    ngOnInit(): void {
        this.getDepartments()

        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            this.userLoggedInID = u.id;
            this.userProfile = u;
            return this.roles = u.roles;
        });
        this.getAllHofJustificationsRejected();
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

    public getAllHofJustificationsRejected(): void {
        this.SpinnerService.show()
        this.formationOfTcService.getAllRejectedJustifications().subscribe(
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

    public openModalToView(proposal: JustificationForTc): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        this.proposalRetrieved = proposal;

        button.setAttribute('data-toggle', 'modal');
        button.setAttribute('data-target', '#editModal');

        this.getDepartmentName(String(this.proposalRetrieved.departmentId))
        this.getSelectedUser(proposal.proposer)

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

    formatFormDate(date: Date) {
        return formatDate(date, this.dateFormat, this.language);
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
                    this.getAllHofJustificationsRejected();
                    this.hideModelB()

                },
                (error: HttpErrorResponse) => {
                    alert(error.message);
                }
            )
        }
    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)
    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)
    }

    @ViewChild('closeModalB') private closeModalB: ElementRef | undefined;

    public hideModelB() {
        this.closeModalB?.nativeElement.click();
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


    get formEditFormationRequest(): any {
        return this.editFormationRequestFormGroup.controls;
    }

    textValidationType(e) {
        this.validTextType = !!e;
    }

}

import {Component, OnInit} from '@angular/core';
import {Observable, of} from 'rxjs';
import {
    Back,
    Branches,
    BranchesService,
    Company,
    loadResponsesFailure,
    loadResponsesSuccess, selectBranchData, selectBranchIdData, selectCompanyIdData, User,
    UsersService
} from '../../../../core/store';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Store} from '@ngrx/store';
import {ConfirmedValidator} from '../../../../core/shared/confirmed.validator';
import {catchError} from 'rxjs/operators';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
    selector: 'app-add-user',
    templateUrl: './add-user.component.html',
    styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit {
    users$: Observable<User[]>;
    branches$: Observable<Branches[]>;

    stepOneForm: FormGroup = new FormGroup({});

    // branchSoFar: Partial<User> | undefined;
    // @ts-ignore
    user: User;
    branch: Branches;
    company$: Company | undefined;

    selectedCompany = -1;
    selectedBranch = -1;
    selectedBranches$: Branches | undefined;
    step = 1;


    constructor(
        private service: UsersService,
        private store$: Store<any>,
        private formBuilder: FormBuilder,
        private branchservice: BranchesService,
    ) {

        this.branches$ = branchservice.entities$;
        branchservice.getAll().subscribe();


    }

    ngOnInit(): void {
        this.stepOneForm = this.formBuilder.group({
                firstName: ['', Validators.required],
                lastName: ['', Validators.required],
                userName: ['', Validators.required],
                email: ['', Validators.required],
                cellphone: ['', Validators.required],
                // otp: new FormControl('', ),
                credentials: ['', Validators.required],
                confirmCredentials: ['', [Validators.required]]
            },
            {
                validators: ConfirmedValidator('credentials', 'confirmCredentials')
            });

        this.store$.select(selectCompanyIdData).subscribe((d) => {
            return this.selectedCompany = d;
        });
        this.store$.select(selectBranchIdData).subscribe((d) => {
            return this.selectedBranch = d;
        });
        this.store$.select(selectBranchData).subscribe((d) => {
            return this.selectedBranches$ = d;
        });


    }


    onClickSave(valid: boolean) {
        if (valid) {
            if (
                this.user === undefined ||
                this.user.id === null ||
                this.user.id === undefined
            ) {
                this.user = this.stepOneForm.value;
                this.user.companyId = this.selectedCompany;
                this.user.plantId = this.selectedBranch;
                this.service.add(this.user).subscribe(
                    (a) => {
                        this.stepOneForm.markAsPristine();
                        this.stepOneForm.reset();
                        this.store$.dispatch(loadResponsesSuccess({
                            message: {
                                response: '00',
                                payload: `Successfully saved ${a.userName}`,
                                status: 200
                            }
                        }));
                    },
                    catchError(
                        (err: HttpErrorResponse) => {
                            return of(loadResponsesFailure({
                                error: {
                                    payload: err.error,
                                    status: err.status,
                                    response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                                }
                            }));
                        }));
            } else {
                this.user = {...this.user, ...this.stepOneForm.value};
                this.service.update(this.user).subscribe(
                    (a) => {
                        this.stepOneForm.markAsPristine();
                        this.stepOneForm.reset();
                        this.store$.dispatch(loadResponsesSuccess({
                            message: {
                                response: '00',
                                payload: `Successfully saved ${a.userName}`,
                                status: 200
                            }
                        }));
                    },
                    catchError(
                        (err: HttpErrorResponse) => {
                            return of(loadResponsesFailure({
                                error: {
                                    payload: err.error,
                                    status: err.status,
                                    response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                                }
                            }));
                        }));
            }


        } else {
            this.store$.dispatch(loadResponsesFailure({
                error: {
                    payload: 'Some required details are missing, kindly recheck',
                    status: 100,
                    response: '05'
                }
            }));
        }


    }

    public goBack(): void {
        this.store$.dispatch(Back());
    }

}

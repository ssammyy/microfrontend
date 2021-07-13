import {Component, OnInit} from '@angular/core';
import {Observable, of} from 'rxjs';
import {
    Back,
    Branches, Go, loadResponsesFailure, loadResponsesSuccess, loadUserId,
    selectBranchData,
    selectBranchIdData,
    selectCompanyIdData, selectUserData,
    User,
    UsersService
} from '../../../../core/store';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Store} from '@ngrx/store';
import {ConfirmedValidator} from '../../../../core/shared/confirmed.validator';
import {catchError} from 'rxjs/operators';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
    selector: 'app-user',
    templateUrl: './user.component.html',
    styleUrls: ['../../company.component.css'
    ]
})
export class UserComponent implements OnInit {

    users$: Observable<User[]>;

    stepOneForm: FormGroup = new FormGroup({});

    // branchSoFar: Partial<User> | undefined;
    // @ts-ignore
    user: User;

    selectedCompany = -1;
    selectedBranch = -1;
    selectedBranches$: Branches | undefined;
    step = 1;


    constructor(
        private service: UsersService,
        private store$: Store<any>,
        private formBuilder: FormBuilder,
    ) {
        this.users$ = service.entities$;
        service.getAll().subscribe();

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
        this.store$.select(selectUserData).subscribe((d) => {
            this.stepOneForm.patchValue(d);
            return this.user = d;
        });
    }

    editRecord(record: User) {

        this.store$.dispatch(loadUserId({payload: record.id, user: record}));
        this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'dashboard/companies/branches/users'}));

    }

    onClickSave(valid: boolean) {
        if (valid) {
            if (this.user.id === null || this.user.id === undefined) {
                this.user = this.stepOneForm.value;
                this.user.companyId = this.selectedCompany;
                this.user.plantId = this.selectedBranch;
                this.service.add(this.user).subscribe(
                    (a) => {
                        this.stepOneForm.markAsPristine();
                        this.stepOneForm.reset();
                        return this.store$.dispatch(loadResponsesSuccess({
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
                        return this.store$.dispatch(loadResponsesSuccess({
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

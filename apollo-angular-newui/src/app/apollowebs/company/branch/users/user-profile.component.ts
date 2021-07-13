import {Component, OnInit} from '@angular/core';
import {loadResponsesFailure, selectUserInfo, UserEntityDto, UserEntityService} from '../../../../core/store';
import {Store} from '@ngrx/store';
import {catchError} from 'rxjs/operators';
import {HttpErrorResponse} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {Titles, TitlesService} from '../../../../core/store/data/title';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ConfirmedValidator} from '../../../../core/shared/confirmed.validator';

@Component({
    selector: 'app-user-profile',
    templateUrl: './user-profile.component.html',
    styles: []
})
export class UserProfileComponent implements OnInit {
    user: UserEntityDto;
    title$: Observable<Titles[]>;
    stepOneForm: FormGroup = new FormGroup({});

    constructor(
        private store$: Store<any>,
        private service: UserEntityService,
        private titleService: TitlesService,
        private formBuilder: FormBuilder,
    ) {
        this.title$ = titleService.entities$;
        titleService.getAll().subscribe();
    }

    ngOnInit(): void {
        this.stepOneForm = this.formBuilder.group({
                id: [''],
                firstName: ['', Validators.required],
                lastName: ['', Validators.required],
                userName: ['', Validators.required],
                userPinIdNumber: ['', Validators.required],
                personalContactNumber: ['', Validators.required],
                email: ['', Validators.required],
                userRegNo: ['', Validators.required],
                enabled: ['', Validators.required],
                accountExpired: ['', Validators.required],
                accountLocked: ['', Validators.required],
                credentialsExpired: ['', Validators.required],
                status: ['', Validators.required],
                registrationDate: ['', Validators.required],
                title: ['', Validators.required],
            },
            {
                validators: ConfirmedValidator('credentials', 'confirmCredentials')
            });
        this.store$.select(selectUserInfo).subscribe(
            (l) => {
                return this.service.getByKey(l.id).subscribe(
                    (u) => {
                        return this.user = u;
                    }, catchError((err: HttpErrorResponse) => {
                        console.log((err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`);
                        return of(loadResponsesFailure({
                            error: {
                                payload: 'Your request could not be processed at the moment, try again later.',
                                status: err.status,
                                response: '99'
                            }
                        }));
                    })
                );
            }
        );
        this.stepOneForm.patchValue(this.user);
    }

    onClickSave(valid: boolean) {
        if (valid) {
            this.user = {...this.user, ...this.stepOneForm.value};
            this.service.update(this.user);

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

}

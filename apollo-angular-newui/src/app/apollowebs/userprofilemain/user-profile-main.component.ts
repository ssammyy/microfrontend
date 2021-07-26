import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {
    Go,
    loadResponsesFailure,
    loadResponsesSuccess,
    selectUserInfo,
    UserEntityDto,
    UserEntityService
} from '../../core/store';
import {Observable, of} from 'rxjs';
import {Titles, TitlesService} from '../../core/store/data/title';
import {Store} from '@ngrx/store';
import {catchError} from 'rxjs/operators';
import {HttpErrorResponse} from '@angular/common/http';


@Component({
    selector: 'app-userprofilemain',
    templateUrl: './user-profile-main.component.html',
    styleUrls: ['./user-profile-main.component.css']
})


export class UserProfileMainComponent implements OnInit {

    user: UserEntityDto;
    title$: Observable<Titles[]>;
    stepOneForm: FormGroup = new FormGroup({});


    constructor(private store$: Store<any>,
                private service: UserEntityService,
                private titleService: TitlesService,
                private formBuilder: FormBuilder) {
        this.title$ = titleService.entities$;
        titleService.getAll().subscribe();
    }

    ngOnInit(): void {


        this.stepOneForm = this.formBuilder.group({
                id: [''],
                firstName: ['', Validators.required],
                lastName: ['', Validators.required],
                userName: ['', Validators.required],
                personalContactNumber: ['', Validators.required],
                email: ['', Validators.required],
                userRegNo: [''],
                enabled: [''],
                accountExpired: [''],
                accountLocked: [''],
                credentialsExpired: [''],
                status: [''],
                registrationDate: [''],
                title: [''],
            },
            {
                // validators: ConfirmedValidator('credentials', 'confirmCredentials')
            });
        this.store$.select(selectUserInfo).subscribe(
            (l) => {
                console.log(`The id is ${l.id}`);
                return this.service.getByKey(`${l.id}/`).subscribe(
                    (u) => {
                        console.log(`The id is ${l.id} vs ${u.id}`);
                        this.stepOneForm.patchValue(u);
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


    }

    onClickSave(valid: boolean) {
        if (valid) {


            this.user = {...this.user, ...this.stepOneForm.value};


            this.service.update(this.user).subscribe(
                (a) => {

                    this.store$.dispatch(
                        loadResponsesSuccess({
                            message: {
                                response: '00',
                                payload: `Profile Successfully Updated.`,
                                status: 200
                            }
                        })
                    );
                    return this.store$.dispatch(Go({
                        payload: null,
                        link: 'profile',
                        redirectUrl: 'profile'
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

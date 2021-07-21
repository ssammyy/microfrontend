import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {Observable, of} from 'rxjs';
import {
    loadBrsValidations, loadBrsValidationsFailure,
    loadBrsValidationsSuccess,
    loadRegistrations, loadRegistrationsFailure,
    loadRegistrationsSuccess,
    loadSendTokenToPhone, loadSendTokenToPhoneFailure,
    loadSendTokenToPhoneSuccess,
    loadValidateTokenAndPhone, loadValidateTokenAndPhoneFailure,
    loadValidateTokenAndPhoneSuccess
} from './registration.actions';
import {catchError, mergeMap, switchMap} from 'rxjs/operators';
import {RegistrationService} from './registration.service';
import {loadResponsesSuccess} from '../../response';
import {HttpErrorResponse} from '@angular/common/http';
import {ApiResponse} from '../../../../domain/response.model';


@Injectable()
export class RegistrationEffects {


    doBrsValidation: Observable<Action> = createEffect(
        () =>
            this.actions$.pipe(
                ofType(loadBrsValidations),
                switchMap((action) => {
                        loadBrsValidationsFailure({error: {response: '', payload: null, status: 400}, data: null, step: 0});
                        return this.service.brsValidation(action.payload)
                            .pipe(
                                mergeMap((data) => {
                                    if (data.status) {
                                        return [
                                            loadBrsValidationsSuccess({data: data, step: 1}),
                                            loadResponsesSuccess({
                                                message: {
                                                    response: 'Success - Continuing to registration',
                                                    status: 200,
                                                    payload: null
                                                }
                                            })

                                        ];
                                    } else {
                                        return [
                                            loadBrsValidationsFailure({
                                                error: {
                                                    payload: null,
                                                    response: 'BRS Validation failed, try again later',
                                                    status: 500
                                                }, data: null, step: 0
                                            })
                                        ];
                                    }


                                }),
                                catchError(
                                    (err: HttpErrorResponse) => {
                                        return of(loadBrsValidationsFailure({
                                                error: {
                                                    payload: null,
                                                    response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`,
                                                    status: err.status
                                                }, data: null, step: 0
                                            })
                                        );
                                    })
                            );
                    }
                )
            ),
        {dispatch: true}
    );

    doSendTokenToPhone: Observable<Action> = createEffect(
        () =>
            this.actions$.pipe(
                ofType(loadSendTokenToPhone),
                switchMap((action) => this.service.sendTokenToPhone(action.payload)
                    .pipe(
                        mergeMap((data) => {
                            if (data.status === 200) {
                                return [
                                    loadSendTokenToPhoneSuccess({data: data, validated: true}),
                                    loadResponsesSuccess({message: data})
                                ];
                            } else {
                                return [
                                    loadSendTokenToPhoneFailure({error: data, validated: false})
                                ];
                            }
                        }),
                        catchError(
                            (err: HttpErrorResponse) => of(loadSendTokenToPhoneFailure({
                                error: {
                                    payload: err.error,
                                    status: err.status,
                                    response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                                }, validated: false
                            })))
                    )
                )
            ),
        {dispatch: true}
    );


    doRegisterCompany: Observable<Action> = createEffect(
        () =>
            this.actions$.pipe(
                ofType(loadRegistrations),
                switchMap((action) => this.service.registerCompany(action.payload)
                    .pipe(
                        mergeMap((data) => {
                            if (data.status === 200) {
                                return [
                                    loadRegistrationsSuccess({data: data, succeeded: true}),
                                    loadResponsesSuccess({message: data})
                                ];
                            } else {
                                return [
                                    loadRegistrationsFailure({error: data, succeeded: false})
                                ];
                            }
                        }),
                        catchError(
                            (err: HttpErrorResponse) => of(loadRegistrationsFailure({
                                error: {
                                    payload: err.error,
                                    status: err.status,
                                    response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                                }, succeeded: false
                            })))
                    )
                )
            ),
        {dispatch: true}
    );

    doValidateTokenAndPhone: Observable<Action> = createEffect(
        () =>
            this.actions$.pipe(
                ofType(loadValidateTokenAndPhone),
                switchMap((action) => this.service.validateTokenAndPhone(action.payload)
                    .pipe(
                        mergeMap((data: ApiResponse) => {
                            if (data.status === 200) {
                                return [
                                    loadValidateTokenAndPhoneSuccess({data: data, validated: true})
                                ];
                            } else {
                                return [
                                    loadValidateTokenAndPhoneFailure({error: data, validated: false})
                                ];
                            }
                        }),
                        catchError(
                            (err: HttpErrorResponse) => of(loadValidateTokenAndPhoneFailure({
                                error: {
                                    payload: err.error,
                                    status: err.status,
                                    response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                                }, validated: false
                            })))
                    )
                )
            ),
        {dispatch: true}
    );

    constructor(
        private actions$: Actions,
        private service: RegistrationService
    ) {
    }

}

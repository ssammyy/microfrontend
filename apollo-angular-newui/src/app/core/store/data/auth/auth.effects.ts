import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {
    doSendTokenForUser,
    doSendTokenForUserSuccess,
    doValidateTokenForUser,
    doValidateTokenForUserB,

    doValidateTokenForUserFailure,
    doValidateTokenForUserSuccess,
    loadAuths,
    loadAuthsSuccess,
    loadLogout,
    loadLogoutFailure,
    loadLogoutSuccess,
    loadResetAuths,
    loadUserCompanyInfo,
    loadUserCompanyInfoSuccess
} from './auth.actions';
import {Observable, of} from 'rxjs';
import {Action} from '@ngrx/store';
import {HttpErrorResponse} from '@angular/common/http';
import {catchError, mergeMap, switchMap} from 'rxjs/operators';
import {Go, GoB} from '../route';
import {AuthService} from './auth.service';
import {loadResponsesFailure, loadResponsesSuccess} from '../response';
import {loadBranchIdSuccess, loadCompanyIdSuccess} from '../companies';


@Injectable()
export class AuthEffects {

    loadResetAuths: Observable<Action> = createEffect(
        () =>
            this.actions$.pipe(
                ofType(loadResetAuths),
                switchMap((action) => this.service.resetUserCredentials(action.payload)
                    .pipe(
                        mergeMap((data) => {
                            if (data.status === 200) {
                                return [
                                    loadResponsesSuccess({message: data}),
                                    GoB({payload: null})
                                ];
                            } else {
                                return [
                                    loadResponsesFailure({error: data})
                                ];
                            }

                        }),
                        catchError(
                            (err: HttpErrorResponse) => {
                                return of(loadResponsesFailure({
                                    error: {
                                        payload: err.error,
                                        status: err.status,
                                        response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                                    }
                                }));
                            })
                    )
                )
            ),
        {dispatch: true}
    );
    doSendTokenForUser: Observable<Action> = createEffect(
        () =>
            this.actions$.pipe(
                ofType(doSendTokenForUser),
                switchMap((action) => this.service.sendTokenForUser(action.payload)
                    .pipe(
                        mergeMap((data) => {
                            return data.status === 200 ? [
                                doSendTokenForUserSuccess({data: data, otpSent: true}),
                                loadResponsesSuccess({message: data})
                            ] : [
                                loadResponsesFailure({error: data})
                            ];

                        }),
                        catchError(
                            (err: HttpErrorResponse) => {
                                return of(loadResponsesFailure({
                                    error: {
                                        payload: err.error,
                                        status: err.status,
                                        response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                                    }
                                }));
                            })
                    )
                )
            ),
        {dispatch: true}
    );

    doValidateTokenForUser: Observable<Action> = createEffect(
        () =>
            this.actions$.pipe(
                ofType(doValidateTokenForUser),
                switchMap((action) => this.service.validateTokenForUser(action.payload)
                    .pipe(
                        mergeMap((data) => {
                            if (data.id) {
                                if (this.hasRole(["PERMIT_APPLICATION"], data.roles)) {
                                    return [
                                        loadAuthsSuccess({profile: data, loggedIn: true}),
                                        loadUserCompanyInfo(),
                                        doValidateTokenForUserSuccess({
                                            data: {
                                                payload: "Success, valid OTP received",
                                                response: "00",
                                                status: 200
                                            }, validated: true
                                        }),
                                        loadResponsesSuccess({
                                            message: {
                                                payload: "Success, valid OTP received",
                                                response: "00",
                                                status: 200
                                            }
                                        })
                                    ];
                                } else {
                                    return [
                                        loadAuthsSuccess({profile: data, loggedIn: true}),
                                        doValidateTokenForUserSuccess({
                                            data: {
                                                payload: "Success, valid OTP received",
                                                response: "00",
                                                status: 200
                                            }, validated: true
                                        }),
                                        loadResponsesSuccess({
                                            message: {
                                                payload: "Success, valid OTP received",
                                                response: "00",
                                                status: 200
                                            }
                                        })
                                    ];
                                }
                            } else {
                                return [
                                    doValidateTokenForUserFailure({
                                        data: {
                                            payload: "Failed, invalid OTP received, try again",
                                            response: "99",
                                            status: 500
                                        }, validated: false
                                    })
                                ];
                            }
                        }),
                        catchError(
                            (err: HttpErrorResponse) => {
                                return of(doValidateTokenForUserFailure({
                                    data: {
                                        payload: err.error,
                                        status: err.status,
                                        response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                                    }, validated: false
                                }));
                            })
                    )
                )
            ),
        {dispatch: true}
    );
    doValidateTokenForUserB: Observable<Action> = createEffect(
        () =>
            this.actions$.pipe(
                ofType(doValidateTokenForUserB),
                switchMap((action) => this.service.validateTokenForUserB(action.payload)
                    .pipe(
                        mergeMap((data) => {
                            if (data.status === 200) {
                                return [
                                    doValidateTokenForUserSuccess({data: data, validated: true}),
                                    loadResponsesSuccess({message: data})
                                ];
                            } else {
                                return [
                                    doValidateTokenForUserFailure({data: data, validated: false})
                                ];
                            }
                        }),
                        catchError(
                            (err: HttpErrorResponse) => {
                                return of(doValidateTokenForUserFailure({
                                    data: {
                                        payload: err.error,
                                        status: err.status,
                                        response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                                    }, validated: false
                                }));
                            })
                    )
                )
            ),
        {dispatch: true}
    );

    loadUserCompanyInfo: Observable<Action> = createEffect(
        () =>
            this.actions$.pipe(
                ofType(loadUserCompanyInfo),
                switchMap(() => this.service.fetchCompanyDetails()
                    .pipe(
                        mergeMap((data) => {
                            if (data.companyId != null) {
                                return [
                                    loadUserCompanyInfoSuccess({data: data}),
                                ];
                            } else {
                                return [
                                    loadResponsesFailure({
                                        error: {
                                            status: 500,
                                            payload: 'No data returned',
                                            response: '99'
                                        }
                                    })
                                ];
                            }
                        }),
                        catchError(
                            (err: HttpErrorResponse) => {
                                return of(loadResponsesFailure({
                                    error: {
                                        payload: err.error,
                                        status: err.status,
                                        response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                                    }
                                }));
                            })
                    )
                )
            ),
        {dispatch: true}
    );

    doLogout: Observable<Action> = createEffect(
        () =>
            this.actions$.pipe(
                ofType(loadLogout),
                switchMap((action) => this.service.logout()
                    .pipe(
                        mergeMap((data) => {
                            return [
                                loadLogoutSuccess({
                                    data: data,
                                    loggedIn: false,
                                    profile: {
                                        id: 0,
                                        username: '',
                                        roles: undefined,
                                        expiry: undefined,
                                        email: '',
                                        accessToken: '',
                                        fullName: '',
                                        companyID: 0,
                                        redirectUrl:undefined
                                    }
                                }),
                                loadUserCompanyInfoSuccess({data: null}),
                                loadBranchIdSuccess({branchId: null, branch: null}),
                                loadCompanyIdSuccess({companyId: null, company: null}),
                                // Go({payload: null, link: action.loginUrl, redirectUrl: null})
                            ];
                        }),
                        catchError(
                            (err: HttpErrorResponse) => {
                                loadLogoutFailure({
                                    error: {
                                        payload: err.error,
                                        status: err.status,
                                        response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                                    }, loggedIn: false, profile: null
                                });
                                return of(Go({payload: err, link: action.loginUrl, redirectUrl: null}));
                            })
                    )
                )
            ),
        {dispatch: true}
    );
    hasRole(privileges: string[], roles: any[]): boolean {
        for (let role of roles) {
            for (let p of privileges) {
                if (role == p) {
                    return true
                }
            }
        }
        return false
    }
    doLogin: Observable<Action> = createEffect(
        () =>
            this.actions$.pipe(
                ofType(loadAuths),
                switchMap((action) => this.service.login(action.payload)
                    .pipe(
                        mergeMap((data) => {
                            // data.redirectUrl = action.redirectUrl
                            // if(this.hasRole(["PERMIT_APPLICATION"], data.roles)){
                                return [
                                    loadAuthsSuccess({
                                        loggedIn: false,
                                        profile: {
                                            id: 0,
                                            username: action.payload.username,
                                            roles: undefined,
                                            expiry: undefined,
                                            email: action.payload.username,
                                            accessToken: data.payload,
                                            fullName: '',
                                            companyID: 0,
                                            redirectUrl: action.redirectUrl
                                        }
                                    }),
                                    Go({
                                        payload: action.redirectUrl,
                                        link: 'login/otp',
                                        redirectUrl: null
                                    })
                                ];
                         //   }
                         //    return [
                         //        loadAuthsSuccess({profile: data, loggedIn: true}),
                         //        Go({
                         //            payload: action.redirectUrl,
                         //            link: 'login/otp',
                         //            redirectUrl: null
                         //        })
                         //    ];
                        }),

                        catchError(
                            (err: HttpErrorResponse) => of(loadResponsesFailure({
                                error: {
                                    payload: err.error,
                                    status: err.status,
                                    response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                                }
                            })))
                    )
                )
            ),
        {dispatch: true}
    );

    constructor(
        private actions$: Actions,
        private service: AuthService,
        // private store$: Store<any>,
    ) {
    }


}

import {Action, createReducer, on} from '@ngrx/store';
import {CompanyInfoDto, LoggedInUser} from './auth.model';
import {
    doSendTokenForUserSuccess, doValidateTokenForUserFailure,
    doValidateTokenForUserSuccess,
    loadAuthsFailure,
    loadAuthsSuccess,
    loadResetAuthsFailure,
    loadResetAuthsSuccess,
    loadUserCompanyInfoFailure,
    loadUserCompanyInfoSuccess
} from './auth.actions';
import {ApiResponse} from '../../../domain/response.model';


export const authFeatureKey = 'auth';
export const tokenValidatedFeatureKey = 'tokenValidated';
export const tokenSentFeatureKey = 'tokenSent';
export const companyInfoDtoStateFeatureKey = 'companyInfoDtoState';

export interface AuthState {
    profile: LoggedInUser;
    loggedIn: boolean;

}

export interface TokenSentState {
    data: ApiResponse;
    otpSent: boolean;
}

export interface TokenValidatedState {
    data: ApiResponse;
    validated: boolean;
}

export interface CompanyInfoDtoState {
    data: CompanyInfoDto;
}

export const initialState: AuthState = {
    profile: {
        id: 0,
        accessToken: '',
        email: '',
        fullName: '',
        roles: [],
        username: '',
        expiry: new Date(),
        // companyID: 0,
        // branchID: 0,
        // turnover: 0
    },
    loggedIn: false

};
export const initialCompanyInfoDtoState: CompanyInfoDtoState = {
    data: {branchCount: 0, branchId: 0, companyId: 0, userId: 0, turnover: 0, countAwarded: 0, countExpired: 0}

};

const tokenCompanyInfoDtoInternalReducer = createReducer(
    initialCompanyInfoDtoState,
    on(loadUserCompanyInfoSuccess, (state, {data}) => {
        return {
            ...state,
            data
        };
    }),
    on(loadUserCompanyInfoFailure, (state, {error}) => {
        return {
            ...state,
            error
        };
    })
);

export const initialTokenValidatedState: TokenValidatedState = {
    data: {payload: '', status: 0, response: ''},
    validated: false

};
export const initialTokenSentState: TokenSentState = {
    data: {payload: '', status: 0, response: ''},
    otpSent: false

};


const authStateInternalReducer = createReducer(
    initialState,
    on(loadAuthsSuccess, (state, {profile, loggedIn}) => {
        return {
            ...state,
            profile,
            loggedIn
        };
    }),
    on(loadResetAuthsSuccess, (state, {profile, loggedIn}) => {
        return {
            ...state,
            profile,
            loggedIn
        };
    }),
    on(loadAuthsFailure, (state, {error}) => {
        return {
            ...state,
            error
        };
    }),
    on(loadResetAuthsFailure, (state, {error}) => {
        return {
            ...state,
            error
        };
    }),
);

const tokenValidatedStateInternalReducer = createReducer(
    initialTokenValidatedState,
    on(doValidateTokenForUserSuccess, (state, {data, validated}) => {
        return {
            ...state,
            data,
            validated
        };
    }),
    on(doValidateTokenForUserFailure, (state, {data, validated}) => {
        return {
            ...state,
            data,
            validated
        };
    })
);

const tokenSentStateInternalReducer = createReducer(
    initialTokenSentState,
    on(doSendTokenForUserSuccess, (state, {data, otpSent}) => {
        return {
            ...state,
            data,
            otpSent
        };
    })
);

export function authReducer(state: AuthState, action: Action): AuthState {
    return authStateInternalReducer(state, action);
}

export function tokenValidatedStateReducer(state: TokenValidatedState, action: Action): TokenValidatedState {
    return tokenValidatedStateInternalReducer(state, action);
}

export function tokenSentStateReducer(state: TokenSentState, action: Action): TokenSentState {
    return tokenSentStateInternalReducer(state, action);
}

export function companyInfoDtoStateStateReducer(state: CompanyInfoDtoState, action: Action): CompanyInfoDtoState {
    return tokenCompanyInfoDtoInternalReducer(state, action);
}



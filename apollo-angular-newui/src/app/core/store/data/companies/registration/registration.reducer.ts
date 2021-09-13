import {Action, createReducer, on} from '@ngrx/store';
import {ApiResponse} from '../../../../domain/response.model';
import {Company} from '../company';
import {
    loadBrsValidationsFailure,
    loadBrsValidationsSuccess,
    loadRegistrationsFailure,
    loadRegistrationsSuccess,
    loadSendTokenToPhoneFailure,
    loadSendTokenToPhoneSuccess,
    loadValidateTokenAndPhoneFailure,
    loadValidateTokenAndPhoneSuccess
} from './registration.actions';


export const registrationFeatureKey = 'registration';
export const brsValidationStateFeatureKey = 'brsValidation';
export const sendTokenToPhoneStateFeatureKey = 'sendTokenToPhone';
export const validateTokenAndPhoneStateFeatureKey = 'validateTokenAndPhone';

export interface RegistrationState {
    response: ApiResponse;
    succeeded: boolean;
}

export interface BrsValidationState {
    response: ApiResponse;
    data: Company;
    step: number;

}

export interface SendTokenToPhoneState {
    response: ApiResponse;
    validated: boolean;
}

export interface ValidateTokenAndPhoneState {
    response: ApiResponse;
    validated: boolean;
}

export const initialRegistrationState: RegistrationState = {
    response: {response: '', status: 0, payload: null},
    succeeded: false
};
export const initialSendTokenToPhoneState: SendTokenToPhoneState = {
    response: {response: '', status: 0, payload: null},
    validated: false
};
export const initialValidateTokenAndPhoneState: ValidateTokenAndPhoneState = {
    response: {response: '', status: 0, payload: null},
    validated: false
};


export const initialBrsValidationState: BrsValidationState = {
    response: {response: '', status: 0, payload: null},
    data: {
        id: -1,
        buildingName: '',
        branchName: '',
        name: '',
        businessLines: 0,
        businessNatures: 0,
        companyEmail: '',
        companyTelephone: '',
        county: 0,
        directorIdNumber: '',
        plotNumber: '',
        kraPin: '',
        physicalAddress: '',
        postalAddress: '',
        region: 0,
        registrationNumber: '',
        streetName: '',
        town: 0,
        yearlyTurnover: 0,
        status: false
    },
    step: 0

};

const registrationReducerInternal = createReducer(
    initialRegistrationState,
    on(loadRegistrationsSuccess, (state, {data, succeeded}) => {
        return {
            ...state,
            data,
            succeeded
        };
    }),
    on(loadRegistrationsFailure, (state, {error, succeeded}) => {
        return {
            ...state,
            error,
            succeeded
        };
    }),
);

const sendTokenToPhoneStateReducerInternal = createReducer(
    initialSendTokenToPhoneState,
    on(loadSendTokenToPhoneSuccess, (state, {data, validated}) => {
        return {
            ...state,
            data,
            validated
        };
    }),
    on(loadSendTokenToPhoneFailure, (state, {error, validated}) => {
        return {
            ...state,
            error,
            validated
        };
    }),
);

const brsValidationReducerInternal = createReducer(
    initialBrsValidationState,
    on(loadBrsValidationsSuccess, (state, {data, step}) => {
        return {
            ...state,
            data,
            step
        };
    }),
    on(loadBrsValidationsFailure, (state, {error, data, step}) => {
        return {
            ...state,
            error,
            data,
            step
        };
    }),
);

const validateTokenAndPhoneReducerInternal = createReducer(
    initialValidateTokenAndPhoneState,
    on(loadValidateTokenAndPhoneSuccess, (state, {data, validated}) => {
        return {
            ...state,
            data,
            validated
        };
    }),
    on(loadValidateTokenAndPhoneFailure, (state, {error}) => {
        return {
            ...state,
            error
        };
    }),
);


export function registrationReducer(state: RegistrationState, action: Action): RegistrationState {
    return registrationReducerInternal(state, action);
}

export function brsValidationReducer(state: BrsValidationState, action: Action): BrsValidationState {
    return brsValidationReducerInternal(state, action);
}

export function sendTokenToPhoneReducer(state: SendTokenToPhoneState, action: Action): SendTokenToPhoneState {
    return sendTokenToPhoneStateReducerInternal(state, action);
}

export function validateTokenAndPhoneReducer(state: ValidateTokenAndPhoneState, action: Action): ValidateTokenAndPhoneState {
    return validateTokenAndPhoneReducerInternal(state, action);
}


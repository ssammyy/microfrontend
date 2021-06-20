import {Action, createReducer, on} from '@ngrx/store';
import {ApiResponse} from "../../../../domain/response.model";
import {Company} from "../company.model";
import {
  loadBrsValidationsFailure,
  loadBrsValidationsSuccess,
  loadSendTokenToPhoneFailure,
  loadSendTokenToPhoneSuccess,
  loadValidateTokenAndPhoneFailure,
  loadValidateTokenAndPhoneSuccess
} from "./registration.actions";


export const registrationFeatureKey = 'registration';
export const brsValidationStateFeatureKey = 'brsValidation';
export const sendTokenToPhoneStateFeatureKey = 'sendTokenToPhone';
export const validateTokenAndPhoneStateFeatureKey = 'validateTokenAndPhone';

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

export const initialSendTokenToPhoneState: SendTokenToPhoneState = {
  response: {response: '', status: 0, payload: null},
  validated: false
}
export const initialValidateTokenAndPhoneState: ValidateTokenAndPhoneState = {
  response: {response: '', status: 0, payload: null},
  validated: false
}


export const initialBrsValidationState: BrsValidationState = {
  response: {response: '', status: 0, payload: null},
  data: {
    buildingName: '',
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
    yearlyTurnover: 0
  },
  step: 0

};

const sendTokenToPhoneStateReducerInternal = createReducer(
  initialSendTokenToPhoneState,
  on(loadSendTokenToPhoneSuccess, (state, {data, validated}) => {
    return {
      ...state,
      data,
      validated
    };
  }),
  on(loadSendTokenToPhoneFailure, (state, {error}) => {
    return {
      ...state,
      error,
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
  on(loadBrsValidationsFailure, (state, {error}) => {
    return {
      ...state,
      error
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


export function brsValidationReducer(state: BrsValidationState, action: Action): BrsValidationState {
  return brsValidationReducerInternal(state, action);
}

export function sendTokenToPhoneReducer(state: SendTokenToPhoneState, action: Action): SendTokenToPhoneState {
  return sendTokenToPhoneStateReducerInternal(state, action);
}

export function validateTokenAndPhoneReducer(state: ValidateTokenAndPhoneState, action: Action): ValidateTokenAndPhoneState {
  return validateTokenAndPhoneReducerInternal(state, action);
}


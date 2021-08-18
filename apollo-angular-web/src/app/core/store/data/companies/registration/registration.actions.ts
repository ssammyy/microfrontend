import {createAction, props} from '@ngrx/store';
import {ApiResponse} from '../../../../domain/response.model';
import {Company} from '../company';
import {BrsLookUpRequest, RegistrationPayload, SendTokenToPhone, ValidateTokenAndPhone} from './registration.models';

export const loadRegistrations = createAction(
    '[Registration] doRegisterCompany',
    props<{ payload: RegistrationPayload }>()
);
export const loadBrsValidations = createAction(
    '[BrsValidation] Load Brs Validations',
    props<{ payload: BrsLookUpRequest }>()
);
export const loadSendTokenToPhone = createAction(
    '[SendTokenToPhone] doSendTokenToPhone',
    props<{ payload: SendTokenToPhone }>()
);
export const loadValidateTokenAndPhone = createAction(
    '[ValidateTokenAndPhone] Load ValidateTokenAndPhone',
    props<{ payload: ValidateTokenAndPhone }>()
);

export const loadRegistrationsSuccess = createAction(
    '[Registration] Load Registrations Success',
    props<{ data: ApiResponse, succeeded: boolean }>()
);
export const loadBrsValidationsSuccess = createAction(
    '[BrsValidation] Load Brs Validations Success',
    props<{ data: Company, step: number }>()
);
export const loadValidateTokenAndPhoneSuccess = createAction(
    '[BrsValidation] Load ValidateTokenAndPhone Success',
    props<{ data: ApiResponse, validated: boolean }>()
);

export const loadSendTokenToPhoneSuccess = createAction(
    '[SendTokenToPhone] Load SendTokenToPhone Success',
    props<{ data: ApiResponse, validated: boolean }>()
);

export const loadRegistrationsFailure = createAction(
    '[Registration] Load Registrations Failure',
    props<{ error: ApiResponse, succeeded: boolean }>()
);

export const loadBrsValidationsFailure = createAction(
    '[BrsValidation] Load Brs Validations Failure',
    props<{ error: ApiResponse, data: Company | undefined, step: number }>()
);
export const loadSendTokenToPhoneFailure = createAction(
    '[SendTokenToPhone] Load Send Token To Phone Failure',
    props<{ error: ApiResponse, validated: boolean }>()
);
export const loadValidateTokenAndPhoneFailure = createAction(
    '[BrsValidation] Load ValidateTokenAndPhone Failure',
    props<{ error: ApiResponse, validated: boolean }>()
);

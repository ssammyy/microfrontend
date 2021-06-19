import {createAction, props} from '@ngrx/store';
import {ApiResponse} from "../../../../domain/response.model";
import {Company} from "../company.model";
import {BrsLookUpRequest} from "./registration.models";

export const loadRegistrations = createAction(
  '[Registration] Load Registrations'
);
export const loadBrsValidations = createAction(
  '[BrsValidation] Load Brs Validations',
  props<{ payload: BrsLookUpRequest }>()
);

export const loadRegistrationsSuccess = createAction(
  '[Registration] Load Registrations Success',
  props<{ data: ApiResponse }>()
);
export const loadBrsValidationsSuccess = createAction(
  '[BrsValidation] Load Brs Validations Success',
  props<{ data: Company, step: number }>()
);

export const loadRegistrationsFailure = createAction(
  '[Registration] Load Registrations Failure',
  props<{ error: ApiResponse }>()
);

export const loadBrsValidationsFailure = createAction(
  '[BrsValidation] Load Brs Validations Failure',
  props<{ error: ApiResponse }>()
);

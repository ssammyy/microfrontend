import {Action, createReducer, on} from '@ngrx/store';
import {ApiResponse} from "../../../../domain/response.model";
import {Company} from "../company.model";
import {loadBrsValidationsFailure, loadBrsValidationsSuccess} from "./registration.actions";


export const registrationFeatureKey = 'registration';
export const brsValidationStateFeatureKey = 'brsValidation';

export interface BrsValidationState {
  response: ApiResponse;
  data: Company;
  step: number;

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


export function brsValidationReducer(state: BrsValidationState, action: Action): BrsValidationState {
  return brsValidationReducerInternal(state, action);
}


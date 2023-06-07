import {Action, createReducer, on} from '@ngrx/store';
import {loadCountyIdFailure, loadCountyIdSuccess} from "./town.actions";

export interface CountyIdState {
  countyId: number
}


export const countyIdFeatureKey = 'countyId';

export const initialCountyIdState: CountyIdState = {
  countyId: 1
};

const internalCountyIdReducer = createReducer(
  initialCountyIdState,
  on(loadCountyIdSuccess, (state, {countyId}) => {
    return {
      ...state,
      countyId
    };
  }),
  on(loadCountyIdFailure, (state, {error}) => {
    return {
      ...state,
      error
    };
  })
);

export function countyIdReducer(state: CountyIdState | undefined, action: Action): CountyIdState {
  return internalCountyIdReducer(state, action);
}


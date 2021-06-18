import {Action, createReducer, on} from '@ngrx/store';
import {ApiResponse} from "../../../domain/response.model";
import * as actions from './response.actions';


export const responseFeatureKey = 'responses';

export interface ResponseState {
  data: ApiResponse;
}

export const initialResponseState: ResponseState = {
  data: {response: '', status: 0, payload: null}
};


export const responseReducerInternal = createReducer(
  initialResponseState,
  on(actions.loadResponses, (state, {}) => {
    return {
      ...state
    };
  }),
  on(actions.loadResponsesSuccess, (state, {message}) => {
    return {
      ...state,
      data: message
    };
  }),
  on(actions.loadResponsesFailure, (state, {error}) => {
    return {
      ...state,
      data: error
    };
  })
);

export function responseReducer(state: ResponseState, action: Action): ResponseState {
  return responseReducerInternal(state, action);
}



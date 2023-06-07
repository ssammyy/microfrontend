import {createAction, props} from '@ngrx/store';
import {ApiResponse} from '../../../domain/response.model';


export const loadResponses = createAction(
  '[Response] Load Responses'
);

export const loadResponsesSuccess = createAction(
  '[Response] Load Responses Success',
  props<{ message: ApiResponse }>()


);

export const loadResponsesFailure = createAction(
  '[Response] Load Responses Failure',
  props<{ error: ApiResponse }>()
);

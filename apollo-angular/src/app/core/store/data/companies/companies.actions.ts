import {createAction, props} from '@ngrx/store';
import {ApiResponse} from "../../../domain/response.model";

export const loadCompanyId = createAction(
  '[Companies] loadCompanyId',
  props<{ payload: number }>()
);
export const loadBranchId = createAction(
  '[Companies] loadBranchId',
  props<{ payload: number }>()
);

export const loadDirectorId = createAction(
  '[Companies] loadDirectorId',
  props<{ payload: number }>()
);

export const loadUserId = createAction(
  '[Companies] loadUserId',
  props<{ payload: number }>()
);

export const loadCompanyIdSuccess = createAction(
  '[Request] Load loadCompanyId Success',
  props<{ companyId: number }>()
);
export const loadUserIdSuccess = createAction(
  '[Request] Load loadUserId Success',
  props<{ userId: number }>()
);


export const loadBranchIdSuccess = createAction(
  '[Request] Load loadBranchId Success',
  props<{ branchId: number }>()
);


export const loadDirectorIdSuccess = createAction(
  '[Request] Load loadDirectorId Success',
  props<{ directorId: number }>()
);

export const loadCompanyIdFailure = createAction(
  '[Request] Load loadCompanyId Failure',
  props<{ error: ApiResponse }>()
);


export const loadUserIdFailure = createAction(
  '[Request] Load loadUserId Failure',
  props<{ error: ApiResponse }>()
);


export const loadBranchIdFailure = createAction(
  '[Request] Load loadBranchId Failure',
  props<{ error: ApiResponse }>()
);

export const loadDirectorIdFailure = createAction(
  '[Request] Load loadDirectorId Failure',
  props<{ error: ApiResponse }>()
);





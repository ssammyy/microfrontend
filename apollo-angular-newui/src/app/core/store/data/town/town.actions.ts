import {createAction, props} from "@ngrx/store";
import {ApiResponse} from "../../../domain/response.model";


export const loadCountyId = createAction(
  '[Town] loadCountyId',
  props<{ payload: number }>()
);

export const loadCountyIdSuccess = createAction(
  '[Town] Load loadCountyId Success',
  props<{ countyId: number }>()
);

export const loadCountyIdFailure = createAction(
  '[Town] Load loadCountyId Failure',
  props<{ error: ApiResponse }>()
);





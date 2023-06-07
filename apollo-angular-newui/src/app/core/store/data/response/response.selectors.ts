import {createFeatureSelector, createSelector} from '@ngrx/store';
import {responseFeatureKey, ResponseState} from "./response.reducer";

export const getResponsesFeatureState = createFeatureSelector<ResponseState>(responseFeatureKey);

export const selectResponseData = createSelector(
  getResponsesFeatureState,
  (state: ResponseState) => state.data
);


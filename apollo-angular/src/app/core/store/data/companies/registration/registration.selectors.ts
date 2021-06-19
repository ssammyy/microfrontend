import {createFeatureSelector, createSelector} from '@ngrx/store';
import {BrsValidationState, brsValidationStateFeatureKey} from "./registration.reducer";

export const getBrsValidationFeatureState = createFeatureSelector<BrsValidationState>(brsValidationStateFeatureKey);
export const selectBrsValidationCompany = createSelector(
  getBrsValidationFeatureState,
  (state: BrsValidationState) => state.data
)
export const selectBrsValidationStep = createSelector(
  getBrsValidationFeatureState,
  (state: BrsValidationState) => state.step
)


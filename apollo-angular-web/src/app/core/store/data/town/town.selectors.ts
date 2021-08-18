import {createFeatureSelector, createSelector} from '@ngrx/store';
import {countyIdFeatureKey, CountyIdState} from "./town.reducer";


export const countyIdFeatureSelector = createFeatureSelector<CountyIdState>(countyIdFeatureKey);

export const selectCountyIdData = createSelector(
  countyIdFeatureSelector,
  (state: CountyIdState) => state.countyId
);


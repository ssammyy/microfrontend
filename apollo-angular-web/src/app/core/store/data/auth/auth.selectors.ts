import {createFeatureSelector, createSelector} from '@ngrx/store';
import {authFeatureKey, AuthState} from "./auth.reducer";

export const getAuthFeatureState = createFeatureSelector<AuthState>(authFeatureKey);


export const selectIsAuthenticated = createSelector(
  getAuthFeatureState,
  (state: AuthState) => state.loggedIn
);
export const selectUserInfo = createSelector(
  getAuthFeatureState,
  (state: AuthState) => state.profile
);
export const selectGetNames = createSelector(
  getAuthFeatureState,
  (state: AuthState) => state.profile.fullName
);

import {createFeatureSelector, createSelector} from '@ngrx/store';
import {authFeatureKey, AuthState, TokenValidatedState} from "./auth.reducer";

export const getAuthFeatureState = createFeatureSelector<AuthState>(authFeatureKey);
export const getTokenFeatureState = createFeatureSelector<TokenValidatedState>(authFeatureKey);


export const selectIsAuthenticated = createSelector(
  getAuthFeatureState,
  (state: AuthState) => state.loggedIn && new Date(state.profile.expiry).getTime() > Date.now()
);
export const selectUserInfo = createSelector(
  getAuthFeatureState,
  (state: AuthState) => state.profile
);
export const selectGetNames = createSelector(
  getAuthFeatureState,
  (state: AuthState) => state.profile.fullName
);


export const selectTokenValidatedStateValidated = createSelector(
  getTokenFeatureState,
  (state: TokenValidatedState) => state.validated
)

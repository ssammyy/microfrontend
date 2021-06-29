import {createFeatureSelector, createSelector} from '@ngrx/store';
import {
  authFeatureKey,
  AuthState,
  tokenSentFeatureKey,
  TokenSentState,
  tokenValidatedFeatureKey,
  TokenValidatedState
} from "./auth.reducer";

export const getAuthFeatureState = createFeatureSelector<AuthState>(authFeatureKey);
export const getTokenFeatureState = createFeatureSelector<TokenValidatedState>(tokenValidatedFeatureKey);
export const getTokenSentFeatureState = createFeatureSelector<TokenSentState>(tokenSentFeatureKey);


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
export const selectTokenSentStateOtpSent = createSelector(
  getTokenSentFeatureState,
  (state: TokenSentState) => state.otpSent
)

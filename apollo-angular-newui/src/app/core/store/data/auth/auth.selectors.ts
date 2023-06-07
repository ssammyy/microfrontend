import {createFeatureSelector, createSelector} from '@ngrx/store';
import {
  authFeatureKey,
  AuthState,
  CompanyInfoDtoState,
  companyInfoDtoStateFeatureKey,
  tokenSentFeatureKey,
  TokenSentState,
  tokenValidatedFeatureKey,
  TokenValidatedState
} from './auth.reducer';
import {RouteGuard} from '../../../route-guard/route.guard';

export const getAuthFeatureState = createFeatureSelector<AuthState>(authFeatureKey);
export const getTokenFeatureState = createFeatureSelector<TokenValidatedState>(tokenValidatedFeatureKey);
export const getTokenSentFeatureState = createFeatureSelector<TokenSentState>(tokenSentFeatureKey);
export const getCompanyInfoDtoFeatureState = createFeatureSelector<CompanyInfoDtoState>(companyInfoDtoStateFeatureKey);


export const selectIsAuthenticated = createSelector(
    getAuthFeatureState,
    // (state: AuthState) => state.loggedIn &&  RouteGuard.tokenExpired(state.profile.accessToken)
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


export const selectTokenValidatedStateValidated = createSelector(
  getTokenFeatureState,
  (state: TokenValidatedState) => state.validated
);

export const selectTokenSentStateOtpSent = createSelector(
  getTokenSentFeatureState,
  (state: TokenSentState) => state.otpSent
);

export const selectCompanyInfoDtoStateData = createSelector(
    getCompanyInfoDtoFeatureState,
    (state: CompanyInfoDtoState) => state.data
);


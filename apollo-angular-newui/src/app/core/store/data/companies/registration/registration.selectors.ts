import {createFeatureSelector, createSelector} from '@ngrx/store';
import {
  BrsValidationState,
  brsValidationStateFeatureKey,
  registrationFeatureKey,
  RegistrationState,
  SendTokenToPhoneState,
  sendTokenToPhoneStateFeatureKey,
  ValidateTokenAndPhoneState,
  validateTokenAndPhoneStateFeatureKey
} from './registration.reducer';

export const getRegistrationFeatureState = createFeatureSelector<RegistrationState>(registrationFeatureKey);
export const getBrsValidationFeatureState = createFeatureSelector<BrsValidationState>(brsValidationStateFeatureKey);
export const getValidateTokenAndPhoneFeatureState = createFeatureSelector<ValidateTokenAndPhoneState>(validateTokenAndPhoneStateFeatureKey);
export const getSendTokenToPhoneFeatureState = createFeatureSelector<SendTokenToPhoneState>(sendTokenToPhoneStateFeatureKey);

export const selectBrsValidationCompany = createSelector(
  getBrsValidationFeatureState,
  (state: BrsValidationState) => state.data
);
export const selectBrsValidationStep = createSelector(
  getBrsValidationFeatureState,
  (state: BrsValidationState) => state.step
);
export const selectValidateTokenAndPhoneResponse = createSelector(
  getValidateTokenAndPhoneFeatureState,
  (state: ValidateTokenAndPhoneState) => state.response
);

export const selectValidateTokenAndPhoneValidated = createSelector(
  getValidateTokenAndPhoneFeatureState,
  (state: ValidateTokenAndPhoneState) => state.validated
);
export const selectSendTokenToPhoneStateResponse = createSelector(
  getSendTokenToPhoneFeatureState,
  (state: SendTokenToPhoneState) => state.response
);
export const selectSendTokenToPhoneStateSent = createSelector(
  getSendTokenToPhoneFeatureState,
  (state: SendTokenToPhoneState) => state.validated
);
export const selectRegistrationStateSucceeded = createSelector(
  getRegistrationFeatureState,
  (state: RegistrationState) => state.succeeded
);


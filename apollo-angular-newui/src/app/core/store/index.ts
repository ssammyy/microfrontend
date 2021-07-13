import {
  AuthEffects,
  authReducer,
  branchIdReducer,
  brsValidationReducer,
  CompaniesEffects,
  companyIdReducer,
  countyIdReducer,
  directorIdReducer,
    RegistrationEffects,
    registrationReducer,
    responseReducer,
    RouterEffects,
    sendTokenToPhoneReducer,
    tokenSentStateReducer,
    tokenValidatedStateReducer,
    TownEffects,
    userIdReducer,
    validateTokenAndPhoneReducer
} from './data';
import {ActionReducer, MetaReducer} from '@ngrx/store';
import {localStorageSync} from 'ngrx-store-localstorage';

export * from './data';

export const appReducer = {
    responses: responseReducer,
    brsValidation: brsValidationReducer,
    sendTokenToPhone: sendTokenToPhoneReducer,
    validateTokenAndPhone: validateTokenAndPhoneReducer,
    registration: registrationReducer,
  auth: authReducer,
  companyId: companyIdReducer,
  countyId: countyIdReducer,
  branchId: branchIdReducer,
  userId: userIdReducer,
  directorId: directorIdReducer,
  tokenSent: tokenSentStateReducer,
  tokenValidated: tokenValidatedStateReducer
};

export function localStorageSyncReducer(reducer: ActionReducer<any>): ActionReducer<any> {
  return localStorageSync({
    keys: [
      'routes',
      'auth',
      'companyId',
      'userId',
      'branchId'

    ],
    rehydrate: true,
    storage: sessionStorage
  })(reducer);
}

export const metaReducers: Array<MetaReducer<any, any>> = [localStorageSyncReducer];

export const appEffects = [RegistrationEffects, RouterEffects, AuthEffects, CompaniesEffects, TownEffects];

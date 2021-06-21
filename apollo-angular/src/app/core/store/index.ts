import {
  AuthEffects,
  authReducer,
  branchIdReducer,
  brsValidationReducer,
  companyIdReducer,
  directorIdReducer,
  RegistrationEffects,
  registrationReducer,
  responseReducer,
  RouterEffects,
  sendTokenToPhoneReducer,
  userIdReducer,
  validateTokenAndPhoneReducer
} from "./data";
import {ActionReducer, MetaReducer} from "@ngrx/store";
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
  branchId: branchIdReducer,
  userId: userIdReducer,
  directorId: directorIdReducer,
};

export function localStorageSyncReducer(reducer: ActionReducer<any>): ActionReducer<any> {
  return localStorageSync({
    keys: [
      'responses',
      'routes',
      'auth'
    ],
    rehydrate: true,
    storage: sessionStorage
  })(reducer);
}

export const metaReducers: Array<MetaReducer<any, any>> = [localStorageSyncReducer];

export const appEffects = [RegistrationEffects, RouterEffects, AuthEffects];

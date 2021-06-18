import {responseReducer} from "./data";
import {ActionReducer, MetaReducer} from "@ngrx/store";
import {localStorageSync} from 'ngrx-store-localstorage';

export * from './data';

export const appReducer = {
  responses: responseReducer,
};

export function localStorageSyncReducer(reducer: ActionReducer<any>): ActionReducer<any> {
  return localStorageSync({
    keys: [
      'responses'
    ],
    rehydrate: true,
    storage: sessionStorage
  })(reducer);
}

export const metaReducers: Array<MetaReducer<any, any>> = [localStorageSyncReducer];

export const appEffects = [];

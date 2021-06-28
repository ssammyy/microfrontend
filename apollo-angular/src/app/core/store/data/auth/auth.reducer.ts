import {Action, createReducer, on} from '@ngrx/store';
import {LoggedInUser} from "./auth.model";
import {doValidateTokenForUserSuccess, loadAuthsFailure, loadAuthsSuccess} from "./auth.actions";
import {ApiResponse} from "../../../domain/response.model";


export const authFeatureKey = 'auth';

export interface AuthState {
  profile: LoggedInUser;
  loggedIn: boolean;

}

export interface TokenValidatedState {
  data: ApiResponse;
  validated: Boolean;
}

export const initialState: AuthState = {
  profile: {id: 0, accessToken: '', email: '', fullName: '', roles: [], username: '', expiry: Date()},
  loggedIn: false

};
export const initialTokenValidatedState: TokenValidatedState = {
  data: {payload: '', status: 0, response: ''},
  validated: false

};


const authStateInternalReducer = createReducer(
  initialState,
  on(loadAuthsSuccess, (state, {profile, loggedIn}) => {
    return {
      ...state,
      profile,
      loggedIn
    };
  }),
  on(loadAuthsFailure, (state, {error}) => {
    return {
      ...state,
      error
    };
  }),
);

const tokenValidatedStateInternalReducer = createReducer(
  initialTokenValidatedState,
  on(doValidateTokenForUserSuccess, (state, {data, validated}) => {
    return {
      ...state,
      data,
      validated
    };
  })
);

export function authReducer(state: AuthState, action: Action): AuthState {
  return authStateInternalReducer(state, action);
}

export function tokenValidatedStateReducer(state: TokenValidatedState, action: Action): TokenValidatedState {
  return tokenValidatedStateInternalReducer(state, action);
}



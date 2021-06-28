import {Action, createReducer, on} from '@ngrx/store';
import {LoggedInUser} from "./auth.model";
import {loadAuthsFailure, loadAuthsSuccess} from "./auth.actions";


export const authFeatureKey = 'auth';

export interface AuthState {
  profile: LoggedInUser;
  loggedIn: boolean;

}

export const initialState: AuthState = {
  profile: {id: 0, accessToken: '', email: '', fullName: '', roles: [], username: ''},
  loggedIn: false

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

export function authReducer(state: AuthState, action: Action): AuthState {
  return authStateInternalReducer(state, action);
}



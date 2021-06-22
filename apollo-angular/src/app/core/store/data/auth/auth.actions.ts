import {createAction, props} from '@ngrx/store';
import {LoggedInUser, LoginCredentials} from "./auth.model";
import {ApiResponse} from "../../../domain/response.model";

export const loadLogout = createAction(
  '[Auth] Load loadLogout',
  props<{ loginUrl: string }>()
)

export const loadLogoutSuccess = createAction(
  '[Auth] Load loadLogoutSuccess',
  props<{ data: ApiResponse }>()
);
export const loadLogoutFailure = createAction(
  '[Auth] Load loadLogoutFailure',
  props<{ error: ApiResponse }>()
);

export const loadAuths = createAction(
  '[Auth] Load Auths',
  props<{ payload: LoginCredentials, redirectUrl: string }>()
);

export const loadAuthsSuccess = createAction(
  '[Auth] Load Auths Success',
  props<{ profile: LoggedInUser, loggedIn: boolean; }>()
);

export const loadAuthsFailure = createAction(
  '[Auth] Load Auths Failure',
  props<{ error: ApiResponse }>()
);

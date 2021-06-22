import {createAction, props} from '@ngrx/store';
import {LoggedInUser, LoginCredentials} from "./auth.model";
import {ApiResponse} from "../../../domain/response.model";

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

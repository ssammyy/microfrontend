import {createAction, props} from '@ngrx/store';
import {LoggedInUser, LoginCredentials, SendTokenRequestDto, ValidateTokenRequestDto} from "./auth.model";
import {ApiResponse} from "../../../domain/response.model";

export const loadLogout = createAction(
  '[Auth] Load loadLogout',
  props<{ loginUrl: string }>()
)
export const doSendTokenForUser = createAction(
  '[Auth] doSendTokenForUser',
  props<{ payload: SendTokenRequestDto }>()
)
export const doSendTokenForUserSuccess = createAction(
  '[Auth] doSendTokenForUserSuccess',
  props<{ data: ApiResponse, otpSent: boolean }>()
)
export const doValidateTokenForUser = createAction(
  '[Auth] doValidateTokenForUser',
  props<{ payload: ValidateTokenRequestDto }>()
)
export const doValidateTokenForUserSuccess = createAction(
  '[Auth] doValidateTokenForUserSuccess',
  props<{ data: ApiResponse, validated: boolean }>()
)

export const loadLogoutSuccess = createAction(
  '[Auth] Load loadLogoutSuccess',
  props<{ data: ApiResponse }>()
);
export const loadLogoutFailure = createAction(
  '[Auth] Load loadLogoutFailure',
  props<{ error: ApiResponse }>()
);

export const loadResetAuths = createAction(
  '[Auth] loadResetAuths',
  props<{ payload: LoginCredentials, redirectUrl: string }>()
);
export const loadAuths = createAction(
  '[Auth] Load Auths',
  props<{ payload: LoginCredentials, redirectUrl: string }>()
);

export const loadAuthsSuccess = createAction(
  '[Auth] Load Auths Success',
  props<{ profile: LoggedInUser, loggedIn: boolean; }>()
);
export const loadResetAuthsSuccess = createAction(
  '[Auth] Load loadResetAuths Success',
  props<{ profile: LoggedInUser, loggedIn: boolean; }>()
);

export const loadAuthsFailure = createAction(
  '[Auth] Load Auths Failure',
  props<{ error: ApiResponse }>()
);
export const loadResetAuthsFailure = createAction(
  '[Auth] loadResetAuths Failure',
  props<{ error: ApiResponse }>()
);
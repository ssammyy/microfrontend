import {Action, createReducer, on} from '@ngrx/store';
import {
  loadBranchIdSuccess,
  loadCompanyIdFailure,
  loadCompanyIdSuccess,
  loadDirectorIdSuccess,
  loadUserIdSuccess
} from './companies.actions';
import {Branches} from './branch';
import {Company} from './company';


export const companyIdFeatureKey = 'companyId';
export const userIdFeatureKey = 'userId';
export const branchIdFeatureKey = 'branchId';
export const directorIdFeatureKey = 'directorId';

export interface CompanyIdState {
  companyId: number;
  company?: Company;
}

export interface UserIdState {
  userId: number;
}

export interface BranchIdState {
  branchId: number;
  branch?: Branches;
}

export interface DirectorIdState {
  directorId: number;
}

export const initialCompanyIdState: CompanyIdState = {
  companyId: -1, company: undefined
};
export const initialUserIdState: UserIdState = {
  userId: -1
};
export const initialBranchIdState: BranchIdState = {
  branchId: -1, branch: undefined
};
export const initialDirectorIdState: DirectorIdState = {
  directorId: -1
};


export const internalCompanyIdReducer = createReducer(
  initialCompanyIdState,
  on(loadCompanyIdSuccess, (state, {companyId, company}) => {
    return {
      ...state,
      companyId,
      company
    };
  }),
  on(loadCompanyIdFailure, (state, {error}) => {
    return {
      ...state,
      error
    };
  })
);

const internalUserIdReducer = createReducer(
  initialUserIdState,
  on(loadUserIdSuccess, (state, {userId}) => {
    return {
      ...state,
      userId
    };
  }),
  on(loadCompanyIdFailure, (state, {error}) => {
    return {
      ...state,
      error
    };
  })
);


export const internalBranchIdReducer = createReducer(
  initialBranchIdState,
  on(loadBranchIdSuccess, (state, {branchId, branch}) => {
    return {
      ...state,
      branchId,
      branch
    };
  }),
  on(loadCompanyIdFailure, (state, {error}) => {
    return {
      ...state,
      error
    };
  })
);

export const internalDirectorIdReducer = createReducer(
  initialDirectorIdState,
  on(loadDirectorIdSuccess, (state, {directorId}) => {
    return {
      ...state,
      directorId
    };
  }),
  on(loadCompanyIdFailure, (state, {error}) => {
    return {
      ...state,
      error
    };
  })
);


export function companyIdReducer(state: CompanyIdState | undefined, action: Action): CompanyIdState {
  return internalCompanyIdReducer(state, action);
}

export function branchIdReducer(state: BranchIdState | undefined, action: Action): BranchIdState {
  return internalBranchIdReducer(state, action);
}

export function userIdReducer(state: UserIdState | undefined, action: Action): UserIdState {
  return internalUserIdReducer(state, action);
}

export function directorIdReducer(state: DirectorIdState | undefined, action: Action): DirectorIdState {
  return internalDirectorIdReducer(state, action);
}

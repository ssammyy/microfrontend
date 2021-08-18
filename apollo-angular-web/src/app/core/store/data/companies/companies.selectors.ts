import {createFeatureSelector, createSelector} from '@ngrx/store';
import {
    branchIdFeatureKey,
    BranchIdState,
    companyIdFeatureKey,
    CompanyIdState,
    directorIdFeatureKey,
    DirectorIdState,
    userIdFeatureKey,
    UserIdState
} from './companies.reducer';


export const companyIdFeatureSelector = createFeatureSelector<CompanyIdState>(companyIdFeatureKey);
export const branchIdFeatureSelector = createFeatureSelector<BranchIdState>(branchIdFeatureKey);
export const directorIdFeatureSelector = createFeatureSelector<DirectorIdState>(directorIdFeatureKey);
export const userIdFeatureSelector = createFeatureSelector<UserIdState>(userIdFeatureKey);

export const selectCompanyIdData = createSelector(
    companyIdFeatureSelector,
    (state: CompanyIdState) => state.companyId
);
export const selectCompanyData = createSelector(
    companyIdFeatureSelector,
    (state: CompanyIdState) => state.company
);

export const selectBranchIdData = createSelector(
    branchIdFeatureSelector,
    (state: BranchIdState) => state.branchId
);
export const selectBranchData = createSelector(
    branchIdFeatureSelector,
    (state: BranchIdState) => state.branch
);
export const selectDirectorIdData = createSelector(
    directorIdFeatureSelector,
    (state: DirectorIdState) => state.directorId
);

export const selectUserIdData = createSelector(
    userIdFeatureSelector,
    (state: UserIdState) => state.userId
);
export const selectUserData = createSelector(
    userIdFeatureSelector,
    (state: UserIdState) => state.user
);

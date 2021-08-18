import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {
    Back,
    Branches,
    Go,
    loadUserId,
    selectBranchData,
    selectBranchIdData,
    selectCompanyIdData,
    User,
    UsersService
} from '../../../../core/store';
import {Store} from '@ngrx/store';
import {Router} from "@angular/router";


@Component({
    selector: 'app-user',
    templateUrl: './user.list.html',
    styles: []
})
export class UserList implements OnInit {


     users$: Observable<User[]>;

    user: User;

    selectedCompany = -1;
    selectedBranch = -1;
    selectedBranches$: Branches | undefined;


    constructor(
        private service: UsersService,
        private store$: Store<any>,
        private router:Router,
    ) {
        this.users$ = service.entities$;
        service.getAll().subscribe();

    }

    ngOnInit(): void {
        this.store$.select(selectCompanyIdData).subscribe((d) => {
            return this.selectedCompany = d;
        });
        this.store$.select(selectBranchIdData).subscribe((d) => {
            return this.selectedBranch = d;
        });
        this.store$.select(selectBranchData).subscribe((d) => {
            return this.selectedBranches$ = d;
        });
    }

    editRecord(record: User) {
        this.store$.dispatch(loadUserId({payload: record.id, user: record}));
        this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'companies/branches/user'}));

    }

    public goBack(): void {
        this.store$.dispatch(Back());
    }
    addBranches() {
        // this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'dashboard/companies/branch'}));

        this.router.navigate(['users/add_users']);
    }

}

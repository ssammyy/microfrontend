import {Component, OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {Observable} from 'rxjs';
import {
  Back,
  Branches,
  BranchesService,
  Company,
  Go,
  loadBranchId,
  loadCompanyId,
  selectCompanyData,
  selectCompanyIdData, selectUserInfo
} from 'src/app/core/store';
import {Store} from '@ngrx/store';
import {Router} from "@angular/router";

@Component({
  selector: 'app-branch',
  templateUrl: './branch.list.html',
  styleUrls: ['../company.component.css'
  ]
})
export class BranchList implements OnInit {

  branches$: Observable<Branches[]>;
  filterName = '';
  stepTwoForm!: FormGroup;
  stepThreeForm!: FormGroup;
  // @ts-ignore
  branch: Branches;
  company$: Company | undefined;
  submitted = false;
  selectedCompany = 0;
  roles: string[];

  constructor(
      private service: BranchesService,
      private store$: Store<any>,
      private router: Router
  ) {
    this.branches$ = service.entities$;
    service.getAll().subscribe();
  }

  ngOnInit(): void {
    this.store$.select(selectCompanyIdData).subscribe((d) => {
      return this.selectedCompany = d;
    });


    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      return this.roles = u.roles;
    });

    this.store$.select(selectCompanyData).subscribe((d) => {
      return this.company$ = d;
    });

  }

  editRecord(record: Branches) {

    this.store$.dispatch(loadBranchId({payload: record.id, branch: record}));
    this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'companies/branch'}));

  }

  get formStepTwoForm(): any {
    return this.stepTwoForm.controls;
  }

  get formStepThreeForm(): any {
    return this.stepThreeForm.controls;
  }

  onClickUsers(record: Branches) {
    this.store$.dispatch(loadCompanyId({payload: record.companyProfileId, company: this?.company$}));
    this.store$.dispatch(loadBranchId({payload: record.id, branch: record}));
    this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'companies/branches/users'}));

  }

  addBranches() {
    // this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'dashboard/companies/branch'}));

    this.router.navigate(['branches/add_branch']);
  }

  public goBack(): void {
    this.store$.dispatch(Back());
  }

  viewRecord(record: Branches) {
    this.store$.dispatch(loadBranchId({payload: record.id, branch: record}));
    this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'companies/view/branch'}));
  }
}

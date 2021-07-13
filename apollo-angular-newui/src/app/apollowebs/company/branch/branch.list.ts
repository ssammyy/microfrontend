import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import {
  Back,
  Branches,
  BranchesService,
  Company, Go, loadBranchId, loadCompanyId,
  selectCompanyData,
  selectCompanyIdData
} from 'src/app/core/store';
import {Store} from '@ngrx/store';

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

  constructor(
      private service: BranchesService,
      private store$: Store<any>,
  ) {
    this.branches$ = service.entities$;
    service.getAll().subscribe();
  }

  ngOnInit(): void {
    this.store$.select(selectCompanyIdData).subscribe((d) => {
      return this.selectedCompany = d;
    });

    this.store$.select(selectCompanyData).subscribe((d) => {
      return this.company$ = d;
    });

  }

  editRecord(record: Branches) {

    this.store$.dispatch(loadBranchId({payload: record.id, branch: record}));
    this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'dashboard/companies/branch'}));

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
    this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'dashboard/companies/branches/users'}));

  }

  public goBack(): void {
    this.store$.dispatch(Back());
  }

}

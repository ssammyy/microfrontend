import {Component, OnInit} from '@angular/core';
import {Observable, Subject} from "rxjs";
import {
  loadResponsesFailure,
  selectBranchIdData,
  selectCompanyIdData,
  User,
  UsersService
} from "../../../../core/store";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";

@Component({
  selector: 'app-users',
  templateUrl: './users.list.html',
  styles: []
})
export class UsersList implements OnInit {
  users$: Observable<User[]>;

  stepOneForm: FormGroup = new FormGroup({});

  // branchSoFar: Partial<User> | undefined;
  // @ts-ignore
  user: User;

  selectedCompany: number = -1;
  selectedBranch: number = -1;

  dtTrigger: Subject<any> = new Subject<any>();
  dtOptions: DataTables.Settings = {
    pagingType: 'full_numbers',
    pageLength: 5,
    paging: true,
    processing: true
  };

  constructor(
    private service: UsersService,
    private store$: Store<any>,
  ) {
    this.users$ = service.entities$;
    service.getAll().subscribe();
  }

  ngOnInit(): void {
    this.stepOneForm = new FormGroup({
      firstName: new FormControl(),
      lastName: new FormControl('', [Validators.required]),
      userName: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required]),
      cellphone: new FormControl('', [Validators.required]),
      otp: new FormControl('',),
      credentials: new FormControl('', [Validators.required]),
      confirmCredentials: new FormControl('', [Validators.required]),
    });

    this.store$.select(selectCompanyIdData).subscribe((d) => {
      return this.selectedCompany = d;
    })
    this.store$.select(selectBranchIdData).subscribe((d) => {
      return this.selectedBranch = d;
    })
  }

  editRecord(record: User) {
    this.stepOneForm.patchValue(record);
    this.user = record;

  }

  onClickStep(valid: boolean) {
    if (valid) {
      this.user = this.stepOneForm.value;
      if (this.user.id === null || this.user.id === undefined) {
        this.user.companyId = this.selectedCompany;
        this.user.plantId = this.selectedBranch;
        this.service.add(this.user);
      } else {
        this.service.update(this.user);
      }

      this.stepOneForm.markAsPristine();
      this.stepOneForm.reset();

    } else {
      this.store$.dispatch(loadResponsesFailure({
        error: {
          payload: 'Some required details are missing, kindly recheck',
          status: 100,
          response: '05'
        }
      }));
    }


  }
}

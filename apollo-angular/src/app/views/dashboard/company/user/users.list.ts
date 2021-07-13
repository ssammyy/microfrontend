import {Component, OnInit} from '@angular/core';
import {Observable, of, Subject} from 'rxjs';
import {
  Branches,
  loadResponsesFailure, loadResponsesSuccess, selectBranchData,
  selectBranchIdData,
  selectCompanyIdData,
  User,
  UsersService
} from '../../../../core/store';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Store} from '@ngrx/store';
import {Location} from '@angular/common';
import {faArrowLeft} from '@fortawesome/free-solid-svg-icons';
import {catchError, first, map} from "rxjs/operators";
import {HttpErrorResponse} from "@angular/common/http";
import {ConfirmedValidator} from "../../../../core/shared/confirmed.validator";

@Component({
  selector: 'app-users',
  templateUrl: './users.list.html',
  styles: []
})
export class UsersList implements OnInit {

  backIcon = faArrowLeft;

  users$: Observable<User[]>;

  stepOneForm: FormGroup = new FormGroup({});

  // branchSoFar: Partial<User> | undefined;
  // @ts-ignore
  user: User;

  selectedCompany: number = -1;
  selectedBranch: number = -1;
  selectedBranches$: Branches | undefined;

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
    private formBuilder: FormBuilder,
    private location: Location
  ) {
    this.users$ = service.entities$;
    service.getAll().subscribe();

  }

  ngOnInit(): void {
    this.stepOneForm = this.formBuilder.group({
      firstName:  ['', Validators.required],
      lastName: ['', Validators.required],
      userName: ['', Validators.required],
      email: ['', Validators.required],
      cellphone: ['', Validators.required],
      // otp: new FormControl('', ),
        credentials: ['', Validators.required],
        confirmCredentials:  ['', [Validators.required]]},
      {validators: ConfirmedValidator('credentials','confirmCredentials')
      });

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
    this.stepOneForm.patchValue(record);
    this.user = record;

  }

  onClickStep(valid: boolean) {
    if (valid) {
      this.user = this.stepOneForm.value;
      if (this.user.id === null || this.user.id === undefined) {
        this.user.companyId = this.selectedCompany;
        this.user.plantId = this.selectedBranch;
        this.service.add(this.user).pipe(map((a) => {
            this.stepOneForm.markAsPristine();
            this.store$.dispatch(loadResponsesSuccess({
              message: {
                response: '00',
                payload: 'Successfully saved',
                status: 200
              }
            }));
            return this.stepOneForm.reset();
            // return of(loadResponsesSuccess({message: {response: '00', payload: 'Successfully saved', status: 200}}));
          }),
          catchError(
            (err: HttpErrorResponse) => {
              return of(loadResponsesFailure({
                error: {
                  payload: err.error,
                  status: err.status,
                  response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                }
              }));
            }));
      } else {
        this.service.update(this.user).pipe(
          map((a) => {
              this.stepOneForm.markAsPristine();
              this.stepOneForm.reset();
            return of(loadResponsesSuccess({message: {response: '00', payload: 'Successfully saved', status: 200}}));
          }),
          catchError(
            (err: HttpErrorResponse) => {
              return of(loadResponsesFailure({
                error: {
                  payload: err.error,
                  status: err.status,
                  response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                }
              }));
            }));
      }



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

  public goBack(): void {
    this.location.back();
  }

  onClickReset() {
    this.stepOneForm.markAsPristine();
    this.stepOneForm.reset();
    this.user = new User();
  }

}

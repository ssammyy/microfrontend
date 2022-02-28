import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import {UserRegister} from '../../../shared/models/user';
import {AdministratorService} from '../../../shared/services/administrator.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AlertService} from '../../../shared/services/alert.service';
import {UserRequestDetailEntityDto, UserRequestEntityDto} from '../../../shared/models/master-data-details';

@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.css']
})
export class UsersListComponent implements OnInit {

  p = 1;
  p2 = 1;
  allUsersData: UserRegister[] = [];
  allUsersRequestData: UserRequestDetailEntityDto[] = [];
  searchFormGroup!: FormGroup;
  searchFormUserRequestGroup!: FormGroup;

  loading = false;
  submitted = false;

  constructor(
    private administratorService: AdministratorService,
    private alertService: AlertService,
    private router: Router,
    private formBuilder: FormBuilder,
    private spinner: NgxSpinnerService,
  ) {
  }

  get formSearch(): any {
    return this.searchFormGroup.controls;
  }

  get formUserRequestSearch(): any {
    return this.searchFormUserRequestGroup.controls;
  }

  ngOnInit(): void {
    this.searchFormGroup = this.formBuilder.group({
      userName: ['', Validators.required],
      lastName: ['', null],
      firstName: ['', null],
      email: ['', null]
    });

    this.searchFormUserRequestGroup = this.formBuilder.group({
      userName: ['', Validators.required],
    });

    this.spinner.show();
    this.administratorService.loadUsers().subscribe(
      (data: any) => {
        this.allUsersData = data;
        console.log(data);
      }
    );

    this.administratorService.loadUsersRequests().subscribe(
      (data: any) => {
        this.allUsersRequestData = data;
        console.log('Test' + data);
      }
    );
  }

  public onSelect(userID: any): any {
    this.router.navigate(['/user-details'], {fragment: userID});
  }

  public onSelectRequest(requestId: any, userID: any, requestName: any): any {
    this.router.navigate(['/user-details', requestId, requestName], {fragment: userID });
  }


  onSubmitSearch(): any {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.searchFormGroup.invalid) {
      return;
    }
    this.loading = true;
    this.administratorService.loadUserSearch(this.searchFormGroup.value).subscribe(
      (data: any) => {
        console.log(data);
        this.allUsersData = data;
        this.p = 1;
      },
      (error: { error: { message: any; }; }) => {
        this.alertService.error(error.error.message, 'Access Denied');
        // this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      });
  }

  onSubmitUserRequestSearch(): any {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.searchFormGroup.invalid) {
      return;
    }
    this.loading = true;
    this.administratorService.loadUserSearch(this.searchFormGroup.value).subscribe(
      (data: any) => {
        console.log(data);
        this.allUsersData = data;
        this.p = 1;
      },
      (error: { error: { message: any; }; }) => {
        this.alertService.error(error.error.message, 'Access Denied');
        // this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      });
  }
}

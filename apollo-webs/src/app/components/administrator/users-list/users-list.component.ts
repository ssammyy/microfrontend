import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import {UserRegister} from '../../../shared/models/user';
import {AdministratorService} from '../../../shared/services/administrator.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AlertService} from '../../../shared/services/alert.service';

@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.css']
})
export class UsersListComponent implements OnInit {

  p = 1;
  allUsersData: UserRegister[] = [];
  searchFormGroup!: FormGroup;

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

  ngOnInit(): void {
    this.searchFormGroup = this.formBuilder.group({
      userName: ['', Validators.required],
      lastName: ['', null],
      firstName: ['', null],
      email: ['', null]
    });

    this.spinner.show();
    this.administratorService.loadUsers().subscribe(
      (data: any) => {
        this.allUsersData = data;
        console.log(data);
      }
    );
  }

  public onSelect(userRegNo: any): any {
    this.router.navigate(['/user-details'], {fragment: userRegNo});
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
}

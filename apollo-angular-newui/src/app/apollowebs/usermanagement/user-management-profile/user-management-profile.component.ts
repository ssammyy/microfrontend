import {Component, OnInit} from '@angular/core';
import {LoadingService} from '../../../core/services/loader/loadingservice.service';
import {NgxSpinnerService} from 'ngx-spinner';
import {UserEntityService} from '../../../core/store';
import {MasterService} from '../../../core/store/data/master/master.service';
import {UserRegister} from '../../../../../../apollo-webs/src/app/shared/models/user';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-user-management-profile',
  templateUrl: './user-management-profile.component.html',
  styleUrls: ['./user-management-profile.component.css']
})
export class UserManagementProfileComponent implements OnInit {

  public userID!: string;
  public userDetails!: UserRegister;

  constructor(
      private route: ActivatedRoute,
      private _loading: LoadingService,
      private SpinnerService: NgxSpinnerService,
      private service: UserEntityService,
      private masterService: MasterService,
  ) {
  }

  ngOnInit(): void {
    this.getSelectedUser();
  }

  private getSelectedUser() {
    this.route.fragment.subscribe(params => {
      this.userID = params;
      console.log(this.userID);
      this.masterService.loadUserDetails(this.userID).subscribe(
          (data: UserRegister) => {
            this.userDetails = data;
            // this.onSelectL1SubSubSection(this.userDetails?.employeeProfile?.l1SubSubSection);

          }
      );
    });
  }
}

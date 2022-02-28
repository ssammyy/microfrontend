import {Component, OnInit} from '@angular/core';
import {faAngleDown, faChartLine, faFile, faReceipt, faUser} from '@fortawesome/free-solid-svg-icons';
import {AccountService} from '../../shared/services/account.service';

@Component({
  selector: 'app-administrator',
  templateUrl: './administrator.component.html',
  styleUrls: ['./administrator.component.css']
})
export class AdministratorComponent implements OnInit {
  dashboardIcon = faChartLine;
  userIcon = faUser;
  reportsIcon = faFile;
  paymentsIcon = faReceipt;
  arrowDownIcon = faAngleDown;

  constructor(
    private accountService: AccountService
  ) {
  }

  ngOnInit(): void {
  }

  logout(): void {
    this.accountService.logout();
    // this.router.navigate(['login']);
  }
}

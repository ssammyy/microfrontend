import { Component, OnInit } from '@angular/core';
import {faChartLine, faFile, faReceipt, faUser} from "@fortawesome/free-solid-svg-icons";
import {AccountService} from "../../shared/services/account.service";

@Component({
  selector: 'app-standards-development',
  templateUrl: './standards-development.component.html',
  styleUrls: ['./standards-development.component.css']
})
export class StandardsDevelopmentComponent implements OnInit {


  dashboardIcon = faChartLine;
  userIcon = faUser;
  reportsIcon = faFile;
  paymentsIcon = faReceipt;

  constructor(
    private accountService: AccountService
  ) { }

  ngOnInit(): void {
  }
  logout(): void {
    this.accountService.logout();
    // this.router.navigate(['login']);
  }
}

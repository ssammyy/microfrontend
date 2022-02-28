import {Component, OnInit} from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {fader} from '../../shared/animations/route-animations';
import {faAngleDown, faChartLine, faFile, faReceipt, faUser} from '@fortawesome/free-solid-svg-icons';
import {User} from "../../shared/models/user";
import {AccountService} from "../../shared/services/account.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  animations: [
    fader,
    // slider
  ]
})
export class HomeComponent implements OnInit {

  dashboardIcon = faChartLine;
  userIcon = faUser;
  reportsIcon = faFile;
  paymentsIcon = faReceipt;
  arrowDownIcon = faAngleDown;
  user?: User;

  constructor(
    private router: Router,
    private accountService: AccountService
  ) {
    // this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
  }

  ngOnInit(): void {

  }

  prepareRoute(outlet: RouterOutlet): void {
    return outlet && outlet.activatedRouteData && outlet.activatedRouteData.animation;
  }

  logout(): void {
    this.accountService.logout();
    // this.router.navigate(['login']);
  }
}

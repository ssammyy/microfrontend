import {Component, OnInit} from '@angular/core';
import {faArrowRight} from '@fortawesome/free-solid-svg-icons/faArrowRight';
import {NgxSpinnerService} from 'ngx-spinner';
import {MarketSurveillanceService} from '../../../shared/services/market-surveillance.service';
import {User} from '../../../shared/models/user';
import {AccountService} from '../../../shared/services/account.service';


@Component({
  selector: 'app-ms-home',
  templateUrl: './ms-home.component.html',
  styleUrls: ['./ms-home.component.css']
})
export class MsHomeComponent implements OnInit {

  arrowRightIcon = faArrowRight;
  msTypes = [];
  user?: User;

  // fullName = localStorage.getItem(fullName);
  constructor(
    private marketSurveillanceService: MarketSurveillanceService,
    private spinner: NgxSpinnerService,
    private accountService: AccountService
  ) {
    this.user = this.accountService.userValue;
  }

  ngOnInit(): void {
    this.spinner.show();
    // this.marketSurveillanceService.loadMSTypes().subscribe(
    //     (data: any) => {
    //       this.msTypes = data;
    //       console.log(data);
    //   }
    // );
  }


}

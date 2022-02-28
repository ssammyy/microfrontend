import {Component, OnInit} from '@angular/core';
import {Complaints} from '../../../shared/models/complaints';
import {MarketSurveillanceService} from '../../../shared/services/market-surveillance.service';
import {NgxSpinnerService} from 'ngx-spinner';
import {Router} from '@angular/router';

@Component({
  selector: 'app-access-control',
  templateUrl: './access-control.component.html',
  styleUrls: ['./access-control.component.css']
})
export class AccessControlComponent implements OnInit {


  allComplaintsData: Complaints[] = [];

  constructor(
    private marketSurveillanceService: MarketSurveillanceService,
    private router: Router,
    private spinner: NgxSpinnerService,
  ) {
  }

  ngOnInit(): void {
    this.spinner.show();
    this.marketSurveillanceService.loadMSComplaintList().subscribe(
      (data: any) => {
        this.allComplaintsData = data;
        console.log(data);
      }
    );
  }

  public onSelect(refNumber: string): any {
    this.router.navigate(['/complaint-details'], {fragment: refNumber});
  }

}

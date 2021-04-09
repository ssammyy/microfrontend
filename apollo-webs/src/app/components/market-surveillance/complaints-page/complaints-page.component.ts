import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import {MarketSurveillanceService} from '../../../shared/services/market-surveillance.service';
import {Complaints} from '../../../shared/models/complaints';

@Component({
  selector: 'app-complaints-page',
  templateUrl: './complaints-page.component.html',
  styleUrls: ['./complaints-page.component.css']
})
export class ComplaintsPageComponent implements OnInit {


  allComplaintsData: Complaints[] = [];
  p = 1;

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

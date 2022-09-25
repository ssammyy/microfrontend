import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {PaymentDetails} from "../../../core/store/data/levy/levy.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-standard-levy-penalty-report',
  templateUrl: './standard-levy-penalty-report.component.html',
  styleUrls: ['./standard-levy-penalty-report.component.css']
})
export class StandardLevyPenaltyReportComponent implements OnInit {
  paymentDetails: PaymentDetails[]=[];
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger1: Subject<any> = new Subject<any>();
  loadingText: string;
  constructor(
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private levyService: LevyService,
  ) { }

  ngOnInit(): void {
    this.getPenaltyReport();
  }
  ngOnDestroy(): void {
    this.dtTrigger1.unsubscribe();
  }
  public getPenaltyReport(): void{
    this.loadingText='Loading Penalty Report...';
    this.SpinnerService.show();
    this.levyService.getPenaltyReport().subscribe(
        (response: PaymentDetails[])=> {
          this.paymentDetails = response;
          console.log(this.paymentDetails);
          this.SpinnerService.hide();
          this.rerender();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  rerender(): void {
    this.dtElements.forEach((dtElement: DataTableDirective) => {
      if (dtElement.dtInstance)
        dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
          dtInstance.destroy();
        });
    });
    setTimeout(() => {
      this.dtTrigger1.next();
    });

  }

}

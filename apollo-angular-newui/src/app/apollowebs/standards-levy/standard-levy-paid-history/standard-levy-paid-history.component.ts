import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {DataTableDirective} from "angular-datatables";
import {PaymentDetails, PenaltyDetails} from "../../../core/store/data/levy/levy.model";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-standard-levy-paid-history',
  templateUrl: './standard-levy-paid-history.component.html',
  styleUrls: ['./standard-levy-paid-history.component.css']
})
export class StandardLevyPaidHistoryComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger1: Subject<any> = new Subject<any>();
  p = 1;
  p2 = 1;
  tasks: PaymentDetails[] = [];
  constructor(
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private levyService: LevyService
  ) { }

  ngOnInit(): void {
    this.getManufacturesPayments();
  }
  ngOnDestroy(): void {
    this.dtTrigger1.unsubscribe();
  }

  public getManufacturesPayments(): void{
    this.SpinnerService.show();
    this.levyService.getManufacturesPayments().subscribe(
        (response: PaymentDetails[])=> {
          this.tasks = response;
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

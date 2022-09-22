import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {CompanyModel, PaymentDetails} from "../../../core/store/data/levy/levy.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-standard-levy-all-payments',
  templateUrl: './standard-levy-all-payments.component.html',
  styleUrls: ['./standard-levy-all-payments.component.css']
})
export class StandardLevyAllPaymentsComponent implements OnInit {

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
    this.getAllLevyPayments();
  }
  ngOnDestroy(): void {
    this.dtTrigger1.unsubscribe();
  }
  public getAllLevyPayments(): void{
    this.loadingText = "Loading ...."
    this.SpinnerService.show();
    this.levyService.getAllLevyPayments().subscribe(
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

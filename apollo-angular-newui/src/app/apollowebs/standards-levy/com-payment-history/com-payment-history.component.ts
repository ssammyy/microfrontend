import {Component, OnInit, ViewChild} from '@angular/core';
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {PaidLevy, PaymentDetails} from "../../../core/store/data/levy/levy.model";
import {DataTableDirective} from "angular-datatables";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-com-payment-history',
  templateUrl: './com-payment-history.component.html',
  styleUrls: ['./com-payment-history.component.css']
})
export class ComPaymentHistoryComponent implements OnInit {
  tasks: PaidLevy[] = [];
  paymentDetails: PaymentDetails[] = [];
  public actionRequest: PaidLevy | undefined;

  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;

  dtTrigger: Subject<any> = new Subject<any>();
  isDtInitialized: boolean = false
  loadingText: string;
  constructor(
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private levyService: LevyService,
  ) { }

  ngOnInit(): void {
    this.getManufacturesLevyPayments();
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  public getManufacturesLevyPayments(): void{
    this.loadingText = "Retrieving Payment History ...."
    this.SpinnerService.show();
    this.levyService.getManufacturesLevyPayments().subscribe(
        (response: PaymentDetails[]) => {
          this.paymentDetails = response;
          console.log(this.paymentDetails);
          this.SpinnerService.hide();
          if (this.isDtInitialized) {
            this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
              dtInstance.destroy();
              this.dtTrigger.next();
            });
          } else {
            this.isDtInitialized = true
            this.dtTrigger.next();
          }
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);

        }
    );
  }
}

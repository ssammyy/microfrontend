import {Component, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {DefaulterDetails, PaymentDetails} from "../../../core/store/data/levy/levy.model";
import {DataTableDirective} from "angular-datatables";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-standard-levy-defaulter',
  templateUrl: './standard-levy-defaulter.component.html',
  styleUrls: ['./standard-levy-defaulter.component.css']
})
export class StandardLevyDefaulterComponent implements OnInit {

  defaulterDetails: PaymentDetails[]=[];
    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
  isDtInitialized: boolean = false
  loadingText: string;
    public actionRequest: PaymentDetails | undefined;
    paidDetails: PaymentDetails[] = [];
  constructor(
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private levyService: LevyService,
  ) { }

  ngOnInit(): void {
    this.getLevyDefaulters();
  }

  public getLevyDefaulters(): void{
    this.loadingText = "Retrieving Defaulters ...."
    this.SpinnerService.show();
    this.levyService.getLevyDefaulters().subscribe(
        (response: PaymentDetails[]) => {
          this.defaulterDetails = response;
          console.log(this.defaulterDetails);
          this.SpinnerService.hide();
            this.rerender();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);

        }
    );
  }
  ngOnDestroy(): void {
    this.dtTrigger1.unsubscribe();
    this.dtTrigger2.unsubscribe();
  }

    public onOpenModalPayment(defaulterDetail:PaymentDetails ,mode: string,companyId: number): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');

        if (mode === 'defaulterDetails') {
            this.actionRequest = defaulterDetail;
            button.setAttribute('data-target', '#defaulterDetails');
            this.loadingText = "Loading ...."
            this.SpinnerService.show();
            this.levyService.getManufacturesLevyPaymentsList(companyId).subscribe(
                (response: PaymentDetails[]) => {
                    this.paidDetails = response;
                    this.SpinnerService.hide();
                    console.log(this.paidDetails);
                    this.rerender();
                },
                (error: HttpErrorResponse) => {
                    this.SpinnerService.hide();
                    console.log(error.message);
                }
            );

        }

        // @ts-ignore
        container.appendChild(button);
        button.click();

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
            this.dtTrigger2.next();
        });

    }


}

import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {PaymentDetails, PenaltyDetails} from "../../../core/store/data/levy/levy.model";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-std-levy-manufacturer-penalty',
  templateUrl: './std-levy-manufacturer-penalty.component.html',
  styleUrls: ['./std-levy-manufacturer-penalty.component.css']
})
export class StdLevyManufacturerPenaltyComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger1: Subject<any> = new Subject<any>();
  p = 1;
  p2 = 1;
  tasks: PenaltyDetails[] = [];

  constructor(
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private levyService: LevyService
  ) { }

  ngOnInit(): void {
    this.getManufacturesLevyPenalty();
  }
  ngOnDestroy(): void {
    this.dtTrigger1.unsubscribe();
  }
  public getManufacturesLevyPenalty(): void{
    this.SpinnerService.show();
    this.levyService.getManufacturesLevyPenalty().subscribe(
        (response: PenaltyDetails[])=> {
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

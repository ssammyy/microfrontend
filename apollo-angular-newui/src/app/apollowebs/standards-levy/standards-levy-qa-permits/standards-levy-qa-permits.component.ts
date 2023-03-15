import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {ReportsPermitEntityDto} from "../../../core/store/data/qa/qa.model";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {PenaltyDetails} from "../../../core/store/data/levy/levy.model";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-standards-levy-qa-permits',
  templateUrl: './standards-levy-qa-permits.component.html',
  styleUrls: ['./standards-levy-qa-permits.component.css']
})
export class StandardsLevyQaPermitsComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger1: Subject<any> = new Subject<any>();
  public allSMarkPermitDatas: ReportsPermitEntityDto[] = [];
  loadingText: string;

  constructor(
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private levyService: LevyService,
  ) { }

  ngOnInit(): void {
    this.loadPermitGrantedReports();
  }

  ngOnDestroy(): void {
    this.dtTrigger1.unsubscribe();
  }

  public loadPermitGrantedReports(): void{
    this.loadingText = "Retrieving Data ...."
    this.SpinnerService.show();
    this.levyService.loadPermitGrantedReports().subscribe(
        (response: ReportsPermitEntityDto[]) => {
          this.allSMarkPermitDatas = response;
          console.log(this.allSMarkPermitDatas);
          this.SpinnerService.hide();
          this.rerender();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);

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

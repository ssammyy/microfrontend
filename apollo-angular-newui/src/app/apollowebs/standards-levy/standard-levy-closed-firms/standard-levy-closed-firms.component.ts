import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {CompanyModel} from "../../../core/store/data/levy/levy.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-standard-levy-closed-firms',
  templateUrl: './standard-levy-closed-firms.component.html',
  styleUrls: ['./standard-levy-closed-firms.component.css']
})
export class StandardLevyClosedFirmsComponent implements OnInit {

  companyModels: CompanyModel[]=[];
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
    this.dtOptions = {
      processing: true,
      dom: 'Bfrtip'
    };
    this.getClosedFirms();
  }
  ngOnDestroy(): void {
    this.dtTrigger1.unsubscribe();
  }
  public getClosedFirms(): void{
    this.loadingText='Loading Closed Firms...';
    this.SpinnerService.show();
    this.levyService.getClosedFirms().subscribe(
        (response: CompanyModel[])=> {
          this.companyModels = response;
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

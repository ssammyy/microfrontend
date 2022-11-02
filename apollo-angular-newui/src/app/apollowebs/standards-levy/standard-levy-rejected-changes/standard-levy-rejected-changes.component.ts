import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {PenaltyDetails, RejectedComDetails, SiteVisitRemarks} from "../../../core/store/data/levy/levy.model";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-standard-levy-rejected-changes',
  templateUrl: './standard-levy-rejected-changes.component.html',
  styleUrls: ['./standard-levy-rejected-changes.component.css']
})
export class StandardLevyRejectedChangesComponent implements OnInit {

  siteVisitRemarks: SiteVisitRemarks[] = [];
  rejectedChanges: RejectedComDetails[]=[];
  public actionRequest: RejectedComDetails | undefined;
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger1: Subject<any> = new Subject<any>();
  loadingText: string;
  public isShowRemarksTab= true;
  constructor(
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private levyService: LevyService
  ) { }

  ngOnInit(): void {
    this.getRejectedCompanyDetails();
  }
  ngOnDestroy(): void {
    this.dtTrigger1.unsubscribe();
  }

  public getRejectedCompanyDetails(): void{
    this.loadingText='Loading ...';
    this.SpinnerService.show();
    this.levyService.getRejectedCompanyDetails().subscribe(
        (response: RejectedComDetails[])=> {
          this.rejectedChanges = response;
          this.SpinnerService.hide();
          this.rerender();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  toggleDisplayCompanyRemarks(editID: number){
    this.loadingText = "Loading ...."
    this.SpinnerService.show();
    this.levyService.getComEditRemarks(editID).subscribe(
        (response: SiteVisitRemarks[]) => {
          this.siteVisitRemarks = response;
          this.SpinnerService.hide();
          console.log(this.siteVisitRemarks)
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
    this.isShowRemarksTab=!this.isShowRemarksTab;

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

  public onOpenModalList(rejectedChange:RejectedComDetails ,mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');

    if (mode === 'viewList') {
      this.actionRequest = rejectedChange;
      button.setAttribute('data-target', '#viewList');


    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  exportExcel(): void {
    this.levyService.exportExcel('ExampleTable');
  }

}

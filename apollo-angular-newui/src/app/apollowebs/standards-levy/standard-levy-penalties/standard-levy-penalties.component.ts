import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {
  PenaltyDetails
} from "../../../core/store/data/levy/levy.model";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";
import {DataTableDirective} from "angular-datatables";

@Component({
  selector: 'app-standard-levy-penalties',
  templateUrl: './standard-levy-penalties.component.html',
  styleUrls: ['./standard-levy-penalties.component.css']
})
export class StandardLevyPenaltiesComponent implements OnInit {
  penaltyDetails: PenaltyDetails[] = [];
  penaltiesDetails: PenaltyDetails[] = [];
  public actionRequest: PenaltyDetails | undefined;
  public pdfRequest: PenaltyDetails | undefined;
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger1: Subject<any> = new Subject<any>();
  dtTrigger2: Subject<any> = new Subject<any>();

  isDtInitialized: boolean = false
  loadingText: string;
  constructor(
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private levyService: LevyService,
  ) { }

  ngOnInit(): void {
    this.getLevyPenalty();
  }

  ngOnDestroy(): void {
    this.dtTrigger1.unsubscribe();
    this.dtTrigger2.unsubscribe();
  }



  public getLevyPenalty(): void{
    this.loadingText = "Retrieving Penalties ...."
    this.SpinnerService.show();
    this.levyService.getLevyPenalty().subscribe(
        (response: PenaltyDetails[]) => {
          this.penaltyDetails = response;
          console.log(this.penaltyDetails);
          this.SpinnerService.hide();
          this.rerender();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);

        }
    );
  }



  public onOpenModalPenalty(penaltyDetail:PenaltyDetails ,mode: string,companyId: number): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');

    if (mode === 'penaltyDetails') {
      this.actionRequest = penaltyDetail;
      button.setAttribute('data-target', '#penaltyDetails');
      this.loadingText = "Loading ...."
      this.SpinnerService.show();
      this.levyService.getManufacturesLevyPenaltyList(companyId).subscribe(
          (response: PenaltyDetails[]) => {
            this.penaltiesDetails = response;
            this.SpinnerService.hide();
            console.log(this.penaltiesDetails);
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

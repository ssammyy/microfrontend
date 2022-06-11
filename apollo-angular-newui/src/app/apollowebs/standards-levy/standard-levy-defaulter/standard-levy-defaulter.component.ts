import {Component, OnInit, ViewChild} from '@angular/core';
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {DefaulterDetails} from "../../../core/store/data/levy/levy.model";
import {DataTableDirective} from "angular-datatables";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-standard-levy-defaulter',
  templateUrl: './standard-levy-defaulter.component.html',
  styleUrls: ['./standard-levy-defaulter.component.css']
})
export class StandardLevyDefaulterComponent implements OnInit {

  defaulterDetails: DefaulterDetails[]=[];
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
    this.getLevyDefaulters();
  }

  public getLevyDefaulters(): void{
    this.loadingText = "Retrieving Defaulters ...."
    this.SpinnerService.show();
    this.levyService.getLevyDefaulters().subscribe(
        (response: DefaulterDetails[]) => {
          this.defaulterDetails = response;
          console.log(this.defaulterDetails);
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
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

}

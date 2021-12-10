import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {ManufacturePenalty} from "../../../core/store/data/levy/levy.model";
import {HttpErrorResponse} from "@angular/common/http";
import {LevyService} from "../../../core/store/data/levy/levy.service";

@Component({
  selector: 'app-standard-levy-penalty-history',
  templateUrl: './standard-levy-penalty-history.component.html',
  styleUrls: ['./standard-levy-penalty-history.component.css']
})
export class StandardLevyPenaltyHistoryComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  p = 1;
  p2 = 1;
  tasks: ManufacturePenalty[] = [];
  public actionRequest: ManufacturePenalty | undefined;
  constructor(
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private levyService: LevyService,
  ) { }

  ngOnInit(): void {
    this.getManufacturerPenaltyHistory();
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  public getManufacturerPenaltyHistory(): void{
    this.SpinnerService.show();
    this.levyService.getManufacturerPenaltyHistory().subscribe(
        (response: ManufacturePenalty[])=> {
          this.tasks = response;
          this.dtTrigger.next();
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

}

import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {ManufacturePenalty} from "../../../core/store/data/levy/levy.model";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-standard-levy-penalties',
  templateUrl: './standard-levy-penalties.component.html',
  styleUrls: ['./standard-levy-penalties.component.css']
})
export class StandardLevyPenaltiesComponent implements OnInit {
  title = 'datatables';
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  posts;
  tasks: ManufacturePenalty[] = [];
  public actionRequest: ManufacturePenalty | undefined;
  constructor(
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private levyService: LevyService,
  ) { }

  ngOnInit(): void {
    this.getManufacturerPenaltyHistory();
    this.dtOptions = {
      pagingType: 'full_numbers',
      pageLength: 10,
      processing: true
    };
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

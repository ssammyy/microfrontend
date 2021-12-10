import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import { PaidLevy} from "../../../core/store/data/levy/levy.model";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-standard-levy-paid',
  templateUrl: './standard-levy-paid.component.html',
  styleUrls: ['./standard-levy-paid.component.css']
})
export class StandardLevyPaidComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: PaidLevy[] = [];
  public actionRequest: PaidLevy | undefined;
  constructor(
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private levyService: LevyService,
  ) { }

  ngOnInit(): void {
    this.getPaidLevies();
    this.dtOptions = {
      pagingType: 'full_numbers',
      pageLength: 10,
      processing: true
    };
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
  public getPaidLevies(): void{
    this.SpinnerService.show();
    this.levyService.getPaidLevies().subscribe(
        (response: PaidLevy[])=> {
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

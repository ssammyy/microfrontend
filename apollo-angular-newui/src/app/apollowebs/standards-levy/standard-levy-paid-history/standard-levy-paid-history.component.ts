import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";

@Component({
  selector: 'app-standard-levy-paid-history',
  templateUrl: './standard-levy-paid-history.component.html',
  styleUrls: ['./standard-levy-paid-history.component.css']
})
export class StandardLevyPaidHistoryComponent implements OnInit {

  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  constructor(
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

}

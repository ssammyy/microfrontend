import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";

@Component({
  selector: 'app-standard-levy-defaulter-history',
  templateUrl: './standard-levy-defaulter-history.component.html',
  styleUrls: ['./standard-levy-defaulter-history.component.css']
})
export class StandardLevyDefaulterHistoryComponent implements OnInit {

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

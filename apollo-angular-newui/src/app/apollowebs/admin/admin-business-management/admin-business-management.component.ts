import {Component, OnInit, ViewChild} from '@angular/core';
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {HttpErrorResponse} from "@angular/common/http";
import {PublishingService} from "../../../core/store/data/std/publishing.service";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {BusinessLinesService} from "../../../core/store/data/admin/business-lines.service";
import {BusinessLines, BusinessNatures} from "../../../core/store";
import {BusinessNatureService} from "../../../core/store/data/admin/business-nature.service";

@Component({
  selector: 'app-admin-business-management',
  templateUrl: './admin-business-management.component.html',
  styleUrls: ['./admin-business-management.component.css']
})
export class AdminBusinessManagementComponent implements OnInit {
  tasks: BusinessLines[] = [];
  tasksB: BusinessNatures[] = [];

  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false
  loadingText: string;


  dtOptionsB: DataTables.Settings = {};
  dtTriggerB: Subject<any> = new Subject<any>();
  @ViewChild(DataTableDirective, {static: false})
  dtElementB: DataTableDirective;
  isDtInitializedB: boolean = false


  constructor(private publishingService: PublishingService, private notifyService: NotificationService,
              private SpinnerService: NgxSpinnerService, private businessLineService: BusinessLinesService, private businessNatureService: BusinessNatureService) {
  }

  ngOnInit(): void {
    this.getBusinessLines();
    this.getBusinessNature();

  }

  public getBusinessLines() {
    this.loadingText = "Retrieving Data Please Wait ...."
    this.SpinnerService.show();
    this.businessLineService.getAllBusinessLines().subscribe(
        (response: BusinessLines[]) => {
          this.tasks = response;
          console.log(response)
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
          alert(error.message);
          this.SpinnerService.hide();
        }
    );
  }

  public getBusinessNature() {
    this.loadingText = "Retrieving Data Please Wait ...."
    this.SpinnerService.show();
    this.businessNatureService.getAllBusinessNature().subscribe(
        (response: BusinessNatures[]) => {
          this.tasksB = response;
          console.log(response)
          this.SpinnerService.hide();

          if (this.isDtInitializedB) {
            this.dtElementB.dtInstance.then((dtInstance: DataTables.Api) => {
              dtInstance.destroy();
              this.dtTriggerB.next();
            });
          } else {
            this.isDtInitializedB = true
            this.dtTriggerB.next();
          }
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
          this.SpinnerService.hide();
        }
    );
  }
}

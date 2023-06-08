import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ReviewApplicationTask} from "../../../core/store/data/std/request_std.model";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {MembershipToTcService} from "../../../core/store/data/std/membership-to-tc.service";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {NgForm} from "@angular/forms";
import {QaService} from "../../../core/store/data/qa/qa.service";
import {Payments} from "../../../core/store/data/qa/qa.model";
import * as moment from 'moment';

@Component({
  selector: 'app-payments',
  templateUrl: './payments.component.html',
  styleUrls: ['./payments.component.css']
})
export class PaymentsComponent implements OnInit {

  p = 1;
  p2 = 1;
  public tcTasks: Payments[] = [];
  public actionRequest: ReviewApplicationTask | undefined;
  loadingText: string;
  public uploadedFiles: FileList;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false
  blob: Blob;
  display = 'none'; //default Variable


  constructor(        private qaService: QaService,
       private notifyService: NotificationService,
              private SpinnerService: NgxSpinnerService) {
  }

  ngOnInit(): void {
    this.getApplicationsForReview();

  }


  public getApplicationsForReview(): void {
    this.loadingText = "Retrieving All Payments Please Wait ...."
    this.SpinnerService.show();
    this.qaService.loadAllPayments().subscribe(
        (response: Payments[]) => {
          console.log(response);
          this.tcTasks = response;
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
          alert(error.message);
        }
    )
  }

  public onOpenModal(task: ReviewApplicationTask): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    console.log(task.taskId);
    this.actionRequest = task;
    button.setAttribute('data-target', '#decisionModal');

    // @ts-ignore
    container.appendChild(button);
    button.click();
  }

  @ViewChild('closeViewModal') private closeModal: ElementRef;

  public hideModel() {
    this.closeModal?.nativeElement.click();
  }

  @ViewChild('editForm') private mytemplateForm: NgForm;
  pdfSrc: any;

  public clearForm() {
    this.mytemplateForm?.resetForm();
  }

  showToasterError(title: string, message: string) {
    this.notifyService.showError(message, title)

  }
    public transform(value: string, format: string = 'MMM D, YYYY HH:MM A'): string {
        return value ? moment.utc(value).format(format) : 'N/A';
    }

}

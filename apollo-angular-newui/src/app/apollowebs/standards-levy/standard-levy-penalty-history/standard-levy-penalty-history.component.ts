import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {ManufacturePenalty} from "../../../core/store/data/levy/levy.model";
import {HttpErrorResponse} from "@angular/common/http";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {KnwSecTasks} from "../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DataTableDirective} from "angular-datatables";
declare const $: any;

@Component({
  selector: 'app-standard-levy-penalty-history',
  templateUrl: './standard-levy-penalty-history.component.html',
  styleUrls: ['./standard-levy-penalty-history.component.css']
})
export class StandardLevyPenaltyHistoryComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger1: Subject<any> = new Subject<any>();
  dtTrigger2: Subject<any> = new Subject<any>();
  p = 1;
  p2 = 1;
  tasks: ManufacturePenalty[] = [];
  public actionRequest: ManufacturePenalty | undefined;
  public scheduleVisitFormGroup!: FormGroup;
  constructor(
      private formBuilder: FormBuilder,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private levyService: LevyService,
  ) { }

  ngOnInit(): void {
    this.getManufacturerPenaltyHistory();

    this.scheduleVisitFormGroup = this.formBuilder.group({
      scheduledVisitDate: ['', Validators.required],
      manufacturerEntity: []

    });
  }
  get scheduleVisitForm(): any {
    return this.scheduleVisitFormGroup.controls;
  }
  ngOnDestroy(): void {
    this.dtTrigger1.unsubscribe();
    this.dtTrigger2.unsubscribe();
  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }

  public getManufacturerPenaltyHistory(): void{
    this.SpinnerService.show();
    this.levyService.getManufacturerPenaltyHistory().subscribe(
        (response: ManufacturePenalty[])=> {
          this.tasks = response;
          this.rerender();
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  scheduleVisit(): void {

    this.SpinnerService.show();
    this.levyService.scheduleSiteVisit(this.scheduleVisitFormGroup.value).subscribe(
        (response ) => {
          console.log(response);

          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Visit Scheduled`);
          this.scheduleVisitFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Scheduling Visit`);
          console.log(error.message);
        }
    );
  }

  public onOpenModal(task: ManufacturePenalty,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='schedule'){
      this.actionRequest=task;
      button.setAttribute('data-target','#schedule');
    }


    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  showNotification(from: any, align: any) {
    const type = ['', 'info', 'success', 'warning', 'danger', 'rose', 'primary'];

    const color = Math.floor((Math.random() * 6) + 1);

    $.notify({
      icon: 'notifications',
      message: 'Welcome to <b>Material Dashboard</b> - a beautiful dashboard for every web developer.'
    }, {
      type: type[color],
      timer: 3000,
      placement: {
        from: from,
        align: align
      },
      template: '<div data-notify="container" class="col-xs-11 col-sm-3 alert alert-{0} alert-with-icon" role="alert">' +
          '<button mat-raised-button type="button" aria-hidden="true" class="close" data-notify="dismiss">  <i class="material-icons">close</i></button>' +
          '<i class="material-icons" data-notify="icon">notifications</i> ' +
          '<span data-notify="title"></span> ' +
          '<span data-notify="message">Ensure all required fields and items have been filled</span>' +
          '<div class="progress" data-notify="progressbar">' +
          '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" ></div>' +
          '</div>' +
          '<a href="{3}" target="{4}" data-notify="url"></a>' +
          '</div>'
    });
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

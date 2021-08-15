import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgxSpinnerService} from "ngx-spinner";
import {Subject} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {ComHodTasks, ComStdAction, UsersEntity} from "../../../../core/store/data/std/std.model";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";

@Component({
  selector: 'app-com-std-request-list',
  templateUrl: './com-std-request-list.component.html',
  styleUrls: ['./com-std-request-list.component.css']
})
export class ComStdRequestListComponent implements OnInit,OnDestroy {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  public users !: UsersEntity[] ;
  tasks: ComHodTasks[] = [];
  public actionRequest: ComHodTasks | undefined;
  constructor(
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getHODTasks();
    this.getUserList();
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
  public getHODTasks(): void{
    this.SpinnerService.show();
    this.stdComStandardService.getHODTasks().subscribe(
        (response: ComHodTasks[])=> {
          this.SpinnerService.hide();
          this.tasks = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  public onOpenModal(task: ComHodTasks,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='approve'){
      this.actionRequest=task;
      button.setAttribute('data-target','#assignModal');
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  //Assign Project Leader
  public onAssign(comStdAction: ComStdAction): void{
    this.SpinnerService.show();
    this.stdComStandardService.assignRequest(comStdAction).subscribe(
        (response: ComStdAction) => {
          this.SpinnerService.hide();
          console.log(response);
          this.getHODTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  public getUserList(): void{
    this.SpinnerService.show();
    this.stdComStandardService.getUserList().subscribe(
        (response: UsersEntity[])=> {
          this.SpinnerService.hide();
          this.users = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

}

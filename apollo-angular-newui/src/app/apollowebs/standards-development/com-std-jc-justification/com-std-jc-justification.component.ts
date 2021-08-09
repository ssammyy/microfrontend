import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {StdComStandardService} from "../../../core/store/data/std/std-com-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import Swal from "sweetalert2";
import {ComJcJustification, ComJcJustificationAction} from "../../../core/store/data/std/std.model";
import {User, UsersService} from "../../../core/store";
import {HttpErrorResponse} from "@angular/common/http";


@Component({
  selector: 'app-com-std-jc-justification',
  templateUrl: './com-std-jc-justification.component.html',
  styleUrls: ['./com-std-jc-justification.component.css']
})
export class ComStdJcJustificationComponent implements OnInit, OnDestroy {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ComJcJustification[] = [];
  public actionRequest: ComJcJustification | undefined;
  user?: User;
  constructor(
     // private usersService: UsersService,
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) {
    //this.user = this.usersService.userValue;
  }

  ngOnInit(): void {
    this.getPlTasks();
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
  public getPlTasks(): void{
    this.SpinnerService.show();
    this.stdComStandardService.getPlTasks().subscribe(
        (response: ComJcJustification[])=> {
          this.SpinnerService.hide();
          this.tasks = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  public onOpenModal(task: ComJcJustification,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='prepJcJustification'){
      this.actionRequest=task;
      button.setAttribute('data-target','#prepJcJustification');
    }
    if (mode==='prepPd'){
      this.actionRequest=task;
      button.setAttribute('data-target','#prepPd');
    }
    if (mode==='approvePd'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approvePd');
    }

    if (mode==='reject'){
      this.actionRequest=task;
      button.setAttribute('data-target','#reject');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  public saveJustification(comJcJustificationAction: ComJcJustificationAction): void{
    this.SpinnerService.show();
    this.stdComStandardService.prepareJustification(comJcJustificationAction).subscribe(
        (response: ComJcJustificationAction) => {
          this.SpinnerService.hide();
          Swal.fire('Thank you...', 'Justification Submitted!', 'success');
          console.log(response);
          this.getPlTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

}

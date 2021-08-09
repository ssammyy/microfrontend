import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {StdNwaService} from "../../../core/store/data/std/std-nwa.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NWAWorkShopDraft, SacSecTasks} from "../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-sac-sec-tasks',
  templateUrl: './sac-sec-tasks.component.html',
  styleUrls: ['./sac-sec-tasks.component.css']
})
export class SacSecTasksComponent implements OnInit,OnDestroy {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: SacSecTasks[] = [];
  public actionRequest: SacSecTasks | undefined;
  constructor(
      private stdNwaService: StdNwaService,
      private SpinnerService: NgxSpinnerService,
  ) { }

  ngOnInit(): void {
    this.getSacSecTasks();
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
  public getSacSecTasks(): void{
    this.SpinnerService.show();
    this.stdNwaService.getSacSecTasks().subscribe(
        (response: SacSecTasks[])=> {
          this.SpinnerService.hide();
          this.tasks = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  public onOpenModal(task: SacSecTasks,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='approve'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approveModal');
    }
    if (mode==='reject'){
      this.actionRequest=task;
      button.setAttribute('data-target','#rejectModal');
    }


    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  public decisionOnWd(nwaWorkShopDraft: NWAWorkShopDraft): void{
    this.SpinnerService.show();
    this.stdNwaService.decisionOnWd(nwaWorkShopDraft).subscribe(
        (response: NWAWorkShopDraft) => {
          this.SpinnerService.hide();
          console.log(response);
          this.getSacSecTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
}

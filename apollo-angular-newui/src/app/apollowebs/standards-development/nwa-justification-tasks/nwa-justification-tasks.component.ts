import {Component, OnDestroy, OnInit} from '@angular/core';
import {StdNwaService} from "../../../core/store/data/std/std-nwa.service";
import {NWAJustification, SPCSECTasks} from "../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {NgxSpinnerService} from "ngx-spinner";
import {Subject} from "rxjs";



@Component({
  selector: 'app-nwa-justification-tasks',
  templateUrl: './nwa-justification-tasks.component.html',
  styleUrls: ['./nwa-justification-tasks.component.css']
})
export class NwaJustificationTasksComponent implements OnInit , OnDestroy {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();

  p = 1;
  p2 = 1;
  tasks: SPCSECTasks[] = [];
  public actionRequest: SPCSECTasks | undefined;

  constructor(
      private stdNwaService: StdNwaService,
      private SpinnerService: NgxSpinnerService,
  ) { }

  ngOnInit(): void {
    this.getSPCSECTasks();
  }

  public getSPCSECTasks(): void {
    this.SpinnerService.show();
    this.stdNwaService.getSPCSECTasks().subscribe(
        (response: SPCSECTasks[]) => {
          this.SpinnerService.hide();
          this.tasks = response;

          this.dtTrigger.next();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }
  public onOpenModal(task: SPCSECTasks,mode:string): void{
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
  public onDecision(nWAJustification: NWAJustification): void{
    this.SpinnerService.show();
    this.stdNwaService.decisionOnJustification(nWAJustification).subscribe(
        (response: NWAJustification) => {
          this.SpinnerService.hide();
          console.log(response);
          this.getSPCSECTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }



}


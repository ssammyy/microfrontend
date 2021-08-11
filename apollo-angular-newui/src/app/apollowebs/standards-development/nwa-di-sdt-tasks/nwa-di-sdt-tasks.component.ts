import {Component, OnDestroy, OnInit} from '@angular/core';
import {StdNwaService} from "../../../core/store/data/std/std-nwa.service";
import {NgxSpinnerService} from "ngx-spinner";
import {DISDTTasks, NWADiSdtJustification} from "../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";

@Component({
  selector: 'app-nwa-di-sdt-tasks',
  templateUrl: './nwa-di-sdt-tasks.component.html',
  styleUrls: ['./nwa-di-sdt-tasks.component.css']
})
export class NwaDiSdtTasksComponent implements OnInit ,OnDestroy{
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  p = 1;
  p2 = 1;
  tasks: DISDTTasks[] = [];
  public actionRequest: DISDTTasks | undefined;
  constructor(
      private stdNwaService: StdNwaService,
      private SpinnerService: NgxSpinnerService,
  ) { }

  ngOnInit(): void {
    this.getDiSdtTasks();
  }
  public getDiSdtTasks(): void{
    this.SpinnerService.show();
    this.stdNwaService.getDiSdtTasks().subscribe(
        (response: DISDTTasks[])=> {
          this.SpinnerService.hide();
          this.dtTrigger.next();
          this.tasks = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  public onOpenModal(task: DISDTTasks,mode:string): void{
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

  public onDecision(nwaDiSdtJustification: NWADiSdtJustification): void{
    this.SpinnerService.show();
    this.stdNwaService.decisionOnDiSdtJustification(nwaDiSdtJustification).subscribe(
        (response: NWADiSdtJustification) => {
          this.SpinnerService.hide();
          console.log(response);
          this.getDiSdtTasks();
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
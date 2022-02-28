import { Component, OnInit } from '@angular/core';
import {
  DISDTTasks,
  KnwSecTasks,
  NWADiSdtJustification,
  NWAJustification,
  SPCSECTasks
} from "../../../shared/models/standard-development";
import {StdDevelopmentNwaService} from "../../../shared/services/std-development-nwa.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-nwa-di-sdt-tasks',
  templateUrl: './nwa-di-sdt-tasks.component.html',
  styleUrls: ['./nwa-di-sdt-tasks.component.css']
})
export class NwaDiSdtTasksComponent implements OnInit {

  p = 1;
  p2 = 1;
  tasks: DISDTTasks[] = [];
  public actionRequest: DISDTTasks | undefined;
  constructor(
    private stdDevelopmentNwaService: StdDevelopmentNwaService
  ) { }

  ngOnInit(): void {
    this.getDiSdtTasks();
  }
  public getDiSdtTasks(): void{
    this.stdDevelopmentNwaService.getDiSdtTasks().subscribe(
      (response: DISDTTasks[])=> {
        this.tasks = response;
      },
      (error: HttpErrorResponse)=>{
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
    this.stdDevelopmentNwaService.decisionOnDiSdtJustification(nwaDiSdtJustification).subscribe(
      (response: NWADiSdtJustification) => {
        console.log(response);
        this.getDiSdtTasks();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

}

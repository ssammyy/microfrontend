import { Component, OnInit } from '@angular/core';
import {StdDevelopmentNwaService} from "../../../shared/services/std-development-nwa.service";
import {NWAJustification, SPCSECTasks, StandardTasks} from "../../../shared/models/standard-development";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-nwa-justification-tasks',
  templateUrl: './nwa-justification-tasks.component.html',
  styleUrls: ['./nwa-justification-tasks.component.css']
})
export class NwaJustificationTasksComponent implements OnInit {
  p = 1;
  p2 = 1;
  tasks: SPCSECTasks[] = [];
  public actionRequest: SPCSECTasks | undefined;
  constructor(
    private stdDevelopmentNwaService: StdDevelopmentNwaService
  ) { }

  ngOnInit(): void {
    this.getSPCSECTasks();
  }

  public getSPCSECTasks(): void{
    this.stdDevelopmentNwaService.getSPCSECTasks().subscribe(
      (response: SPCSECTasks[])=> {
        this.tasks = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
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
    this.stdDevelopmentNwaService.decisionOnJustification(nWAJustification).subscribe(
      (response: NWAJustification) => {
        console.log(response);
        this.getSPCSECTasks();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
}

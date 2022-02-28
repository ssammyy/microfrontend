import { Component, OnInit } from '@angular/core';
import {
  KnwSecTasks,
  NWADiSdtJustification,
  NWAPreliminaryDraft
} from "../../../shared/models/standard-development";
import {StdDevelopmentNwaService} from "../../../shared/services/std-development-nwa.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-nwa-knw-sec-tasks',
  templateUrl: './nwa-knw-sec-tasks.component.html',
  styleUrls: ['./nwa-knw-sec-tasks.component.css']
})
export class NwaKnwSecTasksComponent implements OnInit {

  p = 1;
  p2 = 1;
  tasks: KnwSecTasks[] = [];
  public actionRequest: KnwSecTasks | undefined;
  constructor(
    private stdDevelopmentNwaService: StdDevelopmentNwaService
  ) { }

  ngOnInit(): void {
    this.knwtasks();
  }

  public knwtasks(): void{
    this.stdDevelopmentNwaService.knwtasks().subscribe(
      (response: KnwSecTasks[])=> {
        this.tasks = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }

  public onOpenModal(task: KnwSecTasks,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='prepareJustification'){
      this.actionRequest=task;
      button.setAttribute('data-target','#prepareJustification');
    }
    if (mode==='prepDiSdt'){
      this.actionRequest=task;
      button.setAttribute('data-target','#prepDiSdt');
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
  public uploadDiSdt(nWADiSdtJustification: NWADiSdtJustification): void{
    this.stdDevelopmentNwaService.prepareDisDtJustification(nWADiSdtJustification).subscribe(
      (response: NWADiSdtJustification) => {
        console.log(response);
        this.knwtasks();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
  public uploadPreliminaryDraft(nwaPreliminaryDraft: NWAPreliminaryDraft): void{
    this.stdDevelopmentNwaService.preparePreliminaryDraft(nwaPreliminaryDraft).subscribe(
      (response: NWAPreliminaryDraft) => {
        console.log(response);
        this.knwtasks();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
  public approvePreliminaryDraft(nwaPreliminaryDraft: NWAPreliminaryDraft): void{
    this.stdDevelopmentNwaService.decisionOnPD(nwaPreliminaryDraft).subscribe(
      (response: NWAPreliminaryDraft) => {
        console.log(response);
        this.knwtasks();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
}

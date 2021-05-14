import { Component, OnInit } from '@angular/core';
import {NWAPreliminaryDraft, NWAWorkShopDraft, SacSecTasks} from "../../../shared/models/standard-development";
import {StdDevelopmentNwaService} from "../../../shared/services/std-development-nwa.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-sac-sec-tasks',
  templateUrl: './sac-sec-tasks.component.html',
  styleUrls: ['./sac-sec-tasks.component.css']
})
export class SacSecTasksComponent implements OnInit {

  p = 1;
  p2 = 1;
  tasks: SacSecTasks[] = [];
  public actionRequest: SacSecTasks | undefined;
  constructor(
    private stdDevelopmentNwaService: StdDevelopmentNwaService
  ) { }

  ngOnInit(): void {
    this.getSacSecTasks();
  }

  public getSacSecTasks(): void{
    this.stdDevelopmentNwaService.getSacSecTasks().subscribe(
      (response: SacSecTasks[])=> {
        this.tasks = response;
      },
      (error: HttpErrorResponse)=>{
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
    this.stdDevelopmentNwaService.decisionOnWd(nwaWorkShopDraft).subscribe(
      (response: NWAWorkShopDraft) => {
        console.log(response);
        this.getSacSecTasks();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }


}

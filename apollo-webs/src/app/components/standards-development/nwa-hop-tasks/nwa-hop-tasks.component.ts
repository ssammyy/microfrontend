import { Component, OnInit } from '@angular/core';
import {
  HOPTasks, NWAStandard,
  NWAWorkShopDraft
} from "../../../shared/models/standard-development";
import {StdDevelopmentNwaService} from "../../../shared/services/std-development-nwa.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-nwa-hop-tasks',
  templateUrl: './nwa-hop-tasks.component.html',
  styleUrls: ['./nwa-hop-tasks.component.css']
})
export class NwaHopTasksComponent implements OnInit {

  p = 1;
  p2 = 1;
  tasks: HOPTasks[] = [];
  standards: NWAStandard[] = [];
  public actionRequest: HOPTasks | undefined;
  constructor(
    private stdDevelopmentNwaService: StdDevelopmentNwaService
  ) { }

  ngOnInit(): void {
    this.getHOPTasks();
  }

  public getHOPTasks(): void{
    this.stdDevelopmentNwaService.getHOPTasks().subscribe(
      (response: HOPTasks[])=> {
        this.tasks = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }

  public onOpenModal(task: HOPTasks,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='edit'){
      this.actionRequest=task;
      button.setAttribute('data-target','#editModal');
    }
    if (mode==='upload'){
      this.actionRequest=task;
      button.setAttribute('data-target','#uploadModal');
    }



    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  public onEdit(nwaWorkShopDraft: NWAWorkShopDraft): void{
    this.stdDevelopmentNwaService.editWorkshopDraft(nwaWorkShopDraft).subscribe(
      (response: NWAWorkShopDraft) => {
        console.log(response);
        this.getHOPTasks();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
  public onNwaUpload(nWAStandard: NWAStandard): void{
    this.stdDevelopmentNwaService.uploadNwaStandard(nWAStandard).subscribe(
      (response: NWAStandard) => {
        console.log(response);
        this.getHOPTasks();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

}

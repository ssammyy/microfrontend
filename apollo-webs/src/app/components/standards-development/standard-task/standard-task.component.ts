import { Component, OnInit } from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {StandardTaskService} from "../../../shared/services/standard-task.service";
import {StandardTasks} from "./standardtasks";

@Component({
  selector: 'app-standard-task',
  templateUrl: './standard-task.component.html',
  styleUrls: ['./standard-task.component.css']
})
export class StandardTaskComponent implements OnInit {
  p = 1;
  p2 = 1;
  tasks: StandardTasks[] = [];
  public actionRequest: StandardTasks | undefined;

  constructor(private standardTaskService:StandardTaskService) { }

  ngOnInit(): void {
    this.getHOFTasks();
  }

  public getHOFTasks(): void{
    this.standardTaskService.getHOFTasks().subscribe(
      (response: StandardTasks[])=> {
        this.tasks = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }

  public onReviewTask(task: StandardTasks): void{
    this.standardTaskService.reviewTask(task).subscribe(
      (response:StandardTasks) =>{
        console.log(response);
        this.getHOFTasks();
      },
      (error:HttpErrorResponse) =>{
        alert(error.message);
      }
    )
  }

  public onOpenModal(task: StandardTasks,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='edit'){
      this.actionRequest=task;
      button.setAttribute('data-target','#updateRequestModal');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

}

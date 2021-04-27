import { Component, OnInit } from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {StandardDevelopmentService} from "../../../shared/services/standard-development.service";
import {StandardTasks} from "../../../shared/models/standard-development";

@Component({
  selector: 'app-standard-task',
  templateUrl: './standard-task.component.html',
  styleUrls: ['./standard-task.component.css']
})
export class StandardTaskComponent implements OnInit {
  p = 1;
  p2 = 1;
  countLine=0;
  tasks: StandardTasks[] = [];
  public actionRequest: StandardTasks | undefined;

  constructor(private standardDevelopmentService:StandardDevelopmentService) { }

  ngOnInit(): void {
    this.getHOFTasks();
  }

  public getHOFTasks(): void{
    this.standardDevelopmentService.getHOFTasks().subscribe(
      (response: StandardTasks[])=> {
        this.tasks = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }

  public onReviewTask(task: StandardTasks): void{
    this.standardDevelopmentService.reviewTask(task).subscribe(
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

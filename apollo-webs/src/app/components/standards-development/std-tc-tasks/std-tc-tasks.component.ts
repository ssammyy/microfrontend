import { Component, OnInit } from '@angular/core';
import {StdTCTask, Stdtsectask} from "../../../shared/models/standard-development";
import {StandardDevelopmentService} from "../../../shared/services/standard-development.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-std-tc-tasks',
  templateUrl: './std-tc-tasks.component.html',
  styleUrls: ['./std-tc-tasks.component.css']
})
export class StdTcTasksComponent implements OnInit {
  p = 1;
  p2 = 1;
  public tcTasks : StdTCTask[] =[];
  constructor(
    private  standardDevelopmentService:StandardDevelopmentService
  ) { }

  ngOnInit(): void {
    this.getTCTasks();
  }

  public getTCTasks(): void{
    this.standardDevelopmentService.getTCTasks().subscribe(
      (response: StdTCTask[]) =>{
        console.log(response);
        this.tcTasks=response;
      },
      (error:HttpErrorResponse) =>{
        alert(error.message);
      }
    )
  }

}

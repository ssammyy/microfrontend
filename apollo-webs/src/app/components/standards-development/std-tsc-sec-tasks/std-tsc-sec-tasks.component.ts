import { Component, OnInit } from '@angular/core';
import {StdTscSecTasksService} from "../../../shared/services/std-tsc-sec-tasks.service";
import {Stdtsectask} from "./stdtsectask";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-std-tsc-sec-tasks',
  templateUrl: './std-tsc-sec-tasks.component.html',
  styleUrls: ['./std-tsc-sec-tasks.component.css']
})
export class StdTscSecTasksComponent implements OnInit {

  p = 1;
  p2 = 1;
  public secTasks: Stdtsectask[] = [];

  constructor(private  stdTscSecTasksService:StdTscSecTasksService) { }

  ngOnInit(): void {
    this.getTCSECTasks();
  }

  public getTCSECTasks(): void{
    this.stdTscSecTasksService.getTCSECTasks().subscribe(
      (response: Stdtsectask[]) =>{
        this.secTasks=response;
      },
      (error:HttpErrorResponse) =>{
        alert(error.message);
      }
    )
  }

}

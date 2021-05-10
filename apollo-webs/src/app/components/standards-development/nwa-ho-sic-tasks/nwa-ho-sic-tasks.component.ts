import { Component, OnInit } from '@angular/core';
import {
  HoSicTasks,
   UpdateNwaGazette,
  UploadNwaGazette
} from "../../../shared/models/standard-development";
import {StdDevelopmentNwaService} from "../../../shared/services/std-development-nwa.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-nwa-ho-sic-tasks',
  templateUrl: './nwa-ho-sic-tasks.component.html',
  styleUrls: ['./nwa-ho-sic-tasks.component.css']
})
export class NwaHoSicTasksComponent implements OnInit {
  p = 1;
  p2 = 1;
  tasks: HoSicTasks[] = [];
  public actionRequest: HoSicTasks | undefined;
  constructor(
    private stdDevelopmentNwaService: StdDevelopmentNwaService
  ) { }

  ngOnInit(): void {
    this.getHoSicTasks();
  }

  public getHoSicTasks(): void{
    this.stdDevelopmentNwaService.getHoSicTasks().subscribe(
      (response: HoSicTasks[])=> {
        this.tasks = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }

  public onOpenModal(task: HoSicTasks,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='notice'){
      this.actionRequest=task;
      button.setAttribute('data-target','#noticeModal');
    }
    if (mode==='gazetteDate'){
      this.actionRequest=task;
      button.setAttribute('data-target','#gazettementModal');
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  public onSave(uploadNwaGazette: UploadNwaGazette): void{
    this.stdDevelopmentNwaService.uploadGazetteNotice(uploadNwaGazette).subscribe(
      (response: UploadNwaGazette) => {
        console.log(response);
        this.getHoSicTasks();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
  public onUpdate(updateNwaGazette: UpdateNwaGazette): void{
    this.stdDevelopmentNwaService.updateGazettementDate(updateNwaGazette).subscribe(
      (response: UpdateNwaGazette) => {
        console.log(response);
        this.getHoSicTasks();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

}

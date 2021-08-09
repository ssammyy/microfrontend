import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {StdNwaService} from "../../../core/store/data/std/std-nwa.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HoSicTasks, UpdateNwaGazette, UploadNwaGazette} from "../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-ho-sic-tasks',
  templateUrl: './ho-sic-tasks.component.html',
  styleUrls: ['./ho-sic-tasks.component.css']
})
export class HoSicTasksComponent implements OnInit,OnDestroy {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: HoSicTasks[] = [];
  public actionRequest: HoSicTasks | undefined;
  constructor(
      private stdNwaService: StdNwaService,
      private SpinnerService: NgxSpinnerService,
  ) { }

  ngOnInit(): void {
    this.getHoSicTasks();
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
  public getHoSicTasks(): void{
    this.SpinnerService.show();
    this.stdNwaService.getHoSicTasks().subscribe(
        (response: HoSicTasks[])=> {
          this.SpinnerService.hide();
          this.tasks = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
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
    this.SpinnerService.show();
    this.stdNwaService.uploadGazetteNotice(uploadNwaGazette).subscribe(
        (response: UploadNwaGazette) => {
          this.SpinnerService.hide();
          console.log(response);
          this.getHoSicTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  public onUpdate(updateNwaGazette: UpdateNwaGazette): void{
    this.SpinnerService.show();
    this.stdNwaService.updateGazettementDate(updateNwaGazette).subscribe(
        (response: UpdateNwaGazette) => {
          console.log(response);
          this.SpinnerService.hide();
          this.getHoSicTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

}

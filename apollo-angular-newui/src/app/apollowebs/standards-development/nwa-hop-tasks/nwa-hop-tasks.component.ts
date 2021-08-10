import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {StdNwaService} from "../../../core/store/data/std/std-nwa.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HOPTasks, NWAStandard, NWAWorkShopDraft} from "../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-nwa-hop-tasks',
  templateUrl: './nwa-hop-tasks.component.html',
  styleUrls: ['./nwa-hop-tasks.component.css']
})
export class NwaHopTasksComponent implements OnInit ,OnDestroy {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: HOPTasks[] = [];
  standards: NWAStandard[] = [];
  public actionRequest: HOPTasks | undefined;
  constructor(
      private stdNwaService: StdNwaService,
      private SpinnerService: NgxSpinnerService,
  ) { }

  ngOnInit(): void {
    this.getHOPTasks();
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
  public getHOPTasks(): void{
    this.SpinnerService.show();
    this.stdNwaService.getHOPTasks().subscribe(
        (response: HOPTasks[])=> {
          this.SpinnerService.hide();
          this.tasks = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
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
    this.SpinnerService.show();
    this.stdNwaService.editWorkshopDraft(nwaWorkShopDraft).subscribe(
        (response: NWAWorkShopDraft) => {
          this.SpinnerService.hide();
          console.log(response);
          this.getHOPTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  public onNwaUpload(nWAStandard: NWAStandard): void{
    this.SpinnerService.show();
    this.stdNwaService.uploadNwaStandard(nWAStandard).subscribe(
        (response: NWAStandard) => {
          this.SpinnerService.hide();
          console.log(response);
          this.getHOPTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
}

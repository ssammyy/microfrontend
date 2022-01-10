import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {
    CompanyModel,
    ManufactureCompleteTask,
    ManufactureDetailList,
    ManufacturePendingTask
} from "../../../core/store/data/levy/levy.model";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";
import {DISDTTasks} from "../../../core/store/data/std/std.model";

@Component({
  selector: 'app-standard-levy-manufacture-details',
  templateUrl: './standard-levy-manufacture-details.component.html',
  styleUrls: ['./standard-levy-manufacture-details.component.css']
})
export class StandardLevyManufactureDetailsComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  manufactureLists: ManufactureDetailList[] = [];
    manufacturePendingTasks: ManufacturePendingTask[] = [];
    manufactureCompleteTasks: ManufactureCompleteTask[] = [];
  public actionRequestList: ManufactureDetailList | undefined;
    public actionRequestPending: ManufacturePendingTask | undefined;
    public actionRequestComplete: ManufactureCompleteTask | undefined;
  constructor(
      private levyService: LevyService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getMnCompleteTask();
    this.getManufacturerList();
    this.getMnPendingTask();
  }
  public getManufacturerList(): void{
    this.SpinnerService.show();
    this.levyService.getManufacturerList().subscribe(
        (response: ManufactureDetailList[])=> {
          this.SpinnerService.hide();
          this.dtTrigger.next();
          this.manufactureLists = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  public getMnPendingTask(): void{
    this.SpinnerService.show();
    this.levyService.getMnPendingTask().subscribe(
        (response: ManufacturePendingTask[])=> {
          this.SpinnerService.hide();
          this.dtTrigger.next();
          this.manufacturePendingTasks = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  public getMnCompleteTask(): void{
    this.SpinnerService.show();
    this.levyService.getMnCompleteTask().subscribe(
        (response: ManufactureCompleteTask[])=> {
          this.SpinnerService.hide();
          this.dtTrigger.next();
          this.manufactureCompleteTasks = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
    public onOpenModalList(manufactureLists: ManufactureDetailList,mode:string): void{
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle','modal');
        if (mode==='viewList'){
            this.actionRequestList=manufactureLists;
            button.setAttribute('data-target','#viewList');
        }
        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    public onOpenModalPending(manufacturePendingTask: ManufacturePendingTask,mode:string): void{
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle','modal');
        if (mode==='viewPending'){
            this.actionRequestPending=manufacturePendingTask;
            button.setAttribute('data-target','#viewPending');
        }
        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    public onOpenModalComplete(manufactureCompleteTask: ManufactureCompleteTask,mode:string): void{
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle','modal');
        if (mode==='viewComplete'){
            this.actionRequestComplete=manufactureCompleteTask;
            button.setAttribute('data-target','#viewComplete');
        }
        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

}

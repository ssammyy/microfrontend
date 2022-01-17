import {Component, OnInit, ViewChild} from '@angular/core';
import {Subject} from "rxjs";
import {
    ManufactureCompleteTask,
    ManufactureDetailList,
    ManufacturePendingTask
} from "../../../core/store/data/levy/levy.model";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";
import {DataTableDirective} from "angular-datatables";
import {Department} from "../../../core/store/data/std/request_std.model";

@Component({
    selector: 'app-standard-levy-manufacture-details',
    templateUrl: './standard-levy-manufacture-details.component.html',
    styleUrls: ['./standard-levy-manufacture-details.component.css']
})
export class StandardLevyManufactureDetailsComponent implements OnInit {
    p = 1;
    p2 = 1;
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;
    isDtInitialized: boolean = false
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();

    dtOptions1: DataTables.Settings = {};
    dtTrigger1: Subject<any> = new Subject<any>();

    dtOptions2: DataTables.Settings = {};
    dtTrigger2: Subject<any> = new Subject<any>();


    manufactureLists: ManufactureDetailList[] = [];
    manufacturePendingTasks: ManufacturePendingTask[] = [];
    manufactureCompleteTasks: ManufactureCompleteTask[] = [];
    public actionRequestList: ManufactureDetailList | undefined;
    public actionRequestPending: ManufacturePendingTask | undefined;
    public actionRequestComplete: ManufactureCompleteTask | undefined;

    constructor(
        private levyService: LevyService,
        private SpinnerService: NgxSpinnerService,
        private notifyService: NotificationService
    ) {
    }

    ngOnInit(): void {

    }

    ngAfterViewInit(): void {
        this.getMnCompleteTask();
        this.getManufacturerList();
        this.getMnPendingTask();
    }

    // public getManufacturerList(): void {
    //     this.SpinnerService.show();
    //     this.levyService.getManufacturerList().subscribe(
    //         (response: ManufactureDetailList[]) => {
    //             console.log(response)
    //             this.SpinnerService.hide();
    //             this.manufactureLists = response;
    //             if (this.isDtInitialized) {
    //                 this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
    //                     dtInstance.destroy();
    //                     this.dtTrigger.next();
    //                 });
    //             } else {
    //                 this.isDtInitialized = true
    //                 this.dtTrigger.next();
    //             }
    //         },
    //         (error: HttpErrorResponse) => {
    //             this.SpinnerService.hide();
    //             alert(error.message);
    //         }
    //     );
    // }
    public getManufacturerList(): void {
        this.levyService.getManufacturerList().subscribe(
            (response: ManufactureDetailList[]) => {
                this.manufactureLists = response;
                console.log(response)
                if (this.isDtInitialized) {
                    this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
                        dtInstance.destroy();
                        this.dtTrigger.next();
                    });
                } else {
                    this.isDtInitialized = true
                    this.dtTrigger.next();
                }
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
    }

    public getMnPendingTask(): void {
        this.SpinnerService.show();
        this.levyService.getMnPendingTask().subscribe(
            (response: ManufacturePendingTask[]) => {
          this.SpinnerService.hide();
          this.dtTrigger1.next();
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
          this.dtTrigger2.next();
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

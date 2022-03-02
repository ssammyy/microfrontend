import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {ManufactureCompletedTask} from "../../../core/store/data/levy/levy.model";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {FormBuilder} from "@angular/forms";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";

declare const $: any;
@Component({
  selector: 'app-std-levy-complete-tasks',
  templateUrl: './std-levy-complete-tasks.component.html',
  styleUrls: ['./std-levy-complete-tasks.component.css']
})
export class StdLevyCompleteTasksComponent implements OnInit {

    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;

    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    isDtInitialized: boolean = false
    loadingText: string;
  blob: Blob;

  manufactureCompleteTasks: ManufactureCompletedTask[] = [];
  public actionRequestComplete: ManufactureCompletedTask | undefined;

  constructor(
      private router: Router,
      private store$: Store<any>,
      private formBuilder: FormBuilder,
      private levyService: LevyService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getMnCompleteTask();

  }
  public getMnCompleteTask(): void{
    this.SpinnerService.show();
    this.levyService.getMnCompleteTask().subscribe(
        (response: ManufactureCompletedTask[])=> {
          console.log(this.manufactureCompleteTasks)
          this.manufactureCompleteTasks = response;
          this.SpinnerService.hide();
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
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }
  viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.levyService.viewReportDoc(pdfId).subscribe(
        (dataPdf: any) => {
          this.SpinnerService.hide();
          this.blob = new Blob([dataPdf], {type: applicationType});

          // tslint:disable-next-line:prefer-const
          let downloadURL = window.URL.createObjectURL(this.blob);
          const link = document.createElement('a');
          link.href = downloadURL;
          link.download = fileName;
          link.click();
          // this.pdfUploadsView = dataPdf;
        },
    );
  }

  public onOpenModalComplete(manufactureCompleteTask: ManufactureCompletedTask,mode:string): void{
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

  @ViewChild('closeModal') private closeModal: ElementRef | undefined;

  public hideModel() {
    this.closeModal?.nativeElement.click();
  }

}

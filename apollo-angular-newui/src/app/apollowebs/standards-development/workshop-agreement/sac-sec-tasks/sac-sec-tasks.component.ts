import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {StdNwaService} from "../../../../core/store/data/std/std-nwa.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HOPTasks, NWAWDDecision, NWAWorkShopDraft, SacSecTasks} from "../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {NotificationService} from "../../../../core/store/data/std/notification.service";

@Component({
  selector: 'app-sac-sec-tasks',
  templateUrl: './sac-sec-tasks.component.html',
  styleUrls: ['./sac-sec-tasks.component.css']
})
export class SacSecTasksComponent implements OnInit,OnDestroy {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: SacSecTasks[] = [];
    blob: Blob;
  public actionRequest: SacSecTasks | undefined;
  constructor(
      private stdNwaService: StdNwaService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getSacSecTasks();
  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  showToasterWarning(title:string,message:string){
    this.notifyService.showWarning(message, title)

  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  public getSacSecTasks(): void{
    this.SpinnerService.show();
    this.stdNwaService.getSacSecTasks().subscribe(
        (response: SacSecTasks[])=> {
          this.tasks = response;
          this.dtTrigger.next();
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);

        }
    );
  }
  public onOpenModal(task: SacSecTasks,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='approve'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approveModal');
    }
    if (mode==='reject'){
      this.actionRequest=task;
      button.setAttribute('data-target','#rejectModal');
    }


    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  public decisionOnWd(nwaWDDecision: NWAWDDecision): void{
    this.SpinnerService.show();
    this.stdNwaService.decisionOnWd(nwaWDDecision).subscribe(
        (response: NWAWorkShopDraft) => {
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Workshop Draft Approved`);
          console.log(response);
          this.getSacSecTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Try Again`);
          this.getSacSecTasks();
          console.log(error.message);
        }
    );
  }
  public decisionRejectWd(nwaWDDecision: NWAWDDecision): void{
    this.SpinnerService.show();
    this.stdNwaService.decisionOnWd(nwaWDDecision).subscribe(
        (response: NWAWorkShopDraft) => {
          this.SpinnerService.hide();
          this.showToasterWarning('Success', `Workshop Draft Rejected`);
          console.log(response);
          this.getSacSecTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Try Again`);
          this.getSacSecTasks();
          console.log(error.message);
        }
    );
  }
    viewWDFile(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.stdNwaService.viewWorkshopDraftPDF(pdfId).subscribe(
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
}

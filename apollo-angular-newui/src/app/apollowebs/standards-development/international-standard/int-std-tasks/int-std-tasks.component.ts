import {Component, OnInit, ViewChild} from '@angular/core';
import {ISAdoptionProposal, ProposalComments} from "../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {FormBuilder} from "@angular/forms";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";

declare const $: any;

@Component({
  selector: 'app-int-std-tasks',
  templateUrl: './int-std-tasks.component.html',
  styleUrls: ['./int-std-tasks.component.css']
})
export class IntStdTasksComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ProposalComments[] = [];
  public actionRequest: ProposalComments | undefined;
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false
  loadingText: string;

  constructor(
      private store$: Store<any>,
      private router: Router,
      private stdIntStandardService:StdIntStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.getUserTasks();
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)
  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  public getUserTasks(): void {
    this.loadingText = "Retrieving Tasks...";
    this.SpinnerService.show();
    this.stdIntStandardService.getUserTasks().subscribe(
        (response: ProposalComments[]) => {
          this.tasks = response;
          console.log(this.tasks)
          if (this.isDtInitialized) {
            this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
              dtInstance.destroy();
              this.dtTrigger.next();
              this.SpinnerService.hide();
            });
          } else {
            this.isDtInitialized = true
            this.dtTrigger.next();
            this.SpinnerService.hide();
          }

        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }

  public onOpenModal(proposalComments: ProposalComments,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='comment'){
      this.actionRequest=proposalComments;
      button.setAttribute('data-target','#commentModal');

    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
}

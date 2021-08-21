import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgxSpinnerService} from "ngx-spinner";

import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {
    Department, ISAdoptionJustification,
    ISAdoptionProposal,
    ISTcSecTASKS,
    TechnicalCommittee
} from "../../../../core/store/data/std/std.model";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";

@Component({
  selector: 'app-int-std-responses-list',
  templateUrl: './int-std-responses-list.component.html',
  styleUrls: ['./int-std-responses-list.component.css']
})
export class IntStdResponsesListComponent implements OnInit ,OnDestroy{
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
  public departments !: Department[] ;
  public committees !: TechnicalCommittee[] ;
  tasks: ISTcSecTASKS[] = [];
  public actionRequest: ISTcSecTASKS | undefined;
  constructor(
      private stdIntStandardService : StdIntStandardService,
      private standardDevelopmentService : StandardDevelopmentService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getTCSECTasks();
    this.getDepartments();
  }
  public getTCSECTasks(): void{
    this.stdIntStandardService.getTCSECTasks().subscribe(
        (response: ISTcSecTASKS[])=> {
          this.tasks = response;
        },
        (error: HttpErrorResponse)=>{
          alert(error.message);
        }
    );
  }
  public getDepartments(): void{
    this.standardDevelopmentService.getDepartments().subscribe(
        (response: Department[])=> {
          this.departments = response;
        },
        (error: HttpErrorResponse)=>{
          alert(error.message);
        }
    );
  }
  onSelectDepartment(value: any): any {
    this.standardDevelopmentService.getTechnicalCommittee(value).subscribe(
        (response: TechnicalCommittee[]) => {
          console.log(response);
          this.committees = response
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }


  public onOpenModal(task: ISTcSecTASKS,mode:string): void{
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
    if (mode==='prepareJustification'){
      this.actionRequest=task;
      button.setAttribute('data-target','#justificationModal');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  // onDecision
  public onDecision(iSAdoptionProposal: ISAdoptionProposal): void{
      this.SpinnerService.show();
    this.stdIntStandardService.decisionOnProposal(iSAdoptionProposal).subscribe(
        (response: ISAdoptionProposal) => {
          console.log(response);
            this.SpinnerService.hide();
          this.getTCSECTasks();
        },
        (error: HttpErrorResponse) => {
            this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  //save justification
  public uploadJustification(iSAdoptionJustification: ISAdoptionJustification): void{
      this.SpinnerService.show();
    this.stdIntStandardService.prepareJustification(iSAdoptionJustification).subscribe(
        (response: ISAdoptionJustification) => {
          console.log(response);
            this.SpinnerService.hide();
          this.getTCSECTasks();
        },
        (error: HttpErrorResponse) => {
            this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
    ngOnDestroy(): void {
        this.dtTrigger.unsubscribe();
    }

}

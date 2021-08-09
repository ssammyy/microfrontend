import {Component, OnDestroy, OnInit} from '@angular/core';
import {StdIntStandardService} from "../../../core/store/data/std/std-int-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {ISAdoptionComments, ProposalComments} from "../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";

@Component({
  selector: 'app-int-std-comments',
  templateUrl: './int-std-comments.component.html',
  styleUrls: ['./int-std-comments.component.css']
})
export class IntStdCommentsComponent implements OnInit,OnDestroy {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ProposalComments[] = [];
  public actionRequest: ProposalComments | undefined;
  constructor(
      private stdIntStandardService : StdIntStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getISProposals();
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  public getISProposals(): void{
    this.SpinnerService.show();
    this.stdIntStandardService.getISProposals().subscribe(
        (response: ProposalComments[])=> {
          this.tasks = response;
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          alert(error.message);
        }
    );
  }
  public onOpenModal(task: ProposalComments,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='comment'){
      this.actionRequest=task;
      button.setAttribute('data-target','#commentModal');
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  public onSubmit(iSAdoptionComments: ISAdoptionComments): void{
    this.SpinnerService.show();
    this.stdIntStandardService.submitAPComments(iSAdoptionComments).subscribe(
        (response: ISAdoptionComments) => {
          console.log(response);
          this.SpinnerService.hide();
          this.getISProposals();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

}

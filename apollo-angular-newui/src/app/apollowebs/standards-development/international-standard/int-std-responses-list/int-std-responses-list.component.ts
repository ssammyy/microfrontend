import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgxSpinnerService} from "ngx-spinner";

import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {
    Department, DiSdtDECISION, ISAdoptionJustification,
    ISAdoptionProposal, ISDecision,
    ISTcSecTASKS, NWADiSdtJustification,
    TechnicalCommittee
} from "../../../../core/store/data/std/std.model";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {Store} from "@ngrx/store";
import {selectUserInfo} from "../../../../core/store";
import swal from "sweetalert2";

@Component({
  selector: 'app-int-std-responses-list',
  templateUrl: './int-std-responses-list.component.html',
  styleUrls: ['./int-std-responses-list.component.css']
})
export class IntStdResponsesListComponent implements OnInit ,OnDestroy{
    fullname = '';
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
  public departments !: Department[] ;
  public committees !: TechnicalCommittee[] ;
  tasks: ISTcSecTASKS[] = [];
  public actionRequest: ISTcSecTASKS | undefined;
    public technicalCommittees !: TechnicalCommittee[];
    public uploadedFiles: FileList;
  constructor(
      private store$: Store<any>,
      private stdIntStandardService : StdIntStandardService,
      private standardDevelopmentService : StandardDevelopmentService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getTCSECTasks();
    this.getDepartments();
      this.store$.select(selectUserInfo).pipe().subscribe((u) => {
          return this.fullname = u.fullName;
      });
  }
    showToasterError(title:string,message:string){
        this.notifyService.showError(message, title)

    }
    showToasterSuccess(title:string,message:string){
        this.notifyService.showSuccess(message, title)

    }
  public getTCSECTasks(): void{
    this.stdIntStandardService.getTCSECTasks().subscribe(
        (response: ISTcSecTASKS[])=> {
          this.tasks = response;
          this.dtTrigger.next();
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
        this.SpinnerService.show();
        this.standardDevelopmentService.getTechnicalCommitteeb(value).subscribe(
            (response: TechnicalCommittee[]) => {
                console.log(response);
                this.SpinnerService.hide();
                this.technicalCommittees = response
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
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

    public onDecision(iSDecision: ISDecision): void{
        this.SpinnerService.show();
        this.stdIntStandardService.decisionOnProposal(iSDecision).subscribe(
            (response: ISAdoptionProposal) => {
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Proposal Approved`);
                console.log(response);
                this.getTCSECTasks();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Try Again`);
                console.log(error.message);
                this.getTCSECTasks();
                //alert(error.message);
            }
        );
    }
    public onDecisionReject(iSDecision: ISDecision): void{
        this.SpinnerService.show();
        this.stdIntStandardService.decisionOnProposal(iSDecision).subscribe(
            (response: ISAdoptionProposal) => {
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Proposal Not Approved`);
                console.log(response);
                this.getTCSECTasks();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Try Again`);
                console.log(error.message);
                this.getTCSECTasks();
                //alert(error.message);
            }
        );
    }
  //save justification
  public uploadJustification(iSAdoptionJustification: ISAdoptionJustification): void{
      this.SpinnerService.show();
    this.stdIntStandardService.prepareJustification(iSAdoptionJustification).subscribe(
        (response) => {
          console.log(response);
            this.SpinnerService.hide();
            this.onClickSaveUploads(response.body.savedRowID)
        },
        (error: HttpErrorResponse) => {
            this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

    onClickSaveUploads(isJustificationID: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }

            this.SpinnerService.show();
            this.stdIntStandardService.uploadJSFile(isJustificationID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = null;
                    console.log(data);
                    swal.fire({
                        title: 'Justification Prepared.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    this.getTCSECTasks();
                },
            );
        }

    }

    ngOnDestroy(): void {
        this.dtTrigger.unsubscribe();
    }

}

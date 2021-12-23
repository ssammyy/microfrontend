import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {ISAdoptionComments, ProposalComments} from "../../../../core/store/data/std/std.model";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {Store} from "@ngrx/store";
import {selectUserInfo} from "../../../../core/store";

@Component({
  selector: 'app-int-std-comments',
  templateUrl: './int-std-comments.component.html',
  styleUrls: ['./int-std-comments.component.css']
})
export class IntStdCommentsComponent implements OnInit,OnDestroy {
  fullname = '';
  blob: Blob;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ProposalComments[] = [];
  public actionRequest: ProposalComments | undefined;
  constructor(
      private store$: Store<any>,
      private stdIntStandardService : StdIntStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getISProposals();

    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      return this.fullname = u.fullName;
    });
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

  public getISProposals(): void{
    this.SpinnerService.show();
    this.stdIntStandardService.getISProposals().subscribe(
        (response: ProposalComments[])=> {
          this.tasks = response;
          this.dtTrigger.next();
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
            this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.stdIntStandardService.viewProposalPDF(pdfId).subscribe(
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
        (error: HttpErrorResponse) => {
            this.SpinnerService.hide();
            this.showToasterError('Error', `Error opening document`);
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
          this.showToasterSuccess('Success', `Comment Submitted`);
          this.getISProposals();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Request`);
          alert(error.message);
        }
    );
  }

}

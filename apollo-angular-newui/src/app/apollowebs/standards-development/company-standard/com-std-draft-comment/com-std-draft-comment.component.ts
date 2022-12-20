import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {ApproveDraft, ComJcJustificationDec, COMPreliminaryDraft} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-com-std-draft-comment',
  templateUrl: './com-std-draft-comment.component.html',
  styleUrls: ['./com-std-draft-comment.component.css']
})
export class ComStdDraftCommentComponent implements OnInit {
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: COMPreliminaryDraft[] = [];
  public actionRequest: COMPreliminaryDraft | undefined;
  public committeeFormGroup!: FormGroup;

  constructor(
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.getUploadedStdDraftForComment();
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }
  showToasterWarning(title:string,message:string){
    this.notifyService.showWarning(message, title)

  }
  public getUploadedStdDraftForComment(): void{
    this.SpinnerService.show();
    this.stdComStandardService.getUploadedStdDraftForComment().subscribe(
        (response: COMPreliminaryDraft[])=> {
          this.SpinnerService.hide();
          this.rerender();
          this.tasks = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  public onOpenModal(task: COMPreliminaryDraft,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='approveJustification'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approveJustification');
    }


    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  rerender(): void {
    this.dtElements.forEach((dtElement: DataTableDirective) => {
      if (dtElement.dtInstance)
        dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
          dtInstance.destroy();
        });
    });
    setTimeout(() => {
      this.dtTrigger.next();
    });
  }

  public commentOnDraft(approveDraft: ApproveDraft): void{
    this.SpinnerService.show();
    this.stdComStandardService.commentOnDraft(approveDraft).subscribe(
        (response: ComJcJustificationDec) => {
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Comment Uploaded`);
          console.log(response);
          this.getUploadedStdDraftForComment();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Action`);
          console.log(error.message);
          this.getUploadedStdDraftForComment();
          //alert(error.message);
        }
    );
  }

}

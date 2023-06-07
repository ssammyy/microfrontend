import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {KNWCommittee, KNWDepartment, StandardReviewTasks, UsersEntity} from "../../../../core/store/data/std/std.model";
import {StdReviewService} from "../../../../core/store/data/std/std-review.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {selectUserInfo} from "../../../../core/store";
import {Store} from "@ngrx/store";

@Component({
  selector: 'app-systemic-review-editor',
  templateUrl: './systemic-review-editor.component.html',
  styleUrls: ['./systemic-review-editor.component.css']
})
export class SystemicReviewEditorComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: StandardReviewTasks[]=[];
  public actionRequest: StandardReviewTasks | undefined;
  public editFormGroup!: FormGroup;
  public editStandardFormGroup!: FormGroup;
  loadingText: string;

  constructor(
      private store$: Store<any>,
      private stdReviewService : StdReviewService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {

    this.getEditorTasks();

    this.editFormGroup= this.formBuilder.group({
      taskId:[],
      processId:[],
      title: [],
      scope: [],
      normativeReference: [],
      symbolsAbbreviatedTerms: [],
      clause: [],
      special: [],
      standardNumber: [],
      standardType: [],
      dateFormed: [],
      documentType: []

    });
      this.editStandardFormGroup= this.formBuilder.group({
          taskId:[],
          processId:[],
          title: [],
          scope: [],
          normativeReference: [],
          symbolsAbbreviatedTerms: [],
          clause: [],
          special: [],
          standardNumber: [],
          standardType: [],
          dateFormed: [],
          documentType: [],
          draftId: []

      });

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
  public getEditorTasks(): void{
    this.SpinnerService.show();
    this.stdReviewService.getEditorTasks().subscribe(
        (response: StandardReviewTasks[])=> {
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
  public onOpenModal(task: StandardReviewTasks,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='comment'){
      this.actionRequest=task;
      button.setAttribute('data-target','#commentModal');
      this.editFormGroup.patchValue(
          {
            taskId: this.actionRequest.taskId,
            processId: this.actionRequest.processId,
            title: this.actionRequest.taskData.title,
            standardNumber: this.actionRequest.taskData.standardNumber,
            documentType: this.actionRequest.taskData.documentType,
            scope: this.actionRequest.taskData.scope,
            normativeReference: this.actionRequest.taskData.normativeReference,
            symbolsAbbreviatedTerms: this.actionRequest.taskData.symbolsAbbreviatedTerms,
            clause: this.actionRequest.taskData.clause,
            special: this.actionRequest.taskData.special,
            standardType: this.actionRequest.taskData.standardType

          });

    }
      if (mode==='editStandard'){
          this.actionRequest=task;
          button.setAttribute('data-target','#editStandardModal');
          this.editStandardFormGroup.patchValue(
              {
                  taskId: this.actionRequest.taskId,
                  processId: this.actionRequest.processId,
                  title: this.actionRequest.taskData.title,
                  standardNumber: this.actionRequest.taskData.standardNumber,
                  documentType: this.actionRequest.taskData.documentType,
                  scope: this.actionRequest.taskData.scope,
                  normativeReference: this.actionRequest.taskData.normativeReference,
                  symbolsAbbreviatedTerms: this.actionRequest.taskData.symbolsAbbreviatedTerms,
                  clause: this.actionRequest.taskData.clause,
                  special: this.actionRequest.taskData.special,
                  standardType: this.actionRequest.taskData.standardType,
                  draftId: this.actionRequest.taskData.draftId

              });

      }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  @ViewChild('closeModalCDetails') private closeModalCDetails: ElementRef | undefined;

  public hideModelCDetails() {
    this.closeModalCDetails?.nativeElement.click();
  }
    @ViewChild('closeModalStandard') private closeModalStandard: ElementRef | undefined;

    public hideModelStandard() {
        this.closeModalStandard?.nativeElement.click();
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

  submitDraftForEditing(): void {
    this.loadingText = "Editing Draft ...."
    this.SpinnerService.show();
    this.stdReviewService.submitDraftForEditing(this.editFormGroup.value).subscribe(
        (response) => {
          console.log(response);
          this.getEditorTasks();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Draft Edited`);

        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Editing Draft Try Again`);
          console.log(error.message);
        }
    );
    this.hideModelCDetails();

  }

    editStandardDraft(): void {
        this.loadingText = "Editing Standard ...."
        this.SpinnerService.show();
        this.stdReviewService.editStandardDraft(this.editStandardFormGroup.value).subscribe(
            (response) => {
                console.log(response);
                this.getEditorTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Standard Edited`);

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Editing Standard Try Again`);
                console.log(error.message);
            }
        );
        this.hideModelStandard();

    }


}

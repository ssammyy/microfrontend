import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {StandardReviewTasks} from "../../../../core/store/data/std/std.model";
import {StdReviewService} from "../../../../core/store/data/std/std-review.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-systemic-review-draughts-man',
  templateUrl: './systemic-review-draughts-man.component.html',
  styleUrls: ['./systemic-review-draughts-man.component.css']
})
export class SystemicReviewDraughtsManComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: StandardReviewTasks[]=[];
  public actionRequest: StandardReviewTasks | undefined;
  loadingText: string;
  public draftStandardFormGroup!: FormGroup;

  constructor(
      private stdReviewService : StdReviewService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.getDraughtsManTasks();

    this.draftStandardFormGroup= this.formBuilder.group({
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
  public getDraughtsManTasks(): void{
    this.SpinnerService.show();
    this.stdReviewService.getDraughtsManTasks().subscribe(
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
  public onOpenModal(StandardReviewTask: StandardReviewTasks,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='draughting'){
      this.actionRequest=StandardReviewTask;
      button.setAttribute('data-target','#draughtingModal');
      this.draftStandardFormGroup.patchValue(
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

  draftStandard(): void {
    this.loadingText = "Draughting Standard ...."
    this.SpinnerService.show();
    this.stdReviewService.draftStandard(this.draftStandardFormGroup.value).subscribe(
        (response) => {
          console.log(response);
          this.getDraughtsManTasks();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Standard Draughted`);

        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Draughting Standard Try Again`);
          console.log(error.message);
        }
    );
    this.hideModelStandard();

  }

}

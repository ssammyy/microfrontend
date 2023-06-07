import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {StandardReviewTasks} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {StdReviewService} from "../../../../core/store/data/std/std-review.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-system-review-update-gazette',
  templateUrl: './system-review-update-gazette.component.html',
  styleUrls: ['./system-review-update-gazette.component.css']
})
export class SystemReviewUpdateGazetteComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: StandardReviewTasks[]=[];
  public actionRequest: StandardReviewTasks | undefined;
  loadingText: string;
  public gazetteFormGroup!: FormGroup;

  constructor(
      private stdReviewService : StdReviewService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.getHoSicTasks();
    this.gazetteFormGroup= this.formBuilder.group({
      standardID:[],
      processId:[],
      taskId:[]
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

  public getHoSicTasks(): void{
    this.SpinnerService.show();
    this.stdReviewService.getHoSicTasks().subscribe(
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
      this.gazetteFormGroup.patchValue(
          {

            taskId: this.actionRequest.taskId,
            processId: this.actionRequest.processId,
            standardID: this.actionRequest.taskData.standardID


          }
      );

    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  @ViewChild('closeModalCDetails') private closeModalCDetails: ElementRef | undefined;

  public hideModelCDetails() {
    this.closeModalCDetails?.nativeElement.click();
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

  updateGazettementDate(): void {
    this.loadingText = "Gazetting Standard ...."
    this.SpinnerService.show();
    this.stdReviewService.updateGazettementDate(this.gazetteFormGroup.value).subscribe(
        (response) => {
          console.log(response);
          this.getHoSicTasks();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Standard Gazetted`);

        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Gazetting Standard Try Again`);
          console.log(error.message);
        }
    );
    this.hideModelCDetails();

  }


}

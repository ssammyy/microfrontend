import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {StdNwaService} from "../../../../core/store/data/std/std-nwa.service";
import {NWADiSdtJustification, NWAJustification, SPCSECTasks} from "../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {NgxSpinnerService} from "ngx-spinner";
import {Subject} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NotificationService} from "../../../../core/store/data/std/notification.service";



@Component({
  selector: 'app-nwa-justification-tasks',
  templateUrl: './nwa-justification-tasks.component.html',
  styleUrls: ['./nwa-justification-tasks.component.css']
})
export class NwaJustificationTasksComponent implements OnInit , OnDestroy {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: SPCSECTasks[] = [];
  public actionRequest: SPCSECTasks | undefined;
  public approveFormGroup!: FormGroup;

  constructor(
      private formBuilder: FormBuilder,
      private stdNwaService: StdNwaService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService

  ) {
  }

  ngOnInit(): void {
    this.getSPCSECTasks();
    this.approveFormGroup = this.formBuilder.group({
      taskId: [],
      accentTo: []

    });

  }

  public getSPCSECTasks(): void {
    this.SpinnerService.show();
    this.stdNwaService.getSPCSECTasks().subscribe(
        (response: SPCSECTasks[]) => {
          this.hideModel()
          this.tasks = response;
          this.dtTrigger.next();
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }
  public onOpenModal(task: SPCSECTasks,mode:string): void{
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
  @ViewChild('closeModal') private closeModal: ElementRef | undefined;
  public hideModel() {
    this.closeModal?.nativeElement.click();
  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  get formDecision(): any {
    return this.approveFormGroup.controls;
  }
   onDecision(): void{
    this.SpinnerService.show();
     this.stdNwaService.decisionOnJustification(this.approveFormGroup.value).subscribe(
        (response) => {
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Justification Approved`);
          this.dtTrigger.next();
          console.log(response);
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Justification Was Not Approved`);
          alert(error.message);
        }
    );
  }



  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }



}


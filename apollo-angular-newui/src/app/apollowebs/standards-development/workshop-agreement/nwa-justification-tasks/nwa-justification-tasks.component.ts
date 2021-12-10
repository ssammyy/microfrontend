import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {StdNwaService} from "../../../../core/store/data/std/std-nwa.service";
import {
    DiSdtDECISION, KnwSecTasks,
    NWADiSdtJustification,
    NWAJustification, NWAJustificationDecision,
    SPCSECTasks
} from "../../../../core/store/data/std/std.model";
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
    displayTable: boolean = false;
  blob: Blob;
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
    public getSPCSECTasks(): void{
        this.SpinnerService.show();
        this.stdNwaService.getSPCSECTasks().subscribe(
            (response: SPCSECTasks[])=> {
                this.tasks = response;
                this.dtTrigger.next();
                this.displayTable = true;
                this.SpinnerService.hide();
            },
            (error: HttpErrorResponse)=>{
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }

  // public getSPCSECTasks(): void {
  //   this.SpinnerService.show();
  //   this.stdNwaService.getSPCSECTasks().subscribe(
  //       (response: SPCSECTasks[]) => {
  //         this.hideModel()
  //         this.tasks = response;
  //         this.dtTrigger.next();
  //         this.SpinnerService.hide();
  //       },
  //       (error: HttpErrorResponse) => {
  //         this.SpinnerService.hide();
  //         console.log(error.message);
  //       }
  //   );
  // }
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
    public onDecision(nwaJustificationDecision: NWAJustificationDecision): void{
        this.SpinnerService.show();
        this.stdNwaService.decisionOnJustification(nwaJustificationDecision).subscribe(
            (response: NWAJustification) => {
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Justification Approved`);
                console.log(response);
                this.getSPCSECTasks();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
                this.showToasterError('Error', `Justification Was Not Approved`);
                this.getSPCSECTasks();
                //alert(error.message);
            }
        );
    }
    public rejectDecision(nwaJustificationDecision: NWAJustificationDecision): void{
        this.SpinnerService.show();
        this.stdNwaService.decisionOnJustification(nwaJustificationDecision).subscribe(
            (response: NWAJustification) => {
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Justification Was Declined`);
                console.log(response);
                this.getSPCSECTasks();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
                this.showToasterError('Error', `Justification Was Not Approved`);
                this.getSPCSECTasks();
                //alert(error.message);
            }
        );
    }

    viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.stdNwaService.viewJustificationPDF(pdfId).subscribe(
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
        );
    }





    ngOnDestroy(): void {
        this.dtTrigger.unsubscribe();
    }



}


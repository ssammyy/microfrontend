import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {
  StdJustification,
  Stdtsectask,
  StdtsecTaskJustification
} from "../../../../core/store/data/std/request_std.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";

@Component({
  selector: 'app-std-justification',
  templateUrl: './std-justification.component.html',
  styleUrls: ['./std-justification.component.css']
})
export class StdJustificationComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  p = 1;
  p2 = 1;
  public secTasks: StdtsecTaskJustification[] = [];
  public tscsecRequest !: StdtsecTaskJustification | undefined;
  public stdTSecFormGroup!: FormGroup;

  public formActionRequest: StdJustification | undefined;

  public  itemId = "F&A/1:2021";
  public referenceMaterial: string="ReferenceMaterialJustification";


  constructor(
      private formBuilder: FormBuilder,
      private  standardDevelopmentService: StandardDevelopmentService
  ) {
  }

  ngOnInit(): void {
    this.getTCSECTasksJustification();

  }

  get formStdTSec(): any {
    return this.stdTSecFormGroup.controls;
  }

  public getTCSECTasksJustification(): void {
    this.standardDevelopmentService.getTCSECTasksJustification().subscribe(
        (response: StdtsecTaskJustification[]) => {
          console.log(response);
          this.secTasks = response;
          this.dtTrigger.next();

        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    )
  }

  /*
   public onUpload(secTask: Stdtsectask): void {
     this.standardDevelopmentService.uploadNWI(secTask).subscribe(
       (response: Stdtsectask) => {
         console.log(response);
         this.getTCSECTasks();
       },
       (error: HttpErrorResponse) => {
         alert(error.message);
       }
     )
   }
   uploadNWI(): void {
     this.standardDevelopmentService.uploadNWI(this.stdTSecFormGroup.value).subscribe(
       (response: Stdtsectask) => {
         console.log(response);
         this.getTCSECTasks();
       },
       (error: HttpErrorResponse) => {
         alert(error.message);
       }
     );
   }*/

  uploadJustification(stdJustification: StdJustification): void {

    console.log(stdJustification);

    this.standardDevelopmentService.uploadJustification(stdJustification).subscribe(
        (response: Stdtsectask) => {
          console.log(response);
          this.getTCSECTasksJustification();
          this.hideModel();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    )
  }

  public onOpenModal(secTask: StdtsecTaskJustification, mode: string): void {

    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'edit') {
      console.log(secTask.taskId)
      this.tscsecRequest = secTask;
      this.itemId = this.tscsecRequest.taskData.requestNumber;
      button.setAttribute('data-target', '#uploadSPCJustification');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  @ViewChild('closeModal') private closeModal: ElementRef | undefined;
  public hideModel() {
    this.closeModal?.nativeElement.click();
  }

}

import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Subject} from "rxjs";
import {StdTCSecWorkPlan, StdWorkPlan} from "../../../../core/store/data/std/request_std.model";
import {FormBuilder} from "@angular/forms";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-std-tc-workplan',
  templateUrl: './std-tc-workplan.component.html',
  styleUrls: ['./std-tc-workplan.component.css']
})
export class StdTcWorkplanComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  p = 1;
  p2 = 1;
  public secTasks: StdTCSecWorkPlan[] = [];
  public tscsecRequest !: StdTCSecWorkPlan | undefined;

  constructor(
      private formBuilder: FormBuilder,
      private  standardDevelopmentService: StandardDevelopmentService
  ) {
  }

  ngOnInit(): void {
    this.getTCSECWorkPlan();

  }

  public getTCSECWorkPlan(): void {
    this.standardDevelopmentService.getTCSECWorkPlan().subscribe(
        (response: StdTCSecWorkPlan[]) => {
          console.log(response);
          this.secTasks = response;
          this.dtTrigger.next();

        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    )
  }

  public onOpenModal(secTask: StdTCSecWorkPlan, mode: string): void {

    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'edit') {
      console.log(secTask.taskId)
      this.tscsecRequest = secTask;
      button.setAttribute('data-target', '#updateNWIModal');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }


  uploadWorkPlan(stdWorkPlan: StdWorkPlan): void {

    console.log(stdWorkPlan);

    this.standardDevelopmentService.uploadWorkPlan(stdWorkPlan).subscribe(
        (response: StdWorkPlan) => {
          console.log(response);
          this.getTCSECWorkPlan();
          this.hideModel();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    )
  }

  @ViewChild('closeModal') private closeModal: ElementRef | undefined;
  public hideModel() {
    this.closeModal?.nativeElement.click();
  }



}

import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {HOFFeedback, StandardTasks, TaskData} from "../../../../core/store/data/std/request_std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";

@Component({
  selector: 'app-standard-task',
  templateUrl: './standard-task.component.html',
  styleUrls: ['./standard-task.component.css']
})
export class StandardTaskComponent implements OnInit {
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
  p = 1;
  p2 = 1;
  countLine = 0;
  tasks: StandardTasks[] = [];
  public actionRequest: StandardTasks | undefined;

  public hofFeedback: HOFFeedback | undefined;

  public technicalName ="";

  public taskData: TaskData | undefined;

  constructor(private standardDevelopmentService: StandardDevelopmentService) {
  }

  ngOnInit(): void {
    this.getHOFTasks();

  }

  @ViewChild('closeModal') private closeModal: ElementRef | undefined;
  public hideModel() {
    this.closeModal?.nativeElement.click();
  }

  public getHOFTasks(): void {
    this.standardDevelopmentService.getHOFTasks().subscribe(
        (response: StandardTasks[]) => {
          this.tasks = response;
          console.log(response)
          this.hideModel()
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }

  public  getTechnicalCommitteeName(id:number): void {
    this.standardDevelopmentService.getTechnicalCommitteeName(id).subscribe(
        (response: string) => {
          console.log(response)
          this.technicalName = response;
        },
        (error: HttpErrorResponse) => {
          //alert(error.message);
        }
    );
  }

  public onReviewTask(hofFeedback:HOFFeedback): void {
    console.log(hofFeedback);
    this.standardDevelopmentService.reviewTask(hofFeedback).subscribe(
        (response: HOFFeedback) => {
          console.log(response);
          this.getHOFTasks();
        },
        (error: HttpErrorResponse) => {
          //alert(error.message);
        }
    )
  }

  public onOpenModal(task: StandardTasks, mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    console.log(task.taskId)
    if (mode === 'edit') {
      this.actionRequest = task;
      button.setAttribute('data-target', '#updateRequestModal');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

}


import {Component, OnInit} from '@angular/core';
import {PublicReviewService} from "../public-review.service";
import {SPCSECTasks} from "../../../../shared/models/standard-development";
import {HttpErrorResponse} from "@angular/common/http";
import {New_work_item, PublicReviewDraft, TCTasks} from "../../../../shared/models/committee_module";
import {Observable} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-view-public-review',
  templateUrl: './view-public-review.component.html',
  styleUrls: ['./view-public-review.component.css']
})
export class ViewPublicReviewComponent implements OnInit {
  tasks: TCTasks[] = [];
  public actionRequest: TCTasks | undefined;
  newWorkItem: Observable<PublicReviewDraft[]> | undefined;
  p = 1;
  p2 = 1;

  constructor(private publicReviewService: PublicReviewService, private router: Router) {
  }

  ngOnInit(): void {
    this.getTCTasks();
    this.reloadData();


  }

  public getTCTasks(): void {
    this.publicReviewService.getTCTasks().subscribe(
      (response: TCTasks[]) => {
        this.tasks = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  reloadData() {
    this.newWorkItem = this.publicReviewService.getPublicReviewList();
  }

  public_review_details(id: number) {
    this.router.navigate(['preparePrComment', id]);
  }

  // public onOpenModal(task: TCTasks, mode: string): void {
  //   const container = document.getElementById('main-container');
  //   const button = document.createElement('button');
  //   button.type = 'button';
  //   button.style.display = 'none';
  //   button.setAttribute('data-toggle', 'modal');
  //   if (mode === 'approve') {
  //     this.actionRequest = task;
  //     button.setAttribute('data-target', '#approveModal');
  //   }
  //   if (mode === 'reject') {
  //     this.actionRequest = task;
  //     button.setAttribute('data-target', '#rejectModal');
  //   }
  //   // @ts-ignore
  //   container.appendChild(button);
  //   button.click();
  //
  // }

}

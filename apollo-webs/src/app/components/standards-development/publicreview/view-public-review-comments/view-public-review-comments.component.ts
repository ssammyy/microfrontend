import {Component, OnInit} from '@angular/core';
import {PublicReviewDraft, PublicReviewDraftComment, TCTasks} from "../../../../shared/models/committee_module";
import {Observable} from "rxjs";
import {PublicReviewService} from "../public-review.service";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-view-public-review-comments',
  templateUrl: './view-public-review-comments.component.html',
  styleUrls: ['./view-public-review-comments.component.css']
})
export class ViewPublicReviewCommentsComponent implements OnInit {
  // tasks: TCTasks[] = [];
  // public actionRequest: TCTasks | undefined;
  publicReviewDraftComment: Observable<PublicReviewDraftComment[]> | undefined;
  p = 1;
  p2 = 1;

  constructor(private publicReviewService: PublicReviewService, private router: Router) {
  }

  ngOnInit(): void {
    //this.getTCTasks();
    this.reloadData();

  }

  // public getTCTasks(): void {
  //   this.publicReviewService.getTCTasks().subscribe(
  //     (response: TCTasks[]) => {
  //       this.tasks = response;
  //     },
  //     (error: HttpErrorResponse) => {
  //       alert(error.message);
  //     }
  //   );
  // }

  reloadData() {
    this.publicReviewDraftComment = this.publicReviewService.getPublicReviewCommentList();
  }

  // public_review_details(id: number) {
  //   this.router.navigate(['preparePrComment', id]);
  // }


}

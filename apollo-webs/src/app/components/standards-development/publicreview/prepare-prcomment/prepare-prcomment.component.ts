import {Component, OnInit} from '@angular/core';
import {PublicReviewService} from "../public-review.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup} from "@angular/forms";
import {PublicReviewDraft, PublicReviewDraftComment} from "../../../../shared/models/committee_module";
import {HttpErrorResponse} from "@angular/common/http";
import {NotificationService} from "../../../../shared/services/notification.service";

@Component({
  selector: 'app-prepare-prcomment',
  templateUrl: './prepare-prcomment.component.html',
  styleUrls: ['./prepare-prcomment.component.css']
})
export class PreparePRCommentComponent implements OnInit {

  constructor(private publicReviewService: PublicReviewService, private router: Router, private formBuilder: FormBuilder, private route: ActivatedRoute, private toastr: NotificationService) {
  }

  public public_review_drafts !: PublicReviewDraft[];
  public_review_draft!: PublicReviewDraft;
  submitted = false;
  id: bigint | undefined;


  ngOnInit() {
    this.getPublicReviews();

    this.id = this.route.snapshot.params['id'];
    this.getPublicReview(this.id);


  }


  public onDecision(publicReviewDraftComment: PublicReviewDraftComment): void {
    this.publicReviewService.preparePublicReviewDraftComment(publicReviewDraftComment).subscribe(
      (response: PublicReviewDraftComment) => {
        this.showSuccess("Hello", "Wazzup");

        //this.gotoList();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }


  gotoList() {
    this.router.navigate(['/viewPr']);
  }

  public getPublicReviews(): void {
    this.publicReviewService.getPublicReviewList().subscribe(
      (response: PublicReviewDraft[]) => {
        this.public_review_drafts = response;
        console.log(response);

      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  getPublicReview(value: any): any {
    this.publicReviewService.getPublicReview(value).subscribe(
      (response: PublicReviewDraft) => {
        console.log(response);
        this.public_review_draft = response
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  showSuccess(title: string, message: string) {
    this.toastr.showSuccess(title, message);
  }
}

import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {StdReviewService} from "../../../../core/store/data/std/std-review.service";
import {ReviewForm} from "../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-review-standards',
  templateUrl: './review-standards.component.html',
  styleUrls: ['./review-standards.component.css']
})
export class ReviewStandardsComponent implements OnInit {
  public uploadedFiles: FileList;
  public isProposalFormGroup!: FormGroup;
  public stdReviewFormGroup!: FormGroup;
  constructor(
      private formBuilder: FormBuilder,
      private stdReviewService : StdReviewService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.stdReviewFormGroup = this.formBuilder.group({
      title:['', Validators.required],
      documentType:['', Validators.required],
      datePrepared:['', Validators.required],
      standardNumber:['', Validators.required]
    });
  }
  get formStdReview(): any{
    return this.stdReviewFormGroup.controls;
  }
  reviewStandard(): void {
    this.SpinnerService.show();
    this.stdReviewService.reviewStandard(this.stdReviewFormGroup.value).subscribe(
        (response: ReviewForm) => {
          this.SpinnerService.hide();
          console.log(response);
          this.stdReviewFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

}

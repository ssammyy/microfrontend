import {Component, OnInit} from '@angular/core';
import {PublicReviewService} from "../public-review.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {PublicReviewDraft} from "../../../../shared/models/committee_module";
import {HttpErrorResponse, HttpEventType, HttpResponse} from "@angular/common/http";
import {Router} from "@angular/router";
import {Observable} from "rxjs";

@Component({
  selector: 'app-prepare-public-review',
  templateUrl: './prepare-public-review.component.html',
  styleUrls: ['./prepare-public-review.component.css']
})
export class PreparePublicReviewComponent implements OnInit {
  selectedFiles?: FileList;
  currentFile?: File;
  progress = 0;
  message = '';
  fileInfos?: Observable<any>;
  fileName = null;

  constructor(private publicReviewService: PublicReviewService, private router: Router, private formBuilder: FormBuilder) {
  }

  public preparePublicReviewFormGroup!: FormGroup;
  submitted = false;


  ngOnInit(): void {
    this.preparePublicReviewFormGroup = this.formBuilder.group({
      prdName: [''],
      prdraftBy: [''],
      prdpath: [''],
    });
    this.fileInfos = this.publicReviewService.getFiles();

  }

  save(): void {
    this.publicReviewService
      .preparePublicReview(this.preparePublicReviewFormGroup.value).subscribe(
      (response: PublicReviewDraft) => {
        console.log(response);
        this.gotoList();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  onSubmit() {
    this.submitted = true;
    this.save();
  }

  gotoList() {
    this.router.navigate(['/viewPr']);
  }

  selectFile(event: any): void {
    this.selectedFiles = event.target.files;
    this.fileName = event.target.files[0].name;
  }

  upload(): void {
    this.progress = 0;

    if (this.selectedFiles) {
      const file: File | null = this.selectedFiles.item(0);

      if (file) {
        this.currentFile = file;

        this.publicReviewService.upload(this.currentFile).subscribe(
          (event: any) => {
            if (event.type === HttpEventType.UploadProgress) {
              this.progress = Math.round(100 * event.loaded / event.total);
            } else if (event instanceof HttpResponse) {
              this.message = event.body.message;
              this.fileInfos = this.publicReviewService.getFiles();
            }
          },
          (err: any) => {
            console.log(err);
            this.progress = 0;

            if (err.error && err.error.message) {
              this.message = err.error.message;
            } else {
              this.message = 'Could not upload the file!';
            }

            this.currentFile = undefined;
          });
      }

      this.selectedFiles = undefined;
    }
  }


}

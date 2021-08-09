import { Component, OnInit } from '@angular/core';
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ISAdoptionProposal} from "../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-is-proposal-form',
  templateUrl: './is-proposal-form.component.html',
  styleUrls: ['./is-proposal-form.component.css']
})
export class IsProposalFormComponent implements OnInit {
  public uploadedFiles: FileList;
  public isProposalFormGroup!: FormGroup;
  title = 'toaster-not';
  constructor(
      private formBuilder: FormBuilder,
      private stdIntStandardService : StdIntStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.isProposalFormGroup = this.formBuilder.group({
      proposal_doc_name: ['', Validators.required]
    });

  }
  get formISProposal(): any{
    return this.isProposalFormGroup.controls;
  }
  uploadProposal(): void {
    this.SpinnerService.show();
    this.stdIntStandardService.prepareAdoptionProposal(this.isProposalFormGroup.value).subscribe(
        (response: ISAdoptionProposal) => {
          console.log(response);
          this.SpinnerService.hide();
          this.isProposalFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

}

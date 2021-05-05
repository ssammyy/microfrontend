import {Component, OnInit} from '@angular/core';
import {Committee_Draft, Preliminary_Draft} from "../../../../shared/models/committee_module";
import {FormBuilder, FormGroup} from "@angular/forms";
import {CommitteeDraftService} from "../committee-draft.service";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";
import {PreliminaryDraftService} from "../preliminary-draft.service";

@Component({
  selector: 'app-prepare-cd',
  templateUrl: './prepare-cd.component.html',
  styleUrls: ['./prepare-cd.component.css']
})
export class PrepareCdComponent implements OnInit {

  committee_draft: Committee_Draft | undefined;
  submitted = false;
  public committee_drafts !: Committee_Draft[];
  public preliminary_drafts !: Preliminary_Draft[];

  public committee_draftFormGroup!: FormGroup;
  preliminary_draft: Preliminary_Draft | undefined;

  constructor(private committee_service: CommitteeDraftService, private preliminary_draft_service: PreliminaryDraftService, private router: Router, private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.getNWIS();
    this.getPDS();
    this.committee_draftFormGroup = this.formBuilder.group({
      nwiID: [''],
      pdID: [''],
      cdName: [''],
      cdBy: [''],
    });
  }

  // newPreliminary_draft(): void {
  //   this.submitted = false;
  //   this.preliminary_draft = new Preliminary_Draft();
  // }

  save(): void {
    this.committee_service
      .createCommitteeDraft(this.committee_draftFormGroup.value).subscribe(
      (response: Preliminary_Draft) => {
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
    this.router.navigate(['/committee_draft']);
  }

  public getNWIS(): void {
    this.preliminary_draft_service.getNWIS().subscribe(
      (response: Preliminary_Draft[]) => {
        this.preliminary_drafts = response;
        console.log(response);

      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
  public getPDS(): void {
    this.committee_service.getPDS().subscribe(
      (response: Committee_Draft[]) => {
        this.committee_drafts = response;
        console.log(response);

      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
}

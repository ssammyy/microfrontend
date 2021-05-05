import {Component, OnInit, Input} from '@angular/core';
import {PreliminaryDraftService} from "../preliminary-draft.service";
import {Preliminary_Draft} from "../../../../shared/models/committee_module";
import {Router} from '@angular/router';
import {HttpErrorResponse} from "@angular/common/http";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-prepare-pd',
  templateUrl: './prepare-pd.component.html',
  styleUrls: ['./prepare-pd.component.css']
})
export class PreparePdComponent implements OnInit {

  preliminary_draft: Preliminary_Draft | undefined;
  submitted = false;
  public preliminary_drafts !: Preliminary_Draft[];
  public preliminary_draftFormGroup!: FormGroup;


  constructor(private preliminary_draft_service: PreliminaryDraftService,
              private router: Router, private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.getNWIS();
    this.preliminary_draftFormGroup = this.formBuilder.group({
      nwiID: [''],
      pdName: [''],
      pdBy: [''],
    });
  }

  // newPreliminary_draft(): void {
  //   this.submitted = false;
  //   this.preliminary_draft = new Preliminary_Draft();
  // }

  save(): void {
    this.preliminary_draft_service
      .createPreliminaryDraft(this.preliminary_draftFormGroup.value).subscribe(
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
    this.router.navigate(['/preliminary_draft']);
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

}

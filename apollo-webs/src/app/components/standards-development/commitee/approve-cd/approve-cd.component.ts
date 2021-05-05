import { Component, OnInit } from '@angular/core';
import {Committee_Draft, New_work_item} from "../../../../shared/models/committee_module";
import {Observable} from "rxjs";
import {CommitteeDraftService} from "../committee-draft.service";
import {PreliminaryDraftService} from "../preliminary-draft.service";
import {Router} from "@angular/router";
import {FormBuilder} from "@angular/forms";
@Component({
  selector: 'app-approve-cd',
  templateUrl: './approve-cd.component.html',
  styleUrls: ['./approve-cd.component.css']
})
export class ApproveCdComponent implements OnInit {

  committee_draft: Observable<Committee_Draft[]> | undefined;
  public committee_drafts:Committee_Draft[]|undefined;
  public actionRequest:Committee_Draft|undefined;


  constructor(private committee_draft_service: CommitteeDraftService,  private router: Router, private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.reloadData();

  }
  public onApproveTask(committee_draft: Committee_Draft): void{

  }
  public onOpenModal(committee_draft: Committee_Draft, mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'edit') {
      this.actionRequest = committee_draft;
      button.setAttribute('data-target', '#updateRequestModal');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  reloadData() {
    this.committee_draft = this.committee_draft_service.getCommitteeDraftList();
  }

}

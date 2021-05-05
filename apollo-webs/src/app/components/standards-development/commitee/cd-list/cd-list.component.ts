import {Component, OnInit} from '@angular/core';
import {Committee_Draft} from "../../../../shared/models/committee_module";
import {Observable} from "rxjs";
import {CommitteeDraftService} from "../committee-draft.service";
import {PreliminaryDraftService} from "../preliminary-draft.service";
import {Router} from "@angular/router";
import {FormBuilder} from "@angular/forms";

@Component({
  selector: 'app-cd-list',
  templateUrl: './cd-list.component.html',
  styleUrls: ['./cd-list.component.css']
})
export class CdListComponent implements OnInit {
  committee_draft: Observable<Committee_Draft[]> | undefined;

  constructor(private committee_draft_service: CommitteeDraftService,  private router: Router, private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.reloadData();

  }

  reloadData() {
    this.committee_draft = this.committee_draft_service.getCommitteeDraftList();
  }

}

import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {PreliminaryDraftService} from "../preliminary-draft.service";
import {Preliminary_Draft} from "../../../../shared/models/committee_module";
import {Router} from '@angular/router';

@Component({
  selector: 'app-pd-list',
  templateUrl: './pd-list.component.html',
  styleUrls: ['./pd-list.component.css']
})
export class PdListComponent implements OnInit {

  preliminary_draft: Observable<Preliminary_Draft[]> | undefined;


  constructor(private preliminary_draft_service: PreliminaryDraftService,
              private router: Router) {
  }

  ngOnInit() {
    this.reloadData();
  }

  reloadData() {
    this.preliminary_draft = this.preliminary_draft_service.getPreliminaryDraftList();
  }

  deletePreliminaryDraft(id: number) {
    this.preliminary_draft_service.deletePreliminaryDraft(id)
      .subscribe(
        data => {
          console.log(data);
          this.reloadData();
        },
        error => console.log(error));
  }

  PreliminaryDraftDetails(id: number) {
    this.router.navigate(['details', id]);
  }
}

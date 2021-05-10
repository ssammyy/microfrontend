import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {PreliminaryDraftService} from "../preliminary-draft.service";
import {Committee_Draft} from "../../../../shared/models/committee_module";
import {Router} from '@angular/router';
import {CommitteeDraftService} from "../committee-draft.service";

@Component({
  selector: 'app-comment-list',
  templateUrl: './comment-cd.component.html',
  styleUrls: ['./comment-cd.component.css']
})
export class CommentCdComponent implements OnInit {

  committee_draft: Observable<Committee_Draft[]> | undefined;
  actionRequest: Committee_Draft | undefined;

  constructor(private committee_draft_service: CommitteeDraftService,
              private router: Router) {
  }

  ngOnInit() {
    this.reloadData();
  }

  reloadData() {
    this.committee_draft = this.committee_draft_service.getCommitteeDraftList();
  }

  public onOpenModal(committee_draft: Committee_Draft, mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'yes') {
      this.actionRequest = committee_draft;
      button.setAttribute('data-target', '#myModal');
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();
  }

  // deletePreliminaryDraft(id: number) {
  //   this.committee_draft_service.deletePreliminaryDraft(id)
  //     .subscribe(
  //       data => {
  //         console.log(data);
  //         this.reloadData();
  //       },
  //       error => console.log(error));
  // }

  PreliminaryDraftDetails(id: number) {
    this.router.navigate(['details', id]);
  }

  // public onReSubmit(notification: DepartmentResponse): void{
  //   this.standardDevelopmentService.deptResponse(notification).subscribe(
  //     (response: DepartmentResponse) => {
  //       console.log(response);
  //       this.getRequests();
  //     },
  //     (error: HttpErrorResponse) => {
  //       alert(error.message);
  //     }
  //   );
  // }
}

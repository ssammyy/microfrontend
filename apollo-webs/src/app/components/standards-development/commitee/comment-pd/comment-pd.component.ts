import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {Preliminary_Draft} from "../../../../shared/models/committee_module";
import {PreliminaryDraftService} from "../preliminary-draft.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-comment-pd',
  templateUrl: './comment-pd.component.html',
  styleUrls: ['./comment-pd.component.css']
})
export class CommentPdComponent implements OnInit {
  preliminary_draft: Observable<Preliminary_Draft[]> | undefined;
  actionRequest: Preliminary_Draft | undefined;

  constructor(private preliminary_draft_service: PreliminaryDraftService,
              private router: Router) {
  }

  ngOnInit() {
    this.reloadData();
  }

  reloadData() {
    this.preliminary_draft = this.preliminary_draft_service.getPreliminaryDraftList();
  }

  public onOpenModal(preliminary_draft: Preliminary_Draft, mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'yes') {
      this.actionRequest = preliminary_draft;
      button.setAttribute('data-target', '#myModal');
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();
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

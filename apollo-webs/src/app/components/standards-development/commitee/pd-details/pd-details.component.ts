import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {PreliminaryDraftService} from "../preliminary-draft.service";
import {Preliminary_Draft} from "../../../../shared/models/committee_module";
import {ActivatedRoute, Router} from '@angular/router'

@Component({
  selector: 'app-pd-details',
  templateUrl: './pd-details.component.html',
  styleUrls: ['./pd-details.component.css']
})
export class PdDetailsComponent implements OnInit {

  id: number | undefined;
  preliminaryDraft: Preliminary_Draft | undefined;

  constructor(private route: ActivatedRoute, private router: Router,
              private preliminaryDraftService: PreliminaryDraftService) {
  }

  ngOnInit(): void {
    //this.preliminaryDraft = new Preliminary_Draft();

    this.id = this.route.snapshot.params['id'];

    this.preliminaryDraftService.getPreliminaryDraft(this.id)
      .subscribe(data => {
        console.log(data)
        this.preliminaryDraft = data;
      }, error => console.log(error));
  }

  list() {
    this.router.navigate(['preliminary_draft']);
  }

}

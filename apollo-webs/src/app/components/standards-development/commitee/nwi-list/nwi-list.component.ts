import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {Router} from "@angular/router";
import {New_work_item} from "../../../../shared/models/committee_module";
import {NewWorkItemService} from "../new-work-item.service";

@Component({
  selector: 'app-nwi-list',
  templateUrl: './nwi-list.component.html',
  styleUrls: ['./nwi-list.component.css']
})
export class NwiListComponent implements OnInit {

  newWorkItem: Observable<New_work_item[]> | undefined;


  constructor(private newWorkItemService: NewWorkItemService,
              private router: Router) {
  }

  ngOnInit() {
    this.reloadData();
  }

  reloadData() {
    this.newWorkItem = this.newWorkItemService.getNWIList();
  }

  deleteNWI(id: number) {
    this.newWorkItemService.deleteNWI(id)
      .subscribe(
        data => {
          console.log(data);
          this.reloadData();
        },
        error => console.log(error));
  }

  NWIDetails(id: number) {
    this.router.navigate(['details', id]);
  }

}

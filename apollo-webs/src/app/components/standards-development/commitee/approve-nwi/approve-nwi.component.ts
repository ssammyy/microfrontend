import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {New_work_item} from "../../../../shared/models/committee_module";
import {NewWorkItemService} from "../new-work-item.service";
import {Router} from "@angular/router";
import {StandardTasks} from "../../../../shared/models/standard-development";
import {HttpErrorResponse} from "@angular/common/http";


@Component({
  selector: 'app-approve-nwi',
  templateUrl: './approve-nwi.component.html',
  styleUrls: ['./approve-nwi.component.css']
})
export class ApproveNwiComponent implements OnInit {

  newWorkItem: Observable<New_work_item[]> | undefined;
  public newWorkItems: New_work_item[] | undefined;
  public actionRequest: New_work_item | undefined;


  constructor(private newWorkItemService: NewWorkItemService,
              private router: Router) {
  }

  ngOnInit() {
    this.reloadData();
  }

  reloadData() {
    this.newWorkItem = this.newWorkItemService.getNWIList();
  }
  public onApproveTask(newWorkItem: New_work_item): void{

  }


  public onOpenModal(newWorkItem: New_work_item, mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'edit') {
      this.actionRequest = newWorkItem;
      button.setAttribute('data-target', '#updateRequestModal');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

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

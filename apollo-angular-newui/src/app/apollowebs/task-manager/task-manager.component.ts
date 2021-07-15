import { Component, OnInit } from '@angular/core';
import {PermitEntityDto, TaskDto} from "../../core/store/data/qa/qa.model";
import {QaService} from "../../core/store/data/qa/qa.service";
import {Router} from "@angular/router";

declare interface DataTable {
  headerRow: string[];
  footerRow: string[];
  dataRows: string[][];
}

declare const $: any;

@Component({
  selector: 'app-task-manager',
  templateUrl: './task-manager.component.html',
  styleUrls: ['./task-manager.component.css']
})
export class TaskManagerComponent implements OnInit {
  public dataTable: DataTable;
  public allTaskData: TaskDto[];
  constructor(private qaService: QaService,
              private router: Router,) { }

  ngOnInit() {
    let formattedArray = [];
    this.dataTable;

    this.qaService.taskListFind().subscribe(
        (data:any) => {

          this.allTaskData = data;
          // tslint:disable-next-line:max-line-length
          formattedArray = data.map(i => [i.taskName, i.taskCreateTime, i.permitRefNo]);

          this.dataTable = {
            headerRow: ['Task Name', 'Task Created Time', 'Permit Reference Number', 'Actions'],
            footerRow: ['Task Name', 'Task Created Time', 'Permit Reference Number', 'Actions'],
            dataRows: formattedArray


            // ['REFFM#202107095913D', 'Andrew Mike', '09/07/2021', 'Dassani', 'Water', '']

          }

        });


    // this.dataTable = {
    //   headerRow: ['Permit Ref No', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Status', 'Actions'],
    //   footerRow: ['Permit Ref No', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Status', 'Actions'],
    //   dataRows: []
    //
    //
    //   // ['REFFM#202107095913D', 'Andrew Mike', '09/07/2021', 'Dassani', 'Water', '']
    //
    // }
    // console.log(this.dataTable);
    // this.allPermitData = this.Object.json().results;
    // console.log(formattedArray);


    //
  }
  ngAfterViewInit() {
    $('#datatables').DataTable({
      'pagingType': 'full_numbers',
      'lengthMenu': [
        [10, 25, 50, -1],
        [10, 25, 50, 'All']
      ],
      responsive: true,
      language: {
        search: '_INPUT_',
        searchPlaceholder: 'Search records',
      }
    });
    let table: any;
    table = $(`#datatables`).DataTable();

  }

  onSelect(rowElement: string) {
    this.router.navigate(['/permitdetails'], {fragment: rowElement});
  }
}

import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-other-version-details',
  templateUrl: './other-version-details.component.html',
  styleUrls: ['./other-version-details.component.css']
})
export class OtherVersionDetailsComponent implements OnInit {
  public settings = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    actions: {
      columnTitle: 'Actions',
      add: false,
      edit: false,
      delete: false,
      custom: [
      //  { name: 'editRecord', title: '<i class="btn btn-sm btn-primary">View More</i>' },
         { name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View More</i>' }
      ],
      position: 'right' // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true
    },
    noDataMessage: 'No data found',
    columns: {
     
      ucr_no: {
        title: 'UCR No',
        type: 'string'
      },
      version_no: {
        title: 'Version Number',
        type: 'string'
      },
    },
    pager: {
      display: true,
      perPage: 20
    }
  };
  dataSet: any = [{
    ucr_no: 'UCR9922', version_no: 'VN002323'
  }];
  constructor() { }

  ngOnInit(): void {
  }

}


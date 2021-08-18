import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-item-details-list-view',
  templateUrl: './item-details-list-view.component.html',
  styleUrls: ['./item-details-list-view.component.css']
})
export class ItemDetailsListViewComponent implements OnInit {
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
      number: {
        title: 'Number',
        type: 'string'
      },
      ucr_no: {
        title: 'UCR No',
        type: 'string'
      },
      description: {
        title: 'Description',
        type: 'string'
      },
      hs_code: {
        title: 'HS Code',
        type: 'string'
      },
      quantity: {
        title: 'Quantity',
        type: 'string'
      },
      unit_quantity: {
        title: 'Unit Quantity',
        type: 'string'
      },
      country_of_origin: {
        title: 'Country of Origin',
        type: 'string'
      },
      inspection_status: {
        title: 'Inspection Status',
        type: 'string'
      }
    },
    pager: {
      display: true,
      perPage: 20
    }
  };
  dataSet: any = [{
    number: 1002, description: 'Some Description', hs_code: 'HS Code', quantity: '20', unit_quantity: 'UN',
    country_of_origin: 'Zambia', inspection_status: 'YES'
  }];
  constructor() { }

  ngOnInit(): void {
  }

}

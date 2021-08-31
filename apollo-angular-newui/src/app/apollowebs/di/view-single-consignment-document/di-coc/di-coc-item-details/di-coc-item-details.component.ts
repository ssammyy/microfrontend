import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-di-coc-item-details',
  templateUrl: './di-coc-item-details.component.html',
  styleUrls: ['./di-coc-item-details.component.css']
})
export class DiCocItemDetailsComponent implements OnInit {

  public settings = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    actions: false,
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true
    },
    noDataMessage: 'No data found',
    columns: {
      number: {
        title: 'NUMBER',
        type: 'string'
      },
      hsCode: {
        title: 'HS CODE',
        type: 'string'
      },
      vin: {
        title: 'VIN',
        type: 'string'
      },
      stickerNumber: {
        title: 'STICKER NUMBER',
        type: 'string'
      },
      ics: {
        title: 'ICS',
        type: 'string'
      },
      standardsRef: {
        title: 'Standards Ref',
        type: 'string'
      },
      licenseRef: {
        title: 'License Ref',
        type: 'string'
      },
      registration: {
        title: 'Registration',
        type: 'string'
      },
      brandName: {
        title: 'Brand Name',
        type: 'string'
      }
    },
    pager: {
      display: true,
      perPage: 20
    }
  };
  dataSet: any = [{
    number: 1002, hsCode: 'cc03902', vin: 'zm', stickerNumber: '20',
    ics: 1002, standardsRef: 'cc03902', licenseRef: 'zm', registration: '20',
  brandName: '20'
  }];
  constructor() { }

  ngOnInit(): void {
  }
  onCustomAction(data: any){

  }
}

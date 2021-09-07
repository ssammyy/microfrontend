import {Component, Input, OnInit} from '@angular/core';

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
      shipmentLineNumber: {
        title: 'NUMBER',
        type: 'string'
      },
      cocNumber: {
        title: 'COC NUMBER',
        type: 'string'
      },
      shipmentLineHscode: {
        title: 'HS CODE',
        type: 'string'
      },
      shipmentLineDescription: {
        title: 'DESCRIPTION',
        type: 'string'
      },
      shipmentLineVin: {
        title: 'VIN',
        type: 'string'
      },
      shipmentLineStickerNumber: {
        title: 'STICKER NUMBER',
        type: 'string'
      },
      shipmentLineIcs: {
        title: 'ICS',
        type: 'string'
      },
      shipmentLineStandardsReference: {
        title: 'Standards Ref',
        type: 'string'
      },
      shipmentLineLicenceReference: {
        title: 'License Ref',
        type: 'string'
      },
      shipmentLineRegistration: {
        title: 'Registration',
        type: 'string'
      },
      shipmentLineBrandName: {
        title: 'Brand Name',
        type: 'string'
      }
    },
    pager: {
      display: true,
      perPage: 20
    }
  };
  @Input()dataSet: any[]
  constructor() { }

  ngOnInit(): void {
  }
  onCustomAction(data: any){
  }
}

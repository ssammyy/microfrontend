import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-view-declaration-documents-item-details-list',
  templateUrl: './view-declaration-documents-item-details-list.component.html',
  styleUrls: ['./view-declaration-documents-item-details-list.component.css']
})
export class ViewDeclarationDocumentsItemDetailsListComponent implements OnInit {

  public settings = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    actions: {
      columnTitle: 'Actions',
      add: false,
      edit: false,
      delete: false,
      custom: false,
      position: 'right' // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true
    },
    noDataMessage: 'No data found',
    columns: {
      itemNum: {
        title: 'ITEM NUM',
        type: 'string'
      },
      commodityCode: {
        title: 'COMMODITY CODE',
        type: 'string'
      },
      originCountry: {
        title: 'ORIGIN COUNTRY',
        type: 'string'
      },
      itemPrice: {
        title: 'ITEM PRICE',
        type: 'string'
      },
      tariffGoodsDesc: {
        title: 'TARIFF GOOD DESCRIPTION',
        type: 'string',
        hide: true
      },
      commercialGoods: {
        title: 'COMMERCIAL GOODS',
        type: 'string',
        hide: true
      }
    },
    pager: {
      display: true,
      perPage: 20
    }
  };
  @Input() dataSet: any = [];
  constructor() { }

  ngOnInit(): void {
  }

}

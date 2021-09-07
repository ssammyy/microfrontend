import {Component, Input, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import {act} from "@ngrx/effects";

@Component({
  selector: 'app-demand-note-list',
  templateUrl: './demand-note-list.component.html',
  styleUrls: ['./demand-note-list.component.css']
})
export class DemandNoteListComponent implements OnInit {

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
        {name: 'download', title: '<i class="btn btn-sm btn-primary">Download</i>'}
      ],
      position: 'right' // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true
    },
    noDataMessage: 'No data found',
    columns: {
      demandNoteNumber: {
        title: 'DEMAND NOTE NUMBER',
        type: 'string'
      },
      dateGenerated: {
        title: 'DATE GENERATED',
        type: 'string'
      },
      nameImporter: {
        title: 'NAME IMPORTER',
        type: 'string'
      },
      telephone: {
        title: 'TELEPHONE',
        type: 'string'
      },
      product: {
        title: 'PRODUCT',
        type: 'string'
      },
      cfvalue: {
        title: 'CF VALUE',
        type: 'string'
      },
      amountPayable: {
        title: 'AMOUNT PAYABLE',
        type:'string'
      },
      totalAmount: {
        title: 'TOTAL AMOUNT',
        type: 'string'
      },
      rate: {
        title: 'RATE',
        type: 'string'
      },
    },
    pager: {
      display: true,
      perPage: 20
    }
  };
  @Input() dataSet: any = [];
  @Input() cdId: any[]
  constructor(private diService: DestinationInspectionService) { }

  ngOnInit(): void {
  }

  onCustomAction(action: any){
      // let d=341
      this.diService.downloadDocument("/api/v1/download/demand/note/"+action.data.id)
  }

}

import {Component, Input, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import * as fileSaver from 'file-saver';
@Component({
  selector: 'app-attachment-list',
  templateUrl: './attachment-list.component.html',
  styleUrls: ['./attachment-list.component.css']
})
export class AttachmentListComponent implements OnInit {
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
      name: {
        title: 'File Name',
        type: 'string'
      },
      description: {
        title: 'Description',
        type: 'string'
      },
      documentType: {
        title: 'Document Type',
        type: 'string'
      },
      fileSize: {
        title: 'File Size',
        type: 'string'
      },
    },
    pager: {
      display: true,
      perPage: 20
    }
  };
  @Input() dataSet: any = [];
  constructor(private diService: DestinationInspectionService) { }

  ngOnInit(): void {
  }

  onCustomAction(event){
    switch (event.action) {
      case "download":
        this.diService.downloadDocument("/api/v1/download/attachments/"+event.data.id)
        break
      default:
        console.log("Unsupported action: "+event.action)
    }
  }

}

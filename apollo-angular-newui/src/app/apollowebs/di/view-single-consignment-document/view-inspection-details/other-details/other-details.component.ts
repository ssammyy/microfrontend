import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-other-details',
  templateUrl: './other-details.component.html',
  styleUrls: ['./other-details.component.css']
})
export class OtherDetailsComponent implements OnInit {
  @Input() inspectionDetails: any
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
        // {name: 'requestMinistryChecklist', title: '<i class="btn btn-sm btn-primary">MINISTRY CHECKLIST</i>'},
        // {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View More</i>'}
      ],
      position: 'right' // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true
    },
    noDataMessage: 'No data found',
    columns: {
      id: {
        title: '#',
        type: 'string'
      },
      brand: {
        title: 'Brand',
        type: 'string'
      },
      category: {
        title: 'Item Category',
        type: 'string'
      },
      compliant: {
        title: 'Compliant',
        type: 'string'
      },
      description: {
        title: 'Description',
        type: 'string'
      },
      sampled: {
        title: 'Sampled',
        type: 'string'
      },
      inspectionDate: {
        title: 'Inspection Date',
        type: 'date'
      },
      sampleUpdated: {
        title: 'Sample Updated',
        type: 'string'
      }
    },
    pager: {
      display: true,
      perPage: 20
    }
  };
  constructor() { }

  ngOnInit(): void {
  }

}

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-consignment-document-list',
  templateUrl: './consignment-document-list.component.html',
  styleUrls: ['./consignment-document-list.component.css']
})
export class ConsignmentDocumentListComponent implements OnInit {
  activeStatus: string = 'ongoing';
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
      id: {
        title: 'ID',
        type: 'string'
      },
      ucr_no: {
        title: 'UCR No',
        type: 'string'
      },
      applicant_name: {
        title: 'Applicant Name',
        type: 'string'
      },
      application_date: {
        title: 'Application Date',
        type: 'string'
      },
      approval_date: {
        title: 'Approval Date',
        type: 'string'
      },
      approval_status: {
        title: 'Approval Status',
        type: 'string'
      },
      assigned_officer: {
        title: 'Assigned Officer',
        type: 'string'
      },
      cd_type: {
        title: 'CD Type',
        type: 'string'
      }
    },
    pager: {
      display: true,
      perPage: 20
    }
  };
  dataSet: any = [{
    id: 1002, ucr_no: 'TY993', applicant_name: 'David Njuguna', application_date: '2021-08-16', approval_date: '2021-08-16',
    approval_status: 'Approved', assigned_officer: 'John Mungai', cd_type: 'Imported Vehicles without CoR'
  },
  {
    id: 1002, ucr_no: 'TY993', applicant_name: 'David Njuguna', application_date: '2021-08-16', approval_date: '2021-08-16',
    approval_status: 'Approved', assigned_officer: 'John Mungai', cd_type: 'Imported Vehicles without CoR'
  },
  {
    id: 1002, ucr_no: 'TY993', applicant_name: 'David Njuguna', application_date: '2021-08-16', approval_date: '2021-08-16',
    approval_status: 'Approved', assigned_officer: 'John Mungai', cd_type: 'Imported Vehicles without CoR'
  }];
  constructor(private router: Router) { }

  ngOnInit(): void {
    this.loadData();
  }
  private loadData(): any {
    const model = {
    };
    // this._httpService.post('', model).subscribe(
    //   result => {
    //     this.dataSet = result.list;
    //   }
    // );
  }
  public onCustomAction(event: any): void {
    switch (event.action) {
      case 'viewRecord':
        this.viewRecord(event.data);
        break;
    }
  }
  private viewRecord(data: any) {
    this.router.navigate([`/di`, data.id]);
  }
  toggleStatus(status: string): void {
    this.activeStatus = status;
  }
}

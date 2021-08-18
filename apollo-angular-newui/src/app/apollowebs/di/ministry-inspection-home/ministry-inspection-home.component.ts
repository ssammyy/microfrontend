import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-ministry-inspection-home',
  templateUrl: './ministry-inspection-home.component.html',
  styleUrls: ['./ministry-inspection-home.component.css']
})
export class MinistryInspectionHomeComponent implements OnInit {
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
        title: '#',
        type: 'string'
      },
      ucr_no: {
        title: 'UCR No',
        type: 'string'
      },
      chasis_no: {
        title: 'Chasis Number',
        type: 'string'
      },
      used_indicator: {
        title: 'Used Indicator',
        type: 'string'
      },
      vehicle_year: {
        title: 'Vehicle Year',
        type: 'string'
      },
      vehicle_model: {
        title: 'Vehicle Model',
        type: 'string'
      },
      vehicle_make: {
        title: 'Vehicle Make',
        type: 'string'
      }
    },
    pager: {
      display: true,
      perPage: 20
    }
  };
  dataSet: any = [{
    id: 1002, ucr_no: 'UCR993', chasis_no: 'CHASS976543212', used_indicator: '', vehicle_year: '2021',
    vehicle_model: 'ISIS', vehicle_make: 'TOYOTA'
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
    this.router.navigate([`/ministry-inspection-home`, data.id]);
  }
  toggleStatus(status: string): void {
    this.activeStatus = status;
  }
}


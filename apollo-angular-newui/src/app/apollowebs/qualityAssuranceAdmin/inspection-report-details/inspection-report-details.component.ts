import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-inspection-report-details',
  templateUrl: './inspection-report-details.component.html',
  styleUrls: ['./inspection-report-details.component.css']
})
export class InspectionReportDetailsComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }
  id:any='';
  accordion(ids:any){
    if(this.id == ids){
      this.id= '';
    }
    else{
      this.id=ids;
    }
  }
}

import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-new-inspection-report',
  templateUrl: './new-inspection-report.component.html',
  styleUrls: ['./new-inspection-report.component.css']
})
export class NewInspectionReportComponent implements OnInit {

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

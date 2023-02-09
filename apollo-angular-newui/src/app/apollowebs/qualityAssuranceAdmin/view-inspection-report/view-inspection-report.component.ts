import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-new-inspection-report',
  templateUrl: './view-inspection-report.component.html',
  styleUrls: ['./view-inspection-report.component.css']
})
export class ViewInspectionReportComponent implements OnInit {
  currDivLabel!: string;
  currDiv!: string;

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
  openModalAddDetails(divVal: string): void {

    const arrHead = ['addTechnical', 'permitCompleteness', 'assignOfficer', 'addStandardsDetails', 'scheduleInspectionDate'];

    const arrHeadSave = ['Add Technical: Quality Procedures,Standards And Management Systems', 'Is The Permit Complete', 'Select An officer', 'Add Standard details', 'Set The Date of Inspection'];

    for (let h = 0; h < arrHead.length; h++) {
      if (divVal === arrHead[h]) {
        this.currDivLabel = arrHeadSave[h];
      }
    }

    this.currDiv = divVal;
  }

}

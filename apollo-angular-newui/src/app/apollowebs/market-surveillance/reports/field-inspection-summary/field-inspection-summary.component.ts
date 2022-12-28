import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-field-inspection-summary',
  templateUrl: './field-inspection-summary.component.html',
  styleUrls: ['./field-inspection-summary.component.css']
})
export class FieldInspectionSummaryComponent implements OnInit {

  activeStatus = 'field-inspection-summary';
  constructor() { }

  ngOnInit(): void {
  }

  toggle(statusField: string) {
    if (statusField != this.activeStatus){
      this.activeStatus = statusField;
    }

  }
}

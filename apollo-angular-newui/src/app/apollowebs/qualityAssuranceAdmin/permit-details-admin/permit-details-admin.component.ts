import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-permit-details-admin',
  templateUrl: './permit-details-admin.component.html',
  styleUrls: ['./permit-details-admin.component.css']
})
export class PermitDetailsAdminComponent implements OnInit {

  constructor() { }

  steps:any;
  // stepSoFar: | undefined;
  ngOnInit(): void {
    this.steps = 1;
  }

  id:any ="1";
  tabChange(ids:any){
    this.id=ids;
    console.log(this.id);
  }

  nextStep(currentStep){
    this.steps = currentStep += 1;
  }
  previousStep(currentStep){
    this.steps = currentStep -= 1;
  }

  newinspectionform(NewInspectionForm){
    console.log("Form is submitted")

  }

}

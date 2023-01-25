import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-permit-details',
  templateUrl: './permit-details.component.html',
  styleUrls: ['./permit-details.component.css']
})
export class PermitDetailsComponent implements OnInit {

  steps:any;
  // stepSoFar: | undefined;
  
  constructor() { }

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

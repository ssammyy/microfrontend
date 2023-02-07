import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-inspection-report-inspection-tesing',
  templateUrl: './inspection-report.html',
  styleUrls: ['./inspection-report.css']
})
export class InspectionReport implements OnInit {
  stepSoFar: | undefined;
  step = 1;

  constructor() { }

  ngOnInit(): void {
  }
  onClickPrevious() {
    if (this.step > 1) {
      this.step = this.step - 1;
    } else {
      this.step = 1;
    }
  }

  clickNextToTest()
  {
    this.step += 1;

  }

  onClickNext(valid: boolean) {
    if (valid) {
      switch (this.step) {
        case 1:
         // this.stepSoFar = {...this.sta1Form?.value};
          break;
        case 2:
       //   this.stepSoFar = {...this.sta10Form?.value};
          break;
        case 3:
       //   this.stepSoFar = {...this.sta10FormA?.value};
          break;
        case 4:
      //    this.stepSoFar = {...this.sta10FormB?.value};
          break;
        case 5:
        //  this.stepSoFar = {...this.sta10FormC?.value};
          break;
        case 6:
         // this.stepSoFar = {...this.sta10FormD?.value};
          break;
        case 7:
     //     this.stepSoFar = {...this.sta10FormE?.value};
          break;
        case 8:
       //   this.stepSoFar = {...this.sta10FormF?.value};
          break;
        case 9:
     //     this.stepSoFar = {...this.sta1Form?.value};
          break;
      }
      this.step += 1;
      //(`Clicked and step = ${this.step}`);
    }
  }


}

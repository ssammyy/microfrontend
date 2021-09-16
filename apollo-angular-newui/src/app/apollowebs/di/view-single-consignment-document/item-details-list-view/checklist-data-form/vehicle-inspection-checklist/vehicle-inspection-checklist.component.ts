import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-vehicle-inspection-checklist',
  templateUrl: './vehicle-inspection-checklist.component.html',
  styleUrls: ['./vehicle-inspection-checklist.component.css']
})
export class VehicleInspectionChecklistComponent implements OnInit {
  @Output() private vehicleDetails = new EventEmitter<any>();
  motorVehicleChecklist: FormGroup

  constructor(private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.motorVehicleChecklist = this.fb.group({
      remarks: ['',Validators.required]
    })
    this.motorVehicleChecklist.statusChanges
        .subscribe(
            res=>{
              if(this.motorVehicleChecklist.valid) {
                this.vehicleDetails.emit(this.motorVehicleChecklist.value)
              } else {
                this.vehicleDetails.emit(null)
              }
            }
        )
  }

}

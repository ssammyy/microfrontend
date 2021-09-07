import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-agrochem-inspection-checklist',
  templateUrl: './agrochem-inspection-checklist.component.html',
  styleUrls: ['./agrochem-inspection-checklist.component.css']
})
export class AgrochemInspectionChecklistComponent implements OnInit {
  @Output() private agrochemDetails = new EventEmitter<any>();
  agrochemChecklist: FormGroup

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
    this.agrochemChecklist=this.fb.group({
      serialNumber: ['', [Validators.required,Validators.maxLength(256)]],
      brand: ['',Validators.maxLength(256)],
      ksEasApplicable: ['',Validators.maxLength(256)],
      quantityVerified: ['',Validators.maxLength(256)],
      dateMfgPackaging: ['', Validators.maxLength(256)],
      dateExpiry: ['',Validators.maxLength(256)],
      mfgName: ['',Validators.maxLength(256)],
      mfgAddress: ['',Validators.maxLength(256)],
      compositionIngredients:['',Validators.maxLength(256)],
      storageCondition: ['', Validators.maxLength(256)],
      appearance: ['',Validators.maxLength(256)],
      certMarksPvocDoc: ['', Validators.maxLength(256)],
      sampled: ['', Validators.required],
      remarks: ['',Validators.required]
    })
    this.agrochemChecklist.valueChanges
        .subscribe(
            res=>{
              if(this.agrochemChecklist.valid){
                console.log("valid value changes")
                this.agrochemDetails.emit(this.agrochemChecklist.value)
              } else {
                this.agrochemDetails.emit(null)
              }
            }
        )
  }

}

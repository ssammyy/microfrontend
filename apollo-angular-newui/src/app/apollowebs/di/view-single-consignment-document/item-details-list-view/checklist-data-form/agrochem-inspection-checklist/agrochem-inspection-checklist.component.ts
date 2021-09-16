import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {ItemChecklistComponent} from "./item-checklist/item-checklist.component";

@Component({
  selector: 'app-agrochem-inspection-checklist',
  templateUrl: './agrochem-inspection-checklist.component.html',
  styleUrls: ['./agrochem-inspection-checklist.component.css']
})
export class AgrochemInspectionChecklistComponent implements OnInit {
  @Output() private agrochemDetails = new EventEmitter<any>();
  agrochemChecklist: FormGroup
  constructor(private fb: FormBuilder, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.agrochemChecklist=this.fb.group({
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

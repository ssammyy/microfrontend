import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
  selector: 'app-target-item',
  templateUrl: './target-item.component.html',
  styleUrls: ['./target-item.component.css']
})
export class TargetItemComponent implements OnInit {
  message: any;
  public form: FormGroup;
  constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
              private diService: DestinationInspectionService) {
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      remarks: ['', Validators.required]
    })
  }
 
  saveRecord() {
    this.diService.sendConsignmentDocumentAction(this.form.value,this.data.uuid,"target")
        .subscribe(
            res=>{
              if(res.responseCode==="00"){
                this.dialogRef.close(true)
              } else {
                this.message=res.message
              }
            }
        )
  }
}


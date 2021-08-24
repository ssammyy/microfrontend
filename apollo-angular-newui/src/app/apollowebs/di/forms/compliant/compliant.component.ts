import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
  selector: 'app-compliant',
  templateUrl: './compliant.component.html',
  styleUrls: ['./compliant.component.css']
})
export class CompliantComponent implements OnInit {

  public form: FormGroup;
  message: any
  constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
              private diService: DestinationInspectionService) {
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      compliantStatus: ['', Validators.required],
      remarks: ['', Validators.required]
    })
  }
 
  saveRecord() {
    this.diService.sendConsignmentDocumentAction(this.form.value, this.data.uuid, "compliant-vehicle")
        .subscribe(
            res => {
              if (res.responseCode === "00") {
                this.dialogRef.close(this.form.value)
              } else {
                this.message = res.message
              }
            }
        )
  }
}

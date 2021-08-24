import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
  selector: 'app-target-supervisor',
  templateUrl: './target-supervisor.component.html',
  styleUrls: ['./target-supervisor.component.css']
})
export class TargetSupervisorComponent implements OnInit {

  public form: FormGroup;
  public message: any;
  constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
              private diService: DestinationInspectionService) {
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      inspectionNotificationStatus: ['', Validators.required],
      remarks: ['', Validators.required]
    })
  }

  saveRecord() {
    this.diService.sendConsignmentDocumentAction(this.form.value,this.data.uuid,"target-supervisor")
        .subscribe(
            res=>{
              if(res.responseCode==="00"){
                this.dialogRef.close(this.form.value)
              }else{
                this.message=res.message
              }
            }
        )

  }
}
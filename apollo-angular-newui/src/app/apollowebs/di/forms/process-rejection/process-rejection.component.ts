import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
  selector: 'app-process-rejection',
  templateUrl: './process-rejection.component.html',
  styleUrls: ['./process-rejection.component.css']
})
export class ProcessRejectionComponent implements OnInit {
  message: any
    title: any
  public form: FormGroup;
  constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
              private diService: DestinationInspectionService) {
  }

  ngOnInit(): void {
      this.title=this.data.title
    this.form = this.fb.group({
      approvalStatus: ['', Validators.required],
      remarks: ['', Validators.required]
    })
  }
 
  saveRecord() {
    this.diService.sendConsignmentDocumentAction(this.form.value,this.data.cdUuid,"process/approve-reject/"+this.data.taskId)
        .subscribe(
            res=>{
              if(res.responseCode=="00"){
                this.dialogRef.close(true)
              }else {
                this.message=res.message
              }
            }
        )

  }
}
import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";

@Component({
  selector: 'app-process-rejection',
  templateUrl: './process-rejection.component.html',
  styleUrls: ['./process-rejection.component.css']
})
export class ProcessRejectionComponent implements OnInit {

  public form: FormGroup;
  constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      processRejectionStatus: ['', Validators.required],
      processRejectionDate: ['', Validators.required],
      processRejectionRemarks: ['', Validators.required]
    })
  }
 
  saveRecord() {
    this.dialogRef.close(this.form.value)
  }
}
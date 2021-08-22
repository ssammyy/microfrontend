import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-approve-reject-consignment',
  templateUrl: './approve-reject-consignment.component.html',
  styleUrls: ['./approve-reject-consignment.component.css']
})
export class ApproveRejectConsignmentComponent implements OnInit {
  public statuses: any[]
  public form: FormGroup
  constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder,@Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit(): void {
    console.log(this.data)
    this.statuses=this.data.configurations.CDStatusTypes
      this.form=this.fb.group({
        applicable_status: ['', Validators.required],
          productDescription: ['', Validators.required],
          comments: ['', Validators.required]
      })
  }
  saveRecord() {
      this.dialogRef.close(this.form.value)
  }
}
